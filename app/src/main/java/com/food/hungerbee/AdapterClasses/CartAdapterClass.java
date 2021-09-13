package com.food.hungerbee.AdapterClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.food.hungerbee.ModelClasses.CartModelClass;
import com.food.hungerbee.ModelClasses.FoodModelClass;
import com.food.hungerbee.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartAdapterClass extends RecyclerView.Adapter<CartAdapterClass.CartViewHolder> {
    ArrayList<CartModelClass> cartList;
    Context mContext;

    public CartAdapterClass(ArrayList<CartModelClass> cartList, Context mContext) {
        this.cartList = cartList;
        this.mContext = mContext;
    }

    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_design, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapterClass.CartViewHolder holder, int position) {
        CartModelClass cartModelClass = cartList.get(position);

        /*Glide.with(mContext).load(cartModelClass.getImageURL()).into(holder.ImgFood);*/
        holder.FoodName.setText(cartModelClass.getFoodName());
        holder.FoodPrice.setText(cartModelClass.getFoodPrice());
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(holder.CurrentUser).child("CartItems");
        databaseReference.child(cartList.get(position).getUserId()).child(cartList.get(position).getFoodId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int foodQuantity;
                    foodQuantity = Integer.valueOf(snapshot.child("foodQuantity").getValue(String.class));
                    if (foodQuantity>=1){
                        holder.txtNumberOfItems.setText(String.valueOf(foodQuantity));
                    }else {
                        databaseReference.child(cartList.get(position).getUserId().toString()).child(cartList.get(position).getFoodId()).removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, "Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        DatabaseReference databaseReference;
        String CurrentUser;
        ImageView ImgCartFoodAdd, ImgCartFoodRemove;
        TextView FoodName, FoodPrice, txtNumberOfItems;
        LinearLayout nextAdd;
        int FoodQuantity = 1;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            /*ImgFood = itemView.findViewById(R.id.ImgCartFood);*/
            FoodName = itemView.findViewById(R.id.txtCartFoodName);
            FoodPrice = itemView.findViewById(R.id.txtCartFoodPrice);
            ImgCartFoodAdd = itemView.findViewById(R.id.ImgCartFoodAdd);
            ImgCartFoodRemove = itemView.findViewById(R.id.ImgCartFoodRemove);
            txtNumberOfItems = itemView.findViewById(R.id.txtCartNumberOfItems);
            CurrentUser = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(CurrentUser).child("CartItems");
            ImgCartFoodAdd.setOnClickListener(this);
            ImgCartFoodRemove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            CartModelClass cartModelClass = cartList.get(position);
            switch (v.getId()) {
                case (R.id.ImgCartFoodAdd):
                    databaseReference.child(cartModelClass.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(cartModelClass.getFoodId())) {
                                int intFoodQuantity = Integer.parseInt(snapshot.child(cartModelClass.getFoodId()).child("foodQuantity").getValue(String.class));
                                intFoodQuantity += 1;
                                txtNumberOfItems.setText(String.valueOf(intFoodQuantity));
                                databaseReference.child(cartList.get(position).getUserId().toString()).child(cartList.get(position).getFoodId()).child("foodQuantity").setValue(String.valueOf(intFoodQuantity));
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case (R.id.ImgCartFoodRemove):
                    databaseReference.child(cartModelClass.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(cartModelClass.getFoodId())) {
/*
                                txtNumberOfItems.setText(String.valueOf(snapshot.child(foodModelClass.getItemId()).child("foodQuantity").getValue(String.class)));
*/
                                int intFoodQuantity = Integer.parseInt(snapshot.child(cartModelClass.getFoodId()).child("foodQuantity").getValue(String.class));
                                intFoodQuantity -= 1;
                                if (intFoodQuantity>=1){
                                    txtNumberOfItems.setText(String.valueOf(intFoodQuantity));
                                    databaseReference.child(cartList.get(position).getUserId().toString()).child(cartList.get(position).getFoodId()).child("foodQuantity").setValue(String.valueOf(intFoodQuantity));

                                }else {
                                    databaseReference.child(cartList.get(position).getUserId().toString()).child(cartList.get(position).getFoodId()).removeValue()/*child("foodQuantity").setValue(String.valueOf(intFoodQuantity))*/;
                                    cartList.remove(position);
                                    CartAdapterClass cartAdapterClass;
                                    cartAdapterClass = CartAdapterClass.this;
                                    cartAdapterClass.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
            }
        }
    }
}
