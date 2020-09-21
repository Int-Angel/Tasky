package com.angel.tasky.Adapters;

import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.angel.tasky.GLOBALES.GLOBAL;
import com.angel.tasky.ProjectsAndTagsFragment;
import com.angel.tasky.R;

import static android.content.Context.VIBRATOR_SERVICE;

public class ProjectsAndTagsAdapter extends RecyclerView.Adapter<ProjectsAndTagsAdapter.ProjectAndTagsActivity> {
    public Context context;

    public interface ProjectsAndTagsAdapterListener{
        void EliminarProyecto(int i);
        void EliminarTag(int i, int j);
        void AgregarTag(int i);
        void ActualizarProject(int i);
        void GuardarTag(int i);
    }
    ProjectsAndTagsAdapterListener listener;

    public ProjectsAndTagsAdapter(Context context, ProjectsAndTagsAdapterListener listener){
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProjectAndTagsActivity onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context,R.layout.item_simple_project,null);
        ProjectAndTagsActivity projectAndTagsActivity = new ProjectAndTagsActivity(view);
        return projectAndTagsActivity;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProjectAndTagsActivity projectAndTagsActivity, int i) {
        final int indice = i;
        final Animation rotate = AnimationUtils.loadAnimation(context, R.anim.button_rotation_up);
        final Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

        projectAndTagsActivity.project.setText(GLOBAL.PROYECTOS.get(indice).getNombreProyecto());

        if(indice == GLOBAL.INDICADOR.getPos_proyecto_edit()){
            projectAndTagsActivity.eliminarProyecto.setVisibility(View.VISIBLE);
            projectAndTagsActivity.saveProject.setVisibility(View.VISIBLE);
            projectAndTagsActivity.project.setTextColor(context.getColor(R.color.colorNegro));
            projectAndTagsActivity.project.setFocusableInTouchMode(true);
            projectAndTagsActivity.project.setFocusable(true);
            projectAndTagsActivity.project.setClickable(true);
        }else{
            projectAndTagsActivity.eliminarProyecto.setVisibility(View.GONE);
            projectAndTagsActivity.saveProject.setVisibility(View.GONE);
            projectAndTagsActivity.project.setTextColor(context.getColor(R.color.colorText2));
            projectAndTagsActivity.project.setFocusableInTouchMode(false);
            projectAndTagsActivity.project.setFocusable(false);
            projectAndTagsActivity.project.setClickable(false);
        }

        projectAndTagsActivity.mostrarMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(projectAndTagsActivity.mostrar){
                    projectAndTagsActivity.mostrar = false;
                    projectAndTagsActivity.Lista_Tags.setVisibility(View.GONE);
                    projectAndTagsActivity.agregarTag.setVisibility(View.GONE);

                    projectAndTagsActivity.mostrarMas.startAnimation(rotate);
                    projectAndTagsActivity.mostrarMas.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
                }else{
                    projectAndTagsActivity.mostrar = true;
                    projectAndTagsActivity.Lista_Tags.setVisibility(View.VISIBLE);
                    projectAndTagsActivity.agregarTag.setVisibility(View.VISIBLE);
                    projectAndTagsActivity.mostrarMas.startAnimation(rotate);
                    projectAndTagsActivity.mostrarMas.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
                }
            }
        });
        projectAndTagsActivity.eliminarProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GLOBAL.INDICADOR.setPos_proyecto_edit(-1);
                listener.EliminarProyecto(indice);
            }
        });


        projectAndTagsActivity.project.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                vibrator.vibrate(60);
                if(GLOBAL.INDICADOR.getPos_proyecto_edit() == indice){
                    GLOBAL.INDICADOR.setPos_proyecto_edit(-1);
                }else {
                    GLOBAL.INDICADOR.setPos_proyecto_edit(indice);
                }
                notifyDataSetChanged();
                return false;
            }
        });

        projectAndTagsActivity.saveProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(60);
                projectAndTagsActivity.eliminarProyecto.setVisibility(View.GONE);
                projectAndTagsActivity.saveProject.setVisibility(View.GONE);
                projectAndTagsActivity.project.setTextColor(context.getColor(R.color.colorText2));
                projectAndTagsActivity.project.setFocusableInTouchMode(false);
                projectAndTagsActivity.project.setFocusable(false);
                projectAndTagsActivity.project.setClickable(false);
                GLOBAL.INDICADOR.setPos_proyecto_edit(-1);
                GLOBAL.PROYECTOS.get(indice).setNombreProyecto(projectAndTagsActivity.project.getText().toString());
                listener.ActualizarProject(indice);
            }
        });

        projectAndTagsActivity.agregarTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.AgregarTag(indice);
                //tagAdapter.notifyDataSetChanged();
            }
        });

        projectAndTagsActivity.tagAdapter = new TagAdapter(context, new TagAdapter.TagAdapterListener() {
            @Override
            public void EliminarTag(int pos_tag) {
                listener.EliminarTag(indice,pos_tag);
            }

            @Override
            public void GuardarTag(int pos_tag) {
                listener.GuardarTag(pos_tag);
            }

            @Override
            public void REFRESH() {
                notifyDataSetChanged();
            }
        },GLOBAL.PROYECTOS.get(indice).getId_Proyecto());



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);

        projectAndTagsActivity.Lista_Tags.setAdapter(projectAndTagsActivity.tagAdapter);
        projectAndTagsActivity.Lista_Tags.setLayoutManager(linearLayoutManager);

        /*if(projectAndTagsActivity.tagAdapter != null){
            listener.REFRESCARTAGS();
            projectAndTagsActivity.tagAdapter.notifyDataSetChanged();
        }*/
    }


    @Override
    public int getItemCount() {
        return GLOBAL.PROYECTOS.size();
    }



    public class ProjectAndTagsActivity extends RecyclerView.ViewHolder {
        EditText project;
        RecyclerView Lista_Tags;
        ImageButton mostrarMas, agregarTag, eliminarProyecto, saveProject;
        public TagAdapter tagAdapter;
        Boolean mostrar;


        public ProjectAndTagsActivity(@NonNull View itemView) {
            super(itemView);
            project = itemView.findViewById(R.id.project_simple_project);
            Lista_Tags = itemView.findViewById(R.id.LISTA_SIMPLE_TAGS);
            mostrarMas = itemView.findViewById(R.id.mostrar_mas_item_simple_project);
            agregarTag = itemView.findViewById(R.id.button_new_tag_simple_project);
            eliminarProyecto = itemView.findViewById(R.id.delete_project);
            saveProject = itemView.findViewById(R.id.save_project);
            mostrar = true;
        }

    }
}
