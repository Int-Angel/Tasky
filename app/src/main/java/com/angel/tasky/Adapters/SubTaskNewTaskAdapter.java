package com.angel.tasky.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.angel.tasky.GLOBALES.GLOBAL;
import com.angel.tasky.POJOs.SubTask;
import com.angel.tasky.R;

import java.util.ArrayList;

public class SubTaskNewTaskAdapter extends RecyclerView.Adapter<SubTaskNewTaskAdapter.SubTaskActivity> {
    Context context;

    public SubTaskNewTaskAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public SubTaskActivity onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.item_subtask,null);
        SubTaskActivity subTaskActivity = new SubTaskActivity(view);
        return subTaskActivity;
    }

    @Override
    public void onBindViewHolder(@NonNull final SubTaskActivity subTaskActivity, int i) {
        final int indice = i;

        subTaskActivity.subtask.setClickable(true);
        subTaskActivity.subtask.setFocusable(true);
        subTaskActivity.subtask.setFocusableInTouchMode(true);

        subTaskActivity.subtask.setText(GLOBAL.SUB_TASKS_TEMP.get(indice).getSubTarea());

        subTaskActivity.subtask.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                try {
                    GLOBAL.SUB_TASKS_TEMP.get(indice).setSubTarea(subTaskActivity.subtask.getText().toString());
                }catch (Exception e){

                }
            }
        });

        subTaskActivity.subtask.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

               /* if (keyEvent.getAction() == KeyEvent.ACTION_DOWN )
                {


                    switch (keyCode)
                    {

                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            GLOBAL.SUB_TASKS_TEMP.get(indice).setSubTarea(subTaskActivity.subtask.getText().toString());
                            notifyDataSetChanged();
                            return true;
                        default:
                            break;
                    }
                }*/

               if(keyCode == 66){
                   GLOBAL.SUB_TASKS_TEMP.get(indice).setSubTarea(subTaskActivity.subtask.getText().toString());
               }


                return false;
            }
        });

        subTaskActivity.deleteSubTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GLOBAL.SUB_TASKS_TEMP.remove(GLOBAL.SUB_TASKS_TEMP.get(indice));
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return GLOBAL.SUB_TASKS_TEMP.size();
    }

    public class SubTaskActivity extends RecyclerView.ViewHolder {
        RelativeLayout contenedor;
        EditText subtask;
        CheckBox completed;
        ImageButton deleteSubTask;
        public SubTaskActivity(@NonNull View itemView) {
            super(itemView);

            contenedor = itemView.findViewById(R.id.SubTask_container);
            subtask = itemView.findViewById(R.id.subtarea_1);
            completed = itemView.findViewById(R.id.checkbox_subtask);
            deleteSubTask = itemView.findViewById(R.id.delete_subtask);

        }
    }
}
