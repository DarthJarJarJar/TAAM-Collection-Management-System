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
 * singleton class that queries the firebase database for all items and categories in real time
 */
final class DatabaseManager {
    private static DatabaseManager manager;
    final private FirebaseDatabase db;
    private final List<Item> itemList;
    private final List<String> categoryList;
    private final List<String> periodList;

    private MainScreenPresenterInterface mainScreenPresenterInterface;

    /**
     * private constructor for singleton class. initializes fields and loads data from firebase
     */
    private DatabaseManager() {
        this.db = FirebaseDatabase.getInstance(
                "https://cscb07final-default-rtdb.firebaseio.com/");
        itemList = new ArrayList<>();
        categoryList = new ArrayList<>();
        periodList = new ArrayList<>();
        loadData();
    }

    /**
     * setter for mainScreenPresenterInterface, used to communicate with main screen UI
     * @param mainScreenPresenterInterface the main screen interface
     */
    public void setMainScreenPresenterInterface(
            MainScreenPresenterInterface mainScreenPresenterInterface) {
        this.mainScreenPresenterInterface = mainScreenPresenterInterface;
    }

    /**
     * returns the only instance of this class, and creates one if there is no instance
     * @return instance of DatabaseManager
     */
    public static DatabaseManager getInstance() {
        if (manager == null) {
            manager = new DatabaseManager();
        }
        return manager;
    }

    /**
     * loads items, categories and periods from DB
     */
    private void loadData() {
        loadItems();
        loadCategories();
        loadPeriods();
    }

    /**
     * queries the database for items and informs the main screen about any updates
     */
     void loadItems() {
        DatabaseReference itemsRef = db.getReference("Lot Number");
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    itemList.add(item);
                }
                if (mainScreenPresenterInterface != null)
                    mainScreenPresenterInterface.update((itemList.size() + 9) / 10);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    /**
     * queries the database for all categories
     */
    private void loadCategories() {
        DatabaseReference itemsRef = db.getReference("Categories");
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String category = snapshot.getValue(String.class);
                    categoryList.add(category);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    /**
     * queries the database for all periods
     */
    private void loadPeriods() {
        DatabaseReference itemsRef = db.getReference("Periods");
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                periodList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String period = snapshot.getValue(String.class);
                    periodList.add(period);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    /**
     * getter for itemList
     * @return list of all items in DB
     */
    public List<Item> getItems(){
        return itemList;
    }

    /**
     * getter for categoryList
     * @return list of all categories in DB
     */
    public List<String> getCategories(){
        return categoryList;
    }

    /**
     * getter for periodList
     * @return list of all periods in DB
     */
    public List<String> getPeriods(){
        return periodList;
    }

}
