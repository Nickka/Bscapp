package com.example.thinknick.bscapp.Service;

import com.google.firebase.database.IgnoreExtraProperties;


/**
 * Created by ThinkNick on 15-11-2016.
 */
@IgnoreExtraProperties
public class CardService {

    public String uid;
    public String email;


    public CardService() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public CardService(String uid, String email) {
        this.uid = uid;
        this.email = email;

    }


}


