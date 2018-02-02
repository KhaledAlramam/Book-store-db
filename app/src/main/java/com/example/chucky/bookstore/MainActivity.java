package com.example.chucky.bookstore;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.chucky.bookstore.DataUtil.BookDbHelper;
import com.example.chucky.bookstore.DataUtil.BooksContract.BooksEntry;

public class MainActivity extends AppCompatActivity {

    BookDbHelper dpHelper;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dpHelper=new BookDbHelper(this);
        title="Lord of the rings2";
        insertData();
    }

    public void insertData(){
        // Gets the data repository in write mode
        SQLiteDatabase db = dpHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BooksEntry.COLUMN_PRODUCT_NAME,title);
        values.put(BooksEntry.COLUMN_PRICE,100);
        values.put(BooksEntry.COLUMN_QUANTITY,20);
        values.put(BooksEntry.COLUMN_SUPPLIER_NAME,"John");
        values.put(BooksEntry.COLUMN_SUPPLIER_EMAIL,"example@test.com");
        values.put(BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER,"002121212");
        long newId=db.insert(BooksEntry.TABLE_NAME,null,values);
        Toast.makeText(this,String.valueOf(newId),Toast.LENGTH_SHORT).show();
    }

    public Cursor queryData(){
        SQLiteDatabase dp=dpHelper.getReadableDatabase();
        String[] projection={
                BooksEntry.COLUMN_PRODUCT_NAME,
                BooksEntry.COLUMN_PRICE,
                BooksEntry.COLUMN_QUANTITY,
                BooksEntry.COLUMN_SUPPLIER_NAME,
                BooksEntry.COLUMN_SUPPLIER_EMAIL,
                BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER
        };
        Cursor cursor= dp.query(BooksEntry.TABLE_NAME,
                projection,null,null,
                null,null,null);
        return cursor;
    }

}
