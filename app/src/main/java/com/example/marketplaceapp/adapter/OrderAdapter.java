package com.example.marketplaceapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.marketplaceapp.R;
import com.example.marketplaceapp.model.Order;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Adaptador para mostrar la lista de pedidos en el historial
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    // Lista de pedidos inicializada vacía
    private List<Order> orders = new ArrayList<>();

    // Actualiza la lista de pedidos y notifica al adaptador
    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged(); // ¡IMPORTANTE! Esto refresca la pantalla
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el layout de cada fila de pedido
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        // Vinculamos los datos del pedido con la vista
        holder.bind(orders.get(position));
    }

    @Override
    public int getItemCount() {
        // Devuelve el número de pedidos
        return orders != null ? orders.size() : 0;
    }

    // Clase interna para mantener las referencias a las vistas de cada ítem
    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView date, total, status;

        public OrderViewHolder(@NonNull View v) {
            super(v);
            date = v.findViewById(R.id.tvOrderDate);
            total = v.findViewById(R.id.tvOrderTotal);
            status = v.findViewById(R.id.tvOrderStatus);
        }

        // Asigna los valores del pedido a los TextViews
        public void bind(Order order) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            date.setText(sdf.format(new Date(order.getDate())));
            total.setText(String.format("%.2f €", order.getTotalAmount()));
            status.setText(order.getStatus());
        }
    }
}