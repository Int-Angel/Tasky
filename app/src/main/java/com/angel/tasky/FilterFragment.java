package com.angel.tasky;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.angel.tasky.Adapters.ProjectsFilterAdapter;
import com.angel.tasky.Adapters.SelectedTagsAdapter;
import com.angel.tasky.Adapters.TagsFilterAdapter;
import com.angel.tasky.Adapters.TagsFilterAdapterPro;
import com.angel.tasky.GLOBALES.GLOBAL;
import com.angel.tasky.POJOs.Proyecto;
import com.angel.tasky.POJOs.Tag;

public class FilterFragment extends Fragment {

    ImageButton back;
    Button apply;
    LinearLayout background;

    RecyclerView selectedTags, projects, tags;

    public interface FilterFragmentListener{
        void cerrarFilterFragment(boolean x);
        void AplicarFiltro();
    }

    FilterFragmentListener listener;

    ProjectsFilterAdapter projectsFilterAdapter;
    TagsFilterAdapterPro tagsFilterAdapter;
    SelectedTagsAdapter selectedTagsAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = View.inflate(getContext(),R.layout.fragment_filter,null);

        back = v.findViewById(R.id.button_back_filter);
        apply = v.findViewById(R.id.apply_filters);
        selectedTags = v.findViewById(R.id.LISTA_SELECTED_TAGS_FILTER);
        projects = v.findViewById(R.id.LISTA_PROJECTS_FILTER);
        tags = v.findViewById(R.id.LISTA_TAGS_FILTER);
        background = v.findViewById(R.id.filters_background);

        LinearLayoutManager layoutManagerProjects, layoutManagerTags, layoutManagerSelectedTags;

        layoutManagerProjects = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        layoutManagerTags = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManagerSelectedTags = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);


        projectsFilterAdapter = new ProjectsFilterAdapter(getContext(), new ProjectsFilterAdapter.ProjectsFilterAdapterListener() {
            @Override
            public void añadirProyecto(Proyecto x) {
                GLOBAL.SELECTED_PROJECTS.add(x);
                tagsFilterAdapter.changeSelectedProjects(GLOBAL.SELECTED_PROJECTS);
            }

            @Override
            public void quitarProyecto(Proyecto x) {
                GLOBAL.SELECTED_PROJECTS.remove(x);
                tagsFilterAdapter.changeSelectedProjects(GLOBAL.SELECTED_PROJECTS);
            }

            @Override
            public void apagarAll() {

            }
        });

        tagsFilterAdapter = new TagsFilterAdapterPro(getContext(), GLOBAL.SELECTED_PROJECTS, new TagsFilterAdapterPro.TagsFilterAdapterProListener() {
            @Override
            public void apagarAll() {

            }

            @Override
            public void añadirTag(Tag x) {
                GLOBAL.SELECTED_TAGS.remove(x);
                GLOBAL.SELECTED_TAGS.add(x);
                selectedTagsAdapter.notifyDataSetChanged();
            }

            @Override
            public void quitarTag(Tag x) {

            }
        });

        selectedTagsAdapter = new SelectedTagsAdapter(getContext(), new SelectedTagsAdapter.SelectedTagsAdapterListener() {
            @Override
            public void QuitarSelectedTag(Tag x) {
                GLOBAL.SELECTED_TAGS.remove(x);
                selectedTagsAdapter.notifyDataSetChanged();
            }
        });

        projects.setAdapter(projectsFilterAdapter);
        projects.setLayoutManager(layoutManagerProjects);

        tags.setAdapter(tagsFilterAdapter);
        tags.setLayoutManager(layoutManagerTags);

        selectedTags.setAdapter(selectedTagsAdapter);
        selectedTags.setLayoutManager(layoutManagerSelectedTags);

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.cerrarFilterFragment(true);
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AplicarFiltros();
            }
        });

        return v;
    }

    private void AplicarFiltros() {

        listener.AplicarFiltro();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


            listener = (FilterFragmentListener) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
