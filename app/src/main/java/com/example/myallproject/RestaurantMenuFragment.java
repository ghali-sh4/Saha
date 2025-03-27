package com.example.myallproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class RestaurantMenuFragment extends Fragment {

    private String restaurantId, restaurantName;
    private TextView title;
    private RecyclerView rvMeals;
    private ArrayList<Meal> meals;
    private MealAdapter adapter;
    private FirebaseServices fbs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_restaurant_menu, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FirebaseServices.getInstance();

        restaurantId = getArguments().getString("restaurantId");
        restaurantName = getArguments().getString("restaurantName");

        title = getView().findViewById(R.id.tvRestaurantMenuTitle);
        rvMeals = getView().findViewById(R.id.rvRestaurantMeals);
        title.setText("Menu: " + restaurantName);

        meals = new ArrayList<>();
        adapter = new MealAdapter(getContext(), meals,true, m -> {});
        rvMeals.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMeals.setAdapter(adapter);

        fbs.getFire()
                .collection("restaurants")
                .document(restaurantId)
                .collection("menu")
                .get()
                .addOnSuccessListener(snapshot -> {
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        Meal meal = doc.toObject(Meal.class);
                        meals.add(meal);
                    }
                    adapter.notifyDataSetChanged();
                });

        // Handle "Add Meal" button
        Button btnAddMeal = getView().findViewById(R.id.btnAddMeal);
        btnAddMeal.setOnClickListener(v -> {
            AddMealFragment addMealFragment = new AddMealFragment();
            Bundle bundle = new Bundle();
            bundle.putString("restaurantId", restaurantId);
            addMealFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.ownerRestuarantsContainer, addMealFragment)
                    .addToBackStack(null)
                    .commit();
        });

        Button btnViewOrders = getView().findViewById(R.id.btnViewOrders);

        btnViewOrders.setOnClickListener(v -> {
            RestaurantOrdersFragment ordersFragment = new RestaurantOrdersFragment();

            Bundle bundle = new Bundle();
            bundle.putString("restaurantId", restaurantId);
            ordersFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.ownerRestuarantsContainer, ordersFragment)
                    .addToBackStack(null)
                    .commit();
        });

    }
}
