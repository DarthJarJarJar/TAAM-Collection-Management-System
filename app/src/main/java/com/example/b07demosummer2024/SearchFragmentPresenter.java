package com.example.b07demosummer2024;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
public class SearchFragmentPresenter {

    private SearchFragmentView view;
    private SearchFragmentModel model;

    public SearchFragmentPresenter(SearchFragmentView view, SearchFragmentModel model) {
        this.view = view;
        this.model = model;
    }

    public void handleSearchButtonClick() {
        int lotNumber = view.getLotNumber();
        String itemName = view.getItemName();
        String category = view.getCategory();
        String period = view.getPeriod();

        boolean noCriteriaEntered = lotNumber == -1 && itemName.isEmpty() && category.equals("Any") && period.equals("Any");

        if (noCriteriaEntered) {
            view.showToast("Please enter at least one search criterion.");
        } else {
            List<Item> filteredItems = model.filterItems(lotNumber, itemName, category, period, view.categoryChecked(), view.periodChecked());
            view.displayResults(filteredItems);
        }
    }

    public List<Item> getAllItems() {
        return RecyclerViewStaticFragment.getItems();
    }

    public void setupSpinners() {
        List<String> categories = RecyclerViewStaticFragment.getCategories();
        List<String> periods = RecyclerViewStaticFragment.getPeriods();

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
