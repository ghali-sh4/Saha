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

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {
    Context context;
    ArrayList<Resturant> restList;
    private View.OnClickListener clickListener; // Pass click listener directly
    private FirebaseServices fbs;


    public RestaurantAdapter(Context context, ArrayList<Resturant> restList,View.OnClickListener clickListener) {
        this.context = context;
        this.restList = restList;
        this.fbs = FirebaseServices.getInstance();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RestaurantAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v= LayoutInflater.from(context).inflate(R.layout.restitem,parent,false);
        return  new RestaurantAdapter.MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.MyViewHolder holder, int position) {
        Resturant rest = restList.get(position);
        holder.tvName.setText(rest.getName());
        holder.tvLocation.setText(rest.getLocation());

        // Load Image using Glide
        Glide.with(context).load(rest.getPhoto()).into(holder.imageView);

        // Set Click Listener
        holder.itemView.setTag(rest); // Store project in tag to access in click listener
        holder.itemView.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return restList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvLocation;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvNameRestItem);
            tvLocation=itemView.findViewById(R.id.tvLocationRestItem);
            imageView = itemView.findViewById(R.id.rest_image);

        }
    }

}
