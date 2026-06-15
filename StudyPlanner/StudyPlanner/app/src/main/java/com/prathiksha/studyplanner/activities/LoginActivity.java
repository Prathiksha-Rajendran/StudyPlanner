package com.prathiksha.studyplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.prathiksha.studyplanner.R;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    EditText etPhone;
    Button btnSendOtp;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mAuth.getFirebaseAuthSettings()
                .setAppVerificationDisabledForTesting(true);

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
            return;
        }

        etPhone = findViewById(R.id.etPhone);
        btnSendOtp = findViewById(R.id.btnSendOtp);

        btnSendOtp.setOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim();
            if (phone.isEmpty() || phone.length() < 10) {
                Toast.makeText(this, "Enter a valid phone number",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!phone.startsWith("+")) {
                phone = "+91" + phone;
            }
            sendOtp(phone);
        });
    }

    private void sendOtp(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider
                        .OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(
                            com.google.firebase.auth.PhoneAuthCredential credential) {
                        mAuth.signInWithCredential(credential)
                                .addOnSuccessListener(r -> {
                                    startActivity(new Intent(LoginActivity.this,
                                            DashboardActivity.class));
                                    finish();
                                });
                    }

                    @Override
                    public void onVerificationFailed(
                            com.google.firebase.FirebaseException e) {
                        Toast.makeText(LoginActivity.this,
                                "Failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {
                        Intent intent = new Intent(LoginActivity.this,
                                OtpActivity.class);
                        intent.putExtra("verificationId", verificationId);
                        intent.putExtra("phone", phoneNumber);
                        startActivity(intent);
                    }
                }).build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}