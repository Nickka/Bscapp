package com.example.thinknick.bscapp.Activities.RetiredActivities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.thinknick.bscapp.Activities.CameraActivity;
import com.example.thinknick.bscapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import static com.example.thinknick.bscapp.R.id.imageView;

/**
 * Created by ThinkNick on 28-10-2016.
 */



public class OldCardActivity extends Activity {
    Bitmap bmp = null;
    Bitmap bmp2;
    Bitmap bitmap;

    Button sButton, sButton2;
    View pButton1, pButton2, sButton3;
    private String mCurrentPhotoPath;
    private ImageView mImageView, mImageView2;
    private Bitmap mImageBitmap;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://bscapp-f1436.appspot.com/pics/");
    StorageReference mountainsRef = storageRef.child("mountains.jpg");
    StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_activity_card);
        pButton1 = findViewById(R.id.pButton1);
        pButton1.setOnClickListener(onClickListener);
        pButton2 = findViewById(R.id.pButton2);
        pButton2.setOnClickListener(onClickListener);
        sButton = (Button) findViewById(R.id.vButton);
        sButton.setOnClickListener(onClickListener);
        sButton2 = (Button) findViewById(R.id.button2);
        sButton2.setOnClickListener(onClickListener);
        sButton3 = findViewById(R.id.button2);
        sButton3.setOnClickListener(onClickListener);
        //Bundle extras = getIntent().getExtras();
        //byte[] byteArray = extras.getByteArray("bitmapbytes");
        //bmp2 = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);


    }


    public void returnImage() {
        Intent cam = new Intent(this, CameraActivity.class);
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
                mImageView = (ImageView) findViewById(imageView);
                mImageView.setImageBitmap(bitmap);
                mImageView.setRotation(180);
            }
            break;

            case (2): {
                returnImage2();
                mImageView = (ImageView) findViewById(R.id.imageView2);
                mImageView.setImageBitmap(bitmap);
                mImageView.setRotation(180);
            }
            break;
        }
    }
    //BAD PRACTISE MED SWITCHES, NOTE TIL NICKLAS TO READ UP ON BUT FUCK IT RIGHT NOW
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.pButton1:
                        Intent cam = new Intent(OldCardActivity.this, CameraActivity.class);
                        cam.putExtra("test",1);
                    startActivityForResult(cam, 1);

                        break;
                //Bitmap ER parceable, men filelimit er 1 mb, gonna fuck up
                case R.id.pButton2:
                    //Intent cam2 = new Intent(OldCardActivity.this, CameraActivity.class);
                    //cam2.putExtra("test",2);
                    //startActivityForResult(cam2, 2);
                    System.out.println("ok");
                    UploadFirebase();
                    break;
                case R.id.sButton:

                    break;
            }
        }


    };

    private void UploadFirebase(){
// Get the data from an ImageView as bytes
        mImageView.setDrawingCacheEnabled(true);
        mImageView.buildDrawingCache();
        Bitmap bitmap = mImageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("not ok");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("helt ok");
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });

    }
}