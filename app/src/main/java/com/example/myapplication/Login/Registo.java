package com.example.myapplication.Login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Inicio.Inicio;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Registo extends AppCompatActivity {

    EditText email;
    EditText nome;
    EditText password;
    EditText c_password;
    AutoCompleteTextView autoCompleteTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);

        //Selecionar centro
        autoCompleteTextView = findViewById(R.id.editCentro);
        String[]option = {"Viseu", "Algarve", "Tondela","França"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(Registo.this,R.layout.centros_item , option);
        autoCompleteTextView.setAdapter(arrayAdapter);


        email = findViewById(R.id.editEmail);
        nome = findViewById(R.id.editNome);
        password = findViewById(R.id.editPassword);
        c_password = findViewById(R.id.editConPassword);

        Button f_registo = (Button) findViewById(R.id.fazer_registo);

        f_registo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Validar campos
                // mudar para atividade Inicio
              //  if (checkDataEntered())
                if(true)
                {

                   // String ROUTER = "/auth/register";
                    String URL = "http://176.79.161.72:5000/auth/register";//+ROUTER;

                    JSONObject request = new JSONObject();

                    try {
                        request.put("Nome", "diogo");
                        request.put("Email", "diogo@gmail.com");
                        request.put("IDCentro", 1);
                        request.put("Password", "Escola1234?");



                            RequestQueue requestQueue = Volley.newRequestQueue(Registo.this);
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                    Request.Method.POST,
                                    URL,
                                    request,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                                            if(response.opt("status").toString()=="success"){
                                                //se o resgito se verficar entao entra na pagina principal
                                                Intent mRegisto= new Intent(getApplicationContext(), Inicio.class);
                                                startActivity(mRegisto);
                                            }else{
                                                Toast.makeText(getApplicationContext(), response.opt("data").toString(),Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                                }
                            }
                            );
                            requestQueue.add(jsonObjectRequest);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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