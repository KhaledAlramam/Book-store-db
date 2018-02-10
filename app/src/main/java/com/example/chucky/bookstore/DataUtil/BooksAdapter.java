package com.example.chucky.bookstore.DataUtil;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chucky.bookstore.DataUtil.BooksContract.BooksEntry;
import com.example.chucky.bookstore.DetailsActivity;
import com.example.chucky.bookstore.R;

/**
 * Created by chucky on 2/7/18.
 */

public class BooksAdapter extends CursorAdapter {

    public BooksAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    String name;
    int quantity;
    int price;
    String currentPub;
    String pubNum;
    String pubEmail;
    int currentId;

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        final TextView bookTitle = view.findViewById(R.id.book_name_item);
        final TextView bookPrice = view.findViewById(R.id.book_price_item);
        TextView bookQuantity = view.findViewById(R.id.book_quantity_item);
        final TextView sellView = view.findViewById(R.id.sell);
        TextView detailsView=view.findViewById(R.id.details);
        name = cursor.getString(cursor.getColumnIndex(BooksEntry.COLUMN_PRODUCT_NAME));
        price = cursor.getInt(cursor.getColumnIndex(BooksEntry.COLUMN_PRICE));
        quantity = cursor.getInt(cursor.getColumnIndex(BooksEntry.COLUMN_QUANTITY));
        currentPub = cursor.getString(cursor.getColumnIndex(BooksEntry.COLUMN_SUPPLIER_NAME));
        pubNum = cursor.getString(cursor.getColumnIndex(BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER));
        pubEmail = cursor.getString(cursor.getColumnIndex(BooksEntry.COLUMN_SUPPLIER_EMAIL));
        currentId=cursor.getInt(cursor.getColumnIndex(BooksEntry._ID));
        bookTitle.setText(name);
        bookPrice.setText(String.valueOf(price));
        bookQuantity.setText(String.valueOf(quantity));
        final String selection = BooksEntry._ID + " =?";
        final String[] selectionArgs = new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(BooksEntry._ID)))};
        sellView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 0) {
                    --quantity;
                    ContentValues values = new ContentValues();
                    values.put(BooksEntry.COLUMN_PRODUCT_NAME, String.valueOf(bookTitle.getText()));
                    values.put(BooksEntry.COLUMN_PRICE, String.valueOf(bookPrice.getText()));
                    values.put(BooksEntry.COLUMN_QUANTITY, String.valueOf(quantity));
                    values.put(BooksEntry.COLUMN_SUPPLIER_NAME, currentPub);
                    values.put(BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER, pubNum);
                    values.put(BooksEntry.COLUMN_SUPPLIER_EMAIL, pubEmail);
                    context.getContentResolver().update(BooksEntry.CONTENT_URI, values, selection, selectionArgs);
                } else {
                    Toast.makeText(context, "Books are out of stock already", Toast.LENGTH_SHORT).show();
                }
            }
        });
        detailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DetailsActivity.class);
                intent.putExtra(BooksEntry._ID,currentId);
                intent.putExtra(BooksEntry.COLUMN_PRODUCT_NAME,name);
                intent.putExtra(BooksEntry.COLUMN_PRICE,price);
                intent.putExtra(BooksEntry.COLUMN_QUANTITY,quantity);
                intent.putExtra(BooksEntry.COLUMN_SUPPLIER_NAME,currentPub);
                intent.putExtra(BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER,pubNum);
                intent.putExtra(BooksEntry.COLUMN_SUPPLIER_EMAIL,pubEmail);
                context.startActivity(intent);
            }
        });
    }
}
