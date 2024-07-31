package com.example.b07demosummer2024;

public class CollectionItem {
    private String name;
    private int lot_number;
    private String category;
    private String period;


    public CollectionItem(int lot_number, String name, String category, String period) {
        this.name = name;
        this.category = category;
        this.period = period;
        this.lot_number = lot_number;
    }

    public int getLot_number() {
        return lot_number;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getPeriod() {
        return period;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setLot_number(int lot_number) {
        this.lot_number = lot_number;
    }

    public void setName(String name) {
        this.name = name;
    }
}
