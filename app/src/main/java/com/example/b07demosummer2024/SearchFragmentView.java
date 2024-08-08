package com.example.b07demosummer2024;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;

/**
 * view for the search fragment
 */
public class SearchFragmentView extends Fragment {


    private EditText lotNumberInput;
    private EditText itemNameInput;
    private EditText keyWordInput;
    private Spinner categorySpinner;
    private Spinner periodSpinner;
    private TextView categoryLabel;
    private TextView periodLabel;
    private Button searchButton;
    private CheckBox checkBoxCategory;
    private CheckBox checkBoxPeriod;
    private SearchFragmentPresenter presenter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        lotNumberInput = view.findViewById(R.id.lotNumberInput);
        itemNameInput = view.findViewById(R.id.itemNameInput);
        keyWordInput = view.findViewById(R.id.keyWordInput);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        periodSpinner = view.findViewById(R.id.periodSpinner);
        categoryLabel = view.findViewById(R.id.categoryLabel);
        periodLabel = view.findViewById(R.id.periodLabel);
        searchButton = view.findViewById(R.id.buttonSearch);
        checkBoxCategory = view.findViewById(R.id.checkBoxCategory);
        checkBoxPeriod = view.findViewById(R.id.checkBoxPeriod);

        presenter = new SearchFragmentPresenter(this, new SearchFragmentModel());

        setupSpinners();
        setupCheckBoxes();

        searchButton.setOnClickListener(v -> presenter.handleSearchButtonClick());

        if (presenter.getAllItems() != null) {
            Log.d("SearchFragment", "allItems: " + presenter.getAllItems().toString());
        } else {
            Log.e("SearchFragment", "allItems is null");
        }

        return view;
    }


    /**
     * init all spinners
     */
    private void setupSpinners() {
        presenter.setupSpinners();
    }

    /**
     * change the check boxes' labels visibility based on whether they are selected or not
     */
    private void setupCheckBoxes() {
        checkBoxCategory.setOnCheckedChangeListener((buttonView, isChecked) -> {
            categoryLabel.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            categorySpinner.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });


        checkBoxPeriod.setOnCheckedChangeListener((buttonView, isChecked) -> {
            periodLabel.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            periodSpinner.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });


        categoryLabel.setVisibility(View.GONE);
        categorySpinner.setVisibility(View.GONE);
        periodLabel.setVisibility(View.GONE);
        periodSpinner.setVisibility(View.GONE);
    }


    /**
     * gets the lot number from the form, if valid integer
     * @return lot number entered
     */
    public int getLotNumber() {
        if (lotNumberInput != null) {
            String lotNumberString = lotNumberInput.getText().toString().trim();
            if (!lotNumberString.isEmpty()) {
                try {
                    return Integer.parseInt(lotNumberString);
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Invalid lot number", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return -1;
    }


    /**
     * gets the item name from UI form
     * @return item name if exists, otherwise empty string
     */
    public String getItemName() {
        return itemNameInput != null ? itemNameInput.getText().toString().trim() : "";
    }


    /**
     * gets the category name from UI form
     * @return category name if exists, "Any" otherwise
     */
    public String getCategory() {
        return checkBoxCategory.isChecked() ? (categorySpinner != null
                && categorySpinner.getSelectedItem() != null ?
                categorySpinner.getSelectedItem().toString() : "Any") : "Any";
    }


    /**
     * gets the period name from UI form
     * @return period name if exists, "Any" otherwise
     */
    public String getPeriod() {
        return checkBoxPeriod.isChecked() ? (periodSpinner != null &&
                periodSpinner.getSelectedItem() != null ?
                periodSpinner.getSelectedItem().toString() : "Any") : "Any";
    }

    /**
     * gets the keyword string from the UI form
     * @return the keyword string if exists, otherwise empty string
     */
    public String getKeyword(){
        return keyWordInput != null ? keyWordInput.getText().toString().trim() : "";
    }


    /**
     * returns category checkbox state from the form
     * @return true iff category checkbox is checked
     */
    public boolean categoryChecked(){
        return checkBoxCategory.isChecked();
    }

    /**
     * returns period checkbox state from the form
     * @return true iff period checkbox is checked
     */
    public boolean periodChecked(){
        return checkBoxPeriod.isChecked();
    }


    /**
     * getter for ui spinner for category
     * @return ui spinner for category
     */
    public Spinner getCategorySpinner(){
        return categorySpinner;
    }


    /**
     * getter for ui spinner for period
     * @return ui spinner for period
     */
    public Spinner getPeriodSpinner(){
        return periodSpinner;
    }


    /**
     * shows a toast message on the ui
     * @param message the message to show
     */
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    /**
     * displays the search results in a recycler view
     * @param filteredItems list of items corresponding to the search result
     */
    public void displayResults(List<Item> filteredItems) {
        if (filteredItems.isEmpty()) {
            showToast("No items match the search criteria.");
        } else {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment newFragment = ViewItemsFragmentView.newInstance(filteredItems);


            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, newFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}


