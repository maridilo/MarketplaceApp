package com.example.marketplaceapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.marketplaceapp.MainActivity;
import com.example.marketplaceapp.R;
import com.google.firebase.auth.FirebaseAuth;

// Actividad para el registro de nuevos usuarios en Firebase
public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etEmail, etPass, etConfirm;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar la instancia de autenticacion
        mAuth = FirebaseAuth.getInstance();

        // Vincular los elementos de la interfaz
        etEmail = findViewById(R.id.etEmailReg);
        etPass = findViewById(R.id.etPassReg);
        etConfirm = findViewById(R.id.etPassConfirm);
        progressBar = findViewById(R.id.progressBarReg);
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView tvGoLogin = findViewById(R.id.tvGoLogin);

        // Configurar el boton de registro
        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPass.getText().toString().trim();
            String confirm = etConfirm.getText().toString().trim();

            // Validaciones de campos vacios
            if (email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Rellena todo", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar que las contraseñas coincidan
            if (!pass.equals(confirm)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar longitud minima de contraseña
            if (pass.length() < 6) {
                Toast.makeText(this, "Mínimo 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            registerUser(email, pass);
        });

        // Volver a la pantalla de login
        tvGoLogin.setOnClickListener(v -> finish());
    }

    // Metodo para crear usuario en Firebase con email y contraseña
    private void registerUser(String email, String password) {
        // Mostrar barra de carga
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // Ocultar barra de carga
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Cuenta creada", Toast.LENGTH_SHORT).show();
                        // Registro correcto: navegar a la tienda
                        Intent intent = new Intent(this, MainActivity.class);
                        // Limpiar historial de navegacion
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error: mostrar mensaje
                        Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}