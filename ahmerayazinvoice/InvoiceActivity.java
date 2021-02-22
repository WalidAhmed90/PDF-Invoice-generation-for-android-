package com.example.ahmerayazinvoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.DialogTitle;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class InvoiceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Bitmap bitmap, scaledBitmap;
    private FirebaseAuth mAuth;
    private DatabaseReference productRef, invoiceRef;
    private String KeyValue, saveCurrentDate, Customer, CustomerNo, option;
    private ArrayList<Product> ProductArray;
    private Spinner selector;
    String[] users = { "Quotation", "Invoice"};


    private TextView CusName,CusNo;
    

    private Button CreateInvoice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        KeyValue = getIntent().getStringExtra("Key_Value");
        invoiceRef = FirebaseDatabase.getInstance().getReference().child("Invoice").child(KeyValue);
        productRef = FirebaseDatabase.getInstance().getReference().child("Product");
        ProductArray = new ArrayList<>();

        CusName = (TextView) findViewById(R.id.cusname);
        CusNo = (TextView) findViewById(R.id.cusno);
        selector = (Spinner) findViewById(R.id.selector);
        CreateInvoice = (Button) findViewById(R.id.CreateInvoice);
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
        scaledBitmap = Bitmap.createScaledBitmap(bitmap,150,150,false);

        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selector.setAdapter(adapter);
        selector.setOnItemSelectedListener(this);

        invoiceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Customer = snapshot.child("customerName").getValue().toString();
                CustomerNo = snapshot.child("phoneNo").getValue().toString();

                CusName.setText("Customer Name : "+Customer);
                CusNo.setText("Contact No : "+CustomerNo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        convertDataIntoArray();
        CreatePdf();

    }

    private void convertDataIntoArray()
    {
        Query myProductQuery = productRef.orderByChild("keyvalue")
                .startAt(KeyValue).endAt(KeyValue + "\uf8ff");

        myProductQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot product : snapshot.getChildren())
                {
                    Product list = product.getValue(Product.class);

                    ProductArray.add(list);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void CreatePdf()
    {
        CreateInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PdfDocument mPdfDocument = new PdfDocument();
                Paint mypaint = new Paint();
                PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(800  ,1200  ,1).create();
                PdfDocument.Page mypage1 = mPdfDocument.startPage(myPageInfo);

                Canvas canvas = mypage1.getCanvas();

                canvas.drawBitmap(scaledBitmap,(myPageInfo.getPageWidth()/2)-60,20,mypaint);

                mypaint.setTextAlign(Paint.Align.CENTER);
                mypaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
                mypaint.setTextSize(30f);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("Ahmer Ayaz Services",(myPageInfo.getPageWidth()/2)+20, 200 ,mypaint);

                mypaint.setTextAlign(Paint.Align.CENTER);
                mypaint.setTextSize(20f);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("Contact NO: ",(myPageInfo.getPageWidth()/2)-50, 230 ,mypaint);

                mypaint.setTextAlign(Paint.Align.CENTER);
                mypaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.ITALIC));
                mypaint.setTextSize(20f);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("031234567890",(myPageInfo.getPageWidth()/2)+75
                        , 230 ,mypaint);

                mypaint.setColor(Color.rgb(114,188,212));
                canvas.drawRect(0,270,canvas.getWidth(),280,mypaint);

                mypaint.setTextAlign(Paint.Align.RIGHT);
                mypaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
                mypaint.setTextSize(30f);
                mypaint.setColor(Color.BLACK);
                canvas.drawText(""+option,canvas.getWidth()-60, 340 ,mypaint);


                mypaint.setTextAlign(Paint.Align.RIGHT);
                mypaint.setTextSize(20f);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("Created Date: ",canvas.getWidth()-230, 370 ,mypaint);

                Calendar calFordDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                saveCurrentDate = currentDate.format(calFordDate.getTime());

                mypaint.setTextAlign(Paint.Align.RIGHT);
                mypaint.setTextSize(20f);
                mypaint.setColor(Color.BLACK);
                canvas.drawText(""+saveCurrentDate,canvas.getWidth()-60, 370 ,mypaint);




                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
                mypaint.setTextSize(24f);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("" + Customer,60, 455 ,mypaint);

                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(20f);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("" + CustomerNo,60, 475 ,mypaint);


                mypaint.setColor(Color.rgb(211,211,211));
                canvas.drawRect(60,560,canvas.getWidth()-60,590,mypaint);

                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(20f);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("Item",70, 580 ,mypaint);

                mypaint.setTextAlign(Paint.Align.RIGHT);
                mypaint.setTextSize(20f);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("Price",canvas.getWidth()-350, 580 ,mypaint);

                mypaint.setTextAlign(Paint.Align.RIGHT);
                mypaint.setTextSize(20f);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("Quantity",canvas.getWidth()-200, 580 ,mypaint);

                mypaint.setTextAlign(Paint.Align.RIGHT);
                mypaint.setTextSize(20f);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("Total",canvas.getWidth()-70, 580 ,mypaint);

                int j = 40;
                int Total_Amount = 0;
                for (int i = 0; i < ProductArray.size() ; i++) {


                    mypaint.setTextAlign(Paint.Align.LEFT);
                    mypaint.setTextSize(20f);
                    mypaint.setColor(Color.BLACK);
                    canvas.drawText("" + ProductArray.get(i).getProductname(),70, 580 + j ,mypaint);

                    mypaint.setTextAlign(Paint.Align.RIGHT);
                    mypaint.setTextSize(20f);
                    mypaint.setColor(Color.BLACK);
                    canvas.drawText("" + ProductArray.get(i).getPrice(),canvas.getWidth()-350, 580 + j,mypaint);

                    mypaint.setTextAlign(Paint.Align.RIGHT);
                    mypaint.setTextSize(20f);
                    mypaint.setColor(Color.BLACK);
                    canvas.drawText("" + ProductArray.get(i).getQuantity(),canvas.getWidth()-200, 580 + j,mypaint);

                    mypaint.setTextAlign(Paint.Align.RIGHT);
                    mypaint.setTextSize(20f);
                    mypaint.setColor(Color.BLACK);
                    canvas.drawText("" + ProductArray.get(i).getTotal(),canvas.getWidth()-70, 580 + j,mypaint);

                    mypaint.setColor(Color.rgb(211,211,211));
                    canvas.drawRect(60,588 + j - 3,canvas.getWidth()-60,590 + j  ,mypaint);

                    Total_Amount = Total_Amount + Integer.parseInt(ProductArray.get(i).getTotal());

                    j = j + 40;
                }

                mypaint.setTextAlign(Paint.Align.RIGHT);
                mypaint.setTextSize(20f);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("Total Amount: ",canvas.getWidth()-200, 1000,mypaint);

                mypaint.setTextAlign(Paint.Align.RIGHT);
                mypaint.setTextSize(20f);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("" + Total_Amount,canvas.getWidth()-70, 1000,mypaint);

                String totalAmount = String.valueOf(Total_Amount);

                if(totalAmount.length() > 0)
                {
                    HashMap Customermap = new HashMap();
                    Customermap.put("totalAmount", totalAmount);
                    invoiceRef.updateChildren(Customermap);
                }


                mypaint.setColor(Color.rgb(114,188,212));
                canvas.drawRect(0,1100,canvas.getWidth(),1110,mypaint);




                mPdfDocument.finishPage(mypage1);

                File docFolder = new File(Environment.getExternalStorageDirectory()+ "/Ahmer Ayaz Invoice");
                if (!docFolder.exists())
                {
                    docFolder.mkdir();
                }
                File file = new File(docFolder.getAbsolutePath(),"/"+Customer+"_"+saveCurrentDate+".pdf");


                try {
                    mPdfDocument.writeTo(new FileOutputStream(file));
                    Toast.makeText(InvoiceActivity.this, "Pdf Created Successfully.", Toast.LENGTH_LONG).show();
                    SendUserToMainActivity();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                mPdfDocument.close();

            }
        });
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(InvoiceActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        option = users[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}