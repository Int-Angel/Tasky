package com.angel.tasky.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.angel.tasky.GLOBALES.GLOBAL;
import com.angel.tasky.R;

public class SubTaskEditModeAdapter extends RecyclerView.Adapter<SubTaskEditModeAdapter.SubTaskEditModeActivity> {
    Context context;
    int idTask;

    public interface SubTaskEditModeAdapterListener{



    }
    SubTaskEditModeAdapterListener listener;

    public SubTaskEditModeAdapter(Context context, int idTask, SubTaskEditModeAdapterListener listener){
        this.context = context;
        this.idTask = idTask;
        this.listener = listener;

    }

    @NonNull
    @Override
    public SubTaskEditModeActivity onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.item_subtask,null);
        SubTaskEditModeActivity subTaskEditModeActivity = new SubTaskEditModeActivity(view);
        return subTaskEditModeActivity;
    }

    @Override
    public void onBindViewHolder(@NonNull SubTaskEditModeActivity subTaskEditModeActivity, int i) {

    }

    @Override
    public int getItemCount() {
        return GLOBAL.SUB_TASKS.size();
    }

    public class SubTaskEditModeActivity extends RecyclerView.ViewHolder {
        public SubTaskEditModeActivity(@NonNull View itemView) {
            super(itemView);
        }
    }
}
