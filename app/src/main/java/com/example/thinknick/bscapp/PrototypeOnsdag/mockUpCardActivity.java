package com.example.thinknick.bscapp.PrototypeOnsdag;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.thinknick.bscapp.Andet.SendActivity;
import com.example.thinknick.bscapp.R;

import static com.example.thinknick.bscapp.R.id.card_view;

public class mockUpCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_up_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        CardView cW = (CardView) findViewById(card_view);
        cW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Cam = new Intent(mockUpCardActivity.this, CamActivity.class);
                startActivity(Cam);
            }

        });
    }

}
