package com.example.ahmerayazinvoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class HistoryActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView myCustomerLists;
    private FirebaseAuth mAuth;
    private DatabaseReference invoiceRef;
    private String KeyValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        invoiceRef = FirebaseDatabase.getInstance().getReference().child("Invoice");
        KeyValue = invoiceRef.getKey();

        mToolbar = (Toolbar) findViewById(R.id.customer_list_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Customer Invoice List");

        myCustomerLists = (RecyclerView) findViewById(R.id.my_customer_list);
        myCustomerLists.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myCustomerLists.setLayoutManager(linearLayoutManager);

        DisplayAllInvoice();
    }

    private void DisplayAllInvoice()
    {
        Query myInvoiceQuery = invoiceRef.orderByChild("createDTM");

       FirebaseRecyclerOptions<Invoice> options = new FirebaseRecyclerOptions.Builder<Invoice>().setQuery(myInvoiceQuery,Invoice.class).build();
       FirebaseRecyclerAdapter<Invoice, InvoiceViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Invoice, InvoiceViewHolder>(options) {
           @Override
           protected void onBindViewHolder(@NonNull InvoiceViewHolder invoiceViewHolder, int i, @NonNull Invoice invoice) {
               invoiceViewHolder.setCustomerName(invoice.getCustomerName());
               invoiceViewHolder.setPhoneNo(invoice.getPhoneNo());
               invoiceViewHolder.setCreateDTM(invoice.getCreateDTM());
               invoiceViewHolder.setTotalAmount(invoice.getTotalAmount());
           }

           @NonNull
           @Override
           public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
               HistoryActivity.InvoiceViewHolder viewHolder = new HistoryActivity.InvoiceViewHolder(view);
               return viewHolder;
           }
       };
        myCustomerLists.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }

    public static class InvoiceViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public InvoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;


        }


        public void setCustomerName(String customerName) {
            TextView pName = (TextView) mView.findViewById(R.id.product_name);
            pName.setText(customerName);
        }

        public void setPhoneNo(String phoneNo) {
            TextView pQuantity = (TextView) mView.findViewById(R.id.product_quantity);
            pQuantity.setText("Contact: " + phoneNo);
        }

        public void setCreateDTM(String createDTM) {
            TextView pPrice = (TextView) mView.findViewById(R.id.product_price);
            pPrice.setText("Created On: " + createDTM);
        }

        public void setTotalAmount(String totalAmount) {
            TextView pTotal = (TextView) mView.findViewById(R.id.product_total);
            pTotal.setText("Total: "+totalAmount);
        }

    }
}