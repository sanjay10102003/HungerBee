package com.food.hungerbee.AdapterClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.food.hungerbee.BillingClass;
import com.food.hungerbee.ModelClasses.CartModelClass;
import com.food.hungerbee.R;
import com.food.hungerbee.UserCartActivity;
import java.util.ArrayList;

public class CartAdapterClass extends RecyclerView.Adapter<CartAdapterClass.CartViewHolder> {
    ArrayList<CartModelClass> cartList;
    Context mContext;
    UserCartActivity cart;

    public CartAdapterClass(ArrayList<CartModelClass> cartList, Context mContext, UserCartActivity cart) {
        this.cartList = cartList;
        this.mContext = mContext;
        this.cart = cart;
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


        holder.ImgCartFoodAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int intFoodQuantity;
                BillingClass billingClass;
                intFoodQuantity = Integer.parseInt(cartModelClass.getFoodQuantity());
                intFoodQuantity += 1;
                holder.txtNumberOfItems.setText(String.valueOf(intFoodQuantity));
                cartModelClass.setFoodQuantity(String.valueOf(intFoodQuantity));
                UserCartActivity.cartList.set(position, cartModelClass);
                billingClass = new BillingClass(cartList, UserCartActivity.currentRestaurantDistance);
                cart.strfoodAmountvalue = billingClass.Amount();
                cart.strChargesvalue = billingClass.Charges();
                cart.strTotalAmountvalue = billingClass.TotalAmount();
                cart.txtfoodAmountvalue.setText(cart.strfoodAmountvalue + "+");
                cart.txtChargesvalue.setText(cart.strChargesvalue + "+");
                cart.txtTotalAmountvalue.setText("Rs." + cart.strTotalAmountvalue + "/-");
            }
        });

        holder.ImgCartFoodRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int intFoodQuantity;
                BillingClass billingClass;
                intFoodQuantity = Integer.parseInt(cartModelClass.getFoodQuantity());
                intFoodQuantity -= 1;
                if (intFoodQuantity >= 1) {
                    holder.txtNumberOfItems.setText(String.valueOf(intFoodQuantity));
                    cartModelClass.setFoodQuantity(String.valueOf(intFoodQuantity));
                    UserCartActivity.cartList.set(position, cartModelClass);
                } else {
                    UserCartActivity.cartList.remove(cartModelClass);
                    if (UserCartActivity.cartList.isEmpty()) {
                        UserCartActivity.currentRestaurantName = null;
                        UserCartActivity.currentRestaurantAddress = null;
                        UserCartActivity.currentRestaurantDistance=null;
                        UserCartActivity.currentRestaurantId=null;
                        UserCartActivity.currentRestaurantImg=null;
                        UserCartActivity.cartLinearLayout.setVisibility(View.GONE);
                        UserCartActivity.cartEmptyLinearLayout.setVisibility(View.VISIBLE);

                    }
                    CartAdapterClass cartAdapterClass;
                    cartAdapterClass = CartAdapterClass.this;
                    cartAdapterClass.notifyDataSetChanged();
                }
                billingClass = new BillingClass(cartList, UserCartActivity.currentRestaurantDistance);
                cart.strfoodAmountvalue = billingClass.Amount();
                cart.strChargesvalue = billingClass.Charges();
                cart.strTotalAmountvalue = billingClass.TotalAmount();
                cart.txtfoodAmountvalue.setText(cart.strfoodAmountvalue + "+");
                cart.txtChargesvalue.setText(cart.strChargesvalue + "+");
                cart.txtTotalAmountvalue.setText("Rs." + cart.strTotalAmountvalue + "/-");
            }
        });

        String pos = cartList.get(position).getFoodId();
        for (CartModelClass cartModelClass1 : UserCartActivity.cartList) {
            if (cartModelClass1.getFoodId().equals(pos)) {
                int foodQuantity;
                foodQuantity = Integer.parseInt(cartModelClass1.getFoodQuantity());
                if (foodQuantity >= 1) {
                    holder.txtNumberOfItems.setText(String.valueOf(foodQuantity));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ImgCartFoodAdd, ImgCartFoodRemove;
        TextView FoodName, FoodPrice, txtNumberOfItems;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            /*ImgFood = itemView.findViewById(R.id.ImgCartFood);*/
            FoodName = itemView.findViewById(R.id.txtCartFoodName);
            FoodPrice = itemView.findViewById(R.id.txtCartFoodPrice);
            ImgCartFoodAdd = itemView.findViewById(R.id.ImgCartFoodAdd);
            ImgCartFoodRemove = itemView.findViewById(R.id.ImgCartFoodRemove);
            txtNumberOfItems = itemView.findViewById(R.id.txtCartNumberOfItems);
        }
    }
}



       /* @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            CartModelClass cartModelClass = cartList.get(position);
            int intFoodQuantity;
            BillingClass billingClass;
            switch (v.getId()) {
                case (R.id.ImgCartFoodAdd):
                    intFoodQuantity = Integer.parseInt(cartModelClass.getFoodQuantity());
                    intFoodQuantity += 1;
                    txtNumberOfItems.setText(String.valueOf(intFoodQuantity));
                    cartModelClass.setFoodQuantity(String.valueOf(intFoodQuantity));
                    UserCartActivity.cartList.set(position, cartModelClass);
                    billingClass = new BillingClass(cartList, UserCartActivity.currentRestaurantDistance);
                    cart.strfoodAmountvalue = billingClass.Amount();
                    cart.strChargesvalue = billingClass.Charges();
                    cart.strTotalAmountvalue = billingClass.TotalAmount();
                    cart.txtfoodAmountvalue.setText(cart.strfoodAmountvalue + "+");
                    cart.txtChargesvalue.setText(cart.strChargesvalue + "+");
                    cart.txtTotalAmountvalue.setText("Rs." + cart.strTotalAmountvalue + "/-");
                    break;*/
                    /*databaseReference.child(cartModelClass.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(cartModelClass.getFoodId())) {
                                int intFoodQuantity;
                                intFoodQuantity = Integer.parseInt(snapshot.child(cartModelClass.getFoodId()).child("foodQuantity").getValue(String.class));
                                intFoodQuantity += 1;
                                txtNumberOfItems.setText(String.valueOf(intFoodQuantity));
                                databaseReference.child(cartList.get(position).getUserId().toString()).child(cartList.get(position).getFoodId()).child("foodQuantity").setValue(String.valueOf(intFoodQuantity));

                                cartList.get(position).setFoodQuantity(String.valueOf(intFoodQuantity));
                                *//*int amount = 0;
                                int NumberOfFoods=cartList.size();
                                for(int i=0; i<NumberOfFoods;i++){
                                    amount = amount +Integer.parseInt(cartList.get(position).getFoodQuantity())*Integer.parseInt(cartList.get(position).getFoodPrice());
                                }*//*
                                BillingClass billingClass = new BillingClass(cartList);
                                cart.strfoodAmountvalue = billingClass.Amount();
                                cart.strChargesvalue = billingClass.Charges();
                                cart.strTotalAmountvalue = billingClass.TotalAmount();
                                cart.txtfoodAmountvalue.setText(cart.strfoodAmountvalue+"+");
                                cart.txtChargesvalue.setText(cart.strChargesvalue+"+");
                                cart.txtTotalAmountvalue.setText("Rs."+cart.strTotalAmountvalue);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });*/
                /*case (R.id.ImgCartFoodRemove):
                    intFoodQuantity = Integer.parseInt(cartModelClass.getFoodQuantity());
                    intFoodQuantity -= 1;
                    if (intFoodQuantity >= 1) {
                        txtNumberOfItems.setText(String.valueOf(intFoodQuantity));
                        cartModelClass.setFoodQuantity(String.valueOf(intFoodQuantity));
                        UserCartActivity.cartList.set(position, cartModelClass);
                    } else {
                        UserCartActivity.cartList.remove(cartModelClass);
                        if (UserCartActivity.cartList.isEmpty()) {
                            UserCartActivity.currentRestaurantName = null;
                            UserCartActivity.currentRestaurantAddress = null;
                            UserCartActivity.cartLinearLayout.setVisibility(View.GONE);
                            UserCartActivity.cartEmptyLinearLayout.setVisibility(View.VISIBLE);

                        }
                        CartAdapterClass cartAdapterClass;
                        cartAdapterClass = CartAdapterClass.this;
                        cartAdapterClass.notifyDataSetChanged();
                    }
                    billingClass = new BillingClass(cartList, UserCartActivity.currentRestaurantDistance);
                    cart.strfoodAmountvalue = billingClass.Amount();
                    cart.strChargesvalue = billingClass.Charges();
                    cart.strTotalAmountvalue = billingClass.TotalAmount();
                    cart.txtfoodAmountvalue.setText(cart.strfoodAmountvalue + "+");
                    cart.txtChargesvalue.setText(cart.strChargesvalue + "+");
                    cart.txtTotalAmountvalue.setText("Rs." + cart.strTotalAmountvalue + "/-");
                    break;*/
                    /*databaseReference.child(cartModelClass.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(cartModelClass.getFoodId())) {
*//*
                                txtNumberOfItems.setText(String.valueOf(snapshot.child(foodModelClass.getItemId()).child("foodQuantity").getValue(String.class)));
*//*
                                int intFoodQuantity = Integer.parseInt(snapshot.child(cartModelClass.getFoodId()).child("foodQuantity").getValue(String.class));
                                intFoodQuantity -= 1;
                                if (intFoodQuantity>=1){
                                    txtNumberOfItems.setText(String.valueOf(intFoodQuantity));
                                    databaseReference.child(cartList.get(position).getUserId().toString()).child(cartList.get(position).getFoodId()).child("foodQuantity").setValue(String.valueOf(intFoodQuantity));
                                    cartList.get(position).setFoodQuantity(String.valueOf(intFoodQuantity));
                                    BillingClass billingClass = new BillingClass(cartList);
                                    cart.strfoodAmountvalue = billingClass.Amount();
                                    cart.strChargesvalue = billingClass.Charges();
                                    cart.strTotalAmountvalue = billingClass.TotalAmount();
                                    cart.txtfoodAmountvalue.setText(cart.strfoodAmountvalue+"+");
                                    cart.txtChargesvalue.setText(cart.strChargesvalue+"+");
                                    cart.txtTotalAmountvalue.setText("Rs."+cart.strTotalAmountvalue+"/-");

                                }else {
                                    databaseReference.child(cartList.get(position).getUserId().toString()).child(cartList.get(position).getFoodId()).removeValue()*//*child("foodQuantity").setValue(String.valueOf(intFoodQuantity))*//*;
                                    cartList.remove(position);
                                    CartAdapterClass cartAdapterClass;
                                    cartAdapterClass = CartAdapterClass.this;
                                    cartAdapterClass.notifyDataSetChanged();
                                    BillingClass billingClass = new BillingClass(cartList);
                                    cart.strfoodAmountvalue = billingClass.Amount();
                                    cart.strChargesvalue = billingClass.Charges();
                                    cart.strTotalAmountvalue = billingClass.TotalAmount();
                                    cart.txtfoodAmountvalue.setText(cart.strfoodAmountvalue+"+");
                                    cart.txtChargesvalue.setText(cart.strChargesvalue+"+");
                                    cart.txtTotalAmountvalue.setText("Rs."+cart.strTotalAmountvalue+"/-");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });*/

