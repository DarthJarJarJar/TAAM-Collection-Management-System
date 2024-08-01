package com.example.b07demosummer2024;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeNavbarFragment extends Fragment {

    // private FragmentManager fragmentManager = getParentFragmentManager();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_navbar, container, false);

        ImageButton buttonView = view.findViewById(R.id.view_button);
        ImageButton buttonSearch = view.findViewById(R.id.search_button);
        ImageButton buttonAdmin = view.findViewById(R.id.admin_button);

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

            }
        });

        return view;
    }

    private void handleViewButtonClick() {
        List<Item> itemList = RecyclerViewStaticFragment.getItems();
        List<Item> toViewitemList = new ArrayList<>();
        for(int i = 0; i < itemList.size(); i++){
            Item curItem = itemList.get(i);
            if(curItem.isChecked()){
                toViewitemList.add(curItem);
            }
        }

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        Fragment newFragment = ViewItemsFragment.newInstance(toViewitemList);

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, newFragment)
                .addToBackStack(null)
                .commit();

    }




    private void handleSearchButtonClick() {
    }

    private void handleUserButtonClick() {
    }
}
