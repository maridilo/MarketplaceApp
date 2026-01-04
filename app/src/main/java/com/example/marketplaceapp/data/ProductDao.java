package com.example.marketplaceapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.marketplaceapp.model.CartItem;
import com.example.marketplaceapp.model.Order;
import com.example.marketplaceapp.model.Product;

import java.util.List;

// Interfaz DAO que define las operaciones SQL para Room
@Dao
public interface ProductDao {

    // PRODUCTOS

    // Obtiene la lista completa de productos disponibles
    @Query("SELECT * FROM products")
    LiveData<List<Product>> getAllProducts();

    // Filtra productos cuyo nombre contenga el texto de busqueda
    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%'")
    LiveData<List<Product>> searchProducts(String query);

    // Inserta la lista inicial de productos (datos de prueba)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProducts(List<Product> products);

    // CARRITO

    // Recupera los items del carrito asociados a un usuario
    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    LiveData<List<CartItem>> getCartItems(String userId);

    // Guarda un item en el carrito o lo reemplaza si ya existe
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addToCart(CartItem item);

    // Elimina un item especifico del carrito
    @Delete
    void deleteCartItem(CartItem item);

    // Actualiza solo la cantidad de un producto en el carrito
    @Query("UPDATE cart_items SET quantity = :newQuantity WHERE id = :itemId")
    void updateQuantity(long itemId, int newQuantity);

    // Borra todos los productos del carrito de un usuario
    @Query("DELETE FROM cart_items WHERE userId = :userId")
    void clearCart(String userId);

    // PEDIDOS

    // Obtiene el historial de pedidos del usuario ordenados por fecha reciente
    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY date DESC")
    LiveData<List<Order>> getAllOrders(String userId);

    // Guarda un nuevo pedido en la base de datos
    @Insert
    void insertOrder(Order order);
}