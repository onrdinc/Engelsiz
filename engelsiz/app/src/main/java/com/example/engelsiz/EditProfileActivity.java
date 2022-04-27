package com.example.engelsiz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    CircleImageView userPhoto;


    private EditText txtFullName;
    private TextView txtEmail;
    private EditText txtPhoneNumber;
    private EditText txtBirthDate;
    private Button btnUpdateProfile;


    private DatabaseReference userName,userPhoneNumber, userBirthDate, userProfilePhoto;
    private DatabaseReference RootRef;


    private static final int GalleryPick = 1;

    String nameValue, phoneValue, birthValue, imageValue;
    private String currentUserID;

    private StorageReference UserProfileImagesRef;


    String resimUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();

        RootRef = FirebaseDatabase.getInstance().getReference();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        currentUserID = mAuth.getCurrentUser().getUid();

        txtEmail = findViewById(R.id.txtEmail);
        txtFullName = findViewById(R.id.txtFullName);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtBirthDate = findViewById(R.id.txtBirthDate);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        userPhoto= findViewById(R.id.profile_image);

        txtEmail.setText(mAuth.getCurrentUser().getEmail());

        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fotoğraf galerisi erişim
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GalleryPick);
            }
        });
        //profili güncelle butonu
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = mAuth.getCurrentUser().getUid();
                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user_id);

                String name = txtFullName.getText().toString();
                String phoneNumber = txtPhoneNumber.getText().toString();
                String birthDate = txtBirthDate.getText().toString();
                //kullanıcının profil bilgileri databasede UserInfo altında kaydediliyor.
                Map newPost = new HashMap();
                newPost.put("name", name);
                newPost.put("number",phoneNumber);
                newPost.put("birthDate", birthDate);
                newPost.put("image", imageValue);

                current_user_db.setValue(newPost);

                Toast.makeText(EditProfileActivity.this, "Bilgileriniz başarıyla güncellendi!", Toast.LENGTH_SHORT).show();



            }
        });

        String user_id = mAuth.getCurrentUser().getUid();

        userName = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user_id).child("name");
        userPhoneNumber = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user_id).child("number");
        userBirthDate = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user_id).child("birthDate");
        userProfilePhoto = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user_id).child("image");

        //kullanıcının girdiği kişisel bilgilerin sabit şekilde kalmasını sağlayan kod.
        userName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    nameValue = dataSnapshot.getValue().toString();
                    txtFullName.setText(nameValue);
                    System.out.println(nameValue);
                }
                catch(Exception e) {
                    System.out.println("BAŞARISIZ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        userPhoneNumber.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    phoneValue = dataSnapshot.getValue().toString();
                    txtPhoneNumber.setText(phoneValue);
                    System.out.println(phoneValue);
                }
                catch(Exception e) {
                    System.out.println("Başarısız");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userBirthDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    birthValue = dataSnapshot.getValue().toString();
                    txtBirthDate.setText(birthValue);
                    System.out.println(birthValue);
                }
                catch(Exception e) {
                    System.out.println("Something went wrong.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userProfilePhoto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    imageValue = dataSnapshot.getValue().toString();
                    Picasso.get().load(imageValue).into(userPhoto);
                    System.out.println("picasso " + imageValue);

                }
                catch(Exception e) {
                    System.out.println("Something went wrong.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //initialize and assign variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

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


    public void settings(View view) {

        startActivity(new Intent(getApplicationContext(), com.example.engelsiz.AccountSettingsActivity.class));
        finish();

    }

    //galeriden seçilen fotoğraf üzerinde işlemler
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode== RESULT_OK && data!=null){

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                StorageReference filePath = UserProfileImagesRef.child(currentUserID + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(EditProfileActivity.this,"Profil fotoğrafı başarıyla yüklendi...",
                                    Toast.LENGTH_SHORT).show();
                            StorageReference filePath2 = UserProfileImagesRef.child(currentUserID + ".jpg");
                            filePath2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    resimUrl = uri.toString();
                                    System.out.println(resimUrl);
                                    RootRef.child("UserInfo").child(currentUserID).child("image").setValue(resimUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(EditProfileActivity.this, "Fotoğraf veritabanına başarıyla eklendi...",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        String message = task.getException().toString();
                                                        Toast.makeText(EditProfileActivity.this, "Error: " + message,
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });

                            //System.out.println("download url     " + downloadUrl);


                        }
                        else
                        {
                            String message = task.getException().toString();
                            Toast.makeText(EditProfileActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        }

    }
}
