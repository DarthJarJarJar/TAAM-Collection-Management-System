package com.example.b07demosummer2024;

import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class MainActivityPresenter implements MainActivityPresenterLoginInterface {
    MainActivityView view;
    MainActivityModel model;
    boolean adminView;

    public MainActivityPresenter(MainActivityView view, MainActivityModel model) {
        this.view = view;
        this.model = model;
    }

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

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = view.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

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
            adminView = false;
            view.showToast("Logged Out");
            view.invalidateOptionsMenu();
            loadFragment(new MainScreenView());
            return true;
        }

        return false;
    }

    private void handleViewButtonClick() {
        List<Item> toViewItemList = getSelectedItems();
        if (toViewItemList.isEmpty()) {
            view.showToast("No items selected. Select Item First");
        } else {
            loadFragment(ViewItemsFragmentView.newInstance(toViewItemList));
        }
    }

    private void handleDeleteButtonClick() {
        List<Item> toDeleteItemList = getSelectedItems();
        if (toDeleteItemList.isEmpty()) {
            view.showToast("No items selected. Select Item First");
        } else {
            loadFragment(DeleteItemFragment.newInstance(toDeleteItemList));
        }
    }

    public void toggleAdminNavbarOnLoginSuccess() {
        adminView = true;
        loadFragment(new MainScreenView());
        view.invalidateOptionsMenu();
    }
}