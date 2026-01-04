package com.example.marketplaceapp.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.marketplaceapp.model.CartItem;
import com.example.marketplaceapp.model.Order;
import com.example.marketplaceapp.model.Product;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Configuracion de la base de datos Room con sus entidades y version
@Database(entities = {Product.class, CartItem.class, Order.class}, version = 1, exportSchema = false)
public abstract class MarketplaceDatabase extends RoomDatabase {

    // Metodo abstracto para acceder al DAO
    public abstract ProductDao productDao();

    // Instancia unica (Singleton) para evitar abrir multiples conexiones
    private static volatile MarketplaceDatabase INSTANCE;

    // Executor para realizar operaciones de base de datos en segundo plano
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Metodo estatico para obtener la instancia de la base de datos
    public static MarketplaceDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MarketplaceDatabase.class) {
                if (INSTANCE == null) {
                    // Crea la base de datos si no existe
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MarketplaceDatabase.class, "marketplace_database")
                            // Borra y reconstruye la base de datos si cambia el esquema
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}