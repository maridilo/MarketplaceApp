package com.example.marketplaceapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.marketplaceapp.R;
import com.example.marketplaceapp.model.CartItem;

// Adaptador para la lista del carrito usando ListAdapter
public class CartAdapter extends ListAdapter<CartItem, CartAdapter.CartViewHolder> {

    // Interfaces para los clics en los botones de cantidad y borrar
    public interface OnQuantityChangeListener { void onChange(CartItem item, int change); }
    public interface OnDeleteListener { void onDelete(CartItem item); }

    private final OnQuantityChangeListener qtyListener;
    private final OnDeleteListener deleteListener;

    // Constructor que recibe los listeners
    public CartAdapter(OnQuantityChangeListener qtyListener, OnDeleteListener deleteListener) {
        super(DIFF_CALLBACK);
        this.qtyListener = qtyListener;
        this.deleteListener = deleteListener;
    }

    // Callback para calcular diferencias y optimizar actualizaciones de la lista
    private static final DiffUtil.ItemCallback<CartItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<CartItem>() {
        @Override public boolean areItemsTheSame(@NonNull CartItem old, @NonNull CartItem neew) {
            return old.getId() == neew.getId();
        }
        @Override public boolean areContentsTheSame(@NonNull CartItem old, @NonNull CartItem neew) {
            return old.getQuantity() == neew.getQuantity();
        }
    };

    @NonNull @Override public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el diseño de cada fila del carrito
        return new CartViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        // Vinculamos los datos con la vista
        holder.bind(getItem(position), qtyListener, deleteListener);
    }

    // Clase interna para mantener referencias a las vistas
    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, qty;
        Button plus, minus;
        ImageButton delete;
        ImageView ivImage;

        public CartViewHolder(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.tvCartName);
            price = v.findViewById(R.id.tvCartPrice);
            qty = v.findViewById(R.id.tvCartQty);
            plus = v.findViewById(R.id.btnPlus);
            minus = v.findViewById(R.id.btnMinus);
            delete = v.findViewById(R.id.btnDelete);
            ivImage = v.findViewById(R.id.ivCartImage);
        }

        // Metodo para asignar valores y listeners a los elementos visuales
        public void bind(CartItem item, OnQuantityChangeListener qL, OnDeleteListener dL) {
            name.setText(item.getName());
            price.setText(String.format("%.2f €", item.getPrice()));
            qty.setText(String.valueOf(item.getQuantity()));

            // Carga de imagen con Glide
            com.bumptech.glide.Glide.with(itemView.getContext())
                    .load(item.getImageUrl())
                    .centerCrop()
                    .into(ivImage);

            // Asignación de acciones a los botones
            plus.setOnClickListener(v -> qL.onChange(item, 1));
            minus.setOnClickListener(v -> qL.onChange(item, -1));
            delete.setOnClickListener(v -> dL.onDelete(item));
        }
    }
}