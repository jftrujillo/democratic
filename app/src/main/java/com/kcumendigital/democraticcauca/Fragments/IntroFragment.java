package com.kcumendigital.democraticcauca.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kcumendigital.democraticcauca.R;
import com.kcumendigital.democraticcauca.Util.StringsIntro;
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
        TextView text = (TextView) v.findViewById(R.id.text_intro2);
        ImageView img = (ImageView) v.findViewById(R.id.bacground_intro);
        if (pos == 0){
            Picasso.with(getActivity()).load(R.drawable.intro1).into(img);
            txt.setText(StringsIntro.INTRO_1);
            text.setText(StringsIntro.INTRO_N);
        }

        if (pos == 1){
            Picasso.with(getActivity()).load(R.drawable.intro2).into(img);
            txt.setText(StringsIntro.INTRO_3);
            text.setText(StringsIntro.INTRO_N3);

        }

        if (pos == 3){
            Picasso.with(getActivity()).load(R.drawable.intro3).into(img);
            txt.setText(StringsIntro.INTRO_2);
            text.setText(StringsIntro.INTRO_N2);
        }

        if (pos == 4){
            Picasso.with(getActivity()).load(R.drawable.intro4).into(img);
            txt.setText(StringsIntro.INTRO_4);
            text.setText(StringsIntro.INTRO_N4);
        }
        return  v;
    }



}
