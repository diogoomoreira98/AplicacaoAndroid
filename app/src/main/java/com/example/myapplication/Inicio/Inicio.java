package com.example.myapplication.Inicio;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.Inicio.Fragmentos.Calendario;
import com.example.myapplication.Inicio.Fragmentos.Notificacoes;
import com.example.myapplication.Inicio.Fragmentos.Reserva;
import com.example.myapplication.Inicio.Fragmentos.Reservas;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;


public class Inicio extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Reservas reservasFragment = new Reservas();
    Reserva reservaFragment = new Reserva();
    Notificacoes notificacoesFragment = new Notificacoes();
    Calendario calendarioFragment = new Calendario();

   //SCAN
    Button btnScan;
    String[] permissions = {
            Manifest.permission.CAMERA
    };
    int PERM_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, reservasFragment).commit();

       bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId())
               {
                   case R.id.reservas:
                       getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,reservasFragment).commit();
                       return true;
                   case R.id.reserva:
                       getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,reservaFragment).commit();
                       return true;
                   case R.id.notificacoes:
                       getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,notificacoesFragment).commit();
                       return true;
                   case R.id.calendario:
                       getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,calendarioFragment).commit();
                       return true;
               }
               return false;
           }
       });

       //Botão de scan
        btnScan = (Button) findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkpermissions()) {
                    startActivity(new Intent(getApplicationContext(), ScannerActivity.class));
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "faltam permissões", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }
    private boolean checkpermissions(){
        List<String> listofpermisssions = new ArrayList<>();
        for (String perm: permissions){
            if (ContextCompat.checkSelfPermission(getApplicationContext(), perm) != PackageManager.PERMISSION_GRANTED){
                listofpermisssions.add(perm);
            }
        }
        if (!listofpermisssions.isEmpty()){
            ActivityCompat.requestPermissions(this, listofpermisssions.toArray(new String[listofpermisssions.size()]), PERM_CODE);
            return false;
        }
        return true;
    }

}





