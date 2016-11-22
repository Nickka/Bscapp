package com.example.thinknick.bscapp.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thinknick.bscapp.R;
import com.example.thinknick.bscapp.Service.CardService;
import com.example.thinknick.bscapp.Service.DownloadPicService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.thinknick.bscapp.Service.DownloadPicService.MyBinder;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class RecievedSB extends AppCompatActivity {
    private LoginActivity.UserLoginTask mAuthTask = null;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "Login";
    private DatabaseReference mPostReference;
    private TextView recievedCardTextView;
    private String path1;
    private ImageView recievedCardImageView;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://bscapp-6b91c.appspot.com/");
    /** Messenger for communicating with the service. */
     DownloadPicService mService;

    /** Flag indicating whether we have called bind on the service. */
    boolean mBound;
    private File localFile;
    private String friendpicpath;
    private String userid;
    private String card;
    private String textpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieved_sb);
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child("YaMbfYZ3IDc19GX9XheMFPIoa8J3");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        recievedCardTextView = (TextView) findViewById(R.id.recievedCardTextView);
        try {
            getImage();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onStart(){
        super.onStart();

        if (mAuthTask != null) {
            return;
        }
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                card = dataSnapshot.child("users").child(userid).child("card").getValue(String.class);

                friendpicpath = dataSnapshot.child("users").child(userid).child("friend").getValue(String.class)+
                        dataSnapshot.child("users").child(userid).child("card").getValue(String.class); // = AkdjkaJLJDAjakljdlad+card
                textpath = dataSnapshot.child("SB").child(userid+card).child("text").getValue(String.class); // = SB / usercard / text
                CardService cid = dataSnapshot.getValue(CardService.class);
                recievedCardTextView.setText(textpath);
                System.out.println(textpath);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mPostReference.addValueEventListener(postListener);

        Intent intent = new Intent(this, DownloadPicService.class);
        this.startService(intent);
        this.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConnection);
    }
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            MyBinder myBinder = (MyBinder) service;
            mService = myBinder.getService();

            mBound = true;
            //getPath();


        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            mBound = false;
        }
    };

    // Eneste måde vi kan få billedet i activityen, der kan ikke bruges en service til dette umiddelbart da eneste måde er at sætte stien til billedet, men stien bliver lavet og givet tilbage til activityen
    // før at billedet er downloadet, hvilket bare giver et tomt element. Så backend kaldet er smidt i denne metode. It works... 6 timer spildt.
    public void getImage() throws IOException{
            StorageReference islandRef = storageRef.child("/userimages/" + friendpicpath + "/" + friendpicpath);
            localFile = File.createTempFile("images", ".jpg");
            path1 = localFile.getAbsolutePath();

            islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "worked buddy");
                    Log.d(TAG, path1);
                    File imgFile = new File(path1);
                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        ImageView myImage = (ImageView) findViewById(R.id.recievedCardImageView);

                        myImage.setImageBitmap(myBitmap);
                    }
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d(TAG, "stmh went wrong");
                }
            });

        }
}
