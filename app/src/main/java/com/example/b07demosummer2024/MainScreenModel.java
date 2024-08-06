package com.example.b07demosummer2024;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainScreenModel {
    DatabaseManager manager;
    List<Item> itemList;
    MainScreenPresenterInterface mainScreenPresenterInterface;

    public MainScreenModel() {
        itemList = new ArrayList<>();
        manager = DatabaseManager.getInstance();
        itemList = manager.getItems();

    }

    List<Item> getItemList() {
        return manager.getItems();
    }

    void loadItemsFromDb() {
        this.itemList = manager.getItems();
    }

    public void setPresenterInterface(MainScreenPresenterInterface mainScreenPresenterInterface) {
        this.mainScreenPresenterInterface = mainScreenPresenterInterface;
        manager.setMainScreenPresenterInterface(mainScreenPresenterInterface);
    }

    public boolean areItemsLoaded() {
        return !itemList.isEmpty();
    }
}
