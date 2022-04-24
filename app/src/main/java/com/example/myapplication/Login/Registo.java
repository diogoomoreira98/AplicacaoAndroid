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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import org.json.JSONException;
import org.json.JSONObject;

public class Registo extends AppCompatActivity {

    EditText email;
    EditText nome;
    EditText password;
    EditText c_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);

        email = findViewById(R.id.editEmail);
        nome = findViewById(R.id.editNome);
        password = findViewById(R.id.editPassword);
        c_password = findViewById(R.id.editConPassword);

        Button f_registo = (Button) findViewById(R.id.fazer_registo);

        f_registo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Validar campos
                // mudar para atividade Inicio
                //if (checkDataEntered())
                if (true)
                {
                    /*
                    String ROUTER = "/register";
                    String URL = "176.79.161.72:3000"+ROUTER;

                    JSONObject request = new JSONObject();
                    request.put("username", nome.getText());
                    request.put("email", nome.getText());
                    request.put("centro", 1);
                    request.put("confirm_password", c_password.getText());
                    */
                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(Registo.this);
                    String ROUTER = "/register";
                    String url = "176.79.161.72:3000"+ROUTER;

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    Toast.makeText(Registo.this, response, Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Registo.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });

                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);


                }

            }
        });
    }

    //Verificar se é email
    boolean isEmail(EditText text){
        CharSequence email = text.getText().toString();
        System.out.println(Patterns.EMAIL_ADDRESS.matcher(email).matches());
        return (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
    //Verificar se o campo não está vazio
    boolean isEmpty(EditText text){
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    public boolean  checkDataEntered(){
        boolean check = true;

        if (isEmpty(nome)){
            nome.setError("Introduz um nome");
            check = false;
        }
        if (isEmpty(password)){
            password.setError("Introduz uma password válida");
            check = false;
        }
        if (isEmpty(c_password)){
            c_password.setError("Confirmação de password incorreta");
            check = false;
        }
        if (isEmail(email)){
            email.setError("Introduza um email");
            check = false;
        }


        return check;
    }
}