package com.example.b07demosummer2024;

import java.util.List;

public class ReportFragmentModel {

    private final List<String> categoryList;
    private final List<String> periodList;
    private final List<Item> itemList;

    public ReportFragmentModel() {
        DatabaseManager manager = DatabaseManager.getInstance();
        this.categoryList = manager.getCategories();
        this.periodList = manager.getPeriods();
        this.itemList = manager.getItems();
    }

    public List<String> getPeriods() {
        return this.periodList;
    }

    public List<String> getCategories() {
        return this.categoryList;
    }

    public List<Item> getItems() {
        return this.itemList;
    }
}
