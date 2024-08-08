package com.example.b07demosummer2024;

import java.util.List;

/**
 * model for the report fragment
 */
public class ReportFragmentModel {

  private final List<String> categoryList;
  private final List<String> periodList;
  private final List<Item> itemList;

  /**
   * constructor for the model. initializes the Db manager, and fills in categoryList, periodList
   * and itemList from DB manager
   */
  public ReportFragmentModel() {
    DatabaseManager manager = DatabaseManager.getInstance();
    this.categoryList = manager.getCategories();
    this.periodList = manager.getPeriods();
    this.itemList = manager.getItems();
  }

  /**
   * getter for period list
   *
   * @return list of periods
   */
  public List<String> getPeriods() {
    return this.periodList;
  }

  /**
   * getter for categoryList
   *
   * @return list of categories
   */
  public List<String> getCategories() {
    return this.categoryList;
  }

  /**
   * getter for itemList
   *
   * @return list of items
   */
  public List<Item> getItems() {
    return this.itemList;
  }
}
