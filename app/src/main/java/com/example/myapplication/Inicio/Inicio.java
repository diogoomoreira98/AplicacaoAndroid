package com.example.myapplication.Inicio;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.Inicio.Fragmentos.marcar_reserva.EscolherCentro;
import com.example.myapplication.Inicio.Fragmentos.Reservas;
import com.example.myapplication.Inicio.Fragmentos.v_mensal;
import com.example.myapplication.Login.SaveDataContract;
import com.example.myapplication.Login.SaveDataDbHelper;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import java.util.ArrayList;
import java.util.List;


public class Inicio extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Reservas reservasFragment = new Reservas();
    v_mensal calendarioFragment = new v_mensal();
    EscolherCentro escolherCentroFragment = new EscolherCentro();

    private  androidx.appcompat.widget.Toolbar mTopToolbar;

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
        mTopToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, reservasFragment).commit();

       bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId())
               {
                   case R.id.reservas:
                       getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,reservasFragment).commit();
                       getSupportFragmentManager().popBackStack();
                       return true;
                   case R.id.escolherCentro:
                       getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,escolherCentroFragment).commit();
                       getSupportFragmentManager().popBackStack();
                       return true;
                   case R.id.calendario:
                       getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,calendarioFragment).commit();
                       getSupportFragmentManager().popBackStack();
                       return true;

               }
               return false;
           }
       });

       /*Botão de scan
        btnScan = (Button) findViewById(R.id.btnqrcode);
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
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btnqrcode) {
            if(checkpermissions()) {
                startActivity(new Intent(getApplicationContext(), ScannerActivity.class));
            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(), "faltam permissões", Toast.LENGTH_SHORT);
                toast.show();
            }
            return true;
        }
        if (id == R.id.btnlogout) {
            SaveDataDbHelper dbHelper = new SaveDataDbHelper(Inicio.this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] projection = {
                    SaveDataContract.SaveData.ID_USER,
                    SaveDataContract.SaveData.TOKEN,
            };

            Cursor cursor = db.query(
                    SaveDataContract.SaveData.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            List dados = new ArrayList<>();
            while(cursor.moveToNext()) {
                long itemId = cursor.getInt(cursor.getColumnIndexOrThrow(SaveDataContract.SaveData.ID_USER));
                String token = cursor.getString(cursor.getColumnIndexOrThrow(SaveDataContract.SaveData.TOKEN));
                dados.add(itemId);
                dados.add(token);
            }
            cursor.close();
            if(!dados.isEmpty()){
                int deletedRows = db.delete(SaveDataContract.SaveData.TABLE_NAME, null, null);
                //startActivity(new Intent(getContext(), Login.class));
                this.finish();
                Toast.makeText(Inicio.this, "Logout efetuado com sucesso", Toast.LENGTH_LONG).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
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





