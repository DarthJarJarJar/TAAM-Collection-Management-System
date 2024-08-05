package com.example.b07demosummer2024;

public abstract class AddItemFragmentModelObserver {
    AddItemFragmentModel model;
    public AddItemFragmentModelObserver(AddItemFragmentModel model) {
        this.model = model;
    }
    public abstract void showToast(String message);
    public abstract void showProgressBar();
    public abstract void clearProgressBar();
    public abstract void clearForm();
}
