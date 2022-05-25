package com.example.myapplication.Inicio.Fragmentos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.Inicio.Inicio;
import com.example.myapplication.Login.Login;
import com.example.myapplication.Login.SaveDataContract;
import com.example.myapplication.Login.SaveDataDbHelper;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;


public class Notificacoes extends Fragment {


    Button sair;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notificacoes, container, false);

        sair = view.findViewById(R.id.btn_logout);

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveDataDbHelper dbHelper = new SaveDataDbHelper(getContext());
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
                    getActivity().finish();
                    Toast.makeText(getContext(), "Logout efetuado com sucesso", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}