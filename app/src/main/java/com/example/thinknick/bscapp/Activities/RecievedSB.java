package com.example.thinknick.bscapp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.thinknick.bscapp.R;
import com.example.thinknick.bscapp.Service.CardService;
import com.example.thinknick.bscapp.Service.DownloadPicService;
import com.example.thinknick.bscapp.Service.FirebaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecievedSB extends AppCompatActivity {
    private LoginActivity.UserLoginTask mAuthTask = null;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "Login";
    private DatabaseReference mPostReference;
    private TextView recievedCardTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_recieved_sb);
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child("YaMbfYZ3IDc19GX9XheMFPIoa8J3");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        recievedCardTextView = (TextView) findViewById(R.id.recievedCardTextView);


        Intent intent = new Intent(this, DownloadPicService.class);
       // intent.putExtra("FBservice", "DL");
        this.startService(intent);
    }

    @Override
    public void onStart(){
        super.onStart();

        if (mAuthTask != null) {
            return;
        }
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                CardService cid = dataSnapshot.getValue(CardService.class);
                recievedCardTextView.setText(cid.email);
                System.out.println(cid.email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mPostReference.addValueEventListener(postListener);
    }
}
