package edu.pucp.lab_5_iot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.pucp.lab_5_iot.DTO.EventoDTO;

public class ListarEventos extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<EventoDTO> listaEventos;
    DatabaseReference databaseReference;
    ListaEventosAdapter adapter;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_actividades);

        firebaseDatabase = FirebaseDatabase.getInstance();

        recyclerView=findViewById(R.id.recyclerViewEventos);
        //TODO PONER NOMBRE DE REFERENCIA Y CHILD
        databaseReference= firebaseDatabase.getReference("").child("");
        listaEventos=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(ListarEventos.this));
        adapter = new ListaEventosAdapter(this,listaEventos);
        recyclerView.setAdapter(adapter);




    }
}