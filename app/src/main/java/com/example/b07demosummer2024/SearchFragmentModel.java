package com.example.b07demosummer2024;

import android.util.Log;


import java.util.ArrayList;
import java.util.List;

/**
 * model for the search fragment
 */
public class SearchFragmentModel {


    private List<Item> allItems;

    /**
     * constructor for the search model. loads items from DB manager
     */
    private void SearchModel() {
        allItems = DatabaseManager.getInstance().getItems();
    }

    /**
     * getter for allItems
     * @return list of all items
     */
    public List<Item> getItems(){
        return allItems;
    }

    /**
     * getter for the list of all categories from DB manager
     * @return list of all categories
     */
    public List<String> getCategories(){
        return DatabaseManager.getInstance().getCategories();
    }

    /**
     * getter for the list of all periods from DB manager
     * @return list of all periods
     */
    public List<String> getPeriods(){
        return DatabaseManager.getInstance().getPeriods();
    }


    /**
     * filters items based on given criteria
     * @param lotNumber given lot number
     * @param itemName given item name
     * @param category given category
     * @param period given period
     * @param checkCategory true if category is given
     * @param checkPeriod true if period is given
     * @param keyword given keyword
     * @return list of filtered items
     */
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

    /**
     * checks if substring is a case insensitive substring of the item title
     * @param item the item
     * @param substring the substring to be checked
     * @return true iff substring is a case insensitive substring of the item title
     */
    private boolean isSubstring(Item item, String substring){
        return item.getTitle().toLowerCase().contains(substring.toLowerCase());
    }
}
