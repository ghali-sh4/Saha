package com.example.myallproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    private FirebaseServices fbs;
    private ArrayList<Meal> menu;
    private RecyclerView rvMenu;
    private MealAdapter adapter;
    private String restId;
    private String restName;
    private TextView restaurantName;
    private Button btnGoToCart;
    private EditText editPhone;
    private double latitude;
    private double longitude;
    private MapView mapView;
    private GoogleMap googleMap;


    public MenuFragment(String restId, String restName,double latitude,double longitude){
        this.restId=restId;
        this.restName=restName;
        this.latitude=latitude;
        this.longitude = longitude;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FirebaseServices.getInstance();
        menu = new ArrayList<>();
        rvMenu = getView().findViewById(R.id.menuRecyclerView);
        restaurantName = getView().findViewById(R.id.restaurantName);
        restaurantName.setText(restName);
        btnGoToCart = getView().findViewById(R.id.btnGoToCart);

        btnGoToCart.setOnClickListener(v -> {
            CartFragment cartFragment = new CartFragment();
            Bundle bundle = new Bundle();
            bundle.putString("restId", restId); // Pass restaurant ID
            cartFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.mainActivity, cartFragment) // use your actual container ID
                    .addToBackStack(null)
                    .commit();
        });

        rvMenu.setHasFixedSize(true);
        rvMenu.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MealAdapter(getActivity(), menu ,false, meal -> {
            CartManager.addToCart(meal);
            Toast.makeText(getContext(), meal.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
        });
        rvMenu.setAdapter(adapter);

        mapView = getView().findViewById(R.id.mapView);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.getMapAsync(gMap -> {
                googleMap = gMap;

                if (latitude != 0 && longitude != 0) {
                    LatLng location = new LatLng(latitude, longitude); // ✅ FIXED
                    googleMap.addMarker(new MarkerOptions().position(location).title(restName));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                }
            });
        }

        fbs.getFire().collection("restaurants").document(restId).collection("menu").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot dataSnapshot: queryDocumentSnapshots.getDocuments()){
                    Meal meal = dataSnapshot.toObject(Meal.class);

                    menu.add(meal);
                }

                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "No data available", Toast.LENGTH_SHORT).show();
                Log.e("AllRestaurantsFragment", e.getMessage());
            }
        });
    }

    @Override public void onResume() { super.onResume(); if (mapView != null) mapView.onResume(); }
    @Override public void onPause() { super.onPause(); if (mapView != null) mapView.onPause(); }
    @Override public void onDestroy() { super.onDestroy(); if (mapView != null) mapView.onDestroy(); }
    @Override public void onLowMemory() { super.onLowMemory(); if (mapView != null) mapView.onLowMemory(); }

}
