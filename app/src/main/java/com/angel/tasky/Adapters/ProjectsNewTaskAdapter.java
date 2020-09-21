package com.angel.tasky.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.angel.tasky.GLOBALES.GLOBAL;
import com.angel.tasky.POJOs.Proyecto;
import com.angel.tasky.R;

public class ProjectsNewTaskAdapter extends RecyclerView.Adapter<ProjectsNewTaskAdapter.ProjectsNewTaskActivity> {
    Context context;
    public interface ProjectsNewTaskAdapterListener{
        Proyecto RetornarProyecto(Proyecto selectedProject);
    }
    ProjectsNewTaskAdapterListener listener;

    public ProjectsNewTaskAdapter(Context context, ProjectsNewTaskAdapterListener listener){
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProjectsNewTaskActivity onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context,R.layout.item_project_1, null);
        ProjectsNewTaskActivity projectsNewTaskActivity = new ProjectsNewTaskActivity(view);
        return projectsNewTaskActivity;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProjectsNewTaskActivity projectsNewTaskActivity, int i) {
        final int indice = i;
        projectsNewTaskActivity.cant.setVisibility(View.GONE);
        projectsNewTaskActivity.project.setText(GLOBAL.PROYECTOS.get(indice).getNombreProyecto());

        if(GLOBAL.INDICADOR.getPos_selected_proy() != indice){
            projectsNewTaskActivity.selected.setChecked(false);
        }
        if(GLOBAL.INDICADOR.getPos_selected_proy() == indice){
            projectsNewTaskActivity.selected.setChecked(true);
        }
        projectsNewTaskActivity.selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GLOBAL.INDICADOR.setPos_selected_proy(indice);
                listener.RetornarProyecto(GLOBAL.PROYECTOS.get(indice));
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return GLOBAL.PROYECTOS.size();
    }

    public class ProjectsNewTaskActivity extends RecyclerView.ViewHolder {
        TextView project, cant;
        RadioButton selected;
        public ProjectsNewTaskActivity(@NonNull View itemView) {
            super(itemView);
            project = itemView.findViewById(R.id.project_1);
            cant = itemView.findViewById(R.id.project_1_number);
            selected = itemView.findViewById(R.id.selected_project_1);
        }
    }
}
