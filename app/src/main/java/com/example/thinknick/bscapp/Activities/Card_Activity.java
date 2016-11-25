package com.example.thinknick.bscapp.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
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
import static com.example.thinknick.bscapp.R.id.goToScrapbookButton;
import static com.example.thinknick.bscapp.R.id.subjectTextView;

public class Card_Activity extends AppCompatActivity {

    private static final String TAG = "Cars_Activity";
    private DatabaseReference mPostReference;
    private TextView themeTextView;
    private TextView participantsTextView;
    private TextView deadlineTextView;
    private View mProgressView;

    private Button goToScrapbookButton;
    private  Button seeYouCard;

    ValueEventListener postListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        goToScrapbookButton = (Button) findViewById(R.id.goToScrapbookButton);
        seeYouCard = (Button) findViewById(R.id.recievedSBb);

        //Get a ref to the whole database
        mPostReference = FirebaseDatabase.getInstance().getReference();

        themeTextView = (TextView) findViewById(R.id.subjectTextView);
        participantsTextView = (TextView) findViewById(R.id.participantsTextView);
        deadlineTextView = (TextView) findViewById(R.id.deadlineTextView);
        mProgressView = findViewById(R.id.card_progress);
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
        showProgress(true);

        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Get UID
                if (FirebaseAuth.getInstance().getCurrentUser().getUid() == null) {return;}
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Log.w(TAG, "uid: " + uid);


                //Get card
                String card = dataSnapshot.child("users").child(uid).child("card").getValue(String.class);


                //Checks if there is any card
                if(card == null) {
                    themeTextView.setText("");
                    participantsTextView.setText("Du har desværre ikke noget kort lige nu");
                    deadlineTextView.setText("");
                    goToScrapbookButton.setVisibility(View.INVISIBLE);
                    showProgress(false);
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
                showProgress(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                showProgress(false);
                themeTextView.setText("Der er sket en fejl! Prøv igen senere, eller kontakt support");

                // ...
            }
        };
        mPostReference.addListenerForSingleValueEvent(postListener);
    }

    @Override
    public void onPause(){
        super.onPause();
        mPostReference.removeEventListener(postListener);
        Log.w(TAG, "Eventlistener on card is removed");
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            participantsTextView.setVisibility(show ? View.GONE : View.VISIBLE);
            participantsTextView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    participantsTextView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            themeTextView.setVisibility(show ? View.GONE : View.VISIBLE);
            themeTextView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    themeTextView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            deadlineTextView.setVisibility(show ? View.GONE : View.VISIBLE);
            deadlineTextView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    deadlineTextView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });


            goToScrapbookButton.setVisibility(show ? View.GONE : View.VISIBLE);
            goToScrapbookButton.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    goToScrapbookButton.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}

