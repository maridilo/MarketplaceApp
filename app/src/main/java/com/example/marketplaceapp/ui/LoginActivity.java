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
import com.google.firebase.auth.FirebaseUser;

// Actividad para gestionar el inicio de sesion con Firebase
public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. Inicializar la instancia de autenticacion de Firebase
        mAuth = FirebaseAuth.getInstance();

        // 2. Comprobar si ya hay un usuario logueado (sesion persistente)
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Si ya existe sesion, vamos directo a la tienda
            goToMain();
        }

        // 3. Vincular los elementos de la interfaz
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        progressBar = findViewById(R.id.progressBar);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvGoRegister);

        // 4. Configurar el boton de inicio de sesion
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                loginUser(email, password);
            } else {
                Toast.makeText(LoginActivity.this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        // 5. Configurar el enlace para ir a la pantalla de registro
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    // Metodo para autenticar al usuario en Firebase con email y contraseña
    private void loginUser(String email, String password) {
        // Mostrar barra de carga mientras se conecta
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // Ocultar barra de carga al terminar
                    progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        // Login correcto: mostrar mensaje y navegar
                        Toast.makeText(LoginActivity.this, "¡Bienvenido!", Toast.LENGTH_SHORT).show();
                        goToMain();
                    } else {
                        // Error: mostrar mensaje con la causa
                        Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Navegar a la pantalla principal y limpiar el historial
    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        // Borrar historial para no volver al login con el boton Atras
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}