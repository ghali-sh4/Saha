package com.example.myallproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Load AllRestaurantFragment by default
        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.resturantsContainer, new AllRestaurantFragment())
                    .commit();
        }

        TextView btnLogin = view.findViewById(R.id.tvLogin);

        btnLogin.bringToFront();

        btnLogin.setOnClickListener(v -> openLoginFragment());

        return view;
    }

    private void openLoginFragment() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.replace(R.id.mainActivity, new LoginFragment());
        ft.addToBackStack(null);
        ft.commit();
    }
}
