package com.speciale.zireael.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jkb.fragment.rigger.annotation.Puppet;
import com.jkb.fragment.rigger.rigger.Rigger;
import com.speciale.zireael.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

@Puppet
public class DetailsFragment extends Fragment {


    public void onRiggerBackPressed(){
        Rigger.getRigger(this).onBackPressed();
        //if you nedd intercept onBackPressed method,do not write the above code.
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        Bundle bundle = getArguments();

        String id = bundle.getString("eventid");
        String deneme = bundle.getString("key");
        System.out.println(deneme);
        System.out.println("idmiz:" + id);

        final Bundle bundle1 = new Bundle();
        bundle1.putString("eventID", id);

        View view = inflater.inflate(R.layout.details_fragment, container, false);

        TextView photoText = view.findViewById(R.id.photosDetails);
        TextView soundText = view.findViewById(R.id.soundsDetails);




        photoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhotosFragment fragment = new PhotosFragment();
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                fragment.setArguments(bundle1);
                ft.replace(R.id.detailsFragment, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        soundText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundsFragment fragment = new SoundsFragment();
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                fragment.setArguments(bundle1);
                ft.replace(R.id.detailsFragment, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }



}
