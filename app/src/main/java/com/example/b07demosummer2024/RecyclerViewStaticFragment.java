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
        itemList.add(new Item("#1", "A Tang 'Sancai' 'Baoxianghua' Box", "Box", "Tang Dynasty", "This tricolor (sancai) box has a round shape with shallow straight walls, a concave circular mouth, a flat base, and a slightly curved lid. The outer surface of the lid is decorated with intricate molded patterns, displaying exquisite and varied designs. The exterior of the box is covered in tricolor glazes, including green, yellow, white, and blue. The interior of the box and the base are covered in yellow glaze. The entire piece is adorned with fine crackle patterns. During the mid-Tang Dynasty, the tricolor glazing technique reached its peak, resulting in a wide variety of vessel forms and refined craftsmanship. The glaze colors during this period were lustrous and the coloring appeared natural. Vessels were often fully glazed both on the inside and outside, utilizing colors such as green, yellow, white, blue, and black, creating a complex and diverse palette. The production process involved applying a base layer of slip before adding various colored glazes to achieve the desired overall effect in terms of both form and decoration. The decoration techniques included carving, stamping, appliqué, and modeling. These tricolor artifacts showcased rich content and were considered exquisite examples of Tang tricolor ware.", "https://media.discordapp.net/attachments/1254846354245685400/1267987778533064725/Screenshot_2024-07-30_at_7.30.39_PM.png?ex=66aac924&is=66a977a4&hm=351e4a6dc7f370a15cd1de991e6f4574faba4880a53d9a618e70a2dcbf9c283f&=&format=webp&quality=lossless&width=828&height=644"));
        itemList.add(new Item("#2", "A Blue-Glazed Ceramic 'Tiger'", "Tiger", "Three Kingdoms Period", "This blue-glazed ceramic tiger was crafted during the Three Kingdoms Period and originates from the Yue Kiln. It features a rounded and well-proportioned form with a slanting neck and circular mouth. The abdomen showcases a bow-shaped handle resembling a tiger in motion, with the tiger's head tilted upwards at a 45-degree angle and a round open mouth. The lower abdomen is sculpted to depict contracted limbs. The handle behind the tiger's head is also adorned with delicate lines. The entire piece is covered in a light blue glaze. Another similar piece with the same design measures 15.7cm in height, 20.9cm in length, and has a mouth diameter of 4.8cm. This tiger was unearthed in 1955 in Zhao Shigang, Nanjing, Jiangsu Province. In August 2013, it was listed in the \"Third Batch of Cultural Relics Prohibited from Going Abroad (Abroad) for Exhibition\" by the National Cultural Heritage Administration. Inscribed on its lower abdomen are thirteen characters: \"Crafted by Master Yuan Yi from Shangyu, Kuaiji, in the 14th year of Chiwu.\" This piece represents one of the earliest known ceramics with an accurate dating inscription discovered through archaeological excavation.", "https://media.discordapp.net/attachments/1254846354245685400/1268049986285994004/Screenshot_2024-07-30_at_11.37.51_PM.png?ex=66ab0313&is=66a9b193&hm=3397910bf664044c4b9dcc367813f6b68caf78529e1b46d59e210b78cda55f77&=&format=webp&quality=lossless&width=832&height=676"));
    }
}
