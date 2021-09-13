package com.food.hungerbee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.food.hungerbee.ModelClasses.LatLngModelClass;
import com.food.hungerbee.ModelClasses.RestaurantModelClass;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;

public class HeadActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Button btnlogout,btnAccessSubmit;
    DatabaseReference databaseReference,AdmindatabaseReference;
    EditText edtPhoneNumber;
    TextView txtLatlng;
    Spinner AccessSpinner,CountryCodeSpinner;
    private static final String[] AccessList = {"","user","admin","head"};
    String[] CountryCodeList = {"","+91"};
    String lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        AdmindatabaseReference = FirebaseDatabase.getInstance().getReference("Admin");
        edtPhoneNumber = findViewById(R.id.edtUsernumber);
        txtLatlng = findViewById(R.id.txtLatlng);
        AccessSpinner = findViewById(R.id.AccessSpinner);
        ArrayAdapter<String> AccessAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,AccessList);
        AccessSpinner.setAdapter(AccessAdapter);
        CountryCodeSpinner = findViewById(R.id.CountryCodeSpinner);
        ArrayAdapter<String> CountryCodeAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,CountryCodeList);
        CountryCodeSpinner.setAdapter(CountryCodeAdapter);

        Intent intent = getIntent();
        if(intent!=null) {
            lat = intent.getStringExtra("StrLat");
            lng = intent.getStringExtra("StrLng");
            txtLatlng.setText("Lat: "+lat+" Lng: "+lng);
        }


        btnAccessSubmit = findViewById(R.id.btnAccessSubmit);

        btnlogout = findViewById(R.id.btnlogout);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        txtLatlng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
            }
        });
        btnAccessSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ValidatePhoneNumber() || !ValidateAccess() || !ValidateCountryCode()){
                    return;
                }
                String StringPhoneNumber = edtPhoneNumber.getText().toString();
                String StringAccess = AccessSpinner.getSelectedItem().toString();
                String StringCountryCode = CountryCodeSpinner.getSelectedItem().toString();

                Query query = databaseReference.child(StringCountryCode+StringPhoneNumber);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists()){
                            edtPhoneNumber.setError("User not found");
                            edtPhoneNumber.requestFocus();
                            return;
                        }
                        databaseReference.child(StringCountryCode+StringPhoneNumber).child("access").setValue(StringAccess);
                        if(StringAccess == "admin") {
                            databaseReference.child(StringCountryCode + StringPhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String Name = snapshot.child("name").getValue().toString();
                                    String UserPhoneNumber = snapshot.child("phoneNumber").getValue().toString();
                                    String Address = "";
                                    String access = "admin";
                                    String Profile = "";
                                    /*String Foods = "";*/
                                    LatLngModelClass latLngModelClass;
                                    latLngModelClass = new LatLngModelClass(Double.parseDouble(lat),Double.parseDouble(lng));
                                    RestaurantModelClass restaurantModelClass = new RestaurantModelClass(Name, UserPhoneNumber, Address, access, Profile);
                                    AdmindatabaseReference.child(StringCountryCode+StringPhoneNumber).setValue(restaurantModelClass);
                                    AdmindatabaseReference.child(StringCountryCode+StringPhoneNumber).child("LatLng").setValue(latLngModelClass);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(HeadActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        /*if(StringAccess == "admin"){
                            DatabaseReference AdmindatabaseReference = FirebaseDatabase.getInstance().getReference("Admin");
                            AdmindatabaseReference.child(StringCountryCode+StringPhoneNumber);
                        }*/

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(HeadActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    private Boolean ValidatePhoneNumber() {
        String value = edtPhoneNumber.getText().toString();
        if (value.isEmpty()) {
            edtPhoneNumber.setError("This Field must be Non empty");
            return false;
        } else {
            edtPhoneNumber.setError(null);
            return true;
        }
    }
    private Boolean ValidateAccess() {
        String value = AccessSpinner.getSelectedItem().toString();
        if (value.isEmpty()) {
            TextView errorText = (TextView) AccessSpinner.getSelectedView();
            errorText.setError("");
            return false;
        } else {
            return true;
        }
    }
    private Boolean ValidateCountryCode() {
        String value = CountryCodeSpinner.getSelectedItem().toString();
        if (value.isEmpty()) {
            TextView errorText = (TextView) CountryCodeSpinner.getSelectedView();
            errorText.setError("");
            return false;
        } else {
            return true;
        }
    }
}