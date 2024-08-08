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

/**
 * model for the AddItem fragment
 */
public class AddItemFragmentModel {

  private FirebaseStorage storage;
  private FirebaseDatabase db;
  private DatabaseReference itemsRef;
  private DatabaseManager manager;
  private List<String> categoryList, periodList;
  AddItemFragmentPresenterInterface presenterInterface;

  /**
   * this constructor initializes instances of DatabaseManager, Firebase Storage and Realtime
   * Database
   */
  public AddItemFragmentModel() {
    storage = FirebaseStorage.getInstance();
    manager = DatabaseManager.getInstance();
    db = FirebaseDatabase.getInstance("https://cscb07final-default-rtdb.firebaseio.com/");
    categoryList = manager.getCategories();
    periodList = manager.getPeriods();
  }

  /**
   * getter for periodList
   *
   * @return list of all periods
   */
  public List<String> getPeriodList() {
    return periodList;
  }

  /**
   * getter for categoryList
   *
   * @return list of all categories
   */
  public List<String> getCategoryList() {
    return categoryList;
  }

  /**
   * checks if category is currently in the DB, and adds it if not
   *
   * @param category the category to be checked and added
   */
  private void checkAndAddCategoryToDB(String category) {
    if (!manager.getCategories().contains(category)) {
      DatabaseReference categoriesRef = db.getReference("Categories");
      String id = categoriesRef.push().getKey();

      categoriesRef.child(id).setValue(category).addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
          presenterInterface.showToast("New category added");
        } else {
          presenterInterface.showToast("Failed to add new category");
        }
      });
    }
  }

  /**
   * checks if period is currently in the DB, and adds it if not
   *
   * @param period the period to be checked and added
   */
  private void checkAndAddPeriodToDB(String period) {
    if (!manager.getPeriods().contains(period)) {
      DatabaseReference periodsRef = db.getReference("Periods");
      String id = periodsRef.push().getKey();

      periodsRef.child(id).setValue(period).addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
          presenterInterface.showToast("New period added");
        } else {
          presenterInterface.showToast("Failed to add period");
        }
      });

    }
  }

  /**
   * adds an item to the database, and also adds its category/period if they do not currently exist
   * in the DB
   *
   * @param item the item to be added
   */
  private void add(Item item) {
    itemsRef.child(String.valueOf(item.getId())).setValue(item).addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        presenterInterface.clearForm();
      } else {
        presenterInterface.showToast("Failed to add item");
      }
    });

    checkAndAddCategoryToDB(item.getCategory());
    checkAndAddPeriodToDB(item.getPeriod());

    presenterInterface.clearProgressBar();
  }

  /**
   * adds an item with given attributes, its category and its period to DB if they do not exist
   *
   * @param lotNumber       the lot number of the item
   * @param itemName        the name of the item
   * @param itemPeriod      the period of the item
   * @param itemCategory    the category of the item
   * @param itemDescription the description of the item
   * @param chosenUri       the chosen URI from UI, if chosen
   * @param mediaType       the type of media (IMAGE/VIDEO)
   */
  void addItemToDb(int lotNumber, String itemName, String itemPeriod, String itemCategory,
      String itemDescription, Uri chosenUri, String mediaType) {

    itemsRef = db.getReference("Lot Number");
    presenterInterface.showProgressBar();

    itemsRef.child(String.valueOf(lotNumber))
        .addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
              presenterInterface.showToast("Lot number already exists");
              presenterInterface.clearProgressBar();
              return;
            }

            if (chosenUri == null) {
              String defaultImageUrl = "https://firebasestorage.googleapis.com/v0/b/cscb07final.appspot.com/o/images%2Fplaceholder.jpg?alt=media&token=b2ffde6a-ccfd-44a3-b636-46156b2559da";
              add(new Item(lotNumber, itemName, itemCategory, itemPeriod, itemDescription,
                  defaultImageUrl, "Image"));
              return;
            }

            StorageReference storageRef = storage.getReference();

            if (mediaType.equals("Image")) {
              StorageReference imagesRef = storageRef.child(
                  "images/" + System.currentTimeMillis() + "_" + chosenUri.getLastPathSegment());
              UploadTask uploadTask = imagesRef.putFile(chosenUri);

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
                      String uploadedImageUri = downloadUri.toString();
                      Item item = new Item(lotNumber, itemName, itemCategory, itemPeriod,
                          itemDescription, uploadedImageUri, "Image");
                      add(item);
                    }
                  });
                }
              });
            } else if (mediaType.equals("Video")) {
              StorageReference videosRef = storageRef.child(
                  "videos/" + System.currentTimeMillis() + "_" + chosenUri.getLastPathSegment());
              UploadTask uploadTask = videosRef.putFile(chosenUri);

              uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                  presenterInterface.showToast("Upload failed: " + exception.getMessage());
                  presenterInterface.clearProgressBar();

                }
              }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  videosRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {
                      String uploadedVideoUri = downloadUri.toString();
                      Item item = new Item(lotNumber, itemName, itemCategory, itemPeriod,
                          itemDescription, uploadedVideoUri, "Video");
                      add(item);
                    }
                  });
                }
              });
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {
            presenterInterface.showToast(error.getMessage());
            presenterInterface.clearProgressBar();
          }
        });

  }

}
