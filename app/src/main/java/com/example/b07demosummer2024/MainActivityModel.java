package com.example.b07demosummer2024;

import java.util.List;

public class MainActivityModel {
    DatabaseManager manager;

    public MainActivityModel() {
        this.manager = DatabaseManager.getInstance();
    }

    public List<Item> getItems() {
        return manager.getItems();
    }
}
