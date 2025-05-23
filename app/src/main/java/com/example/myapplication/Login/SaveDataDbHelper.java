package com.example.myapplication.Login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;



public class SaveDataDbHelper extends SQLiteOpenHelper {
    
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SaveDataContract.SaveData.TABLE_NAME + " (" +
                    SaveDataContract.SaveData.ID_USER + " INTEGER PRIMARY KEY," +
                    SaveDataContract.SaveData.TOKEN + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SaveDataContract.SaveData.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SaveData.db";

    public SaveDataDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}



