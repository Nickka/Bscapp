package com.example.thinknick.bscapp;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Karl-Emil on 09-11-2016.
 */

@IgnoreExtraProperties
public class User {

    public String password;
    public String email;
    public String username;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String password, String email, String username) {
        this.password = password;
        this.email = email;
        this.username = username;
    }
}