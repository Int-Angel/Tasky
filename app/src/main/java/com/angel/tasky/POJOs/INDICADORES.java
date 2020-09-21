package com.angel.tasky.POJOs;

import android.support.v4.app.Fragment;

public class INDICADORES {
    int fragment_open;
    Boolean usedZone;
    int pos_tag_edit;
    int pos_proyecto_edit;
    int pos_selected_proy;

    public INDICADORES(){
        fragment_open = 0;
        usedZone = false;
        pos_tag_edit = -1;
        pos_proyecto_edit = -1;
        pos_proyecto_edit = -1;
    }

    public int getFragment_open() {
        return fragment_open;
    }

    public void setFragment_open(int fragment_open) {
        this.fragment_open = fragment_open;
    }

    public Boolean getUsedZone() {
        return usedZone;
    }

    public void setUsedZone(Boolean usedZone) {
        this.usedZone = usedZone;
    }

    public int getPos_tag_edit() {
        return pos_tag_edit;
    }

    public void setPos_tag_edit(int pos_tag_edit) {
        this.pos_tag_edit = pos_tag_edit;
    }

    public int getPos_proyecto_edit() {
        return pos_proyecto_edit;
    }

    public void setPos_proyecto_edit(int pos_proyecto_edit) {
        this.pos_proyecto_edit = pos_proyecto_edit;
    }

    public int getPos_selected_proy() {
        return pos_selected_proy;
    }

    public void setPos_selected_proy(int pos_selected_proy) {
        this.pos_selected_proy = pos_selected_proy;
    }
}
