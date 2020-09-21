package com.angel.tasky;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AyudanteBaseDeDatos extends SQLiteOpenHelper {

    private static final String NOMBRE_BASE_DE_DATOS = "TaskDB";
    private static final int VERSION_BASE_DE_DATOS = 1;

    private static final String TABLA_TASK = "Task"
            , TABLA_SUBTASK = "SubTask"
            , TABLA_IMAGEN = "Imagen"
            , TABLA_PROYECTO = "Proyecto"
            , TABLA_TAG = "Tag"
            , TABLA_TAGTASK = "TagTask";

    public AyudanteBaseDeDatos(Context context) {
        super(context, NOMBRE_BASE_DE_DATOS, null, VERSION_BASE_DE_DATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(Id_Proyecto INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombreProyecto TEXT)",TABLA_PROYECTO));

        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(Id_Tag INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombreTag TEXT, Id_Proyecto INT," +
                "FOREIGN KEY(Id_Proyecto) REFERENCES Proyecto(Id_Proyecto) ON DELETE CASCADE)",TABLA_TAG));

        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(Id_Task INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Tarea TEXT, ColorEstado INT, FechaInicio TEXT, FechaDeadline TEXT, FechaToDo TEXT, Progreso INT," +
                "Completado INT, ActiveFechaDeadline INT, ActiveFechaToDo INT, Id_Proyecto INT, " +
                "FOREIGN KEY(Id_Proyecto) REFERENCES Proyecto(Id_Proyecto) ON DELETE CASCADE)",TABLA_TASK));

        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(Id_TagTask INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Id_Tag INT, Id_Task INT," +
                "FOREIGN KEY(Id_Tag) REFERENCES Tag(Id_Tag) ON DELETE CASCADE," +
                "FOREIGN KEY(Id_Task) REFERENCES Task(Id_Task) ON DELETE CASCADE)",TABLA_TAGTASK));

        /*db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(Id_Imagen INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Imagen BLOB, Id_Task INT," +
                "FOREIGN KEY(Id_Task) REFERENCES Task(Id_Task) ON DELETE CASCADE)",TABLA_IMAGEN));*/

        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(Id_Imagen INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Imagen TEXT, Id_Task INT," +
                "FOREIGN KEY(Id_Task) REFERENCES Task(Id_Task) ON DELETE CASCADE)",TABLA_IMAGEN));

        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(Id_SubTask INTEGER PRIMARY KEY AUTOINCREMENT," +
                "SubTarea TEXT, Completado INT, Id_Task INT," +
                "FOREIGN KEY(Id_Task) REFERENCES Task(Id_Task) ON DELETE CASCADE)",TABLA_SUBTASK));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
