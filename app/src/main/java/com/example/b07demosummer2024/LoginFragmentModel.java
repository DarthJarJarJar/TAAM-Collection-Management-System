package com.example.b07demosummer2024;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragmentModel{

    private final DatabaseReference dbRef;

    public LoginFragmentModel() {
        dbRef = FirebaseDatabase.getInstance().getReference("Admin");
    }


    public void queryCredentials(String username, String password, LoginListener listener) {
        dbRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String storedPassword = dataSnapshot.getValue(String.class);
                    if (password.equals(storedPassword)) {
                        listener.onLoginSuccess();
                    } else {
                        listener.onLoginFailure("Invalid credentials");
                    }
                } else {
                    listener.onLoginFailure("Invalid credentials");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onLoginFailure("Database error: " + databaseError.getMessage());
            }
        });
    }
}