package com.example.b07demosummer2024;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SearchFragmentModel {

    private List<Item> allItems;

    private void SearchModel() {
        allItems = DatabaseManager.getInstance().getItems();
    }

    public List<Item> getItems(){
        return allItems;
    }

    public List<Item> filterItems(int lotNumber, String itemName, String category, String period, boolean checkCategory, boolean checkPeriod) {
        List<Item> filteredItems = new ArrayList<>();
        SearchModel();
        if (allItems == null) {
            Log.e("SearchModel", "allItems is null in filterItems");
            return filteredItems;
        }
        for (Item item : allItems) {
            boolean matchesLotNumber = (lotNumber == -1) || (item.getId() == lotNumber);
            boolean matchesItemName = itemName.isEmpty() || item.getTitle().equalsIgnoreCase(itemName);
            boolean matchesCategory = !checkCategory || category.equals("Any") || item.getCategory().equals(category);
            boolean matchesPeriod = !checkPeriod || period.equals("Any") || item.getPeriod().equals(period);

            if (matchesLotNumber && matchesItemName && matchesCategory && matchesPeriod) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }
}
