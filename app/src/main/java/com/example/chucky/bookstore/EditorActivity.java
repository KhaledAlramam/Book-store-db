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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.chucky.bookstore.DataUtil.BooksContract.BooksEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri currentBookUri;
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection={
                BooksEntry._ID,
                BooksEntry.COLUMN_PRODUCT_NAME,
                BooksEntry.COLUMN_PRICE,
                BooksEntry.COLUMN_QUANTITY,
                BooksEntry.COLUMN_SUPPLIER_NAME,
                BooksEntry.COLUMN_SUPPLIER_EMAIL,
                BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER
        };

        return new CursorLoader(this,currentBookUri,projection
        ,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()){
            String title=cursor.getString(cursor.getColumnIndex(BooksEntry.COLUMN_PRODUCT_NAME));
            int price=cursor.getInt(cursor.getColumnIndex(BooksEntry.COLUMN_PRICE));
            int quantity = cursor.getInt(cursor.getColumnIndex(BooksEntry.COLUMN_QUANTITY));
            String pubName=cursor.getString(cursor.getColumnIndex(BooksEntry.COLUMN_SUPPLIER_NAME));
            String pubEmail=cursor.getString(cursor.getColumnIndex(BooksEntry.COLUMN_SUPPLIER_EMAIL));
            String pubNum=cursor.getString(cursor.getColumnIndex(BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER));
            titleEdit.setText(title);
            priceEdit.setText(String.valueOf(price));
            quantityEdit.setText(String.valueOf(quantity));
            pubNAmeEdit.setText(pubName);
            pubEmailEdit.setText(pubEmail);
            pubNumberEdit.setText(pubNum);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    EditText titleEdit;
    EditText priceEdit;
    EditText quantityEdit;
    EditText pubNAmeEdit;
    EditText pubEmailEdit;
    EditText pubNumberEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        currentBookUri=getIntent().getData();
        if (currentBookUri==null){
            setTitle("Add a book");
        }else {
            setTitle("Edit book");
        }
        titleEdit=findViewById(R.id.title_edit);
        priceEdit =findViewById(R.id.price_edit);
        quantityEdit =findViewById(R.id.quantity_edit);
        pubNAmeEdit =findViewById(R.id.publisher_name_edit);
        pubEmailEdit =findViewById(R.id.publisher_email_edit);
        pubNumberEdit =findViewById(R.id.publisher_num_edit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.edit_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ContentValues values=new ContentValues();
        values.put(BooksEntry.COLUMN_PRODUCT_NAME, String.valueOf(titleEdit.getText()));
        values.put(BooksEntry.COLUMN_PRICE, String.valueOf(priceEdit.getText()));
        values.put(BooksEntry.COLUMN_QUANTITY, String.valueOf(quantityEdit.getText()));
        values.put(BooksEntry.COLUMN_SUPPLIER_NAME, String.valueOf(pubNAmeEdit.getText()));
        values.put(BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER, String.valueOf(pubNumberEdit.getText()));
        values.put(BooksEntry.COLUMN_SUPPLIER_EMAIL, String.valueOf(pubEmailEdit.getText()));
        if (item.getItemId()==R.id.save&&currentBookUri==null){
            getContentResolver().insert(BooksEntry.CONTENT_URI,values);
            finish();
        }else if (item.getItemId()==R.id.save&&currentBookUri!=null){
            getContentResolver().update(currentBookUri,values,null,null);
        }else if (item.getItemId()==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);

        }
        return true;
    }
}
