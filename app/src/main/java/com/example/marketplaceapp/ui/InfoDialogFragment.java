package com.example.marketplaceapp.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.marketplaceapp.R;

// Fragmento personalizado para mostrar dialogos de informacion o error
public class InfoDialogFragment extends DialogFragment {

    // Metodo estatico para crear una nueva instancia pasando titulo y mensaje
    public static InfoDialogFragment newInstance(String title, String message, boolean isError) {
        InfoDialogFragment fragment = new InfoDialogFragment();
        Bundle args = new Bundle();
        args.putString("TITLE", title);
        args.putString("MSG", message);
        args.putBoolean("IS_ERROR", isError);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Configuramos el fondo transparente para que se vean las esquinas redondeadas del diseño
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return inflater.inflate(R.layout.dialog_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Referencias a los elementos visuales del dialogo
        TextView tvTitle = view.findViewById(R.id.tvDialogTitle);
        TextView tvMsg = view.findViewById(R.id.tvDialogMessage);
        ImageView ivIcon = view.findViewById(R.id.ivDialogIcon);
        Button btnOk = view.findViewById(R.id.btnDialogOk);

        // Recuperamos los argumentos pasados al crear el fragmento
        if (getArguments() != null) {
            tvTitle.setText(getArguments().getString("TITLE"));
            tvMsg.setText(getArguments().getString("MSG"));
            boolean isError = getArguments().getBoolean("IS_ERROR");

            // Cambiamos el estilo visual dependiendo de si es un error o un exito
            if (isError) {
                // Modo ERROR: Icono y colores en rojo
                ivIcon.setImageResource(android.R.drawable.ic_delete);
                ivIcon.setColorFilter(Color.parseColor("#F44336"));
                btnOk.setBackgroundColor(Color.parseColor("#F44336"));
                tvTitle.setTextColor(Color.parseColor("#F44336"));
            } else {
                // Modo EXITO: Icono y colores en verde
                ivIcon.setImageResource(android.R.drawable.ic_dialog_info);
                ivIcon.setColorFilter(Color.parseColor("#4CAF50"));
                btnOk.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
        }

        // Boton para cerrar la ventana
        btnOk.setOnClickListener(v -> dismiss());
    }
}