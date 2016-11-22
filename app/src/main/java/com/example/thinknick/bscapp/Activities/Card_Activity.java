package com.example.thinknick.bscapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.thinknick.bscapp.R;

public class Card_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        Button goToScrapbookButton = (Button) findViewById(R.id.goToScrapbookButton);
        goToScrapbookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent2 = new Intent(Card_Activity.this, ScrapBActivity.class);
                startActivity(Intent2);
            }
        });
        Button goToRecievedCard = (Button) findViewById(R.id.recievedSBb);
        goToRecievedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recievedSB = new Intent(Card_Activity.this, RecievedSB.class);
                startActivity(recievedSB);
            }
        });


    }
}
