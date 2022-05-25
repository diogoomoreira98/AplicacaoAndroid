package com.example.myapplication.Utils.VolleyAPI;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.myapplication.Login.SaveDataContract;
import com.example.myapplication.Login.SaveDataDbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomJsonRequest extends Request<JSONObject> {
    private Response.Listener<JSONObject> response;
    private Map<String, String> params;
    private Context context;
    public CustomJsonRequest(Context context,int method, String url, Map<String, String> params, Response.Listener<JSONObject> response, Response.ErrorListener onErrorResponse) {
        super(method, ("http://176.79.161.72:5000"+url), onErrorResponse);
        this.params = params;
        this.response = response;
        this.context = context;
    }

    @Nullable
    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String js = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return (Response.success(new JSONObject(js), HttpHeaderParser.parseCacheHeaders(response)));
        } catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        this.response.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        SaveDataDbHelper dbHelper = new SaveDataDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {SaveDataContract.SaveData.ID_USER, SaveDataContract.SaveData.TOKEN,};
        Cursor cursor = db.query(SaveDataContract.SaveData.TABLE_NAME, projection, null, null, null, null, null);
        int itemId;
        String token = "";
        while(cursor.moveToNext()) {
            itemId = cursor.getInt(cursor.getColumnIndexOrThrow(SaveDataContract.SaveData.ID_USER));
            token = cursor.getString(cursor.getColumnIndexOrThrow(SaveDataContract.SaveData.TOKEN));
        }
        cursor.close();

        String bearer = "Bearer ".concat(token);
        Map<String, String> headersSys = super.getHeaders();
        Map<String, String> headers = new HashMap<String, String>();
        headersSys.remove("Authorization");
        headers.put("Authorization", bearer);
        headers.putAll(headersSys);
        return headers;
    }
}

