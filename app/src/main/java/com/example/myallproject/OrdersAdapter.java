package com.example.myallproject;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private Context context;
    private ArrayList<Order> orders;

    public OrdersAdapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvPhone.setText("Phone: " + order.getPhoneNumber());
        holder.tvTotal.setText("Total: ₪" + order.getTotal());

        StringBuilder items = new StringBuilder();
        for (Meal meal : order.getItems()) {
            items.append("- ").append(meal.getName()).append(" (₪")
                    .append(meal.getPrice()).append(")\n");
        }
        holder.tvItems.setText(items.toString());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvPhone, tvTotal, tvItems;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPhone = itemView.findViewById(R.id.tvOrderPhone);
            tvTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvItems = itemView.findViewById(R.id.tvOrderItems);
        }
    }
}