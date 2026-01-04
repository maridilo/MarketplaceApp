package com.example.marketplaceapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

// Entidad que define la estructura de un producto en la base de datos
@Entity(tableName = "products")
public class Product implements Serializable {

    // Clave primaria autogenerada
    @PrimaryKey(autoGenerate = true)
    private long id;

    // Campos de datos del producto
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String category;

    // Constructor vacio obligatorio para Room y Firebase
    public Product() {
    }

    // Constructor completo para crear nuevos productos
    public Product(String name, String description, double price, String imageUrl, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    // Getters y Setters para acceder y modificar los datos

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}