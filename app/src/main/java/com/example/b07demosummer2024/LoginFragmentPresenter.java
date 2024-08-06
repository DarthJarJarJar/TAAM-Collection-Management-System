package com.example.b07demosummer2024;

public class LoginFragmentPresenter {

    private final LoginFragmentView view;
    private final LoginFragmentModel model;

    public LoginFragmentPresenter(LoginFragmentView view, LoginFragmentModel model) {
        this.view = view;
        this.model = model;
    }

    public void handleLoginButtonClick() {
        String username = view.getUsername();
        String password = view.getPassword();

        if (!username.isEmpty() && !password.isEmpty()) {
            model.queryCredentials(username, password, new LoginListener() {
                @Override
                public void onLoginSuccess() {
                    view.showLoginSuccess();
                }

                @Override
                public void onLoginFailure(String message) {
                    view.showLoginFailure(message);
                }
            });
        } else {
            view.showLoginFailure("Missing fields");
        }
    }
}