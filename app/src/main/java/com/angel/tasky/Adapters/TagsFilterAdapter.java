package com.angel.tasky.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angel.tasky.GLOBALES.GLOBAL;
import com.angel.tasky.POJOs.Proyecto;
import com.angel.tasky.POJOs.Tag;
import com.angel.tasky.POJOs.TagTask;
import com.angel.tasky.R;

import java.util.ArrayList;

public class TagsFilterAdapter extends RecyclerView.Adapter<TagsFilterAdapter.TagsFilterActivity> {
    Context context;
    boolean all;
    ArrayList<Proyecto> selectedProjects;
    ArrayList<Tag> tags;
    public boolean editMode;

    public interface TagsFilterAdapterListener{
        void apagarAll();
        void añadirTag(Tag x);
        void quitarTag(Tag x);
    }
    TagsFilterAdapterListener listener;

    public TagsFilterAdapter(Context context, ArrayList<Proyecto> selectedProjects, TagsFilterAdapterListener listener){
        this.context = context;
        this.listener = listener;
        all = false;
        editMode = false;
        this.selectedProjects = new ArrayList<>(selectedProjects);
        this.tags = new ArrayList<>();
        llenarTags();
    }

    @NonNull
    @Override
    public TagsFilterActivity onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.item_obj,null);
        TagsFilterActivity tagsFilterActivity = new TagsFilterActivity(view);
        return tagsFilterActivity;
    }

    @Override
    public void onBindViewHolder(@NonNull final TagsFilterActivity tagsFilterActivity,  int i) {
        final int indice = i;

        if(editMode){
            for(Tag otrotag : GLOBAL.TAGS_TEMP){
                if(tags.get(indice).getId_Tag() == otrotag.getId_Tag() ){
                    tagsFilterActivity.checkBox.setChecked(true);
                }
            }
            if(indice == tags.size()-1){
                editMode = false;
            }
        }else {
            if(all){



                if(!tagsFilterActivity.checkBox.isChecked()){
                    listener.añadirTag(tags.get(indice));
                }

                tagsFilterActivity.checkBox.setChecked(true);
            }else{

                tagsFilterActivity.checkBox.setChecked(false);
                listener.quitarTag(tags.get(indice));
            }
        }



        tagsFilterActivity.checkBox.setText(tags.get(indice).getNombreTag()+"  ");
        tagsFilterActivity.cantidad.setText(""+contarTareas(tags.get(indice).getId_Tag()));

        tagsFilterActivity.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(all){
                    all = false;
                    listener.apagarAll();
                }
                if(tagsFilterActivity.checkBox.isChecked()){
                    listener.añadirTag(tags.get(indice));
                }else{
                    listener.quitarTag(tags.get(indice));
                }
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

    public void selectAll(){
        all = true;
        notifyDataSetChanged();
    }

    public void quitarAll(){
        all = false;
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




    public class TagsFilterActivity extends RecyclerView.ViewHolder {
        LinearLayout container;
        CheckBox checkBox;
        TextView cantidad;
        public TagsFilterActivity(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.obj_container);
            checkBox = itemView.findViewById(R.id.checked_obj);
            cantidad = itemView.findViewById(R.id.obj_number);
        }
    }
}
