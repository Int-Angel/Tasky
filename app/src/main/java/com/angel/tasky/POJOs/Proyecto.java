package com.angel.tasky.POJOs;

import android.os.Parcel;
import android.os.Parcelable;

public class Proyecto implements Parcelable {
    int Id_Proyecto;
    String nombreProyecto;

    public Proyecto(int id_Proyecto, String nombreProyecto) {
        Id_Proyecto = id_Proyecto;
        this.nombreProyecto = nombreProyecto;
    }

    public Proyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public int getId_Proyecto() {
        return Id_Proyecto;
    }

    public void setId_Proyecto(int id_Proyecto) {
        Id_Proyecto = id_Proyecto;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Id_Proyecto);
        dest.writeString(this.nombreProyecto);
    }

    protected Proyecto(Parcel in) {
        this.Id_Proyecto = in.readInt();
        this.nombreProyecto = in.readString();
    }

    public static final Parcelable.Creator<Proyecto> CREATOR = new Parcelable.Creator<Proyecto>() {
        @Override
        public Proyecto createFromParcel(Parcel source) {
            return new Proyecto(source);
        }

        @Override
        public Proyecto[] newArray(int size) {
            return new Proyecto[size];
        }
    };
}
