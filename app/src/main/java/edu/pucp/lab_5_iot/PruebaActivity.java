package edu.pucp.lab_5_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;

import edu.pucp.lab_5_iot.DTO.EventoDTO;

public class PruebaActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getUid() != null && Build.VERSION.SDK_INT >= 26){
            DatabaseReference ref = firebaseDatabase.getReference(firebaseAuth.getUid());

            // Guardar tabla
            EventoDTO evento = new EventoDTO();
            evento.setTitulo("xd");
            evento.setDescripcion("xd");
            evento.setFechaYHora(LocalDateTime.now());
            evento.setImgID("xd");

            ref.child(evento.getFechaYHora().toString()).setValue(evento)
                    .addOnSuccessListener(aVOid -> Log.d("msg", "Información Guardada Exitosamente"))
                    .addOnFailureListener(e -> Log.d("msg", e.getMessage()));

        }

    }
}