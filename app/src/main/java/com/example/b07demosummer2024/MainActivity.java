package com.example.b07demosummer2024;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityLoginInterface {

    boolean adminView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        }


        DatabaseManager manager = DatabaseManager.getInstance();

        if (savedInstanceState == null) {
            loadFragment(new MainScreenView());
        }
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment instanceof MainScreenView) {
            // cant go back from main screen
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navbar_menu, menu);
        updateMenuItems(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (!(currentFragment instanceof MainScreenView)) {
                // no back button for main screen
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
                }
            }
            return true;
        }

        // all the options
        if (id == R.id.nav_home) {
            loadFragment(new MainScreenView());
            return true;
        } else if (id == R.id.nav_view) {
            handleViewButtonClick();
            return true;
        } else if (id == R.id.nav_search) {
            loadFragment(new SearchFragmentView());
            return true;
        } else if (id == R.id.admin_login) {
            loadFragment(LoginFragmentView.newInstance(this));
            return true;
        } else if (id == R.id.admin_add) {
            loadFragment(new AddItemFragmentView());
            return true;
        } else if (id == R.id.admin_delete) {
            handleDeleteButtonClick();
            return true;
        } else if (id == R.id.admin_report) {
            loadFragment(new ReportFragmentView());
            return true;
        } else if (id == R.id.admin_logout) {
            adminView = false;
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
            invalidateOptionsMenu();
            loadFragment(new MainScreenView());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void handleViewButtonClick() {
        List<Item> toViewItemList = NavbarFragmentPresenter.getSelectedItems();
        if (toViewItemList.isEmpty()) {
            Toast.makeText(this, "No items selected. Select Item First", Toast.LENGTH_SHORT).show();
        } else {
            loadFragment(ViewItemsFragmentView.newInstance(toViewItemList));
        }
    }

    private void handleDeleteButtonClick() {
        List<Item> toDeleteItemList = NavbarFragmentPresenter.getSelectedItems();
        if (toDeleteItemList.isEmpty()) {
            Toast.makeText(this, "No items selected. Select Item First", Toast.LENGTH_SHORT).show();
        } else {
            loadFragment(DeleteItemFragment.newInstance(toDeleteItemList));
        }
    }

    public void toggleAdminNavbarOnLoginSuccess() {
        adminView = true;
        loadFragment(new MainScreenView());
        invalidateOptionsMenu();
    }

    private void updateMenuItems(Menu menu) {
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
}