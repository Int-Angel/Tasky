package com.angel.tasky;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class NewTagDialog  extends AppCompatDialogFragment {

    public interface  NewTagDialogListener{
        void addTag(String tagTitle, int id);
    }

    private EditText tag;
    private NewTagDialogListener listener;
    public int id;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_new_tag, null);
        tag = v.findViewById(R.id.dialog_new_tag);

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
                        String tagTexto = tag.getText().toString();
                        listener.addTag(tagTexto, id);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            listener = (NewTagDialogListener) context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString() + "must implement NewTagDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
