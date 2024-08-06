package com.example.b07demosummer2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);

        Button buttonRecyclerView = view.findViewById(R.id.buttonRecyclerView);
        Button buttonScroller = view.findViewById(R.id.buttonScroller);
        Button buttonSpinner = view.findViewById(R.id.buttonSpinner);
        Button buttonManageItems = view.findViewById(R.id.buttonManageItems);

        buttonRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new RecyclerViewStaticFragment(), true);
            }
        });

        buttonScroller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ReportFragment(), false);
            }
        });

        buttonSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new AddFragment(), false);
            }
        });

        buttonManageItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loadFragment(new ManageItemsFragment(), false);}
        });

        return view;
    }

    private void loadFragment(Fragment fragment, boolean is_main) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);

        if (is_main) {
            transaction.addToBackStack("main");
        } else {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
