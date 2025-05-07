package com.example.lostfoundapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class LostFoundDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME    = "lost_found.db";
    private static final int    DATABASE_VERSION = 1;

    // Table and columns
    private static final String TABLE_NAME       = "items";
    private static final String KEY_ID           = "id";
    private static final String KEY_TITLE        = "title";
    private static final String KEY_DESCRIPTION  = "description";
    private static final String KEY_LOCATION     = "location";
    private static final String KEY_TYPE         = "type";
    private static final String KEY_CONTACT      = "contact";

    public LostFoundDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE       + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_LOCATION    + " TEXT,"
                + KEY_TYPE        + " TEXT,"
                + KEY_CONTACT     + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addItem(LostFoundItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE,       item.getTitle());
        cv.put(KEY_DESCRIPTION, item.getDescription());
        cv.put(KEY_LOCATION,    item.getLocation());
        cv.put(KEY_TYPE,        item.getType());
        cv.put(KEY_CONTACT,     item.getContact());
        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    public List<LostFoundItem> getAllItems() {
        List<LostFoundItem> list = new ArrayList<>();
        String q = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(q, null);
        if (c.moveToFirst()) {
            do {
                LostFoundItem i = new LostFoundItem(
                        c.getInt(c.getColumnIndexOrThrow(KEY_ID)),
                        c.getString(c.getColumnIndexOrThrow(KEY_TITLE)),
                        c.getString(c.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                        c.getString(c.getColumnIndexOrThrow(KEY_LOCATION)),
                        c.getString(c.getColumnIndexOrThrow(KEY_TYPE)),
                        c.getString(c.getColumnIndexOrThrow(KEY_CONTACT))
                );
                list.add(i);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }

    public LostFoundItem getItemById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_NAME,
                null,
                KEY_ID + "=?",
                new String[]{ String.valueOf(id) },
                null, null, null
        );
        LostFoundItem item = null;
        if (c.moveToFirst()) {
            item = new LostFoundItem(
                    c.getInt(c.getColumnIndexOrThrow(KEY_ID)),
                    c.getString(c.getColumnIndexOrThrow(KEY_TITLE)),
                    c.getString(c.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                    c.getString(c.getColumnIndexOrThrow(KEY_LOCATION)),
                    c.getString(c.getColumnIndexOrThrow(KEY_TYPE)),
                    c.getString(c.getColumnIndexOrThrow(KEY_CONTACT))
            );
        }
        c.close();
        db.close();
        return item;
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAllItems() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}
