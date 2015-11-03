package com.kcumendigital.democratic.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kcumendigital.democratic.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumberFragmetFragment extends android.support.v4.app.Fragment {
    int position;
    public void init (int position){
        this.position = position;

    }


    public NumberFragmetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_number_fragmet, container, false);
        TextView txt = (TextView) v.findViewById(R.id.numberPage);
        txt.setText("323");
        ImageView img = (ImageView) v.findViewById(R.id.img);
        if (position == 0 ){
            Picasso.with(getActivity()).load(R.drawable.dots).into(img);
        }

        if (position == 1){
            Picasso.with(getActivity()).load(R.drawable.dots2).into(img);
        }
        return v;
    }


}
