package com.example.b07demosummer2024;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Registry;

import java.util.ArrayList;
import java.util.List;

public class NavbarFragment extends Fragment implements LoginListener{

    boolean adminView = true;

    private ImageButton buttonAdmin;
    private ImageButton buttonReport;
    private ImageButton buttonDelete;
    private ImageButton buttonAdd;
    private ImageButton buttonBack;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_navbar, container, false);

        ImageButton buttonHome = view.findViewById(R.id.home_button);
        ImageButton buttonView = view.findViewById(R.id.view_button);
        ImageButton buttonSearch = view.findViewById(R.id.search_button);

        buttonBack = view.findViewById(R.id.back_button);

        buttonAdmin = view.findViewById(R.id.admin_button);
        buttonReport = view.findViewById(R.id.report_button);
        buttonDelete = view.findViewById(R.id.delete_button);
        buttonAdd = view.findViewById(R.id.add_button);

        buttonBack.setVisibility(View.INVISIBLE);
        buttonBack.setEnabled(false);
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

        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                handleAddButtonClick();
            }
        });

        buttonReport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                handleReportButtonClick();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                handleDeleteButtonClick();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toggleBackButton();
                handleBackButtonClick();
            }
        });

        return view;
    }

    private void handleBackButtonClick() {
        if (buttonBack.isEnabled()) {
            getParentFragmentManager().popBackStackImmediate();
            toggleBackButton();
        }
    }

    private void handleHomeButtonClick(){
        loadFragment(new RecyclerViewStaticFragment(), true);
    }

    private void handleAddButtonClick() {
        loadFragment(new AddFragment(), false);
    }

    private void handleReportButtonClick() {
        loadFragment(new ReportFragment(), false);
    }

    private List<Item> addToViewItemList(){
        List<Item> itemList = RecyclerViewStaticFragment.getItems();
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
        List<Item> toViewitemList = addToViewItemList();

        if(toViewitemList.isEmpty()){
            Toast.makeText(getContext(), "No items selected. Select Item First", Toast.LENGTH_SHORT).show();
        }else{
            loadFragment(DeleteItemFragment.newInstance(toViewitemList), false);
        }
    }

    private void handleViewButtonClick() {
        List<Item> toViewitemList = addToViewItemList();

        if(toViewitemList.isEmpty()){
            Toast.makeText(getContext(), "No items selected. Select Item First", Toast.LENGTH_SHORT).show();
        }else{
            loadFragment(ViewItemsFragment.newInstance(toViewitemList), false);
        }
    }


    private void handleSearchButtonClick() {
           loadFragment(new SearchFragment(), false);

    }

    private void handleAdminButtonClick() {
        if(adminView){
            toggleAdminNavbar();
        }else{
            loadFragment(LoginFragment.newInstance(), false);
        }
        toggleBackButton();
    }

    @Override
    public void onLoginSuccess(){
        toggleAdminNavbar();
        loadFragment(new RecyclerViewStaticFragment(), true);
    }

    private void toggleBackButton(){

        int index = getParentFragmentManager().getBackStackEntryCount() - 1;

        FragmentManager.BackStackEntry backEntry = getParentFragmentManager().getBackStackEntryAt(index);
        String tag = backEntry.getName();


            Log.d("testing", Integer.toString(index));


        if ("main".equals(backEntry.getName()) || index <= 1){
            buttonBack.setVisibility(View.INVISIBLE);
            buttonBack.setEnabled(false);
        } else {
            buttonBack.setVisibility(View.VISIBLE);
            buttonBack.setEnabled(true);
        }
    }

    private void toggleAdminNavbar(){

        if(adminView){
            adminView = false;

            buttonAdd.setVisibility(View.INVISIBLE);
            buttonAdd.setEnabled(false);

            buttonDelete.setVisibility(View.INVISIBLE);
            buttonDelete.setEnabled(false);

            buttonReport.setVisibility(View.INVISIBLE);
            buttonReport.setEnabled(false);

            buttonAdmin.setImageResource(R.drawable.baseline_person_24);

        }else{
            adminView = true;

            buttonAdd.setVisibility(View.VISIBLE);
            buttonAdd.setEnabled(true);

            buttonDelete.setVisibility(View.VISIBLE);
            buttonDelete.setEnabled(true);

            buttonReport.setVisibility(View.VISIBLE);
            buttonReport.setEnabled(true);

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
