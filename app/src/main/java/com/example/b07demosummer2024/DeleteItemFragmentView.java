package com.example.b07demosummer2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DeleteItemFragmentView extends Fragment {

    private Button buttonDelete;
    private ListView delete_list;
    DeleteItemFragmentPresenter presenter;

    private List<Map<String, String>> output_delete_list = new ArrayList<Map<String, String>>();
    private List<Item> itemList;

    public static DeleteItemFragmentView newInstance(List<Item> itemList) {
        DeleteItemFragmentView fragment = new DeleteItemFragmentView();
        fragment.itemList = itemList;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_item, container, false);

        delete_list = view.findViewById(R.id.delete_list);
        buttonDelete = view.findViewById(R.id.buttonDelete);


        presenter = new DeleteItemFragmentPresenter(requireContext(), this, new DeleteItemFragmentModel());

        presenter.setSearch(new SearchFragmentModel());
        presenter.setItemList(itemList);
        output_delete_list = new ArrayList<Map<String, String>>(presenter.setupSpinner());

        SimpleAdapter adapter = new SimpleAdapter(getContext(), output_delete_list, android.R.layout.simple_list_item_2, new String[] {"Title", "Lot Number"}, new int[] {android.R.id.text1, android.R.id.text2});
        delete_list.setAdapter(adapter);


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.iterate_delete_items();
            }
        });

        return view;
    }

    void closeFragment(){
        getParentFragmentManager().popBackStack();
    }

    void showToast(String message){
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }
}
