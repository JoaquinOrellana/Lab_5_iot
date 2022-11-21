package edu.pucp.lab_5_iot;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import edu.pucp.lab_5_iot.DTO.EventoDTO;

public class agregarEvento extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage storage;
    StorageReference StorRef;
    //static int cont = 1;
    int numero = (int)(Math.random()*11351+1);
    ActivityResultLauncher<Intent> launcherPhotos = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Uri uri = result.getData().getData();
                    StorageReference child = StorRef.child("photo" + numero + ".jpg");

                    child.putFile(uri)
                            .addOnSuccessListener(taskSnapshot -> Log.d("msg-test", "Subido correctamente"))
                            .addOnFailureListener(e -> Log.d("msg-test", "error"))
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d("msg-test", "ruta archivo: " + task.getResult());
                                }
                            });
                } else {
                    Toast.makeText(agregarEvento.this, "Debe seleccionar un archivo", Toast.LENGTH_SHORT).show();
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        firebaseDatabase = FirebaseDatabase.getInstance(); // se obtiene la conexion a la db
        storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorRef = storageReference.child("lab5photos");

        StorageReference photoRef = StorRef.child("photo" + numero + ".jpg");
        ImageView imageView = findViewById(R.id.imageView2);

        Glide.with(this)
                .load(photoRef)
                .into(imageView);
    }

    public void guardarEvento(View view){
        // 1. se obtiene la referencia a la db
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = firebaseDatabase.getReference();
        DatabaseReference refeventos = ref.child("eventos");
        // 2. se obtienen los datos
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
            evento.setImgID("photo" + numero + ".jpg");
            // 3. se guardan los datos
            refeventos.push().setValue(evento).addOnSuccessListener(unused -> {
                Toast.makeText(agregarEvento.this, "Guardado correctamente", Toast.LENGTH_SHORT).show();
            });

            // 4. se resetean los valores
            nombreActividad.setText("");
            descripcion.setText("");
            fechaInicio.setText("");
            fechaFin.setText("");
            horaInicio.setText("");
            horaFin.setText("");

        }



    }


    public void subirImagen(View view) {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/jpeg");
        launcherPhotos.launch(intent);

    }

}