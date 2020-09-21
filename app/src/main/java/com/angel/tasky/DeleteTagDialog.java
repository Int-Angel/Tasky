package com.angel.tasky;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.angel.tasky.DeleteProjectDialog;
import com.angel.tasky.R;

public class DeleteTagDialog extends AppCompatDialogFragment {

    public interface  DeleteTagDialogListener{
        void deleteTag(int x);
    }


    private DeleteTagDialogListener listener;
    public int pos;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_delete_tag, null);


        builder.setView(v)
                .setTitle("")
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.deleteTag(pos);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            listener = (DeleteTagDialogListener) context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString() + "must implement NewProjectDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}