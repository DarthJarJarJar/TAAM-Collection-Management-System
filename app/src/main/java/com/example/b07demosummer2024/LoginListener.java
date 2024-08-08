package com.example.b07demosummer2024;

/**
 * interface to listen to success/failure of login
 */
public interface LoginListener {
    void onLoginSuccess();
    void onLoginFailure(String message);
}
