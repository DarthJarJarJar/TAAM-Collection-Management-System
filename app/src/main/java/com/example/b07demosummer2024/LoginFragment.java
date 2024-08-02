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

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class LoginFragment extends Fragment {

    private DatabaseReference dbRef;
    private EditText usernameTextfield;
    private EditText passwordTextfield;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_form_fragment, container, false);

        Button loginButton = view.findViewById(R.id.login_button);

        usernameTextfield = view.findViewById(R.id.username_textfield);
        passwordTextfield = view.findViewById(R.id.password_textfield);

        dbRef = FirebaseDatabase.getInstance().getReference("Admin");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLoginButtonClick();
            }

        });

        return view;
    }

    private void handleLoginButtonClick() {
        String username = usernameTextfield.getText().toString();
        String password = passwordTextfield.getText().toString();

        if (!username.isEmpty() && !password.isEmpty()) {
            queryCredentials(username, password);
        } else {
            Toast.makeText(getContext(), "Missing fields", Toast.LENGTH_SHORT).show();
        }
    }

    // add redirection to admin view after
    private void queryCredentials(String username, String password){
        dbRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String storedPassword = dataSnapshot.getValue(String.class);
                    if (password.equals(storedPassword)) {
                        Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
