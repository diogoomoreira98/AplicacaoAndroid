package com.example.myapplication.Inicio.Fragmentos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Inicio.Adapters.listar_centros;
import com.example.myapplication.Inicio.Adapters.listar_salas;
import com.example.myapplication.Inicio.Fragmentos.marcar_reserva.EscolherCentro;
import com.example.myapplication.Inicio.Inicio;
import com.example.myapplication.Inicio.models.CentroModel;
import com.example.myapplication.Inicio.models.SalaModel;
import com.example.myapplication.R;
import com.example.myapplication.Utils.VolleyAPI.CustomJsonRequest;
import com.example.myapplication.Utils.VolleyAPI.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerificarDisp extends Fragment {

    String NParticipantes, HoraInicio, HoraFim, Titulo, IDCentro;
    List<SalaModel> listaSalas = new ArrayList<>();
    listar_salas adapterSalas;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    SalaModel idsala;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            NParticipantes = getArguments().getString("NParticipantes");
            HoraInicio = getArguments().getString("HoraInicio");
            HoraFim = getArguments().getString("HoraFim");
            Titulo = getArguments().getString("Titulo");
            IDCentro = getArguments().getString("IDCentro");


            try {
                JSONArray array = new JSONArray(getArguments().getString("response"));
                for(int i=0; i < array.length(); i++)
                {
                    JSONObject response = array.getJSONObject(i);
                    int IDSala = response.getInt("IDSala");
                    String Nome = response.getString("Nome");
                    String duracaolimpeza = response.getString("DuracaoLimpeza");
                    String nmaxutentes = response.getString("NMaxUtentes");
                    String alocacaomaxima = response.getString("AlocacaoMaxima");

                    listaSalas.add(new SalaModel(
                                IDSala,
                                Nome,
                                duracaolimpeza,
                                nmaxutentes,
                                alocacaomaxima
                            )
                    );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verificar_disp, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_lista_salas);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        adapterSalas= new listar_salas(getContext(), (List<SalaModel>) listaSalas, VerificarDisp.this::onRvClick);
        recyclerView.setAdapter(adapterSalas);

        return view;
    }
    public void confirmar_reserva(){
        /*
        var dados = {
                IDSala: sala,
                Titulo: titulo,
                NParticipantes: nParticipantes,
                HoraInicio: new Date(data.toDateString() + " " + horaInicio + ":00"),
                HoraFim: new Date(data.toDateString() + " " + horaFim + ":00"),
            }
         */
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
                "/reservas/create",
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
    }

    private void onRvClick(int position) {
        idsala = listaSalas.get(position);
       // Toast.makeText(getContext(),""+idsala.getIdsala() , Toast.LENGTH_LONG).show();
        createNewDialog(idsala);
    }
    public void createNewDialog(SalaModel idsala){
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View viewdialog = getLayoutInflater().inflate(R.layout.confirmar_popup,null);

        Button btn_confirmar = (Button) viewdialog.findViewById(R.id.btn_confirmar);
        Button btn_cancelar = (Button) viewdialog.findViewById(R.id.btn_cancelar);

        dialogBuilder.setView(viewdialog);
        dialog = dialogBuilder.create();
        dialog.show();
        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Botão para confirmar a reserva
                confirmar_reserva();
            }
        });
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Botão para voltar
                dialog.cancel();
            }
        });
    }
}