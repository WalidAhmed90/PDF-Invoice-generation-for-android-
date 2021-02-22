package com.example.ahmerayazinvoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;
    private Button Setting, Logout, Profile, CreateInvoice, History;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        Setting = (Button) findViewById(R.id.Setting_btn);
        Logout = (Button) findViewById(R.id.logout_btn);
        Profile = (Button) findViewById(R.id.profile_btn);
        CreateInvoice = (Button) findViewById(R.id.create_btn);
        History = (Button) findViewById(R.id.history_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null)
        {
            SendUserToLoginActivity();
        }
        else
        {
           CurrentUserExistence();
        }

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                SendUserToLoginActivity();
            }
        });

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendUserToProfileActivity();
            }
        });

        Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendUserToSettingActivity();
            }
        });

        CreateInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendUserToCustomerActivity();
            }
        });
        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendUserToHistoryActivity();
            }
        });
    }

    private void SendUserToHistoryActivity()
    {
        Intent historyIntent = new Intent(MainActivity.this, HistoryActivity.class);
        startActivity(historyIntent);
    }

    private void SendUserToCustomerActivity()
    {
        Intent CustomerIntent = new Intent(MainActivity.this, CustomerDetailsActivity.class);
        startActivity(CustomerIntent);


    }

    private void SendUserToSettingActivity()
    {
        Intent SettingIntent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(SettingIntent);
    }

    private void SendUserToProfileActivity()
    {
        Intent ProfileIntent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(ProfileIntent);
    }

    private void CurrentUserExistence()
    {
        final String current_user_Id = mAuth.getCurrentUser().getUid();

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (!snapshot.hasChild(current_user_Id))
                {
                    SendUserToSetupActivity();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SendUserToSetupActivity()
    {
        Intent SetupIntent = new Intent(MainActivity.this, SetupActivity.class);
        SetupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(SetupIntent);
        finish();
    }

    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
}