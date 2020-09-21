package com.angel.tasky;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.angel.tasky.Adapters.ImagenNewTaskAdapter;
import com.angel.tasky.Adapters.ProjectsNewTaskAdapter;
import com.angel.tasky.Adapters.SubTaskNewTaskAdapter;
import com.angel.tasky.Adapters.TagsFilterAdapter;
import com.angel.tasky.Adapters.TagsNewTaskAdapter;
import com.angel.tasky.GLOBALES.GLOBAL;
import com.angel.tasky.POJOs.Imagen;
import com.angel.tasky.POJOs.Proyecto;
import com.angel.tasky.POJOs.SubTask;
import com.angel.tasky.POJOs.Tag;
import com.angel.tasky.POJOs.TagTask;
import com.angel.tasky.POJOs.Task;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.content.Context.VIBRATOR_SERVICE;

public class NewTaskFragment extends Fragment {

    TextView fechaInicio, fechaDeadline, fechaHacer, textofinal, textoDeadline, textoHacer, textoInicio;
    String dateInicio, dateDeadline, dateHacer;
    Switch activeDeadline, activeHacer;
    ImageButton showProjectsAndTags, addSubtarea, addImagen, back;
    RecyclerView listaProjects, listaTags, listaSubtareas, listaImagenes;
    EditText tarea;
    FloatingActionButton fab;
    RelativeLayout cortina;
    View separador;
    Boolean mostarMas;
    RadioButton project_vacio;



    TASKDBCONTROLLER taskdbcontroller;

    ProjectsNewTaskAdapter projectsNewTaskAdapter;
    //TagsNewTaskAdapter tagsNewTaskAdapter;
    TagsFilterAdapter tagsNewTaskAdapter;
    SubTaskNewTaskAdapter subTaskNewTaskAdapter;
    ImagenNewTaskAdapter imagenNewTaskAdapter;

    Calendar c;
    DatePickerDialog datePickerDialog;

    DateFormat dateFormat = new SimpleDateFormat("EEE d MMM yyyy");

    ArrayList<Tag> tagsSeleccionados;

    public boolean editMode;
    public boolean restoreData;
    public int editPos;

    public interface NewTaskFragmentListener{
       void cerrarNewTaskFragment(boolean x);
    }


    NewTaskFragmentListener listener;
    private int GALLERY = 1, CAMERA = 2;

    ArrayList<Proyecto> selectedProyecto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_task, container, false);


        taskdbcontroller = new TASKDBCONTROLLER(getContext());

        selectedProyecto = new ArrayList<>();
        tagsSeleccionados = new ArrayList<>();

        final Animation rotate = AnimationUtils.loadAnimation(getContext(),R.anim.button_rotation_up);
        mostarMas = true;
        separador = v.findViewById(R.id.separador_projects_tags);
        fechaInicio = v.findViewById(R.id.fecha_inicio_new_task);
        fechaDeadline = v.findViewById(R.id.fecha_deadline_new_task);
        fechaHacer =  v.findViewById(R.id.fecha_hacer_new_task);
        textoDeadline = v.findViewById(R.id.textDeadlineInfo);
        textoHacer = v.findViewById(R.id.textHacerInfo);
        textoInicio = v.findViewById(R.id.textInicioInfo);
        project_vacio = v.findViewById(R.id.project_vacio);

        activeDeadline = v.findViewById(R.id.swicth_active_deadline_new_task);
        activeHacer = v.findViewById(R.id.swicth_active_hacer_new_task);

        showProjectsAndTags = v.findViewById(R.id.button_show_more_prj_y_tags_new_task);
        addImagen = v.findViewById(R.id.button_new_imagen_new_task);
        addSubtarea = v.findViewById(R.id.button_new_subtare_new_task);
        back = v.findViewById(R.id.button_back_new_task);
        fab = v.findViewById(R.id.fab_new_task);
        cortina = v.findViewById(R.id.new_task_cortina);
        textofinal = v.findViewById(R.id.texto_mensaje_final);

        listaImagenes = v.findViewById(R.id.LISTA_IMAGENES_NEW_TASK);
        listaProjects = v.findViewById(R.id.LISTA_PROJECTS_NEW_TASK);
        listaTags = v.findViewById(R.id.LISTA_TAGS_NEW_TASK);
        listaSubtareas = v.findViewById(R.id.LISTA_SUB_TAREAS_NEW_TASK);

        tarea = v.findViewById(R.id.edit_text_tarea_new_task);

        final DateFormat df = new SimpleDateFormat("EEE d MMM yyyy");
        dateInicio = df.format(Calendar.getInstance().getTime());
        dateDeadline = df.format(Calendar.getInstance().getTime());
        dateHacer = df.format(Calendar.getInstance().getTime());


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              listener.cerrarNewTaskFragment(true);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editMode){
                    Guardar();
                }else {
                    Actualizar();
                }
            }
        });

        Animation aparecerFab = AnimationUtils.loadAnimation(getContext(),R.anim.appear_fab_2);
        fab.startAnimation(aparecerFab);

        fechaInicio.setText(dateInicio);

        activeDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeDeadline.isChecked()){
                    textoDeadline.setTextColor(getContext().getColor(R.color.colorNegro));
                    fechaDeadline.setTextColor(getContext().getColor(R.color.colorNegro));
                    RevisarFechas();
                    fechaDeadline.setText(dateDeadline);
                }else{
                    textoDeadline.setTextColor(getContext().getColor(R.color.colorSpecialTransparent));
                    fechaDeadline.setTextColor(getContext().getColor(R.color.colorSpecialTransparent));
                    textoDeadline.setError(null);
                }
            }
        });

        activeHacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeHacer.isChecked()){
                    textoHacer.setTextColor(getContext().getColor(R.color.colorNegro));
                    fechaHacer.setTextColor(getContext().getColor(R.color.colorNegro));
                    RevisarFechas();
                    fechaHacer.setText(dateHacer);
                }else{
                    textoHacer.setTextColor(getContext().getColor(R.color.colorSpecialTransparent));
                    fechaHacer.setTextColor(getContext().getColor(R.color.colorSpecialTransparent));
                    textoHacer.setError(null);

                }
            }
        });

        fechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();

                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year= c.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                        dateInicio = df.format(calendar.getTime());
                        fechaInicio.setText(dateInicio);
                        RevisarFechas();
                    }
                },year,month,day);

                datePickerDialog.show();
            }
        });


        fechaDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeDeadline.isChecked()){
                    c = Calendar.getInstance();

                    int day = c.get(Calendar.DAY_OF_MONTH);
                    int month = c.get(Calendar.MONTH);
                    int year= c.get(Calendar.YEAR);

                    datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.YEAR,year);
                            calendar.set(Calendar.MONTH,month);
                            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                            dateDeadline = df.format(calendar.getTime());
                            fechaDeadline.setText(dateDeadline);
                            RevisarFechas();
                        }
                    },year,month,day);

                    datePickerDialog.show();
                }
            }
        });
        fechaHacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeHacer.isChecked()){
                    c = Calendar.getInstance();


                    int day = c.get(Calendar.DAY_OF_MONTH);
                    int month = c.get(Calendar.MONTH);
                    int year= c.get(Calendar.YEAR);

                    datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.YEAR,year);
                            calendar.set(Calendar.MONTH,month);
                            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                            dateHacer = df.format(calendar.getTime());
                            fechaHacer.setText(dateHacer);
                            RevisarFechas();
                        }
                    },year,month,day);

                    datePickerDialog.show();
                }
            }
        });

        showProjectsAndTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mostarMas){
                    mostarMas = false;
                    showProjectsAndTags.startAnimation(rotate);
                    showProjectsAndTags.setImageDrawable(getContext().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
                    listaProjects.setVisibility(View.GONE);
                    listaTags.setVisibility(View.GONE);
                    project_vacio.setVisibility(View.GONE);
                    //separador.setVisibility(View.GONE);
                }else{
                    mostarMas = true;
                    showProjectsAndTags.startAnimation(rotate);
                    showProjectsAndTags.setImageDrawable(getContext().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
                    listaProjects.setVisibility(View.VISIBLE);
                    listaTags.setVisibility(View.VISIBLE);
                    project_vacio.setVisibility(View.VISIBLE);
                    //separador.setVisibility(View.VISIBLE);
                }
            }
        });

        GLOBAL.INDICADOR.setPos_selected_proy(-1);

        LinearLayoutManager linearLayoutManagerProjects = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false);
        LinearLayoutManager linearLayoutManagerImagenes = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManagerSubTasks = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false);

        final LinearLayoutManager linearLayoutManagerTags = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false);

        project_vacio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GLOBAL.INDICADOR.setPos_selected_proy(-1);
                projectsNewTaskAdapter.notifyDataSetChanged();


               /* tagsNewTaskAdapter = new TagsNewTaskAdapter(getContext(), -1, new TagsNewTaskAdapter.TagsNewTaskAdapterListener() {
                    @Override
                    public ArrayList<Tag> ReturnSelectedTags(ArrayList<Tag> _selectedTags) {
                        tagsSeleccionados.clear();
                        return null;
                    }
                });*/
                GLOBAL.TAGS_TEMP.clear();
               selectedProyecto.clear();
               tagsNewTaskAdapter.changeSelectedProjects(selectedProyecto);


            }
        });

        projectsNewTaskAdapter = new ProjectsNewTaskAdapter(getContext(), new ProjectsNewTaskAdapter.ProjectsNewTaskAdapterListener() {
            @Override
            public Proyecto RetornarProyecto(Proyecto selectedProject) {

                project_vacio.setChecked(false);
                GLOBAL.PROYECTO_TEMP.setId_Proyecto(selectedProject.getId_Proyecto());
                GLOBAL.PROYECTO_TEMP.setNombreProyecto(selectedProject.getNombreProyecto());

                selectedProyecto.clear();
                selectedProyecto.add(selectedProject);

                GLOBAL.TAGS_TEMP.clear();

                tagsNewTaskAdapter.changeSelectedProjects(selectedProyecto);

               //tagsNewTaskAdapter.id_proy = selectedProject.getId_Proyecto();
               //tagsNewTaskAdapter.notifyDataSetChanged();



                return null;
            }
        });

        listaProjects.setAdapter(projectsNewTaskAdapter);
        listaProjects.setLayoutManager(linearLayoutManagerProjects);

       /* tagsNewTaskAdapter = new TagsNewTaskAdapter(getContext(), -1, new TagsNewTaskAdapter.TagsNewTaskAdapterListener() {
            @Override
            public ArrayList<Tag> ReturnSelectedTags(ArrayList<Tag> _selectedTags) {
                tagsSeleccionados.clear();
                tagsSeleccionados.addAll(_selectedTags);
                GLOBAL.TAGS_TEMP.clear();
                GLOBAL.TAGS_TEMP.addAll(_selectedTags);
                return null;
            }
        });*/

       tagsNewTaskAdapter = new TagsFilterAdapter(getContext(), selectedProyecto, new TagsFilterAdapter.TagsFilterAdapterListener() {
           @Override
           public void apagarAll() {

           }

           @Override
           public void añadirTag(Tag x) {
               GLOBAL.TAGS_TEMP.add(x);
           }

           @Override
           public void quitarTag(Tag x) {
               GLOBAL.TAGS_TEMP.remove(x);
           }
       });

        listaTags.setAdapter(tagsNewTaskAdapter);
        listaTags.setLayoutManager(linearLayoutManagerTags);
        tagsNewTaskAdapter.notifyDataSetChanged();


        subTaskNewTaskAdapter = new SubTaskNewTaskAdapter(getContext());
        listaSubtareas.setLayoutManager(linearLayoutManagerSubTasks);
        listaSubtareas.setAdapter(subTaskNewTaskAdapter);

        addSubtarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = (int) taskdbcontroller.getNextAutoIncrementTaskTable();
                SubTask x;
                if(editMode){
                    x = new SubTask(null,0,GLOBAL.TASKS.get(editPos).getId_Task());
                }else{
                    x = new SubTask(null,0,a);
                }
                GLOBAL.SUB_TASKS_TEMP.add(x);
                subTaskNewTaskAdapter.notifyDataSetChanged();
            }
        });

        imagenNewTaskAdapter = new ImagenNewTaskAdapter(getContext());


        listaImagenes.setAdapter(imagenNewTaskAdapter);
        listaImagenes.setLayoutManager(linearLayoutManagerImagenes);

        addImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

        if(editMode || restoreData){
            CargarDatos();
        }

        return v;
    }

    private void CargarDatos() {

        tarea.setText(GLOBAL.TASK_TEMP.getTarea());
        fechaInicio.setText(GLOBAL.TASK_TEMP.getFechaInicio());
        if(GLOBAL.TASK_TEMP.getActiveFechaDeadline() == 1){
            activeDeadline.setChecked(true);
            textoDeadline.setTextColor(getContext().getColor(R.color.colorNegro));
            fechaDeadline.setTextColor(getContext().getColor(R.color.colorNegro));
            fechaDeadline.setText(GLOBAL.TASK_TEMP.getFechaDeadline());
        }else{
            activeDeadline.setChecked(false);
            textoDeadline.setTextColor(getContext().getColor(R.color.colorSpecialTransparent));
            fechaDeadline.setTextColor(getContext().getColor(R.color.colorSpecialTransparent));
        }

        if(GLOBAL.TASK_TEMP.getActiveFechaToDo() == 1){
            activeHacer.setChecked(true);
            textoHacer.setTextColor(getContext().getColor(R.color.colorNegro));
            fechaHacer.setTextColor(getContext().getColor(R.color.colorNegro));
            fechaHacer.setText(GLOBAL.TASK_TEMP.getFechaToDo());
        }else{
            activeHacer.setChecked(false);
            textoHacer.setTextColor(getContext().getColor(R.color.colorSpecialTransparent));
            fechaHacer.setTextColor(getContext().getColor(R.color.colorSpecialTransparent));
        }

        obtenerProyectoPorIdyPonerEnTem(GLOBAL.TASK_TEMP.getId_Proyecto());
        obtenerTagsyPonerEnTemp(GLOBAL.TASK_TEMP.getId_Task());
        obtenerSubtaskyPonerEnTemp(GLOBAL.TASK_TEMP.getId_Task());
        obtenerImagenesyPonerEnTemp(GLOBAL.TASK_TEMP.getId_Task());

        GLOBAL.INDICADOR.setPos_selected_proy(obtenerPosicionProyecto(GLOBAL.TASK_TEMP.getId_Proyecto()));

        if(GLOBAL.INDICADOR.getPos_selected_proy()>=0){
            project_vacio.setChecked(false);
        }

        selectedProyecto.clear();
        selectedProyecto.add(GLOBAL.PROYECTO_TEMP);
        tagsNewTaskAdapter.changeSelectedProjects(selectedProyecto);
        tagsNewTaskAdapter.editMode = true;
        tagsNewTaskAdapter.notifyDataSetChanged();


        projectsNewTaskAdapter.notifyDataSetChanged();
        subTaskNewTaskAdapter.notifyDataSetChanged();
        imagenNewTaskAdapter.notifyDataSetChanged();

    }
    public void obtenerProyectoPorIdyPonerEnTem(int id){

        for(Proyecto proyecto : GLOBAL.PROYECTOS){
            if(proyecto.getId_Proyecto() == id){
                GLOBAL.PROYECTO_TEMP.setId_Proyecto(proyecto.getId_Proyecto());
                GLOBAL.PROYECTO_TEMP.setNombreProyecto(proyecto.getNombreProyecto());
                return;
            }
        }

    }
    public void obtenerTagsyPonerEnTemp(int id){
        for (TagTask tagTask : GLOBAL.TAG_TASKS){
            if(tagTask.getId_Task() == id){
                GLOBAL.TAGS_TEMP.add(obtenerTagPorId(tagTask.getId_Tag()));
            }
        }
    }
    public Tag obtenerTagPorId(int idTag){
        for(Tag tag: GLOBAL.TAGS){
            if(tag.getId_Tag() == idTag){
                return tag;
            }
        }
        return null;
    }
    public void obtenerSubtaskyPonerEnTemp(int id){
        for(SubTask subTask : GLOBAL.SUB_TASKS){
            if(subTask.getId_Task() == id){
                GLOBAL.SUB_TASKS_TEMP.add(subTask);
            }
        }
    }

    public void obtenerImagenesyPonerEnTemp(int id){
        for (Imagen imagen : GLOBAL.IMAGENS){
            if(imagen.getId_Task() == id){
                GLOBAL.IMAGENS_TEMP.add(imagen);
            }
        }
    }
    public int obtenerPosicionProyecto(int idProy){
        int n =0;
        for(Proyecto proyecto : GLOBAL.PROYECTOS){
            if(proyecto.getId_Proyecto() == idProy){

                return n;
            }
            n++;
        }
        return -1;
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getContext());
        pictureDialog.setTitle("Añadir imagen");
        /*String[] pictureDialogItems = {
                "De la galeria",
                "Tomar una foto" };*/
        String[] pictureDialogItems = {
                "De la galeria"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                if(checkPermission()){
                                    takePhotoFromCamera();
                                }
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Imagen x;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY)

        {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), contentURI);
                    //imageView.setImageBitmap(bitmap);




                    String realPath = ImageFilePath.getPath(getContext(),contentURI);
                    File imgprueba = new File(realPath);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;



                    if(editMode){
                        x = new Imagen(bitmap,GLOBAL.TASKS.get(editPos).getId_Task());
                        x.setPath(realPath);
                    }else {
                        x = new Imagen(bitmap,(int)taskdbcontroller.getNextAutoIncrementTaskTable());
                        x.setPath(realPath);
                    }
                    //Toast.makeText(getContext(),realPath,Toast.LENGTH_LONG);


                    GLOBAL.IMAGENS_TEMP.add(x);
                    imagenNewTaskAdapter.notifyDataSetChanged();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            Uri uri = (Uri) data.getExtras().get("data");
            //imageView.setImageBitmap(thumbnail);
            String realPath = ImageFilePath.getPath(getContext(),uri);
            x = new Imagen(thumbnail,(int)taskdbcontroller.getNextAutoIncrementTaskTable());
            //x.setPath(realPath);
            tarea.setText(realPath);
            GLOBAL.IMAGENS_TEMP.add(x);
            imagenNewTaskAdapter.notifyDataSetChanged();

        }
    }

    private void RevisarFechas() {

        Date InicioFecha, DeadlineFecha, HacerFecha;

        InicioFecha = new Date();
        DeadlineFecha = new Date();
        HacerFecha = new Date();

        try {
            InicioFecha = dateFormat.parse(dateInicio);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(activeDeadline.isChecked()){
            try {
                DeadlineFecha = dateFormat.parse(dateDeadline);

                if(InicioFecha.after(DeadlineFecha)){
                    textoDeadline.setError("No puede ser \n antes del Inicio");
                }else{
                    textoDeadline.setError(null);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if(activeHacer.isChecked()){
            try {
                HacerFecha = dateFormat.parse(dateHacer);

                if(activeDeadline.isChecked()){
                    if(HacerFecha.after(DeadlineFecha) || HacerFecha.before(InicioFecha)){
                        textoHacer.setError("Tiene que estar entre fecha\n de Inicio y Deadline");
                    }else{
                        textoHacer.setError(null);
                    }
                }else{
                    if(HacerFecha.before(InicioFecha)){
                        textoHacer.setError("Ya se te paso \n la fecha de hacer!");
                    }else{
                        textoHacer.setError(null);
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }


    private void Guardar() {
        int ColorEstado, Progreso, Completado, Id_Proyecto, ActiveFechaDeadline, ActiveFechaToDo;
        String Tarea, FechaInicio, FechaDeadline, FechaToDo, FechaHoy;
        Task nuevaTask;
        TagTask tagTask;
        Date HOY = new Date(), HACER = new Date(), DEADLINE = new Date();

        FechaHoy = dateFormat.format(c.getInstance().getTime());

        try {
            HOY = dateFormat.parse(FechaHoy);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // false -> 0   true -> 1
        ColorEstado = 1; // Azul ->1 = progreso                    Verde ->2 = Completado                 Rojo -> 3 = URGENTE fecha pasada                    Naranja-> 4 = dia de hacer
        Progreso = 0;
        Completado = 0;

        Tarea = tarea.getText().toString();
        FechaInicio = fechaInicio.getText().toString();



        if(activeDeadline.isChecked()){
            ActiveFechaDeadline = 1;
            FechaDeadline = fechaDeadline.getText().toString();
            try {
                DEADLINE = dateFormat.parse(FechaDeadline);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(DEADLINE.equals(HOY)){
                ColorEstado = 4;
            }

        }else{
            ActiveFechaDeadline = 0;
            FechaDeadline = "vacio";
        }

        if(activeHacer.isChecked()){
            ActiveFechaToDo = 1;
            FechaToDo = fechaHacer.getText().toString();
            try {
                HACER = dateFormat.parse(FechaToDo);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(HACER.equals(HOY)){
                ColorEstado = 4;
            }
        }else{
            ActiveFechaToDo = 0;
            FechaToDo = "vacio";
        }

        if(project_vacio.isChecked()){
            nuevaTask = new Task(Tarea,ColorEstado,FechaInicio,FechaDeadline,
                    FechaToDo,Progreso,Completado,ActiveFechaDeadline,ActiveFechaToDo);
        }else {
            Id_Proyecto =GLOBAL.PROYECTO_TEMP.getId_Proyecto();
            nuevaTask = new Task(Tarea,ColorEstado,FechaInicio,FechaDeadline,
                    FechaToDo,Progreso,Completado,Id_Proyecto,ActiveFechaDeadline,ActiveFechaToDo);

            for(Tag x : GLOBAL.TAGS_TEMP){
                tagTask = new TagTask(x.getId_Tag(),(int)taskdbcontroller.getNextAutoIncrementTaskTable());
                long resultadoNewTagTask = taskdbcontroller.newTagTask(tagTask);
                if(resultadoNewTagTask == -1){
                    AnimacionError();
                }
            }

        }
        long resultadoImagen = 0, resultadoSubTask = 0;

        for(Imagen imagen : GLOBAL.IMAGENS_TEMP){
            resultadoImagen = taskdbcontroller.newImagen(imagen,imagen.getPath());
        }

        for (SubTask subTask : GLOBAL.SUB_TASKS_TEMP){
            resultadoSubTask = taskdbcontroller.newSubTask(subTask);
        }

        long id = taskdbcontroller.newTask(nuevaTask);

        //long id = -1;

        if(id == -1 || resultadoImagen == -1 || resultadoSubTask == -1){
            AnimacionError();
        }else{
            AnimacionGuardar();
        }

    }

    private void Actualizar() {
        int ColorEstado, Progreso, Completado, Id_Proyecto, ActiveFechaDeadline, ActiveFechaToDo;
        String Tarea, FechaInicio, FechaDeadline, FechaToDo, FechaHoy;
        Task nuevaTask;
        TagTask tagTask;
        Date HOY = new Date(), HACER = new Date(), DEADLINE = new Date();

        FechaHoy = dateFormat.format(c.getInstance().getTime());

        try {
            HOY = dateFormat.parse(FechaHoy);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // false -> 0   true -> 1
        ColorEstado = 1; // Azul ->1 = progreso                    Verde ->2 = Completado                 Rojo -> 3 = URGENTE fecha pasada                    Naranja-> 4 = dia de hacer
        Progreso = 0;
        Completado = 0;

        Tarea = tarea.getText().toString();
        FechaInicio = fechaInicio.getText().toString();



        if(activeDeadline.isChecked()){
            ActiveFechaDeadline = 1;
            FechaDeadline = fechaDeadline.getText().toString();
            try {
                DEADLINE = dateFormat.parse(FechaDeadline);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(DEADLINE.equals(HOY)){
                ColorEstado = 4;
            }

        }else{
            ActiveFechaDeadline = 0;
            FechaDeadline = "vacio";
        }

        if(activeHacer.isChecked()){
            ActiveFechaToDo = 1;
            FechaToDo = fechaHacer.getText().toString();
            try {
                HACER = dateFormat.parse(FechaToDo);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(HACER.equals(HOY)){
                ColorEstado = 4;
            }
        }else{
            ActiveFechaToDo = 0;
            FechaToDo = "vacio";
        }

        if(project_vacio.isChecked()){
            nuevaTask = new Task(Tarea,ColorEstado,FechaInicio,FechaDeadline,
                    FechaToDo,Progreso,Completado,ActiveFechaDeadline,ActiveFechaToDo);
        }else {
            Id_Proyecto =GLOBAL.PROYECTO_TEMP.getId_Proyecto();
            nuevaTask = new Task(Tarea,ColorEstado,FechaInicio,FechaDeadline,
                    FechaToDo,Progreso,Completado,Id_Proyecto,ActiveFechaDeadline,ActiveFechaToDo);

            DeleteOldTagTask();
            for(Tag x : GLOBAL.TAGS_TEMP){
                tagTask = new TagTask(x.getId_Tag(),GLOBAL.TASKS.get(editPos).getId_Task());
                long resultadoNewTagTask = taskdbcontroller.newTagTask(tagTask);
                if(resultadoNewTagTask == -1){
                    AnimacionError();
                }
            }

        }
        long resultadoImagen = 0, resultadoSubTask = 0;

        DeleteOldImages();
        for(Imagen imagen : GLOBAL.IMAGENS_TEMP){
            resultadoImagen = taskdbcontroller.newImagen(imagen,imagen.getPath());
        }

        DeleteOldSubTasks();
        for (SubTask subTask : GLOBAL.SUB_TASKS_TEMP){
            resultadoSubTask = taskdbcontroller.newSubTask(subTask);
        }

        final int temId = GLOBAL.TASKS.get(editPos).getId_Task();
        nuevaTask.setId_Task(temId);
        GLOBAL.TASKS.get(editPos).copyTask(nuevaTask);

        long id = taskdbcontroller.actualizarTask(GLOBAL.TASKS.get(editPos));

        //long id = -1;

        if(id == -1 || resultadoImagen == -1 || resultadoSubTask == -1){
            AnimacionError();
        }else{
            AnimacionGuardar();
        }

    }

    private void DeleteOldSubTasks() {
        for(SubTask subTask : GLOBAL.SUB_TASKS){
            if(subTask.getId_Task() == GLOBAL.TASKS.get(editPos).getId_Task()){
                taskdbcontroller.eliminarSubTask(subTask);
            }
        }
    }

    private void DeleteOldImages() {
        for(Imagen imagen : GLOBAL.IMAGENS){
            if(imagen.getId_Task() == GLOBAL.TASKS.get(editPos).getId_Task()){
                taskdbcontroller.eliminarImagen(imagen);
            }
        }
    }

    private void DeleteOldTagTask() {
        for(TagTask tagTask : GLOBAL.TAG_TASKS){
            if(tagTask.getId_Task() == GLOBAL.TASKS.get(editPos).getId_Task()){
                taskdbcontroller.eliminarTagTask(tagTask);
            }
        }

    }

    public void guardarTemp(){
        int ColorEstado, Progreso, Completado, Id_Proyecto, ActiveFechaDeadline, ActiveFechaToDo;
        String Tarea, FechaInicio, FechaDeadline, FechaToDo, FechaHoy;
        Task nuevaTask;
        TagTask tagTask;
        Date HOY = new Date(), HACER = new Date(), DEADLINE = new Date();

        FechaHoy = dateFormat.format(c.getInstance().getTime());

        try {
            HOY = dateFormat.parse(FechaHoy);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // false -> 0   true -> 1
        ColorEstado = 1; // Azul ->1 = progreso                    Verde ->2 = Completado                 Rojo -> 3 = URGENTE fecha pasada                    Naranja-> 4 = dia de hacer
        Progreso = 0;
        Completado = 0;

        Tarea = tarea.getText().toString();
        FechaInicio = fechaInicio.getText().toString();



        if(activeDeadline.isChecked()){
            ActiveFechaDeadline = 1;
            FechaDeadline = fechaDeadline.getText().toString();
            try {
                DEADLINE = dateFormat.parse(FechaDeadline);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(DEADLINE.equals(HOY)){
                ColorEstado = 4;
            }

        }else{
            ActiveFechaDeadline = 0;
            FechaDeadline = "vacio";
        }

        if(activeHacer.isChecked()){
            ActiveFechaToDo = 1;
            FechaToDo = fechaHacer.getText().toString();
            try {
                HACER = dateFormat.parse(FechaToDo);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(HACER.equals(HOY)){
                ColorEstado = 4;
            }
        }else{
            ActiveFechaToDo = 0;
            FechaToDo = "vacio";
        }

        if(project_vacio.isChecked()){
            nuevaTask = new Task(Tarea,ColorEstado,FechaInicio,FechaDeadline,
                    FechaToDo,Progreso,Completado,ActiveFechaDeadline,ActiveFechaToDo);
        }else {
            Id_Proyecto =GLOBAL.PROYECTO_TEMP.getId_Proyecto();
            nuevaTask = new Task(Tarea,ColorEstado,FechaInicio,FechaDeadline,
                    FechaToDo,Progreso,Completado,Id_Proyecto,ActiveFechaDeadline,ActiveFechaToDo);


        }

        /*

        *  Tag Temp
        *  Imagenes Temp
        *  Subtask Temp
        *  Project Temp
        *
        */

        GLOBAL.TASK_TEMP.copyTask(nuevaTask);

    }

    private void AnimacionError() {
        Animation aparecerTexto = AnimationUtils.loadAnimation(getContext(), R.anim.slide_derecha_to_izquierda);
        cortina.setVisibility(View.VISIBLE);
        textofinal.startAnimation(aparecerTexto);

        textofinal.setText("Error!");
        cortina.setBackground(getContext().getDrawable(R.color.colorRojo));

        aparecerTexto.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                listener.cerrarNewTaskFragment(true);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void AnimacionGuardar(){
        Animation aparecerTexto = AnimationUtils.loadAnimation(getContext(), R.anim.slide_derecha_to_izquierda);
        cortina.setVisibility(View.VISIBLE);
        textofinal.startAnimation(aparecerTexto);

        textofinal.setText("Guardado!");
        cortina.setBackground(getContext().getDrawable(R.color.colorVerde));

        aparecerTexto.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                listener.cerrarNewTaskFragment(true);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NewTaskFragmentListener) {
            listener = (NewTaskFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Fragment new task Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }




    public String getFechaInicio() {
        return fechaInicio.getText().toString();
    }

    public String getFechaDeadline() {
        return fechaDeadline.getText().toString();
    }

    public String getFechaHacer() {
        return fechaHacer.getText().toString();
    }

    public int getActiveDeadline() {
        if(activeDeadline.isChecked()){
            return 1;
        }else{
            return 0;
        }

    }



    public int getActiveHacer() {
        if(activeHacer.isChecked()){
            return 1;
        }else{
            return 0;
        }
    }

    public String getTarea() {
        return tarea.getText().toString();
    }

    public int getProject_vacio() {
        if(project_vacio.isChecked()){
            return 1;
        }else{
            return 0;
        }
    }

    public boolean isEditMode() {
        return editMode;
    }

    public int getEditPos() {
        return editPos;
    }

    public ArrayList<Proyecto> getSelectedProyecto() {
        return selectedProyecto;
    }

}


