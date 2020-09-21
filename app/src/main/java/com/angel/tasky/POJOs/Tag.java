package com.angel.tasky.POJOs;

import android.os.Parcel;
import android.os.Parcelable;

public class Tag implements Parcelable {
    int Id_Tag, Id_Proyecto;
    String nombreTag;

    public Tag(int id_Tag,String nombreTag ,int id_Proyecto) {
        Id_Tag = id_Tag;
        Id_Proyecto = id_Proyecto;
        this.nombreTag = nombreTag;
    }

    public Tag(int id_Proyecto, String nombreTag) {
        Id_Proyecto = id_Proyecto;
        this.nombreTag = nombreTag;
    }

    public int getId_Tag() {
        return Id_Tag;
    }

    public void setId_Tag(int id_Tag) {
        Id_Tag = id_Tag;
    }

    public int getId_Proyecto() {
        return Id_Proyecto;
    }

    public void setId_Proyecto(int id_Proyecto) {
        Id_Proyecto = id_Proyecto;
    }

    public String getNombreTag() {
        return nombreTag;
    }

    public void setNombreTag(String nombreTag) {
        this.nombreTag = nombreTag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Id_Tag);
        dest.writeInt(this.Id_Proyecto);
        dest.writeString(this.nombreTag);
    }

    protected Tag(Parcel in) {
        this.Id_Tag = in.readInt();
        this.Id_Proyecto = in.readInt();
        this.nombreTag = in.readString();
    }

    public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel source) {
            return new Tag(source);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };
}
