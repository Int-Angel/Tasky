package com.angel.tasky;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FullCalendarPro extends LinearLayout {
    GridView grid;
    ImageButton siguiente, pasado;
    LinearLayout header;
    TextView mesYaño;

    public FullCalendarPro(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.fullcalendarpro,this);

        grid = findViewById(R.id.fullCalendarPro_grid);
        siguiente = findViewById(R.id.fullCalendarPro_siguiente_mes);
        pasado = findViewById(R.id.fullCalendarPro_mes_anterior);
        header = findViewById(R.id.fullCalendarPro_header);
        mesYaño = findViewById(R.id.fullCalendarPro_mesYaño);

    }

    private void updateCalendar(){
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_MONTH,1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) -1;

        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        /*while (cells.size()< DAYS_COUNT){
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }*/

        //((Ca))

    }
}
