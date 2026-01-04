package com.example.marketplaceapp.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.marketplaceapp.data.ProductDao;
import com.example.marketplaceapp.data.MarketplaceDatabase;
import com.example.marketplaceapp.model.CartItem;
import com.example.marketplaceapp.model.Order;
import com.example.marketplaceapp.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

// Repositorio central que gestiona los datos de Room y Firebase
public class MarketplaceRepository {

    private final ProductDao productDao;
    private final LiveData<List<Product>> allProducts;

    // LiveData para detectar cambios en el usuario logueado
    private final MutableLiveData<String> currentUserId = new MutableLiveData<>();

    // Inicializamos la base de datos y el usuario actual de Firebase
    public MarketplaceRepository(Application application) {
        MarketplaceDatabase db = MarketplaceDatabase.getDatabase(application);
        productDao = db.productDao();
        allProducts = productDao.getAllProducts();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId.setValue(user != null ? user.getUid() : "invitado");
    }

    // Devuelve todos los productos de la base de datos
    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    // Detecta cambios en el usuario y actualiza la lista del carrito automaticamente
    public LiveData<List<CartItem>> getMyCart() {
        return Transformations.switchMap(currentUserId, productDao::getCartItems);
    }

    // Detecta cambios en el usuario y actualiza el historial de pedidos
    public LiveData<List<Order>> getMyOrders() {
        return Transformations.switchMap(currentUserId, productDao::getAllOrders);
    }

    // Busca productos por nombre
    public LiveData<List<Product>> searchProducts(String query) {
        return productDao.searchProducts(query);
    }

    // Ejecuta la insercion de datos de prueba en un hilo secundario
    public void insertInitialData() {
        MarketplaceDatabase.databaseWriteExecutor.execute(() -> {
            List<Product> dummies = new ArrayList<>();
            dummies.add(new Product("Cámara Vintage", "Cámara clásica 35mm", 120.50, "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?auto=format&fit=crop&w=500&q=60", "Electrónica"));
            dummies.add(new Product("Zapatillas Running", "Nike Air Zoom", 45.00, "https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=500&q=60", "Deporte"));
            dummies.add(new Product("Reloj Inteligente", "Smartwatch con GPS", 89.99, "https://images.unsplash.com/photo-1546868871-7041f2a55e12?auto=format&fit=crop&w=500&q=60", "Electrónica"));
            dummies.add(new Product("Mochila de Viaje", "Resistente al agua", 35.00, "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?auto=format&fit=crop&w=500&q=60", "Accesorios"));
            dummies.add(new Product("Gafas de Sol", "Protección UV400", 25.00, "https://images.unsplash.com/photo-1572635196237-14b3f281503f?auto=format&fit=crop&w=500&q=60", "Accesorios"));

            productDao.insertProducts(dummies);
        });
    }

    // Añade un producto al carrito asociandolo al usuario actual
    public void addToCart(Product product) {
        String uid = currentUserId.getValue();
        if (uid == null) return;

        MarketplaceDatabase.databaseWriteExecutor.execute(() -> {
            CartItem item = new CartItem(uid, product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), 1);
            productDao.addToCart(item);
        });
    }

    // Modifica la cantidad de un item o lo borra si la cantidad es cero
    public void changeQuantity(CartItem item, int change) {
        MarketplaceDatabase.databaseWriteExecutor.execute(() -> {
            int newQty = item.getQuantity() + change;
            if (newQty > 0) {
                productDao.updateQuantity(item.getId(), newQty);
            } else {
                productDao.deleteCartItem(item);
            }
        });
    }

    // Elimina un articulo del carrito
    public void deleteCartItem(CartItem item) {
        MarketplaceDatabase.databaseWriteExecutor.execute(() -> {
            productDao.deleteCartItem(item);
        });
    }

    // Crea el pedido y vacia el carrito del usuario
    public void checkout(double totalAmount) {
        String uid = currentUserId.getValue();
        if (uid == null) return;

        MarketplaceDatabase.databaseWriteExecutor.execute(() -> {
            Order order = new Order(uid, System.currentTimeMillis(), totalAmount, "Pagado");
            productDao.insertOrder(order);
            productDao.clearCart(uid);
        });
    }

    // Actualiza el ID del usuario al hacer login o logout
    public void refreshUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId.setValue(user != null ? user.getUid() : "invitado");
    }
}