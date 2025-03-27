package com.example.myallproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ResturantOwnerHomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Load AllRestaurantFragment by default
        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.ownerRestuarantsContainer, new AllMyRestaurantFragment())
                    .commit();
        }

        TextView btnaddRes = view.findViewById(R.id.tvAddRestuarant);

        btnaddRes.bringToFront();

        btnaddRes.setOnClickListener(v -> openAddRestaurantFragment());

        return view;
    }

    private void openAddRestaurantFragment() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.replace(R.id.mainActivity, new AddRestaurantFragment());
        ft.commit();
    }
}
