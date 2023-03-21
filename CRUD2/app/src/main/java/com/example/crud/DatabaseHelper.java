package com.example.crud;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myproduct.sqlite";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_EXPIRATION_DATE = "expiration_date";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_IMAGE_URI = "image_uri";

    private static final String CREATE_PRODUCTS_TABLE =
            "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_UNIT + "  TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_EXPIRATION_DATE + " TEXT, " +
                    COLUMN_QUANTITY + " INTEGER, " +
                    COLUMN_IMAGE_URI + " TEXT " +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add product to the database
    public long addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_UNIT, product.getUnit());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_EXPIRATION_DATE, product.getExpirationDate());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        values.put(COLUMN_IMAGE_URI, product.getImageUri());

        long id = db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        return id;
    }

    @SuppressLint("Range")
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_PRODUCTS,
                new String[] {COLUMN_ID, COLUMN_NAME, COLUMN_UNIT, COLUMN_PRICE, COLUMN_EXPIRATION_DATE, COLUMN_QUANTITY, COLUMN_IMAGE_URI},
                null,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()) {
            Product product = new Product();
            product.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            product.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            product.setUnit(cursor.getString(cursor.getColumnIndex(COLUMN_UNIT)));
            product.setPrice(cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE)));
            product.setExpirationDate(cursor.getString(cursor.getColumnIndex(COLUMN_EXPIRATION_DATE)));
            product.setQuantity(cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY)));
            product.setImageUri(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URI)));
            productList.add(product);
        }
        cursor.close();
        db.close();

        return productList;
    }

    @SuppressLint("Range")
    public Product getProduct(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_PRODUCTS,
                new String[] {COLUMN_ID, COLUMN_NAME, COLUMN_UNIT, COLUMN_PRICE, COLUMN_EXPIRATION_DATE, COLUMN_QUANTITY, COLUMN_IMAGE_URI},
                COLUMN_ID + "=?",
                new String[] {String.valueOf(id)},
                null, null, null, null
        );
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Product product = new Product();
        product.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        product.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
        product.setUnit(cursor.getString(cursor.getColumnIndex(COLUMN_UNIT)));
        product.setPrice(cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE)));
        product.setExpirationDate(cursor.getString(cursor.getColumnIndex(COLUMN_EXPIRATION_DATE)));
        product.setQuantity(cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY)));
        product.setImageUri(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URI)));
        cursor.close();
        db.close();
        return product;
    }
}
