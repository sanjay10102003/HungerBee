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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.food.hungerbee.ModelClasses.RestaurantModelClass;
import com.food.hungerbee.R;
import com.food.hungerbee.UserFoodActivity;

public class SearchRecyclerAdapter extends FirebaseRecyclerAdapter<RestaurantModelClass, SearchRecyclerAdapter.SearchViewholder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    static Context mContext;
    public SearchRecyclerAdapter(@NonNull FirebaseRecyclerOptions<RestaurantModelClass> options ,Context context) {
        super(options);
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull SearchRecyclerAdapter.SearchViewholder holder, int position, @NonNull RestaurantModelClass model) {
        holder.RestaurantName.setText(model.getName());
        holder.RestaurantAddress.setText(model.getAddress());
        Glide.with(holder.RestaurantImg.getContext()).load(model.getProfile()).into(holder.RestaurantImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserFoodActivity.class);
                intent.putExtra("PhoneNumber", model.getPhoneNumber());
                intent.putExtra("RestaurantName",model.getName());
                intent.putExtra("RestaurantAddress",model.getAddress());
                intent.putExtra("RestaurantImg",model.getProfile());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
            });
    }

    @Override
    public SearchViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_details, parent, false);
        return new SearchViewholder(view);
    }

    public static class SearchViewholder extends RecyclerView.ViewHolder {
        ImageView RestaurantImg;
        TextView RestaurantName, RestaurantAddress;
        public SearchViewholder(@NonNull View itemView) {
            super(itemView);
            RestaurantImg = itemView.findViewById(R.id.ImgRestaurent);
            RestaurantName = itemView.findViewById(R.id.txtRestaurentName);
            RestaurantAddress = itemView.findViewById(R.id.txtRestaurentAddress);
        }
    }
}
