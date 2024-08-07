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


    public List<String> getCategories(){
        return DatabaseManager.getInstance().getCategories();
    }


    public List<String> getPeriods(){
        return DatabaseManager.getInstance().getPeriods();
    }


    public List<Item> filterItems(int lotNumber, String itemName, String category, String period, boolean checkCategory, boolean checkPeriod, String keyword) {
        List<Item> filteredItems = new ArrayList<>();
        SearchModel();
        if (allItems == null) {
            Log.e("SearchModel", "allItems is null in filterItems");
            return filteredItems;
        }
        for (Item item : allItems) {
            boolean matchesLotNumber = (lotNumber == -1) || (item.getId() == lotNumber);
            boolean matchesItemName = (itemName.isEmpty() && keyword.isEmpty()) ||
                    (keyword.isEmpty() && item.getTitle().equalsIgnoreCase(itemName)) ||
                    (itemName.isEmpty() && isSubstring(item, keyword)) ||
                    (item.getTitle().equalsIgnoreCase((itemName)) && isSubstring(item, keyword));
            boolean matchesCategory = !checkCategory || category.equals("Any") || item.getCategory().equals(category);
            boolean matchesPeriod = !checkPeriod || period.equals("Any") || item.getPeriod().equals(period);


            if (matchesLotNumber && matchesItemName && matchesCategory && matchesPeriod) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    private boolean isSubstring(Item item, String substring){
        return item.getTitle().toLowerCase().contains(substring.toLowerCase());
    }
}
