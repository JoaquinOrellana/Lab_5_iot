package edu.pucp.lab_5_iot;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListaActividadesAdapter extends RecyclerView.Adapter<ListaActividadesAdapter.ActividadViewHolder> {

    public class ActividadViewHolder extends RecyclerView.ViewHolder{
        //aqui hay un solo empleado
        Actividad actividad;
        public ActividadViewHolder(@NonNull View itemView) {

            super(itemView);
            Button button = itemView.findViewById(R.id.btnEditarActividad);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("msg", "id: "+String.valueOf(actividad.getId()));
                }
            });
        }
    }







    @NonNull
    @Override
    public ActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
