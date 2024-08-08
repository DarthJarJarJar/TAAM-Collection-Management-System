package com.example.b07demosummer2024;

import java.util.List;

/**
 * model for the main activity
 */
public class MainActivityModel {

  DatabaseManager manager;

  /**
   * sets manager to an instance of DatabaseManager
   */
  public MainActivityModel() {
    this.manager = DatabaseManager.getInstance();
  }

  /**
   * returns a list of all DB items from manager
   *
   * @return the list of items in DB
   */
  public List<Item> getItems() {
    return manager.getItems();
  }
}
