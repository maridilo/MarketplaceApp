package com.example.marketplaceapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketplaceapp.R;
import com.example.marketplaceapp.adapter.CartAdapter;
import com.example.marketplaceapp.model.CartItem;

import java.util.List;

// Actividad que gestiona la pantalla del carrito de la compra
public class CartActivity extends AppCompatActivity {

    private double totalAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Configurar la barra superior para tener boton atras
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarCart);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // Hacer que la flecha funcione (cerrar actividad al pulsar)
        toolbar.setNavigationOnClickListener(v -> finish());

        // Obtenemos las referencias a las vistas una sola vez
        RecyclerView rv = findViewById(R.id.rvCartItems);
        TextView tvTotal = findViewById(R.id.tvCartTotal);
        Button btnPay = findViewById(R.id.btnCheckout);
        View emptyView = findViewById(R.id.tvEmptyCart); // La vista del mensaje vacio

        // Configurar el RecyclerView con un LinearLayoutManager vertical
        rv.setLayoutManager(new LinearLayoutManager(this));

        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Pasamos las funciones lambda para manejar el cambio de cantidad y el borrado
        CartAdapter adapter = new CartAdapter(
                (item, change) -> viewModel.changeQuantity(item, change),
                item -> viewModel.deleteFromCart(item)
        );
        rv.setAdapter(adapter);

        // Observamos la lista del carrito desde el ViewModel
        viewModel.getCartItems().observe(this, items -> {
            adapter.submitList(items); // Actualizamos el adaptador
            calculateTotal(items, tvTotal); // Recalculamos el total

            // LOGICA DE VISIBILIDAD
            if (items == null || items.isEmpty()) {
                // Si esta vacio: Muestra mensaje, oculta lista, desactiva boton
                emptyView.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
                btnPay.setEnabled(false);
                btnPay.setAlpha(0.5f); // Efecto visual de deshabilitado
            } else {
                // Si hay productos: Oculta mensaje, muestra lista, activa boton
                emptyView.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                btnPay.setEnabled(true);
                btnPay.setAlpha(1.0f);
            }
        });

        // BOTON PAGAR
        btnPay.setOnClickListener(v -> {
            if (totalAmount > 0) {
                // Realizar el checkout en base de datos
                viewModel.checkout(totalAmount);
                // Mostrar ventana VERDE de exito
                InfoDialogFragment.newInstance("¡Pedido Realizado!", "Gracias por tu compra.", false)
                        .show(getSupportFragmentManager(), "dialog_checkout");
            } else {
                // Mostrar ventana ROJA de error
                InfoDialogFragment.newInstance("Carrito Vacío", "Añade productos antes de pagar.", true)
                        .show(getSupportFragmentManager(), "dialog_error");
            }
        });
    }

    // Metodo auxiliar para calcular el precio total del carrito
    private void calculateTotal(List<CartItem> items, TextView tv) {
        totalAmount = 0;
        for (CartItem item : items) {
            totalAmount += (item.getPrice() * item.getQuantity());
        }
        tv.setText(String.format("Total: %.2f €", totalAmount));
    }
}