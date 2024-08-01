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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddFragment extends Fragment {
    private static final String TAG = "AddFragment";
    ActivityResultLauncher<Intent> resultLauncher;
    Button button;
    ImageView img;
    EditText txt, t2;
    FirebaseStorage storage;
    Uri imageUri;
    StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        storage = FirebaseStorage.getInstance();

        button = view.findViewById(R.id.imageUploadButton);
        img = view.findViewById(R.id.uploadImagePreview);
        txt = view.findViewById(R.id.itemPeriodInput);
        t2 = view.findViewById(R.id.itemCategoryInput);

        storageReference = FirebaseStorage.getInstance().getReference();

        registerResult();

        button.setOnClickListener(v -> pickImage());

        return view;
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
                            imageUri = result.getData().getData();
                            if (imageUri != null) {
                                Log.d(TAG, "Image URI: " + imageUri.toString());
                                txt.setText(imageUri.toString());
                                img.setImageURI(imageUri);
                                uploadImageToFirebase(imageUri);
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

    private void uploadImageToFirebase(Uri imageUri) {
        if (imageUri == null) {
            Toast.makeText(getContext(), "No Image URI", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images/" + imageUri.getLastPathSegment());
        t2.setText(imageUri.getLastPathSegment());



        UploadTask uploadTask = imagesRef.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getContext(), "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Upload failed", exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {
                        Log.d(TAG, "Download URL: " + downloadUri.toString());
                        txt.setText(downloadUri.toString());
                        Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getContext(), "Failed to get download URL: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Failed to get download URL", exception);
                    }
                });
            }
        });
    }
}