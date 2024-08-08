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
 * presenter for the delete item fragment
 */
public class DeleteItemFragmentPresenter implements DeleteItemFragmentPresenterInterface {

  DeleteItemFragmentModel model;
  DeleteItemFragmentView view;
  private SearchFragmentModel search;
  Context context;


  int counter = 1;
  private List<Map<String, String>> output_delete_list = new ArrayList<Map<String, String>>();
  private List<Item> itemList;

  /**
   * constructor for the presenter
   *
   * @param context the context
   * @param view    the view
   * @param model   the model
   */
  public DeleteItemFragmentPresenter(Context context, DeleteItemFragmentView view,
      DeleteItemFragmentModel model) {
    model.presenterInterface = this;
    this.view = view;
    this.model = model;
    this.context = context;
  }

  /**
   * iterates over itemList and attempts to delete all the items in it, and all the non-default
   * categories and periods if there are no items corresponding to them left in the DB
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
   * checks if a category has any more items associated with it in the db. if not, removes that
   * category from the db if it is a non-default one
   *
   * @param category the category to check
   */
  private void checkRemoveCategory(String category) {
    List<Item> res = search.filterItems(-1, "", category, "",
        true, false, "");
    String[] defaultCategories = context.getResources().getStringArray(R.array.categories_array);
    List<String> defaultCategoriesList = Arrays.asList(defaultCategories);

    if (res.isEmpty() && !defaultCategoriesList.contains(category)) {
      model.removeField("Categories", category);
    }
  }

  /**
   * checks if a period has any more items associated with it in the db. if not, removes that period
   * from the db if it is a non-default one
   *
   * @param period the category to check
   */
  private void checkRemovePeriod(String period) {
    List<Item> res = search.filterItems(-1, "", "", period,
        false, true, "");
    String[] defaultPeriods = context.getResources().getStringArray(R.array.period_array);
    List<String> defaultPeriodsList = Arrays.asList(defaultPeriods);

    if (res.isEmpty() && !defaultPeriodsList.contains(period)) {
      model.removeField("Periods", period);
    }
  }

  /**
   * closes the delete screen and displays the number of items deleted as a toast message
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
   * shows a toast with a failure message
   *
   * @param message the message to be shown
   */
  @Override
  public void printFailure(String message) {
    view.showToast(message);
  }

  /**
   * sets the search model
   *
   * @param search the search model
   */
  void setSearch(SearchFragmentModel search) {
    this.search = search;
  }

  /**
   * sets the itemList field by copying all elements of the given itemList to it
   *
   * @param itemList the list of items to copy from
   */
  void setItemList(List<Item> itemList) {
    this.itemList = new ArrayList<>(itemList);
  }

  /**
   * sets up the delete items preview list with all the items to be deleted
   *
   * @return list of title-lot number maps of the selected items to be deleted
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