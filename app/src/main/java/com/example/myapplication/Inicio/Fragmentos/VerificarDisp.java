package com.example.myapplication.Inicio.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;

import org.json.JSONObject;

public class VerificarDisp extends Fragment {

    String NParticipantes, HoraInicio, HoraFim, Titulo, IDCentro;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            NParticipantes = getArguments().getString("NParticipantes");
            HoraInicio = getArguments().getString("HoraInicio");
            HoraFim = getArguments().getString("HoraFim");
            Titulo = getArguments().getString("Titulo");
            IDCentro = getArguments().getString("IDCentro");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verificar_disp, container, false);
    }
}