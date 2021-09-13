package com.food.hungerbee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.food.hungerbee.AdapterClasses.MenuFoodAdapterClass;
import com.food.hungerbee.ModelClasses.FoodModelClass;
import com.food.hungerbee.ModelClasses.RestaurantModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class UserFoodActivity extends AppCompatActivity {
    TextView txtRestaurantName,txtRestaurantAddress,txtRestaurentDistance;
    ImageView ImgRestaurant;
    RecyclerView RestaurantMenuRecyclerView;
    ArrayList<FoodModelClass> MenuFoodList;
    DatabaseReference FooddatabaseReference;
    FirebaseAuth firebaseAuth;
    String PhoneNumberforId,StringRestaurantName,StringRestaurantAddress,StringRestaurantImg,StringRestaurantDistance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_food);

        FooddatabaseReference = FirebaseDatabase.getInstance().getReference("Admin");
        txtRestaurantName = findViewById(R.id.txtRestaurentName);
        txtRestaurantAddress = findViewById(R.id.txtRestaurentAddress);
        ImgRestaurant = findViewById(R.id.ImgRestaurent);
        txtRestaurentDistance = findViewById(R.id.txtRestaurentDistance);

        Intent intent = getIntent();
        PhoneNumberforId = intent.getStringExtra("PhoneNumber");
        StringRestaurantName = intent.getStringExtra("RestaurantName");
        StringRestaurantAddress = intent.getStringExtra("RestaurantAddress");
        StringRestaurantImg = intent.getStringExtra("RestaurantImg");
        /*StringRestaurantDistance = intent.getStringExtra("RestaurantDistance");*/

        Glide.with(getApplicationContext()).load(StringRestaurantImg).into(ImgRestaurant);
        txtRestaurantName.setText(StringRestaurantName);
        txtRestaurantAddress.setText(StringRestaurantAddress);
/*
        txtRestaurentDistance.setText(StringRestaurantDistance+"km");
*/


        RestaurantMenuRecyclerView = findViewById(R.id.RestaurantMenuRecyclerView);
        RestaurantMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        RestaurantMenuRecyclerView.setHasFixedSize(true);
        MenuFoodList = new ArrayList<FoodModelClass>();
        MenuFoodAdapterClass menuFoodAdapterClass = new MenuFoodAdapterClass(MenuFoodList,getApplicationContext());
        RestaurantMenuRecyclerView.setAdapter(menuFoodAdapterClass);

        FooddatabaseReference.child(PhoneNumberforId).child("Foods").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot FoodDataSnapshot : snapshot.getChildren())
                        if(FoodDataSnapshot.exists()) {
                            FoodModelClass foodModelClass = FoodDataSnapshot.getValue(FoodModelClass.class);
                            MenuFoodList.add(foodModelClass);
                            menuFoodAdapterClass.notifyDataSetChanged();
                        }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserFoodActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });

    }
}