package com.example.b07demosummer2024;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

final class DatabaseManager {
    private static DatabaseManager manager;
    final private FirebaseDatabase db;
    private final List<Item> itemList;
    private final List<String> categoryList;
    private final List<String> periodList;

    private DatabaseManager() {
        this.db = FirebaseDatabase.getInstance("https://cscb07final-default-rtdb.firebaseio.com/");
        itemList = new ArrayList<>();
        categoryList = new ArrayList<>();
        periodList = new ArrayList<>();
        loadData();
    }

    public static DatabaseManager getInstance() {
        if (manager == null) {
            manager = new DatabaseManager();
        }
        return manager;
    }

    private void loadData() {
        loadItems();
        loadCategories();
        loadPeriods();
    }

    private void loadItems() {
        DatabaseReference itemsRef = db.getReference("Lot Number");
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    itemList.add(item);
                }
//                maxPages = (itemList.size() + 9) / 10;
//                updatePage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

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

    public List<Item> getItems(){
        return itemList;
    }
    public List<String> getCategories(){
        return categoryList;
    }
    public List<String> getPeriods(){
        return periodList;
    }

}
