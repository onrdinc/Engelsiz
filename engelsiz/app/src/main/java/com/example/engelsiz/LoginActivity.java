package com.example.engelsiz;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView txtRegister;
    TextView forgotTextLink;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private String userName;
    private String userPassword;
    ProgressBar progressBar;
    private DatabaseReference userNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //eklenen içeriklere id verme işlemi
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin =  findViewById(R.id.buttonLogin);
        txtRegister = findViewById(R.id.txtRegister);
        progressBar = findViewById(R.id.progressBar2);
        forgotTextLink = findViewById(R.id.txtPasswordReset);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser(); //giriş yapmış olan kullanıcı

        if(firebaseUser != null){ // Oturum durumu kontrol ediliyor.
            Intent i = new Intent(LoginActivity.this, ProfileActivity.class);
            startActivity(i);
            finish();
        }
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = editTextEmail.getText().toString();
                userPassword = editTextPassword.getText().toString();
                if(userName.isEmpty() || userPassword.isEmpty()){
                    //kullanıcı adı ve şifrenin boş olup olmaması kontrol ediliyor.
                    Toast.makeText(getApplicationContext(),"Lütfen gerekli alanları doldurunuz!",Toast.LENGTH_SHORT).show();
                }else{
                    //login için gerekli alanlar doldurulmuşsa loginFunc() methodu çağırılıyor.
                    progressBar.setVisibility(View.VISIBLE);
                    loginFunc();
                }
            }
        });
        //henüz kayıt olmamış kullanıcıları kaydolma sayfasına yönlendiriyor.
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private void loginFunc() {
        mAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(LoginActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(LoginActivity.this, ProfileActivity.class);
                            startActivity(i); //eğer login işlemi başarılıysa profil sayfasını açıyor.
                            finish();
                            String user_id = mAuth.getCurrentUser().getUid();
                            userNewPassword = FirebaseDatabase.getInstance().getReference();
                            String password = editTextPassword.getText().toString();
                            userNewPassword.child("Users").child(user_id).child("password").setValue(password);
                        }
                        else{
                            // hata
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }

                });


    }

    public void forgotPassword(View view){
        startActivity(new Intent(getApplicationContext(), com.example.engelsiz.ForgotPasswordActivity.class));
        finish();
    }
    public void register(View view) {
        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
        finish();

    }
}
