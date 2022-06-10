package com.example.myapplication.Inicio.Fragmentos.marcar_reserva;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Inicio.Adapters.listar_centros;
import com.example.myapplication.Inicio.Fragmentos.Reserva;
import com.example.myapplication.Inicio.Fragmentos.Reservas;
import com.example.myapplication.Inicio.models.CentroModel;
import com.example.myapplication.Inicio.models.ReservasModel;
import com.example.myapplication.Login.SaveDataContract;
import com.example.myapplication.Login.SaveDataDbHelper;
import com.example.myapplication.R;
import com.example.myapplication.Utils.VolleyAPI.CustomJsonRequest;
import com.example.myapplication.Utils.VolleyAPI.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EscolherCentro extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Cursor cursor;
    listar_centros adapterCentros;
    ArrayList<CentroModel> centros;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_escolher_centro, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_centros);
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
                "/centros/list",
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

                            centros = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length();i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                int centroID = object.getInt("IDCentro");
                                String nomeCentro = object.getString("Nome").trim();
                                String moradaCentro = object.getString("Morada").trim();

                                centros.add(new CentroModel(
                                        centroID,
                                        nomeCentro,
                                        moradaCentro
                                        )
                                );
                            }
                            adapterCentros= new listar_centros(getContext(),centros, EscolherCentro.this::onRvClick);
                            recyclerView.setAdapter(adapterCentros);

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

        //Fim de listar centros



        return view;
    }

    private void onRvClick(int position) {
        Reserva reservaFragment = new Reserva();
        final CentroModel idcentro = centros.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("IDCentro", idcentro.getidcentro()+"");
        reservaFragment.setArguments(bundle);

       // Toast.makeText(getContext(),""+idcentro.getidcentro() , Toast.LENGTH_LONG).show();
        getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,reservaFragment).commit();
    }
}