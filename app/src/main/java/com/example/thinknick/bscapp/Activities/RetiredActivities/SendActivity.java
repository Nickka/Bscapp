package com.example.thinknick.bscapp.Activities.RetiredActivities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.thinknick.bscapp.Activities.MainActivity;
import com.example.thinknick.bscapp.R;

/**
 * Created by ThinkNick on 26-10-2016.
 */

public class SendActivity extends Activity implements View.OnClickListener{
    private View sButton;
    CheckBox checkBox, checkBox2, checkBox3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_send);
        sButton = findViewById(R.id.Sbutton);

        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent2 = new Intent(SendActivity.this, MainActivity.class);
                startActivity(Intent2);            }
        });

        checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox.setOnClickListener(this);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox2.setOnClickListener(this);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        checkBox3.setOnClickListener(this);

    }

        @Override
        public void onClick(View v) {
            String name1 = ((CheckBox) findViewById(R.id.checkBox)).getText().toString();
            String name2 = ((CheckBox) findViewById(R.id.checkBox2)).getText().toString();
            String name3 = ((CheckBox) findViewById(R.id.checkBox3)).getText().toString();
            switch (v.getId()) {
                case R.id.checkBox:
                    if (checkBox.isChecked()){
                        checkBox.setTypeface(null, Typeface.BOLD);
                    Toast.makeText(SendActivity.this, name1 + " checked", Toast.LENGTH_LONG).show();
                    sButton.setVisibility(View.VISIBLE);

            }
                    break;

                case R.id.checkBox2:
                    if (checkBox2.isChecked()) {
                        checkBox2.setTypeface(null, Typeface.BOLD);
                        Toast.makeText(SendActivity.this, name2 + " checked", Toast.LENGTH_LONG).show();
                        sButton.setVisibility(View.VISIBLE);
                    }
                    break;

                case R.id.checkBox3:

                    if (checkBox3.isChecked()) {
                        checkBox3.setTypeface(null, Typeface.BOLD);

                        Toast.makeText(SendActivity.this, name3 + " checked", Toast.LENGTH_LONG).show();
                        sButton.setVisibility(View.VISIBLE);

                    }
                    break;

            }
        }










}
