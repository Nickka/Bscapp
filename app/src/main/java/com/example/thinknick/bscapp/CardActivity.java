package com.example.thinknick.bscapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.thinknick.bscapp.FullscreenActivity.REQUEST_IMAGE_CAPTURE;
import static com.example.thinknick.bscapp.FullscreenActivity.REQUEST_TAKE_PHOTO;
import static java.security.AccessController.getContext;

/**
 * Created by ThinkNick on 28-10-2016.
 */

public class CardActivity extends Activity {
    Bitmap bmp = null;

    View pButton1, pButton2;
    private String mCurrentPhotoPath;
    private ImageView mImageView, mImageView2;
    private Bitmap mImageBitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        pButton1 = findViewById(R.id.pButton1);
        pButton1.setOnClickListener(onClickListener);
        pButton2 = findViewById(R.id.pButton2);
        pButton2.setOnClickListener(onClickListener);


    }
    public void returnImage() {
        Intent cam = new Intent(CardActivity.this, CameraActivity.class);
        startActivity(cam);
        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //BAD PRACTISE MED SWITCHES, NOTE TIL NICKLAS TO READ UP ON BUT FUCK IT RIGHT NOW
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.pButton1:
                    returnImage();
                    //Bitmap bitmap = BitmapFactory.decodeStream(cam.openFileInput("myImage"));
                    mImageView = (ImageView)findViewById(R.id.imageView);
                    mImageView.setImageBitmap(bmp);
                    mImageView.setRotation(180);
                    mImageView.setVisibility(View.VISIBLE);
                    break;
                //Bitmap ER parceable, men filelimit er 1 mb, gonna fuck up
                case R.id.pButton2:

                    returnImage();
                    //Bitmap bitmap = BitmapFactory.decodeStream(cam.openFileInput("myImage"));
                    mImageView = (ImageView)findViewById(R.id.imageView2);
                    mImageView.setImageBitmap(bmp);
                    mImageView.setRotation(180);
                    mImageView.setVisibility(View.VISIBLE);
                    break;
            }
        }


    };



}
