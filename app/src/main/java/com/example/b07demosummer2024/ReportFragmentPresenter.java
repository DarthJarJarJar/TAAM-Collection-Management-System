package com.example.b07demosummer2024;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportFragmentPresenter {
    ReportFragmentView view;
    ReportFragmentModel model;

    public ReportFragmentPresenter(ReportFragmentView view, ReportFragmentModel model) {
        this.view = view;
        this.model = model;
    }

    void makePdf() {
        String reportType = view.reportTypeSpinner.getSelectedItem().toString();
        List<Item> allItems = RecyclerViewStaticFragment.getItems();
        List<Item> reportList = new ArrayList<Item>();
        String reportTitle = "Report_";
        boolean imageAndDescriptionOnly = view.descriptionAndImageOnlyCheckbox.isChecked();

        switch (reportType) {
            case "Lot Number":
                int lotNumber = Integer.parseInt(view.editTextReportParameter.getText().toString());

                for (Item item: allItems) {
                    if (item.getId() == lotNumber) {
                        reportList.add(item);
                    }
                }

                reportTitle += "Lot_Number_" + lotNumber;
                break;
            case "Name":
                String name = view.editTextReportParameter.getText().toString();

                for (Item item: allItems) {
                    if (item.getTitle().equals(name)) {
                        reportList.add(item);
                    }
                }
                reportTitle += "Name_" + name;
                break;
            case "Category":
                String category = view.detailSpinner.getSelectedItem().toString();

                for (Item item: allItems) {
                    if (item.getCategory().equals(category)) {
                        reportList.add(item);
                    }
                }
                reportTitle += "Category_" + category;
                break;
            case "Period":
                String period = view.detailSpinner.getSelectedItem().toString();

                for (Item item: allItems) {
                    if (item.getPeriod().equals(period)) {
                        reportList.add(item);
                    }
                }
                reportTitle += "Period_" + period;
                break;
            case "All Items":
                reportList = allItems;
                reportTitle += "All_Items";
                break;
            default:
                break;
        }

        if (imageAndDescriptionOnly) {
            reportTitle += "_imageAndDescOnly";
        }
        reportTitle += "_" + System.currentTimeMillis() + ".pdf";
        view.progressBar.setVisibility(View.VISIBLE);

        generatePdf(reportList, imageAndDescriptionOnly, reportTitle);

    }

    private void generatePdf(List<Item> itemList, boolean imageAndDescriptionOnly, String reportTitle) {
        PdfDocument document = new PdfDocument();
        loadImageAndGeneratePage(document, itemList, 0, imageAndDescriptionOnly, reportTitle);
    }

    private void loadImageAndGeneratePage(PdfDocument document, List<Item> itemList, int index, boolean imageAndDescriptionOnly, String reportTitle) {
        if (index >= itemList.size()) {
            // All pages generated, write document to file
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(downloadsDir, reportTitle);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                document.writeTo(fos);
                document.close();
                Toast.makeText(view.getContext(), "Report saved to downloads", Toast.LENGTH_SHORT).show();
                view.progressBar.setVisibility(View.GONE);
                view.reportTypeSpinner.setSelection(0);
                view.detailSpinner.setSelection(0);
                view.editTextReportParameter.setText("");
                view.descriptionAndImageOnlyCheckbox.setChecked(false);

            } catch (IOException e) {
                Log.e("ReportFragment", "Error writing PDF", e);
                Toast.makeText(view.getContext(), "Failed to generate PDF", Toast.LENGTH_SHORT).show();
                view.progressBar.setVisibility(View.GONE);
                view.reportTypeSpinner.setSelection(0);
                view.detailSpinner.setSelection(0);
                view.editTextReportParameter.setText("");
                view.descriptionAndImageOnlyCheckbox.setChecked(false);
            }
            return;
        }

        Item item = itemList.get(index);
        View pdfview = LayoutInflater.from(view.getContext()).inflate(R.layout.pdf_layout, null);

        TextView txt = pdfview.findViewById(R.id.pdfLayoutItemTitle);
        TextView cat = pdfview.findViewById(R.id.pdfLayoutItemCategory);
        TextView per = pdfview.findViewById(R.id.pdfLayoutItemPeriod);
        TextView desc = pdfview.findViewById(R.id.pdfLayoutItemDescription);
        ImageView imageView = pdfview.findViewById(R.id.pdfLayoutItemImage);

        txt.setText(item.getTitleWithLotNumber());
        cat.setText(item.getCategoryWithLabel());
        per.setText(item.getPeriodWithLabel());
        desc.setText(item.getDescription());

        if (imageAndDescriptionOnly) {
            txt.setVisibility(View.GONE);
            cat.setVisibility(View.GONE);
            per.setVisibility(View.GONE);
        }

        String imageUrl = item.getImageUrl();

        Glide.with(pdfview.getContext())
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);
                        addPageToDocument(pdfview, document, index + 1);
                        loadImageAndGeneratePage(document, itemList, index + 1, imageAndDescriptionOnly, reportTitle); // Proceed to next item
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // do nothing
                        view.progressBar.setVisibility(View.GONE);
                        view.reportTypeSpinner.setSelection(0);
                        view.detailSpinner.setSelection(0);
                        view.editTextReportParameter.setText("");
                        view.descriptionAndImageOnlyCheckbox.setSelected(false);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        Toast.makeText(view.getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                        view.progressBar.setVisibility(View.GONE);
                        view.reportTypeSpinner.setSelection(0);
                        view.detailSpinner.setSelection(0);
                        view.editTextReportParameter.setText("");
                        view.descriptionAndImageOnlyCheckbox.setSelected(false);
                    }
                });
    }


    private void addPageToDocument(View view, PdfDocument document, int pageNumber) {
        int PAGE_WIDTH_LETTER_SIZE = 612;
        int PAGE_HEIGHT_LETTER_SIZE = 792;
        view.measure(View.MeasureSpec.makeMeasureSpec(PAGE_WIDTH_LETTER_SIZE, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(PAGE_HEIGHT_LETTER_SIZE, View.MeasureSpec.EXACTLY));

        view.layout(0, 0, PAGE_WIDTH_LETTER_SIZE, PAGE_HEIGHT_LETTER_SIZE);

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH_LETTER_SIZE, PAGE_HEIGHT_LETTER_SIZE, pageNumber).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        view.draw(canvas);
        document.finishPage(page);
    }

    void updateDetailSpinnerBasedOnSelection(String selectedItem) {
        view.detailSpinner.setVisibility(View.GONE);
        view.editTextReportParameter.setVisibility(View.GONE);
        view.detailTextView.setVisibility(View.GONE);

        switch (selectedItem) {
            case "Lot Number":
                view.editTextReportParameter.setHint("Enter Lot Number");
                view.editTextReportParameter.setInputType(InputType.TYPE_CLASS_NUMBER);
                view.editTextReportParameter.setVisibility(View.VISIBLE);
                break;
            case "Name":
                view.editTextReportParameter.setHint("Enter Name");
                view.editTextReportParameter.setInputType(InputType.TYPE_CLASS_TEXT);
                view.editTextReportParameter.setVisibility(View.VISIBLE);
                break;
            case "Category":
//                ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(requireContext(),
//                        R.array.categories_array, R.layout.spinner_item_right_aligned);
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(view.requireContext(), R.layout.spinner_item_right_aligned, RecyclerViewStaticFragment.getCategories());
                categoryAdapter.setDropDownViewResource(R.layout.spinner_item_right_aligned);
                view.detailSpinner.setAdapter(categoryAdapter);
                view.detailTextView.setText(R.string.select_category);
                view.detailTextView.setVisibility(View.VISIBLE);
                view.detailSpinner.setVisibility(View.VISIBLE);
                break;
            case "Period":
//                ArrayAdapter<CharSequence> periodAdapter = ArrayAdapter.createFromResource(requireContext(),
//                        R.array.period_array, R.layout.spinner_item_right_aligned);
                ArrayAdapter<String> periodAdapter = new ArrayAdapter<>(view.requireContext(), R.layout.spinner_item_right_aligned, RecyclerViewStaticFragment.getPeriods());
                periodAdapter.setDropDownViewResource(R.layout.spinner_item_right_aligned);
                view.detailSpinner.setAdapter(periodAdapter);
                view.detailTextView.setText(R.string.select_period);
                view.detailTextView.setVisibility(View.VISIBLE);
                view.detailSpinner.setVisibility(View.VISIBLE);
                break;
            case "All Items":
                break;
            default:
                break;
        }
    }
}
