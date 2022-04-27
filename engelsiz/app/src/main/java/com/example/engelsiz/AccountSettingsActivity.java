package com.example.engelsiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AccountSettingsActivity extends AppCompatActivity {

    private TextView signOuttxt;
    private FirebaseAuth auth;
    String currentUserID;
    private FirebaseUser firebaseUser;
    private TextView txtDeleteAcc;


    private FirebaseAuth.AuthStateListener authListener;

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); // mevcut kullanıcıyı alıyor

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    finish();
                }
            }
        };

        signOuttxt=findViewById(R.id.tv_logOut);
        txtDeleteAcc = findViewById(R.id.tv_deleteAcc);
        //hesabı silme işlemi
        txtDeleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUserID = auth.getCurrentUser().getUid();
                DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID);
                DatabaseReference userinfo = FirebaseDatabase.getInstance().getReference("UserInfo").child(currentUserID);

                users.removeValue();
                userinfo.removeValue();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AccountSettingsActivity.this,"Hesap silindi...",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    finish();

                                }
                            }
                        });
            }
        });
        //çıkış yapma işlemi
        signOuttxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutFunc(); // sign out
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();

            }
        });
        //butonlar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return  true;

                    case R.id.nav_add:
                        startActivity(new Intent(getApplicationContext(), com.example.engelsiz.AddActivity.class));
                        overridePendingTransition(0,0);
                        return  true;

                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return  true;

                }
                return false;
            }
        });


    }


    private void signOutFunc() {
        auth.signOut();
    }

//yönlendirmeler
    public void profile(View view) {
        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        finish();
    }
    public void editProfile(View view) {

        startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
        finish();
    }
    public void changePassword(View view) {
        startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
        finish();
    }



}
