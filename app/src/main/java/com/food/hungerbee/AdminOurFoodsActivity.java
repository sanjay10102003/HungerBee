package com.food.hungerbee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.food.hungerbee.AdapterClasses.AdminMainAdapterClass;
import com.food.hungerbee.ModelClasses.FoodModelClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class AdminOurFoodsActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    String firebaseAuthPhoneNumber;
    DatabaseReference AdmindatabaseReference, FooddatabaseReference;
    Toolbar AdminToolbarMain;
    FloatingActionButton floatingActionButton;
    RecyclerView AdminMainrecyclerView;
    ArrayList<FoodModelClass> foodlistMain;
    TextView title;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_our_foods);

        title = findViewById(R.id.Title);
        title.setText("Our Foods");
        imgBack = findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        firebaseAuthPhoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        AdmindatabaseReference = FirebaseDatabase.getInstance().getReference("Admin").child(firebaseAuthPhoneNumber);
        FooddatabaseReference = FirebaseDatabase.getInstance().getReference("Foods");

        AdminMainrecyclerView = findViewById(R.id.AdminMainRecyclerView);
        AdminMainrecyclerView.setHasFixedSize(true);
        AdminMainrecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        foodlistMain = new ArrayList<FoodModelClass>();
        AdminMainAdapterClass adminMainAdapterClass = new AdminMainAdapterClass(foodlistMain, getApplicationContext());
        AdminMainrecyclerView.setAdapter(adminMainAdapterClass);


        ValueEventListener valueEventListener = AdmindatabaseReference.child("Foods").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    foodlistMain.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        FoodModelClass itemModelClass = dataSnapshot.getValue(FoodModelClass.class);
                        foodlistMain.add(itemModelClass);
                        adminMainAdapterClass.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminOurFoodsActivity.this, "Error...while retrive data", Toast.LENGTH_SHORT).show();
            }
        });


        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodlistMain.clear();
                Intent intent = new Intent(getApplicationContext(), AdminFoodUploadActivity.class);
                startActivity(intent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.AdminMenuLogout) {
            firebaseAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    /*@Override
    protected void onStart() {
        super.onStart();
        firebaseAuthPhoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        UserdatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuthPhoneNumber);
        FooddatabaseReference = FirebaseDatabase.getInstance().getReference("Products");

        AdminMainrecyclerView = findViewById(R.id.AdminMainRecyclerView);
        AdminMainrecyclerView.setHasFixedSize(true);
        AdminMainrecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        foodlistMain = new ArrayList<ItemModelClass>();
        AdminMainAdapterClass adminMainAdapterClass = new AdminMainAdapterClass(foodlistMain,AdminMainActivity.this);
        AdminMainrecyclerView.setAdapter(adminMainAdapterClass);

        UserdatabaseReference.child("OurFoods").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodlistMain.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String itemkey = dataSnapshot.getValue().toString();
                    FooddatabaseReference.child(itemkey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ItemModelClass itemModelClass = snapshot.getValue(ItemModelClass.class);
                            foodlistMain.add(itemModelClass);
                            adminMainAdapterClass.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(AdminMainActivity.this, "Error...while retrive data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminMainActivity.this, "Error...while retrive data", Toast.LENGTH_SHORT).show();
            }
        });

    }*/
}