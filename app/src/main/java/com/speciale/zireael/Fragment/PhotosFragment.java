package com.speciale.zireael.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.speciale.zireael.Adapter.ImagesAdapter;
import com.speciale.zireael.Model.ImageInfos;
import com.speciale.zireael.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PhotosFragment extends Fragment {


    String UserID;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    List<ImageInfos> list;
    RecyclerView.Adapter adapter;
    String id;
    LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        View view = inflater.inflate(R.layout.photos_fragment, container, false);


        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserID = firebaseUser.getUid();

        recyclerView = view.findViewById(R.id.recyclerViewPhotos);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ImagesAdapter();
        recyclerView.setAdapter(adapter);

        list = new ArrayList<>();

        Bundle bundle = getArguments();
        id = bundle.getString("eventID");

        databaseReference = FirebaseDatabase.getInstance()
                .getReference("zireael_Photos")
                .child(UserID)
                .child(id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    ImageInfos imageinfo = postSnapshot.getValue(ImageInfos.class);
                    list.add(imageinfo);


//                    for (DataSnapshot snapshot : postSnaphot.getChildren()){
//                        //alttaki oldu link geliyor!!!!!!!!!!
//                        System.out.println("imageden:"+snapshot.getValue());
////                        System.out.println("imagedene:"+ snapshot.getValue(ImageInfos.class).getClass());
////                        ImageInfos imageinfo = snapshot.getValue(ImageInfos.class);
//                        //System.out.println("imagedeneme:"+imageinfo);
//                        list.add(snapshot.getValue(ImageInfos.class));
//
//                        //list.add(imageinfo);
//                    }

                    //alttaki oldu link geliyor!!!!!!!!!!
//                    System.out.println("imageden:"+postSnaphot.child("imageURL").getValue());
//                    ImageInfos imageinfo = postSnaphot.child("imageURL").getValue(ImageInfos.class);
//                    System.out.println("imagedeneme:"+imageinfo);
//                    list.add(imageinfo);
                }

                adapter = new ImagesAdapter(getContext(), list);

                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        return view;

    }







}
