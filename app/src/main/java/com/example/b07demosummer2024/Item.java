package com.example.b07demosummer2024;

public class Item {

  private int id;
  private String title;
  private String category;
  private String period;
  private String description;
  private String url;
  private String mediaType;
  private boolean isCheckedForView;

  public Item() {
  }

  public Item(int id, String title, String category, String period, String description, String url,
      String mediaType) {
    this.id = id;
    this.title = title;
    this.category = category;
    this.period = period;
    this.description = description;
    this.url = url;
    this.mediaType = mediaType;
    this.isCheckedForView = false;
  }

  // Getters and setters for each field
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getPeriod() {
    return period;
  }

  public void setPeriod(String period) {
    this.period = period;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getMediaType() {
    return mediaType;
  }

  public void setMediaType() {
    this.mediaType = mediaType;
  }

  public boolean isChecked() {
    return isCheckedForView;
  }

  public void setChecked(boolean isChecked) {
    isCheckedForView = isChecked;
  }

  /**
   * generates a string consisting of the pound sign followed by the lot number of this item
   *
   * @return a string consisting of the pound sign followed by the lot number of this item
   */
  public String getLotNumberString() {
    return "#" + String.valueOf(id);
  }

  /**
   * concatenates the category string with category label
   *
   * @return the category along with label
   */
  public String getCategoryWithLabel() {
    return "Category: " + category;
  }

  /**
   * concatenates the period string with category label
   *
   * @return the period along with label
   */
  public String getPeriodWithLabel() {
    return "Period: " + period;
  }

  /**
   * concatenates the title string with lot number
   *
   * @return the title string with lot number
   */
  public String getTitleWithLotNumber() {
    return getLotNumberString() + ": " + getTitle();
  }

}
