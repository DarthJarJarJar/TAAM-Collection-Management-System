package com.example.b07demosummer2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;

public class ItemDetails extends Fragment {
    Item item;

    public ItemDetails(Item item) {
        this.item = item;
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
        ImageView back_button = view.findViewById(R.id.back_arrow);

        back_button.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        String imgUrl = item.getImageUrl();

        Glide.with(this)
                        .load(imgUrl)
                                .into(image);


        title.setText(item.getTitle());
        category.setText(item.getCategory());
        period.setText(item.getPeriod());
        description.setText(item.getDescription());

        return view;
    }
}
