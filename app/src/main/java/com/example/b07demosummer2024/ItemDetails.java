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

/**
 * view representing the expanded screen of a given item with all its details
 */
public class ItemDetails extends Fragment {

  Item item;
  DatabaseManager manager;

  /**
   * constructor for this view
   *
   * @param item the item whose details will be displayed
   */
  public ItemDetails(Item item) {
    this.item = item;
    manager = DatabaseManager.getInstance();
  }

  /**
   * sets all the fields of the item inside the view, and displays image or video according to
   * whatever the current item has
   *
   * @param inflater           The LayoutInflater object that can be used to inflate any views in
   *                           the fragment,
   * @param container          If non-null, this is the parent view that the fragment's UI should be
   *                           attached to.  The fragment should not add the view itself, but this
   *                           can be used to generate the LayoutParams of the view.
   * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
   *                           saved state as given here.
   * @return the created view
   */
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
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

      video.setVideoPath(imgUrl + ".mp4");

      MediaController mediaController = new MediaController(getContext(), false);
      mediaController.setAnchorView(video);
      video.setMediaController(null);

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
