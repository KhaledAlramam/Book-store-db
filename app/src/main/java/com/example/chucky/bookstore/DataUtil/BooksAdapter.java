package com.example.chucky.bookstore.DataUtil;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.chucky.bookstore.DataUtil.BooksContract.BooksEntry;

import com.example.chucky.bookstore.R;

/**
 * Created by chucky on 2/7/18.
 */

public class BooksAdapter extends CursorAdapter {

    public BooksAdapter(Context context,Cursor cursor){
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView bookTitle=view.findViewById(R.id.book_name_item);
        TextView bookPrice=view.findViewById(R.id.book_price_item);
        TextView bookQuantity=view.findViewById(R.id.book_quantity_item);
        String name=cursor.getString(cursor.getColumnIndex(BooksEntry.COLUMN_PRODUCT_NAME));
        int price=cursor.getInt(cursor.getColumnIndex(BooksEntry.COLUMN_PRICE));
        int quantity=cursor.getInt(cursor.getColumnIndex(BooksEntry.COLUMN_QUANTITY));
        bookTitle.setText(name);
        bookPrice.setText(String.valueOf(price));
        bookQuantity.setText(String.valueOf(quantity));
    }

}
