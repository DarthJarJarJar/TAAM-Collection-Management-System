package com.example.b07demosummer2024;


import android.widget.ArrayAdapter;


import java.util.ArrayList;
import java.util.List;

/**
 * the presenter of search fragment
 */
public class SearchFragmentPresenter {


  private SearchFragmentView view;
  private SearchFragmentModel model;


  /**
   * constructor for the presenter
   *
   * @param view  the view
   * @param model the model
   */
  public SearchFragmentPresenter(SearchFragmentView view, SearchFragmentModel model) {
    this.view = view;
    this.model = model;
  }


  /**
   * initiates a search based on the given criteria in the ui
   */
  public void handleSearchButtonClick() {
    int lotNumber = view.getLotNumber();
    String itemName = view.getItemName();
    String category = view.getCategory();
    String period = view.getPeriod();
    String keyword = view.getKeyword();

    boolean noCriteriaEntered = (lotNumber == -1) &&
        itemName.isEmpty() &&
        category.equals("Any") &&
        period.equals("Any") &&
        keyword.isEmpty();

    if (noCriteriaEntered) {
      view.showToast("Please enter at least one search criterion.");
    } else {
      List<Item> filteredItems = model.filterItems(lotNumber,
          itemName, category, period, view.categoryChecked(),
          view.periodChecked(), keyword);
      view.displayResults(filteredItems);
    }
  }


  /**
   * getter for list of all items from model
   *
   * @return list of all items
   */
  public List<Item> getAllItems() {
    return model.getItems();
  }

  /**
   * sets up the spinners on the form if their corresponding checkbox is selected on the UI
   */
  public void setupSpinners() {
    List<String> categories = model.getCategories();
    List<String> periods = model.getPeriods();

    if (categories == null) {
      categories = new ArrayList<>();
    }
    if (periods == null) {
      periods = new ArrayList<>();
    }

    if (categories.isEmpty()) {
      categories.add("Default Category");
    }
    if (periods.isEmpty()) {
      periods.add("Default Period");
    }

    ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
        view.getContext(),
        android.R.layout.simple_spinner_item,
        categories
    );
    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    view.getCategorySpinner().setAdapter(categoryAdapter);

    ArrayAdapter<String> periodAdapter = new ArrayAdapter<>(
        view.getContext(),
        android.R.layout.simple_spinner_item,
        periods
    );
    periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    view.getPeriodSpinner().setAdapter(periodAdapter);
  }
}
