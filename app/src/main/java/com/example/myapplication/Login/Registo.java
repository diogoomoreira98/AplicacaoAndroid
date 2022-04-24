package com.example.myapplication.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Inicio.Inicio;
import com.example.myapplication.R;

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
                if (checkDataEntered())
                {
                    Intent mRegisto= new Intent(getApplicationContext(), Inicio.class);
                     startActivity(mRegisto);
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