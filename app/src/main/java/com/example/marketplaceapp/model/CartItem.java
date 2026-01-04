package com.example.marketplaceapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Entidad que representa un articulo del carrito en la base de datos
@Entity(tableName = "cart_items")
public class CartItem {

    // Clave primaria autogenerada
    @PrimaryKey(autoGenerate = true)
    private long id;

    // Campos de datos
    private String userId;     // ID del usuario dueño del carrito
    private long productId;    // ID del producto original
    private String name;       // Nombre del producto
    private double price;      // Precio del producto
    private String imageUrl;   // URL de la imagen
    private int quantity;      // Cantidad de unidades

    // Constructor vacio obligatorio para Room
    public CartItem() {
    }

    // Constructor completo para crear nuevos objetos
    public CartItem(String userId, long productId, String name, double price, String imageUrl, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    // Getters y Setters para acceder y modificar los campos

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}