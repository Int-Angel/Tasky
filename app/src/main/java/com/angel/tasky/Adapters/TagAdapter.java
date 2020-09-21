package com.angel.tasky.Adapters;

import android.content.Context;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.angel.tasky.GLOBALES.GLOBAL;
import com.angel.tasky.POJOs.Tag;
import com.angel.tasky.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.VIBRATOR_SERVICE;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagActivity> {
    Context context;
    int id_proy;


    public interface TagAdapterListener{
        void EliminarTag(int i);
        void GuardarTag(int i);
        void REFRESH();
    }

    TagAdapterListener listener;

    public TagAdapter(Context context, TagAdapterListener listener, int id_proy){
        this.context = context;
        this.listener = listener;
        this.id_proy = id_proy;


    }

    @NonNull
    @Override
    public TagActivity onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.item_simple_tag,null);
        TagActivity tagActivity = new TagActivity(view);
        return tagActivity;
    }

    @Override
    public void onBindViewHolder(@NonNull final TagActivity tagActivity, int i) {
        final int indice = i;
        final Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

        if(GLOBAL.TAGS.get(indice).getId_Proyecto() != id_proy){
            tagActivity.contenedor.setVisibility(View.GONE);
        }else{
            tagActivity.tagTexto.setText(GLOBAL.TAGS.get(indice).getNombreTag());


            if(GLOBAL.INDICADOR.getPos_tag_edit() == indice){
                tagActivity.tagTexto.setTextColor(context.getColor(R.color.colorNegro));
                tagActivity.tagTexto.setFocusableInTouchMode(true);
                tagActivity.tagTexto.setFocusable(true);
                tagActivity.tagTexto.setClickable(true);
                tagActivity.guardarTag.setVisibility(View.VISIBLE);
                tagActivity.eliminarTag.setVisibility(View.VISIBLE);
            }else{
                tagActivity.tagTexto.setTextColor(context.getColor(R.color.colorText2));
                tagActivity.tagTexto.setFocusableInTouchMode(false);
                tagActivity.tagTexto.setFocusable(false);
                tagActivity.tagTexto.setClickable(false);
                tagActivity.guardarTag.setVisibility(View.GONE);
                tagActivity.eliminarTag.setVisibility(View.GONE);
            }


            tagActivity.tagTexto.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    vibrator.vibrate(60);

                    if(GLOBAL.INDICADOR.getPos_tag_edit() == indice){
                        GLOBAL.INDICADOR.setPos_tag_edit(-1);
                    }else{
                        GLOBAL.INDICADOR.setPos_tag_edit(indice);
                    }
                    notifyDataSetChanged();
                    listener.REFRESH();
                    return false;
                }
            });

            tagActivity.guardarTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(60);
                    tagActivity.tagTexto.setTextColor(context.getColor(R.color.colorText2));
                    tagActivity.tagTexto.setFocusableInTouchMode(false);
                    tagActivity.tagTexto.setFocusable(false);
                    tagActivity.tagTexto.setClickable(false);
                    tagActivity.guardarTag.setVisibility(View.GONE);
                    tagActivity.eliminarTag.setVisibility(View.GONE);
                    GLOBAL.INDICADOR.setPos_tag_edit(-1);

                    GLOBAL.TAGS.get(indice).setNombreTag(tagActivity.tagTexto.getText().toString());
                    listener.GuardarTag(indice);
                }
            });

            tagActivity.eliminarTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GLOBAL.INDICADOR.setPos_tag_edit(-1);
                    listener.EliminarTag(indice);
                }
            });
        }




    }


    @Override
    public int getItemCount() {
        return GLOBAL.TAGS.size();
    }


    public class TagActivity extends RecyclerView.ViewHolder {
        EditText tagTexto;
        ImageButton eliminarTag, guardarTag;
        RelativeLayout contenedor;

        public TagActivity(@NonNull View itemView) {
            super(itemView);

            tagTexto = itemView.findViewById(R.id.tag_simple_tag);
            eliminarTag = itemView.findViewById(R.id.delete_tag);
            contenedor = itemView.findViewById(R.id.tag_contenedor);
            guardarTag = itemView.findViewById(R.id.save_tag);
        }
    }
}
