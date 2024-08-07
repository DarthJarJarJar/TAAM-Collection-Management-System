package com.example.b07demosummer2024;

import java.util.List;

public class NavbarFragmentModel {
    public static List<Item> getItems(){
        return DatabaseManager.getInstance().getItems();
    }
}
