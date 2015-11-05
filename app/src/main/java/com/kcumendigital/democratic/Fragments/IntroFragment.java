package com.kcumendigital.democratic.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kcumendigital.democratic.R;
import com.kcumendigital.democratic.Util.StringsIntro;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFragment extends android.support.v4.app.Fragment {
    int pos;
    public void init (int pos){
        this.pos = pos;
    }

    public IntroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_intro_custom, container, false);
        TextView txt = (TextView) v.findViewById(R.id.text_intro);
        ImageView img = (ImageView) v.findViewById(R.id.bacground_intro);
        if (pos == 0){
            Picasso.with(getActivity()).load(R.drawable.intro1).into(img);
            txt.setText(StringsIntro.INTRO_1);
        }

        if (pos == 1){
            Picasso.with(getActivity()).load(R.drawable.intro2).into(img);
            txt.setText(StringsIntro.INTRO_2);
        }
        return  v;
    }


}
