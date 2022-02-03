package com.food.hungerbee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.StatusBarManager;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.food.hungerbee.AdapterClasses.CategoriesAdapter;
import com.food.hungerbee.AdapterClasses.UserMainAllRestaurantAdapterClass;
import com.food.hungerbee.ModelClasses.CategoriesModelClass;
import com.food.hungerbee.ModelClasses.LatLngModelClass;
import com.food.hungerbee.ModelClasses.RestaurantModelClass;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference RestaurantdatabaseReference;
    RecyclerView RecyclerViewDashboardCategories, RecyclerViewDashboardAllRestaurants;
    ArrayList<CategoriesModelClass> dashboardCategoriesItemList;
    ArrayList<RestaurantModelClass> dashboard_AllRestaurantList;
    UserMainAllRestaurantAdapterClass userMainAllRestaurantAdapterClass;
    CategoriesAdapter categoriesAdapter;
    TextView txtViewMoreCategory,txtNoRestaurantFound;
    LinearLayout dashboardLinearLayout;
    ImageView profileimg,ImgCart;
    Double UserLat=10.1787,UserLng=78.9960;
    EditText edtMainSearch;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNoRestaurantFound = findViewById(R.id.txtNoRestaurantFound);
        dashboardLinearLayout = findViewById(R.id.dashboardLinearLayout);
        edtMainSearch = findViewById(R.id.edtMainSearch);
        ImgCart = findViewById(R.id.ImgCart);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

/*
        txtViewMoreCategory = findViewById(R.id.txtViewMoreCategory);
*/
/*
        RecyclerViewDashboardCategories = findViewById(R.id.RecyclerView_dashboard_categories);
*/
        RecyclerViewDashboardAllRestaurants = findViewById(R.id.RecyclerView_dashboard_AllFoods);
        profileimg = findViewById(R.id.profileimg);


        /*RecyclerViewDashboardCategories.setHasFixedSize(true);
        RecyclerViewDashboardCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        dashboardCategoriesItemList = new ArrayList<CategoriesModelClass>();
        dashboardCategoriesItemList.add(new CategoriesModelClass(R.drawable.pizzaimg, "Pizza"));
        dashboardCategoriesItemList.add(new CategoriesModelClass(R.drawable.burgerimg, "Burger"));
        dashboardCategoriesItemList.add(new CategoriesModelClass(R.drawable.shawarmaimg, "Shawarma"));
        dashboardCategoriesItemList.add(new CategoriesModelClass(R.drawable.donuts, "Donut"));
        dashboardCategoriesItemList.add(new CategoriesModelClass(R.drawable.milkshake, "Milk shake"));
        dashboardCategoriesItemList.add(new CategoriesModelClass(R.drawable.freshjuice, "Fresh Juice"));
        categoriesAdapter = new CategoriesAdapter(dashboardCategoriesItemList);
        RecyclerViewDashboardCategories.setAdapter(categoriesAdapter);
        categoriesAdapter.notifyDataSetChanged();*/


        RecyclerViewDashboardAllRestaurants.setHasFixedSize(true);
        RecyclerViewDashboardAllRestaurants.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        dashboard_AllRestaurantList = new ArrayList<RestaurantModelClass>();
        userMainAllRestaurantAdapterClass = new UserMainAllRestaurantAdapterClass(dashboard_AllRestaurantList, getApplicationContext());
        RecyclerViewDashboardAllRestaurants.setAdapter(userMainAllRestaurantAdapterClass);
        RestaurantdatabaseReference = FirebaseDatabase.getInstance().getReference("Admin");
        ValueEventListener valueEventListener = RestaurantdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    dashboard_AllRestaurantList.clear();
                    for(DataSnapshot AdmindataSnapshot: snapshot.getChildren()){
                        if(AdmindataSnapshot.exists()) {
                            RestaurantModelClass restaurantModelClass = AdmindataSnapshot.getValue(RestaurantModelClass.class);
                            String PhoneNumber = restaurantModelClass.getPhoneNumber();
                            RestaurantdatabaseReference.child(PhoneNumber).child("LatLng").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    LatLngModelClass latLngModelClass = snapshot.getValue(LatLngModelClass.class);
                                    String RestaurantLat = latLngModelClass.getLat();
                                    String  RestaurantLng = latLngModelClass.getLng();
                                    float[] result = new float[1];
                                    Location.distanceBetween(UserLat,UserLng,Double.parseDouble(RestaurantLat),Double.parseDouble(RestaurantLng),result);
                                    float distance=result[0];
                                    if(distance<=2000.0) {
                                        RestaurantModelClass restaurantModelClass1 = new RestaurantModelClass(restaurantModelClass,String.valueOf(distance));
                                        dashboard_AllRestaurantList.add(restaurantModelClass1);
                                        userMainAllRestaurantAdapterClass.notifyDataSetChanged();
                                        dashboardLinearLayout.setVisibility(View.VISIBLE);
                                        txtNoRestaurantFound.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(MainActivity.this, "no Restaurant found", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }else{
                    Toast.makeText(MainActivity.this, "no Restaurant found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        if(dashboard_AllRestaurantList.size()==0){
            dashboardLinearLayout.setVisibility(View.INVISIBLE);
            txtNoRestaurantFound.setVisibility(View.VISIBLE);;
        }

        edtMainSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);
            }
        });
        ImgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UserCartActivity.class);
                startActivity(intent);
            }
        });
        /*txtViewMoreCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserFullCategoryList.class);
                startActivity(intent);
            }
        });*/
        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestaurantdatabaseReference.removeEventListener(valueEventListener);
                firebaseAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    public Double getUserLat() {
        return UserLat;
    }

    public void setUserLat(Double userLat) {
        UserLat = userLat;
    }

    public Double getUserLng() {
        return UserLng;
    }

    public void setUserLng(Double userLng) {
        UserLng = userLng;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

}