package com.angel.tasky;

//import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.angel.tasky.Adapters.ImagenTaskInfoAdapter;
import com.angel.tasky.Adapters.SubTaskInfoAdapter;
import com.angel.tasky.GLOBALES.GLOBAL;
import com.angel.tasky.POJOs.Proyecto;
import com.angel.tasky.POJOs.Tag;
import com.angel.tasky.POJOs.TagTask;
import com.angel.tasky.POJOs.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.datatype.Duration;

import static android.widget.Toast.makeText;

public class TaskInfoFragment extends android.support.v4.app.Fragment  {



    public interface TaskInfoFragmentListener{
        void cerrarTaskInfoFragment(boolean x);
        void actualizarTask_info(int pos);
        void actualizarSubTask_info(int pos);
        void borrarTaskInfo(int pos);
        void abrirEditMode(boolean x, int pos);
    }
    TaskInfoFragmentListener listener;
    public int pos;

    DateFormat dateFormat = new SimpleDateFormat("EEE d MMM yyyy");

    ImageButton back;
    Switch activeDeadline, activeTodo;
    TextView openMenu, fechaInicio, fechaDeadline, fechaHacer, fechaDeadlineLetrero, fechaHacerLetrero, progreso, proyecto, tags;
    ImageButton mostarProjectsAndTags, agregarImagen, agregarSubTarea;
    RecyclerView Lista_Proyectos, Lista_Tags, Lista_Imagenes, Lista_SubTareas;
    View SeparadorProjectsAndTags, importancia_TaskInfo;
    EditText tarea;
    CheckBox completeTask;
    RelativeLayout containerProjectsAndTags;
    FloatingActionButton fab;
    FrameLayout detailedImageContainer;
    public String fechaHoy;


    SubTaskInfoAdapter subTaskInfoAdapter;
    ImagenTaskInfoAdapter imagenTaskInfoAdapter;

    DetailedImageFragment detailedImageFragment;

    public Boolean editMode;
    public Boolean detailedImageOpen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = View.inflate(getContext(),R.layout.fragment_task_info,null);

        editMode = false;
        detailedImageOpen = false;

        back = v.findViewById(R.id.button_back_task_info);
        openMenu = v.findViewById(R.id.task_info_menu);
        fechaInicio = v.findViewById(R.id.fecha_inicio_task_info);
        fechaDeadline = v.findViewById(R.id.fecha_deadline_task_info);
        fechaHacer = v.findViewById(R.id.fecha_hacer_task_info);
        fechaDeadlineLetrero = v.findViewById(R.id.fecha_deadline_letrero_task_info);
        fechaHacerLetrero = v.findViewById(R.id.fecha_hacer_letrero_task_info);
        activeDeadline = v.findViewById(R.id.swicth_active_deadline_task_info);
        activeTodo = v.findViewById(R.id.swicth_active_hacer_task_info);
        mostarProjectsAndTags = v.findViewById(R.id.button_show_more_prj_y_tags_task_info);
        agregarImagen = v.findViewById(R.id.button_new_imagen_task_info);
        agregarSubTarea = v.findViewById(R.id.button_new_subtare_task_info);
        Lista_Imagenes = v.findViewById(R.id.LISTA_IMAGENES_TASK_INFO);
        Lista_Proyectos = v.findViewById(R.id.LISTA_PROJECTS_TASK_INFO);
        Lista_SubTareas = v.findViewById(R.id.LISTA_SUB_TAREAS_TASK_INFO);
        Lista_Tags = v.findViewById(R.id.LISTA_TAGS_TASK_INFO);
        SeparadorProjectsAndTags = v.findViewById(R.id.separador_projects_tags_info);
        completeTask = v.findViewById(R.id.checkbox_task_info);
        tarea = v.findViewById(R.id.edit_text_tarea_task_info);
        progreso = v.findViewById(R.id.progress_text_task_info);
        containerProjectsAndTags = v.findViewById(R.id.project_tags_container_task_info);
        proyecto = v.findViewById(R.id.task_info_project);
        tags = v.findViewById(R.id.task_info_tags);
        fab = v.findViewById(R.id.fab_task_info);
        fab.hide();
        importancia_TaskInfo = v.findViewById(R.id.importancia_TaskInfo);
        detailedImageContainer = v.findViewById(R.id.detailed_image_container);


        tarea.setText(GLOBAL.TASKS.get(pos).getTarea());

        proyecto.setText(obtenerProyectoPorId(GLOBAL.TASKS.get(pos).getId_Proyecto()));
        tags.setText(obtenerListaTags(GLOBAL.TASKS.get(pos).getId_Task()));
        mostarProjectsAndTags.setVisibility(View.GONE);

        if(GLOBAL.TASKS.get(pos).getColorEstado() == 1){
            importancia_TaskInfo.setBackground(getContext().getDrawable(R.drawable.ic_barra_azul));
        }
        if(GLOBAL.TASKS.get(pos).getColorEstado() == 2){
            importancia_TaskInfo.setBackground(getContext().getDrawable(R.drawable.ic_barra_verde));
        }
        if(GLOBAL.TASKS.get(pos).getColorEstado() == 3){
            importancia_TaskInfo.setBackground(getContext().getDrawable(R.drawable.ic_barra_roja));
        }
        if(GLOBAL.TASKS.get(pos).getColorEstado() == 4){
            importancia_TaskInfo.setBackground(getContext().getDrawable(R.drawable.ic_barra_naranja));
        }

        fechaInicio.setText(GLOBAL.TASKS.get(pos).getFechaInicio());
        if(GLOBAL.TASKS.get(pos).getActiveFechaToDo() == 1){
            fechaHacerLetrero.setTextColor(getContext().getColor(R.color.colorNegro));
            fechaHacer.setTextColor(getContext().getColor(R.color.colorNegro));

            fechaHacer.setText(GLOBAL.TASKS.get(pos).getFechaToDo());
        }else{
            fechaHacerLetrero.setTextColor(getContext().getColor(R.color.colorSpecialTransparent));
            fechaHacer.setTextColor(getContext().getColor(R.color.colorSpecialTransparent));
            fechaHacerLetrero.setError(null);

        }

        if(GLOBAL.TASKS.get(pos).getActiveFechaDeadline() == 1){
            fechaDeadlineLetrero.setTextColor(getContext().getColor(R.color.colorNegro));
            fechaDeadline.setTextColor(getContext().getColor(R.color.colorNegro));

            fechaDeadline.setText(GLOBAL.TASKS.get(pos).getFechaDeadline());
        }else{
            fechaDeadlineLetrero.setTextColor(getContext().getColor(R.color.colorSpecialTransparent));
            fechaDeadline.setTextColor(getContext().getColor(R.color.colorSpecialTransparent));
            fechaDeadlineLetrero.setError(null);

        }
        LinearLayoutManager linearLayoutManagerDetalles = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false);
        subTaskInfoAdapter = new SubTaskInfoAdapter(getContext(), GLOBAL.TASKS.get(pos).getId_Task(), new SubTaskInfoAdapter.SubTaskInfoAdapterListener() {
            @Override
            public void updateProgress(int checked, int total, int otrapos) {
                GLOBAL.TASKS.get(pos).setProgreso((100/total)*checked);
                progreso.setText((100/total)*checked + "%");
                if(checked == total){
                    GLOBAL.TASKS.get(pos).setProgreso(100);
                    progreso.setText("100%");
                    GLOBAL.TASKS.get(pos).setCompletado(1);
                    GLOBAL.TASKS.get(pos).setColorEstado(2);
                    importancia_TaskInfo.setBackground(getContext().getDrawable(R.drawable.ic_barra_verde));
                }else {
                    GLOBAL.TASKS.get(pos).setCompletado(0);
                    calcularColorEstado(pos,fechaHoy);
                    if(GLOBAL.TASKS.get(pos).getColorEstado() == 1){
                        importancia_TaskInfo.setBackground(getContext().getDrawable(R.drawable.ic_barra_azul));
                    }
                    if(GLOBAL.TASKS.get(pos).getColorEstado() == 3){
                        importancia_TaskInfo.setBackground(getContext().getDrawable(R.drawable.ic_barra_roja));
                    }
                    if(GLOBAL.TASKS.get(pos).getColorEstado() == 4){
                        importancia_TaskInfo.setBackground(getContext().getDrawable(R.drawable.ic_barra_naranja));
                    }
                }
                listener.actualizarTask_info(pos);
                listener.actualizarSubTask_info(otrapos);
            }
        });
        Lista_SubTareas.setAdapter(subTaskInfoAdapter);
        Lista_SubTareas.setLayoutManager(linearLayoutManagerDetalles);

        if(subTaskInfoAdapter.isThereSubTasks()){
            progreso.setVisibility(View.VISIBLE);
            completeTask.setVisibility(View.GONE);
        }else{
            progreso.setVisibility(View.GONE);
            completeTask.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManagerImagenes = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false);
        imagenTaskInfoAdapter = new ImagenTaskInfoAdapter(getContext(), GLOBAL.TASKS.get(pos).getId_Task(), new ImagenTaskInfoAdapter.ImagenTaskInfoAdapterListener() {
            @Override
            public void ExpandirImagen(int pos) {
                detailedImageFragment = new DetailedImageFragment();
                detailedImageFragment.imagePos = pos;

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detailed_image_container,detailedImageFragment,detailedImageFragment.getTag())
                        .commit();
                detailedImageOpen = true;
            }
        });
        Lista_Imagenes.setAdapter(imagenTaskInfoAdapter);
        Lista_Imagenes.setLayoutManager(linearLayoutManagerImagenes);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode){
                    DisableEditMode();
                }else {
                    listener.cerrarTaskInfoFragment(true);
                }
            }
        });

        final PopupMenu popupMenu = new PopupMenu(getContext(),openMenu);
        popupMenu.inflate(R.menu.menu_task_info);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast toast1;
                switch (menuItem.getItemId()){
                    case R.id.menu_task_info_editar:
                        ActiveEditMode();
                        break;
                    case R.id.menu_task_info_eliminar:
                        listener.cerrarTaskInfoFragment(true);
                        listener.borrarTaskInfo(pos);
                        break;
                    case R.id.menu_task_info_copy:
                        CopyToClipBoard();
                        break;
                }
                return false;
            }
        });
        openMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
            }
        });
        progreso.setText(GLOBAL.TASKS.get(pos).getProgreso()+"%");

        if(GLOBAL.TASKS.get(pos).getCompletado() == 1){
            completeTask.setChecked(true);
            progreso.setText("100%");
            ///importancia_TaskInfo.setBackground(getContext().getDrawable(R.drawable.ic_barra_verde));
        }else{
            completeTask.setChecked(false);
            //importancia_TaskInfo.setBackground(getContext().getDrawable(R.drawable.ic_barra_azul));
        }
        completeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(completeTask.isChecked()){
                    GLOBAL.TASKS.get(pos).setCompletado(1);
                    GLOBAL.TASKS.get(pos).setProgreso(100);
                    GLOBAL.TASKS.get(pos).setColorEstado(2);
                    importancia_TaskInfo.setBackground(getContext().getDrawable(R.drawable.ic_barra_verde));
                }else {
                    GLOBAL.TASKS.get(pos).setCompletado(0);
                    GLOBAL.TASKS.get(pos).setProgreso(0);
                    calcularColorEstado(pos,fechaHoy);
                    if(GLOBAL.TASKS.get(pos).getColorEstado() == 1){
                        importancia_TaskInfo.setBackground(getContext().getDrawable(R.drawable.ic_barra_azul));
                    }
                    if(GLOBAL.TASKS.get(pos).getColorEstado() == 3){
                        importancia_TaskInfo.setBackground(getContext().getDrawable(R.drawable.ic_barra_roja));
                    }
                    if(GLOBAL.TASKS.get(pos).getColorEstado() == 4){
                        importancia_TaskInfo.setBackground(getContext().getDrawable(R.drawable.ic_barra_naranja));
                    }
                }
                listener.actualizarTask_info(pos);
            }
        });


        return v;
    }

    public void CopyToClipBoard() {
        String textTarea="";
        ClipboardManager clipboardManager =  (ClipboardManager) getContext().getSystemService(getContext().CLIPBOARD_SERVICE);

        textTarea = textTarea + proyecto.getText().toString()+": "+ tags.getText().toString()+"\n";
        textTarea = textTarea + "Para: "+ fechaDeadline.getText().toString()+"\n\n";
        textTarea = textTarea + tarea.getText().toString()+"\n";
        textTarea = textTarea + subTaskInfoAdapter.textDetalles();



        ClipData clip = ClipData.newPlainText("Tarea copiada!",textTarea);


        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        View view = View.inflate(getContext(), R.layout.toas_copiado_al_portapapeles,null);
        toast.setView(view);
        toast.show();

        clipboardManager.setPrimaryClip(clip);
    }

    private void ActiveEditMode() {
        GLOBAL.TASK_TEMP.copyTask(GLOBAL.TASKS.get(pos));
        listener.abrirEditMode(true, pos);
    }

    public void DisableEditMode(){
        editMode = false;
        fab.hide();
        activeDeadline.setVisibility(View.GONE);
        activeTodo.setVisibility(View.GONE);
        openMenu.setVisibility(View.VISIBLE);


        if(subTaskInfoAdapter.isThereSubTasks()){
            progreso.setVisibility(View.VISIBLE);
            completeTask.setVisibility(View.GONE);
        }else{
            progreso.setVisibility(View.GONE);
            completeTask.setVisibility(View.VISIBLE);
        }

        tarea.setClickable(false);
        tarea.setFocusable(false);
        tarea.setFocusableInTouchMode(false);

        tarea.setText(GLOBAL.TASKS.get(pos).getTarea());

        agregarImagen.setVisibility(View.GONE);
        agregarSubTarea.setVisibility(View.GONE);


    }

    public String obtenerListaTags(int id){
        String cadena = "";
        for (TagTask tagTask : GLOBAL.TAG_TASKS){
            if(tagTask.getId_Task() == id){
                cadena = cadena + " #"+obtenerTagPorId(tagTask.getId_Tag());
            }
        }

        return cadena;
    }
    public String obtenerTagPorId(int id){
        for(Tag tag : GLOBAL.TAGS){
            if(tag.getId_Tag() == id){
                return tag.getNombreTag();
            }
        }
        return "";
    }
    public String obtenerProyectoPorId(int id){
        for(Proyecto proyecto : GLOBAL.PROYECTOS){
            if(proyecto.getId_Proyecto() == id){
                return proyecto.getNombreProyecto();
            }
        }
        return "";
    }

    public int calcularColorEstado(int pos, String hoy){
        Date deadlineDate, toDoDate, hoyDate;

        deadlineDate = new Date();
        toDoDate = new Date();
        hoyDate = new Date();

        try {
            hoyDate = dateFormat.parse(hoy);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            toDoDate  =dateFormat.parse(GLOBAL.TASKS.get(pos).getFechaToDo());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            deadlineDate = dateFormat.parse(GLOBAL.TASKS.get(pos).getFechaDeadline());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(GLOBAL.TASKS.get(pos).getActiveFechaToDo() == 1 && GLOBAL.TASKS.get(pos).getActiveFechaDeadline() == 1){

            if(GLOBAL.TASKS.get(pos).getFechaToDo().equals(hoy) || GLOBAL.TASKS.get(pos).getFechaDeadline().equals(hoy)){
                GLOBAL.TASKS.get(pos).setColorEstado(4);
                return 4;
            }else
            if(deadlineDate.before(hoyDate)){
                GLOBAL.TASKS.get(pos).setColorEstado(3);
                return 3;
            }else{
                GLOBAL.TASKS.get(pos).setColorEstado(1);
                return 1;
            }
        } else if(GLOBAL.TASKS.get(pos).getActiveFechaToDo() == 0 && GLOBAL.TASKS.get(pos).getActiveFechaDeadline() == 1){

            if( GLOBAL.TASKS.get(pos).getFechaDeadline().equals(hoy)){
                GLOBAL.TASKS.get(pos).setColorEstado(4);
                return 4;
            }else
            if(deadlineDate.before(hoyDate)){
                GLOBAL.TASKS.get(pos).setColorEstado(3);
                return 3;
            }else {
                GLOBAL.TASKS.get(pos).setColorEstado(1);
                return 1;
            }
        }else if(GLOBAL.TASKS.get(pos).getActiveFechaToDo() == 1 && GLOBAL.TASKS.get(pos).getActiveFechaDeadline() == 0){

            if(GLOBAL.TASKS.get(pos).getFechaToDo().equals(hoy) ){
                GLOBAL.TASKS.get(pos).setColorEstado(4);
                return 4;
            }else
            if(toDoDate.before(hoyDate)){
                GLOBAL.TASKS.get(pos).setColorEstado(3);
                return 3;
            }else {
                GLOBAL.TASKS.get(pos).setColorEstado(1);
                return 1;
            }
        }else{
            GLOBAL.TASKS.get(pos).setColorEstado(1);
            return 1;
        }
    }

    public void CerrarDetailedImage(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .remove(detailedImageFragment)
                .commit();

        detailedImageOpen = false;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TaskInfoFragmentListener) {
            listener = (TaskInfoFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Fragment info task Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
