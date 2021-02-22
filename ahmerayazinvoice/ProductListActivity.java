package com.example.ahmerayazinvoice;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ProductListActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView myProductLists;
    private FirebaseAuth mAuth;
    private DatabaseReference productRef;
    private String KeyValue;
    private Button Next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        KeyValue = getIntent().getStringExtra("Key_Value");
        productRef = FirebaseDatabase.getInstance().getReference().child("Product");

        Next = (Button) findViewById(R.id.next_btn);

        mToolbar = (Toolbar) findViewById(R.id.product_list_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Product List");


        myProductLists = (RecyclerView) findViewById(R.id.my_product_list);
        myProductLists.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myProductLists.setLayoutManager(linearLayoutManager);

        DisplayallProducts();

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent invoiceIntent = new Intent(ProductListActivity.this, InvoiceActivity.class);
                invoiceIntent.putExtra("Key_Value",KeyValue);
                startActivity(invoiceIntent);
            }
        });

    }

    private void DisplayallProducts()
    {
          Query myProductQuery = productRef.orderByChild("keyvalue")
                .startAt(KeyValue).endAt(KeyValue + "\uf8ff");


        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>().setQuery(myProductQuery, Product.class).build();
        FirebaseRecyclerAdapter<Product, ProductViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Product product) {

                productViewHolder.setProductname(product.getProductname());
                productViewHolder.setPrice(product.getPrice());
                productViewHolder.setQuantity(product.getQuantity());
                productViewHolder.setTotal(product.getTotal());
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                ProductViewHolder viewHolder = new ProductViewHolder(view);
                return viewHolder;
            }
        };
        myProductLists.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }


        public static class ProductViewHolder extends RecyclerView.ViewHolder {

            View mView;
            public ProductViewHolder(@NonNull View itemView) {
                super(itemView);
                mView = itemView;


            }


            public void setProductname(String productname) {
                TextView pName = (TextView) mView.findViewById(R.id.product_name);
                pName.setText(productname);
            }

            public void setQuantity(String quantity) {
                TextView pQuantity = (TextView) mView.findViewById(R.id.product_quantity);
                pQuantity.setText("Quantity: " + quantity);
            }

            public void setPrice(String price) {
                TextView pPrice = (TextView) mView.findViewById(R.id.product_price);
                pPrice.setText("Price: " + price);
            }

            public void setTotal(String total) {
                TextView pTotal = (TextView) mView.findViewById(R.id.product_total);
                pTotal.setText("Total: "+total);
            }

    }
}