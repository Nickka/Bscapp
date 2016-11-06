package com.example.thinknick.bscapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.thinknick.bscapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        View sButton = findViewById(R.id.sButton);

        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (user != null) {
                    Intent Intent2 = new Intent(MainActivity.this, Card_Activity.class);
                    startActivity(Intent2);
                }
                else{
                    Intent Intent2 = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(Intent2);
                }

            }
        });
    }
}
