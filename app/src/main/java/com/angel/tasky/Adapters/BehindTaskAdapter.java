package com.angel.tasky.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.angel.tasky.GLOBALES.GLOBAL;
import com.angel.tasky.R;

public class BehindTaskAdapter extends RecyclerView.Adapter<BehindTaskAdapter.BehindTaskActivity> {
    Context context;

    public BehindTaskAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public BehindTaskActivity onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.item_behind_task,null);
        BehindTaskActivity behindTaskActivity = new BehindTaskActivity(view);
        return behindTaskActivity;
    }

    @Override
    public void onBindViewHolder(@NonNull BehindTaskActivity behindTaskActivity, int i) {

    }

    @Override
    public int getItemCount() {
        return GLOBAL.TASKS.size();
    }

    public class BehindTaskActivity extends RecyclerView.ViewHolder {
        public BehindTaskActivity(@NonNull View itemView) {
            super(itemView);
        }
    }
}
