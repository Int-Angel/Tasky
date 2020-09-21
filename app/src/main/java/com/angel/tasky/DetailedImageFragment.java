package com.angel.tasky;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.angel.tasky.GLOBALES.GLOBAL;
import com.github.chrisbanes.photoview.PhotoView;

public class DetailedImageFragment extends Fragment {
    public int imagePos;

    public interface DetailedImageFragmentListener{
        void CerrarDetailedImage();
    }

    DetailedImageFragmentListener listener;

    ImageButton back;
    //ImageView imagen;
    PhotoView imagen;
    RelativeLayout background;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_detailed_image,null);

        back = view.findViewById(R.id.image_detailed_back);
        imagen = view.findViewById(R.id.detailed_image);
        background = view.findViewById(R.id.detailed_image_background);

        imagen.setImageBitmap(GLOBAL.IMAGENS.get(imagePos).getImagen());
        //imagen.setImageResource(R.drawable.circulo_rojo);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.CerrarDetailedImage();
            }
        });

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DetailedImageFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
