package edu.pucp.lab_5_iot;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
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
import java.util.Calendar;
import java.util.Locale;

import edu.pucp.lab_5_iot.DTO.EventoDTO;

public class agregarEvento extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage storage;
    StorageReference StorRef;

    EditText nombreActividad, descripcion;
    TextView textDate,fechafinText,horainicioText,horafinText;


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




    @SuppressLint("MissingInflatedId")
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


        nombreActividad = findViewById(R.id.nombreActividad);
        descripcion = findViewById(R.id.descripcion);
        //  EditText fechaInicio = findViewById(R.id.fechainicio);
        //   EditText fechaFin = findViewById(R.id.fechafin);

        textDate = findViewById(R.id.fechacalendario);
        fechafinText = findViewById(R.id.finText);

        Button selecDate = findViewById(R.id.btn);


        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Elige una fecha").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
        selecDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                datePicker.show(getSupportFragmentManager(), "Material_Date_Picker");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        textDate.setText(datePicker.getHeaderText());
                    }
                });
            }
        });


        Button fechafinBtn = findViewById(R.id.btn_fechafin);

        MaterialDatePicker datePicker2 = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Elige una fecha").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
        fechafinBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                datePicker2.show(getSupportFragmentManager(), "Material_Date_Picker");
                datePicker2.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        fechafinText.setText(datePicker2.getHeaderText());
                    }
                });
            }
        });


        horainicioText = findViewById(R.id.horainicioText);
        Button btn_horaInicio = findViewById(R.id.btn_horaInicio);
        Calendar calendar = Calendar.getInstance();
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))
                .build();


        btn_horaInicio.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                materialTimePicker.show(getSupportFragmentManager(),"Time");
                materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int Hour2 = materialTimePicker.getHour();
                        int minute2 = materialTimePicker.getMinute();
                        horainicioText.setText(Hour2+":"+minute2);
                    }
                });
            }
        });

        horafinText = findViewById(R.id.horafinText);
        Button btn_horafin = findViewById(R.id.btn_horafin);

        MaterialTimePicker materialTimePicker2 = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))
                .build();

        btn_horafin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                materialTimePicker2.show(getSupportFragmentManager(),"Time");
                materialTimePicker2.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int Hour = materialTimePicker2.getHour();
                        int minute = materialTimePicker2.getMinute();
                        horafinText.setText(Hour+":"+minute);
                    }
                });
            }
        });

    }

    public void guardarEvento(View view){
        // 1. se obtiene la referencia a la db
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = firebaseDatabase.getReference();
        DatabaseReference refeventos = ref.child("eventos");
        // 2. se obtienen los datos

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
          /*  EditText nombreActividad = findViewById(R.id.nombreActividad);
            EditText descripcion = findViewById(R.id.descripcion);
            //  EditText fechaInicio = findViewById(R.id.fechainicio);
            //   EditText fechaFin = findViewById(R.id.fechafin);
            EditText horaInicio = findViewById(R.id.horaInicio);
            EditText horaFin = findViewById(R.id.horaFin);

           */

            DateTimeFormatter fechas = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy");
            DateTimeFormatter horas = DateTimeFormatter.ofPattern("HH:mm");
            LocalDate fechainicio1 = LocalDate.parse(textDate.getText(),fechas);
            LocalTime horainicio1 = LocalTime.parse(horainicioText.getText(),horas);
            Log.d("msg", horainicio1.toString());
            LocalDateTime fechaYhoraInicio = LocalDateTime.of(fechainicio1,horainicio1);

            LocalDate fechafin1 = LocalDate.parse(fechafinText.getText(),fechas);
            LocalTime horafin1 = LocalTime.parse(horafinText.getText(),horas);
            LocalDateTime fechaYhoraFin = LocalDateTime.of(fechafin1,horafin1);

         //   DateTimeFormatter fechas = DateTimeFormatter.ofPattern("dd/M/uuu");
           // DateTimeFormatter horas = DateTimeFormatter.ofPattern("HH:mm");
          //  LocalDate fechainicio1 = LocalDate.parse(fechaInicio.getText(),fechas);
           // LocalDate fechafin1 = LocalDate.parse(fechaFin.getText(),fechas);
          //  LocalTime horainicio1 = LocalTime.parse(horaInicio.getText(),horas);
          //  LocalTime horafin1 = LocalTime.parse(horaFin.getText(),horas);

           // LocalDateTime fechaYhoraInicio = LocalDateTime.of(fechainicio1,horainicio1);
           // LocalDateTime fechaYhoraFin = LocalDateTime.of(fechafin1,horafin1);

            EventoDTO evento = new EventoDTO();
            evento.setTitulo(nombreActividad.getText().toString());
            evento.setFechaYHoraInicio(fechaYhoraInicio);
            evento.setFechaYHoraFin(fechaYhoraFin);
            evento.setDescripcion(descripcion.getText().toString());
            evento.setImgID("photo" + numero + ".jpg");
            // 3. se guardan los datos
            refeventos.child(currentUser.getUid()).push().setValue(evento).addOnSuccessListener(unused -> {
                Toast.makeText(agregarEvento.this, "Guardado correctamente", Toast.LENGTH_SHORT).show();
            });

            // 4. se resetean los valores
            nombreActividad.setText("");
            descripcion.setText("");
            textDate.setText("");
            fechafinText.setText("");
            horainicioText.setText("");
            horafinText.setText("");

        }



    }


    public void subirImagen(View view) {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/jpeg");
        launcherPhotos.launch(intent);

    }

}