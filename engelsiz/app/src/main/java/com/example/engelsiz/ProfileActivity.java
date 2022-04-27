package com.example.engelsiz;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {



    CircleImageView userPhoto;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private TextView txtUserFullName;
    DatabaseReference userName;
    String nameValue;


    String imageValue;
    DatabaseReference userProfilePhoto;

    DatabaseReference reference;
    RecyclerView recyclerViewProfile;
    ArrayList<com.example.engelsiz.Posts> list;
    ArrayList<String> keyList;
    ProfileAdapter adapter;

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
        setContentView(R.layout.activity_profile);


        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) { // kimlik doğrulama durumu
                    finish();
                }
            }
        };


        userPhoto = findViewById(R.id.profile_image);
        txtUserFullName = findViewById(R.id.txtUserFullName);
        final String user_id = auth.getCurrentUser().getUid();
        //profil sayfasında kullanıcının ismi geliyor
        userName = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user_id).child("name");
        userName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    nameValue = dataSnapshot.getValue().toString();
                    txtUserFullName.setText(nameValue);
                }
                catch(Exception e) {
                    System.out.println("Something went wrong.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //profil sayfasında kullanıcının seçtiği profil fotoğrafını gösteriyor
        userProfilePhoto = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user_id).child("image");
        userProfilePhoto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    imageValue = dataSnapshot.getValue().toString();
                    Picasso.get().load(imageValue).into(userPhoto);
                    System.out.println("picasso " + imageValue);
                }
                catch(Exception e) {
                    System.out.println("BAŞARISIZ.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        recyclerViewProfile = findViewById(R.id.recyclerViewProfile);
        recyclerViewProfile.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<com.example.engelsiz.Posts>();
        keyList = new ArrayList<String>();
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerViewProfile);
       // kullanıcının eklediği kendi postlarını profil sayfasında gösteriyor
        reference = FirebaseDatabase.getInstance().getReference().child("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        String aa = dataSnapshot1.getKey().toString();
                        com.example.engelsiz.Posts p = dataSnapshot1.getValue(com.example.engelsiz.Posts.class);
                        String ID = dataSnapshot1.child("user_id").getValue().toString();
                        if (ID.matches(user_id)) {
                            if (list.indexOf(p) == -1) {
                                    list.add(p);
                                    keyList.add(aa);
                            }
                        }
                    }
                    Collections.reverse(list);
                    Collections.reverse(keyList);
                }
                catch(Exception e){
                }
                adapter = new ProfileAdapter(ProfileActivity.this,list);
                recyclerViewProfile.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this,"hata",Toast.LENGTH_SHORT).show();

            }
        });

        //initialize and assign variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set home selected
        //bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        //BUTONLAR
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), com.example.engelsiz.HomeActivity.class));
                        overridePendingTransition(0,0);
                        return  true;

                    case R.id.nav_add:
                        startActivity(new Intent(getApplicationContext(), com.example.engelsiz.AddActivity.class));
                        overridePendingTransition(0,0);
                        return  true;

                    case R.id.nav_profile:
                        return  true;

                }
                return false;
            }
        });
    }


    public void settings(View view){
        startActivity(new Intent(getApplicationContext(), com.example.engelsiz.AccountSettingsActivity.class));
        finish();
    }

    //kaydırarak post silme özelliği
    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int delete = viewHolder.getAdapterPosition();
            list.remove(delete);
            adapter.notifyDataSetChanged();
            System.out.println(keyList.get(delete));
            reference = FirebaseDatabase.getInstance().getReference().child("Posts");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        if(ds.getKey().toString().matches(keyList.get(delete))){
                            ds.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                            System.out.println("---------  " + ds.getKey().toString());
                        }
                        else
                            System.out.println("OLMUYOR");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    };





}

