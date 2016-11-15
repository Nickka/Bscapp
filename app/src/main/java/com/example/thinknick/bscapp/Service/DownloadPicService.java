package com.example.thinknick.bscapp.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

public class DownloadPicService extends Service {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://bscapp-6b91c.appspot.com/");

    Bundle extras;
    private static final String TAG = "Login";
    private FirebaseAuth.AuthStateListener mAuthListener;
    public DownloadPicService() {
        try {
            getImageFromPath();
            Log.d(TAG, "Starter DL");

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void getImageFromPath() throws IOException {
        StorageReference islandRef = storageRef.child("userimages/test@test.dk/test@test.dk");

        File localFile = File.createTempFile("images", "jpg");
        try {
            islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Downloaded", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Downloaded til path");

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
}
