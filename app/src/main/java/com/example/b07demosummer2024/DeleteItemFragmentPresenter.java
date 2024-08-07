package com.example.b07demosummer2024;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteItemFragmentPresenter implements DeleteItemFragmentPresenterInterface {

    DeleteItemFragmentModel model;
    DeleteItemFragmentView view;
    private SearchFragmentModel search;


    int counter = 1;
    private List<Map<String, String>> output_delete_list = new ArrayList<Map<String, String>>();
    private List<Item> itemList;

    public DeleteItemFragmentPresenter(DeleteItemFragmentView view, DeleteItemFragmentModel model) {
        model.presenterInterface = this;
        this.view = view;
        this.model = model;
    }

    void iterate_delete_items() {
        for (Item curItem : itemList) {
            model.remove_item(curItem, new DeletionSuccessListener() {
                @Override
                public void onSuccess(String category, String period) {
                    checkRemoveCategory(category);
                    checkRemovePeriod(period);
                }
            });
        }
    }

    private void checkRemoveCategory(String category){
        List<Item> res = search.filterItems(-1, "", category, "", true, false, "");

        if(res.isEmpty()){
            model.removeField("Categories", category);
        }
    }

    private void checkRemovePeriod(String period){
        List<Item> res = search.filterItems(-1, "", "", period, false, true, "");

        if(res.isEmpty()){
            model.removeField("Periods", period);
        }
    }

    @Override
    public void close(){
        if (counter == itemList.size()) {
            view.showToast(Integer.toString(counter) + " Items deleted");
            view.closeFragment();
        } else {
            counter ++;
        }
    }

    @Override
    public void printFailure(String message){
        view.showToast(message);
    }

    void setSearch(SearchFragmentModel search){
        this.search = search;
    }

    void setItemList(List<Item> itemList){
        this.itemList = new ArrayList<>(itemList);
    }

    List<Map<String, String>> setupSpinner(){
        for(int i = 0; i < itemList.size(); i++){
            Item curItem = itemList.get(i);
            Map<String, String> data = new HashMap<String, String>(2);
            data.put("Title", curItem.getTitle());
            data.put("Lot Number", "#"+Integer.toString(curItem.getId()));
            output_delete_list.add(data);
        }
        return output_delete_list;
    }


}
