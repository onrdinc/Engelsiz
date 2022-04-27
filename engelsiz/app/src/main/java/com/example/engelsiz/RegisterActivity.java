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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class RegisterActivity extends AppCompatActivity {
    //kullanılacak olanlar
    private EditText mFullName;
    private EditText registerEmail;
    private EditText registerPassword;
    private Button buttonRegister;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.registerFullName);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        //kullanıcı kayıt yaptığında profil sayfasına yönlendirme
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(RegisterActivity.this, com.example.engelsiz.ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
                return;
            }
        };
        // kaydol butonu
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this,
                                                                                    new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                            //Edit textlerden kullanıcının girdiği bilgileri alıp string değişkenlerin içinde tutuyorum
                            String name = mFullName.getText().toString();
                            String email = registerEmail.getText().toString();
                            String password = registerPassword.getText().toString();
                            //veritabanına kullanıcı ekleniyor
                            Map newPost = new HashMap();
                            newPost.put("name", name);
                            newPost.put("eMail",email);
                            newPost.put("password",password);
                            current_user_db.setValue(newPost);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Bir hata oluştu...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    //Listener ile kullanıcı çıkışa basana kadar oturum açık kalıyor
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
    //bu method çoktan kayıt olmuş kullanıcıları login sayfasına gitmesi için yönlendiriyor.
    public void login(View view) {
        startActivity(new Intent(getApplicationContext(), com.example.engelsiz.LoginActivity.class));
        finish();
    }
}