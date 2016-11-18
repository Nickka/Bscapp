package com.example.thinknick.bscapp.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.thinknick.bscapp.Activities.RecievedSB;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

/*
OUT OF SERVICE. BLIVER IKKE BRUGT PT.
 */

public class DownloadPicService extends Service {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://bscapp-6b91c.appspot.com/");

    private static final String TAG = "DLService";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String path1;
    private IBinder mBinder = new MyBinder();
    private String path2;
    private File localFile;

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "in onBind");
        return mBinder;
    }

    public DownloadPicService() {
        try {
            getImagePath2();
            Log.d(TAG, "Starter DL");

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public void getImageFromPath() throws IOException {
        StorageReference islandRef = storageRef.child("/userimages/test@test.dk/test@test.dk");
        File directory = new File(Environment.getDataDirectory()
                + "/Test2/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";


        final File localFile = File.createTempFile(imageFileName, ".jpg", directory);
        String path = localFile.getAbsolutePath();

        System.out.println("test" + path);

        try {
            islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Downloaded", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Downloaded til path");
                    String mCurrentPhotoPath = "file:" + localFile.getAbsolutePath();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d(TAG, "Downloaded IKKE til path");
                }
            });
            throw new IOException("Works not");
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }

    }

    public String getImagePath2() throws IOException {
        StorageReference islandRef = storageRef.child("/userimages/test@test.dk/test@test.dk");
        localFile = File.createTempFile("images", ".jpg");
        path1 = localFile.getAbsolutePath();

        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "worked buddy");
                Log.d(TAG, path1);
              }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "stmh went wrong");
            }
        });
       

        return path1;
    }
    public String getPath(){

        return "hej";
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "in onUnbind");
        return true;
    }

    public class MyBinder extends Binder {
        public DownloadPicService getService() {
            return DownloadPicService.this;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "in onDestroy");
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v(TAG, "in onRebind");
        super.onRebind(intent);
    }
}
