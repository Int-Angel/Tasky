package com.angel.tasky.Adapters;

import android.content.Context;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angel.tasky.GLOBALES.GLOBAL;
import com.angel.tasky.POJOs.Proyecto;
import com.angel.tasky.POJOs.Tag;
import com.angel.tasky.POJOs.TagTask;
import com.angel.tasky.POJOs.Task;
import com.angel.tasky.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskActivity> {
    public Context context;
    //public ArrayList<Task> tareitas;
    TaskAdapterListener listener;
    //ArrayList<Proyecto> filterProjects;
    //ArrayList<Tag> filterTags;
    String filterDate;
    Boolean showAll;
    Boolean blue,green,orange,red, activeColorFilter;

    public String fechaHoy;

    DateFormat dateFormat;

    public interface TaskAdapterListener{
        void elementoEliminado(int pos);
        void abrirInfo(int pos);
        void actualizarElemento(int pos);
        void actualizarElementoSecundario(int pos);
        int pruebaVariable = 0;
    }

    public TaskAdapter(Context _context, String filterDate,TaskAdapterListener listener){
        //tareitas = new ArrayList<>(GLOBAL.TASKS);
        context = _context;
        this.listener = listener;
        //this.filterProjects = new ArrayList<>(filterProjects);
        //this.filterTags = new ArrayList<>(filterTags);
        this.filterDate = filterDate;

        showAll = false;
        dateFormat = new SimpleDateFormat("EEE d MMM yyyy");

        activeColorFilter = false;
        blue = true;
        green = true;
        orange = true;
        red = true;

    }

    public TaskAdapter(Context _context, TaskAdapterListener listener){
        context = _context;
        this.listener = listener;
    }



    @NonNull
    @Override
    public TaskActivity onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.item_task, null);
        TaskActivity taskActivity = new TaskActivity(view);
        return taskActivity;
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskActivity taskActivity, int i) {
        final int indice = i;
        final Animation rotar =AnimationUtils.loadAnimation(context,R.anim.button_rotation_up);
        Boolean appear = true;


        if(showAll){
            appear = colorFilter(indice);
        }else{
            appear = filterTask(indice);
        }

        if(appear){

            taskActivity.taskContainer.setVisibility(View.VISIBLE);

            taskActivity.tarea.setText(GLOBAL.TASKS.get(indice).getTarea());
            taskActivity.project.setText(obtenerProyectoPorId(GLOBAL.TASKS.get(indice).getId_Proyecto()));
            taskActivity.tags.setText(obtenerListaTags(GLOBAL.TASKS.get(indice).getId_Task()));
            taskActivity.deadline.setText("Deadline: "+GLOBAL.TASKS.get(indice).getFechaDeadline());
            taskActivity.inicio.setText("Inicio: "+GLOBAL.TASKS.get(indice).getFechaInicio());
            taskActivity.hacer.setText("Hacer: "+GLOBAL.TASKS.get(indice).getFechaToDo());

            if(GLOBAL.TASKS.get(indice).getColorEstado() == 1){
                taskActivity.color_imp_task.setBackground(context.getDrawable(R.drawable.ic_barra_azul));
            }
            if(GLOBAL.TASKS.get(indice).getColorEstado() == 2){
                taskActivity.color_imp_task.setBackground(context.getDrawable(R.drawable.ic_barra_verde));
            }
            if(GLOBAL.TASKS.get(indice).getColorEstado() == 3){
                taskActivity.color_imp_task.setBackground(context.getDrawable(R.drawable.ic_barra_roja));
            }
            if(GLOBAL.TASKS.get(indice).getColorEstado() == 4){
                taskActivity.color_imp_task.setBackground(context.getDrawable(R.drawable.ic_barra_naranja));
            }



            taskActivity.tarea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.abrirInfo(indice);
                }
            });
            taskActivity.taskContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.abrirInfo(indice);
                }
            });

            taskActivity.progreso.setText(GLOBAL.TASKS.get(indice).getProgreso()+"%");

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
            taskActivity.subTaskInfoAdapter = new SubTaskInfoAdapter(context, GLOBAL.TASKS.get(indice).getId_Task(), new SubTaskInfoAdapter.SubTaskInfoAdapterListener() {
                @Override
                public void updateProgress(int checked, int total, int pos) {
                    GLOBAL.TASKS.get(indice).setProgreso((100/total)*checked);
                    taskActivity.progreso.setText((100/total)*checked + "%");
                    if(checked == total){
                        GLOBAL.TASKS.get(indice).setProgreso(100);
                        taskActivity.progreso.setText("100%");

                        GLOBAL.TASKS.get(indice).setCompletado(1);
                        GLOBAL.TASKS.get(indice).setColorEstado(2);
                        taskActivity.color_imp_task.setBackground(context.getDrawable(R.drawable.ic_barra_verde));
                    }else {
                        GLOBAL.TASKS.get(indice).setCompletado(0);
                        calcularColorEstado(indice,fechaHoy);
                        if(GLOBAL.TASKS.get(indice).getColorEstado() == 1){
                            taskActivity.color_imp_task.setBackground(context.getDrawable(R.drawable.ic_barra_azul));
                        }
                        if(GLOBAL.TASKS.get(indice).getColorEstado() == 2){
                            taskActivity.color_imp_task.setBackground(context.getDrawable(R.drawable.ic_barra_verde));
                        }
                        if(GLOBAL.TASKS.get(indice).getColorEstado() == 3){
                            taskActivity.color_imp_task.setBackground(context.getDrawable(R.drawable.ic_barra_roja));
                        }
                        if(GLOBAL.TASKS.get(indice).getColorEstado() == 4){
                            taskActivity.color_imp_task.setBackground(context.getDrawable(R.drawable.ic_barra_naranja));
                        }
                    }


                    listener.actualizarElemento(indice);
                    listener.actualizarElementoSecundario(pos);

                }
            });

            taskActivity.listaSubtareas.setLayoutManager(linearLayoutManager);
            taskActivity.listaSubtareas.setAdapter(taskActivity.subTaskInfoAdapter);
            taskActivity.sihaySubtasks = taskActivity.subTaskInfoAdapter.isThereSubTasks();

            if(taskActivity.sihaySubtasks){
                taskActivity.checkBox.setVisibility(View.GONE);
                taskActivity.progreso.setVisibility(View.VISIBLE);
            }else{
                taskActivity.checkBox.setVisibility(View.VISIBLE);
                taskActivity.progreso.setVisibility(View.GONE);
            }

            taskActivity.progreso.setText(GLOBAL.TASKS.get(indice).getProgreso()+"%");
            if(GLOBAL.TASKS.get(indice).getCompletado() == 1){
                taskActivity.progreso.setText("100%");
                taskActivity.checkBox.setChecked(true);

            }else{
                taskActivity.checkBox.setChecked(false);

            }

            taskActivity.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(taskActivity.checkBox.isChecked()){
                        GLOBAL.TASKS.get(indice).setCompletado(1);
                        GLOBAL.TASKS.get(indice).setProgreso(100);
                        GLOBAL.TASKS.get(indice).setColorEstado(2);
                        taskActivity.color_imp_task.setBackground(context.getDrawable(R.drawable.ic_barra_verde));

                    }else{
                        GLOBAL.TASKS.get(indice).setCompletado(0);
                        GLOBAL.TASKS.get(indice).setProgreso(0);
                        calcularColorEstado(indice,fechaHoy);
                        if(GLOBAL.TASKS.get(indice).getColorEstado() == 1){
                            taskActivity.color_imp_task.setBackground(context.getDrawable(R.drawable.ic_barra_azul));
                        }
                        if(GLOBAL.TASKS.get(indice).getColorEstado() == 2){
                            taskActivity.color_imp_task.setBackground(context.getDrawable(R.drawable.ic_barra_verde));
                        }
                        if(GLOBAL.TASKS.get(indice).getColorEstado() == 3){
                            taskActivity.color_imp_task.setBackground(context.getDrawable(R.drawable.ic_barra_roja));
                        }
                        if(GLOBAL.TASKS.get(indice).getColorEstado() == 4){
                            taskActivity.color_imp_task.setBackground(context.getDrawable(R.drawable.ic_barra_naranja));
                        }

                    }

                    listener.actualizarElemento(indice);
                    //notifyDataSetChanged();
                }
            });


            final int altura = taskActivity.task_item_container.getMeasuredHeight();
            taskActivity.mostrarMas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskActivity.mostrarMas.startAnimation(rotar);
                    if(taskActivity.mostar){
                        taskActivity.mostar = false;
                        taskActivity.inicio.setVisibility(View.GONE);
                        taskActivity.deadline.setVisibility(View.GONE);
                        taskActivity.mostrarMas.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
                        taskActivity.listaSubtareas.setVisibility(View.GONE);
                        taskActivity.textoDetalles.setVisibility(View.GONE);
                        //collapse(taskActivity.task_item_container, altura);
                    }else{
                        taskActivity.mostar = true;
                        taskActivity.inicio.setVisibility(View.VISIBLE);
                        taskActivity.deadline.setVisibility(View.VISIBLE);
                        taskActivity.mostrarMas.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
                        taskActivity.listaSubtareas.setVisibility(View.VISIBLE);
                        if (taskActivity.sihaySubtasks){
                            taskActivity.textoDetalles.setVisibility(View.VISIBLE);
                        }
                        //expand(taskActivity.task_item_container, altura);
                    }
                }
            });
        }else{
            taskActivity.taskContainer.setVisibility(View.GONE);
        }


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

    public void removeTask(int pos){

        GLOBAL.TASKS.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, GLOBAL.TASKS.size());
        listener.elementoEliminado(pos);
    }

    public void restoreTask(Task task, int pos){

        Task x = new Task(null,-1,null,null,null,0,0,0,0);
        x.copyTask(task);
        GLOBAL.TASKS.add(pos, x);
        // notify item added by position
        notifyItemInserted(pos);
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


    public boolean filterTask(int pos){
        if(revisarTags(pos) && revisarFechas(pos)){
            return true;
        }
        return false;
    }

    private Boolean revisarTags(int pos){
        for (TagTask tagTask : GLOBAL.TAG_TASKS
        ) {

            if(tagTask.getId_Task() == GLOBAL.TASKS.get(pos).getId_Task()){

                for (Tag tag: GLOBAL.SELECTED_TAGS
                ) {
                    if(tagTask.getId_Tag() == tag.getId_Tag()){
                        return true;
                    }
                }

            }
        }

        return false;
    }

    private boolean revisarFechas(int pos){

        if(filterDate.equals(GLOBAL.TASKS.get(pos).getFechaDeadline()) ||
                filterDate.equals(GLOBAL.TASKS.get(pos).getFechaInicio()) ||
                filterDate.equals(GLOBAL.TASKS.get(pos).getFechaToDo())){
            return true;
        }

        return false;
    }

    private Boolean colorFilter(int indice) {
        if(blue && GLOBAL.TASKS.get(indice).getColorEstado() == 1 ||
                green && GLOBAL.TASKS.get(indice).getColorEstado() == 2 ||
                red && GLOBAL.TASKS.get(indice).getColorEstado() == 3 ||
                orange && GLOBAL.TASKS.get(indice).getColorEstado() == 4){
            return true;
        }
        return false;
    }


    public void setBlue(Boolean blue) {
        this.blue = blue;
        notifyDataSetChanged();
    }

    public void setGreen(Boolean green) {
        this.green = green;
        notifyDataSetChanged();
    }

    public void setOrange(Boolean orange) {
        this.orange = orange;
        notifyDataSetChanged();
    }

    public void setRed(Boolean red) {
        this.red = red;
        notifyDataSetChanged();
    }

    public void setActiveColorFilter(Boolean activeColorFilter) {
        this.activeColorFilter = activeColorFilter;
        notifyDataSetChanged();
    }

    public void setFilterDate(String filterDate){
        this.filterDate = filterDate;
        notifyDataSetChanged();
    }

    public void setShowAll(boolean x){
        showAll = x;
        notifyDataSetChanged();
    }

    public static void expand(final View v, int alturaInicial) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        //final int targetHeight = v.getMeasuredHeight();
        final int targetHeight = v.getMeasuredHeight();


        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = alturaInicial;
        //v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? RecyclerView.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v, final int alturaFinal) {
        final int initialHeight = v.getMeasuredHeight();


        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                /*if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }*/
                if(interpolatedTime>300){
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    //v.getLayoutParams().height = initialHeight - v.getHeight() - 1;
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    @Override
    public int getItemCount() {
        return GLOBAL.TASKS.size();
    }

    public class TaskActivity extends RecyclerView.ViewHolder {

        TextView tarea, project, tags, deadline, inicio, hacer,textoDetalles, progreso;
        CheckBox checkBox;
        ImageButton mostrarMas;
        RecyclerView listaSubtareas;
        Boolean mostar, sihaySubtasks;
        LinearLayout taskContainer;
        View color_imp_task;
        CardView task_item_container;
        public SubTaskInfoAdapter subTaskInfoAdapter;

        public TaskActivity(@NonNull View itemView) {
            super(itemView);
            mostar = false;
            tarea = itemView.findViewById(R.id.tarea_text_item_task);
            project = itemView.findViewById(R.id.item_task_project);
            tags = itemView.findViewById(R.id.item_task_tag);
            deadline = itemView.findViewById(R.id.item_task_deadline);
            inicio = itemView.findViewById(R.id.item_task_inicio);
            hacer = itemView.findViewById(R.id.item_task_todo);
            textoDetalles = itemView.findViewById(R.id.texto_detalles_item_task);
            listaSubtareas = itemView.findViewById(R.id.LISTA_SUB_TAREAS_ITEM);
            progreso = itemView.findViewById(R.id.progress_text_task);
            taskContainer = itemView.findViewById(R.id.task_container);
            color_imp_task = itemView.findViewById(R.id.color_imp_task);
            checkBox = itemView.findViewById(R.id.checkbox_item_task);
            mostrarMas = itemView.findViewById(R.id.mostrar_mas_item_task);
            task_item_container = itemView.findViewById(R.id.task_item_container);

        }
    }
}
