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
import android.widget.Button;
import android.widget.TextView;
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
     TextView editDate,editTime,editTime2,editTitulo,editParticipantes;

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
                            if (jsonArray.length()<1){
                                //Toast.makeText(getContext(), "Não há reservas", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            reservas = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length();i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                int reserva = object.getInt("IDReserva");
                                String sala = object.getString("NomeSala").trim();
                                String titulo = object.getString("Titulo").trim();
                                String data = object.getString("HoraInicio").split("T")[0].trim();
                                String horaInicio = object.getString("HoraInicio").split("T")[1].trim();
                                String horaFim = object.getString("HoraFim").split("T")[1].trim();
                                String centro = object.getString("NomeCentro").trim();
                                int participantes = object.getInt("NParticipantes");

                                reservas.add(new ReservasModel(
                                                reserva,
                                                titulo,
                                                data,
                                                sala,
                                                horaInicio,
                                                horaFim,
                                                centro,
                                                participantes
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
        createNewDialog(position);
        final ReservasModel idreserva = reservas.get(position);
        Toast.makeText(getContext(),""+idreserva.getidreserva() , Toast.LENGTH_LONG).show();
    }

    public void editar_reserva(){
       /*
        Map<String, String> request = new HashMap<String, String>();

        request.put("IDSala", idsala.getIdsala()+"");
        request.put("Titulo", Titulo);
        request.put("NParticipantes", NParticipantes);
        request.put("HoraInicio", HoraInicio);
        request.put("HoraFim", HoraFim);

        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        CustomJsonRequest jsonObjectRequest = new CustomJsonRequest(
                context,
                Request.Method.POST,
                "/reservas/update",
                request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(getContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();

                            Reservas InicioFragment = new Reservas();
                            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,InicioFragment).commit();
                            dialog.cancel();
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
        */
    }

    public void inativar_reserva() {

    }



    public void createNewDialog(int position){
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View detalhePopup = getLayoutInflater().inflate(R.layout.detalhepopup,null);
        dialogBuilder.setView(detalhePopup);
        dialog = dialogBuilder.create();
        dialog.show();

        editDate = (TextView) detalhePopup.findViewById(R.id.editDate);
        editTime = (TextView) detalhePopup.findViewById(R.id.editTime);
        editTime2 = (TextView) detalhePopup.findViewById(R.id.editTime2);
        editTitulo = (TextView) detalhePopup.findViewById(R.id.editTitulo);
        editParticipantes= (TextView) detalhePopup.findViewById(R.id.editParticipantes);
        Button btn_editar = (Button) detalhePopup.findViewById(R.id.btn_edit);
        Button btn_inativar = (Button) detalhePopup.findViewById(R.id.btn_ina);

        final ReservasModel data = reservas.get(position);

        editDate.setText(""+data.getData());
        editTime.setText(data.getHoraInicio().toString().split(":")[0] + ":" + data.getHoraInicio().toString().split(":")[1]);
        editTime2.setText(data.getHoraFim().toString().split(":")[0]+ ":" + data.getHoraFim().toString().split(":")[1]);
        editTitulo.setText(""+data.getTitulo());
        editParticipantes.setText(""+data.getParticipantes());

        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Botão para editar a reserva
                editar_reserva();
            }
        });
        btn_inativar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Botão para inativar
                inativar_reserva();

            }
        });

    }
}