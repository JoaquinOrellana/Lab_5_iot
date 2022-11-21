package edu.pucp.lab_5_iot;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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

        //TODO PONER NOMBRE DE REFERENCIA Y CHILD
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference("eventos").child("");

        listaEventos=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerViewEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListarEventos.this));
        adapter = new ListaEventosAdapter(this,listaEventos);
        recyclerView.setAdapter(adapter);

        Button btnLoginb=findViewById(R.id.btnLogin);
        Button button = findViewById(R.id.btnlogout);
        button.setVisibility(View.GONE);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            Log.d("msg-fb",currentUser.getUid());
            Log.d("msg-fb",currentUser.getEmail());

            currentUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(currentUser.isEmailVerified()){

                        btnLoginb.setVisibility(View.GONE);
                        button.setVisibility(View.VISIBLE);
                        TextView textview = findViewById(R.id.textViewUsuario);
                        String str="Usuario: "+currentUser.getDisplayName();
                        textview.setText(str);

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AuthUI.getInstance().signOut(ListarEventos.this)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                button.setVisibility(View.GONE);
                                                btnLoginb.setVisibility(View.VISIBLE);
                                                textview.setVisibility(View.GONE);
                                            }
                                        });
                            }
                        });


                    }else{
                        Toast.makeText(ListarEventos.this,"debes validar tu correo",Toast.LENGTH_SHORT).show();
                        currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("msg-fb","correo enviado");
                            }
                        });
                    }
                }
            });


        }




        btnLoginb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent fbIntent = AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build()
                        ))
                        .setTosAndPrivacyPolicyUrls(
                                "https://example.com/terms.html",
                                "https://example.com/privacy.html")
                        .setIsSmartLockEnabled(false)
                        .build();
                activityResultLauncher.launch(fbIntent);
            }
        });




    }

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(), result -> {
                onSignInOnResult(result);
            });

    private void onSignInOnResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse idpResponse = result.getIdpResponse();
        Button btnLoginb=findViewById(R.id.btnLogin);
        Button button = findViewById(R.id.btnlogout);
        button.setVisibility(View.GONE);
        if (result.getResultCode() == RESULT_OK) {
            Log.d("msg-fb", idpResponse.getEmail());
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            Log.d("msg-fb", currentUser.getUid());

            DatabaseReference ref = firebaseDatabase.getReference()
                    .child("usuarios")
                    .child(currentUser.getUid());

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("nombre", currentUser.getDisplayName());
                        map.put("provider", currentUser.getProviderId());
                        map.put("dominio", currentUser.getEmail().split("@")[1]);

                        ref.setValue(map).addOnCompleteListener(task -> {
                            System.out.println("usuario creado exitosamente");
                        });
                    }
                    if (currentUser.isEmailVerified()) {
                        btnLoginb.setVisibility(View.GONE);

                        button.setVisibility(View.VISIBLE);
                        TextView textview = findViewById(R.id.textViewUsuario);
                        String str="Usuario:"+currentUser.getDisplayName();
                        textview.setText(str);

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AuthUI.getInstance().signOut(ListarEventos.this)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                button.setVisibility(View.GONE);
                                                btnLoginb.setVisibility(View.VISIBLE);
                                                textview.setVisibility(View.GONE);
                                            }
                                        });
                            }
                        });


                    } else {
                        Toast.makeText(ListarEventos.this, "debes validar tu correo", Toast.LENGTH_SHORT).show();
                        currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("msg-fb", "correo enviado");
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            Log.d("msg-fb", "error al loguearse");
        }
    }

    public void anadirEvento (View view){
        startActivity(new Intent(ListarEventos.this, agregarEvento.class));
    }

}