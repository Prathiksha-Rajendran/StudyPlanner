package com.prathiksha.studyplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.prathiksha.studyplanner.R;

public class OtpActivity extends AppCompatActivity {

    EditText etOtp;
    Button btnVerify;
    String verificationId;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mAuth = FirebaseAuth.getInstance();
        mAuth.getFirebaseAuthSettings()
                .setAppVerificationDisabledForTesting(true);

        verificationId = getIntent().getStringExtra("verificationId");

        etOtp = findViewById(R.id.etOtp);
        btnVerify = findViewById(R.id.btnVerify);

        btnVerify.setOnClickListener(v -> {
            String otp = etOtp.getText().toString().trim();
            if (otp.length() != 6) {
                Toast.makeText(this, "Enter 6-digit OTP",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            verifyOtp(otp);
        });
    }

    private void verifyOtp(String otp) {
        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(verificationId, otp);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(this, "Login successful!",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, DashboardActivity.class));
                    finishAffinity();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Invalid OTP: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }
}