package com.example.b07demosummer2024;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class RecyclerViewStaticFragment extends Fragment {
    private RecyclerView recyclerView;
    private Button buttonBack;
    private Button buttonNext;
    private TextView pageNoInfo;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;
    private int pageNumber = 1;
    private int maxPages;

    private FirebaseDatabase db;
    private DatabaseReference itemsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        itemList = new ArrayList<>();

        db = FirebaseDatabase.getInstance("https://cscb07final-default-rtdb.firebaseio.com/");


        loadStaticItems();
//        itemAdapter = new ItemAdapter(itemList.subList(0, Math.min(itemList.size(),10)), getParentFragmentManager());
//        recyclerView.setAdapter(itemAdapter);
        maxPages = itemList.size() / 10 + 1;
        recyclerView = view.findViewById(R.id.recyclerView);
        buttonBack = view.findViewById(R.id.buttonBack);
        buttonNext = view.findViewById(R.id.buttonNext);
        pageNoInfo = view.findViewById(R.id.textView2);
        pageNoInfo.setText(makePageString());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ensureButtonBounds();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNumber--;
                pageNoInfo.setText(makePageString());
                createAdapter();
                ensureButtonBounds();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNumber++;
                pageNoInfo.setText(makePageString());
                createAdapter();
                ensureButtonBounds();
            }
        });

        return view;
    }

    private String makePageString() {
        return "Page " + pageNumber + " of " + maxPages;
    }

    private void ensureButtonBounds() {
        if (pageNumber == 1) {
            buttonBack.setEnabled(false);
            buttonBack.getBackground().setTint(Color.DKGRAY);
        } else {
            buttonBack.setEnabled(true);
            buttonBack.getBackground().setTint(Color.BLACK);
        }
        if (pageNumber == maxPages) {
            buttonNext.setEnabled(false);
            buttonNext.getBackground().setTint(Color.GRAY);
        } else {
            buttonNext.setEnabled(true);
            buttonNext.getBackground().setTint(Color.BLACK);

        }
    }

    private void createAdapter() {
        itemAdapter = new ItemAdapter(itemList.subList(Math.max(0, (pageNumber - 1) * 10), Math.min(itemList.size(), (pageNumber) * 10)), getParentFragmentManager());
//        itemAdapter = new ItemAdapter(itemList, getParentFragmentManager());
        recyclerView.setAdapter(itemAdapter);
    }

    private void loadStaticItems() {
        // Load static items from strings.xml or hardcoded values
        itemsRef = db.getReference("Lot Number");
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    itemList.add(item);
                }
                createAdapter();
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });

    }
}
