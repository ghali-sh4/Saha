package com.example.myallproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MyViewHolder>  {
    Context context;
    ArrayList<Meal> menu;
    private final OnAddToCartClick onAddToCartClick;
    private boolean isOwnerView;

    public MealAdapter(Context context, ArrayList<Meal> menu, boolean isOwnerView ,OnAddToCartClick onAddToCartClick) {
        this.context = context;
        this.menu = menu;
        this.onAddToCartClick = onAddToCartClick;
        this.isOwnerView = isOwnerView;
    }
    @NonNull
    @Override
    public MealAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.menu_item,parent,false);
        return  new MealAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MealAdapter.MyViewHolder holder, int position) {

        Meal item = menu.get(position);
        holder.tvName.setText(item.getName());
        holder.tvDesc.setText(item.getDescription());
        holder.tvPrice.setText("₪" + item.getPrice());
        if (isOwnerView) {
            holder.btAdd.setVisibility(View.GONE);
        } else {
            holder.btAdd.setVisibility(View.VISIBLE);
            holder.btAdd.setOnClickListener(v -> {
                onAddToCartClick.onAdd(item); // Call the lambda directly
            });
        }


        Glide.with(context).load(item.getImgUrl()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return menu.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvDesc,tvPrice;
        ImageView imageView;

        Button btAdd;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.menuName);
            tvDesc=itemView.findViewById(R.id.menuDescription);
            imageView = itemView.findViewById(R.id.menuImage);
            tvPrice=itemView.findViewById(R.id.menuPrice);
            btAdd= itemView.findViewById(R.id.addToCartButton);

        }
    }
    public interface OnAddToCartClick {
        void onAdd(Meal meal);
    }
}
