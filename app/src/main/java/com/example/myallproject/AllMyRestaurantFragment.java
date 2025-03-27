package com.example.myallproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllMyRestaurantFragment extends Fragment {

    private RecyclerView rvMyRestaurants;
    private ArrayList<Resturant> myRestaurants;
    private OwnerRestaurantAdapter adapter;
    private FirebaseServices fbs;
    private String currentOwnerId ; // Replace with real ID or pass via arguments

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_my_restaurants, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FirebaseServices.getInstance();
        currentOwnerId =fbs.getAuth().getUid().toString();
        rvMyRestaurants = getView().findViewById(R.id.rvMyRestaurants);
        rvMyRestaurants.setLayoutManager(new LinearLayoutManager(getContext()));
        myRestaurants = new ArrayList<>();
        adapter = new OwnerRestaurantAdapter(getContext(), myRestaurants, restaurant -> {
            RestaurantMenuFragment menuFragment = new RestaurantMenuFragment();

            Bundle bundle = new Bundle();
            bundle.putString("restaurantId", restaurant.getId());
            bundle.putString("restaurantName", restaurant.getName());
            menuFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.ownerRestuarantsContainer, menuFragment)
                    .addToBackStack(null)
                    .commit();
        });
        rvMyRestaurants.setAdapter(adapter);

        // Fetch restaurants where ownerId matches
        fbs.getFire().collection("restaurants")
                .whereEqualTo("oid", currentOwnerId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Resturant restaurant = doc.toObject(Resturant.class);
                        restaurant.setId(doc.getId()); // Optional
                        myRestaurants.add(restaurant);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load restaurants", Toast.LENGTH_SHORT).show();
                });
    }
}
