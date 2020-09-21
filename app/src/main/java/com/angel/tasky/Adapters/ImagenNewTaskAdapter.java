package com.angel.tasky.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.angel.tasky.GLOBALES.GLOBAL;
import com.angel.tasky.R;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ImagenNewTaskAdapter extends RecyclerView.Adapter<ImagenNewTaskAdapter.ImagenActivity> {
    Context context;

    public ImagenNewTaskAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ImagenActivity onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context,R.layout.item_image_1,null);
        ImagenActivity imagenActivity = new ImagenActivity(view);
        return imagenActivity;
    }

    @Override
    public void onBindViewHolder(@NonNull ImagenActivity imagenActivity, int i) {
        final int indice = i;


        imagenActivity.imagen.setImageBitmap(GLOBAL.IMAGENS_TEMP.get(indice).getImagen());
        imagenActivity.borrarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GLOBAL.IMAGENS_TEMP.remove(indice);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return GLOBAL.IMAGENS_TEMP.size();
    }

    public class ImagenActivity extends RecyclerView.ViewHolder {
        ImageView imagen;
        ImageButton borrarImagen;

        public ImagenActivity(@NonNull View itemView) {
            super(itemView);

            imagen = itemView.findViewById(R.id.item_imagen_1_imagen);
            borrarImagen = itemView.findViewById(R.id.delete_image_image_item_1);
        }
    }
}
