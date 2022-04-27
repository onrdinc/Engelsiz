package com.example.engelsiz;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class AddActivity extends AppCompatActivity {


    private EditText etType, etQuantity, etAddress, etPhone;
    private Button btnAdd;
    private FirebaseAuth mAuth;
    String helpType, helpQuantity, helpAddress, helpPhone;
    private String currentUserID, currentUserName;
    ProgressBar progressBar;
    DatabaseReference userName;
    DatabaseReference mDatabaseReference;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mAuth = FirebaseAuth.getInstance();
        etType = findViewById(R.id.etType);
        etQuantity = findViewById(R.id.etQuantity);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        btnAdd = findViewById(R.id.btnAdd);
        progressBar = findViewById(R.id.progressBarAdd);


        currentUserID = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = mAuth.getCurrentUser().getUid();
                mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                userName = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(currentUserID).child("name");
                userName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        currentUserName = dataSnapshot.getValue().toString();
                        helpType = etType.getText().toString();
                        helpQuantity = etQuantity.getText().toString();
                        helpAddress = etAddress.getText().toString();
                        helpPhone = etPhone.getText().toString();

                        progressBar.setVisibility(View.VISIBLE);
                        DatabaseReference newPost = mDatabase.push();

                        newPost.child("helpType").setValue(helpType);
                        newPost.child("helpQuantity").setValue (helpQuantity);
                        newPost.child("helpAddress").setValue(helpAddress);
                        newPost.child("helpPhone").setValue(helpPhone);
                        newPost.child("user_id").setValue(currentUserID);
                        newPost.child("username").setValue(currentUserName);

                        Toast.makeText(AddActivity.this, "İlanınız alınmıştır, teşekkürler...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddActivity.this, com.example.engelsiz.HomeActivity.class));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });






            }
        });





        //initialize and assign variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set home selected
        //bottomNavigationView.setSelectedItemId(R.id.nav_add);

        //perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), com.example.engelsiz.HomeActivity.class));
                        overridePendingTransition(0,0);
                        return  true;

                    case R.id.nav_add:
                        return  true;

                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), com.example.engelsiz.ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return  true;

                }
                return false;
            }
        });
    }
}

