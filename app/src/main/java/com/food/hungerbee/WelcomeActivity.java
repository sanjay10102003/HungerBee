package com.food.hungerbee;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.food.hungerbee.ModelClasses.UserModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;


public class WelcomeActivity extends AppCompatActivity {
    public static String PREFS_NAME = "MyPrefsFile";
    TextView txtConnection,txtRetry;
    ImageView imgBee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        txtConnection = findViewById(R.id.txtConnection);
        txtRetry = findViewById(R.id.txtRetry);
        imgBee = findViewById(R.id.imgBee);


        txtRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtRetry.setVisibility(View.GONE);
                txtConnection.setVisibility(View.GONE);
                onStart();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(!(wifiConnection!=null && wifiConnection.isConnected()) || (mobileConnection!=null && mobileConnection.isConnected())){
            txtConnection.setVisibility(View.VISIBLE);
            txtRetry.setVisibility(View.VISIBLE);
        }else {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (currentUser != null) {
                        String phoneNumber = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
                        if (phoneNumber != null) {
                            databaseReference.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        UserModelClass userModelClass = snapshot.getValue(UserModelClass.class);

                                        String check = null;
                                        if (userModelClass != null) {
                                            check = userModelClass.getAccess();
                                        }
                                        if (check != null) {
                                            if (check.equals("admin")) {
                                                Intent intent = new Intent(getApplicationContext(), AdminDashboardActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else if (check.equals("head")) {
                                                Intent intent = new Intent(getApplicationContext(), HeadDashboardActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(WelcomeActivity.this, "Please make sure your Internet is on", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(WelcomeActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, 2000);
        }

    }
}

