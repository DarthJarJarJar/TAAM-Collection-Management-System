package com.example.b07demosummer2024;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * model for the main screen
 */
public class MainScreenModel {
    DatabaseManager manager;
    List<Item> itemList;
    MainScreenPresenterInterface mainScreenPresenterInterface;

    /**
     * the constructor for the model. initializes a lust of all items, the db manager and populates
     * the item list from DB manager
     */
    public MainScreenModel() {
        itemList = new ArrayList<>();
        manager = DatabaseManager.getInstance();
        itemList = manager.getItems();

    }

    /**
     * getter for item list
     * @return the list of all items in manager
     */
    List<Item> getItemList() {
        return manager.getItems();
    }

    /**
     * sets itemList to the list of items in maanger
     */
    void loadItemsFromDb() {
        this.itemList = manager.getItems();
    }

    /**
     * sets presenter interface to talk about to the presenter and through it, to the view
     * @param mainScreenPresenterInterface the presenter interface
     */
    public void setPresenterInterface(MainScreenPresenterInterface mainScreenPresenterInterface) {
        this.mainScreenPresenterInterface = mainScreenPresenterInterface;
        manager.setMainScreenPresenterInterface(mainScreenPresenterInterface);
    }

    /**
     * checks if itemList is empty
     * @return true iff itemList is non empty
     */
    public boolean areItemsLoaded() {
        return !itemList.isEmpty();
    }
}
