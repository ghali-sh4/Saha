package com.example.myallproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    private RecyclerView rvCart;
    private TextView totalText;
    private CartAdapter adapter;
    private ArrayList<Meal> cartList;
    private EditText editPhone;
    private Button btnBuy;
    private  double total;
    private String restId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            restId = getArguments().getString("restId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        cartList = CartManager.getCartItems();
        rvCart = getView().findViewById(R.id.recyclerCart);
        totalText = getView().findViewById(R.id.textTotal);

        adapter = new CartAdapter(getContext(), cartList);
        rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCart.setAdapter(adapter);
        btnBuy = getView().findViewById(R.id.btnBuy);
        editPhone = getView().findViewById(R.id.editPhone);

        btnBuy.setOnClickListener(v -> {
            buy();
        });


        updateTotal();
    }

    private void updateTotal() {
        total = 0;
        for (Meal meal : cartList) {
            total += meal.getPrice();
        }
        totalText.setText("Total: ₪" + total);
    }

    private void buy(){
        String phone = editPhone.getText().toString().trim();

        if (phone.isEmpty()) {
            Toast.makeText(getContext(), "Please enter your phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cartList.isEmpty()) {
            Toast.makeText(getContext(), "Your cart is empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create order
        Order order = new Order(phone, new ArrayList<>(cartList), total);

        // Send to Firebase
        FirebaseServices.getInstance().getFire()
                .collection("restaurants")
                .document(restId)
                .collection("orders")
                .add(order)
                .addOnSuccessListener(doc -> {
                    Toast.makeText(getContext(), "Order sent to restaurant!", Toast.LENGTH_LONG).show();
                    CartManager.clearCart();
                    cartList.clear();
                    adapter.notifyDataSetChanged();
                    totalText.setText("Total: ₪0");
                    editPhone.setText("");

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.mainActivity, new MainFragment());
                    ft.commit();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to send order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}
