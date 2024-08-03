package com.example.b07demosummer2024;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
    private Button searchButton;
    private List<Item> allItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        lotNumberInput = view.findViewById(R.id.lotNumberInput);
        itemNameInput = view.findViewById(R.id.itemNameInput);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        periodSpinner = view.findViewById(R.id.periodSpinner);
        searchButton = view.findViewById(R.id.buttonSearch);

        searchButton.setOnClickListener(v -> handleSearchButtonClick());
        allItems = RecyclerViewStaticFragment.getItems();

        if (allItems != null) {
            Log.d("SearchFragment", "allItems: " + allItems.toString());
        } else {
            Log.e("SearchFragment", "allItems is null");
        }

        return view;
    }

    private void handleSearchButtonClick() {
        // Get input values
        int lotNumber = -1; // Default to an invalid number indicating no input
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
        String category = categorySpinner != null && categorySpinner.getSelectedItem() != null ? categorySpinner.getSelectedItem().toString() : "Any";
        String period = periodSpinner != null && periodSpinner.getSelectedItem() != null ? periodSpinner.getSelectedItem().toString() : "Any";

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
            return filteredItems; // Return empty list if allItems is null
        }
        for (Item item : allItems) {
            boolean matchesLotNumber = (lotNumber == -1) || (item.getId() == lotNumber);
            boolean matchesItemName = itemName.isEmpty() || item.getTitle().equalsIgnoreCase(itemName);
            boolean matchesCategory = category.equals("Any") || item.getCategory().equals(category);
            boolean matchesPeriod = period.equals("Any") || item.getPeriod().equals(period);

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

