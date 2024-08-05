package com.example.b07demosummer2024;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteItemFragment extends Fragment {
    int counter = 1;
    private Button buttonDelete;
    private ListView delete_list;

    private List<Map<String, String>> output_delete_list = new ArrayList<Map<String, String>>();

    private FirebaseDatabase db;
    private DatabaseReference itemsRef;

    private List<Item> itemList;

    public static DeleteItemFragment newInstance(List<Item> itemList) {
        DeleteItemFragment fragment = new DeleteItemFragment();
        fragment.itemList = itemList;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_item, container, false);

        delete_list = view.findViewById(R.id.delete_list);
        buttonDelete = view.findViewById(R.id.buttonDelete);

        db = FirebaseDatabase.getInstance("https://cscb07final-default-rtdb.firebaseio.com/");

        // Set up the spinner with categories
        for(int i = 0; i < itemList.size(); i++){
            Item curItem = itemList.get(i);
            Map<String, String> data = new HashMap<String, String>(2);
            data.put("Title", curItem.getTitle());
            data.put("Lot Number", "#"+Integer.toString(curItem.getId()));
            output_delete_list.add(data);
        }

        SimpleAdapter adapter = new SimpleAdapter(getContext(), output_delete_list, android.R.layout.simple_list_item_2, new String[] {"Title", "Lot Number"}, new int[] {android.R.id.text1, android.R.id.text2});
        delete_list.setAdapter(adapter);


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iterate_delete_items();
            }
        });

        return view;
    }

    private void iterate_delete_items() {
        for (Item curItem : itemList) {
            remove_item(curItem);
        }
    }

    private void close(){
        if (counter == itemList.size()) {
            getParentFragmentManager().popBackStack();
        } else {
            counter ++;
        }
    }

    private void remove_item(Item item) {
        int id = item.getId();
        DatabaseReference itemsRef = db.getReference(   "Lot Number");

        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean itemFound = false;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Item val = snapshot.getValue(Item.class);

                    if (val != null && (val.getId() == id)) {
                        snapshot.getRef().removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Item deleted: " + val.getTitleWithLotNumber(), Toast.LENGTH_SHORT).show();
                                close();
                            } else {
                                Toast.makeText(getContext(), "Failed to delete item" + val.getTitleWithLotNumber(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        itemFound = true;
                        break;
                    }
                }
                if (!itemFound) {
                    Toast.makeText(getContext(), "Item not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}

