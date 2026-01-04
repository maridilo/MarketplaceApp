package com.example.marketplaceapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Entidad que representa un pedido realizado en la base de datos
@Entity(tableName = "orders")
public class Order {

    // Clave primaria autogenerada
    @PrimaryKey(autoGenerate = true)
    private long id;

    // Campos de datos del pedido
    private String userId;
    private long date;          // Fecha en milisegundos
    private double totalAmount; // Precio total pagado
    private String status;      // Estado actual (Pagado, Enviado, etc.)

    // Constructor vacio necesario para Room
    public Order() {
    }

    // Constructor completo para crear nuevos pedidos
    public Order(String userId, long date, double totalAmount, String status) {
        this.userId = userId;
        this.date = date;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters y Setters para acceder y modificar los datos

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}