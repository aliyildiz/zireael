package com.speciale.zireael.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.speciale.zireael.Model.SoundInfos;
import com.speciale.zireael.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SoundsAdapter extends RecyclerView.Adapter<SoundsAdapter.ViewHolder> {

    private Context context;
    private List<SoundInfos> soundInfosList;
    MediaPlayer mediaPlayer;
    int length;

    public SoundsAdapter() {
    }

    public SoundsAdapter(Context context, List<SoundInfos> soundInfosList) {
        this.context = context;
        this.soundInfosList = soundInfosList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sounds_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final SoundInfos soundInfos = soundInfosList.get(position);

        int pozisyon = position + 1;
        holder.textView.setText("Ses Kaydı " + pozisyon);


        final boolean[] isClicked = {false};
        final boolean[] isPaused = {false};

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isClicked[0]) {
                    isClicked[0] = !isClicked[0];

                    holder.imageView.setImageResource(R.mipmap.ic_pause);


                    if (isPaused[0]) {
                        mediaPlayer.seekTo(length);
                        mediaPlayer.start();
                        isPaused[0] = !isPaused[0];
                    } else {

                        mediaPlayer = new MediaPlayer();

                        try {

                            mediaPlayer.setDataSource(soundInfos.getSoundURL());
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mp.start();
                                }
                            });

                            mediaPlayer.prepare();
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    holder.imageView.setImageResource(R.mipmap.ic_play);
                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                } else {
                    isClicked[0] = !isClicked[0];
                    isPaused[0] = true;
                    mediaPlayer.pause();
                    length = mediaPlayer.getCurrentPosition();
                    holder.imageView.setImageResource(R.mipmap.ic_play);
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return soundInfosList == null ? 0 : soundInfosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        TextView textView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.soundItemButton);
            textView = itemView.findViewById(R.id.soundText);


        }
    }
}
