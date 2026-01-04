package com.example.marketplaceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketplaceapp.adapter.ProductAdapter;
import com.example.marketplaceapp.ui.CartActivity;
import com.example.marketplaceapp.ui.DetailActivity;
import com.example.marketplaceapp.ui.InfoDialogFragment;
import com.example.marketplaceapp.ui.LoginActivity;
import com.example.marketplaceapp.ui.MainViewModel;
import com.example.marketplaceapp.ui.OrderHistoryActivity;
import com.example.marketplaceapp.ui.ProfileActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

// Actividad principal que muestra el catalogo de productos
public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar la barra superior (Toolbar)
        setSupportActionBar(findViewById(R.id.toolbar));

        // Inicializar el ViewModel para manejar los datos
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Configurar la lista de productos en formato rejilla (2 columnas)
        RecyclerView rvProducts = findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));

        // Configurar el adaptador con las acciones (Lambdas)
        adapter = new ProductAdapter(
                // Al pulsar en la foto: Navegar al detalle del producto
                product -> {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("PRODUCT_EXTRA", product);
                    startActivity(intent);
                },
                // Al pulsar el boton (+): Añadir al carrito y mostrar aviso
                product -> {
                    viewModel.addToCart(product);

                    // Mostramos el dialogo personalizado de exito
                    InfoDialogFragment.newInstance(
                            "¡Añadido!",
                            "Has añadido " + product.getName() + " al carrito.",
                            false
                    ).show(getSupportFragmentManager(), "infoDialog");
                }
        );

        // Asignar el adaptador al RecyclerView
        rvProducts.setAdapter(adapter);

        // Observamos la lista de productos. Si cambia (o buscamos), actualizamos la pantalla
        viewModel.getProducts().observe(this, products -> {
            adapter.submitList(products);
        });

        // Configurar el boton flotante para ir al carrito
        FloatingActionButton fab = findViewById(R.id.fabGoToCart);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Verificar que el usuario sigue logueado al volver a esta pantalla
        // Si no hay usuario, volvemos al Login por seguridad
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            // Si hay usuario, refrescamos el ID en el repositorio
            viewModel.refreshUser();
        }
    }

    // MENU SUPERIOR (Lupa, Historial, Perfil)

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar el menu con las opciones
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Configurar el buscador de la barra superior
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Al escribir, filtramos la lista automaticamente a traves del ViewModel
                viewModel.search(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Gestionar los clics en los iconos del menu
        int id = item.getItemId();

        if (id == R.id.action_history) {
            // Ir al historial de pedidos
            startActivity(new Intent(this, OrderHistoryActivity.class));
            return true;
        } else if (id == R.id.action_profile) {
            // Ir al perfil de usuario
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}