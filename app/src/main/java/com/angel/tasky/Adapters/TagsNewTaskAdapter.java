package com.angel.tasky.Adapters;

import android.content.Context;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angel.tasky.GLOBALES.GLOBAL;
import com.angel.tasky.POJOs.Tag;
import com.angel.tasky.R;

import java.util.ArrayList;

public class TagsNewTaskAdapter extends RecyclerView.Adapter<TagsNewTaskAdapter.TagsNewTaskActity> {
    Context context;
    public int id_proy;
    ArrayList<Tag> selectedTags;

    public interface TagsNewTaskAdapterListener{
         ArrayList<Tag> ReturnSelectedTags(ArrayList<Tag> _selectedTags);

    }
    TagsNewTaskAdapterListener listener;

    public TagsNewTaskAdapter(Context context, int id_proy, TagsNewTaskAdapterListener listener){
        this.context = context;
        this.id_proy = id_proy;
        this.listener = listener;
        selectedTags = new ArrayList<>();
    }

    @NonNull
    @Override
    public TagsNewTaskActity onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context,R.layout.item_tag_1,null);
        TagsNewTaskActity tagsNewTaskActity = new TagsNewTaskActity(view);
        return tagsNewTaskActity;
    }

    @Override
    public void onBindViewHolder(@NonNull final TagsNewTaskActity tagsNewTaskActity, int i) {
        final int indice = i;
        if(GLOBAL.TAGS.get(indice).getId_Proyecto() != id_proy){
            tagsNewTaskActity.contenedor.setVisibility(View.GONE);
        }else{
            tagsNewTaskActity.tagNombre.setText(GLOBAL.TAGS.get(indice).getNombreTag());
            //tagsNewTaskActity.tagNombre.setVisibility(View.GONE);
            //tagsNewTaskActity.selected.setText(GLOBAL.TAGS.get(indice).getNombreTag());
            tagsNewTaskActity.cant.setVisibility(View.GONE);
            tagsNewTaskActity.selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tagsNewTaskActity.selected.isChecked()){
                        selectedTags.add(GLOBAL.TAGS.get(indice));
                    }else{
                        if(selectedTags.contains(GLOBAL.TAGS.get(indice))){
                            selectedTags.remove(GLOBAL.TAGS.get(indice));
                        }
                    }
                    listener.ReturnSelectedTags(selectedTags);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return GLOBAL.TAGS.size();
    }

    public class TagsNewTaskActity extends RecyclerView.ViewHolder {
        LinearLayout contenedor;
        TextView tagNombre, cant;
        CheckBox selected;
        public TagsNewTaskActity(@NonNull View itemView) {
            super(itemView);

            contenedor = itemView.findViewById(R.id.tag_contenedor_2);
            tagNombre = itemView.findViewById(R.id.tag_1);
            cant = itemView.findViewById(R.id.tag_1_number);
            selected = itemView.findViewById(R.id.checked_tag_1);
        }
    }
}
