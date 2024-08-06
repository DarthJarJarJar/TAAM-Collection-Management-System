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


public class LoginFragmentView extends Fragment {

    private EditText usernameTextfield;
    private EditText passwordTextfield;
    private LoginFragmentPresenter presenter;

    public static LoginFragmentView newInstance() {
        return new LoginFragmentView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_form_fragment, container, false);

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

    public String getUsername() {
        return usernameTextfield.getText().toString();
    }

    public String getPassword() {
        return passwordTextfield.getText().toString();
    }

    public void showLoginSuccess() {
        Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
        NavbarFragmentView navbar = (NavbarFragmentView) getActivity().getSupportFragmentManager().findFragmentById(R.id.navbar_container);
        navbar.onLoginSuccess();
    }

    public void showLoginFailure(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
