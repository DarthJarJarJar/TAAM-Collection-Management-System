package com.example.b07demosummer2024;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import java.util.List;

public class AddItemFragmentView extends Fragment {
    private ImageView itemImagePreview;
    private EditText editTextItemName, editTextItemLotNumber;
    private TextInputEditText editTextItemDescription;
    private AutoCompleteTextView autoCompleteCategory, autoCompletePeriod;
    private ProgressBar progressBar;

    private AddItemFragmentPresenter presenter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);
        presenter = new AddItemFragmentPresenter(this, new AddItemFragmentModel());

        itemImagePreview = view.findViewById(R.id.uploadImagePreview);
        editTextItemName = view.findViewById(R.id.itemNameInput);
        editTextItemLotNumber = view.findViewById(R.id.lotNumberInput);
        editTextItemDescription = view.findViewById(R.id.textInputEditText);
        autoCompleteCategory = view.findViewById(R.id.categoryAutoCompleteTextView);
        autoCompletePeriod = view.findViewById(R.id.periodAutoCompleteTextView);
        progressBar = view.findViewById(R.id.progressBar);

        Button addItemButton = view.findViewById(R.id.addButton);
        Button uploadImageButton = view.findViewById(R.id.imageUploadButton);


        ArrayAdapter<String> categoryAutoCompleteAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, RecyclerViewStaticFragment.getCategories());
        categoryAutoCompleteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteCategory.setAdapter(categoryAutoCompleteAdapter);

        ArrayAdapter<String> periodAutoCompleteAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, RecyclerViewStaticFragment.getPeriods());
        periodAutoCompleteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompletePeriod.setAdapter(periodAutoCompleteAdapter);

        presenter.registerResult();

        uploadImageButton.setOnClickListener(v -> presenter.pickImage());

        addItemButton.setOnClickListener(v -> {
            presenter.addItem(editTextItemLotNumber.getText().toString().trim(),
                    editTextItemName.getText().toString().trim(),
                    autoCompletePeriod.getText().toString().trim(),
                    autoCompleteCategory.getText().toString().trim(),
                    editTextItemDescription.getText().toString().trim()
            );

        });

        return view;
    }

    void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }
    void clearProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    void clearForm() {
        showToast("Item added");
        editTextItemDescription.setText("");
        editTextItemLotNumber.setText("");
        editTextItemName.setText("");
        autoCompleteCategory.setText("");
        autoCompletePeriod.setText("");
        itemImagePreview.setImageResource(R.drawable.box);
    }

    void setPreviewImageUri(Uri imageUri) {
        itemImagePreview.setImageURI(imageUri);
    }

}