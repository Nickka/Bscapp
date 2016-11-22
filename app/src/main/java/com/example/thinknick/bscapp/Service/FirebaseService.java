package com.example.thinknick.bscapp.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FirebaseService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.thinknick.bscapp.Service.action.FOO";
    private static final String ACTION_BAZ = "com.example.thinknick.bscapp.Service.action.BAZ";
 private String uid;
    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.thinknick.bscapp.Service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.thinknick.bscapp.Service.extra.PARAM2";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://bscapp-6b91c.appspot.com/");

    Bundle extras;
    private static final String TAG = "Login";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userid;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private String text1;
    private String card;
    private String emailid;
    private DatabaseReference mPostReference;


    public FirebaseService() {
        super("FirebaseService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, FirebaseService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, FirebaseService.class);

        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle bundle = intent.getExtras();
        String id = bundle.getString("FBservice");
        text1 = bundle.getString("FBServiceTxt");


        if (intent != null) {
            final String action = intent.getAction();
            if (id.equals("DL"))  {
                try {
                    getImageFromPath();
                    Log.d(TAG, "Starter DL");

                }
                catch(IOException e)
                {
                    System.out.println(e.getMessage());
                }
            } else if (id.equals("UL")) {
                getUser();
                upload2Firebase2();


            }
        }
    }




    public void upload2Firebase2() {
        StorageReference imageRef = storageRef.child("userimages/" + userid+card + "/" + userid+card);

        try {
            Bitmap bitmap = BitmapFactory.decodeStream(this.openFileInput("myImage"));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = imageRef.putBytes(data);
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
                    Toast.makeText(FirebaseService.this, "Card uploaded!", Toast.LENGTH_SHORT).show();
                }
            });

            String imgPath = imageRef.toString();

            mDatabase.child("SB").child(userid+card).child("Bruger").setValue(userid);
            mDatabase.child("SB").child(userid+card).child("picturepath").setValue(imgPath);
            mDatabase.child("SB").child(userid+card).child("text").setValue(text1);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    public void getUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                card = dataSnapshot.child("users").child(userid).child("card").getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                 emailid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
            }
        }

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
