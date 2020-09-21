package com.angel.tasky.POJOs;

import android.os.Parcel;
import android.os.Parcelable;

public class SubTask implements Parcelable {
    int Id_SubTask, Completado, Id_Task;
    String SubTarea;

    public SubTask(int id_SubTask, String subTarea,int completado, int id_Task) {
        Id_SubTask = id_SubTask;
        Completado = completado;
        Id_Task = id_Task;
        SubTarea = subTarea;
    }

    public SubTask(String subTarea,int completado,int id_Task) {
        Completado = completado;
        Id_Task = id_Task;
        SubTarea = subTarea;
    }

    public int getId_SubTask() {
        return Id_SubTask;
    }

    public void setId_SubTask(int id_SubTask) {
        Id_SubTask = id_SubTask;
    }

    public int getCompletado() {
        return Completado;
    }

    public void setCompletado(int completado) {
        Completado = completado;
    }

    public int getId_Task() {
        return Id_Task;
    }

    public void setId_Task(int id_Task) {
        Id_Task = id_Task;
    }

    public String getSubTarea() {
        return SubTarea;
    }

    public void setSubTarea(String subTarea) {
        SubTarea = subTarea;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Id_SubTask);
        dest.writeInt(this.Completado);
        dest.writeInt(this.Id_Task);
        dest.writeString(this.SubTarea);
    }

    protected SubTask(Parcel in) {
        this.Id_SubTask = in.readInt();
        this.Completado = in.readInt();
        this.Id_Task = in.readInt();
        this.SubTarea = in.readString();
    }

    public static final Parcelable.Creator<SubTask> CREATOR = new Parcelable.Creator<SubTask>() {
        @Override
        public SubTask createFromParcel(Parcel source) {
            return new SubTask(source);
        }

        @Override
        public SubTask[] newArray(int size) {
            return new SubTask[size];
        }
    };
}
