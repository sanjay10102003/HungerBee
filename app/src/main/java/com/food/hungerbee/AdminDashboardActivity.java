package com.food.hungerbee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class AdminDashboardActivity extends AppCompatActivity {

    CardView cardViewUpload,cardViewOurFoods,cardViewOrderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        cardViewUpload = findViewById(R.id.cardViewUpload);
        cardViewOurFoods = findViewById(R.id.cardViewOurFoods);
        cardViewOrderHistory = findViewById(R.id.cardViewOrderHistory);

        cardViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AdminFoodUploadActivity.class);
                startActivity(intent);
            }
        });
        cardViewOurFoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AdminOurFoodsActivity.class);
                startActivity(intent);
            }
        });
        cardViewOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AdminOrderHistoryActivity.class);
                startActivity(intent);
            }
        });
    }
}