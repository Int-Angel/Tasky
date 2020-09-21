package com.angel.tasky.POJOs;

public class TagTask {
    int Id_TagTask, Id_Tag, Id_Task;

    public TagTask(int id_TagTask, int id_Tag, int id_Task) {
        Id_TagTask = id_TagTask;
        Id_Tag = id_Tag;
        Id_Task = id_Task;
    }

    public TagTask(int id_Tag, int id_Task) {
        Id_Tag = id_Tag;
        Id_Task = id_Task;
    }

    public int getId_TagTask() {
        return Id_TagTask;
    }

    public void setId_TagTask(int id_TagTask) {
        Id_TagTask = id_TagTask;
    }

    public int getId_Tag() {
        return Id_Tag;
    }

    public void setId_Tag(int id_Tag) {
        Id_Tag = id_Tag;
    }

    public int getId_Task() {
        return Id_Task;
    }

    public void setId_Task(int id_Task) {
        Id_Task = id_Task;
    }
}
