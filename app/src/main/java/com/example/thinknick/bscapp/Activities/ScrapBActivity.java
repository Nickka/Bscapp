package com.example.thinknick.bscapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.thinknick.bscapp.Activities.RetiredActivities.SendActivity;
import com.example.thinknick.bscapp.R;
import com.example.thinknick.bscapp.Service.FirebaseService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.thinknick.bscapp.R.id.imageView;
import static com.example.thinknick.bscapp.R.id.imageView2;

/**
 * Created by ThinkNick on 26-10-2016.
 */

public class ScrapBActivity extends Activity {
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    private ImageView mImageView;
    View changeView, button3;
    public EditText editText;
    Bitmap bitmap;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_scrapb);
        changeView = findViewById(R.id.vButton);
        editText = (EditText)findViewById(R.id.editText);
        button3 = findViewById(R.id.button3);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cam = new Intent(ScrapBActivity.this, CameraActivity.class);
                cam.putExtra("test",1);
                startActivityForResult(cam, 1);
            }
        });

        signInAnonymously();

        changeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToFB();
                Toast.makeText(ScrapBActivity.this, "Cool!", Toast.LENGTH_SHORT).show();
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
    private void signInAnonymously() {
      //  showProgressDialog();
        // [START signin_anonymously]
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                      //  Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                          //  Log.w(TAG, "signInAnonymously", task.getException());
                            Toast.makeText(ScrapBActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                       // hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END signin_anonymously]
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
        mImageView.setDrawingCacheEnabled(true);
        mImageView.buildDrawingCache();
        Bitmap bitmap = mImageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        Bundle bundle = new Bundle();
        bundle.putByteArray("bitmap", data);

        Intent intent = new Intent(this, FirebaseService.class);
        intent.putExtra("picture", bundle);
        this.startService(intent);
    }
}
