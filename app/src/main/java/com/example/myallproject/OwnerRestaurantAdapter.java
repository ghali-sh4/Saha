package com.example.myallproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class OwnerRestaurantAdapter extends RecyclerView.Adapter<OwnerRestaurantAdapter.RestaurantViewHolder> {

    private Context context;
    private ArrayList<Resturant> restaurantList;
    private OnRestaurantClickListener listener;

    public OwnerRestaurantAdapter(Context context, ArrayList<Resturant> restaurantList, OnRestaurantClickListener listener) {
        this.context = context;
        this.restaurantList = restaurantList;
        this.listener =listener;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.restitem_owner, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Resturant restaurant = restaurantList.get(position);
        holder.name.setText(restaurant.getName());

        Glide.with(context)
                .load(restaurant.getPhoto())
                .placeholder(R.drawable.ic_launcher_background) // fallback
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            listener.onRestaurantClick(restaurant);
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.restName);
            image = itemView.findViewById(R.id.restImage);
        }
    }

    public interface OnRestaurantClickListener {
        void onRestaurantClick(Resturant restaurant);
    }
}

