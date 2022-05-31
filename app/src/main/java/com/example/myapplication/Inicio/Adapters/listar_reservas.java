package com.example.myapplication.Inicio.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Inicio.models.ReservasModel;
import com.example.myapplication.R;

import java.util.List;

public class listar_reservas extends RecyclerView.Adapter<listar_reservas.MyViewHolder> {


    private List<ReservasModel> model;
    private Context context;
    private int idreserva;
    private onRvListener monRvListener;


    public listar_reservas(Context context, List<ReservasModel> itemList, onRvListener onRvListener){
        this.context = context;
        this.model = itemList;
        this.monRvListener = onRvListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.listar_reservas,parent,false);


        return new MyViewHolder(itemView,monRvListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.titulo.setText(model.get(position).getTitulo());
        holder.data.setText(model.get(position).getData());
        holder.sala.setText(model.get(position).getSala());
        holder.horaInicio.setText(model.get(position).getHoraInicio());
        holder.horaFim.setText(model.get(position).getHoraFim());
        holder.centro.setText(model.get(position).getCentro());
        idreserva = model.get(position).getidreserva();



    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView titulo,data,sala,horaInicio,horaFim,centro;
        RecyclerView rvReservas;
        onRvListener onRvListener;

        public MyViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView, onRvListener onRvListener)
        {
            super(itemView);
            titulo = itemView.findViewById(R.id.txt_titulo);
            data = itemView.findViewById(R.id.txt_data);
            sala = itemView.findViewById(R.id.txt_sala);
            horaInicio = itemView.findViewById(R.id.txt_inicio);
            horaFim = itemView.findViewById(R.id.txt_fim);
            centro = itemView.findViewById(R.id.txt_centro);
            rvReservas = itemView.findViewById(R.id.rvReservas);

            this.onRvListener = onRvListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onRvListener.onRvClick(getAdapterPosition());
        }
    }

    public interface onRvListener{
        void onRvClick(int position);

    }
}
