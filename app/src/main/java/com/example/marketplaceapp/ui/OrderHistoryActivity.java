package com.example.marketplaceapp.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.marketplaceapp.R;
import com.example.marketplaceapp.adapter.OrderAdapter;

// Actividad que muestra el historial de pedidos realizados por el usuario
public class OrderHistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // Configurar la barra superior con boton de retroceso
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarHistory);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Cerrar actividad al pulsar la flecha
        toolbar.setNavigationOnClickListener(v -> finish());

        // Configurar el RecyclerView para la lista
        RecyclerView rv = findViewById(R.id.rvOrders);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar y asignar el adaptador
        OrderAdapter adapter = new OrderAdapter();
        rv.setAdapter(adapter);

        // Obtener el ViewModel y observar los pedidos
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getOrders().observe(this, orders -> {
            adapter.setOrders(orders);

            // Logica de visibilidad: si esta vacio muestra mensaje, si no muestra lista
            if (orders.isEmpty()) {
                findViewById(R.id.tvEmptyView).setVisibility(View.VISIBLE);
                findViewById(R.id.rvOrders).setVisibility(View.GONE);
            } else {
                findViewById(R.id.tvEmptyView).setVisibility(View.GONE);
                findViewById(R.id.rvOrders).setVisibility(View.VISIBLE);
            }
        });
    }
}