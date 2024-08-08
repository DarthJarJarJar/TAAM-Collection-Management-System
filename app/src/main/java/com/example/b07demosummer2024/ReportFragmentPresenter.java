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

/**
 * presenter for report fragment
 */
public class ReportFragmentPresenter {

  private static final int PAGE_WIDTH_LETTER_SIZE = 612;
  private static final int PAGE_HEIGHT_LETTER_SIZE = 792;

  ReportFragmentView view;
  ReportFragmentModel model;

  /**
   * constructor for the presenter
   *
   * @param view  the view
   * @param model the model
   */
  public ReportFragmentPresenter(ReportFragmentView view, ReportFragmentModel model) {
    this.view = view;
    this.model = model;
  }

  /**
   * validates the generate report form based on the selected reportType
   *
   * @param reportType the type of report selected in UI
   * @return trye iff the fields corresponding the reportType are nonempty
   */
  private boolean validateGenerateReportForm(String reportType) {
    switch (reportType) {
      case "Lot Number":
      case "Name":
        return !(view.getEditTextValue().isEmpty());
      case "Category":
      case "Period":
        return !(view.getDetailSpinnerValue().isEmpty());
      case "All Items":
        return true;
      default:
        return false;
    }
  }

  /**
   * generates pdf report of items
   *
   * @param reportType              the type of report to be generated
   * @param imageAndDescriptionOnly whether the report will only have image and description
   */
  void makePdf(String reportType, boolean imageAndDescriptionOnly) {
    if (!validateGenerateReportForm(reportType)) {
      view.showToast("Please fill all the fields");
      return;
    }

    List<Item> allItems = model.getItems();
    List<Item> reportList = new ArrayList<>();
    String title = generateReportTitle(reportType, imageAndDescriptionOnly);

    switch (reportType) {
      case "Lot Number":
        int lotNumber = Integer.parseInt(view.getEditTextValue());
        for (Item item : allItems) {
          if (item.getId() == lotNumber) {
            reportList.add(item);
          }
        }
        break;
      case "Name":
        String name = view.getEditTextValue();
        for (Item item : allItems) {
          if (item.getTitle().equals(name)) {
            reportList.add(item);
          }
        }
        break;
      case "Category":
        String category = view.getEditTextValue();
        for (Item item : allItems) {
          if (item.getCategory().equals(category)) {
            reportList.add(item);
          }
        }
        break;
      case "Period":
        String period = view.getDetailSpinnerValue();
        for (Item item : allItems) {
          if (item.getPeriod().equals(period)) {
            reportList.add(item);
          }
        }
        break;
      case "All Items":
        reportList.addAll(allItems);
        break;
      default:
        break;
    }

    if (reportList.isEmpty()) {
      view.showToast("No items match your criteria, report not created");
      return;
    }

    view.showProgressBar();

    generatePdf(reportList, imageAndDescriptionOnly, title);

  }

  /**
   * generates title of report based on its type
   *
   * @param reportType       type of the report
   * @param imageAndDescOnly whether the report will only have images and descriptions
   * @return the report title
   */
  private String generateReportTitle(String reportType, boolean imageAndDescOnly) {
    String title = "Report_";

    switch (reportType) {
      case "Category":
      case "Period":
        title += reportType + "_" + view.getDetailSpinnerValue();
        break;
      case "Lot Number":
        title += "Lot_Number_" + view.getEditTextValue();
        break;
      case "Name":
        title += reportType + "_" + view.getEditTextValue();
        break;
      case "All Items":
        title += "All_Items";
        break;
      default:
        break;
    }

    if (imageAndDescOnly) {
      title += "_Image_And_Desc_Only";
    }
    title += "_" + System.currentTimeMillis() + ".pdf";

    return title;

  }

  /**
   * generates all pages of the pdf document
   *
   * @param itemList                list of items to include in the report
   * @param imageAndDescriptionOnly whether the report is item and description only
   * @param reportTitle             the title of the report
   */
  private void generatePdf(List<Item> itemList, boolean imageAndDescriptionOnly,
      String reportTitle) {
    PdfDocument document = new PdfDocument();
    loadImageAndGeneratePage(document, itemList, 0, imageAndDescriptionOnly, reportTitle);
  }

  /**
   * recursively loads pages to the pdf
   *
   * @param document                the pdf document
   * @param itemList                the list of items
   * @param index                   index of the item whose page is to be generated
   * @param imageAndDescriptionOnly whether the report is image and description only
   * @param reportTitle             the title of the report
   */
  private void loadImageAndGeneratePage(PdfDocument document, List<Item> itemList, int index,
      boolean imageAndDescriptionOnly, String reportTitle) {
    if (index >= itemList.size()) {
      // base case: all pages have been generated at this point
      File downloadsDir = Environment.getExternalStoragePublicDirectory(
          Environment.DIRECTORY_DOWNLOADS);
      File file = new File(downloadsDir, reportTitle);

      try (FileOutputStream fos = new FileOutputStream(file)) {
        document.writeTo(fos);
        document.close();
        view.showToast("Report saved to downloads");
        view.resetForm();

      } catch (IOException e) {
        view.showToast("Failed to generate PDF");
        view.resetForm();
      }
      return;
    }

    Item item = itemList.get(index);
    View pdfView = createPdfPageView(item, imageAndDescriptionOnly);
    ImageView imageView = pdfView.findViewById(R.id.pdfLayoutItemImage);

    String imageUrl = item.getUrl();

    Glide.with(pdfView.getContext())
        .asBitmap()
        .load(imageUrl)
        .into(new CustomTarget<Bitmap>() {
          @Override
          public void onResourceReady(@NonNull Bitmap resource,
              @Nullable Transition<? super Bitmap> transition) {
            imageView.setImageBitmap(resource);
            addPageToDocument(pdfView, document, index + 1);
            loadImageAndGeneratePage(document, itemList, index + 1, imageAndDescriptionOnly,
                reportTitle); // Proceed to next item
          }

          @Override
          public void onLoadCleared(@Nullable Drawable placeholder) {
            // do nothing
            view.resetForm();
          }

          @Override
          public void onLoadFailed(@Nullable Drawable errorDrawable) {
            Toast.makeText(view.getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
            view.resetForm();
          }
        });
  }


  /**
   * adds a page to a pdf document
   *
   * @param pdfView    the xml view representing the page layout
   * @param document   the document where the page will be added
   * @param pageNumber page number of the page to be added
   */
  private void addPageToDocument(View pdfView, PdfDocument document, int pageNumber) {
    pdfView.measure(
        View.MeasureSpec.makeMeasureSpec(PAGE_WIDTH_LETTER_SIZE, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(PAGE_HEIGHT_LETTER_SIZE, View.MeasureSpec.EXACTLY));

    pdfView.layout(0, 0, PAGE_WIDTH_LETTER_SIZE, PAGE_HEIGHT_LETTER_SIZE);

    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH_LETTER_SIZE,
        PAGE_HEIGHT_LETTER_SIZE, pageNumber).create();
    PdfDocument.Page page = document.startPage(pageInfo);

    Canvas canvas = page.getCanvas();
    pdfView.draw(canvas);
    document.finishPage(page);
  }

  /**
   * sets the reports detail spinner depending on the type of report
   *
   * @param selectedSpinnerItem the selected report type
   */
  public void setSpinner(String selectedSpinnerItem) {
    view.updateDetailSpinnerBasedOnSelection(selectedSpinnerItem, model.getCategories(),
        model.getPeriods());
  }

  /**
   * creates a pdf page from an xml view
   *
   * @param item                    the item which will be on the page
   * @param imageAndDescriptionOnly whether to include image and description only
   * @return the generated view having info about the item
   */
  private View createPdfPageView(Item item, boolean imageAndDescriptionOnly) {
    View pdfView = LayoutInflater.from(view.getContext()).inflate(R.layout.pdf_layout, null);

    TextView title = pdfView.findViewById(R.id.pdfLayoutItemTitle);
    TextView category = pdfView.findViewById(R.id.pdfLayoutItemCategory);
    TextView period = pdfView.findViewById(R.id.pdfLayoutItemPeriod);
    TextView description = pdfView.findViewById(R.id.pdfLayoutItemDescription);

    title.setText(item.getTitleWithLotNumber());
    category.setText(item.getCategoryWithLabel());
    period.setText(item.getPeriodWithLabel());
    description.setText(item.getDescription());

    if (imageAndDescriptionOnly) {
      title.setVisibility(View.GONE);
      category.setVisibility(View.GONE);
      period.setVisibility(View.GONE);
    }

    return pdfView;
  }

}
