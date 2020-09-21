package com.angel.tasky.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.angel.tasky.GLOBALES.GLOBAL;
import com.angel.tasky.POJOs.Tag;
import com.angel.tasky.R;

public class SelectedTagsAdapter extends RecyclerView.Adapter<SelectedTagsAdapter.SelectedTagActivity> {
    Context context;

    public interface SelectedTagsAdapterListener{
        void QuitarSelectedTag(Tag x);
    }

    SelectedTagsAdapterListener listener;

    public SelectedTagsAdapter (Context context, SelectedTagsAdapterListener listener){
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SelectedTagActivity onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context,R.layout.item_selected_tag,null);
        SelectedTagActivity selectedTagActivity = new SelectedTagActivity(view);
        return selectedTagActivity;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedTagActivity selectedTagActivity, int i) {
        final int indice = i;

        selectedTagActivity.cant.setVisibility(View.GONE);

        selectedTagActivity.tag.setText("  "+GLOBAL.SELECTED_TAGS.get(indice).getNombreTag());

        selectedTagActivity.quitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.QuitarSelectedTag(GLOBAL.SELECTED_TAGS.get(indice));
            }
        });
    }

    @Override
    public int getItemCount() {
        return GLOBAL.SELECTED_TAGS.size();
    }

    public class SelectedTagActivity extends RecyclerView.ViewHolder {
        TextView tag, cant;
        ImageButton quitar;
        public SelectedTagActivity(@NonNull View itemView) {
            super(itemView);

            tag = itemView.findViewById(R.id.selected_tag_text);
            quitar = itemView.findViewById(R.id.remove_selected_tag);
            cant = itemView.findViewById(R.id.selected_tag_number);
        }
    }
}
