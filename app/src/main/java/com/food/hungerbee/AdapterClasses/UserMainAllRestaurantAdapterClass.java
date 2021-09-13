package com.food.hungerbee.AdapterClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.food.hungerbee.ModelClasses.RestaurantModelClass;
import com.food.hungerbee.R;
import com.food.hungerbee.UserFoodActivity;

import java.util.ArrayList;


public class UserMainAllRestaurantAdapterClass extends RecyclerView.Adapter<UserMainAllRestaurantAdapterClass.UserMainAllFoodViewHolder> {
    ArrayList<RestaurantModelClass> RestaurantList;
    Context mContext;

    public UserMainAllRestaurantAdapterClass(ArrayList<RestaurantModelClass> RestaurantList, Context mContext) {
        this.RestaurantList = RestaurantList;
        this.mContext = mContext;
    }

    @Override
    public UserMainAllFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_details, parent, false);
        return new UserMainAllFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserMainAllRestaurantAdapterClass.UserMainAllFoodViewHolder holder, int position) {
        RestaurantModelClass restaurantModelClass = RestaurantList.get(position);
        if (!restaurantModelClass.getProfile().equals("")) {
            Glide.with(mContext)
                    .load(restaurantModelClass.getProfile())
                    .into(holder.RestaurantImg);
        }
        holder.RestaurantName.setText(restaurantModelClass.getName());
        holder.RestaurantAddress.setText(restaurantModelClass.getAddress());
    }

    @Override
    public int getItemCount() {
        return RestaurantList.size();
    }

    public class UserMainAllFoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView RestaurantImg;
        TextView RestaurantName, RestaurantAddress;

        public UserMainAllFoodViewHolder(@NonNull View itemView) {
            super(itemView);

            RestaurantImg = itemView.findViewById(R.id.ImgRestaurent);
            RestaurantName = itemView.findViewById(R.id.txtRestaurentName);
            RestaurantAddress = itemView.findViewById(R.id.txtRestaurentAddress);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent intent = new Intent(mContext, UserFoodActivity.class);
            intent.putExtra("PhoneNumber", RestaurantList.get(position).getPhoneNumber());
            intent.putExtra("RestaurantName",RestaurantList.get(position).getName());
            intent.putExtra("RestaurantAddress",RestaurantList.get(position).getAddress());
            intent.putExtra("RestaurantImg",RestaurantList.get(position).getProfile());

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }
}
