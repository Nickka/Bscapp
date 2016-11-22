package com.example.thinknick.bscapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
    View changeView, takePic;
    public EditText editText;
    Bitmap bitmap;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_scrapb);
        changeView = findViewById(R.id.vButton);
        editText = (EditText)findViewById(R.id.editText);
        takePic = findViewById(R.id.button3);
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
                returnImage2();
                mImageView = (ImageView) findViewById(imageView2);
                mImageView.setImageBitmap(bitmap);
                mImageView.setRotation(180);
            }
            break;
        }
    }
    public void sendToFB(){

        Intent intent = new Intent(this, FirebaseService.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("FBservice", "UL");
        mBundle.putString("FBServiceTxt", text);
        intent.putExtras(mBundle);
        this.startService(intent);
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
}
