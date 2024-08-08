package com.example.b07demosummer2024;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * view for the main screen
 */
public class MainScreenView extends Fragment {
    private TextView pageNoInfo;
    private ItemAdapter itemAdapter;
    private MainScreenPresenter presenter;
    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        presenter = new MainScreenPresenter(this, new MainScreenModel());
        recyclerView = view.findViewById(R.id.recyclerView);
        pageNoInfo = view.findViewById(R.id.textView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Button buttonBack = view.findViewById(R.id.buttonBack);
        Button buttonNext = view.findViewById(R.id.buttonNext);

        itemAdapter = new ItemAdapter(new ArrayList<>(), getParentFragmentManager());
        recyclerView.setAdapter(itemAdapter);

        buttonBack.setOnClickListener(v -> {
            presenter.handlePageDown();
        });

        buttonNext.setOnClickListener(v -> {
            presenter.handlePageUp();
        });

        presenter.loadItems();

        return view;
    }

    /**
     * runs when going to another fragment, so starts showing the back button on the navbar
     */
    @Override
    public void onPause() {
        // can never be null because we set action bar inside main activity always
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onPause();
    }

    /**
     * updates the recycler view list with items from updatedList
     * @param updatedList a list of items
     */
    void updateRecyclerList(List<Item> updatedList) {
        itemAdapter.updateList(updatedList);
    }

    /**
     * sets the page number info string in UI
     * @param pageInfo the page number info string
     */
    void updatePageInfo(String pageInfo) {
        pageNoInfo.setText(pageInfo);
    }

    /**
     * scrolls to the top of the recycler view
     */
    void scrollToTop() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (layoutManager != null)
            layoutManager.scrollToPositionWithOffset(0, 0);
    }

    /**
     * loads items again when coming back to this view from somewhere else
     */
    @Override
    public void onResume() {
        super.onResume();
        presenter.loadItems();
    }
}
