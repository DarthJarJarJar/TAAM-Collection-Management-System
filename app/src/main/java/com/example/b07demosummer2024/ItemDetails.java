package com.example.b07demosummer2024;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;

import java.net.CookieHandler;
import java.net.CookieManager;

public class ItemDetails extends Fragment {
    Item item;
    DatabaseManager manager;

    public ItemDetails(Item item) {
        this.item = item;
        manager = DatabaseManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_details, container, false);

        TextView title = view.findViewById(R.id.textViewTitle);
        TextView category = view.findViewById(R.id.textViewCategory);
        TextView period = view.findViewById(R.id.textViewPeriod);
        TextView description = view.findViewById(R.id.textViewDescription);
        ImageView image = view.findViewById(R.id.imageViewItemImage);

        VideoView video = view.findViewById(R.id.videoViewItem);

        image.setVisibility(View.INVISIBLE);
        video.setVisibility(View.INVISIBLE);



        String imgUrl = item.getUrl();

        if (item.getMediaType().equals("Image")) {
            image.setVisibility(View.VISIBLE);
            video.setVisibility(View.INVISIBLE);
            Glide.with(this)
                    .load(imgUrl)
                    .into(image);
        } else if (item.getMediaType().equals("Video")) {
            video.setVisibility(View.VISIBLE);
            image.setVisibility(View.INVISIBLE);

            video.setVideoPath(imgUrl+".mp4");

            MediaController mediaController = new MediaController(getContext());
            mediaController.setAnchorView(video);
            video.setMediaController(mediaController);

            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    mp.start();
                }
            });
        }

        title.setText(item.getTitle());
        category.setText(item.getCategory());
        period.setText(item.getPeriod());
        description.setText(item.getDescription());

        return view;
    }
}
