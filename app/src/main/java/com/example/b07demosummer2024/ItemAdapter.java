package com.example.b07demosummer2024;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * each individual item on the recycler view that is on the main screen
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private final List<Item> itemList;
    private final FragmentManager fragmentManager;
    private Map<String, Boolean> checkedStates;


    /**
     * initializes the adapter
     * @param itemList list of items
     * @param fragmentManager fragment manager of parent
     */
    public ItemAdapter(List<Item> itemList, FragmentManager fragmentManager) {
        this.itemList = itemList;
        this.fragmentManager = fragmentManager;
    }

    /**
     * updates the list while tracking which items are checked
     * @param newList the new list whose contents will be added to itemList
     */
    public void updateList(List<Item> newList) {
        itemList.clear();
        itemList.addAll(newList);

        checkedStates = new HashMap<>();
        for (Item item : itemList) {
            checkedStates.put(String.valueOf(item.getId()), item.isChecked());
        }
        notifyDataSetChanged();
    }

    /**
     * inflates the item card layout
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return the ItemViewHolder
     */
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_layout,
                parent, false);
        return new ItemViewHolder(view);
    }


    /**
     * adds data to the individual item card
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);

        holder.viewCheckBox.setOnCheckedChangeListener(null);

        if (checkedStates != null) {
            Boolean isChecked = checkedStates.get(String.valueOf(item.getId()));
            holder.viewCheckBox.setChecked(isChecked);

        } else {
            holder.viewCheckBox.setChecked(false);
        }

        holder.viewCheckBox.setOnCheckedChangeListener((buttonView, isChecked1) -> {
            if (checkedStates != null) {
                checkedStates.put(String.valueOf(item.getId()), isChecked1);
            }
            item.setChecked(isChecked1);
        });

        holder.arrowButton.setOnClickListener(v -> {
            Fragment newFragment = new ItemDetails(item);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        holder.textViewTitle.setText(item.getTitle());
        holder.textViewPeriod.setText(item.getPeriod());
        holder.textViewCategory.setText(item.getCategory());
        holder.textViewLotNumber.setText(item.getLotNumberString());


        Glide.with(holder.itemView.getContext())
                .load(item.getUrl())
                .into(holder.itemImage);
    }

    /**
     * counts the size of itemList
     * @return the size of itemList
     */
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /**
     * the view holder for each card in the recycler view
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewLotNumber, textViewCategory, textViewPeriod;
        ImageView arrowButton;
        ImageView itemImage;
        CheckBox viewCheckBox;


        /**
         * initializes the item view holder and sets all the view fields
         * @param itemView the item view
         */
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategory = itemView.findViewById(R.id.category);
            textViewPeriod = itemView.findViewById(R.id.period);
            textViewLotNumber = itemView.findViewById(R.id.number);
            textViewTitle = itemView.findViewById(R.id.title);
            arrowButton = itemView.findViewById(R.id.arrow_icon);
            itemImage = itemView.findViewById(R.id.imageViewItemImage);
            viewCheckBox = itemView.findViewById(R.id.view_checkbox);

        }

    }
}
