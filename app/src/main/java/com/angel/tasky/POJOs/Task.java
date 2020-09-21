package com.angel.tasky.POJOs;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    int Id_Task, ColorEstado, Progreso, Completado, Id_Proyecto, ActiveFechaDeadline, ActiveFechaToDo;
    String Tarea, FechaInicio, FechaDeadline, FechaToDo;

    public Task(int id_Task, String tarea, int colorEstado, String fechaInicio,
                String fechaDeadline, String fechaToDo, int progreso, int completado,
                int id_Proyecto, int activeFechaDeadline, int activeFechaToDo) {

        Id_Task = id_Task;
        ColorEstado = colorEstado;
        Progreso = progreso;
        Completado = completado;
        Id_Proyecto = id_Proyecto;
        ActiveFechaDeadline = activeFechaDeadline;
        ActiveFechaToDo = activeFechaToDo;
        Tarea = tarea;
        FechaInicio = fechaInicio;
        FechaDeadline = fechaDeadline;
        FechaToDo = fechaToDo;
    }

    public Task(String tarea, int colorEstado, String fechaInicio,
                String fechaDeadline, String fechaToDo, int progreso, int completado,
                int id_Proyecto, int activeFechaDeadline, int activeFechaToDo) {

        ColorEstado = colorEstado;
        Progreso = progreso;
        Completado = completado;
        Id_Proyecto = id_Proyecto;
        ActiveFechaDeadline = activeFechaDeadline;
        ActiveFechaToDo = activeFechaToDo;
        Tarea = tarea;
        FechaInicio = fechaInicio;
        FechaDeadline = fechaDeadline;
        FechaToDo = fechaToDo;
    }

    public Task(String tarea, int colorEstado, String fechaInicio,
                String fechaDeadline, String fechaToDo, int progreso, int completado,
                 int activeFechaDeadline, int activeFechaToDo) {

        ColorEstado = colorEstado;
        Progreso = progreso;
        Completado = completado;

        ActiveFechaDeadline = activeFechaDeadline;
        ActiveFechaToDo = activeFechaToDo;
        Tarea = tarea;
        FechaInicio = fechaInicio;
        FechaDeadline = fechaDeadline;
        FechaToDo = fechaToDo;
    }


    public int getId_Task() {
        return Id_Task;
    }

    public void setId_Task(int id_Task) {
        Id_Task = id_Task;
    }

    public int getColorEstado() {
        return ColorEstado;
    }

    public void setColorEstado(int colorEstado) {
        ColorEstado = colorEstado;
    }

    public int getProgreso() {
        return Progreso;
    }

    public void setProgreso(int progreso) {
        Progreso = progreso;
    }

    public int getCompletado() {
        return Completado;
    }

    public void setCompletado(int completado) {
        Completado = completado;
    }

    public int getId_Proyecto() {
        return Id_Proyecto;
    }

    public void setId_Proyecto(int id_Proyecto) {
        Id_Proyecto = id_Proyecto;
    }

    public int getActiveFechaDeadline() {
        return ActiveFechaDeadline;
    }

    public void setActiveFechaDeadline(int activeFechaDeadline) {
        ActiveFechaDeadline = activeFechaDeadline;
    }

    public int getActiveFechaToDo() {
        return ActiveFechaToDo;
    }

    public void setActiveFechaToDo(int activeFechaToDo) {
        ActiveFechaToDo = activeFechaToDo;
    }

    public String getTarea() {
        return Tarea;
    }

    public void setTarea(String tarea) {
        Tarea = tarea;
    }

    public String getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        FechaInicio = fechaInicio;
    }

    public String getFechaDeadline() {
        return FechaDeadline;
    }

    public void setFechaDeadline(String fechaDeadline) {
        FechaDeadline = fechaDeadline;
    }

    public String getFechaToDo() {
        return FechaToDo;
    }

    public void setFechaToDo(String fechaToDo) {
        FechaToDo = fechaToDo;
    }

    public void copyTask(Task x){
        Id_Task = x.Id_Task;
        ColorEstado = x.ColorEstado;
        Progreso = x.Progreso;
        Completado = x.Completado;
        Id_Proyecto = x.Id_Proyecto;
        ActiveFechaDeadline = x.ActiveFechaDeadline;
        ActiveFechaToDo = x.ActiveFechaToDo;
        Tarea = x.Tarea;
        FechaInicio = x.FechaInicio;
        FechaDeadline = x.FechaDeadline;
        FechaToDo = x.FechaToDo;
    }
    public void clear(){
        Task a =new Task(null,-1,null,null,null,0,0,0,0);
        copyTask(a);

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Id_Task);
        dest.writeInt(this.ColorEstado);
        dest.writeInt(this.Progreso);
        dest.writeInt(this.Completado);
        dest.writeInt(this.Id_Proyecto);
        dest.writeInt(this.ActiveFechaDeadline);
        dest.writeInt(this.ActiveFechaToDo);
        dest.writeString(this.Tarea);
        dest.writeString(this.FechaInicio);
        dest.writeString(this.FechaDeadline);
        dest.writeString(this.FechaToDo);
    }

    protected Task(Parcel in) {
        this.Id_Task = in.readInt();
        this.ColorEstado = in.readInt();
        this.Progreso = in.readInt();
        this.Completado = in.readInt();
        this.Id_Proyecto = in.readInt();
        this.ActiveFechaDeadline = in.readInt();
        this.ActiveFechaToDo = in.readInt();
        this.Tarea = in.readString();
        this.FechaInicio = in.readString();
        this.FechaDeadline = in.readString();
        this.FechaToDo = in.readString();
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
