package com.example.b07demosummer2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
    private Button buttonBack;
    private Button buttonNext;
    private TextView pageNoInfo;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;
    private int pageNumber = 1;
    private int maxPages;

    private FirebaseDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        itemList = new ArrayList<>();

        db = FirebaseDatabase.getInstance("https://cscb07final-default-rtdb.firebaseio.com/");

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        buttonBack = view.findViewById(R.id.buttonBack);
        buttonNext = view.findViewById(R.id.buttonNext);
        pageNoInfo = view.findViewById(R.id.textView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        itemAdapter = new ItemAdapter(new ArrayList<>(), getParentFragmentManager());
        recyclerView.setAdapter(itemAdapter);

        buttonBack.setOnClickListener(v -> {
            if (pageNumber > 1) {
                pageNumber--;
                updatePage();
            }
        });

        buttonNext.setOnClickListener(v -> {
            if (pageNumber < maxPages) {
                pageNumber++;
                updatePage();
            }
        });

        loadStaticItems();

        return view;
    }

    private String makePageString() {
        return "Page " + pageNumber + " of " + maxPages;
    }

    private void ensureButtonBounds() {
        buttonBack.setEnabled(pageNumber > 1);
        buttonNext.setEnabled(pageNumber < maxPages);
    }

    private void updatePage() {
        pageNoInfo.setText(makePageString());
        List<Item> pagedList = new ArrayList<>();
        int start = (pageNumber - 1) * 10;
        int end = Math.min(start + 10, itemList.size());

        for (int i = start; i < end; i++) {
            pagedList.add(itemList.get(i));
        }

        itemAdapter.updateList(pagedList);
        ensureButtonBounds();
    }

    private void loadStaticItems() {
        DatabaseReference itemsRef = db.getReference("Lot Number");
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    itemList.add(item);
                }
                maxPages = (itemList.size() + 9) / 10;
                updatePage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }
}