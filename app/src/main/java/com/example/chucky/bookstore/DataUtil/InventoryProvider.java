package com.example.chucky.bookstore.DataUtil;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.chucky.bookstore.DataUtil.BooksContract.BooksEntry;

/**
 * Created by chucky on 2/6/18.
 */

public class InventoryProvider extends ContentProvider {

    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();
    public static final int BOOKS = 100;
    public static final int BOOK_ID = 101;
    private BookDbHelper mDpHelper;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(BooksContract.CONTENT_AUTHORITY,
                BooksContract.PATH_BOOKS, BOOKS);
        sUriMatcher.addURI(BooksContract.CONTENT_AUTHORITY,
                BooksContract.PATH_BOOKS + "/#", BOOK_ID);
    }


    @Override
    public boolean onCreate() {
        mDpHelper = new BookDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection
            , @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase dp = mDpHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                cursor = dp.query(BooksEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ID:
                selection = BooksEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = dp.query(BooksEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Can't query Unknwon Uri " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return BooksEntry.CONTENT_LIST_TYPE;
            case BOOK_ID:
                return BooksEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return insetBook(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    public Uri insetBook(Uri uri, ContentValues contentValues) {
        String name = contentValues.getAsString(BooksEntry.COLUMN_PRODUCT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Book name required..!");
        }
        int price = contentValues.getAsInteger(BooksEntry.COLUMN_PRICE);
        if (price == 0) {
            throw new IllegalArgumentException("Price can't be 0");
        }
        String supName = contentValues.getAsString(BooksEntry.COLUMN_SUPPLIER_NAME);
        if (supName == null) {
            throw new IllegalArgumentException("Supplier name required..!");
        }
        String supEmail = contentValues.getAsString(BooksEntry.COLUMN_SUPPLIER_EMAIL);
        if (supEmail == null) {
            throw new IllegalArgumentException("Supplier email required..!");
        }
        String supNum = contentValues.getAsString(BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
        if (supNum == null) {
            throw new IllegalArgumentException("Supplier number required..!");
        }
        SQLiteDatabase db = mDpHelper.getWritableDatabase();
        long id = db.insert(BooksEntry.TABLE_NAME, null, contentValues);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        int rowsDeleted;
        SQLiteDatabase dp = mDpHelper.getWritableDatabase();
        switch (match) {
            case BOOKS:
                rowsDeleted = dp.delete(BooksEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOK_ID:
                selection = BooksEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = dp.delete(BooksEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase dp = mDpHelper.getWritableDatabase();
        int rows = 0;
        switch (match) {
            case BOOKS:
                rows = dp.update(BooksEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case BOOK_ID:
                selection = BooksEntry._ID + " =?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rows = dp.update(BooksEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rows;
    }

}
