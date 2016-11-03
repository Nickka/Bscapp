package com.example.thinknick.bscapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.thinknick.bscapp.Activities.RetiredActivities.SendActivity;
import com.example.thinknick.bscapp.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        changeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SMessage = new Intent(ScrapBActivity.this, SendActivity.class);
                startActivity(SMessage);
            }
        });
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

}
