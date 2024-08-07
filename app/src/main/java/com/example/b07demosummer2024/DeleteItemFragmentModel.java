package com.example.b07demosummer2024;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class DeleteItemFragmentModel {
    private FirebaseDatabase db;
    private DatabaseReference itemsRef;

    DeleteItemFragmentPresenterInterface presenterInterface;

    public DeleteItemFragmentModel() {
        this.db = FirebaseDatabase.getInstance("https://cscb07final-default-rtdb.firebaseio.com/");
    }

    void remove_item(Item item, DeletionSuccessListener listener) {
        int id = item.getId();
        String category = item.getCategory();
        String period = item.getPeriod();

        itemsRef = db.getReference(   "Lot Number");

        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean itemFound = false;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Item val = snapshot.getValue(Item.class);

                    if (val != null && (val.getId() == id)) {
                        snapshot.getRef().removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                listener.onSuccess(category, period);
                                presenterInterface.close();
                            } else {
                                presenterInterface.printFailure("Failed to delete item: " + item.getTitleWithLotNumber());
                            }
                        });
                        itemFound = true;
                        break;
                    }
                }
                if (!itemFound) {
                    presenterInterface.printFailure("No such item found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                presenterInterface.printFailure("Cancelled");
            }
        });
    }

    void removeField(String reference, String fieldToRemove){
        itemsRef = db.getReference(reference);
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String fieldValue = snapshot.getValue(String.class);
                    if (fieldToRemove.equals(fieldValue)) {
                        // Remove the field
                        snapshot.getRef().removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("removeField", "Field removed successfully");
                            } else {
                                Log.d("removeField", "Failed to remove field");
                            }
                        });
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("removeField", "Database error: " + error.getMessage());
            }
        });
    }
}
