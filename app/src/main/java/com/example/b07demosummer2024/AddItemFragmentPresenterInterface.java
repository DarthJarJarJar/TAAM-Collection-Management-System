package com.example.b07demosummer2024;

/**
 * interface that can be used to interact with the UI of AddFragment
 */
public interface AddItemFragmentPresenterInterface {
    void showToast(String message);
    void showProgressBar();
    void clearProgressBar();
    void clearForm();
}
