package com.speciale.zireael.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.speciale.zireael.Model.Event;
import com.speciale.zireael.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class DerslerFragment extends Fragment{


    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    private FirebaseRecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    public static final String event_ID = "com.speciale.zireael.Fragment.eventid";
    FragmentManager fragmentManager;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Dersler");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        View view = inflater.inflate(R.layout.dersler_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);


        fetch();

        return view;
    }



    private void fetch() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        System.out.println("user:"+firebaseUser.getUid());

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("zireael_DB"+"/"+firebaseUser.getUid());


        FirebaseRecyclerOptions<Event> options = new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(query, new SnapshotParser<Event>() {
                    @NonNull
                    @Override
                    public Event parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new Event(
                                snapshot.child("eventID").getValue().toString(),
                                snapshot.child("className").getValue().toString(),
                                snapshot.child("classDay").getValue().toString(),
                                snapshot.child("classStime").getValue().toString(),
                                snapshot.child("classEtime").getValue().toString());
                    }
                })
                .build();
        adapter = new FirebaseRecyclerAdapter <Event, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_layout_item,parent,false);


                return new ViewHolder(view1);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i,
                                            @NonNull final Event event) {

                viewHolder.setTextView1(event.getClassName());
                viewHolder.setTextView2(event.getClassDay());
                viewHolder.setStartTimeText(event.getClassStime());
                viewHolder.setEndTimeText(event.getClassEtime());



                viewHolder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), String.valueOf(i), Toast.LENGTH_LONG).show();

                        String id = event.getEventID();
                        Bundle bundle =new Bundle();
                        bundle.putString("eventid",id);

                        DetailsFragment fragment = new DetailsFragment();
                        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                        fragment.setArguments(bundle);
                        ft.replace(R.id.derslerFragment, fragment);
                        ft.addToBackStack(null);
                        ft.commit();

                    }


                });

            }

            };
        recyclerView.setAdapter(adapter);
        }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        }




    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout root;
        public TextView textView1, textView2, startTimeText, endTimeText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_root);
            textView1 = itemView.findViewById(R.id.text1);
            textView2 = itemView.findViewById(R.id.text2);
            startTimeText = itemView.findViewById(R.id.startTimeText);
            endTimeText = itemView.findViewById(R.id.endTimeText);
        }

        public void setTextView1(String string){
            textView1.setText(string);
        }

        public void setTextView2(String string){
            textView2.setText(string);
        }

        public void setStartTimeText(String string){
            String saat, dakika;
            saat = string.substring(0,2);
            dakika = string.substring(2);
            startTimeText.setText(saat+":"+dakika);
        }

        public void setEndTimeText(String string){
            String saat, dakika;
            saat = string.substring(0,2);
            dakika = string.substring(2);
            endTimeText.setText(saat+":"+dakika);
        }

    }




}


