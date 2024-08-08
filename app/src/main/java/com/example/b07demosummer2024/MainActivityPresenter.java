package com.example.b07demosummer2024;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * presenter for main activity
 */
public class MainActivityPresenter implements MainActivityPresenterLoginInterface {

    MainActivityView view;
    MainActivityModel model;
    boolean adminView;

    /**
     * initializes the presenter
     * @param view the view
     * @param model the model
     */
    public MainActivityPresenter(MainActivityView view, MainActivityModel model) {
        this.view = view;
        this.model = model;
    }

    /**
     * gets the items that are selected (currently marked checked)
     * @return the list of selected items
     */
    public List<Item> getSelectedItems(){
        List<Item> itemList = model.getItems(); //DatabaseManager is the model for this fragment
        List<Item> selectedItemList = new ArrayList<>();
        for(int i = 0; i < itemList.size(); i++){
            Item curItem = itemList.get(i);
            if(curItem.isChecked()){
                selectedItemList.add(curItem);
            }
        }

        return selectedItemList;
    }

    /**
     * loads a fragment
     * @param fragment the fragment to be loaded
     */
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = view.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * updates menu items according to current login state
     * @param menu the menu with all items
     */
    public void updateMenuItems(Menu menu) {
        MenuItem loginItem = menu.findItem(R.id.admin_login);
        MenuItem addItem = menu.findItem(R.id.admin_add);
        MenuItem deleteItem = menu.findItem(R.id.admin_delete);
        MenuItem reportItem = menu.findItem(R.id.admin_report);
        MenuItem logoutItem = menu.findItem(R.id.admin_logout);

        if (adminView) {
            loginItem.setVisible(false);
            addItem.setVisible(true);
            deleteItem.setVisible(true);
            reportItem.setVisible(true);
            logoutItem.setVisible(true);
        } else {
            loginItem.setVisible(true);
            addItem.setVisible(false);
            deleteItem.setVisible(false);
            reportItem.setVisible(false);
            logoutItem.setVisible(false);
        }
    }

    /**
     * handles the click of menu items
     * @param optionId the id of the selected item
     * @return true iff a valid option is selected in the menu
     */
    public boolean loadSelectedOption(int optionId) {
        // all the options
        if (optionId == R.id.nav_home) {
            loadFragment(new MainScreenView());
            return true;
        } else if (optionId == R.id.nav_view) {
            handleViewButtonClick();
            return true;
        } else if (optionId == R.id.nav_search) {
            loadFragment(new SearchFragmentView());
            return true;
        } else if (optionId == R.id.admin_login) {
            loadFragment(LoginFragmentView.newInstance(this));
            return true;
        } else if (optionId == R.id.admin_add) {
            loadFragment(new AddItemFragmentView());
            return true;
        } else if (optionId == R.id.admin_delete) {
            handleDeleteButtonClick();
            return true;
        } else if (optionId == R.id.admin_report) {
            loadFragment(new ReportFragmentView());
            return true;
        } else if (optionId == R.id.admin_logout) {
            setAdminView(false);
            view.showToast("Logged Out");
            view.invalidateOptionsMenu();
            loadFragment(new MainScreenView());
            return true;
        }

        return false;
    }

    /**
     * loads the view items fragment when view button is clicked
     */
    private void handleViewButtonClick() {
        List<Item> toViewItemList = getSelectedItems();
        if (toViewItemList.isEmpty()) {
            view.showToast("No items selected. Select Item First");
        } else {
            loadFragment(ViewItemsFragmentView.newInstance(toViewItemList));
        }
    }

    /**
     * loads the delete items fragment when delete button is clicked
     */
    private void handleDeleteButtonClick() {
        List<Item> toDeleteItemList = getSelectedItems();
        if (toDeleteItemList.isEmpty()) {
            view.showToast("No items selected. Select Item First");
        } else {
            loadFragment(DeleteItemFragmentView.newInstance(toDeleteItemList));
        }
    }

    /**
     * toggles the menu options on toolbar when login is successful and reloads main screen
     */
    public void toggleAdminNavbarOnLoginSuccess() {
        setAdminView(true);
        loadFragment(new MainScreenView());
        view.invalidateOptionsMenu();
    }

    /**
     * setter for adminView
     * @param isAdmin true or false based on if current user is logged in or not
     */
    private void setAdminView(boolean isAdmin) {
        this.adminView = isAdmin;
    }


    /**
     * display the exit app dialog box
     */
    public void exitAppDialogBox() {
        (new AppExitDialogFragment()).show(view.getSupportFragmentManager(), "EXIT_DIALOG");
    }
}
