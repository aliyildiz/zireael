package com.speciale.zireael.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.speciale.zireael.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Photo extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }

        View view = inflater.inflate(R.layout.photo, container, false);

        ImageView imageview =  view.findViewById(R.id.photo);

        Bundle bundle = getArguments();
        String url = bundle.getString("photourl");

        Glide.with(getContext())
                .load(url)
                .into(imageview);

        return view;
    }

}
