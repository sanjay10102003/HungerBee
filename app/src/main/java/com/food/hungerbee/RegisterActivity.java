package com.food.hungerbee;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.food.hungerbee.ModelClasses.UserModelClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;


public class RegisterActivity extends AppCompatActivity {
    EditText edtphnNum, edtName, edtOTP;
    Button btnRegister, btnOTP;
    TextView txtLoginregister;
    ProgressBar progressBarOTP;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String verificationCodeBySystem, Name, PhoneNumber, SelectedCountryCode;
    Spinner Spinnerregister;
    String[] CountryCode = {"", "+91"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtphnNum = findViewById(R.id.edtUserPhnNumRegister);
        btnRegister = findViewById(R.id.btnUserRegister);
        edtName = findViewById(R.id.edtUserNameRegister);
        btnOTP = findViewById(R.id.btnUserregisterOTP);
        edtOTP = findViewById(R.id.edtUserregisterOTP);
        progressBarOTP = findViewById(R.id.progressBarUserregisterOTP);

        Spinnerregister = findViewById(R.id.Spinnerregister);
        ArrayAdapter CountryCodeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, CountryCode);
        CountryCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinnerregister.setAdapter(CountryCodeAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        progressBarOTP.setVisibility(View.GONE);

        txtLoginregister = findViewById(R.id.txtUserLoginregister);

        txtLoginregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarOTP.setVisibility(View.VISIBLE);
                if (!validateName() | !validatePhoneNumber()) {
                    return;
                }
                SelectedCountryCode = Spinnerregister.getSelectedItem().toString();
                if (SelectedCountryCode.equals("")) {
                    TextView errorText = (TextView) Spinnerregister.getSelectedView();
                    errorText.setError("error");
                    progressBarOTP.setVisibility(View.GONE);
                    return;
                }
                PhoneNumber = SelectedCountryCode + edtphnNum.getText().toString();
                Name = edtName.getText().toString();

                Query myquery = databaseReference.child(PhoneNumber);
                myquery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            edtphnNum.setError("Phone number Already exist");
                            edtphnNum.requestFocus();
                            Toast.makeText(RegisterActivity.this, "User already exist", Toast.LENGTH_LONG).show();
                            progressBarOTP.setVisibility(View.GONE);
                            return;
                        }
                        sentVerificationCodetoUser(PhoneNumber);
                        btnOTP.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String code = edtOTP.getText().toString();
                                if (code.isEmpty() || code.length() < 6) {
                                    edtOTP.setError("Wrong OTP...");
                                    edtOTP.requestFocus();
                                    return;
                                }
                                progressBarOTP.setVisibility(View.VISIBLE);
                                verifyCode(code);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(RegisterActivity.this, "query failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void sentVerificationCodetoUser(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,   // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                progressBarOTP.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String codebyUser) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codebyUser);
        signinByCredential(credential);
    }

    private void signinByCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Name = edtName.getText().toString().toUpperCase();
                    String address = "";
                    String access = "user";

                    UserModelClass userModelClass = new UserModelClass(Name, PhoneNumber, address, access);

                    databaseReference.child(PhoneNumber).setValue(userModelClass);

                    databaseReference.child(PhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String check = snapshot.child("access").getValue(String.class);
                                if (check.equals("admin")) {
                                    Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else if (check.equals("head")) {
                                    Intent intent = new Intent(getApplicationContext(), HeadActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(RegisterActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Boolean validateName() {
        String value = edtphnNum.getText().toString();
        if (value.isEmpty()) {
            edtName.setError("This Field must be Non empty");
            return false;
        } else {
            edtName.setError(null);
            return true;
        }
    }

    private Boolean validatePhoneNumber() {
        String value = edtphnNum.getText().toString();
        if (value.isEmpty()) {
            edtphnNum.setError("This Field must be Non empty");
            return false;
        } else {
            edtphnNum.setError(null);
            return true;
        }
    }

}