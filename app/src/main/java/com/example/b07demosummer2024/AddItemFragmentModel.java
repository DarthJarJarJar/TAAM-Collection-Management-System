package com.example.b07demosummer2024;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class AddItemFragmentModel {
    private FirebaseStorage storage;
    private FirebaseDatabase db;
    private DatabaseReference itemsRef;
    private DatabaseManager manager;
    private List<String> categoryList, periodList;
    AddItemFragmentPresenterInterface presenterInterface;

    public AddItemFragmentModel() {
        storage = FirebaseStorage.getInstance();
        manager = DatabaseManager.getInstance();
        db = FirebaseDatabase.getInstance("https://cscb07final-default-rtdb.firebaseio.com/");
        categoryList = manager.getCategories();
        periodList = manager.getPeriods();
    }

    public List<String> getPeriodList() {
        return periodList;
    }

    public List<String> getCategoryList() {
        return categoryList;
    }

    void addItemToDb(int lotNumber, String itemName, String itemPeriod, String itemCategory, String itemDescription, Uri chosenImageUri) {

        itemsRef = db.getReference("Lot Number");
        presenterInterface.showProgressBar();

        itemsRef.child(String.valueOf(lotNumber)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    presenterInterface.showToast("Lot number already exists");
                    presenterInterface.showProgressBar();
                    return;
                }

                StorageReference storageRef = storage.getReference();
                StorageReference imagesRef = storageRef.child("images/" + System.currentTimeMillis() + "_" + chosenImageUri.getLastPathSegment());
                UploadTask uploadTask = imagesRef.putFile(chosenImageUri);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        presenterInterface.showToast("Upload failed: " + exception.getMessage());
                        presenterInterface.clearProgressBar();

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                String  uploadedImageUri = downloadUri.toString();
                                Item item = new Item(lotNumber, itemName, itemCategory, itemPeriod, itemDescription, uploadedImageUri);


                                itemsRef.child(String.valueOf(lotNumber)).setValue(item).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        presenterInterface.clearForm();
                                    } else {
                                        presenterInterface.showToast("Failed to add item");
                                    }
                                });

                                if (!manager.getCategories().contains(itemCategory)) {
                                    DatabaseReference categoriesRef = db.getReference("Categories");
                                    String id = categoriesRef.push().getKey();

                                    categoriesRef.child(id).setValue(itemCategory).addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            presenterInterface.showToast("New category added");
                                        } else {
                                            presenterInterface.showToast("Failed to add new category");
                                        }
                                    });

                                }

                                if (!manager.getPeriods().contains(itemPeriod)) {
                                    DatabaseReference periodsRef = db.getReference("Periods");
                                    String id = periodsRef.push().getKey();

                                    periodsRef.child(id).setValue(itemPeriod).addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            presenterInterface.showToast("New period added");
                                        } else {
                                            presenterInterface.showToast("Failed to add period");
                                        }
                                    });

                                }
                                presenterInterface.clearProgressBar();
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                presenterInterface.showToast(error.getMessage());
                presenterInterface.clearProgressBar();
            }
        });

    }

}
