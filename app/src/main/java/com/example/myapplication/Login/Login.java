package com.example.myapplication.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.Inicio.Inicio;
import com.example.myapplication.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Button button = (Button) findViewById(R.id.entrar);
        Button registo = (Button) findViewById(R.id.registar);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // mudar para atividade inicio
                Intent mInicio = new Intent(getApplicationContext(), Inicio.class);
                startActivity(mInicio);
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

}