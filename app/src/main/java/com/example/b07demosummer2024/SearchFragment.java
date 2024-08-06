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

public class SearchFragment extends Fragment {

    private EditText lotNumberInput;
    private EditText itemNameInput;
    private Spinner categorySpinner;
    private Spinner periodSpinner;
    private TextView categoryLabel;
    private TextView periodLabel;
    private Button searchButton;
    private CheckBox checkBoxCategory;
    private CheckBox checkBoxPeriod;
    private List<Item> allItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        lotNumberInput = view.findViewById(R.id.lotNumberInput);
        itemNameInput = view.findViewById(R.id.itemNameInput);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        periodSpinner = view.findViewById(R.id.periodSpinner);
        categoryLabel = view.findViewById(R.id.categoryLabel);
        periodLabel = view.findViewById(R.id.periodLabel);
        searchButton = view.findViewById(R.id.buttonSearch);
        checkBoxCategory = view.findViewById(R.id.checkBoxCategory);
        checkBoxPeriod = view.findViewById(R.id.checkBoxPeriod);

        setupSpinners();
        setupCheckBoxes();

        searchButton.setOnClickListener(v -> handleSearchButtonClick());
        allItems = DatabaseManager.getInstance().getItems();

        if (allItems != null) {
            Log.d("SearchFragment", "allItems: " + allItems.toString());
        } else {
            Log.e("SearchFragment", "allItems is null");
        }

        return view;
    }

    private void setupSpinners() {

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
                getContext(),
                android.R.layout.simple_spinner_item,
                categories
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        ArrayAdapter<String> periodAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                periods
        );
        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodSpinner.setAdapter(periodAdapter);
    }


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

    private void handleSearchButtonClick() {

        int lotNumber = -1;
        if (lotNumberInput != null) {
            String lotNumberString = lotNumberInput.getText().toString().trim();
            if (!lotNumberString.isEmpty()) {
                try {
                    lotNumber = Integer.parseInt(lotNumberString);
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Invalid lot number", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        String itemName = itemNameInput != null ? itemNameInput.getText().toString().trim() : "";
        String category = checkBoxCategory.isChecked() ? (categorySpinner != null && categorySpinner.getSelectedItem() != null ? categorySpinner.getSelectedItem().toString() : "Any") : "Any";
        String period = checkBoxPeriod.isChecked() ? (periodSpinner != null && periodSpinner.getSelectedItem() != null ? periodSpinner.getSelectedItem().toString() : "Any") : "Any";

        boolean noCriteriaEntered = lotNumber == -1 && itemName.isEmpty() &&
                category.equals("Any") && period.equals("Any");

        if (noCriteriaEntered) {
            Toast.makeText(getActivity(), "Please enter at least one search criterion.", Toast.LENGTH_SHORT).show();
        } else {
            List<Item> filteredItems = filterItems(lotNumber, itemName, category, period);
            displayResults(filteredItems);
        }
    }

    private List<Item> filterItems(int lotNumber, String itemName, String category, String period) {
        List<Item> filteredItems = new ArrayList<>();
        if (allItems == null) {
            Log.e("SearchFragment", "allItems is null in filterItems");
            return filteredItems;
        }
        for (Item item : allItems) {
            boolean matchesLotNumber = (lotNumber == -1) || (item.getId() == lotNumber);
            boolean matchesItemName = itemName.isEmpty() || item.getTitle().equalsIgnoreCase(itemName);
            boolean matchesCategory = !checkBoxCategory.isChecked() || category.equals("Any") || item.getCategory().equals(category);
            boolean matchesPeriod = !checkBoxPeriod.isChecked() || period.equals("Any") || item.getPeriod().equals(period);

            if (matchesLotNumber && matchesItemName && matchesCategory && matchesPeriod) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    private void displayResults(List<Item> filteredItems) {
        if (filteredItems.isEmpty()) {
            Toast.makeText(getActivity(), "No items match the search criteria.", Toast.LENGTH_SHORT).show();
        } else {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment newFragment = ViewItemsFragment.newInstance(filteredItems);

            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, newFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}

