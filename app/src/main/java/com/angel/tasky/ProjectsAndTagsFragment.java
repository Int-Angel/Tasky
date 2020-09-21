package com.angel.tasky;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.angel.tasky.Adapters.ProjectsAndTagsAdapter;
import com.angel.tasky.GLOBALES.GLOBAL;
import com.angel.tasky.POJOs.Proyecto;
import com.angel.tasky.POJOs.Tag;

public class ProjectsAndTagsFragment extends Fragment  {



    public interface ProjectsAndTagsFragmentListener{
        void cerrarProjectsAndTagsListener(boolean x);
        void abrirDialog();
        void abrirDialogTag(int i);
        void abrirDialogEliminarProject(int i);
        void abrirDialogEliminarTag(int i);
        void actualizarListaProjectsAndTags();
    }

    ProjectsAndTagsFragmentListener listener;
    public  ProjectsAndTagsAdapter projectsAndTagsAdapter;

    ImageButton back;
    RecyclerView listaProjects;
    FloatingActionButton fab;

    TASKDBCONTROLLER taskdbcontroller;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_projects_and_tags,container,false);

       taskdbcontroller = new TASKDBCONTROLLER(getContext());

       back = v.findViewById(R.id.button_back_proyects_and_tags);
       listaProjects = v.findViewById(R.id.LISTA_PROJECTS_PROJECTS_AND_TAGS);
       fab = v.findViewById(R.id.fab_proyects_and_tags);


       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               listener.cerrarProjectsAndTagsListener(true);
           }
       });

       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               AbrirDialogMain();
           }
       });


        Animation aparecerFab = AnimationUtils.loadAnimation(getContext(),R.anim.appear_fab_2);
        fab.startAnimation(aparecerFab);

       projectsAndTagsAdapter = new ProjectsAndTagsAdapter(getContext(), new ProjectsAndTagsAdapter.ProjectsAndTagsAdapterListener() {
           @Override
           public void EliminarProyecto(int i) {
               //taskdbcontroller.eliminarProyecto(GLOBAL.PROYECTOS.get(i));
               //refrescarListaDeProyectosyTags();
               listener.abrirDialogEliminarProject(i);
           }

           @Override
           public void EliminarTag(int i, int j) {
               //taskdbcontroller.eliminarTag(GLOBAL.TAGS.get(j));
               //refrescarListaDeProyectosyTags();
               listener.abrirDialogEliminarTag(j);
           }

           @Override
           public void AgregarTag(int i) {
               listener.abrirDialogTag(i);
           }

           @Override
           public void ActualizarProject(int i) {
               taskdbcontroller.actualizarProyecto(GLOBAL.PROYECTOS.get(i));
               refrescarListaDeProyectosyTags();
               listener.actualizarListaProjectsAndTags();
           }

           @Override
           public void GuardarTag(int i) {
               taskdbcontroller.actualizarTag(GLOBAL.TAGS.get(i));
               refrescarListaDeProyectosyTags();
               listener.actualizarListaProjectsAndTags();
           }

       });
       LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

       listaProjects.setAdapter(projectsAndTagsAdapter);
       listaProjects.setLayoutManager(linearLayoutManager);

      // refrescarListaDeProyectosyTags();
        return v;
    }



    private void AbrirDialogMain(){
        listener.abrirDialog();
    }

   /* public void AgregarNuevoProyecto(String nuevoProyecto) {
        Proyecto newProject = new Proyecto(nuevoProyecto);
        taskdbcontroller.newProyecto(newProject);
        RefrescarProjects();
    }

    public void AgregarNuevoTag(String nuevoTag, int id) {
        int verdaderoId;
        verdaderoId = GLOBAL.PROYECTOS.get(id).getId_Proyecto();
        Tag newTag = new Tag(verdaderoId,nuevoTag);
        taskdbcontroller.newTag(newTag);
        RefrescarTags();
       //projectsAndTagsAdapter.RefrescarTags();
    }
    public void RefrescarProjects(){
        if(projectsAndTagsAdapter == null) return;
        taskdbcontroller.obtenerProyectos();
        projectsAndTagsAdapter.notifyDataSetChanged();
    }

    public void RefrescarTags(){
        taskdbcontroller.obtenerTags();
    }*/


    public void refrescarListaDeProyectosyTags() {
        if(projectsAndTagsAdapter == null) return;
        taskdbcontroller.obtenerProyectos();
        taskdbcontroller.obtenerTags();
        projectsAndTagsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ProjectsAndTagsFragmentListener){
            listener = (ProjectsAndTagsFragmentListener) context;

        }else {
            throw new RuntimeException(context.toString()
                    + "must implement ProjectsAndTagsListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
