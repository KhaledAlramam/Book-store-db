package com.example.chucky.bookstore;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chucky.bookstore.DataUtil.BooksContract.BooksEntry;

public class DetailsActivity extends AppCompatActivity {

    TextView title;
    TextView price;
    TextView quantity;
    TextView pub;
    TextView number;
    TextView email;
    Button call;
    Button emailContact;
    String actualEmail;
    String actualNumber;
    Button sell;
    Button restock;
    Button delete;
    int currentId;
    int currentQuantity;
    Uri currentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = findViewById(R.id.details_book);
        price = findViewById(R.id.details_price);
        quantity = findViewById(R.id.details_quantity);
        pub = findViewById(R.id.details_pub);
        number = findViewById(R.id.details_pup_num);
        email = findViewById(R.id.details_pup_email);
        call = findViewById(R.id.call);
        emailContact = findViewById(R.id.email);
        sell = findViewById(R.id.details_sell);
        restock = findViewById(R.id.details_restock);
        delete = findViewById(R.id.details_delete);
        currentId = getIntent().getIntExtra(BooksEntry._ID, 0);
        currentQuantity = getIntent().getIntExtra(BooksEntry.COLUMN_QUANTITY, 0);
        actualEmail = getIntent().getStringExtra(BooksEntry.COLUMN_SUPPLIER_EMAIL);
        actualNumber = getIntent().getStringExtra(BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
        title.setText(getIntent().getStringExtra(BooksEntry.COLUMN_PRODUCT_NAME));
        price.setText(String.valueOf(getIntent().getIntExtra(BooksEntry.COLUMN_PRICE, 0)));
        quantity.setText(String.valueOf(currentQuantity));
        pub.setText(getIntent().getStringExtra(BooksEntry.COLUMN_SUPPLIER_NAME));
        number.setText(actualNumber);
        email.setText(actualEmail);
        currentUri = ContentUris.withAppendedId(BooksEntry.CONTENT_URI, currentId);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + actualNumber));
                startActivity(intent);
            }
        });
        emailContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] ads = {actualEmail};
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, ads);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentQuantity > 0) {
                    currentQuantity--;
                    quantity.setText(String.valueOf(currentQuantity));
                    ContentValues values = new ContentValues();
                    values.put(BooksEntry.COLUMN_PRODUCT_NAME, String.valueOf(title.getText()));
                    values.put(BooksEntry.COLUMN_PRICE, String.valueOf(price.getText()));
                    values.put(BooksEntry.COLUMN_QUANTITY, String.valueOf(currentQuantity));
                    values.put(BooksEntry.COLUMN_SUPPLIER_NAME, String.valueOf(pub.getText()));
                    values.put(BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER, actualNumber);
                    values.put(BooksEntry.COLUMN_SUPPLIER_EMAIL, actualEmail);
                    getContentResolver().update(currentUri, values, null, null);
                } else {
                    Toast.makeText(DetailsActivity.this, "Books already out of stock", Toast.LENGTH_SHORT).show();
                }

            }
        });
        restock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentQuantity++;
                quantity.setText(String.valueOf(currentQuantity));
                ContentValues values = new ContentValues();
                values.put(BooksEntry.COLUMN_PRODUCT_NAME, String.valueOf(title.getText()));
                values.put(BooksEntry.COLUMN_PRICE, String.valueOf(price.getText()));
                values.put(BooksEntry.COLUMN_QUANTITY, String.valueOf(currentQuantity));
                values.put(BooksEntry.COLUMN_SUPPLIER_NAME, String.valueOf(pub.getText()));
                values.put(BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER, actualNumber);
                values.put(BooksEntry.COLUMN_SUPPLIER_EMAIL, actualEmail);
                getContentResolver().update(currentUri, values, null, null);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                builder.setTitle("Confirm");
                builder.setMessage("Do you want to delete this book?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getContentResolver().delete(currentUri, null, null);
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(DetailsActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}
