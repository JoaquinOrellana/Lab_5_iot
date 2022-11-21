package edu.pucp.lab_5_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import edu.pucp.lab_5_iot.DTO.EventoDTO;

public class agregarEvento extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void guardarEvento(View view){
        DatabaseReference ref = firebaseDatabase.getReference();
        EditText nombreActividad = findViewById(R.id.nombreActividad);
        EditText descripcion = findViewById(R.id.descripcion);
        EditText fechaInicio = findViewById(R.id.fechainicio);
        EditText fechaFin = findViewById(R.id.fechafin);
        EditText horaInicio = findViewById(R.id.horaInicio);
        EditText horaFin = findViewById(R.id.horaFin);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            DateTimeFormatter fechas = DateTimeFormatter.ofPattern("dd/M/uuu");
            DateTimeFormatter horas = DateTimeFormatter.ofPattern("HH:mm");
            LocalDate fechainicio1 = LocalDate.parse(fechaInicio.getText(),fechas);
            LocalDate fechafin1 = LocalDate.parse(fechaFin.getText(),fechas);
            LocalTime horainicio1 = LocalTime.parse(horaInicio.getText(),horas);
            LocalTime horafin1 = LocalTime.parse(horaFin.getText(),horas);

            LocalDateTime fechaYhoraInicio = LocalDateTime.of(fechainicio1,horainicio1);
            LocalDateTime fechaYhoraFin = LocalDateTime.of(fechafin1,horafin1);

            EventoDTO evento = new EventoDTO();
            evento.setTitulo(nombreActividad.getText().toString());
            evento.setFechaYHoraInicio(fechaYhoraInicio);
            evento.setFechaYHoraFin(fechaYhoraFin);
            evento.setDescripcion(descripcion.getText().toString());
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            ref.child("eventos").child(currentUser.getUid()).child(fechaYhoraInicio.toString()).setValue(evento);


        }



    }
}