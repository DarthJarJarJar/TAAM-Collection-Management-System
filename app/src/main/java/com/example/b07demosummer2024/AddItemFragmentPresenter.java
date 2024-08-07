package com.example.b07demosummer2024;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.util.List;

public class AddItemFragmentPresenter implements AddItemFragmentPresenterInterface {
    AddItemFragmentModel model;
    AddItemFragmentView view;

    private ActivityResultLauncher<Intent> resultLauncher;
    private Uri chosenUri;

    String media = "Image";

    public AddItemFragmentPresenter(AddItemFragmentView view, AddItemFragmentModel model) {
        model.presenterInterface = this;
        this.view = view;
        this.model = model;
    }

    public void showToast(String message) {
        view.showToast(message);
    }

    public void showProgressBar() {
        view.showProgressBar();
    }

    public void clearProgressBar() {
        view.clearProgressBar();
    }

    public void clearForm() {
        view.clearForm();
        chosenUri = null;
    }

    private String replaceStringWithListOccurence(List<String> stringList, String str) {
        for (String listStr: stringList) {
            if (listStr.equalsIgnoreCase(str)) return listStr;
        }
        return str;
    }

    void addItem(String itemLotNumber, String itemName, String itemPeriod, String itemCategory, String itemDescription) {
        if (itemLotNumber.isEmpty() || itemName.isEmpty() || itemPeriod.isEmpty() || itemCategory.isEmpty()) {
            view.showToast("Please fill out all the fields");
            return;
        }

        int lotNumber;

        try {
            lotNumber = Integer.parseInt(itemLotNumber);
        } catch (Exception e) {
            view.showToast("Lot number must be a number");
            return;
        }

        model.addItemToDb(lotNumber,
                itemName,
                replaceStringWithListOccurence(model.getPeriodList(), itemPeriod),
                replaceStringWithListOccurence(model.getCategoryList(), itemCategory),
                itemDescription,
                chosenUri, media);
    }

    void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent);
        media="Image";
    }

    void pickVideo(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent);
        media="Video";
    }

    void registerResult() {
        resultLauncher = view.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            chosenUri = result.getData().getData();
                            if (chosenUri != null) {
                                view.setPreviewImageUri(chosenUri);
                            } else {
                                showToast("No Image/Video Selected");
                            }
                        } else {
                            showToast("No Image/Video Selected");
                        }
                    }
                }
        );
    }

    List<String> getCategories() {
        return model.getCategoryList();
    }

    List<String> getPeriods() {
        return model.getPeriodList();
    }

}
