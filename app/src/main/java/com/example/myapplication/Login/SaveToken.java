package com.example.myapplication.Login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SaveToken extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SaveTokens";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_TOKEN = "token";

    // Post Table Columns
    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID_FK = "userId";
    private static final String KEY_TOKEN = "text";


    public SaveToken(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_TOKEN +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_USER_ID_FK + " INTEGER REFERENCES " + // Define a foreign key
                KEY_TOKEN +
                ")";

        db.execSQL(CREATE_POSTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOKEN);
            onCreate(db);
        }
    }
}
