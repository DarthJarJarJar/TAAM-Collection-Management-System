package com.example.b07demosummer2024;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import java.util.ArrayList;
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


  int counter = 1;
  private List<Map<String, String>> output_delete_list = new ArrayList<Map<String, String>>();
  private List<Item> itemList;

  /**
   * constructor for the presenter
   *
   * @param view  the view
   * @param model the model
   */
  public DeleteItemFragmentPresenter(DeleteItemFragmentView view, DeleteItemFragmentModel model) {
    model.presenterInterface = this;
    this.view = view;
    this.model = model;
  }

  /**
   * iterates over the selected items in itemList
   */
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

  /**
   * removes category if none of its items are left in the db
   *
   * @param category the category
   */
  private void checkRemoveCategory(String category) {
    List<Item> res = search.filterItems(-1, "", category, "",
        true, false, "");

    if (res.isEmpty()) {
      model.removeField("Categories", category);
    }
  }

  /**
   * removes a period of none of its items are left in the db
   *
   * @param period the period
   */
  private void checkRemovePeriod(String period) {
    List<Item> res = search.filterItems(-1, "", "", period,
        false, true, "");

    if (res.isEmpty()) {
      model.removeField("Periods", period);
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
