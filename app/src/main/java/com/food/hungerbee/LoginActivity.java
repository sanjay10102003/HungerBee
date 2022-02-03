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
import com.food.hungerbee.ModelClasses.UserModelClass;
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
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    EditText edtUserPhnNumlogin, edtOTP;
    Button btnUserlogin, btnOTP;
    TextView txtOTPlogin, txtlogin, txtUserPhnNumlogin;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String verificationCodeBySystem, SelectedCountryCode;
    ProgressBar progressBarOTP;
    Spinner Spinnerlogin;
    String Name,PhoneNumber;
    String[] CountryCode = {"", "+91"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUserPhnNumlogin = findViewById(R.id.edtUserPhnNumlogin);
        btnUserlogin = findViewById(R.id.btnUserlogin);
        txtOTPlogin = findViewById(R.id.txtOTPlogin);
        txtlogin = findViewById(R.id.txtlogin);
        txtUserPhnNumlogin = findViewById(R.id.txtUserPhnNumlogin);
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

        btnUserlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatePhoneNumber()) {
                    return;
                }
                SelectedCountryCode = Spinnerlogin.getSelectedItem().toString();
                if (SelectedCountryCode.equals("")) {
                                /*TextView errorText = (TextView)mySpinner.getSelectedView();
                                errorText.setError("");
                                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                                errorText.setText("my actual error text");//changes the selected item text to this*/
                    TextView errorText = (TextView) Spinnerlogin.getSelectedView();
                    errorText.setError("error");
                    return;
                }

                PhoneNumber = SelectedCountryCode + edtUserPhnNumlogin.getText().toString();

                txtUserPhnNumlogin.setVisibility(View.GONE);
                edtUserPhnNumlogin.setVisibility(View.GONE);
                btnUserlogin.setVisibility(View.GONE);
                Spinnerlogin.setVisibility(View.GONE);
                txtOTPlogin.setVisibility(View.VISIBLE);
                edtOTP.setVisibility(View.VISIBLE);
                btnOTP.setVisibility(View.VISIBLE);
                sentVerificationCodetoUser(PhoneNumber);
            }
        });
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
                    if (Objects.requireNonNull(task.getResult().getAdditionalUserInfo()).isNewUser()) {
                        Name = "";
                        String address = "";
                        String access = "user";
                        UserModelClass userModelClass = new UserModelClass(Name, PhoneNumber, address, access);
                        databaseReference.child(PhoneNumber).setValue(userModelClass);
                        progressBarOTP.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(), FirstTimeProfileActivity.class);
                        intent.putExtra("PhoneNumber",PhoneNumber);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        databaseReference.child(PhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String check = snapshot.child("access").getValue(String.class);
                                    if (check != null) {
                                        if (check.equals("admin")) {
                                            progressBarOTP.setVisibility(View.GONE);
                                            Intent intent = new Intent(getApplicationContext(), AdminDashboardActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        } else if (check.equals("head")) {
                                            progressBarOTP.setVisibility(View.GONE);
                                            Intent intent = new Intent(getApplicationContext(), HeadDashboardActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        } else {
                                            progressBarOTP.setVisibility(View.GONE);
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                progressBarOTP.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    progressBarOTP.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "failed: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private Boolean validatePhoneNumber() {
        String value = edtUserPhnNumlogin.getText().toString();
        if (value.isEmpty()) {
            edtUserPhnNumlogin.setError("This Field must be Non empty");
            return false;
        } else {
            edtUserPhnNumlogin.setError(null);
            return true;
        }
    }
}

