package com.example.myallproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class RestaurantOrdersFragment extends Fragment {

    private RecyclerView rvOrders;
    private ArrayList<Order> orders;
    private OrdersAdapter adapter;
    private FirebaseServices fbs;
    private String restaurantId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_restaurant_orders, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        restaurantId = getArguments().getString("restaurantId");
        fbs = FirebaseServices.getInstance();

        rvOrders = getView().findViewById(R.id.rvRestaurantOrders);
        orders = new ArrayList<>();
        adapter = new OrdersAdapter(getContext(), orders);
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOrders.setAdapter(adapter);

        fbs.getFire().collection("restaurants")
                .document(restaurantId)
                .collection("orders")
                .get()
                .addOnSuccessListener(snapshot -> {
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        Order order = doc.toObject(Order.class);
                        orders.add(order);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
