package com.example.chucky.bookstore;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chucky.bookstore.DataUtil.BooksContract.BooksEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int EDITOR_LOADER = 2;
    private Uri currentBookUri = null;
    EditText titleEdit;
    EditText priceEdit;
    EditText quantityEdit;
    EditText pubNAmeEdit;
    EditText pubEmailEdit;
    EditText pubNumberEdit;
    RelativeLayout sellNBuy;
    Button sell;
    Button buy;
    Button delete;
    int currentQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        currentBookUri = getIntent().getData();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (currentBookUri != null) {
            sellNBuy = findViewById(R.id.sell_n_buy);
            sell = findViewById(R.id.editor_sell);
            buy = findViewById(R.id.editor_restock);
            delete = findViewById(R.id.editor_delete);
            showEditors();
            getLoaderManager().initLoader(EDITOR_LOADER, null, this);
        }
        titleEdit = findViewById(R.id.title_edit);
        priceEdit = findViewById(R.id.price_edit);
        quantityEdit = findViewById(R.id.quantity_edit);
        pubNAmeEdit = findViewById(R.id.publisher_name_edit);
        pubEmailEdit = findViewById(R.id.publisher_email_edit);
        pubNumberEdit = findViewById(R.id.publisher_num_edit);
        if (currentBookUri == null) {
            setTitle("Add a book");
        } else {
            setTitle("Edit book");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ContentValues values = new ContentValues();
        values.put(BooksEntry.COLUMN_PRODUCT_NAME, String.valueOf(titleEdit.getText()));
        values.put(BooksEntry.COLUMN_PRICE, String.valueOf(priceEdit.getText()));
        values.put(BooksEntry.COLUMN_QUANTITY, String.valueOf(quantityEdit.getText()));
        values.put(BooksEntry.COLUMN_SUPPLIER_NAME, String.valueOf(pubNAmeEdit.getText()));
        values.put(BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER, String.valueOf(pubNumberEdit.getText()));
        values.put(BooksEntry.COLUMN_SUPPLIER_EMAIL, String.valueOf(pubEmailEdit.getText()));
        if (item.getItemId() == R.id.save && currentBookUri == null) {
            if (!checkErrors()) {
                getContentResolver().insert(BooksEntry.CONTENT_URI, values);
                finish();
            }
        } else if (item.getItemId() == R.id.save && currentBookUri != null) {
            if (!checkErrors()) {
                getContentResolver().update(currentBookUri, values, null, null);
                finish();
            }
        } else if (item.getItemId() == android.R.id.home) {
            Toast.makeText(this, "Book not saved", Toast.LENGTH_SHORT).show();
            NavUtils.navigateUpFromSameTask(this);
        }
        return true;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                BooksEntry._ID,
                BooksEntry.COLUMN_PRODUCT_NAME,
                BooksEntry.COLUMN_PRICE,
                BooksEntry.COLUMN_QUANTITY,
                BooksEntry.COLUMN_SUPPLIER_NAME,
                BooksEntry.COLUMN_SUPPLIER_EMAIL,
                BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER
        };
        return new CursorLoader(this, currentBookUri, projection
                , null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(BooksEntry.COLUMN_PRODUCT_NAME));
            int price = cursor.getInt(cursor.getColumnIndex(BooksEntry.COLUMN_PRICE));
            final int quantity = cursor.getInt(cursor.getColumnIndex(BooksEntry.COLUMN_QUANTITY));
            String pubName = cursor.getString(cursor.getColumnIndex(BooksEntry.COLUMN_SUPPLIER_NAME));
            String pubEmail = cursor.getString(cursor.getColumnIndex(BooksEntry.COLUMN_SUPPLIER_EMAIL));
            String pubNum = cursor.getString(cursor.getColumnIndex(BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER));
            titleEdit.setText(title);
            priceEdit.setText(String.valueOf(price));
            pubNAmeEdit.setText(pubName);
            pubEmailEdit.setText(pubEmail);
            pubNumberEdit.setText(pubNum);
            currentQuantity = quantity;
            quantityEdit.setText(String.valueOf(currentQuantity));
            sell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentQuantity > 0) {
                        --currentQuantity;
                        quantityEdit.setText(String.valueOf(currentQuantity));
                    } else {
                        Toast.makeText(EditorActivity.this, "Books are out of stock already", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ++currentQuantity;
                    quantityEdit.setText(String.valueOf(currentQuantity));
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getContentResolver().delete(currentBookUri, null, null);
                    finish();
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public boolean checkErrors() {
        if (TextUtils.isEmpty(titleEdit.getText())) {
            Toast.makeText(this, "Book title can't be empty", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (TextUtils.isEmpty(priceEdit.getText())) {
            Toast.makeText(this, "Book must have price", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (TextUtils.isEmpty(quantityEdit.getText())) {
            Toast.makeText(this, "Enter quantity", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (TextUtils.isEmpty(pubNAmeEdit.getText())) {
            Toast.makeText(this, "Enter supplier name", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (TextUtils.isEmpty(pubEmailEdit.getText())) {
            Toast.makeText(this, "Enter supplier email", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (TextUtils.isEmpty(pubNumberEdit.getText())) {
            Toast.makeText(this, "Enter supplier number", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void showEditors() {
        sellNBuy.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);
    }
}
