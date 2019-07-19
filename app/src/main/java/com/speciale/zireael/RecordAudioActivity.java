package com.speciale.zireael;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.speciale.zireael.Model.Event;
import com.speciale.zireael.Model.SoundInfos;

import java.io.File;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;

public class RecordAudioActivity extends AppCompatActivity {

    StorageReference storageReference;
    String filepath, UserID, soundURL;
    SoundInfos soundInfos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageReference = FirebaseStorage.getInstance().getReference();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserID=firebaseUser.getUid();

        filepath = "/sdcard/recorded.wav";
        int color = getResources().getColor(R.color.colorPrimaryDark);
        int requestCode = 0;
        AndroidAudioRecorder.with(this)
                .setFilePath(filepath)
                .setColor(color)
                .setRequestCode(requestCode)
                .record();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0){
            if (resultCode == RESULT_OK){
                Toast.makeText(this, "Everything is allright.", Toast.LENGTH_SHORT).show();
                upload();
            }else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Something gone wrong.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void upload() {

        final Uri uri = Uri.fromFile(new File(filepath));
        final checkTime checkTime = new checkTime();
        String gunEn = checkTime.checkEvent();
        final DatabaseReference databaseReference = FirebaseDatabase
                                                     .getInstance()
                                                     .getReference();
        Query query = databaseReference
                .child("zireael_DB")
                .child(UserID)
                .orderByChild("classDay")
                .equalTo(gunEn);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()){

                    final Event event = postSnapShot.getValue(Event.class);
                    String start = event.getClassStime();
                    String end = event.getClassEtime();
                    boolean test = checkTime.checkTime(start,end);

                    if (test){
                        String EventName = event.getClassName();
                        final StorageReference file = storageReference
                                .child(UserID+"/"+"Sounds"+"/"+EventName)
                                .child("/"+new Date().getTime());
                        file.putFile(uri)
                                .addOnSuccessListener(
                                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                Toast.makeText(RecordAudioActivity.this,
                                        "Uploading Finished",
                                        Toast.LENGTH_SHORT).show();

                                file.getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        soundURL = uri.toString();
                                        soundInfos = new SoundInfos(soundURL);
                                        String eventID = event.getEventID();

                                        DatabaseReference databaseReference1 = FirebaseDatabase
                                                .getInstance()
                                                .getReference("zireael_Sounds"+"/"+UserID);

                                        try {
                                            databaseReference1.child(eventID)
                                                                .push()
                                                                .setValue(soundInfos);
                                        }catch (Exception e){
                                            System.out.println("hata:"+e.getMessage());
                                        }

                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                System.out.println("olmadi");

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });












//
//        StorageReference file = storageReference.child("new_audio"+ new Date().getTime());
//
//        Uri uri = Uri.fromFile(new File(filepath));
//
//        file.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                System.out.println("oldu");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                System.out.println("olmadi");
//            }
//        });




    }
}
