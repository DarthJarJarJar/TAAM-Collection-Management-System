package com.example.b07demosummer2024;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemList;
    private FragmentManager fragmentManager;

    public ItemAdapter(List<Item> itemList, FragmentManager fragmentManager) {
        this.itemList = itemList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_adapater, parent, false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);

        holder.arrowButton.setOnClickListener(v -> {
            Fragment newFragment = new ItemDetails(item, fragmentManager);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        holder.textViewTitle.setText(item.getTitle());
        holder.textViewPeriod.setText(item.getPeriod());
        holder.textViewCategory.setText(item.getCategory());
        holder.textViewLotNumber.setText(item.getId());
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewLotNumber, textViewCategory, textViewPeriod;
        ImageView arrowButton;
        ImageView itemImage;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategory = itemView.findViewById(R.id.category);
            textViewPeriod = itemView.findViewById(R.id.period);
            textViewLotNumber = itemView.findViewById(R.id.number);
            textViewTitle = itemView.findViewById(R.id.title);
            arrowButton = itemView.findViewById(R.id.arrow_icon);
            itemImage = itemView.findViewById(R.id.imageViewItemImage);


        }


    }
}
