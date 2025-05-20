package com.example.lostfoundapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class LostFoundDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "lostfound.db";
    private static final int DB_VERSION = 2;

    public static final String TABLE_NAME = "items";
    public static final String COL_ID    = "_id";
    public static final String COL_NAME  = "name";
    public static final String COL_PHONE = "phone";
    public static final String COL_DESC  = "description";
    public static final String COL_DATE  = "date";
    public static final String COL_LOC   = "location";
    public static final String COL_LAT   = "latitude";
    public static final String COL_LNG   = "longitude";
    public static final String COL_TYPE  = "type";

    public LostFoundDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME  + " TEXT, "
                + COL_PHONE + " TEXT, "
                + COL_DESC  + " TEXT, "
                + COL_DATE  + " TEXT, "
                + COL_LOC   + " TEXT, "
                + COL_LAT   + " REAL, "
                + COL_LNG   + " REAL, "
                + COL_TYPE  + " TEXT"
                + ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        if (oldV < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COL_LAT + " REAL;");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COL_LNG + " REAL;");
        }
    }

    public long addItem(LostFoundItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, item.getName());
        cv.put(COL_PHONE, item.getPhone());
        cv.put(COL_DESC, item.getDescription());
        cv.put(COL_DATE, item.getDate());
        cv.put(COL_LOC, item.getLocation());
        cv.put(COL_LAT,  item.getLatitude());
        cv.put(COL_LNG,  item.getLongitude());
        cv.put(COL_TYPE, item.getType());
        long id = db.insert(TABLE_NAME, null, cv);
        db.close();
        return id;
    }

    public List<LostFoundItem> getAllItems() {
        List<LostFoundItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        while (c.moveToNext()) {
            list.add(new LostFoundItem(
                    c.getLong   (c.getColumnIndexOrThrow(COL_ID)),
                    c.getString (c.getColumnIndexOrThrow(COL_NAME)),
                    c.getString (c.getColumnIndexOrThrow(COL_PHONE)),
                    c.getString (c.getColumnIndexOrThrow(COL_DESC)),
                    c.getString (c.getColumnIndexOrThrow(COL_DATE)),
                    c.getString (c.getColumnIndexOrThrow(COL_LOC)),
                    c.getDouble (c.getColumnIndexOrThrow(COL_LAT)),
                    c.getDouble (c.getColumnIndexOrThrow(COL_LNG)),
                    c.getString (c.getColumnIndexOrThrow(COL_TYPE))
            ));
        }
        c.close();
        db.close();
        return list;
    }

    /** Returns a single item by its ID, or null if not found */
    public LostFoundItem getItemById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_NAME,
                null,
                COL_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null
        );
        LostFoundItem item = null;
        if (c.moveToFirst()) {
            item = new LostFoundItem(
                    c.getLong   (c.getColumnIndexOrThrow(COL_ID)),
                    c.getString (c.getColumnIndexOrThrow(COL_NAME)),
                    c.getString (c.getColumnIndexOrThrow(COL_PHONE)),
                    c.getString (c.getColumnIndexOrThrow(COL_DESC)),
                    c.getString (c.getColumnIndexOrThrow(COL_DATE)),
                    c.getString (c.getColumnIndexOrThrow(COL_LOC)),
                    c.getDouble (c.getColumnIndexOrThrow(COL_LAT)),
                    c.getDouble (c.getColumnIndexOrThrow(COL_LNG)),
                    c.getString (c.getColumnIndexOrThrow(COL_TYPE))
            );
        }
        c.close();
        db.close();
        return item;
    }

    /** Deletes a single item by its ID */
    public void deleteItem(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
