package com.example.b07demosummer2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * the view for login fragment
 */
public class LoginFragmentView extends Fragment {

  private EditText usernameTextfield;
  private EditText passwordTextfield;
  private LoginFragmentPresenter presenter;
  private MainActivityPresenterLoginInterface mainActivityLoginInterface;

  /**
   * constructor for the view
   *
   * @param mainActivity the main activity login interface which will be informed of login
   * @return an instance of this view
   */
  public static LoginFragmentView newInstance(MainActivityPresenterLoginInterface mainActivity) {
    LoginFragmentView fragment = new LoginFragmentView();
    fragment.mainActivityLoginInterface = mainActivity;
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_login_form, container, false);

    usernameTextfield = view.findViewById(R.id.username_textfield);
    passwordTextfield = view.findViewById(R.id.password_textfield);
    Button loginButton = view.findViewById(R.id.login_button);

    presenter = new LoginFragmentPresenter(this, new LoginFragmentModel());

    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        presenter.handleLoginButtonClick();
      }
    });

    return view;
  }

  /**
   * getter for username
   *
   * @return the text in the username text field
   */
  public String getUsername() {
    return usernameTextfield.getText().toString();
  }

  /**
   * getter for password
   *
   * @return the text in the password text field
   */
  public String getPassword() {
    return passwordTextfield.getText().toString();
  }

  /**
   * shows a toast for successful login and informs the navbar inside mainActivityLoginInterface
   */
  public void showLoginSuccess() {
    Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
    mainActivityLoginInterface.toggleAdminNavbarOnLoginSuccess();
  }

  /**
   * shows a toast for login failure
   *
   * @param message the failure message
   */
  public void showLoginFailure(String message) {
    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
  }
}