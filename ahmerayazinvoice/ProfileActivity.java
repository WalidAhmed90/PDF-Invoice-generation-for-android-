package com.example.ahmerayazinvoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView FullName, Number, City, Address;
    private CircleImageView profileImage;

    private DatabaseReference UserProfileRef;
    private FirebaseAuth mAuth;

    private String CurrentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FullName = (TextView) findViewById(R.id.My_Full_name);
        Number = (TextView) findViewById(R.id.My_Number);
        City = (TextView) findViewById(R.id.My_City);
        Address = (TextView) findViewById(R.id.My_Address);
        profileImage = (CircleImageView) findViewById(R.id.My_profile_image);

        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();
        UserProfileRef = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUserId);

        UserProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    String image = snapshot.child("ProfileImage").getValue().toString();
                    String fullName = snapshot.child("FullName").getValue().toString();
                    String mobileNo = snapshot.child("MobileNo").getValue().toString();
                    String city = snapshot.child("City").getValue().toString();
                    String address = snapshot.child("Address").getValue().toString();

                    Picasso.get().load(image).placeholder(R.drawable.profile).into(profileImage);

                    FullName.setText(fullName);
                    Number.setText("Mobile No: " + mobileNo);
                    City.setText("City: "+city);
                    Address.setText("Address: "+address);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}