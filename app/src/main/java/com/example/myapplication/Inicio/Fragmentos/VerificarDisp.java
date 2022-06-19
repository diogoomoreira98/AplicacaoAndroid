package com.example.myapplication.Inicio.Fragmentos;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.Inicio.Adapters.listar_centros;
import com.example.myapplication.Inicio.Adapters.listar_salas;
import com.example.myapplication.Inicio.Fragmentos.marcar_reserva.EscolherCentro;
import com.example.myapplication.Inicio.models.CentroModel;
import com.example.myapplication.Inicio.models.SalaModel;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VerificarDisp extends Fragment {

    String NParticipantes, HoraInicio, HoraFim, Titulo, IDCentro;
    List<SalaModel> listaSalas = new ArrayList<>();
    listar_salas adapterSalas;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;

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

    private void onRvClick(int position) {
        createNewDialog();
    }
    public void createNewDialog(){
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View confirmar = getLayoutInflater().inflate(R.layout.confirmar_popup,null);

        Button btn_confirmar = (Button) getView().findViewById(R.id.btn_confirmar);
        Button btn_cancelar = (Button) getView().findViewById(R.id.btn_cancelar);

        dialogBuilder.setView(confirmar);
        dialog = dialogBuilder.create();
        dialog.show();

        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Botão para confirmar a reserva
            }
        });
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Botão para cancelar a reserva
            }
        });
    }
}