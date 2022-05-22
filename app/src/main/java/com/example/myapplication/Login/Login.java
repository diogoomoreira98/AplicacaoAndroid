package com.example.myapplication.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Button button = (Button) findViewById(R.id.entrar);
        Button registo = (Button) findViewById(R.id.registar);

        EditText email = (EditText)findViewById(R.id.loginEmail);
        EditText password = (EditText)findViewById(R.id.loginPassword);
        email.setText("admin@admin.com");
        password.setText("Admin1234");

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText email = (EditText)findViewById(R.id.loginEmail);
                EditText password = (EditText)findViewById(R.id.loginPassword);


                if(isEmail(email) && isPassword(password)){
                    //rest api login
                    String URL = "http://176.79.161.72:5000/auth/login";
                    JSONObject request = new JSONObject();

                    try {
                        request.put("Email", email.getText());
                        request.put("Password", password.getText());
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Erro de envio de credenciais",Toast.LENGTH_LONG).show();
                        return;
                    }

                    RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        URL,
                        request,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String token = response.optString("token");
                                boolean isDefault = response.optBoolean("isDefault");
                                if(isDefault)
                                {
                                    startActivity(new Intent(getApplicationContext(), NovaPass.class));
                                }else {
                                    startActivity(new Intent(getApplicationContext(), Inicio.class));
                                    Toast.makeText(getApplicationContext(), response.optString("msg"), Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject data = new JSONObject(responseBody);
                                String message = data.optString("msg");
                                Toast.makeText(getApplicationContext(), message ,Toast.LENGTH_LONG).show();
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                }
            }
        });

        registo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // mudar para atividade registo
                Intent mRegisto= new Intent(getApplicationContext(), Registo.class);
                startActivity(mRegisto);
            }
        });
    }

    //Verificar se o campo é email
    boolean isEmail(EditText text){
        CharSequence email = text.getText().toString();
        return !(TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    //Verificar se o campo não está vazio
    boolean isPassword(EditText text){
        CharSequence str = text.getText().toString();
        if (str.length()<6){
            return false;
        }
        return true;
    }

}