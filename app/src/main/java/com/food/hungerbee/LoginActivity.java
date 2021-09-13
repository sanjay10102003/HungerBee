package com.food.hungerbee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    EditText edtphnNumlogin, edtOTP;
    Button btnlogin, btnOTP;
    TextView txtRegisterlogin;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String verificationCodeBySystem,SelectedCountryCode;
    ProgressBar progressBarOTP;
    Spinner Spinnerlogin;
    String[] CountryCode = {"","+91"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtphnNumlogin = findViewById(R.id.edtUserPhnNumlogin);
        btnlogin = findViewById(R.id.btnUserlogin);
        txtRegisterlogin = findViewById(R.id.txtUserRegisterlogin);
        btnOTP = findViewById(R.id.btnUserloginOTP);
        edtOTP = findViewById(R.id.edtUserloginOTP);
        progressBarOTP = findViewById(R.id.progressBarUserloginOTP);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        Spinnerlogin = findViewById(R.id.Spinnerlogin);
        ArrayAdapter CountryCodeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, CountryCode);
        CountryCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinnerlogin.setAdapter(CountryCodeAdapter);


        progressBarOTP.setVisibility(View.GONE);

        txtRegisterlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarOTP.setVisibility(View.VISIBLE);
                if (!validatePhoneNumber()) {
                    return;
                }
                SelectedCountryCode = Spinnerlogin.getSelectedItem().toString();
                if(SelectedCountryCode == ""){
                                /*TextView errorText = (TextView)mySpinner.getSelectedView();
                                errorText.setError("");
                                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                                errorText.setText("my actual error text");//changes the selected item text to this*/
                    TextView errorText = (TextView) Spinnerlogin.getSelectedView();
                    errorText.setError("error");
                    progressBarOTP.setVisibility(View.GONE);
                    return;
                }
                edtOTP.setVisibility(View.VISIBLE);
                btnOTP.setVisibility(View.VISIBLE);
                String PhoneNumber = SelectedCountryCode+edtphnNumlogin.getText().toString();
                Query myquery = databaseReference.child(PhoneNumber);
                myquery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
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
                        } else {
                            edtphnNumlogin.setError("User does not exist");
                            edtphnNumlogin.requestFocus();
                            Toast.makeText(LoginActivity.this, "User does not exist", Toast.LENGTH_LONG).show();
                            progressBarOTP.setVisibility(View.GONE);
                            return;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "query failed..."+error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            private void sentVerificationCodetoUser(String PhoneNumber) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        PhoneNumber,        // Phone number to verify
                        60,        // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        LoginActivity.this,   // Activity (for callback binding)
                        mCallbacks);        // OnVerificationStateChangedCallback
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
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };
            private void verifyCode(String codebyUser) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codebyUser);
                signinByCredential(credential);
            }
            private void signinByCredential(PhoneAuthCredential credential) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInWithCredential(credential).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //String CurrentUserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            String PhoneNumber = SelectedCountryCode+edtphnNumlogin.getText().toString();
                            databaseReference.child(PhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String check = snapshot.child("access").getValue(String.class);
                                    if (check.equals("admin")) {
                                        Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }else if(check.equals("head")){
                                        Intent intent = new Intent(getApplicationContext(), HeadActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(LoginActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                        });
            }
            private Boolean validatePhoneNumber() {
                String value = edtphnNumlogin.getText().toString();
                if (value.isEmpty()) {
                    edtphnNumlogin.setError("This Field must be Non empty");
                    return false;
                } else {
                    edtphnNumlogin.setError(null);
                    return true;
                }
            }
        });
    }
}


    /*private void isUser() {
        String userPhoneNumber = edtphnNumlogin.getText().toString();
        String userPassword = edtPasswordlogin.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        Query checkUser = reference.orderByChild("UserPhoneNumber").equalTo(userPhoneNumber);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    edtphnNumlogin.setError(null);
                    String password = snapshot.child("password").getValue(String.class);
                    if(password.equals(userPassword)){
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else {
                        edtPasswordlogin.setError("Wrong Password");
                        edtPasswordlogin.requestFocus();
                    }
                }else {
                    edtphnNumlogin.setError("User not found");
                    edtphnNumlogin.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error: "+error,Toast.LENGTH_LONG);
            }
        });

    }*/

