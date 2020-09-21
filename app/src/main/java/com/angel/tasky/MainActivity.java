package com.angel.tasky;

//"Si puedes imaginarlo, puedes programarlo"
//                      - Alejandro Taboada
import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.angel.tasky.Adapters.BehindTaskAdapter;
import com.angel.tasky.Adapters.ProjectsFilterAdapter;
import com.angel.tasky.Adapters.TagsFilterAdapter;
import com.angel.tasky.Adapters.TaskAdapter;
import com.angel.tasky.GLOBALES.GLOBAL;
import com.angel.tasky.POJOs.Imagen;
import com.angel.tasky.POJOs.Proyecto;
import com.angel.tasky.POJOs.SubTask;
import com.angel.tasky.POJOs.Tag;
import com.angel.tasky.POJOs.TagTask;
import com.angel.tasky.POJOs.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.microedition.khronos.opengles.GL;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.model.CalendarEvent;
import devs.mulham.horizontalcalendar.utils.CalendarEventsPredicate;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

import static com.angel.tasky.App.CHANNEL_1_ID;



/*


_________________________________________________________________________________

TODO: onresume, onpause - testing
TODO: zoom imagenes   <-2-3
TODO: notificaciones  <-2-3

TODO: full calendar   <-4




TODO: configuracion

TODO: eventos
TODO: rutinas
TODO: estadisticas

TODO: mejorar eficiencia



*/


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        NewTaskFragment.NewTaskFragmentListener, ProjectsAndTagsFragment.ProjectsAndTagsFragmentListener ,
        NewProjectDialog.NewProjectDialogListener, NewTagDialog.NewTagDialogListener, DeleteProjectDialog.DeleteProjectDialogListener,
        DeleteTagDialog.DeleteTagDialogListener, TaskInfoFragment.TaskInfoFragmentListener, TodasLasTareasFragment.TodasLasTareasListener,
        FilterFragment.FilterFragmentListener, DetailedImageFragment.DetailedImageFragmentListener
{

    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int REQUEST_WRITE_PERMISSION = 786;

    FrameLayout contenedor, contenedorFiltros;
    View back, back2,back3, weird, watchSnackbar;
    FloatingActionButton fab;
    Toolbar toolbar;
    ImageButton filter,openSlideMenu,expand_calendar, mostrar_todas_tareas;
    DrawerLayout drawerLayout;
    RecyclerView lista_tareas;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;
    TextView mesYaño;
    HorizontalCalendar horizontalCalendar;
    LinearLayout  supercontainer;
    CheckBox  filterColorAzul,filterColorVerde, filterColorNaranja, filterColorRojo;
    RelativeLayout horizontalCalendar_container,fullCalendar_container, colorsFilterContainer;
    CalendarView fullCalendar;


    TASKDBCONTROLLER taskdbcontroller;
    TaskAdapter taskAdapter;


    Window window;

    ArrayList<Task> LISTADETAREITAS;

    int restoreFragment;
    //boolean x = false;
    boolean fabState;
    boolean calendarMode;
    boolean showAllTasks;

    Snackbar snackbar;

    final DateFormat df = new SimpleDateFormat("MMMM yyyy");
    DateFormat dateFormat = new SimpleDateFormat("EEE d MMM yyyy");

    NewTaskFragment newTaskFragment;                                     //1
    ProjectsAndTagsFragment projectsAndTagsFragment;                     //2
    ConfiguracionFragment configuracionFragment;                         //3
    NotificacionesFragment notificacionesFragment;                       //4
    TaskInfoFragment taskInfoFragment;                                   //5
    TodasLasTareasFragment todasLasTareasFragment;                       //6
    FilterFragment filterFragment;                                       //7

    NotificationManagerCompat notificationManager;


    private Paint p = new Paint();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            restoreFragment = savedInstanceState.getInt("recuperar");
            Task tempTask = savedInstanceState.getParcelable("TASKTEMP");
            //TODO: hacer un for para subtask, imagenes, tags, etc, mandar por aqui la cantindad de veces qeu se itera
            //creo que ya jala jeje
            GLOBAL.TASK_TEMP.copyTask(tempTask);
            Toast.makeText(this,"savedInstanceyeaaa",Toast.LENGTH_LONG).show();
        }

        window = getWindow();

        notificationManager = NotificationManagerCompat.from(this);

        final Animation appearFab = AnimationUtils.loadAnimation(this, R.anim.appear_fab);
        final Animation scrollDownFAB = AnimationUtils.loadAnimation(this, R.anim.scroll_down_fab);
        final Animation scrollUpFAB = AnimationUtils.loadAnimation(this, R.anim.scroll_up_fab);
        final Animation rotation = AnimationUtils.loadAnimation(this,R.anim.button_rotation_up);
        final Animation fullRotation = AnimationUtils.loadAnimation(this,R.anim.full_rotation);
        //final Animation goUpHorizontalCalendar = AnimationUtils.loadAnimation(this,R.anim.go_up_slide);

        showAllTasks = false;

        taskdbcontroller = new TASKDBCONTROLLER(MainActivity.this);
        LISTADETAREITAS = new ArrayList<>();

        calendarMode = false;

        contenedor = findViewById(R.id.contenedor);
        contenedorFiltros = findViewById(R.id.contenedor_filtros);
        back = findViewById(R.id.back);
        back2 = findViewById(R.id.back2);
        back3 = findViewById(R.id.back3);
        watchSnackbar = findViewById(R.id.watch_snackbar);
        fab = findViewById(R.id.fab);
        filter = findViewById(R.id.button_filter_list);
        drawerLayout = findViewById(R.id.drawer_layout);
        lista_tareas = findViewById(R.id.LISTA_TAREAS);
        navigationView = findViewById(R.id.nav_view);
        weird = findViewById(R.id.view_weird);
        mesYaño = findViewById(R.id.mesyAño_fecha_seleccionada);
        openSlideMenu = findViewById(R.id.button_open_slide_menu);
        supercontainer = findViewById(R.id.super_container);
        horizontalCalendar_container = findViewById(R.id.horizontalCalendar_container);
        expand_calendar = findViewById(R.id.expand_calendar);
        fullCalendar_container = findViewById(R.id.fullCalendar_container);
        fullCalendar = findViewById(R.id.fullCalendar);
        mostrar_todas_tareas = findViewById(R.id.mostrar_todas_tareas);
        colorsFilterContainer = findViewById(R.id.Color_Filter_Container);
        filterColorAzul = findViewById(R.id.Filtro_Barra_Azul);
        filterColorNaranja = findViewById(R.id.Filtro_Barra_Naranja);
        filterColorVerde = findViewById(R.id.Filtro_Barra_Verde);
        filterColorRojo = findViewById(R.id.Filtro_Barra_Roja);


        coordinatorLayout = findViewById(R.id.coordinatorlayout);

        weird.setBackground(new ShapeDrawable(new WeirdShape()));

        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        navigationView.setNavigationItemSelectedListener(this);


        final Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -36);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 36);





        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.horizontalCalendar)
                .addEvents(new CalendarEventsPredicate() {
            @Override
            public List<CalendarEvent> events(Calendar date) {

                CalendarEvent evento = new CalendarEvent(getColor(R.color.colorVerde));
                ArrayList<CalendarEvent> eventsList = new ArrayList<>();

                String fechaIterador = dateFormat.format(date.getTime());

                int cant = 0;
                cant = EventosDelDia(date);
                for(int i = 0;i<cant;i++){
                    eventsList.add(evento);
                }

                return eventsList;
            }
        })
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .configure()
                .showTopText(false)
                .end()
                .build();



        String fecha = df.format(startDate.getInstance().getTime());
        mesYaño.setText(fecha.toUpperCase());

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                String fecha = df.format(date.getTime());
                mesYaño.setText(fecha.toUpperCase());
                String completeFecha = dateFormat.format(date.getTime());
                taskAdapter.setFilterDate(completeFecha);
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView,
                                         int dx, int dy) {

            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                return true;
            }
        });
        expand_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeCalendarMode();
            }
        });
        mesYaño.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeCalendarMode();
            }
        });


        if(!GLOBAL.INDICADOR.getUsedZone()){
            fab.show();
            fabState = true;
            fab.startAnimation(appearFab);
        }else{
            fab.hide();
            fabState = false;
            putBackOn();
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GLOBAL.INDICADOR.getFragment_open() == 3 || GLOBAL.INDICADOR.getFragment_open() == 4){
                    onBackPressed();
                }
            }
        });


        openSlideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float slideOffset) {
                if(!GLOBAL.INDICADOR.getUsedZone()){
                    //super.onDrawerSlide(drawerView,slideOffset);

                    toggleTranslateFAB(slideOffset);
                    toolbar.setAlpha(1 - slideOffset/2);
                    window.setNavigationBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorTotalNegro));
                    //window.setNavigationBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorSpecialTransparent2));

                }else{
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                if(GLOBAL.INDICADOR.getUsedZone()){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    //window.setNavigationBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorVerde));
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    window.setNavigationBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorTotalNegro));
                }else {
                    if(!GLOBAL.INDICADOR.getUsedZone()){
                        window.setNavigationBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimaryLight));
                    }
                }
            }
        });


        String completeFecha = dateFormat.format(startDate.getInstance().getTime());
        taskAdapter = new TaskAdapter(MainActivity.this,completeFecha, new TaskAdapter.TaskAdapterListener() {
            @Override
            public void elementoEliminado(int pos) {

                if(GLOBAL.TASKS.size()<=5 && !fabState){
                    if(!snackbar.isShown()){
                        fab.startAnimation(scrollUpFAB);
                        fab.setY(1940);
                        fabState = true;
                    }else{
                        fab.startAnimation(scrollUpFAB);
                        fab.setY(1795);
                        fabState = true;
                    }
                }
            }

            @Override
            public void abrirInfo(int pos) {
                openTaskInfo(pos);
            }

            @Override
            public void actualizarElemento(int pos) {
                taskdbcontroller.actualizarTask(GLOBAL.TASKS.get(pos));
            }

            @Override
            public void actualizarElementoSecundario(int pos) {
                taskdbcontroller.actualizarSubTask(GLOBAL.SUB_TASKS.get(pos));
            }
        });


        String fecha2 = dateFormat.format(startDate.getInstance().getTime());
        taskAdapter.fechaHoy = fecha2;



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false);

        lista_tareas.setAdapter(taskAdapter);
        lista_tareas.setLayoutManager(linearLayoutManager);

        refrescarListaDeTareas();

        snackbar = Snackbar.make(findViewById(R.id.placeSnackbar), " Tarea eliminada!",
                Snackbar.LENGTH_LONG);

        watchSnackbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
                watchSnackbar.setVisibility(View.GONE);
            }
        });


        lista_tareas.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){

                //supercontainer.setY(supercontainer.getY()-dy);

                if (dy > 0){
                    if(fabState && GLOBAL.TASKS.size()>5){
                        fab.startAnimation(scrollDownFAB);
                        fabState = false;

                    }

                    //fab.hide();
                } else if (dy < 0 && GLOBAL.TASKS.size()>5){

                    //fab.show();
                    if(!fabState){



                        if(!snackbar.isShown()){
                            fab.startAnimation(scrollUpFAB);
                            fab.setY(1940);
                            fabState = true;
                        }else{
                            fab.startAnimation(scrollUpFAB);
                            fab.setY(1795);
                            fabState = true;
                        }

                    }
                }


            }
        });
        scrollDownFAB.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fab.setY(2500);
                //fab.hide();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter.startAnimation(fullRotation);
                openFilters();
            }
        });



        cargarListaDeProyectosyTags();
        cargarListaImagenes();
        cargarListaSubTasks();
        cargarListaTagTasks();
        requestPermission();

        enableSwipe();

        /*
                NOTIFICACIONES BROUUU
         */

        mostrar_todas_tareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // mostrarNotificacionPrueba();

                mostrar_todas_tareas.startAnimation(rotation);
                if(showAllTasks){
                    showAllTasks = false;
                    mostrar_todas_tareas.setImageDrawable(getDrawable(R.drawable.ic_filter_tilt_shift_white_24dp));

                    mesYaño.setTextColor(getColor(R.color.colorAzul));

                    if(calendarMode){
                         //fullCalendar_container.setVisibility(View.VISIBLE);
                    }else {
                        horizontalCalendar_container.setVisibility(View.VISIBLE);
                    }

                    colorsFilterContainer.setVisibility(View.GONE);

                    taskAdapter.setShowAll(showAllTasks);
                    taskAdapter.setActiveColorFilter(false);

                }else {
                    showAllTasks = true;
                    mostrar_todas_tareas.setImageDrawable(getDrawable(R.drawable.ic_filter_tilt_shift_green_24dp));

                    colorsFilterContainer.setVisibility(View.VISIBLE);

                    mesYaño.setTextColor(getColor(R.color.colorText2));

                    if(calendarMode){
                         //fullCalendar_container.setVisibility(View.GONE);
                    }else {
                        horizontalCalendar_container.setVisibility(View.GONE);
                    }

                    taskAdapter.setShowAll(showAllTasks);
                    taskAdapter.setActiveColorFilter(true);
                }
            }
        });

        filterColorAzul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterColorAzul.isChecked()){
                    taskAdapter.setBlue(true);
                }else {
                    taskAdapter.setBlue(false);
                }
            }
        });

        filterColorRojo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterColorRojo.isChecked()){
                    taskAdapter.setRed(true);
                }else {
                    taskAdapter.setRed(false);
                }
            }
        });

        filterColorVerde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterColorVerde.isChecked()){
                    taskAdapter.setGreen(true);
                }else {
                    taskAdapter.setGreen(false);
                }
            }
        });

        filterColorNaranja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterColorNaranja.isChecked()){
                    taskAdapter.setOrange(true);
                }else {
                    taskAdapter.setOrange(false);
                }
            }
        });


        //Actualizar color estado
        String fechaHoy = dateFormat.format(startDate.getInstance().getTime());
        Date HOY = new Date();

        try {
            HOY = dateFormat.parse(fechaHoy);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i<GLOBAL.TASKS.size(); i++){
            Date deadline = new Date();
            Date hacer = new Date();

            if(GLOBAL.TASKS.get(i).getCompletado() == 0){
                if(GLOBAL.TASKS.get(i).getActiveFechaDeadline() == 1){
                    try {
                        deadline = dateFormat.parse(GLOBAL.TASKS.get(i).getFechaDeadline());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        deadline = HOY;
                    }

                    if(deadline.before(HOY)){
                        GLOBAL.TASKS.get(i).setColorEstado(3);
                        actualizarTask_info(i);
                    }else if(deadline.equals(HOY)){
                        GLOBAL.TASKS.get(i).setColorEstado(4);
                        actualizarTask_info(i);
                    }

                }
                if(GLOBAL.TASKS.get(i).getActiveFechaToDo() == 1){
                    try {
                        hacer = dateFormat.parse(GLOBAL.TASKS.get(i).getFechaToDo());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        hacer = HOY;
                    }
                    if(hacer.before(HOY)){
                        if(GLOBAL.TASKS.get(i).getActiveFechaDeadline() == 1 && deadline.equals(HOY)){
                            GLOBAL.TASKS.get(i).setColorEstado(4);
                            actualizarTask_info(i);
                        }else {
                            GLOBAL.TASKS.get(i).setColorEstado(3);
                            actualizarTask_info(i);
                        }

                    }else if(hacer.equals(HOY)){
                        GLOBAL.TASKS.get(i).setColorEstado(4);
                        actualizarTask_info(i);
                    }
                }
            }

        }

        mostrarNotificacionPrueba();

    }


    private void mostrarNotificacionPrueba(){


        Calendar c;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiber.class);
        PendingIntent pendingIntent;

        /*c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,6);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) System.currentTimeMillis(), intent,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);

        c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,8);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        pendingIntent = PendingIntent.getBroadcast(this, (int) System.currentTimeMillis(), intent,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);*/

        for(int i = 6; i <= 23; i = i +2){
            c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY,i);
            c.set(Calendar.MINUTE,0);
            c.set(Calendar.SECOND,0);
            pendingIntent = PendingIntent.getBroadcast(this, (int) System.currentTimeMillis(), intent,0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
        }

    }

    private int EventosDelDia(Calendar date){
        int n = 0;

        String x;

        x = dateFormat.format(date.getTime());

        for (Task tareita: GLOBAL.TASKS
             ) {
           if(tareita.getFechaDeadline().equals(x) || tareita.getFechaToDo().equals(x) || tareita.getFechaInicio().equals(x)){
               n++;
           }
        }

        return n;
    }

    private void changeCalendarMode() {
        final Animation rotation = AnimationUtils.loadAnimation(this,R.anim.button_rotation_up);

            if(!showAllTasks){
                expand_calendar.startAnimation(rotation);
                if(calendarMode){
                    calendarMode = false;
                    expand_calendar.setImageDrawable(getDrawable(R.drawable.ic_expand_more_white_24dp));
                    horizontalCalendar_container.setVisibility(View.VISIBLE);
                    //fullCalendar_container.setVisibility(View.GONE);
                    //mesYaño.setVisibility(View.VISIBLE);

                }else{
                    calendarMode = true;
                    expand_calendar.setImageDrawable(getDrawable(R.drawable.ic_expand_less_white_24dp));
                    horizontalCalendar_container.setVisibility(View.GONE);
                    //fullCalendar_container.setVisibility(View.VISIBLE);
                    //mesYaño.setVisibility(View.GONE);
                }
            }

    }

    @Override
    protected void onStart() {
        super.onStart();

        switch (restoreFragment){
            case 1:
                openNewTask(false,true,0);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("recuperar",GLOBAL.INDICADOR.getFragment_open());
        if(GLOBAL.INDICADOR.getFragment_open() == 1){
            newTaskFragment.guardarTemp();
            outState.putParcelable("TASKTEMP",GLOBAL.TASK_TEMP);
        }
        super.onSaveInstanceState(outState);
    }
    private void openTaskInfo(int pos) {

        final Animation aparecer_fragment;
        aparecer_fragment = AnimationUtils.loadAnimation(this, R.anim.prueba_anim_1);

        window.setNavigationBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorTotalNegro));

        if(!GLOBAL.INDICADOR.getUsedZone()){

            GLOBAL.INDICADOR.setFragment_open(5);
            GLOBAL.INDICADOR.setUsedZone(true);

            Calendar startDate = Calendar.getInstance();
            String fecha = dateFormat.format(startDate.getInstance().getTime());

            taskInfoFragment = new TaskInfoFragment();
            taskInfoFragment.pos = pos;
            taskInfoFragment.fechaHoy = fecha;

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor,taskInfoFragment,taskInfoFragment.getTag())
                    .commit();

            contenedor.startAnimation(aparecer_fragment);

            aparecer_fragment.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    fab.hide();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    putBackOn();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }

    }

    public void openNewTask(boolean x,boolean res, int pos){
        final Animation aparecer_fragment;
        aparecer_fragment = AnimationUtils.loadAnimation(this, R.anim.prueba_anim_1);

        window.setNavigationBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorTotalNegro));

        if(!GLOBAL.INDICADOR.getUsedZone()){

            GLOBAL.INDICADOR.setFragment_open(1);
            GLOBAL.INDICADOR.setUsedZone(true);

            newTaskFragment = new NewTaskFragment();
            newTaskFragment.editMode = x;
            newTaskFragment.editPos = pos;
            newTaskFragment.restoreData = res;

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor,newTaskFragment,newTaskFragment.getTag())
                    .commit();

            contenedor.startAnimation(aparecer_fragment);

            aparecer_fragment.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    fab.hide();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    putBackOn();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
    }

    public void NewTask(View view) {
        openNewTask(false,false, 0);
    }

    private void openFilters(){
        final Animation aparecer_fragment;
        //aparecer_fragment = AnimationUtils.loadAnimation(this, R.anim.prueba_anim_1);
        aparecer_fragment = AnimationUtils.loadAnimation(this, R.anim.scale_filter);
        //window.setNavigationBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimaryLight));
       // window.setNavigationBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorTotalNegro));
        if(!GLOBAL.INDICADOR.getUsedZone()){

            GLOBAL.INDICADOR.setFragment_open(7);
            GLOBAL.INDICADOR.setUsedZone(true);

            filterFragment = new FilterFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor,filterFragment,filterFragment.getTag())
                    .commit();

            contenedor.startAnimation(aparecer_fragment);

            aparecer_fragment.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    fab.hide();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    putBackOn2();

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
    }

    private void openProjectsAndTags() {
        final Animation aparecer_fragment;
        aparecer_fragment = AnimationUtils.loadAnimation(this, R.anim.prueba_anim_1);
        window.setNavigationBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorTotalNegro));
        if(!GLOBAL.INDICADOR.getUsedZone()){

            GLOBAL.INDICADOR.setFragment_open(2);
            GLOBAL.INDICADOR.setUsedZone(true);

            projectsAndTagsFragment = new ProjectsAndTagsFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor,projectsAndTagsFragment,projectsAndTagsFragment.getTag())
                    .commit();

            contenedor.startAnimation(aparecer_fragment);

            aparecer_fragment.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    fab.hide();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    putBackOn();

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
    }

    private void openConfiguracion() {
        final Animation aparecer_fragment;
        aparecer_fragment = AnimationUtils.loadAnimation(this, R.anim.prueba_anim_1);
        window.setNavigationBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorTotalNegro));
        if(!GLOBAL.INDICADOR.getUsedZone()){

            GLOBAL.INDICADOR.setFragment_open(3);
            GLOBAL.INDICADOR.setUsedZone(true);

            configuracionFragment = new ConfiguracionFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor,configuracionFragment,configuracionFragment.getTag())
                    .commit();

            contenedor.startAnimation(aparecer_fragment);

            aparecer_fragment.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    fab.hide();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                   // back.setVisibility(View.VISIBLE);
                    back3.setVisibility(View.VISIBLE);
                    window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorSpecialTransparent2));
                   // putBackOn();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
    }

    private void openNotificaciones() {
        final Animation aparecer_fragment;
        aparecer_fragment = AnimationUtils.loadAnimation(this, R.anim.prueba_anim_1);
        window.setNavigationBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorTotalNegro));
        if(!GLOBAL.INDICADOR.getUsedZone()){

            GLOBAL.INDICADOR.setFragment_open(4);
            GLOBAL.INDICADOR.setUsedZone(true);

            notificacionesFragment = new NotificacionesFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor,notificacionesFragment,notificacionesFragment.getTag())
                    .commit();

            contenedor.startAnimation(aparecer_fragment);

            aparecer_fragment.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    fab.hide();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //back.setVisibility(View.VISIBLE);
                    back3.setVisibility(View.VISIBLE);
                    window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorSpecialTransparent2));
                    //putBackOn();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
    }

    public void openTodasLasTareas(){
        final Animation aparecer_fragment;
        aparecer_fragment = AnimationUtils.loadAnimation(this, R.anim.slide_derecha_to_izquierda);
        window.setNavigationBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorTotalNegro));
        if(!GLOBAL.INDICADOR.getUsedZone()){

            GLOBAL.INDICADOR.setFragment_open(6);
            GLOBAL.INDICADOR.setUsedZone(true);

            todasLasTareasFragment = new TodasLasTareasFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor,todasLasTareasFragment,todasLasTareasFragment.getTag())
                    .commit();

            contenedor.startAnimation(aparecer_fragment);

            aparecer_fragment.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    fab.hide();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //back.setVisibility(View.VISIBLE);
                    back3.setVisibility(View.VISIBLE);
                    window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorSpecialTransparent2));
                    //putBackOn();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
            fab.show();
        }else{

            if(GLOBAL.INDICADOR.getUsedZone()){

                if(GLOBAL.INDICADOR.getFragment_open() == 5 && taskInfoFragment.detailedImageOpen){
                    taskInfoFragment.CerrarDetailedImage();
                }else{
                    RemoveFragment();

                    GLOBAL.INDICADOR.setFragment_open(0);
                    GLOBAL.INDICADOR.setUsedZone(false);
                }


            }else{
               // Toast.makeText(this, "super.onBackPressed()", Toast.LENGTH_LONG).show();
                //super.onBackPressed();
            }
        }
    }

    public void toggleTranslateFAB(float slideOffset){
        fab.setTranslationX(slideOffset*800);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

       if(!GLOBAL.INDICADOR.getUsedZone()){
           switch (menuItem.getItemId()){

               case R.id.menu_projects_and_tags:
                  // drawerLayout.closeDrawer(GravityCompat.START);
                   onBackPressed();
                   openProjectsAndTags();
                   break;
               case R.id.menu_notificaciones:
                 onBackPressed();
                 openNotificaciones();
                   break;
               case R.id.menu_configuracion:
                  onBackPressed();
                  openConfiguracion();
                   break;
               case R.id.menu_totas_las_tareas:
                   onBackPressed();
                   openTodasLasTareas();
           }
       }

        return true;
    }

    public void RemoveFragment(){

        Animation moveBack_1 = AnimationUtils.loadAnimation(this,R.anim.prueba_anim_2);
        Animation moveBack_2 = AnimationUtils.loadAnimation(this,R.anim.prueba_anim_2);
        Animation moveBack_3 = AnimationUtils.loadAnimation(this,R.anim.prueba_anim_2);
        Animation moveBack_4 = AnimationUtils.loadAnimation(this,R.anim.prueba_anim_2);
        Animation moveBack_5 = AnimationUtils.loadAnimation(this,R.anim.prueba_anim_2);
        Animation moveBack_6 = AnimationUtils.loadAnimation(this,R.anim.prueba_anim_2);//cambiar
        Animation moveBack_7 = AnimationUtils.loadAnimation(this,R.anim.prueba_anim_2);
        putBackOff();
        window.setNavigationBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimaryLight));

        if(GLOBAL.INDICADOR.getFragment_open() == 1){
            contenedor.startAnimation(moveBack_1);
            GLOBAL.SUB_TASKS_TEMP.clear();
            GLOBAL.IMAGENS_TEMP.clear();
            GLOBAL.TAGS_TEMP.clear();
        }
        if(GLOBAL.INDICADOR.getFragment_open() == 2){
            contenedor.startAnimation(moveBack_2);
            GLOBAL.INDICADOR.setPos_proyecto_edit(-1);
            GLOBAL.INDICADOR.setPos_tag_edit(-1);
        }
        if(GLOBAL.INDICADOR.getFragment_open() == 3){
            contenedor.startAnimation(moveBack_3);
        }
        if(GLOBAL.INDICADOR.getFragment_open() == 4){
            contenedor.startAnimation(moveBack_4);
        }
        if(GLOBAL.INDICADOR.getFragment_open() == 5){
            contenedor.startAnimation(moveBack_5);
        }
        if(GLOBAL.INDICADOR.getFragment_open() == 6){
            contenedor.startAnimation(moveBack_6);
        }
        if(GLOBAL.INDICADOR.getFragment_open() == 7){
            contenedor.startAnimation(moveBack_7);
        }
        //poner una animacion para cada fragment diferente proque si no no sirve (no entra en el if), y crear un listener para la animacion

        moveBack_1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }


            @Override
            public void onAnimationEnd(Animation animation) {
                getSupportFragmentManager().beginTransaction()
                        .remove(newTaskFragment)
                        .commit();
                fab.show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        moveBack_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getSupportFragmentManager().beginTransaction()
                        .remove(projectsAndTagsFragment)
                        .commit();
                toggleTranslateFAB(0);
                fab.show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        moveBack_3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getSupportFragmentManager().beginTransaction()
                        .remove(configuracionFragment)
                        .commit();
                //back.setVisibility(View.GONE);
                window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorTransparente));
                back3.setVisibility(View.GONE);
                toggleTranslateFAB(0);
                fab.show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        moveBack_4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getSupportFragmentManager().beginTransaction()
                        .remove(notificacionesFragment)
                        .commit();
                //back.setVisibility(View.GONE);
                window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorTransparente));
                back3.setVisibility(View.GONE);
                toggleTranslateFAB(0);
                fab.show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        moveBack_5.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }


            @Override
            public void onAnimationEnd(Animation animation) {
                getSupportFragmentManager().beginTransaction()
                        .remove(taskInfoFragment)
                        .commit();
                fab.show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        moveBack_6.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }


            @Override
            public void onAnimationEnd(Animation animation) {
                getSupportFragmentManager().beginTransaction()
                        .remove(todasLasTareasFragment)
                        .commit();
                toggleTranslateFAB(0);
                fab.show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        moveBack_7.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }


            @Override
            public void onAnimationEnd(Animation animation) {
                getSupportFragmentManager().beginTransaction()
                        .remove(filterFragment)
                        .commit();
                fab.show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void putBackOn(){
       // back.setVisibility(View.VISIBLE);
        back2.setVisibility(View.VISIBLE);
        back3.setVisibility(View.VISIBLE);
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorSpecialTransparent2));
        toolbar.setAlpha(0.06f);
        window.setNavigationBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorTotalNegro));
    }
    public void putBackOn2(){
        // back.setVisibility(View.VISIBLE);
        //back2.setVisibility(View.VISIBLE);
        //back3.setVisibility(View.VISIBLE);
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorSpecialTransparent2));
        toolbar.setAlpha(0.06f);
        window.setNavigationBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorTotalNegro));
    }
    public void putBackOff(){
       // back.setVisibility(View.GONE);
        back2.setVisibility(View.GONE);
        back3.setVisibility(View.GONE);
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorTransparente));
        toolbar.setAlpha(1);
    }


    public void refrescarListaDeTareas(){
        if(taskAdapter == null) return;
        LISTADETAREITAS = taskdbcontroller.obtenerTasks();
        taskAdapter.notifyDataSetChanged();
        horizontalCalendar.refresh();
    }

    public void refrescarListaDeProyectosyTags() {
        if(projectsAndTagsFragment.projectsAndTagsAdapter == null) return;
        taskdbcontroller.obtenerProyectos();
        taskdbcontroller.obtenerTags();
        projectsAndTagsFragment.projectsAndTagsAdapter.notifyDataSetChanged();

        actualizarListaProjectsAndTags();
    }

    public void cargarListaDeProyectosyTags(){
        taskdbcontroller.obtenerProyectos();
        taskdbcontroller.obtenerTags();
    }

    public void cargarListaImagenes(){
        taskdbcontroller.obtenerImagenes();
    }
    public void cargarListaSubTasks(){
        taskdbcontroller.obtenerSubTasks();
    }
    public void cargarListaTagTasks(){
        taskdbcontroller.obtenerTagTask();
    }

    @Override
    public void cerrarNewTaskFragment(boolean x) {
        if(x){
            onBackPressed();
            refrescarListaDeTareas();
        }
        if(true){
            cargarListaTagTasks();
            cargarListaSubTasks();
            cargarListaImagenes();
        }
    }

    @Override
    public void cerrarProjectsAndTagsListener(boolean x) {
        if(x){
            onBackPressed();
            //refrescarListaDeTareas();
        }
    }

    @Override
    public void cerrarTaskInfoFragment(boolean x) {
        if(x){
            onBackPressed();
            //refrescarListaDeTareas();
        }
    }

    @Override
    public void cerrarFilterFragment(boolean x) {
        if (x){
            onBackPressed();
        }
    }

    @Override
    public void AplicarFiltro() {
        onBackPressed();
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    public void actualizarTask_info(int pos) {
        taskdbcontroller.actualizarTask(GLOBAL.TASKS.get(pos));
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    public void actualizarSubTask_info(int pos) {
        taskdbcontroller.actualizarSubTask(GLOBAL.SUB_TASKS.get(pos));
    }

    @Override
    public void borrarTaskInfo(int pos) {
        funsionEliminarTarea(pos);
    }

    @Override
    public void abrirEditMode(boolean x, int pos) {
        onBackPressed();

        openNewTask(x,false, pos);
    }

    @Override
    public void CerrarTodasLasTareas(boolean x) {
        if(x){
            onBackPressed();
        }
    }

    @Override
    public void abrirDialog() {
        NewProjectDialog newProjectDialog = new NewProjectDialog();
        newProjectDialog.show(getSupportFragmentManager(),"Nuevo proyecto");
    }

    @Override
    public void abrirDialogTag(int i) {
        NewTagDialog newTagDialog = new NewTagDialog();
        newTagDialog.id = i;
        newTagDialog.show(getSupportFragmentManager(),"Nuevo tag");
    }

    @Override
    public void abrirDialogEliminarProject(int i) {
        DeleteProjectDialog deleteProjectDialog = new DeleteProjectDialog();
        deleteProjectDialog.pos = i;
        deleteProjectDialog.show(getSupportFragmentManager(),"Eliminar Proyecto");
    }

    @Override
    public void abrirDialogEliminarTag(int i) {
        DeleteTagDialog deleteTagDialog = new DeleteTagDialog();
        deleteTagDialog.pos = i;
        deleteTagDialog.show(getSupportFragmentManager(),"Eliminar Tag");

    }

    @Override
    public void actualizarListaProjectsAndTags() {
        refrescarListaDeProyectosyTags();
    }

    @Override
    public void addProject(String projectTitle) {
        //projectsAndTagsFragment.AgregarNuevoProyecto(projectTitle);
        Proyecto newProject = new Proyecto(projectTitle);
        taskdbcontroller.newProyecto(newProject);
        refrescarListaDeProyectosyTags();

    }



    @Override
    public void addTag(String tagTitle, int pos) {
        //projectsAndTagsFragment.AgregarNuevoTag(tagTitle,id);
        //projectsAndTagsFragment.projectsAndTagsAdapter.notifyDataSetChanged();
        int Id;
        Id = GLOBAL.PROYECTOS.get(pos).getId_Proyecto();
        Tag newTag = new Tag(Id,tagTitle);
        taskdbcontroller.newTag(newTag);
        refrescarListaDeProyectosyTags();
    }

    @Override
    public void deleteProject(int x) {
        taskdbcontroller.eliminarProyecto(GLOBAL.PROYECTOS.get(x));
        refrescarListaDeProyectosyTags();
    }

    @Override
    public void deleteTag(int x) {
        taskdbcontroller.eliminarTag(GLOBAL.TAGS.get(x));
        refrescarListaDeProyectosyTags();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }

    private void enableSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    final Task deletedTask = GLOBAL.TASKS.get(position);
                    final int deletedPosition = position;
                    taskAdapter.removeTask(position);
                    snackbar.setText("Añadido a lista de hacer hoy!");
                    taskAdapter.restoreTask(deletedTask, deletedPosition);
                    viewHolder.itemView.setAlpha(1);
                    final DateFormat df = new SimpleDateFormat("EEE d MMM yyyy");
                    String dateTodo = df.format(Calendar.getInstance().getTime());
                    GLOBAL.TASKS.get(position).setFechaToDo(dateTodo);
                    if(GLOBAL.TASKS.get(position).getCompletado() == 0){
                        GLOBAL.TASKS.get(position).setColorEstado(4);
                    }
                    taskAdapter.notifyDataSetChanged();

                    snackbar.setAction("DESHACER", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            GLOBAL.TASKS.get(position).setFechaToDo("vacio");
                            if(GLOBAL.TASKS.get(position).getCompletado() == 0){
                                calcularColorEstado(position,taskAdapter.fechaHoy);
                            }
                            taskAdapter.notifyDataSetChanged();
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                    watchSnackbar.setVisibility(View.VISIBLE);

                        snackbar.addCallback(new Snackbar.Callback(){

                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                //see Snackbar.Callback docs for event details
                                try{
                                    if(GLOBAL.TASKS.get(position).getFechaToDo().equals("vacio")){
                                        GLOBAL.TASKS.get(position).setActiveFechaToDo(0);
                                    }else{
                                        GLOBAL.TASKS.get(position).setActiveFechaToDo(1);
                                    }
                                    //GLOBAL.TASKS.get(position).setColorEstado(1);
                                    taskdbcontroller.actualizarTask(GLOBAL.TASKS.get(position));
                                    horizontalCalendar.refresh();
                                }catch (Exception e){

                                }
                            }

                            @Override
                            public void onShown(Snackbar snackbar) {
                            }

                        });



                } else {
                   funsionEliminarTarea(position);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        viewHolder.itemView.setAlpha(60/(dX*1.2f));

                        Drawable drawable = ContextCompat.getDrawable(MainActivity.this,R.drawable.ic_delete_white_12dp);
                        icon = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_4444);



                        p.setColor(getColor(R.color.colorRojo));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        Canvas canvas = new Canvas(icon);
                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        drawable.draw(canvas);
                        c.drawBitmap(icon,null,icon_dest,p);
                        /*icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_forever_black_24dp);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);*/
                    } else {
                       // viewHolder.itemView.setAlpha(60/((dX*-1)*1.2f));

                        Drawable drawable = ContextCompat.getDrawable(MainActivity.this,R.drawable.ic_schedule_white_12dp);
                        icon = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_4444);



                        p.setColor(getColor(R.color.colorVerde));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        Canvas canvas = new Canvas(icon);
                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        drawable.draw(canvas);
                        c.drawBitmap(icon,null,icon_dest,p);
                        /*icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_forever_black_24dp);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);*/
                    }
                    if(dX == 0){
                        viewHolder.itemView.setAlpha(1);
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(lista_tareas);
    }

    void funsionEliminarTarea(int position){

        final int deletedPosition = position;
        //GLOBAL.DELETEDTASK.clear();
        GLOBAL.DELETEDTASK.copyTask(GLOBAL.TASKS.get(position));

        taskAdapter.removeTask(position);

        snackbar.setAction("DESHACER", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // undo is selected, restore the deleted item
                taskAdapter.restoreTask(GLOBAL.DELETEDTASK, deletedPosition);
                GLOBAL.DELETEDTASK.clear();
                refrescarListaDeTareas();

            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
        watchSnackbar.setVisibility(View.VISIBLE);
        snackbar.addCallback(new Snackbar.Callback(){

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                //see Snackbar.Callback docs for event details

                if(event != DISMISS_EVENT_ACTION){
                    for(SubTask subTask : GLOBAL.SUB_TASKS){
                        if (subTask.getId_Task() == GLOBAL.DELETEDTASK.getId_Task()){
                            taskdbcontroller.eliminarSubTask(subTask);
                        }
                    }
                    for (Imagen imagen: GLOBAL.IMAGENS){
                        if(imagen.getId_Task() == GLOBAL.DELETEDTASK.getId_Task()){
                            taskdbcontroller.eliminarImagen(imagen);
                        }
                    }
                    for(TagTask tagTask : GLOBAL.TAG_TASKS){
                        if(tagTask.getId_Task() == GLOBAL.DELETEDTASK.getId_Task()){
                            taskdbcontroller.eliminarTagTask(tagTask);
                        }
                    }
                    taskdbcontroller.eliminarTask(GLOBAL.DELETEDTASK);
                    refrescarListaDeTareas();
                    cargarListaImagenes();
                    cargarListaSubTasks();
                    cargarListaTagTasks();
                    GLOBAL.DELETEDTASK.clear();

                    horizontalCalendar.refresh();
                }

            }

            @Override
            public void onShown(Snackbar snackbar) {
            }

        });


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
            toDoDate  = dateFormat.parse(GLOBAL.TASKS.get(pos).getFechaToDo());
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

    @Override
    public void CerrarDetailedImage() {
        taskInfoFragment.CerrarDetailedImage();
    }
}
