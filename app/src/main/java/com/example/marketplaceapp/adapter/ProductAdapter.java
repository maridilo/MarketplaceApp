package com.example.marketplaceapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.marketplaceapp.R;
import com.example.marketplaceapp.model.Product;

// Adaptador para mostrar la lista de productos usando ListAdapter
public class ProductAdapter extends ListAdapter<Product, ProductAdapter.ProductViewHolder> {

    // Interfaces para gestionar los eventos de clic en el item o en el boton
    public interface OnItemClickListener {
        void onItemClick(Product product);
    }
    public interface OnAddToCartClickListener {
        void onAddToCart(Product product);
    }

    private final OnItemClickListener itemClickListener;
    private final OnAddToCartClickListener addClickListener;

    public ProductAdapter(OnItemClickListener itemListener, OnAddToCartClickListener addListener) {
        super(DIFF_CALLBACK);
        this.itemClickListener = itemListener;
        this.addClickListener = addListener;
    }

    // Comparador para optimizar la actualizacion de la lista cuando cambian los datos
    private static final DiffUtil.ItemCallback<Product> DIFF_CALLBACK = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId() == newItem.getId(); // Compara por ID unico
        }
        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            // Compara si el contenido visual ha cambiado
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getPrice() == newItem.getPrice();
        }
    };

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el layout de cada tarjeta de producto
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Obtenemos el producto y lo vinculamos a la vista
        Product product = getItem(position);
        holder.bind(product, itemClickListener, addClickListener);
    }

    // Clase interna para mantener las referencias a las vistas
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvPrice;
        private final ImageView ivImage;
        private final ImageButton btnAdd;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            ivImage = itemView.findViewById(R.id.ivProductImage);
            btnAdd = itemView.findViewById(R.id.btnAddCart);
        }

        // Metodo para asignar los valores y los listeners a los elementos de la vista
        public void bind(Product product, OnItemClickListener itemListener, OnAddToCartClickListener addListener) {
            tvName.setText(product.getName());
            tvPrice.setText(String.format("%.2f €", product.getPrice()));

            // Cargar imagen de forma asincrona usando la libreria Glide
            Glide.with(itemView.getContext())
                    .load(product.getImageUrl())
                    .centerCrop()
                    .into(ivImage);

            // Configurar las acciones al pulsar sobre el producto o el boton de añadir
            itemView.setOnClickListener(v -> itemListener.onItemClick(product));
            btnAdd.setOnClickListener(v -> addListener.onAddToCart(product));
        }
    }
}