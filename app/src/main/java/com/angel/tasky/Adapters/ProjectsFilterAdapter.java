package com.angel.tasky.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angel.tasky.GLOBALES.GLOBAL;
import com.angel.tasky.POJOs.Proyecto;
import com.angel.tasky.POJOs.Task;
import com.angel.tasky.R;

public class ProjectsFilterAdapter extends RecyclerView.Adapter<ProjectsFilterAdapter.ProjectsFilterActivity> {
    Context context;
    boolean all;
    public interface ProjectsFilterAdapterListener{
        void añadirProyecto(Proyecto x);
        void quitarProyecto(Proyecto x);
        void apagarAll();
    }
    ProjectsFilterAdapterListener listener;

    public ProjectsFilterAdapter(Context context, ProjectsFilterAdapterListener listener){
        this.context = context;
        this.listener = listener;
        all = false;
    }

    @NonNull
    @Override
    public ProjectsFilterActivity onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.item_obj,null);
        ProjectsFilterActivity projectsFilterActivity = new ProjectsFilterActivity(view);
        return projectsFilterActivity;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProjectsFilterActivity projectsFilterActivity, int i) {
        final int indice = i;

        if(all){
            if(!projectsFilterActivity.checkBox.isChecked()){
                listener.añadirProyecto(GLOBAL.PROYECTOS.get(indice));
            }
            projectsFilterActivity.checkBox.setChecked(true);
        }else{
            projectsFilterActivity.checkBox.setChecked(false);
            listener.quitarProyecto(GLOBAL.PROYECTOS.get(indice));
        }

        projectsFilterActivity.checkBox.setText(GLOBAL.PROYECTOS.get(indice).getNombreProyecto()+"  ");
        projectsFilterActivity.cantidad.setText(""+contarTareas(GLOBAL.PROYECTOS.get(indice).getId_Proyecto()));

        projectsFilterActivity.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(all){
                    listener.apagarAll();
                    all = false;
                }

                if(projectsFilterActivity.checkBox.isChecked()){
                    listener.añadirProyecto(GLOBAL.PROYECTOS.get(indice));
                }else {
                    listener.quitarProyecto(GLOBAL.PROYECTOS.get(indice));
                }
            }
        });

    }
    public void selectAll(){
        all = true;
        notifyDataSetChanged();
    }
    public void quitAll(){
        all = false;
        notifyDataSetChanged();
    }
    public int contarTareas(int id_proy){
        int n =0;

        for (Task task : GLOBAL.TASKS){
            if (task.getId_Proyecto() == id_proy){
                n++;
            }
        }

        return n;
    }

    @Override
    public int getItemCount() {
        return GLOBAL.PROYECTOS.size();
    }

    public class ProjectsFilterActivity extends RecyclerView.ViewHolder {
        LinearLayout container;
        CheckBox checkBox;
        TextView cantidad;
        public ProjectsFilterActivity(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.obj_container);
            checkBox = itemView.findViewById(R.id.checked_obj);
            cantidad = itemView.findViewById(R.id.obj_number);
        }
    }
}
