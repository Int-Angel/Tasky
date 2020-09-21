package com.angel.tasky.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.angel.tasky.GLOBALES.GLOBAL;
import com.angel.tasky.R;

public class ImagenTaskInfoAdapter extends RecyclerView.Adapter<ImagenTaskInfoAdapter.ImagenActivity> {
    Context context;
    int idTask;

    public interface ImagenTaskInfoAdapterListener{
        void ExpandirImagen(int pos);
    }

    ImagenTaskInfoAdapterListener listener;

    public ImagenTaskInfoAdapter(Context context, int idTask, ImagenTaskInfoAdapterListener listener){
        this.context = context;
        this.idTask = idTask;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ImagenActivity onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.item_image_1,null);
        ImagenActivity imagenActivity = new ImagenActivity(view);
        return imagenActivity;
    }

    @Override
    public void onBindViewHolder(@NonNull ImagenActivity imagenActivity, int i) {
        final int indice = i;

        if(GLOBAL.IMAGENS.get(indice).getId_Task() == idTask){
            imagenActivity.imagen.setImageBitmap(GLOBAL.IMAGENS.get(indice).getImagen());
            imagenActivity.imagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.ExpandirImagen(indice);
                }
            });
            imagenActivity.borrarImagen.setVisibility(View.GONE);
            imagenActivity.borrarImagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }else{
            imagenActivity.container.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return GLOBAL.IMAGENS.size();
    }

    public class ImagenActivity extends RecyclerView.ViewHolder {
        ImageView imagen;
        ImageButton borrarImagen;
        RelativeLayout container;

        public ImagenActivity(@NonNull View itemView) {
            super(itemView);

            imagen = itemView.findViewById(R.id.item_imagen_1_imagen);
            borrarImagen = itemView.findViewById(R.id.delete_image_image_item_1);
            container = itemView.findViewById(R.id.image_container);
        }
    }
}
