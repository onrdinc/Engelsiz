package com.example.engelsiz;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText userEmail;
    Button userPass;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        userEmail = findViewById(R.id.eTextEmail);
        userPass = findViewById(R.id.buttonLink);
        progressBar = findViewById(R.id.progressBar3);

        firebaseAuth = FirebaseAuth.getInstance();

        userPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//şifre yenileme linki
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this,
                                    "Şifre yenileme linki gönderildi", Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(ForgotPasswordActivity.this,
                                    task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }


    public void backToLogin(View view) {

        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();

    }
}

