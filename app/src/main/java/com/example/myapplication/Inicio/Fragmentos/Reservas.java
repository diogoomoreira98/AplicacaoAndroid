package com.example.myapplication.Inicio.Fragmentos;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Inicio.Adapters.listar_reservas;
import com.example.myapplication.Inicio.Inicio;
import com.example.myapplication.Inicio.models.ReservasModel;
import com.example.myapplication.Login.SaveDataContract;
import com.example.myapplication.Login.SaveDataDbHelper;
import com.example.myapplication.R;
import com.example.myapplication.Utils.VolleyAPI.CustomJsonRequest;
import com.example.myapplication.Utils.VolleyAPI.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Reservas extends Fragment implements listar_reservas.onRvListener{

     RecyclerView recyclerView;
     LinearLayoutManager layoutManager;
     listar_reservas adapterReservas;
     Cursor cursor;
     ArrayList<ReservasModel> reservas;
     AlertDialog.Builder dialogBuilder;
     AlertDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reservas, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rvReservas);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Context context = getActivity();

        String URL = "http://176.79.161.72:5000/reservas/listbyuser";
        Map<String, String> request = new HashMap<String, String>();

        try {
            SaveDataDbHelper dbHelper = new SaveDataDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String[] projection = {SaveDataContract.SaveData.ID_USER, SaveDataContract.SaveData.TOKEN,};
            cursor = db.query(SaveDataContract.SaveData.TABLE_NAME, projection, null, null, null, null, null);
            while(cursor.moveToNext()) {
                request.put("IDUtilizador",cursor.getInt(cursor.getColumnIndexOrThrow(SaveDataContract.SaveData.ID_USER))+"");
                request.put("token",cursor.getString(cursor.getColumnIndexOrThrow(SaveDataContract.SaveData.TOKEN)));
            }
            cursor.close();
            db.close();
        }catch (Exception e){
            System.out.println(e);
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        CustomJsonRequest jsonObjectRequest = new CustomJsonRequest(
                context,
                Request.Method.POST,
                "/reservas/listbyuser",
                request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("data");

                            reservas = new ArrayList<>();

                            if(jsonArray.length()==0){
                                Toast.makeText(getContext(), "Não há reservas", Toast.LENGTH_SHORT).show();
                            }
                            for (int i = 0; i < jsonArray.length();i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                int reserva = object.getInt("IDReserva");
                                String sala = object.getString("NomeSala").trim();
                                String titulo = object.getString("Titulo").trim();
                                String data = object.getString("HoraInicio").split("T")[0].trim();
                                String horaInicio = object.getString("HoraInicio").split("T")[1].trim();
                                String horaFim = object.getString("HoraFim").split("T")[1].trim();
                                String centro = object.getString("NomeCentro").trim();

                                reservas.add(new ReservasModel(
                                                reserva,
                                                titulo,
                                                data,
                                                sala,
                                                horaInicio,
                                                horaFim,
                                                centro
                                        )
                                );
                            }
                            adapterReservas= new listar_reservas(getContext(),reservas,Reservas.this::onRvClick);
                            recyclerView.setAdapter(adapterReservas);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, VolleyErrorHelper.getMessage(error, getActivity()), Toast.LENGTH_LONG).show();
            }
        });



        requestQueue.add(jsonObjectRequest);

        return view;
    }

    @Override
    public void onRvClick(int position) {
        createNewDialog();
        final ReservasModel idreserva = reservas.get(position);
        Toast.makeText(getContext(),""+idreserva.getidreserva() , Toast.LENGTH_LONG).show();
    }
    public void createNewDialog(){
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View detalhePopup = getLayoutInflater().inflate(R.layout.detalhepopup,null);
        dialogBuilder.setView(detalhePopup);
        dialog = dialogBuilder.create();
        dialog.show();
    }
}