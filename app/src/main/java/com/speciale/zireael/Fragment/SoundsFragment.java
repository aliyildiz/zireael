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
import com.speciale.zireael.Adapter.SoundsAdapter;
import com.speciale.zireael.Model.SoundInfos;
import com.speciale.zireael.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SoundsFragment extends Fragment {

    String UserID;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView.Adapter adapter;
    List <SoundInfos> list;
    String id;
    DatabaseReference databaseReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (container != null) {
            container.removeAllViews();
        }

        View view  = inflater.inflate(R.layout.sounds_fragment,container,false);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserID = firebaseUser.getUid();

        recyclerView = view.findViewById(R.id.recyclerViewSounds);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new SoundsAdapter();
        recyclerView.setAdapter(adapter);

        list = new ArrayList<>();

        Bundle bundle = getArguments();
        id = bundle.getString("eventID");

        databaseReference = FirebaseDatabase.getInstance()
                .getReference("zireael_Sounds")
                .child(UserID)
                .child(id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnaphot : dataSnapshot.getChildren()){

                    SoundInfos soundInfos = postSnaphot.getValue(SoundInfos.class);
                    list.add(soundInfos);

                }

                adapter = new SoundsAdapter(getContext(), list);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }
}
