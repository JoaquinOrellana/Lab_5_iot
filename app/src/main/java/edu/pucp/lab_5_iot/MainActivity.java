package edu.pucp.lab_5_iot;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Log.d("msg-fb", currentUser.getUid());
            Log.d("msg-fb", currentUser.getEmail());
            currentUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (currentUser.isEmailVerified()) {
                        startActivity(new Intent(MainActivity.this, ListarActividades.class));
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "debes validar tu correo", Toast.LENGTH_SHORT).show();
                        currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("msg-fb", "correo enviado");
                            }
                        });
                    }
                }
            });



        }


    }

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new FirebaseAuthUIActivityResultContract(), result -> {
        onSignInOnResult(result);
    });

    private void onSignInOnResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse idpResponse = result.getIdpResponse();
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
                        startActivity(new Intent(MainActivity.this, ListarActividades.class));
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "debes validar tu correo", Toast.LENGTH_SHORT).show();
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


}