package com.food.hungerbee.AdapterClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.food.hungerbee.ModelClasses.FoodModelClass;
import com.food.hungerbee.R;
import java.util.ArrayList;


public class AdminMainAdapterClass extends RecyclerView.Adapter<AdminMainAdapterClass.AdminMainViewHolder> {

    ArrayList<FoodModelClass> FoodList;
    Context mContext;

    public AdminMainAdapterClass(ArrayList<FoodModelClass> foodList, Context mContext) {
        this.FoodList = foodList;
        this.mContext = mContext;
    }

    @Override
    public AdminMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_main_list_food,parent,false);
        return new AdminMainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminMainAdapterClass.AdminMainViewHolder holder, int position) {
        FoodModelClass itemModelClass = FoodList.get(position);

        //holder.FoodImg.setImageResource(itemModelClass.getImageUri());
        Glide.with(mContext)
                .load(itemModelClass.getImageUrl())
                .into(holder.FoodImg);
        holder.FoodName.setText(itemModelClass.getItemName());
        holder.FoodPrice.setText(itemModelClass.getItemPrice());
    }

    @Override
    public int getItemCount() {
        return FoodList.size();
    }

    public static class AdminMainViewHolder extends RecyclerView.ViewHolder{
        ImageView FoodImg;
        TextView FoodName,FoodPrice;
        public AdminMainViewHolder(@NonNull View itemView) {
            super(itemView);

            FoodImg = itemView.findViewById(R.id.ImgadminMainListItem);
            FoodName = itemView.findViewById(R.id.txtNameadminMainListItem);
            FoodPrice = itemView.findViewById(R.id.txtPriceadminMainListItem);
        }
    }
}
