package com.example.thinknick.bscapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.thinknick.bscapp.R;
import com.example.thinknick.bscapp.Service.CardService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.thinknick.bscapp.R.id.deadlineTextView;
import static com.example.thinknick.bscapp.R.id.subjectTextView;

public class Card_Activity extends AppCompatActivity {

    private static final String TAG = "Cars_Activity";
    private DatabaseReference mPostReference;
    private TextView themeTextView;
    private TextView participantsTextView;
    private TextView deadlineTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        Button goToScrapbookButton = (Button) findViewById(R.id.goToScrapbookButton);
        Button seeYouCard = (Button) findViewById(R.id.recievedSBb);

        //Get a ref to the whole database
        mPostReference = FirebaseDatabase.getInstance().getReference();

        themeTextView = (TextView) findViewById(R.id.subjectTextView);
        participantsTextView = (TextView) findViewById(R.id.participantsTextView);
        deadlineTextView = (TextView) findViewById(R.id.deadlineTextView);

        goToScrapbookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent2 = new Intent(Card_Activity.this, ScrapBActivity.class);
                startActivity(Intent2);
            }
        });
        seeYouCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(Card_Activity.this, RecievedSB.class);
                startActivity(Intent);
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Get UID
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Log.w(TAG, "uid: " + uid);


                //Get card
                String card = dataSnapshot.child("users").child(uid).child("card").getValue(String.class);


                //Checks if there is any card
                if(card == null) {
                    themeTextView.setText("Der er ikke noget kort lige nu");
                    return;
                }

                // Get alt indhold til kortet
                String theme = dataSnapshot.child("Cards").child(card).child("theme").getValue(String.class);
                themeTextView.setText("Tema: " + theme);

                Iterable<DataSnapshot> participantsList = dataSnapshot.child("Cards").child(card).child("members").getChildren();
                String participants = "";
                for (DataSnapshot child : participantsList) {
                    participants += child.getValue() + "\r\n";
                }
                participantsTextView.setText("Deltagere:\r\n" + participants);

                String deadline = dataSnapshot.child("Cards").child(card).child("deadline").getValue(String.class);
                deadlineTextView.setText("Deadline: " + deadline);
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

