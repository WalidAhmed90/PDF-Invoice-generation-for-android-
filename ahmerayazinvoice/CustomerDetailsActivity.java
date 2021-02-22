package com.example.ahmerayazinvoice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class CustomerDetailsActivity extends AppCompatActivity {

    private EditText CustomerName, Phone;
    private Button Next;
    private ProgressDialog Loadingbar;

    private FirebaseAuth mAuth;
    private DatabaseReference CustomerRef;
    private String KeyValue, saveCurrentDate, saveCurrentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        mAuth = FirebaseAuth.getInstance();

        CustomerRef = FirebaseDatabase.getInstance().getReference().child("Invoice");
        KeyValue = CustomerRef.push().getKey();


        CustomerName = (EditText) findViewById(R.id.Customer_name);
        Phone = (EditText) findViewById(R.id.Customer_Phone);
        Next = (Button) findViewById(R.id.Next_btn);
        Loadingbar = new ProgressDialog(this);


        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveCustomerDetails();

            }
        });

    }


    private void SaveCustomerDetails() {


        String customerName = CustomerName.getText().toString().toUpperCase();
        String phone = Phone.getText().toString();
        String key = KeyValue;


        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calFordDate.getTime());


        if (TextUtils.isEmpty(customerName)) {
            Toast.makeText(this, "Please Enter Customer Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please Enter Phone number", Toast.LENGTH_SHORT).show();
        } else {
            Loadingbar.setTitle("Saving Information");
            Loadingbar.setMessage("Please wait");
            Loadingbar.show();
            Loadingbar.setCanceledOnTouchOutside(true);

            HashMap Customermap = new HashMap();
            Customermap.put("customerName", customerName);
            Customermap.put("phoneNo", phone);
            Customermap.put("createDTM", saveCurrentDate + saveCurrentTime);

            CustomerRef.child(key).setValue(Customermap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        SendUserToProductActivity();
                        Toast.makeText(CustomerDetailsActivity.this, "Customer Details has been saved Successfully.", Toast.LENGTH_LONG).show();
                        Loadingbar.dismiss();
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(CustomerDetailsActivity.this, "Error Occur: " + message, Toast.LENGTH_SHORT).show();
                        Loadingbar.dismiss();
                    }

                }
            });
        }
    }

    private void SendUserToProductActivity()
    {
        Intent ProductIntent = new Intent(CustomerDetailsActivity.this, AddProductActivity.class);
        ProductIntent.putExtra("Key_Value",KeyValue);
        startActivity(ProductIntent);
    }
}