package com.example.b07demosummer2024;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddFragment extends Fragment {
    private ActivityResultLauncher<Intent> resultLauncher;
    private Button uploadImageButton, addItemButton;
    private ImageView itemImagePreview;
    private EditText editTextItemName, editTextItemLotNumber;
    private TextInputEditText editTextItemDescription;
    private Spinner spinnerCategory, spinnerPeriod;

    private Uri chosenImageUri;
    private String uploadedImageUri;

    private FirebaseStorage storage;
    private FirebaseDatabase db;
    private DatabaseReference itemsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        storage = FirebaseStorage.getInstance();
        db = FirebaseDatabase.getInstance("https://cscb07final-default-rtdb.firebaseio.com/");


        uploadImageButton = view.findViewById(R.id.imageUploadButton);
        itemImagePreview = view.findViewById(R.id.uploadImagePreview);
        editTextItemName = view.findViewById(R.id.itemNameInput);
        editTextItemLotNumber = view.findViewById(R.id.lotNumberInput);
        addItemButton = view.findViewById(R.id.addButton);
        editTextItemDescription = view.findViewById(R.id.textInputEditText);

        spinnerCategory = view.findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> categorySpinnerAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categorySpinnerAdapter);

        spinnerPeriod = view.findViewById(R.id.periodSpinner);
        ArrayAdapter<CharSequence> periodSpinnerAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.period_array, android.R.layout.simple_spinner_item);
        periodSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriod.setAdapter(periodSpinnerAdapter);

        registerResult();

        uploadImageButton.setOnClickListener(v -> pickImage());
        addItemButton.setOnClickListener(v -> addItem());

        return view;
    }

    private void addItem() {
        String itemLotNumber = editTextItemLotNumber.getText().toString().trim();
        String itemName = editTextItemName.getText().toString().trim();
        String itemPeriod = spinnerPeriod.getSelectedItem().toString().trim();
        String itemCategory = spinnerCategory.getSelectedItem().toString().trim();
        String itemDescription = editTextItemDescription.getText().toString().trim();


        if (itemLotNumber.isEmpty() || itemName.isEmpty() || itemPeriod.isEmpty() || itemCategory.isEmpty() || chosenImageUri == null) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int lotNumber = Integer.parseInt(itemLotNumber);

        itemsRef = db.getReference("Lot Number");

        ProgressBar progressBar = getView().findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);


        itemsRef.child(String.valueOf(itemLotNumber)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(getContext(), "Lot number already exists", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                StorageReference storageRef = storage.getReference();
                StorageReference imagesRef = storageRef.child("images/" + System.currentTimeMillis() + "_" + chosenImageUri.getLastPathSegment());
                UploadTask uploadTask = imagesRef.putFile(chosenImageUri);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getContext(), "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                uploadedImageUri = downloadUri.toString();
                                Item item = new Item(lotNumber, itemName, itemCategory, itemPeriod, itemDescription, uploadedImageUri);

                                itemsRef.child(String.valueOf(lotNumber)).setValue(item).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();
                                        editTextItemDescription.setText("");
                                        editTextItemLotNumber.setText("");
                                        editTextItemName.setText("");
                                        spinnerCategory.setSelection(0);
                                        spinnerPeriod.setSelection(0);
                                        itemImagePreview.setImageResource(R.drawable.box);
                                        chosenImageUri = null;
                                    } else {
                                        Toast.makeText(getContext(), "Failed to add item", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
        });

    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent);
    }

    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            chosenImageUri = result.getData().getData();
                            if (chosenImageUri != null) {
                                itemImagePreview.setImageURI(chosenImageUri);
                            } else {
                                Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

}