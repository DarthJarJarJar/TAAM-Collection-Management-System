package com.example.b07demosummer2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewItemsFragmentView extends Fragment {

    private List<Item> itemList;
    private ItemAdapter itemAdapter;

    public static ViewItemsFragmentView newInstance(List<Item> itemList) {
        ViewItemsFragmentView fragment = new ViewItemsFragmentView();
        fragment.itemList = itemList;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewitems_view, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.viewItemsView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        itemAdapter = new ItemAdapter(itemList, getParentFragmentManager());

        recyclerView.setAdapter(itemAdapter);

        return view;
    }

}