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

import com.bumptech.glide.Glide;
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

public class MenuFoodAdapterClass extends RecyclerView.Adapter<MenuFoodAdapterClass.MenuFoodViewHolder> {
    ArrayList<FoodModelClass> FoodList;
    Context mContext;

    public MenuFoodAdapterClass(ArrayList<FoodModelClass> foodList, Context mContext) {
        FoodList = foodList;
        this.mContext = mContext;
    }

    @Override
    public MenuFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_menu_design, parent, false);
        return new MenuFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuFoodAdapterClass.MenuFoodViewHolder holder, int position) {
        FoodModelClass foodModelClass = FoodList.get(position);

        Glide.with(mContext).load(foodModelClass.getImageUrl()).into(holder.ImgFood);
        holder.FoodName.setText(foodModelClass.getItemName());
        holder.FoodPrice.setText(foodModelClass.getItemPrice());
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(holder.CurrentUser).child("CartItems");
        databaseReference.child(FoodList.get(position).getUserId()).child(FoodList.get(position).getItemId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int foodQuantity;
                    foodQuantity = Integer.valueOf(snapshot.child("foodQuantity").getValue(String.class));
                    if (foodQuantity>=1){
                        holder.FoodAdd.setVisibility(View.GONE);
                        holder.nextAdd.setVisibility(View.VISIBLE);
                        holder.txtNumberOfItems.setText(String.valueOf(foodQuantity));
                    }else {
                        holder.nextAdd.setVisibility(View.INVISIBLE);
                        holder.FoodAdd.setVisibility(View.VISIBLE);
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
        return FoodList.size();
    }

    public class MenuFoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        DatabaseReference databaseReference;
        String CurrentUser;
        ImageView ImgFood, ImgFoodRemove, ImgFoodAdd;
        TextView FoodName, FoodPrice, FoodAdd, txtNumberOfItems;
        LinearLayout nextAdd;

        public MenuFoodViewHolder(@NonNull View itemView) {
            super(itemView);

            ImgFood = itemView.findViewById(R.id.ImgMenuFood);
            FoodName = itemView.findViewById(R.id.txtMenuFoodName);
            FoodPrice = itemView.findViewById(R.id.txtMenuFoodPrice);
            FoodAdd = itemView.findViewById(R.id.FoodAdd);
            nextAdd = itemView.findViewById(R.id.nextAdd);
            ImgFoodAdd = itemView.findViewById(R.id.ImgFoodAdd);
            ImgFoodRemove = itemView.findViewById(R.id.ImgFoodRemove);
            txtNumberOfItems = itemView.findViewById(R.id.txtNumberOfItems);
            CurrentUser = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(CurrentUser).child("CartItems");
            FoodAdd.setOnClickListener(this);
            ImgFoodAdd.setOnClickListener(this);
            ImgFoodRemove.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            FoodModelClass foodModelClass = FoodList.get(position);
            switch (v.getId()) {
                case (R.id.FoodAdd):
                    CartModelClass cartModelClass = new CartModelClass(foodModelClass.getItemName(),foodModelClass.getItemPrice(), foodModelClass.getItemId(), String.valueOf(1),foodModelClass.getUserId());
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.hasChild(foodModelClass.getUserId())) {
                                databaseReference.setValue("");
                            }
                            databaseReference.child(FoodList.get(position).getUserId().toString()).child(FoodList.get(position).getItemId()).setValue(cartModelClass);
                            FoodAdd.setVisibility(View.GONE);
                            nextAdd.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case (R.id.ImgFoodAdd):
                    databaseReference.child(foodModelClass.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(foodModelClass.getItemId())) {
/*
                                txtNumberOfItems.setText(String.valueOf(snapshot.child(foodModelClass.getItemId()).child("foodQuantity").getValue(String.class)));
*/
                                int intFoodQuantity = Integer.parseInt(snapshot.child(foodModelClass.getItemId()).child("foodQuantity").getValue(String.class));
                                intFoodQuantity += 1;
                                txtNumberOfItems.setText(String.valueOf(intFoodQuantity));
                                databaseReference.child(FoodList.get(position).getUserId().toString()).child(FoodList.get(position).getItemId()).child("foodQuantity").setValue(String.valueOf(intFoodQuantity));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case (R.id.ImgFoodRemove):
                    databaseReference.child(foodModelClass.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(foodModelClass.getItemId())) {
/*
                                txtNumberOfItems.setText(String.valueOf(snapshot.child(foodModelClass.getItemId()).child("foodQuantity").getValue(String.class)));
*/
                                int intFoodQuantity = Integer.parseInt(snapshot.child(foodModelClass.getItemId()).child("foodQuantity").getValue(String.class));
                                intFoodQuantity -= 1;
                                if (intFoodQuantity>=1){
                                txtNumberOfItems.setText(String.valueOf(intFoodQuantity));
                                databaseReference.child(FoodList.get(position).getUserId().toString()).child(FoodList.get(position).getItemId()).child("foodQuantity").setValue(String.valueOf(intFoodQuantity));

                                }else {
                                    nextAdd.setVisibility(View.INVISIBLE);
                                    FoodAdd.setVisibility(View.VISIBLE);
                                    databaseReference.child(FoodList.get(position).getUserId().toString()).child(FoodList.get(position).getItemId()).removeValue()/*child("foodQuantity").setValue(String.valueOf(intFoodQuantity))*/;
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
            /*FoodAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    FoodModelClass foodModelClass = FoodList.get(position);
                    CartModelClass cartModelClass = new CartModelClass(foodModelClass.getItemName(), foodModelClass.getItemId(), foodModelClass.getItemPrice(), foodModelClass.getImageUrl(), String.valueOf(FoodQuantity));
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.hasChild(foodModelClass.getUserId())) {
                                databaseReference.setValue("");
                            }
                            databaseReference.child(FoodList.get(position).getUserId().toString()).child(FoodList.get(position).getItemId()).setValue(cartModelClass);
                            FoodAdd.setVisibility(View.GONE);
                            nextAdd.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            ImgIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    FoodModelClass foodModelClass = FoodList.get(position);
                    databaseReference.child(foodModelClass.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(foodModelClass.getItemId())){
                                //CartModelClass cartModelClassforIncrease = snapshot.child(foodModelClass.getItemId()).getValue(CartModelClass.class);
                                int intFoodQuantity = Integer.parseInt(cartModelClassforIncrease.getFoodQuantity())
                                int intFoodQuantity = Integer.valueOf(txtNumberOfItems.getText().toString());
                                intFoodQuantity+=1;
                                txtNumberOfItems.setText(String.valueOf(intFoodQuantity));
                                //cartModelClassforIncrease.setFoodQuantity(String.valueOf(intFoodQuantity));
                                //databaseReference.child(FoodList.get(position).getUserId().toString()).child(FoodList.get(position).getItemId()).setValue(cartModelClassforIncrease);
                                databaseReference.child(FoodList.get(position).getUserId().toString()).child(FoodList.get(position).getItemId()).child("foodQuantity").setValue(String.valueOf(intFoodQuantity));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}


ImgIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference.child(foodModelClass.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(foodModelClass.getItemId())){
                                CartModelClass cartModelClass = snapshot.child(foodModelClass.getItemId()).getValue(CartModelClass.class);
                                int IntFoodQuantity = Integer.valueOf(cartModelClass.getFoodQuantity());
                                IntFoodQuantity = IntFoodQuantity+1;
                                txtNumberOfItems.setText(String.valueOf(IntFoodQuantity));
                                databaseReference.child(FoodList.get(position).getUserId().toString()).child(FoodList.get(position).getItemId()).child("foodQuantity").setValue(String.valueOf(FoodQuantity));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            FoodAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartModelClass cartModelClass = new CartModelClass(foodModelClass.getItemName(), foodModelClass.getItemId(), foodModelClass.getItemPrice(), foodModelClass.getImageUrl(), String.valueOf(FoodQuantity));
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.hasChild(foodModelClass.getUserId())) {
                                databaseReference.setValue("");
                            }
                            databaseReference.child(FoodList.get(position).getUserId().toString()).child(FoodList.get(position).getItemId()).setValue(cartModelClass);
                            FoodAdd.setVisibility(View.GONE);
                            nextAdd.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });*/
