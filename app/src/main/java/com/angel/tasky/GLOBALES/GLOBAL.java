package com.angel.tasky.GLOBALES;

import com.angel.tasky.POJOs.INDICADORES;
import com.angel.tasky.POJOs.Imagen;
import com.angel.tasky.POJOs.Proyecto;
import com.angel.tasky.POJOs.SubTask;
import com.angel.tasky.POJOs.Tag;
import com.angel.tasky.POJOs.TagTask;
import com.angel.tasky.POJOs.Task;

import java.util.ArrayList;

// false -> 0   true -> 1

public class GLOBAL {
    public final static INDICADORES INDICADOR = new INDICADORES();

    public final static ArrayList<Task> TASKS = new ArrayList<>();
    public final static ArrayList<SubTask>  SUB_TASKS = new ArrayList<>();


    public final static ArrayList<Proyecto> PROYECTOS = new ArrayList<>();
    public final static ArrayList<Tag> TAGS = new ArrayList<>();
    public final static ArrayList<Imagen> IMAGENS = new ArrayList<>();

    public final static ArrayList<TagTask> TAG_TASKS = new ArrayList<>();

    public final static Task TASK_TEMP = new Task(null,-1,null,null,null,0,0,0,0);

    public final static Task DELETEDTASK = new Task(null,-1,null,null,null,0,0,0,0);

    public final static Proyecto PROYECTO_TEMP = new Proyecto(null);
    public final static ArrayList<Imagen> IMAGENS_TEMP = new ArrayList<>();
    public final static ArrayList<Tag> TAGS_TEMP = new ArrayList<>();
    public final static ArrayList<SubTask>  SUB_TASKS_TEMP = new ArrayList<>();


    public final static ArrayList<Proyecto> SELECTED_PROJECTS = new ArrayList<>();
    public final static ArrayList<Tag> SELECTED_TAGS = new ArrayList<>();

}
