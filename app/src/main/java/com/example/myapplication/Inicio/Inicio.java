package com.example.myapplication.Inicio;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.Inicio.Fragmentos.Calendario;
import com.example.myapplication.Inicio.Fragmentos.Notificacoes;
import com.example.myapplication.Inicio.Fragmentos.Reserva;
import com.example.myapplication.Inicio.Fragmentos.Reservas;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class Inicio extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Reservas reservasFragment = new Reservas();
    Reserva reservaFragment = new Reserva();
    Notificacoes notificacoesFragment = new Notificacoes();
    Calendario calendarioFragment = new Calendario();

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

    }

}





