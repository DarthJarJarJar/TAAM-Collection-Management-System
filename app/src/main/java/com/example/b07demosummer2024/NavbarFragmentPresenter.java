package com.example.b07demosummer2024;

import java.util.ArrayList;
import java.util.List;

public class NavbarFragmentPresenter {
    public static List<Item> getSelectedItems(){
        List<Item> itemList = DatabaseManager.getInstance().getItems(); //DatabaseManager is the model for this fragment
        List<Item> selectedItemList = new ArrayList<>();
        for(int i = 0; i < itemList.size(); i++){
            Item curItem = itemList.get(i);
            if(curItem.isChecked()){
                selectedItemList.add(curItem);
            }
        }

        return selectedItemList;
    }
}
