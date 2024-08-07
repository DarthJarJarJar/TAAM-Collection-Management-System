package com.example.b07demosummer2024;

public class Item {

    private int id;
    private String title;
    private String category;
    private String period;
    private String description;
    private String imageUrl;
    private String url;
    private String mediaType;
    private boolean isCheckedForView;

    public Item() {}

    public Item(int id, String title, String category, String period, String description, String imageUrl, String url, String mediaType) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.period = period;
        this.description = description;
        this.imageUrl = imageUrl;
        this.url = url;
        this.mediaType = mediaType;
        this.isCheckedForView = false;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl(){ return url; }
    public void setUrl(String url) { this.url = url; }

    public String getMediaType() { return mediaType ; }
    public void setMediaType() { this.mediaType = mediaType; }

    public boolean isChecked(){
        return isCheckedForView;
    }

    public String getLotNumberString() {
        return "#" + String.valueOf(id);
    }

    public void setChecked(boolean isChecked) {
        isCheckedForView = isChecked;
    }

    public String getCategoryWithLabel() {
        return "Category: " + category;
    }

    public String getPeriodWithLabel() {
        return "Period: " + period;
    }

    public String getTitleWithLotNumber() {
        return getLotNumberString() + ": " + getTitle();
     }
}
