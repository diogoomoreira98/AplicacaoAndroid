package com.example.myapplication.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Inicio.Inicio;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class NovaPass extends AppCompatActivity {

    Button alterar_pass;
    EditText confirmar_pass, nova_pass;
    String token, iduser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_pass);
        Intent intent = getIntent();

        token = intent.getStringExtra("token");
        iduser = intent.getStringExtra("IDUser");

        alterar_pass = (Button) findViewById(R.id.alterar_pass);
        confirmar_pass = (EditText) findViewById(R.id.confirmar_pass);
        nova_pass = (EditText) findViewById(R.id.nova_pass);

        alterar_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strPass1 = nova_pass.getText().toString();
                String strPass2 = confirmar_pass.getText().toString();
                if(strPass1.equals(strPass2)){

                //rest api login
                String URL = "http://176.79.161.72:5000/auth/update_password_android";
                JSONObject request = new JSONObject();



                try {
                    request.put("Password", confirmar_pass.getText());
                    request.put("IDUtilizador",iduser+"");
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Erro de envio de credenciais",Toast.LENGTH_LONG).show();
                    return;
                }

                RequestQueue requestQueue = Volley.newRequestQueue(NovaPass.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.PUT,
                        URL,
                        request,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                SaveDataDbHelper dbHelper = new SaveDataDbHelper(NovaPass.this);
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(SaveDataContract.SaveData.ID_USER, iduser);
                                values.put(SaveDataContract.SaveData.TOKEN, token);
                                long newRowId = db.insert(SaveDataContract.SaveData.TABLE_NAME, null, values);



                                startActivity(new Intent(getApplicationContext(), Inicio.class));
                                Toast.makeText(getApplicationContext(), response.optString("msg"), Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            if(error.networkResponse!=null) {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject data = new JSONObject(responseBody);
                                String message = data.optString("msg");
                                Toast.makeText(getApplicationContext(), message ,Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(NovaPass.this, "Erro de ligação ao servidor", Toast.LENGTH_LONG).show();
                            }
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                requestQueue.add(jsonObjectRequest);

                }else{
                    Toast.makeText(NovaPass.this, "As password não coincidem!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}