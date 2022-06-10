package com.example.myapplication.Inicio.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Inicio.models.CentroModel;
import com.example.myapplication.R;
import java.util.List;

public class listar_centros extends RecyclerView.Adapter<listar_centros.MyViewHolder> {

    private List<CentroModel> model;
    private Context context;
    private listar_centros.onRvListener monRvListener;

    public listar_centros(Context context, List<CentroModel> itemList, listar_centros.onRvListener onRvListener){
        this.context = context;
        this.model = itemList;
        this.monRvListener = onRvListener;
    }

    @NonNull
    @Override
    public listar_centros.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.listar_centros,parent,false);

        return new listar_centros.MyViewHolder(itemView,monRvListener);
    }

    @Override
    public void onBindViewHolder(@NonNull listar_centros.MyViewHolder holder, int position) {

        //Setar texto nos campos
        holder.nomeCentro.setText(model.get(position).getnome());
        holder.moradaCentro.setText(model.get(position).getmorada());

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nomeCentro,moradaCentro;
        RecyclerView rvCentros;
        listar_centros.onRvListener onRvListener;

        public MyViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView, listar_centros.onRvListener onRvListener)
        {
            super(itemView);
            nomeCentro = itemView.findViewById(R.id.txt_nomeCentro);
            moradaCentro = itemView.findViewById(R.id.txt_moradaCentro);
            rvCentros = itemView.findViewById(R.id.rv_centros);

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
