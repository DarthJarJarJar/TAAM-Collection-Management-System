package com.example.b07demosummer2024;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.database.collection.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportFragment extends Fragment {

    private Spinner detailSpinner, reportTypeSpinner;
    private EditText editTextReportParameter;
    private TextView detailTextView;
    private CheckBox descriptionAndImageOnlyCheckbox;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_fragment, container, false);

        reportTypeSpinner = view.findViewById(R.id.reportType);
        detailSpinner = view.findViewById(R.id.detailSpinner);
        editTextReportParameter = view.findViewById(R.id.editTextReportParamter);
        detailTextView = view.findViewById(R.id.detailTextView);
        descriptionAndImageOnlyCheckbox = view.findViewById(R.id.descriptionAndPictureOnlyCheckBox);

        ArrayAdapter<CharSequence> reportSpinnerAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.report_options, R.layout.spinner_item_right_aligned);
        reportSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportTypeSpinner.setAdapter(reportSpinnerAdapter);

        reportTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                updateDetailSpinnerBasedOnSelection(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        Button generateButton = view.findViewById(R.id.generateReportButton);
        generateButton.setOnClickListener(v -> makePdf());

        return view;
    }

    private void makePdf() {
        String reportType = reportTypeSpinner.getSelectedItem().toString();
        List<Item> allItems = RecyclerViewStaticFragment.getItems();
        List<Item> reportList = new ArrayList<Item>();
        String reportTitle = "Report_";
        boolean imageAndDescriptionOnly = descriptionAndImageOnlyCheckbox.isChecked();

        switch (reportType) {
            case "Lot Number":
                int lotNumber = Integer.parseInt(editTextReportParameter.getText().toString());

                for (Item item: allItems) {
                    if (item.getId() == lotNumber) {
                        reportList.add(item);
                    }
                }

                reportTitle += "Lot_Number_" + lotNumber;
                break;
            case "Name":
                String name = editTextReportParameter.getText().toString();

                for (Item item: allItems) {
                    if (item.getTitle().equals(name)) {
                        reportList.add(item);
                    }
                }
                reportTitle += "Name_" + name;
                break;
            case "Category":
                String category = detailSpinner.getSelectedItem().toString();

                for (Item item: allItems) {
                    if (item.getCategory().equals(category)) {
                        reportList.add(item);
                    }
                }
                reportTitle += "Category_" + category;
                break;
            case "Period":
                String period = detailSpinner.getSelectedItem().toString();

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
                Toast.makeText(getContext(), "Report saved to downloads", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e("ReportFragment", "Error writing PDF", e);
                Toast.makeText(getContext(), "Failed to generate PDF", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        Item item = itemList.get(index);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.pdf_layout, null);

        TextView txt = view.findViewById(R.id.pdfLayoutItemTitle);
        TextView cat = view.findViewById(R.id.pdfLayoutItemCategory);
        TextView per = view.findViewById(R.id.pdfLayoutItemPeriod);
        TextView desc = view.findViewById(R.id.pdfLayoutItemDescription);
        ImageView imageView = view.findViewById(R.id.pdfLayoutItemImage);

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

        Glide.with(getContext())
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);
                        addPageToDocument(view, document, index + 1);
                        loadImageAndGeneratePage(document, itemList, index + 1, imageAndDescriptionOnly, reportTitle); // Proceed to next item
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // do nothing
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
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

    private void updateDetailSpinnerBasedOnSelection(String selectedItem) {
        detailSpinner.setVisibility(View.GONE);
        editTextReportParameter.setVisibility(View.GONE);
        detailTextView.setVisibility(View.GONE);

        switch (selectedItem) {
            case "Lot Number":
                editTextReportParameter.setHint("Enter Lot Number");
                editTextReportParameter.setInputType(InputType.TYPE_CLASS_NUMBER);
                editTextReportParameter.setVisibility(View.VISIBLE);
                break;
            case "Name":
                editTextReportParameter.setHint("Enter Name");
                editTextReportParameter.setInputType(InputType.TYPE_CLASS_TEXT);
                editTextReportParameter.setVisibility(View.VISIBLE);
                break;
            case "Category":
                ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(requireContext(),
                        R.array.categories_array, R.layout.spinner_item_right_aligned);
                categoryAdapter.setDropDownViewResource(R.layout.spinner_item_right_aligned);
                detailSpinner.setAdapter(categoryAdapter);
                detailTextView.setText(R.string.select_category);
                detailTextView.setVisibility(View.VISIBLE);
                detailSpinner.setVisibility(View.VISIBLE);
                break;
            case "Period":
                ArrayAdapter<CharSequence> periodAdapter = ArrayAdapter.createFromResource(requireContext(),
                        R.array.period_array, R.layout.spinner_item_right_aligned);
                periodAdapter.setDropDownViewResource(R.layout.spinner_item_right_aligned);
                detailSpinner.setAdapter(periodAdapter);
                detailTextView.setText(R.string.select_period);
                detailTextView.setVisibility(View.VISIBLE);
                detailSpinner.setVisibility(View.VISIBLE);
                break;
            case "All Items":
                break;
            default:
                break;
        }
    }
}