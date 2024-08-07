package com.example.b07demosummer2024;

import android.os.Bundle;
import android.os.Debug;
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

    private SearchFragmentModel search;

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

        search = new SearchFragmentModel();

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
            remove_item(curItem, new DeletionSuccessListener() {
                @Override
                public void onSuccess(String category, String period) {
                    System.out.println("found");
                    checkRemoveCategory(category);
                    checkRemovePeriod(period);
                }
            });
        }
    }

    private void close(){
        if (counter == itemList.size()) {
            getParentFragmentManager().popBackStack();
        } else {
            counter ++;
        }
    }

    private void remove_item(Item item, DeletionSuccessListener listener) {
        int id = item.getId();
        String category = item.getCategory();
        String period = item.getPeriod();

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
                                listener.onSuccess(category, period);
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

    private void checkRemoveCategory(String category){
        List<Item> res = search.filterItems(-1, "", category, "", true, false, "");

        if(res.isEmpty()){
            removeField("Categories", category);
        }
    }

    private void checkRemovePeriod(String period){
        List<Item> res = search.filterItems(-1, "", "", period, false, true, "");

        if(res.isEmpty()){
            removeField("Periods", period);
        }
    }

    private void removeField(String reference, String fieldToRemove){
        DatabaseReference fieldRef = db.getReference(reference);
        fieldRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String fieldValue = snapshot.getValue(String.class);
                    if (fieldToRemove.equals(fieldValue)) {
                        // Remove the field
                        snapshot.getRef().removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("removeField", "Field removed successfully");
                            } else {
                                Log.d("removeField", "Failed to remove field");
                            }
                        });
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("removeField", "Database error: " + error.getMessage());
            }
        });
    }

}

