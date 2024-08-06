package com.example.b07demosummer2024;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Registry;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;

import java.util.List;

public class NavbarFragmentView extends Fragment {

    boolean adminView = true;
    boolean isInit = true;

    private ImageButton buttonAdmin;
    private ImageButton buttonBack;
    private TextView operations;
    private Spinner adminSpinner;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_navbar, container, false);



        ImageButton buttonHome = view.findViewById(R.id.home_button);
        ImageButton buttonView = view.findViewById(R.id.view_button);
        ImageButton buttonSearch = view.findViewById(R.id.search_button);
        operations = view.findViewById(R.id.operations);

        buttonBack = view.findViewById(R.id.back_button);

        buttonAdmin = view.findViewById(R.id.admin_button);

        adminSpinner = view.findViewById(R.id.adminSpinner);

        buttonBack.setVisibility(View.INVISIBLE);
        buttonBack.setEnabled(false);

        setupSpinner();
        toggleAdminNavbar();

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleHomeButtonClick();
            }

        });

        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleViewButtonClick();
            }

        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSearchButtonClick();
            }

        });

        buttonAdmin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                handleAdminButtonClick();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toggleBackButton();
                handleBackButtonClick();
            }
        });


        adminSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isInit){
                    Log.d("test", "isInit is false");
                    isInit=false;
                    return;
                }

                String selectedItem = parent.getItemAtPosition(position).toString();
                switch (selectedItem) {
                    case "Add":
                        handleAddButtonClick();
                        break;
                    case "Delete":
                        handleDeleteButtonClick();
                        break;
                    case "Report":
                        handleReportButtonClick();
                        break;
                }

                }


            public void onNothingSelected(AdapterView<?> parent){}
        });


        return view;
    }

    private void setupSpinner(){

       ArrayAdapter<CharSequence> adminAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.admin_options, R.layout.spinner_item_right_aligned);
       adminAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       adminSpinner.setAdapter(adminAdapter);
      //  adminSpinner.setSelection(0,true);
adminSpinner.setPrompt("Ops");
       // int initialposition=adminSpinner.getSelectedItemPosition();
      //  adminSpinner.setSelection(initialposition, false);
        //adminSpinner.setVisibility(View.INVISIBLE);
       // adminSpinner.setEnabled(false);
    }


    private void handleBackButtonClick() {
        if (buttonBack.isEnabled()) {
            getParentFragmentManager().popBackStackImmediate();
            toggleBackButton();
        }
    }

    private void handleHomeButtonClick(){
        loadFragment(new MainScreenView(), true);
    }

    private void handleAddButtonClick() {
        Log.d("test", "adding");
        loadFragment(new AddItemFragmentView(), false);
    }

    private void handleReportButtonClick() {
        loadFragment(new ReportFragmentView(), false);
    }

    private List<Item> addToViewItemList(){
        List<Item> itemList = DatabaseManager.getInstance().getItems();
        List<Item> viewList = new ArrayList<>();
        for(int i = 0; i < itemList.size(); i++){
            Item curItem = itemList.get(i);
            if(curItem.isChecked()){
                viewList.add(curItem);
            }
        }

        return viewList;
    }

    private void handleDeleteButtonClick() {
        List<Item> toDeleteItemList = NavbarFragmentPresenter.getSelectedItems();

        if(toDeleteItemList.isEmpty()){
            Toast.makeText(getContext(), "No items selected. Select Item First", Toast.LENGTH_SHORT).show();
        }else{
            loadFragment(DeleteItemFragment.newInstance(toDeleteItemList), false);
        }
    }

    private void handleViewButtonClick() {
        List<Item> toViewItemList = NavbarFragmentPresenter.getSelectedItems();

        if(toViewItemList.isEmpty()){
            Toast.makeText(getContext(), "No items selected. Select Item First", Toast.LENGTH_SHORT).show();
        }else{
            loadFragment(ViewItemsFragmentView.newInstance(toViewItemList), false);
        }
    }


    private void handleSearchButtonClick() {
           loadFragment(new SearchFragment(), false);

    }

    private void handleAdminButtonClick() {
        if(adminView){
            toggleAdminNavbar();
            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
        }else{
            loadFragment(LoginFragmentView.newInstance(), false);
        }
        toggleBackButton();
    }


    public void onLoginSuccess(){
        toggleAdminNavbar();
        loadFragment(new MainScreenView(), true);
    }

    private void toggleBackButton(){

        int index = getParentFragmentManager().getBackStackEntryCount() - 1;

        FragmentManager.BackStackEntry backEntry = getParentFragmentManager().getBackStackEntryAt(index);
        String tag = backEntry.getName();


        if ("main".equals(backEntry.getName()) || index <= 1){
            buttonBack.setVisibility(View.INVISIBLE);
            buttonBack.setEnabled(false);
        } else {
            buttonBack.setVisibility(View.VISIBLE);
            buttonBack.setEnabled(true);
        }
    }


    public void toggleAdminNavbar(){
        if(adminView){
            adminView = false;

            adminSpinner.setVisibility(View.INVISIBLE);
            adminSpinner.setEnabled(false);

            operations.setVisibility(View.INVISIBLE);

            buttonAdmin.setImageResource(R.drawable.baseline_person_24);

        }else{
            adminView = true;

            adminSpinner.setVisibility(View.VISIBLE);
            adminSpinner.setEnabled(true);

            operations.setVisibility(View.VISIBLE);

            buttonAdmin.setImageResource(R.drawable.baseline_exit_to_app_24);
        }
    }

    private void loadFragment(Fragment fragment, boolean is_main){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if (is_main){
            transaction.addToBackStack("main");
        } else {
            transaction.addToBackStack(null);
        }
        transaction.commit();
        getParentFragmentManager().executePendingTransactions();
        toggleBackButton();


    }
}
