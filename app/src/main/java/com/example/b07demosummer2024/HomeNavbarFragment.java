package com.example.b07demosummer2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeNavbarFragment extends Fragment {

    private ImageButton buttonView;
    private ImageButton buttonSearch;
    private ImageButton buttonAdmin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_navbar, container, false);

        buttonView = view.findViewById(R.id.view_button);
        buttonSearch = view.findViewById(R.id.search_button);
        buttonAdmin = view.findViewById(R.id.admin_button);

        buttonView.setOnClickListener(v -> {
            handleViewButtonClick();
        });

        buttonSearch.setOnClickListener(v -> {
            handleSearchButtonClick();
        });

        buttonAdmin.setOnClickListener(v -> {
            handleUserButtonClick();
        });

        return view;
    }

    private void handleViewButtonClick() {
    }

    private void handleSearchButtonClick() {
    }

    private void handleUserButtonClick() {
    }
}
