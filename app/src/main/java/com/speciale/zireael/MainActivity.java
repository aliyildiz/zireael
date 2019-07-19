package com.speciale.zireael;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
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
import com.speciale.zireael.Fragment.DersEkleFragment;
import com.speciale.zireael.Fragment.DerslerFragment;
import com.speciale.zireael.Model.Event;
import com.speciale.zireael.Model.ImageInfos;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int CAMERA_REQUEST_CODE = 1;
    FirebaseAuth auth;
    Intent intent;
    StorageReference storageReference;
    Uri photoUri;
    String UserID;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserID = firebaseUser.getUid();
        System.out.println("user:" + firebaseUser.getUid());




        if (savedInstanceState == null) {
            DerslerFragment derslerFragment = new DerslerFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_main, derslerFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {



            //super.onBackPressed();
            System.out.println("geldi");
            getSupportFragmentManager().popBackStack();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);

        return true;
    }

    private void displaySelectedScreen(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.nav_derslerim:
                fragment = new DerslerFragment();
                break;
            case R.id.nav_ders_ekle:
                fragment = new DersEkleFragment();
                break;
            case R.id.nav_fotograf_cek:
                capture();
                break;
            case R.id.nav_ses_kaydi:
                Intent record = new Intent(this, RecordAudioActivity.class);
                startActivity(record);
                break;
            case R.id.nav_logout:
                auth.signOut();
                if (auth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void capture() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        final String dir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Folder/";
        File newdir = new File(dir);
        newdir.mkdirs();
        String file = dir + DateFormat.format("yyyy-MM-dd_hhmmss",
                new Date()).toString() + ".jpg";
        File newfile = new File(file);
        try {
            newfile.createNewFile();
        } catch (IOException e) {
        }

        photoUri = Uri.fromFile(newfile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            final checkTime checkTime = new checkTime();
            String gunEn = checkTime.checkEvent();

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                    .getReference();
            Query query = databaseReference.child("zireael_DB").child(UserID)
                    .orderByChild("classDay").equalTo(gunEn);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (final DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                        final Event event = postSnapShot.getValue(Event.class);

                        String start = event.getClassStime();
                        String end = event.getClassEtime();
                        boolean test = checkTime.checkTime(start, end);

                        if (test) {
                            String EventName = event.getClassName();
                            final StorageReference filepath = storageReference
                                    .child(UserID + "/" + "Photos" + "/" + EventName)
                                    .child("/" + new Date().getTime());
                            filepath.putFile(photoUri)
                                    .addOnSuccessListener(
                                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot
                                                                              taskSnapshot) {

                                                    final DatabaseReference databaseReference1 =
                                                            FirebaseDatabase
                                                            .getInstance()
                                                            .getReference("zireael_Photos"
                                                                    + "/" + UserID);

                                                    filepath.getDownloadUrl().addOnSuccessListener(
                                                            new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            imageUrl = uri.toString();
                                                            ImageInfos imageInfo = new ImageInfos(
                                                                    imageUrl);

                                                            String eventID = event.getEventID();

                                                            databaseReference1.child(eventID).push()
                                                                    .setValue(imageInfo);
                                                        }
                                                    });

                                                    Toast.makeText(MainActivity.this,
                                                            "Uploading Finished",
                                                            Toast.LENGTH_SHORT).show();

                                                }
                                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

}
