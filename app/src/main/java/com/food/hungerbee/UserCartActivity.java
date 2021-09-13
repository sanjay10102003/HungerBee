package com.food.hungerbee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.food.hungerbee.AdapterClasses.CartAdapterClass;
import com.food.hungerbee.AdapterClasses.MenuFoodAdapterClass;
import com.food.hungerbee.ModelClasses.CartModelClass;
import com.food.hungerbee.ModelClasses.FoodModelClass;
import com.food.hungerbee.ModelClasses.RestaurantModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserCartActivity extends AppCompatActivity {
    Toolbar mToolbar;
    RecyclerView CartRecyclerView;
    ImageView RestaurantImg;
    TextView txtRestaurantName,txtRestaurantAddress;
    ArrayList<CartModelClass> cartList;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference,RestaurantDatabase;
    String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);
        /*mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Cart");*/

        RestaurantImg = findViewById(R.id.RestaurantImg);
        txtRestaurantName = findViewById(R.id.txtRestaurantName);
        txtRestaurantAddress = findViewById(R.id.txtRestaurantAddress);

        CartRecyclerView = findViewById(R.id.CartRecyclerView);
        CartRecyclerView.setHasFixedSize(true);
        CartRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        cartList = new ArrayList<CartModelClass>();
        CartAdapterClass cartAdapterClass = new CartAdapterClass(cartList,getApplicationContext());
        CartRecyclerView.setAdapter(cartAdapterClass);

        firebaseAuth = FirebaseAuth.getInstance();
        phoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        RestaurantDatabase = FirebaseDatabase.getInstance().getReference("Admin");
        databaseReference.child(phoneNumber).child("CartItems").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot RestaurantSnapshot) {
                if(RestaurantSnapshot.exists()) {
                    for(DataSnapshot foodSnapshot: RestaurantSnapshot.getChildren()) {
                        String RestaurantId = foodSnapshot.getKey().toString();
                        RestaurantDatabase.child(RestaurantId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    RestaurantModelClass restaurantModelClass = snapshot.getValue(RestaurantModelClass.class);
                                    Glide.with(getApplicationContext()).load(restaurantModelClass.getProfile()).into(RestaurantImg);
                                    txtRestaurantName.setText(restaurantModelClass.getName());
                                    txtRestaurantAddress.setText(restaurantModelClass.getAddress());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(UserCartActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
                            }
                        });
                        for (DataSnapshot dataSnapshot:foodSnapshot.getChildren()) {
                            CartModelClass cartModelClass = dataSnapshot.getValue(CartModelClass.class);
                            cartList.add(cartModelClass);
                            cartAdapterClass.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserCartActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}