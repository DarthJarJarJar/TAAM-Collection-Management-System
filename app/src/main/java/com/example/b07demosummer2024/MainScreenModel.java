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
//    DatabaseManager manager;
    List<Item> itemList;
    final private FirebaseDatabase db;
    MainScreenPresenterInterface mainScreenPresenterInterface;
    private int maxPages;

    public MainScreenModel() {
        itemList = new ArrayList<>();
//        manager = DatabaseManager.getInstance();
        this.db = FirebaseDatabase.getInstance("https://cscb07final-default-rtdb.firebaseio.com/");
        loadStaticItems();

//        manager.setMainScreenPresenterInterface(maxPages -> mainScreenPresenterInterface.update(maxPages));
//        itemList = manager.getItems();
    }

    List<Item> getItemList() {
        return itemList;
    }

    private void loadStaticItems() {
        DatabaseReference itemsRef = db.getReference("Lot Number");
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    itemList.add(item);
                }
                maxPages = (itemList.size() + 9) / 10;
                mainScreenPresenterInterface.update(maxPages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    public void setPresenterInterface(MainScreenPresenterInterface mainScreenPresenterInterface) {
        this.mainScreenPresenterInterface = mainScreenPresenterInterface;
    }
}
