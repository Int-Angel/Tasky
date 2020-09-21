package com.angel.tasky.POJOs;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Imagen implements Parcelable {
    int Id_Imagen, Id_Task;
    Bitmap Imagen;
    String path;

    public Imagen(int id_Imagen,Bitmap imagen, int id_Task) {
        Id_Imagen = id_Imagen;
        Id_Task = id_Task;
        Imagen = imagen;
    }

    public Imagen(Bitmap imagen, int id_Task) {
        Id_Task = id_Task;
        Imagen = imagen;
    }

    public int getId_Imagen() {
        return Id_Imagen;
    }

    public void setId_Imagen(int id_Imagen) {
        Id_Imagen = id_Imagen;
    }

    public int getId_Task() {
        return Id_Task;
    }

    public void setId_Task(int id_Task) {
        Id_Task = id_Task;
    }

    public Bitmap getImagen() {
        return Imagen;
    }

    public void setImagen(Bitmap imagen) {
        Imagen = imagen;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Id_Imagen);
        dest.writeInt(this.Id_Task);
        dest.writeParcelable(this.Imagen, flags);
        dest.writeString(this.path);
    }

    protected Imagen(Parcel in) {
        this.Id_Imagen = in.readInt();
        this.Id_Task = in.readInt();
        this.Imagen = in.readParcelable(Bitmap.class.getClassLoader());
        this.path = in.readString();
    }

    public static final Parcelable.Creator<Imagen> CREATOR = new Parcelable.Creator<Imagen>() {
        @Override
        public Imagen createFromParcel(Parcel source) {
            return new Imagen(source);
        }

        @Override
        public Imagen[] newArray(int size) {
            return new Imagen[size];
        }
    };
}
