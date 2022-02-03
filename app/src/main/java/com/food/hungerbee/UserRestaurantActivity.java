package com.food.hungerbee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.food.hungerbee.AdapterClasses.MenuFoodAdapterClass;
import com.food.hungerbee.ModelClasses.FoodModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class UserRestaurantActivity extends AppCompatActivity {
    TextView txtRestaurantName,txtRestaurantAddress,txtRestaurentDistance;
    ImageView ImgRestaurant,imgBack;
    RecyclerView RestaurantMenuRecyclerView;
    ArrayList<FoodModelClass> MenuFoodList;
    DatabaseReference FooddatabaseReference;
    FirebaseAuth firebaseAuth;
    SearchView edtMenuSearch;
    CardView ImgSearch;
    MenuFoodAdapterClass menuFoodAdapterClass;
    int searchFlag=0;
    public static String StringRestaurantName,StringRestaurantAddress,PhoneNumberforId,StringRestaurantImg,StringRestaurantDistance;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_food);

        toolbar = findViewById(R.id.toolbar);
        ImgSearch = findViewById(R.id.ImgSearch);
        edtMenuSearch = (SearchView) findViewById(R.id.edtMenuSearch);
        FooddatabaseReference = FirebaseDatabase.getInstance().getReference("Admin");
        txtRestaurantName = findViewById(R.id.txtRestaurentName);
        txtRestaurantAddress = findViewById(R.id.txtRestaurentAddress);
        /*ImgRestaurant = findViewById(R.id.ImgRestaurent);*/
        txtRestaurentDistance = findViewById(R.id.txtRestaurentDistance);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        PhoneNumberforId = intent.getStringExtra("PhoneNumber");
        StringRestaurantName = intent.getStringExtra("RestaurantName");
        StringRestaurantAddress = intent.getStringExtra("RestaurantAddress");
        StringRestaurantImg = intent.getStringExtra("RestaurantImg");
        StringRestaurantDistance = intent.getStringExtra("RestaurantDistance");

        imgBack = findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchFlag==0){
                    edtMenuSearch.setVisibility(View.VISIBLE);
                    searchFlag=1;
                }else{
                    edtMenuSearch.setVisibility(View.GONE);
                    searchFlag=0;
                }
            }
        });

        /*Glide.with(getApplicationContext()).load(StringRestaurantImg).into(ImgRestaurant);*/
        txtRestaurantName.setText(StringRestaurantName);
        txtRestaurantAddress.setText(StringRestaurantAddress);
/*
        txtRestaurentDistance.setText(StringRestaurantDistance+"km");
*/
        RestaurantMenuRecyclerView = findViewById(R.id.RestaurantMenuRecyclerView);
        RestaurantMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        /*RestaurantMenuRecyclerView.setHasFixedSize(true);*/

        edtMenuSearch = (SearchView) findViewById(R.id.edtMenuSearch);
        
        FirebaseRecyclerOptions<FoodModelClass> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<FoodModelClass>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Admin").child(PhoneNumberforId).child("Foods"),FoodModelClass.class)
                .build();

        menuFoodAdapterClass = new MenuFoodAdapterClass(firebaseRecyclerOptions,getApplicationContext());
        menuFoodAdapterClass.startListening();
        RestaurantMenuRecyclerView.setAdapter(menuFoodAdapterClass);

        edtMenuSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                MenuSearchProcess(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                MenuSearchProcess(newText);
                return false;
            }
        });

    }

    private void MenuSearchProcess(String string) {
        FirebaseRecyclerOptions<FoodModelClass> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<FoodModelClass>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Admin").child(PhoneNumberforId).child("Foods").orderByChild("itemName").startAt(string.toUpperCase()).endAt(string.toUpperCase()+ "\uf8ff"),FoodModelClass.class)
                .build();
        menuFoodAdapterClass = new MenuFoodAdapterClass(firebaseRecyclerOptions, getApplicationContext());
        menuFoodAdapterClass.startListening();
        RestaurantMenuRecyclerView.setAdapter(menuFoodAdapterClass);

    }

    @Override
    protected void onStart() {
        super.onStart();
        menuFoodAdapterClass.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        menuFoodAdapterClass.stopListening();
    }
}