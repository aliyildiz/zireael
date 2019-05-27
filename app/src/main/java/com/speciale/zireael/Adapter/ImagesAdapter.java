package com.speciale.zireael.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.speciale.zireael.Fragment.Photo;
import com.speciale.zireael.Fragment.PhotosFragment;
import com.speciale.zireael.Model.ImageInfos;
import com.speciale.zireael.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    private Context context;
    private List<ImageInfos> imageinfoslist;

    public ImagesAdapter(){}


    public ImagesAdapter(Context context, List<ImageInfos> imageinfoslist) {
        this.context = context;
        this.imageinfoslist = imageinfoslist;
    }

    @NonNull
    @Override
    public ImagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photos_item, parent, false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesAdapter.ViewHolder holder, int position) {

        final ImageInfos infos = imageinfoslist.get(position);

        Glide.with(context)
                .load(infos.getImageURL())
                .into(holder.imageview);

        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo fragment = new Photo();
                FragmentTransaction ft = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("photourl",infos.getImageURL());
                fragment.setArguments(bundle);
                ft.replace(R.id.photosFragment, fragment);
                ft.addToBackStack(String.valueOf(PhotosFragment.class));
                ft.commit();
            }
        });

    }

    @Override
    public int getItemCount() {

        return imageinfoslist == null ? 0:imageinfoslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageview = itemView.findViewById(R.id.ImageViewPhotoItem);

        }

    }

}
