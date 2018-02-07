package com.example.chucky.bookstore;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.chucky.bookstore.DataUtil.BookDbHelper;
import com.example.chucky.bookstore.DataUtil.BooksAdapter;
import com.example.chucky.bookstore.DataUtil.BooksContract.BooksEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID=0;
    BooksAdapter booksAdapter;
    BookDbHelper dpHelper;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dpHelper=new BookDbHelper(this);

        ListView listView=findViewById(R.id.list_view);
        booksAdapter=new BooksAdapter(this, null);
        listView.setAdapter(booksAdapter);
        listView.setEmptyView(findViewById(R.id.empty_view));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Uri uri= ContentUris.withAppendedId(BooksEntry.CONTENT_URI,id);
                Intent intent=new Intent(MainActivity.this
                        ,EditorActivity.class);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(LOADER_ID,null,this);
    }

    public Cursor queryAllData(){
        String[] projection={
                BooksEntry._ID,
                BooksEntry.COLUMN_PRODUCT_NAME,
                BooksEntry.COLUMN_PRICE,
                BooksEntry.COLUMN_QUANTITY,
                BooksEntry.COLUMN_SUPPLIER_NAME,
                BooksEntry.COLUMN_SUPPLIER_EMAIL,
                BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER
        };
        Cursor cursor= getContentResolver().query(BooksEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
        return cursor;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.add_book){
            Intent intent=new Intent(this,EditorActivity.class);
            startActivity(intent);
        }else if(item.getItemId()==R.id.delete_all_books){
            getContentResolver().delete(BooksEntry.CONTENT_URI,null,null);
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection={
                BooksEntry._ID,
                BooksEntry.COLUMN_PRODUCT_NAME,
                BooksEntry.COLUMN_PRICE,
                BooksEntry.COLUMN_QUANTITY
        };
        return new CursorLoader(this,
                BooksEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        booksAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        booksAdapter.swapCursor(null);
    }
}
