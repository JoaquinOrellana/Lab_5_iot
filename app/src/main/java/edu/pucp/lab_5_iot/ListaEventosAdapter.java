package edu.pucp.lab_5_iot;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.pucp.lab_5_iot.DTO.EventoDTO;
import edu.pucp.lab_5_iot.DTO.ListaEventosDTO;

public class ListaEventosAdapter extends RecyclerView.Adapter<ListaEventosAdapter.ActividadViewHolder> {

    public ListaEventosAdapter(Context context, ArrayList<EventoDTO> list) {
        this.context = context;
        this.list = list;
    }



    public class ActividadViewHolder extends RecyclerView.ViewHolder{
        TextView titulo,descripcion,fechaYHoraInicio,fechaYHoraFin,imgID;
        Button btneditar, btnEliminar;
        public ActividadViewHolder(@NonNull View itemView) {
            super(itemView);
            btneditar = itemView.findViewById(R.id.btnEditarActividad);
            btnEliminar=itemView.findViewById(R.id.btnEliminarActividad);
            titulo=itemView.findViewById(R.id.textviewTituloActividad);
            descripcion=itemView.findViewById(R.id.textViewDescripcion);
            fechaYHoraInicio=itemView.findViewById(R.id.textViewFechaInicio);
            //TODO FALTA FECHA FIN EN DTO
            fechaYHoraFin=itemView.findViewById(R.id.textViewFechaFin);
            //TODO IMPLEMENTAR IMAGE ID


        }
    }

    Context context;
    ArrayList<EventoDTO> list;


    @NonNull
    @Override
    public ActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_actividad_listar,parent,false);
        return new ActividadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadViewHolder holder, int position) {
        EventoDTO eventoDTO = list.get(position);
        holder.titulo.setText(eventoDTO.getTitulo());
        holder.descripcion.setText(eventoDTO.getDescripcion());
        holder.fechaYHoraInicio.setText(eventoDTO.getFechaYHora().toString());
        //holder.fechaYHoraFin.setText(eventoDTO.getFechaYHoraFin().toString());
        //TODO IMPLEMENTAR FOTO

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
