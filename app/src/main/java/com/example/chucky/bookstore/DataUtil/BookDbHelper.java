package com.example.chucky.bookstore.DataUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.chucky.bookstore.DataUtil.BooksContract.BooksEntry;

/**
 * Created by chucky on 2/2/18.
 */

public class BookDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BookStore.db";


    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create Books table string
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + BooksContract.BooksEntry.TABLE_NAME + " (" +
                        BooksEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        BooksEntry.COLUMN_PRODUCT_NAME + " TEXT, " +
                        BooksEntry.COLUMN_PRICE + " INTEGER," +
                        BooksEntry.COLUMN_QUANTITY + " INTEGER, " +
                        BooksEntry.COLUMN_SUPPLIER_NAME + " TEXT, " +
                        BooksEntry.COLUMN_SUPPLIER_EMAIL + " TEXT, " +
                        BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " TEXT);";
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //Delete Books table string
        String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + BooksEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
