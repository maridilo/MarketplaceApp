package com.example.marketplaceapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.marketplaceapp.R;
import com.google.firebase.auth.FirebaseAuth;

// Actividad para mostrar el perfil del usuario y cerrar sesion
public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Configuracion de la barra superior
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Listener para cerrar la actividad al pulsar la flecha
        toolbar.setNavigationOnClickListener(v -> finish());

        TextView tvEmail = findViewById(R.id.tvProfileEmail);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Obtener el email del usuario actual de Firebase y mostrarlo
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        tvEmail.setText(email);

        // Boton de cierre de sesion con confirmacion
        btnLogout.setOnClickListener(v -> {
            // Mostrar dialogo de alerta nativo de Android
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Cerrar Sesión")
                    .setMessage("¿Estás seguro de que quieres salir?")
                    .setPositiveButton("Sí, salir", (dialog, which) -> {
                        // Accion al confirmar la salida: desconectar de Firebase
                        FirebaseAuth.getInstance().signOut();

                        // Volver a la pantalla de Login y limpiar el historial de navegacion
                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Cancelar", null) // Accion al cancelar (no hace nada)
                    .show();
        });
    }
}