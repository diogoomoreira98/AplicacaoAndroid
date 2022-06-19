package com.example.myapplication.Inicio.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Inicio.models.SalaModel;
import com.example.myapplication.R;

import java.util.List;

public class listar_salas extends RecyclerView.Adapter<listar_salas.MyViewHolder> {

    private List<SalaModel> model;
    private Context context;
    private listar_salas.onRvListener monRvListener;

    public listar_salas(Context context, List<SalaModel> itemList, listar_salas.onRvListener onRvListener){
        this.context = context;
        this.model = itemList;
        this.monRvListener = onRvListener;
    }

    @NonNull
    @Override
    public listar_salas.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.listar_salas,parent,false);

        return new listar_salas.MyViewHolder(itemView,monRvListener);
    }

    @Override
    public void onBindViewHolder(@NonNull listar_salas.MyViewHolder holder, int position) {

        //Setar texto nos campos
        holder.nomeSala.setText(model.get(position).getNome());

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nomeSala;
        RecyclerView rvSalas;
        listar_salas.onRvListener onRvListener;

        public MyViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView, listar_salas.onRvListener onRvListener)
        {
            super(itemView);
            nomeSala = itemView.findViewById(R.id.txt_nomeSala);
            rvSalas = itemView.findViewById(R.id.rv_lista_salas);

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