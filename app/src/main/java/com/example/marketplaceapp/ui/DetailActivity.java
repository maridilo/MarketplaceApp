package com.example.marketplaceapp.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.marketplaceapp.R;
import com.example.marketplaceapp.model.Product;

// Actividad para mostrar el detalle de un producto
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Configurar la barra superior
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            // Habilitar la flecha de retroceso
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Ocultar el titulo de la app por estetica
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        // Listener para cerrar la actividad al pulsar la flecha
        toolbar.setNavigationOnClickListener(v -> finish());

        // Recuperar el producto enviado desde el Intent
        Product product = (Product) getIntent().getSerializableExtra("PRODUCT_EXTRA");
        if (product == null) { finish(); return; }

        // Inicializar el ViewModel para gestionar el carrito
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Referencias a los elementos visuales
        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvPrice = findViewById(R.id.tvDetailPrice);
        TextView tvDesc = findViewById(R.id.tvDetailDesc);
        ImageView ivImage = findViewById(R.id.ivDetailImage);
        Button btnAdd = findViewById(R.id.btnDetailAdd);

        // Asignar datos del producto a las vistas
        tvName.setText(product.getName());
        tvPrice.setText(String.format("%.2f €", product.getPrice()));
        tvDesc.setText(product.getDescription());

        // Cargar la imagen usando Glide
        Glide.with(this).load(product.getImageUrl()).into(ivImage);

        // Accion del boton añadir al carrito
        btnAdd.setOnClickListener(v -> {
            // Añadir producto mediante el ViewModel
            viewModel.addToCart(product);

            // Mostrar dialogo de confirmacion personalizado
            InfoDialogFragment.newInstance(
                    "¡Excelente!",
                    "Has añadido " + product.getName() + " a tu pedido.",
                    false
            ).show(getSupportFragmentManager(), "dialog_detail");
        });
    }
}