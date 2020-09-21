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
import android.widget.ImageButton;

import com.angel.tasky.Adapters.TaskAdapter;

public class TodasLasTareasFragment extends Fragment {

    public interface TodasLasTareasListener{
        void CerrarTodasLasTareas(boolean x);
    }

    TodasLasTareasListener listener;
    RecyclerView Lista;
    ImageButton back;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_todas_las_tareas, container, false);

        Lista = v.findViewById(R.id.LISTA_TODAS_LAS_TAREAS);
        back = v.findViewById(R.id.button_back_todas_las_tareas);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.CerrarTodasLasTareas(true);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        TaskAdapter taskAdapter = new TaskAdapter(getContext(), new TaskAdapter.TaskAdapterListener() {
            @Override
            public void elementoEliminado(int pos) {

            }

            @Override
            public void abrirInfo(int pos) {

            }

            @Override
            public void actualizarElemento(int pos) {

            }

            @Override
            public void actualizarElementoSecundario(int pos) {

            }
        });

        Lista.setAdapter(taskAdapter);
        Lista.setLayoutManager(linearLayoutManager);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof TodasLasTareasListener){
            listener = (TodasLasTareasListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
