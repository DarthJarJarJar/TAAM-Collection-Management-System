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
    List<AddItemFragmentModelObserver> addItemFragmentModelObserverList;

    public AddItemFragmentModel() {
        storage = FirebaseStorage.getInstance();
        manager = DatabaseManager.getInstance();
        db = FirebaseDatabase.getInstance("https://cscb07final-default-rtdb.firebaseio.com/");
        addItemFragmentModelObserverList = new ArrayList<AddItemFragmentModelObserver>();
        categoryList = manager.getCategories();
        periodList = manager.getPeriods();
    }

    public List<String> getPeriodList() {
        return periodList;
    }

    public List<String> getCategoryList() {
        return categoryList;
    }

    public void registerObserver(AddItemFragmentModelObserver addItemFragmentModelObserver) {
        addItemFragmentModelObserverList.add(addItemFragmentModelObserver);
    }

    public void raiseToastInObservers(String message) {
        for (AddItemFragmentModelObserver addItemFragmentModelObserver : addItemFragmentModelObserverList) {
            addItemFragmentModelObserver.showToast(message);
        }
    }

    public void showProgressBarInObservers() {
        for (AddItemFragmentModelObserver addItemFragmentModelObserver : addItemFragmentModelObserverList) {
            addItemFragmentModelObserver.showProgressBar();
        }
    }

    public void clearProgressBarInObservers() {
        for (AddItemFragmentModelObserver addItemFragmentModelObserver : addItemFragmentModelObserverList) {
            addItemFragmentModelObserver.clearProgressBar();
        }
    }

    public void clearFormInObservers() {
        for (AddItemFragmentModelObserver addItemFragmentModelObserver : addItemFragmentModelObserverList) {
            addItemFragmentModelObserver.clearForm();
        }
    }

    void addItemToDb(int lotNumber, String itemName, String itemPeriod, String itemCategory, String itemDescription, Uri chosenImageUri) {

        itemsRef = db.getReference("Lot Number");
        showProgressBarInObservers();

        itemsRef.child(String.valueOf(lotNumber)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    raiseToastInObservers("Lot number already exists");
                    showProgressBarInObservers();
                    return;
                }

                StorageReference storageRef = storage.getReference();
                StorageReference imagesRef = storageRef.child("images/" + System.currentTimeMillis() + "_" + chosenImageUri.getLastPathSegment());
                UploadTask uploadTask = imagesRef.putFile(chosenImageUri);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // todo make this work
                        raiseToastInObservers("Upload failed: " + exception.getMessage());
                        clearProgressBarInObservers();

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
                                        clearFormInObservers();
                                    } else {
                                        raiseToastInObservers("Failed to add item");
                                    }
                                });

                                if (!manager.getCategories().contains(itemCategory)) {
                                    DatabaseReference categoriesRef = db.getReference("Categories");
                                    String id = categoriesRef.push().getKey();

                                    categoriesRef.child(id).setValue(itemCategory).addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            raiseToastInObservers("New category added");
                                        } else {
                                            raiseToastInObservers("Failed to add new category");
                                        }
                                    });

                                }

                                if (!manager.getPeriods().contains(itemPeriod)) {
                                    DatabaseReference periodsRef = db.getReference("Periods");
                                    String id = periodsRef.push().getKey();

                                    periodsRef.child(id).setValue(itemPeriod).addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            raiseToastInObservers("New period added");
                                        } else {
                                            raiseToastInObservers("Failed to add period");
                                        }
                                    });

                                }
                                clearProgressBarInObservers();
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                raiseToastInObservers(error.getMessage());
                clearProgressBarInObservers();
            }
        });

    }

}
