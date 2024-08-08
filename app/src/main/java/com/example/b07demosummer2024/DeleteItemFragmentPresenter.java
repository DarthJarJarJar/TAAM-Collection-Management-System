package com.example.b07demosummer2024;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the presenter for DeleteItem fragment
 */
public class DeleteItemFragmentPresenter implements DeleteItemFragmentPresenterInterface {

    DeleteItemFragmentModel model;
    DeleteItemFragmentView view;
    private SearchFragmentModel search;
    Context context;


    int counter = 1;
    private List<Map<String, String>> output_delete_list = new ArrayList<Map<String, String>>();
    private List<Item> itemList;

    public DeleteItemFragmentPresenter(Context context, DeleteItemFragmentView view, DeleteItemFragmentModel model) {
        model.presenterInterface = this;
        this.view = view;
        this.model = model;
        this.context = context;
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
      });
    }


    private void checkRemoveCategory(String category){
        List<Item> res = search.filterItems(-1, "", category, "", true, false, "");
        String[] defaultCategories = context.getResources().getStringArray(R.array.categories_array);
        List<String> defaultCategoriesList = Arrays.asList(defaultCategories);

        if(res.isEmpty() && !defaultCategoriesList.contains(category)){
            model.removeField("Categories", category);
        }
    }

    private void checkRemovePeriod(String period){
        List<Item> res = search.filterItems(-1, "", "", period, false, true, "");
        String[] defaultPeriods = context.getResources().getStringArray(R.array.period_array);
        List<String> defaultPeriodsList = Arrays.asList(defaultPeriods);

        if(res.isEmpty() && !defaultPeriodsList.contains(period)){
            model.removeField("Periods", period);
        }
    }
  }

  /**
   * closes the delete screen and displays a success message once all items are deleted
   */
  @Override
  public void close() {
    if (counter == itemList.size()) {
      view.showToast(Integer.toString(counter) + " Items deleted");
      view.closeFragment();
    } else {
      counter++;
    }
  }

  /**
   * shows failure message as a toast
   *
   * @param message the message
   */
  @Override
  public void printFailure(String message) {
    view.showToast(message);
  }

  /**
   * sets the search fragment model
   *
   * @param search the search fragment model
   */
  void setSearch(SearchFragmentModel search) {
    this.search = search;
  }

  /**
   * initializes the itemList
   *
   * @param itemList the list of items
   */
  void setItemList(List<Item> itemList) {
    this.itemList = new ArrayList<>(itemList);
  }

  /**
   * sets up the spinner and adds items to the output delete list to be returned
   *
   * @return the list of items that are going to be deleted
   */
  List<Map<String, String>> setupSpinner() {
    for (int i = 0; i < itemList.size(); i++) {
      Item curItem = itemList.get(i);
      Map<String, String> data = new HashMap<String, String>(2);
      data.put("Title", curItem.getTitle());
      data.put("Lot Number", "#" + Integer.toString(curItem.getId()));
      output_delete_list.add(data);
    }
    return output_delete_list;
  }


}
