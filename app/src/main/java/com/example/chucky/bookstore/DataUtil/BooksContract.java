package com.example.chucky.bookstore.DataUtil;

/**
 * Created by chucky on 2/2/18.
 */

public final class BooksContract {
    private BooksContract(){}

    public static class BooksEntry{
        public static final String TABLE_NAME="books";
        public static final String COLUMN_PRODUCT_NAME="product_name";
        public static final String COLUMN_PRICE="price";
        public static final String COLUMN_QUANTITY="quantity";
        public static final String COLUMN_SUPPLIER_NAME="supplier_name";
        public static final String COLUMN_SUPPLIER_EMAIL="supplier_email";
        public static final String COLUMN_SUPPLIER_PHONE_NUMBER="supplier_phone_num";
    }

}
