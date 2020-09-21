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
import com.angel.tasky.POJOs.Task;
import com.angel.tasky.R;

public class SubTaskInfoAdapter extends RecyclerView.Adapter<SubTaskInfoAdapter.SubTaskActivity> {
    Context context;
    Task tarea;
    int idTask;
    int total;
    int checados;
    public interface SubTaskInfoAdapterListener{
        void updateProgress(int checked, int total, int pos);
    }
    SubTaskInfoAdapterListener listener;

    public SubTaskInfoAdapter(Context context,int idTask, SubTaskInfoAdapterListener listener){
        this.context = context;
        this.idTask = idTask;
        this.listener = listener;
        total = countSubTasks();
        checados = 0;

    }

    @NonNull
    @Override
    public SubTaskActivity onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.item_subtask,null);
        SubTaskActivity subTaskActivity = new SubTaskActivity(view);

        return subTaskActivity;
    }

    @Override
    public void onBindViewHolder(@NonNull final SubTaskActivity subTaskActivity, final int i) {
        final int indice = i;

       if(GLOBAL.SUB_TASKS.get(indice).getId_Task() == idTask){
           subTaskActivity.subtask.setText(GLOBAL.SUB_TASKS.get(indice).getSubTarea());
           subTaskActivity.completed.setVisibility(View.VISIBLE);
           subTaskActivity.deleteSubTask.setVisibility(View.GONE);

           if (GLOBAL.SUB_TASKS.get(indice).getCompletado() == 1){
               subTaskActivity.completed.setChecked(true);
               checados++;
           }else{
               subTaskActivity.completed.setChecked(false);

           }

           subTaskActivity.completed.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if(subTaskActivity.completed.isChecked()){
                       GLOBAL.SUB_TASKS.get(indice).setCompletado(1);
                       checados++;
                   }else{
                       GLOBAL.SUB_TASKS.get(indice).setCompletado(0);
                       if(checados>0){
                           checados--;
                       }
                   }
                   listener.updateProgress(checados,total,indice);
               }
           });

           subTaskActivity.deleteSubTask.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   GLOBAL.SUB_TASKS.remove(GLOBAL.SUB_TASKS.get(indice));
                   notifyDataSetChanged();
               }
           });
       }else{
           subTaskActivity.contenedor.setVisibility(View.GONE);
       }

    }

    public String textDetalles(){
        String text = "";
        for (SubTask subTask : GLOBAL.SUB_TASKS){
            if(subTask.getId_Task() == idTask){
                text = text + "   - "+subTask.getSubTarea()+"\n";
            }
        }
        return text;
    }

    public boolean isThereSubTasks(){
        for (SubTask subTask : GLOBAL.SUB_TASKS){
            if(subTask.getId_Task() == idTask){
                return true;
            }
        }
        return false;
    }

    public int countSubTasks(){
        int n =0;
        for (SubTask subTask : GLOBAL.SUB_TASKS){
            if(subTask.getId_Task() == idTask){
                n++;
            }
        }
        return n;
    }


    @Override
    public int getItemCount() {
        return GLOBAL.SUB_TASKS.size();
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
