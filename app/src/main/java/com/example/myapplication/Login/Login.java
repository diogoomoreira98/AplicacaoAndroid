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

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Button button = (Button) findViewById(R.id.entrar);
        Button registo = (Button) findViewById(R.id.registar);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText email = (EditText)findViewById(R.id.loginEmail);
                EditText password = (EditText)findViewById(R.id.loginPassword);




                if(true){
                    //rest api login



                    String URL = "http://176.79.161.72:5000/auth/login";
                    JSONObject request = new JSONObject();

                    try {
                        request.put("Email", "diogo@gmail.com");
                        request.put("Password", "Escola12334?");
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Erro de envio de credenciais",Toast.LENGTH_LONG).show();
                        return;
                    }
                    //este metodo ativa acesso a internet
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                    Request.Method.POST,
                                    URL,
                                    request,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {


                                                String Token= response.optString("Token");

                                                //se o sucesso se verficar entao entra na pagina principal
                                                Intent mInicio = new Intent(getApplicationContext(), Inicio.class);
                                                startActivity(mInicio);

                                                Toast.makeText(getApplicationContext(), response.optString("msg"),Toast.LENGTH_LONG).show();


                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_LONG).show();
                                }
                            }
                            );
                            requestQueue.add(jsonObjectRequest);
                        }
                    });


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
        return (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches());
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