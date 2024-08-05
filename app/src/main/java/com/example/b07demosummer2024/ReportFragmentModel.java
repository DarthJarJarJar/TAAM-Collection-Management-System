package com.example.b07demosummer2024;

import java.util.List;

public class ReportFragmentModel {

    private final List<String> categoryList;
    private final List<String> periodList;

    public ReportFragmentModel() {
        DatabaseManager manager = DatabaseManager.getInstance();
        this.categoryList = manager.getCategories();
        this.periodList = manager.getPeriods();
    }

    public List<String> getPeriodList() {
        return this.periodList;
    }

    public List<String> getCategoryList() {
        return this.categoryList;
    }
}
