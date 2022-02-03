package com.food.hungerbee;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.food.hungerbee.ModelClasses.UserModelClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirstTimeProfileActivity extends AppCompatActivity {
    Button btnProfile;
    EditText edtUserNameProfile,edtUserAddressProfile;
    TextView txtUserPhoneNoProfile;
    String PhoneNumber,Name,Address;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_profile);

        btnProfile = findViewById(R.id.btnProfile);
        edtUserNameProfile = findViewById(R.id.edtUserNameProfile);
        edtUserAddressProfile = findViewById(R.id.edtUserAddressProfile);
        txtUserPhoneNoProfile = findViewById(R.id.txtUserPhoneNoProfile);
        Intent intent = getIntent();
        PhoneNumber = intent.getStringExtra("PhoneNumber");

        txtUserPhoneNoProfile.setText(String.valueOf(PhoneNumber));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateName()){
                    return;
                }

                Name = edtUserNameProfile.getText().toString().toUpperCase();
                Address = edtUserAddressProfile.getText().toString();
                String access = "user";
                UserModelClass userModelClass = new UserModelClass(Name, PhoneNumber, Address, access);

                databaseReference.child(PhoneNumber).setValue(userModelClass);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
    private Boolean validateName() {
        String value = edtUserNameProfile.getText().toString();
        if (value.isEmpty()) {
            edtUserNameProfile.setError("This Field must be Non empty");
            return false;
        } else {
            edtUserNameProfile.setError(null);
            return true;
        }
    }
}