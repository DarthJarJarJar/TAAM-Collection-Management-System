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

public class MainScreenView extends Fragment {
    private Button buttonBack;
    private Button buttonNext;
    private TextView pageNoInfo;
    private ItemAdapter itemAdapter;
    private MainScreenPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        presenter = new MainScreenPresenter(this, new MainScreenModel());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        buttonBack = view.findViewById(R.id.buttonBack);
        buttonNext = view.findViewById(R.id.buttonNext);
        pageNoInfo = view.findViewById(R.id.textView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        itemAdapter = new ItemAdapter(new ArrayList<>(), getParentFragmentManager());
        recyclerView.setAdapter(itemAdapter);

        buttonBack.setOnClickListener(v -> {
            presenter.handlePageDown();
        });

        buttonNext.setOnClickListener(v -> {
            Log.d("Btn", "next pressed");
            presenter.handlePageUp();
        });


        return view;
    }

//    void setButtonNextEnableState(boolean condition) {
//        buttonBack.setEnabled(condition);
//    }
//
//    void setButtonBackEnableState(boolean condition) {
//        buttonNext.setEnabled(condition);
//    }

    void updateRecyclerList(List<Item> updatedList) {
        itemAdapter.updateList(updatedList);
    }

    void updatePageInfo(String pageInfo) {
        pageNoInfo.setText(pageInfo);
    }
}
