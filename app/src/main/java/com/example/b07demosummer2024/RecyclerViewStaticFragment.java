package com.example.b07demosummer2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewStaticFragment extends Fragment {
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        itemList = new ArrayList<>();
        loadStaticItems();
        itemAdapter = new ItemAdapter(itemList, getParentFragmentManager());
        recyclerView.setAdapter(itemAdapter);

        return view;
    }

    private void loadStaticItems() {
        // Load static items from strings.xml or hardcoded values
        itemList.add(new Item("#1", "A Tang 'Sancai' 'Baoxianghua' Box", "Box", "Tang Dynasty", "This tricolor (sancai) box has a round shape with shallow straight walls,\n" +
                "a concave circular mouth, a flat base, and a slightly curved lid. The\n" +
                "outer surface of the lid is decorated with intricate molded patterns,\n" +
                "displaying exquisite and varied designs. The exterior of the box is\n" +
                "covered in tricolor glazes, including green, yellow, white, and blue. The\n" +
                "interior of the box and the base are covered in yellow glaze. The entire\n" +
                "piece is adorned with fine crackle patterns.\n" +
                "During the mid-Tang Dynasty, the tricolor glazing technique reached its\n" +
                "peak, resulting in a wide variety of vessel forms and refined\n" +
                "craftsmanship. The glaze colors during this period were lustrous and the\n" +
                "coloring appeared natural. Vessels were often fully glazed both on the\n" +
                "inside and outside, utilizing colors such as green, yellow, white, blue,\n" +
                "and black, creating a complex and diverse palette. The production\n" +
                "process involved applying a base layer of slip before adding various\n" +
                "colored glazes to achieve the desired overall effect in terms of both form\n" +
                "and decoration. The decoration techniques included carving, stamping,\n" +
                "appliqué, and modeling. These tricolor artifacts showcased rich content\n" +
                "and were considered exquisite examples of Tang tricolor ware."));
        itemList.add(new Item("#2", "A Blue-Glazed Ceramic 'Tiger'", "Tiger", "Three Kingdoms Period", "This blue-glazed ceramic tiger was crafted during the Three\n" +
                "Kingdoms Period and originates from the Yue Kiln. It features a\n" +
                "rounded and well-proportioned form with a slanting neck and\n" +
                "circular mouth. The abdomen showcases a bow-shaped handle\n" +
                "resembling a tiger in motion, with the tiger's head tilted upwards at a\n" +
                "45-degree angle and a round open mouth. The lower abdomen is\n" +
                "sculpted to depict contracted limbs. The handle behind the tiger's\n" +
                "head is also adorned with delicate lines. The entire piece is covered in\n" +
                "a light blue glaze.\n" +
                "Another similar piece with the same design measures 15.7cm in\n" +
                "height, 20.9cm in length, and has a mouth diameter of 4.8cm. This\n" +
                "tiger was unearthed in 1955 in Zhao Shigang, Nanjing, Jiangsu\n" +
                "Province. In August 2013, it was listed in the \"Third Batch of\n" +
                "Cultural Relics Prohibited from Going Abroad (Abroad) for\n" +
                "Exhibition\" by the National Cultural Heritage Administration.\n" +
                "Inscribed on its lower abdomen are thirteen characters: \"Crafted by\n" +
                "Master Yuan Yi from Shangyu, Kuaiji, in the 14th year of Chiwu.\n" +
                "\"\n" +
                "This piece represents one of the earliest known ceramics with an\n" +
                "accurate dating inscription discovered through archaeological\n" +
                "excavation."));
    }
}
