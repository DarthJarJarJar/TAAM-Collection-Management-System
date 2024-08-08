package com.example.b07demosummer2024;

/**
 * the presenter for login fragment
 */
public class LoginFragmentPresenter {

  private final LoginFragmentView view;
  private final LoginFragmentModel model;

  /**
   * initializes the presenter
   *
   * @param view  the view
   * @param model the model
   */
  public LoginFragmentPresenter(LoginFragmentView view, LoginFragmentModel model) {
    this.view = view;
    this.model = model;
  }

  /**
   * handles the clicking of the login button. validates the form and queries the model to attempt
   * login
   */
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