package com.example.myapplication.Inicio.Adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Inicio.models.ReservasModel;
import com.example.myapplication.R;

import java.util.List;

public class listar_por_dia extends RecyclerView.Adapter<listar_por_dia.MyViewHolder> implements ListAdapter {


    private List<ReservasModel> model;
    private Context context;
    private int idreserva;
    private listar_por_dia.onRvListener monRvListener;


    public listar_por_dia(Context context, List<ReservasModel> itemList, listar_por_dia.onRvListener onRvListener){
        this.context = context;
        this.model = itemList;
        this.monRvListener = onRvListener;
    }

    @NonNull
    @Override
    public listar_por_dia.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.event_cell,parent,false);


        return new listar_por_dia.MyViewHolder(itemView,monRvListener);
    }

    @Override
    public void onBindViewHolder(@NonNull listar_por_dia.MyViewHolder holder, int position) {
        holder.titulo.setText(model.get(position).getTitulo());
        holder.data.setText(model.get(position).getData());
        holder.sala.setText(model.get(position).getSala());
        holder.horaInicio.setText(model.get(position).getHoraInicio());
        holder.horaFim.setText(model.get(position).getHoraFim());
        holder.centro.setText(model.get(position).getCentro());
        idreserva = model.get(position).getidreserva();
        holder.info.setText(model.get(position).getStatus());
        if(model.get(position).getStatus() == "A decorrer"){
            holder.info.setTextColor(Color.parseColor("#FF4F33"));
        }else {
            holder.info.setTextColor(Color.parseColor("#36FF33"));
        }

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView titulo,data,sala,horaInicio,horaFim,centro,info;
        RecyclerView rvReservas;
        listar_por_dia.onRvListener onRvListener;

        public MyViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView, listar_por_dia.onRvListener onRvListener)
        {
            super(itemView);
            titulo = itemView.findViewById(R.id.txt_titulo);
            data = itemView.findViewById(R.id.txt_data);
            sala = itemView.findViewById(R.id.txt_sala);
            horaInicio = itemView.findViewById(R.id.txt_inicio);
            horaFim = itemView.findViewById(R.id.txt_fim);
            centro = itemView.findViewById(R.id.txt_centro);
            info = itemView.findViewById(R.id.txt_info);
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

