package com.example.myapplication.Utils.LocalDataDB;

import android.provider.BaseColumns;

public final class SaveDataContract {
    private SaveDataContract() {}

    public static class SaveData implements BaseColumns {
        public static final String TABLE_NAME = "UserData";
        public static final String ID_USER = "IDUser";
        public static final String TOKEN = "Token";
    }
}