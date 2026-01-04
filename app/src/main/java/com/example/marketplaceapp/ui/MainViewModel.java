package com.example.marketplaceapp.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.marketplaceapp.data.MarketplaceRepository;
import com.example.marketplaceapp.model.CartItem;
import com.example.marketplaceapp.model.Order;
import com.example.marketplaceapp.model.Product;

import java.util.List;

// ViewModel que gestiona la logica de la interfaz y comunica con el repositorio
public class MainViewModel extends AndroidViewModel {

    private final MarketplaceRepository repository;

    // LiveData que almacena el texto que el usuario escribe en el buscador
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new MarketplaceRepository(application);
        // Cargar datos de prueba al iniciar la aplicacion
        repository.insertInitialData();
    }

    // LECTURA DE DATOS

    // Devuelve la lista de productos filtrada.
    // Observa el buscador: si esta vacio devuelve todo, si no, busca por nombre.
    public LiveData<List<Product>> getProducts() {
        return Transformations.switchMap(searchQuery, query -> {
            if (query == null || query.isEmpty()) {
                return repository.getAllProducts();
            } else {
                return repository.searchProducts(query);
            }
        });
    }

    // Devuelve los items del carrito actual
    public LiveData<List<CartItem>> getCartItems() { return repository.getMyCart(); }

    // Devuelve el historial de pedidos
    public LiveData<List<Order>> getOrders() { return repository.getMyOrders(); }

    // ACCIONES

    // Actualiza el valor del texto de busqueda
    public void search(String query) {
        searchQuery.setValue(query);
    }

    // Añade un producto al carrito
    public void addToCart(Product product) {
        repository.addToCart(product);
    }

    // Modifica la cantidad de un producto en el carrito (+1 o -1)
    public void changeQuantity(CartItem item, int change) {
        repository.changeQuantity(item, change);
    }

    // Elimina un producto especifico del carrito
    public void deleteFromCart(CartItem item) {
        repository.deleteCartItem(item);
    }

    // Finaliza la compra y guarda el pedido
    public void checkout(double total) {
        repository.checkout(total);
    }

    // Actualiza el usuario en el repositorio (al hacer login o logout)
    public void refreshUser() {
        repository.refreshUser();
    }
}