package com.example.myapplication.Login;

import android.provider.BaseColumns;

public final class SaveDataContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private SaveDataContract() {}

    /* Inner class that defines the table contents */
    public static class SaveData implements BaseColumns {
        public static final String TABLE_NAME = "UserData";
        public static final String ID_USER = "IDUser";
        public static final String TOKEN = "Token";
    }
}