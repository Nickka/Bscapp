package com.example.thinknick.bscapp.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.le.ScanRecord;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.thinknick.bscapp.R;
import com.example.thinknick.bscapp.Service.FirebaseService;
import com.example.thinknick.bscapp.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;

import static com.example.thinknick.bscapp.R.id.imageView2;

/**
 * Created by ThinkNick on 26-10-2016.
 */

public class ScrapBActivity extends AppCompatActivity {
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    private Bitmap mImageBitmap;
    private String uid;
    private String text;
    private ImageView mImageView;
    View changeView;
    Button takePic;
    public EditText editText;
    Bitmap bitmap;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG;
    private View mProgressView;
    private IntentFilter filter1;

    public ScrapBActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_scrapb);
        changeView = findViewById(R.id.vButton);
        editText = (EditText)findViewById(R.id.editText);
        takePic = (Button) findViewById(R.id.button3);
        mProgressView = findViewById(R.id.sb_progress);

        filter1 = new IntentFilter("com.example.thinknick.bscapp.Service");
        registerReceiver(myReceiver, filter1);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });
        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cam = new Intent(ScrapBActivity.this, CameraActivity.class);
                cam.putExtra("test",1);
                startActivityForResult(cam, 1);
            }
        });

        //send til firebase
        changeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = editText.getText().toString();

                sendToFB();
                getUser();
                putTextIntoDatabase();

            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                   // Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                  //  Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }
    private void putTextIntoDatabase() {
        FirebaseUser fBUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = fBUser.getUid();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void returnImage2() {

        try {
            bitmap = BitmapFactory.decodeStream(this.openFileInput("myImage"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case (1): {
                try {
                    returnImage2();
                    if (bitmap != null) {
                        mImageView = (ImageView) findViewById(imageView2);
                        mImageView.setImageBitmap(bitmap);
                        mImageView.setRotation(90);
                        takePic.setText("TAG NYT BILLED");
                    }
                    else {
                        return;
                    }
                }
                catch (NullPointerException e){
                    recreate();
                    Toast.makeText(this, "Husk at tage et billed", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }
    public void sendToFB(){

        try {
            if(bitmap != null) {
                if(!text.equals("Beskriv dit billede")) {
                    Intent intent = new Intent(this, FirebaseService.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("FBservice", "UL");
                    mBundle.putString("FBServiceTxt", text);
                    intent.putExtras(mBundle);
                    this.startService(intent);
                    showProgress(true);
                    Toast.makeText(this, "Sammensætter og sender kort!", Toast.LENGTH_SHORT).show();
                    mImageView.setVisibility(View.INVISIBLE);
                }
                else{
                    Toast.makeText(this, "Husk at skrive en beskrivelse.", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Husk at tage et billed!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (NullPointerException e){

        }
    }
    public void getUser(){

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            editText.setVisibility(show ? View.GONE : View.VISIBLE);
            editText.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    editText.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            takePic.setVisibility(show ? View.GONE : View.VISIBLE);
            takePic.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    takePic.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            changeView.setVisibility(show ? View.GONE : View.VISIBLE);
            changeView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    changeView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equalsIgnoreCase("com.example.thinknick.bscapp.Service")) {

                Intent goback = new Intent(ScrapBActivity.this, Card_Activity.class);
                startActivity(goback);
                Toast.makeText(ScrapBActivity.this, "Beskeden blev afsendt!", Toast.LENGTH_SHORT).show();

            }
        }

        };
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }


}
