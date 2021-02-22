package com.example.ahmerayazinvoice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddProductActivity extends AppCompatActivity {

    private EditText ProductName, Quantity, Price;
    private Button AddItem, Next;


    private FirebaseAuth mAuth;
    private DatabaseReference productRef;
    private String KeyValue, saveCurrentDate, saveCurrentTime, postRandomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        KeyValue = getIntent().getStringExtra("Key_Value");
        productRef = FirebaseDatabase.getInstance().getReference().child("Product");


        ProductName = (EditText) findViewById(R.id.Product_name);
        Quantity = (EditText) findViewById(R.id.Quantity);
        Price = (EditText) findViewById(R.id.Price);
        AddItem = (Button) findViewById(R.id.Add_btn);
        Next = (Button) findViewById(R.id.Next_btn);




        AddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveProductInformation();
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToProductListActivity();
            }
        });



    }

    private void SendUserToProductListActivity()
    {
        Intent ProductIntent = new Intent(AddProductActivity.this, ProductListActivity.class);
        ProductIntent.putExtra("Key_Value",KeyValue);
        startActivity(ProductIntent);
    }


    private void SaveProductInformation() {
        String productName = ProductName.getText().toString();
        String quantity = Quantity.getText().toString();
        String price = Price.getText().toString();
        int Total = (Integer.parseInt(quantity)) * (Integer.parseInt(price));
        String total = String.valueOf(Total);
        String key = KeyValue;

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;




        if (TextUtils.isEmpty(productName)) {
            Toast.makeText(this, "Please Enter Product Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(quantity)) {
            Toast.makeText(this, "Please Enter Quantity", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, "Please Enter Price", Toast.LENGTH_SHORT).show();
        } else
            {
                HashMap Productmap = new HashMap();
                Productmap.put("productname", productName);
                Productmap.put("quantity", quantity);
                Productmap.put("price", price);
                Productmap.put("total", total);
                Productmap.put("keyvalue", key);

                String Key2 = productRef.push().getKey();
                productRef.child(Key2 + postRandomName).setValue(Productmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(AddProductActivity.this, "Product Added Successfully.", Toast.LENGTH_LONG).show();

                            ProductName.setText("");
                            Quantity.setText("");
                            Price.setText("");

                        }
                        else
                        {
                            String message = task.getException().getMessage();
                            Toast.makeText(AddProductActivity.this, "Error Occur: "+ message, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
    }

    private void SendUserToInvoiceActivity()
    {
        Intent ProductIntent = new Intent(AddProductActivity.this, InvoiceActivity.class);
        ProductIntent.putExtra("Key_Value",KeyValue);
        startActivity(ProductIntent);
    }
}