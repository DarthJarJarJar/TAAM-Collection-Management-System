package com.example.b07demosummer2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

public class NavbarFragmentView extends Fragment {

    boolean adminView = true;

    private ImageButton buttonAdmin;
    private ImageButton buttonReport;
    private ImageButton buttonDelete;
    private ImageButton buttonAdd;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_navbar, container, false);

        ImageButton buttonHome = view.findViewById(R.id.home_button);
        ImageButton buttonView = view.findViewById(R.id.view_button);
        ImageButton buttonSearch = view.findViewById(R.id.search_button);

        buttonAdmin = view.findViewById(R.id.admin_button);
        buttonReport = view.findViewById(R.id.report_button);
        buttonDelete = view.findViewById(R.id.delete_button);
        buttonAdd = view.findViewById(R.id.add_button);

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

        buttonSearch.setOnClickListener(v -> {
            handleSearchButtonClick();
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

        return view;
    }

    private void handleHomeButtonClick(){
        loadFragment(new RecyclerViewStaticFragment());
    }
    
    private void handleAddButtonClick() {
        loadFragment(new AddItemFragmentView());
    }

    private void handleReportButtonClick() {
        loadFragment(new ReportFragmentView());
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
            loadFragment(DeleteItemFragment.newInstance(toDeleteItemList));
        }
    }

    private void handleViewButtonClick() {
        List<Item> toViewItemList = NavbarFragmentPresenter.getSelectedItems();

        if(toViewItemList.isEmpty()){
            Toast.makeText(getContext(), "No items selected. Select Item First", Toast.LENGTH_SHORT).show();
        }else{
            loadFragment(ViewItemsFragmentView.newInstance(toViewItemList));
        }
    }


    private void handleSearchButtonClick() {
        loadFragment(new SearchFragment());
    }

    private void handleAdminButtonClick() {
        if(adminView){
            toggleAdminNavbar();
            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
        }else{
            loadFragment(LoginFragmentView.newInstance());
        }
    }


    public void onLoginSuccess(){
        toggleAdminNavbar();
        loadFragment(new RecyclerViewStaticFragment());
    }


    public void toggleAdminNavbar(){
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

    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
