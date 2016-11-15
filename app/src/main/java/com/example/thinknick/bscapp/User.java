package com.example.thinknick.bscapp;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Karl-Emil on 09-11-2016.
 */

@IgnoreExtraProperties
public class User {

    public String password;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.password = username;
        this.email = email;
    }
}