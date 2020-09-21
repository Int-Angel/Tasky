package com.angel.tasky;

import android.content.ContentValues;
import android.content.Context;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.angel.tasky.GLOBALES.GLOBAL;
import com.angel.tasky.POJOs.Imagen;
import com.angel.tasky.POJOs.Proyecto;
import com.angel.tasky.POJOs.SubTask;
import com.angel.tasky.POJOs.Tag;
import com.angel.tasky.POJOs.TagTask;
import com.angel.tasky.POJOs.Task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class TASKDBCONTROLLER {
    private AyudanteBaseDeDatos ayudanteBaseDeDatos;

    private  String NOMBRE_BASE_DE_DATOS = "TaskDB";
    private  int VERSION_BASE_DE_DATOS = 1;

    private  String TABLA_TASK = "Task"
            , TABLA_SUBTASK = "SubTask"
            , TABLA_IMAGEN = "Imagen"
            , TABLA_PROYECTO = "Proyecto"
            , TABLA_TAG = "Tag"
            , TABLA_TAGTASK = "TagTask";

    public TASKDBCONTROLLER(Context context){
        ayudanteBaseDeDatos = new AyudanteBaseDeDatos(context);
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    //convert from bitmap to byte array
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public long newTask(Task task){
        SQLiteDatabase database = ayudanteBaseDeDatos.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("Tarea",task.getTarea());
        contentValues.put("ColorEstado",task.getColorEstado());
        contentValues.put("FechaInicio",task.getFechaInicio());
        contentValues.put("FechaDeadline",task.getFechaDeadline());
        contentValues.put("FechaToDo",task.getFechaToDo());
        contentValues.put("Progreso",task.getProgreso());
        contentValues.put("Completado",task.getCompletado());
        contentValues.put("Id_Proyecto",task.getId_Proyecto());
        contentValues.put("ActiveFechaDeadline",task.getActiveFechaDeadline());
        contentValues.put("ActiveFechaToDo",task.getActiveFechaToDo());

        return database.insert(TABLA_TASK, null, contentValues);
    }

    public long newImagen(Imagen imagen, String path){
        SQLiteDatabase database = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //byte[] data = getBitmapAsByteArray(imagen.getImagen());


        contentValues.put("Imagen",path);
        contentValues.put("Id_Task",imagen.getId_Task());

        return database.insert(TABLA_IMAGEN,null,contentValues);
    }

    public long newProyecto(Proyecto proyecto){
        SQLiteDatabase database = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("nombreProyecto",proyecto.getNombreProyecto());

        return database.insert(TABLA_PROYECTO,null,contentValues);
    }

    public long newTag(Tag tag){
        SQLiteDatabase database = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("nombreTag",tag.getNombreTag());
        contentValues.put("Id_Proyecto",tag.getId_Proyecto());

        return database.insert(TABLA_TAG,null,contentValues);
    }

    public long newTagTask(TagTask tagTask){
        SQLiteDatabase database = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("Id_Tag",tagTask.getId_Tag());
        contentValues.put("Id_Task",tagTask.getId_Task());

        return database.insert(TABLA_TAGTASK,null,contentValues);
    }

    public long newSubTask(SubTask subTask){
        SQLiteDatabase database = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("SubTarea",subTask.getSubTarea());
        contentValues.put("Completado",subTask.getCompletado());
        contentValues.put("Id_Task",subTask.getId_Task());

        return database.insert(TABLA_SUBTASK,null,contentValues);
    }



    public ArrayList<Task> obtenerTasks(){
        ArrayList<Task> tasks = new ArrayList<>();

        SQLiteDatabase database = ayudanteBaseDeDatos.getReadableDatabase();

        String[] columnasAConsultar = {"Id_Task","Tarea","ColorEstado","FechaInicio","FechaDeadline",
                                        "FechaToDo", "Progreso","Completado","Id_Proyecto","ActiveFechaDeadline",
                                        "ActiveFechaToDo"};

        GLOBAL.TASKS.clear();

        Cursor cursor = database.query(
                TABLA_TASK,
                columnasAConsultar,
                null,
                null,
                null,
                null,
                null
                );

        if(cursor == null){
            //hubo un error, regresa lista vacia
            return tasks;
        }

        if(!cursor.moveToFirst()){
            //no hay datos y la lista se queda vacia
            return tasks;
        }

        do{

            Task x = new Task(
                    cursor.getInt(0),    // Id_Task
                    cursor.getString(1), // Tarea
                    cursor.getInt(2),    // Color estado
                    cursor.getString(3), // Fecha inicio
                    cursor.getString(4), // Fecha deadline
                    cursor.getString(5), // Fecha To do
                    cursor.getInt(6),    // Progreso
                    cursor.getInt(7),    // Completado
                    cursor.getInt(8),    // Id_Proyecto
                    cursor.getInt(9),    // ActiveFechaDeadline
                    cursor.getInt(10)    // ActiveFechaToDo
                    );
            tasks.add(x);
            GLOBAL.TASKS.add(x);


        }while(cursor.moveToNext());

        cursor.close();
        return tasks;
    }

    public ArrayList<Proyecto> obtenerProyectos(){
        ArrayList<Proyecto> proyectos = new ArrayList<>();

        SQLiteDatabase database = ayudanteBaseDeDatos.getReadableDatabase();

        String[] columnasAConsultar = {"Id_Proyecto","nombreProyecto"};
        GLOBAL.PROYECTOS.clear();

        Cursor cursor = database.query(
                TABLA_PROYECTO,
                columnasAConsultar,
                null,
                null,
                null,
                null,
                null
        );

        if(cursor == null){
            return proyectos;
        }
        if(!cursor.moveToFirst()){
            return proyectos;
        }

        do{

            Proyecto x = new Proyecto(
                    cursor.getInt(0),    // Id_Proyecto
                    cursor.getString(1)  // nombreProyecto

            );
            proyectos.add(x);
            GLOBAL.PROYECTOS.add(x);

        }while(cursor.moveToNext());

        return proyectos;
    }

    public ArrayList<Tag> obtenerTags(){
        ArrayList<Tag> Tags = new ArrayList<>();

        SQLiteDatabase database = ayudanteBaseDeDatos.getReadableDatabase();

        String[] columnasAConsultar = {"Id_Tag","nombreTag","Id_Proyecto"};
        GLOBAL.TAGS.clear();

        Cursor cursor = database.query(
                TABLA_TAG,
                columnasAConsultar,
                null,
                null,
                null,
                null,
                null
        );

        if(cursor == null){
            return Tags;
        }
        if(!cursor.moveToFirst()){
            return Tags;
        }

        do{

            Tag x = new Tag(
                    cursor.getInt(0),    // Id_Tag
                    cursor.getString(1), // nombreTag
                    cursor.getInt(2)     // Id_Proyecto

            );
            Tags.add(x);
            GLOBAL.TAGS.add(x);

        }while(cursor.moveToNext());

        return Tags;
    }

    public ArrayList<TagTask> obtenerTagTask(){
        ArrayList<TagTask> tagTasks = new ArrayList<>();

        SQLiteDatabase database = ayudanteBaseDeDatos.getReadableDatabase();

        String[] columnasAConsultar = {"Id_TagTask","Id_Tag","Id_Task"};
        GLOBAL.TAG_TASKS.clear();

        Cursor cursor = database.query(
                TABLA_TAGTASK,
                columnasAConsultar,
                null,
                null,
                null,
                null,
                null
        );

        if(cursor == null){
            return tagTasks;
        }
        if(!cursor.moveToFirst()){
            return tagTasks;
        }

        do{

            TagTask x = new TagTask(
                    cursor.getInt(0),    // Id_TagTask
                    cursor.getInt(1),    // Id_Tag
                    cursor.getInt(2)     // Id_Task

            );
            tagTasks.add(x);
            GLOBAL.TAG_TASKS.add(x);

        }while(cursor.moveToNext());

        return tagTasks;
    }

    public ArrayList<SubTask> obtenerSubTasks(){
        ArrayList<SubTask> subTasks = new ArrayList<>();

        SQLiteDatabase database = ayudanteBaseDeDatos.getReadableDatabase();

        String[] columnasAConsultar = {"Id_SubTask","Subtarea","Completado","Id_Task"};
        GLOBAL.SUB_TASKS.clear();

        Cursor cursor = database.query(
                TABLA_SUBTASK,
                columnasAConsultar,
                null,
                null,
                null,
                null,
                null
        );

        if(cursor == null){
            return subTasks;
        }
        if(!cursor.moveToFirst()){
            return subTasks;
        }

        do{

            SubTask x = new SubTask(
                    cursor.getInt(0),    // Id_SubTask
                    cursor.getString(1), // Subtarea
                    cursor.getInt(2),    // Completado
                    cursor.getInt(3)     // Id_Task
            );
            subTasks.add(x);
            GLOBAL.SUB_TASKS.add(x);

        }while(cursor.moveToNext());

        return subTasks;
    }


    public ArrayList<Imagen> obtenerImagenes(){
        ArrayList<Imagen> imagens = new ArrayList<>();

        SQLiteDatabase database = ayudanteBaseDeDatos.getReadableDatabase();

        String[] columnasAConsultar = {"Id_Imagen","Imagen","Id_Task"};
        GLOBAL.IMAGENS.clear();



        Cursor cursor = database.query(
                    TABLA_IMAGEN,
                    columnasAConsultar,
                    null,
                    null,
                    null,
                    null,
                    null
            );

        if(cursor == null){
            return imagens;
        }
        if(!cursor.moveToFirst()){
            return imagens;
        }

        do{
            String dataImage = cursor.getString(1);
            File imgFile = new File(dataImage);

            //File imgFile = new File("Imagen.png");
            Bitmap realImage = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;


            if(imgFile.exists()){
                realImage = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),options);

            }

            Imagen x = new Imagen(
                    cursor.getInt(0),    // Id_Imagen
                    realImage,                       // Imagen
                    cursor.getInt(2)     // Id_Task

            );
            x.setPath(dataImage);
            imagens.add(x);
            GLOBAL.IMAGENS.add(x);

        }while(cursor.moveToNext());

        /*if(cursor == null){
            return imagens;
        }
        if(!cursor.moveToFirst()){
            return imagens;
        }

        do{
            byte[] dataImage = cursor.getBlob(1);
            Bitmap realImage = getImage(dataImage);
            Imagen x = new Imagen(
                    cursor.getInt(0),    // Id_Imagen
                    realImage,                       // Imagen
                    cursor.getInt(2)     // Id_Task

            );
            imagens.add(x);
            GLOBAL.IMAGENS.add(x);

        }while(cursor.moveToNext());*/



        return imagens;
    }

    public int actualizarTask(Task task){
        SQLiteDatabase database = ayudanteBaseDeDatos.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("Tarea",task.getTarea());
        contentValues.put("ColorEstado",task.getColorEstado());
        contentValues.put("FechaInicio",task.getFechaInicio());
        contentValues.put("FechaDeadline",task.getFechaDeadline());
        contentValues.put("FechaToDo",task.getFechaToDo());
        contentValues.put("Progreso",task.getProgreso());
        contentValues.put("Completado",task.getCompletado());
        contentValues.put("Id_Proyecto",task.getId_Proyecto());
        contentValues.put("ActiveFechaDeadline",task.getActiveFechaDeadline());
        contentValues.put("ActiveFechaToDo",task.getActiveFechaToDo());

        String campoParaActualizar = "Id_Task = ?";
        String[] argumentoParaActualizar = {String.valueOf(task.getId_Task())};
        return database.update(TABLA_TASK, contentValues,campoParaActualizar,argumentoParaActualizar);
    }

    public int actualizarProyecto(Proyecto proyecto){
        SQLiteDatabase database = ayudanteBaseDeDatos.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("nombreProyecto",proyecto.getNombreProyecto());

        String campoParaActualizar = "Id_Proyecto = ?";
        String[] argumentoParaActualizar = {String.valueOf(proyecto.getId_Proyecto())};
        return database.update(TABLA_PROYECTO,contentValues,campoParaActualizar,argumentoParaActualizar);

    }

    public int actualizarImagen(Imagen imagen){
        SQLiteDatabase database = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        byte[] data = getBitmapAsByteArray(imagen.getImagen());

        contentValues.put("Imagen",data);
        //contentValues.put("Id_Task",imagen.getId_Task());

        String campoParaActualizar = "Id_Imagen = ?";
        String[] argumentoParaActualizar = {String.valueOf(imagen.getId_Imagen())};
        return database.update(TABLA_IMAGEN,contentValues,campoParaActualizar,argumentoParaActualizar);
    }

    public int actualizarTag(Tag tag){
        SQLiteDatabase database = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("nombreTag",tag.getNombreTag());
        //contentValues.put("Id_Proyecto",tag.getId_Proyecto());

        String campoParaActualizar = "Id_Tag = ?";
        String[] argumentoParaActualizar = {String.valueOf(tag.getId_Tag())};
        return database.update(TABLA_TAG,contentValues,campoParaActualizar,argumentoParaActualizar);
    }

    public int actualizarTagTask(TagTask tagTask){
        SQLiteDatabase database = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("Id_Tag",tagTask.getId_Tag());
        contentValues.put("Id_Task",tagTask.getId_Task());

        String campoParaActualizar = "Id_TagTask = ?";
        String[] argumentoParaActualizar = {String.valueOf(tagTask.getId_TagTask())};
        return database.update(TABLA_TAGTASK,contentValues,campoParaActualizar,argumentoParaActualizar);
    }

    public int actualizarSubTask(SubTask subTask){
        SQLiteDatabase database = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("SubTarea",subTask.getSubTarea());
        contentValues.put("Completado",subTask.getCompletado());
        //contentValues.put("Id_Task",subTask.getId_Task());

        String campoParaActualizar = "Id_SubTask = ?";
        String[] argumentoParaActualizar = {String.valueOf(subTask.getId_SubTask())};
        return database.update(TABLA_SUBTASK,contentValues,campoParaActualizar,argumentoParaActualizar);
    }

    public int eliminarTask(Task task){
        SQLiteDatabase database = ayudanteBaseDeDatos.getWritableDatabase();
        String campo = "Id_Task = ?";
        String[] argumento = {String.valueOf(task.getId_Task())};
        return database.delete(TABLA_TASK,campo,argumento);
    }

    public int eliminarProyecto(Proyecto proyecto){
        SQLiteDatabase database = ayudanteBaseDeDatos.getWritableDatabase();
        String campo = "Id_Proyecto = ?";
        String[] argumento = {String.valueOf(proyecto.getId_Proyecto())};
        return database.delete(TABLA_PROYECTO,campo,argumento);
    }

    public int eliminarTag(Tag tag){
        SQLiteDatabase database = ayudanteBaseDeDatos.getWritableDatabase();
        String campo = "Id_Tag = ?";
        String[] argumento = {String.valueOf(tag.getId_Tag())};
        return database.delete(TABLA_TAG,campo,argumento);
    }

    public int eliminarTagTask(TagTask tagTask){
        SQLiteDatabase database = ayudanteBaseDeDatos.getWritableDatabase();
        String campo = "Id_TagTask = ?";
        String[] argumento = {String.valueOf(tagTask.getId_TagTask())};
        return database.delete(TABLA_TAGTASK,campo,argumento);
    }

    public int eliminarImagen(Imagen imagen){
        SQLiteDatabase database = ayudanteBaseDeDatos.getWritableDatabase();
        String campo = "Id_Imagen = ?";
        String[] argumento = {String.valueOf(imagen.getId_Imagen())};
        return database.delete(TABLA_IMAGEN,campo,argumento);
    }

    public int eliminarSubTask(SubTask subTask){
        SQLiteDatabase database = ayudanteBaseDeDatos.getWritableDatabase();
        String campo = "Id_SubTask = ?";
        String[] argumento = {String.valueOf(subTask.getId_SubTask())};
        return database.delete(TABLA_SUBTASK,campo,argumento);
    }

    public long getNextAutoIncrementTaskTable() {
        /*
         * From the docs:
         * SQLite keeps track of the largest ROWID using an internal table named "sqlite_sequence".
         * The sqlite_sequence table is created and initialized automatically
         * whenever a normal table that contains an AUTOINCREMENT column is created.
         */
        String sqliteSequenceTableName = "sqlite_sequence";
        /*
         * Relevant columns to retrieve from <code>sqliteSequenceTableName</code>
         */
        String[] columns = {"seq"};
        String selection = "name=?";
        String[] selectionArgs = { TABLA_TASK };
        SQLiteDatabase database = ayudanteBaseDeDatos.getReadableDatabase();

        Cursor cursor = database.query(sqliteSequenceTableName,
                columns, selection, selectionArgs, null, null, null);

        long autoIncrement = 0;

        if (cursor.moveToFirst()) {
            int indexSeq = cursor.getColumnIndex(columns[0]);
            autoIncrement = cursor.getLong(indexSeq);
        }

        cursor.close();

        return autoIncrement + 1;
    }

}
