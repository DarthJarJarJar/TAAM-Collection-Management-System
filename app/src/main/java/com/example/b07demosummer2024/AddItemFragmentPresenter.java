package com.example.b07demosummer2024;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.util.List;

/**
 * presenter for the AddItem fragment
 */
public class AddItemFragmentPresenter implements AddItemFragmentPresenterInterface {
    AddItemFragmentModel model;
    AddItemFragmentView view;

    private ActivityResultLauncher<Intent> resultLauncher;
    private Uri chosenUri;

    private String media = "Image";

    /**
     * this constructor initializes the presenter
     * @param view the view for the presenter
     * @param model the model for the presenter
     */
    public AddItemFragmentPresenter(AddItemFragmentView view, AddItemFragmentModel model) {
        model.presenterInterface = this;
        this.view = view;
        this.model = model;
    }

    /**
     * wrapper function that shows a toast message in the UI
     * @param message the message for the toast message
     */
    public void showToast(String message) {
        view.showToast(message);
    }

    /**
     * shows progress bar in the UI
     */
    public void showProgressBar() {
        view.showProgressBar();
    }

    /**
     * removes progress bar from the ui
     */
    public void clearProgressBar() {
        view.clearProgressBar();
    }

    /**
     * clears the add form in UI
     */
    public void clearForm() {
        view.clearForm();
        chosenUri = null;
    }

    /**
     * checks if str is found in stringList ignoring case. if it is, then returns the version of str
     * that is present in stringList. otherwise returns str
     * @param stringList the list of strings to check
     * @param str the string to check
     * @return str, if str does not match any string in stringList ignoring the case, otherwise
     * the ignored-case match of str in stringList
     */
    private String replaceStringWithListOccurence(List<String> stringList, String str) {
        for (String listStr: stringList) {
            if (listStr.equalsIgnoreCase(str)) return listStr;
        }
        return str;
    }

    /**
     * validates the given item details and asks the model to add an item with given attributes
     * to the DB, if valid
     * @param itemLotNumber lot number in the form of a string
     * @param itemName name of the item
     * @param itemPeriod period of the item
     * @param itemCategory category of the item
     * @param itemDescription description of the item
     */
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

    /**
     * prompts the UI to ask the user to pick an image from their device
     */
    void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent);
        media="Image";
    }

    /**
     * prompts the UI to ask the user to pick a video from their device
     */
    void pickVideo(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent);
        media="Video";
    }

    /**
     * registers the URI of the media that the user picks from their device when prompted to do so
     */
    void registerResult() {
        resultLauncher = view.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        view.hideVideo();
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {

                            chosenUri = result.getData().getData();
                            if (chosenUri != null) {
                                String str_URI = chosenUri.toString().toUpperCase();
                                if (str_URI.contains("IMAGE")) {
                                    media="Image";
                                    view.setPreviewImageUri(chosenUri);
                                } else if (str_URI.contains("VIDEO")) {
                                    view.hideImage();
                                    media="Video";
                                    view.setPreviewVideoUri(chosenUri);
                                }
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

    /**
     * getter for categoryList from model
     * @return a list of all categories
     */
    List<String> getCategories() {
        return model.getCategoryList();
    }

    /**
     * getter for periodList from model
     * @return a list of all periods
     */
    List<String> getPeriods() {
        return model.getPeriodList();
    }

}
