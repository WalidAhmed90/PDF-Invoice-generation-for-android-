package com.example.ahmerayazinvoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.widget.Toast.LENGTH_SHORT;

public class SettingActivity extends AppCompatActivity {

    private TextView FullName, Number, City, Address;
    private CircleImageView profileImage;
    private Button UpdateProfile;

    private DatabaseReference UserProfileRef;
    private FirebaseAuth mAuth;

    private String CurrentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        FullName = (TextView) findViewById(R.id.Full_name);
        Number = (TextView) findViewById(R.id.Number);
        City = (TextView) findViewById(R.id.City);
        Address = (TextView) findViewById(R.id.Address);
        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        UpdateProfile = (Button) findViewById(R.id.update_btn);

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
                    Number.setText(mobileNo);
                    City.setText(city);
                    Address.setText(address);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        UpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ValidateAccountSetting();
            }
        });

    }

    private void ValidateAccountSetting()
    {
        String fullname = FullName.getText().toString();
        String mobile = Number.getText().toString();
        String city = City.getText().toString();
        String address = Address.getText().toString();

        if (TextUtils.isEmpty(fullname))
        {
            Toast.makeText(this, "Please enter your fullname.", LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(mobile))
        {
            Toast.makeText(this, "Please enter your mobile no.", LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(city))
        {
            Toast.makeText(this, "Please enter your city.", LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(address))
        {
            Toast.makeText(this, "Please enter your address.", LENGTH_SHORT).show();
        }
        else
        {
            UpdateAccountInfo(fullname, mobile, city, address);
        }

    }

    private void UpdateAccountInfo(String fullname, String mobile, String city, String address)
    {
        HashMap usermap = new HashMap();
        usermap.put("FullName", fullname);
        usermap.put("MobileNo", mobile);
        usermap.put("City", city);
        usermap.put("Address", address);
        UserProfileRef.updateChildren(usermap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task)
            {
                if(task.isSuccessful())
                {
                    SendUserToMainActivity();
                    Toast.makeText(SettingActivity.this, "Your Profile Setting Updated Successfully.", Toast.LENGTH_LONG).show();

                }
                else
                {
                    String message = task.getException().getMessage();
                    Toast.makeText(SettingActivity.this, "Error Occur: "+ message, Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(SettingActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}