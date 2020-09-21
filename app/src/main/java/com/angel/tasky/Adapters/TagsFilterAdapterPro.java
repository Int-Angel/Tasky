package com.angel.tasky.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.angel.tasky.GLOBALES.GLOBAL;
import com.angel.tasky.POJOs.Proyecto;
import com.angel.tasky.POJOs.Tag;
import com.angel.tasky.POJOs.TagTask;
import com.angel.tasky.R;

import java.util.ArrayList;

public class TagsFilterAdapterPro extends RecyclerView.Adapter<TagsFilterAdapterPro.TagFilterActivityPro> {
    Context context;
    boolean all;
    ArrayList<Proyecto> selectedProjects;
    ArrayList<Tag> tags;

    public interface TagsFilterAdapterProListener{
        void apagarAll();
        void añadirTag(Tag x);
        void quitarTag(Tag x);
    }

    TagsFilterAdapterProListener listener;

    public TagsFilterAdapterPro(Context context, ArrayList<Proyecto> selectedProjects, TagsFilterAdapterProListener listener){
        this.context = context;
        this.listener = listener;
        all = false;
        this.selectedProjects = new ArrayList<>(selectedProjects);
        this.tags = new ArrayList<>();
        llenarTags();
    }

    @NonNull
    @Override
    public TagFilterActivityPro onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.item_selected_tag, null);
        TagFilterActivityPro tagFilterActivityPro = new TagFilterActivityPro(view);
        return tagFilterActivityPro;
    }

    @Override
    public void onBindViewHolder(@NonNull TagFilterActivityPro tagFilterActivityPro, int i) {
        final int indice = i;

        tagFilterActivityPro.quitar.setVisibility(View.GONE);

        tagFilterActivityPro.cantidad.setText(""+contarTareas(tags.get(indice).getId_Tag()));

        tagFilterActivityPro.tag.setText(tags.get(indice).getNombreTag());

        tagFilterActivityPro.tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.añadirTag(tags.get(indice));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    private void llenarTags(){
        for (Tag x: GLOBAL.TAGS){
            for (Proyecto y : selectedProjects){
                if(x.getId_Proyecto() == y.getId_Proyecto()){
                    tags.add(x);
                }
            }
        }
    }

    public void changeSelectedProjects(ArrayList<Proyecto> newSelectedProjects){
        selectedProjects.clear();
        tags.clear();
        selectedProjects.addAll(newSelectedProjects);
        llenarTags();
        notifyDataSetChanged();
    }

    public int contarTareas(int id_tag){
        int n = 0;

        for (TagTask tagTask : GLOBAL.TAG_TASKS){
            if(tagTask.getId_Tag() == id_tag){
                n++;
            }
        }

        return n;
    }

    public class TagFilterActivityPro extends RecyclerView.ViewHolder {
        ImageButton quitar;
        TextView tag, cantidad;
        Boolean onList;
        public TagFilterActivityPro(@NonNull View itemView) {
            super(itemView);

            tag = itemView.findViewById(R.id.selected_tag_text);
            quitar = itemView.findViewById(R.id.remove_selected_tag);
            cantidad = itemView.findViewById(R.id.selected_tag_number);
            onList = false;
        }
    }
}
