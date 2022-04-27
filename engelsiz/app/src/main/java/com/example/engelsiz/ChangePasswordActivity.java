package com.example.engelsiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class ChangePasswordActivity extends AppCompatActivity {

    EditText txtOldPassword;
    EditText txtNewPassword;
    Button btnChangePassword;
    private FirebaseAuth mAuth;

    DatabaseReference userPassword;
    String userpassword;
    String user_id;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userPass = database.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mAuth = FirebaseAuth.getInstance();
        txtOldPassword = findViewById(R.id.eTextCurrentPassword);
        txtNewPassword = findViewById(R.id.eTextNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);


        user_id = mAuth.getCurrentUser().getUid();
        userPassword = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("password");
        userPassword.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userpassword = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userOldPassword = txtOldPassword.getText().toString();
                String userNewPassword = txtNewPassword.getText().toString();

                if(userpassword.matches(userOldPassword)){

                    userPass.child(user_id).child("password").setValue(userNewPassword);
                    Toast.makeText(ChangePasswordActivity.this, "Şifreniz başarıyla değiştirildi!", Toast.LENGTH_SHORT).show();


                }
            }
        });


        //initialize and assign variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.nav_add);

        //perform ItemSelectedListener
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

    public void backToSettings(View view) {

        startActivity(new Intent(getApplicationContext(), com.example.engelsiz.AccountSettingsActivity.class));
        finish();

    }
}
