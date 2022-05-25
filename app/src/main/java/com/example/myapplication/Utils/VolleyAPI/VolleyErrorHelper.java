package com.example.myapplication.Utils.VolleyAPI;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.myapplication.Inicio.Inicio;
import com.example.myapplication.Login.Login;
import com.example.myapplication.Login.SaveDataContract;
import com.example.myapplication.Login.SaveDataDbHelper;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class VolleyErrorHelper {
    public static String getMessage(VolleyError error, Activity app) {
        try {
            if (error.networkResponse != null) {
                if (error.networkResponse.statusCode == 403){
                    try {
                        SaveDataDbHelper dbHelper = new SaveDataDbHelper(app.getApplicationContext());
                        SQLiteDatabase db = dbHelper.getReadableDatabase();

                        String[] projection = {SaveDataContract.SaveData.ID_USER, SaveDataContract.SaveData.TOKEN};
                        Cursor cursor = db.query(SaveDataContract.SaveData.TABLE_NAME,projection,null,null,null,null,null);
                        List dados = new ArrayList<>();
                        while(cursor.moveToNext()) {
                            long itemId = cursor.getInt(cursor.getColumnIndexOrThrow(SaveDataContract.SaveData.ID_USER));
                            String token = cursor.getString(cursor.getColumnIndexOrThrow(SaveDataContract.SaveData.TOKEN));
                            dados.add(itemId);
                            dados.add(token);
                        }
                        cursor.close();
                        if(!dados.isEmpty()){
                            int deletedRows = db.delete(SaveDataContract.SaveData.TABLE_NAME, null, null);
                            //app.startActivity(new Intent(app.getApplicationContext(), Login.class));
                            app.finish();
                            Toast.makeText(app.getApplicationContext(), "Logout efetuado com sucesso", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        System.out.println(e);
                    }

                    return "Sessão expirada";
                }else{
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject data = new JSONObject(responseBody);
                    String message = data.optString("msg");
                    return message;
                }
            }
        } catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
        }
        return "Falha de conectividade";
    }
}
