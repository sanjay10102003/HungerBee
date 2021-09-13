package com.food.hungerbee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.food.hungerbee.AdapterClasses.SearchRecyclerAdapter;
import com.food.hungerbee.AdapterClasses.UserMainAllRestaurantAdapterClass;
import com.food.hungerbee.ModelClasses.RestaurantModelClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    SearchView searchView;
    RecyclerView searchRecyclerview;
    SearchRecyclerAdapter searchRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /*edtSearch = findViewById(R.id.edtSearch);*/
        searchRecyclerview = findViewById(R.id.searchRecyclerview);
        searchRecyclerview.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        FirebaseRecyclerOptions<RestaurantModelClass> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<RestaurantModelClass>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Admin").limitToFirst(10),RestaurantModelClass.class)
                .build();
        searchRecyclerAdapter = new SearchRecyclerAdapter(firebaseRecyclerOptions, getApplicationContext());
        searchRecyclerview.setAdapter(searchRecyclerAdapter);

        searchView = (SearchView) findViewById(R.id.edtSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchProcess(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SearchProcess(newText);
                return false;
            }
        });

    }
    private void SearchProcess(String string) {
        FirebaseRecyclerOptions<RestaurantModelClass> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<RestaurantModelClass>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Admin").orderByChild("name").startAt(string.toUpperCase())/*.endAt(string.toUpperCase()+"/uf8ff")*/.endAt(string.toUpperCase()+"~"),RestaurantModelClass.class)
                .build();
        searchRecyclerAdapter = new SearchRecyclerAdapter(firebaseRecyclerOptions, getApplicationContext());
        searchRecyclerAdapter.startListening();
        searchRecyclerview.setAdapter(searchRecyclerAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        searchRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        searchRecyclerAdapter.stopListening();
    }
}