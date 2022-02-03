package com.food.hungerbee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.food.hungerbee.AdapterClasses.CartAdapterClass;
import com.food.hungerbee.ModelClasses.CartModelClass;
import java.util.ArrayList;

public class UserCartActivity extends AppCompatActivity {
    RecyclerView CartRecyclerView;
    ImageView RestaurantImg;
    TextView txtRestaurantName,txtRestaurantAddress,title;
    public static ArrayList<CartModelClass> cartList = new ArrayList<CartModelClass>();
    ImageView imgBack;
    public TextView txtfoodAmountvalue,txtChargesvalue,txtTotalAmountvalue;
    public String strfoodAmountvalue,strChargesvalue,strTotalAmountvalue;
    Button btnPlaceOrder;
    public static String currentRestaurantName =null,currentRestaurantAddress=null,currentRestaurantId=null,currentRestaurantImg=null,currentRestaurantDistance=null;
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout cartLinearLayout,cartEmptyLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);
        /*mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Cart");*/

        title = findViewById(R.id.Title);
        title.setText("Cart");
        imgBack = findViewById(R.id.imgBack);

        cartLinearLayout = findViewById(R.id.cartLinearLayout);
        cartEmptyLinearLayout = findViewById(R.id.cartEmptyLinearLayout);

        RestaurantImg = findViewById(R.id.RestaurantImg);
        txtRestaurantName = findViewById(R.id.txtRestaurantName);
        txtRestaurantAddress = findViewById(R.id.txtRestaurantAddress);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        txtfoodAmountvalue = findViewById(R.id.txtfoodAmountvalue);
        txtChargesvalue = findViewById(R.id.txtChargesvalue);
        txtTotalAmountvalue = findViewById(R.id.txtTotalAmountvalue);

        if(currentRestaurantName != null){
            txtRestaurantName.setText(currentRestaurantName);
        }if(currentRestaurantAddress!=null){
            txtRestaurantAddress.setText(currentRestaurantAddress);
        }if(currentRestaurantImg!=null){
            try{
                Glide.with(getApplicationContext()).load(currentRestaurantImg).into(RestaurantImg);
            }catch (Exception e){
                RestaurantImg.setImageResource(R.drawable.no_image_icon);
            }
        }

        CartRecyclerView = findViewById(R.id.CartRecyclerView);
        CartRecyclerView.setHasFixedSize(true);
        CartRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        CartAdapterClass cartAdapterClass = new CartAdapterClass(cartList,getApplicationContext(),UserCartActivity.this);
        CartRecyclerView.setAdapter(cartAdapterClass);

        BillingClass billingClass = new BillingClass(cartList,currentRestaurantDistance);
        strfoodAmountvalue = billingClass.Amount();
        strChargesvalue = billingClass.Charges();
        strTotalAmountvalue = billingClass.TotalAmount();
        txtfoodAmountvalue.setText(strfoodAmountvalue+"+");
        txtChargesvalue.setText(strChargesvalue+"+");
        txtTotalAmountvalue.setText("Rs."+strTotalAmountvalue+"/-");

        if(cartList.isEmpty()){
            cartLinearLayout.setVisibility(View.GONE);
            cartEmptyLinearLayout.setVisibility(View.VISIBLE);
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*firebaseAuth = FirebaseAuth.getInstance();
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
                        BillingClass billingClass = new BillingClass(cartList);
                        strfoodAmountvalue = billingClass.Amount();
                        strChargesvalue = billingClass.Charges();
                        strTotalAmountvalue = billingClass.TotalAmount();
                        txtfoodAmountvalue.setText(strfoodAmountvalue+"+");
                        txtChargesvalue.setText(strChargesvalue+"+");
                        txtTotalAmountvalue.setText("Rs."+strTotalAmountvalue+"/-");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserCartActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });*/
    }

}