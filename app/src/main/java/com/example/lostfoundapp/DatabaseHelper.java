package com.example.lostfoundapp;

import android.content.Context;

import androidx.room.Room;

public class DatabaseHelper {

    private static final String DATABASE_NAME = "lost_and_found_database3";

    // Singleton instance
    private static DatabaseHelper instance;

    // Your Room database
    private final LostAndFoundDatabase database;

    // Private constructor to enforce singleton
    private DatabaseHelper(Context context) {
        // Use application context to avoid leaking an Activity
        database = Room.databaseBuilder(
                        context.getApplicationContext(),
                        LostAndFoundDatabase.class,
                        DATABASE_NAME
                )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()  // consider removing for real apps!
                .build();
    }

    /**
     * Get the singleton instance of DatabaseHelper.
     * @param context any Context (Activity, Service, etc.)
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    /**
     * Expose your Room database.
     */
    public LostAndFoundDatabase getDatabase() {
        return database;
    }
}
