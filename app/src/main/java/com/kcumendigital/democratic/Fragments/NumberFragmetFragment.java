package com.kcumendigital.democratic.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kcumendigital.democratic.Adapters.PagerAdpater;
import com.kcumendigital.democratic.Models.Discussion;
import com.kcumendigital.democratic.Models.Survey;
import com.kcumendigital.democratic.R;
import com.kcumendigital.democratic.Util.ColletionsStatics;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumberFragmetFragment extends android.support.v4.app.Fragment {
    int position;
    Discussion discussion;
    Survey survey;
    int TYPE_lIST;

    public void init (int position, int TYPE_LIST){
        this.position = position;
        this.TYPE_lIST = TYPE_LIST;
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
        if (TYPE_lIST == PagerAdpater.TYPE_DISCUSION) {
            discussion = ColletionsStatics.getDataDiscusion().get(position);
            txt.setText(discussion.getTitle());
            ImageView img = (ImageView) v.findViewById(R.id.img);
            if (position == 0) {
                Picasso.with(getActivity()).load(R.drawable.dots).into(img);
            }

            if (position == 1) {
                Picasso.with(getActivity()).load(R.drawable.dots2).into(img);
            }
        }

        if (TYPE_lIST == PagerAdpater.TYPE_SURVEY){
            survey = ColletionsStatics.getDataSurvey().get(position);
            txt.setText(survey.getTitle());
            ImageView img = (ImageView) v.findViewById(R.id.img);
            if (position == 0) {
                Picasso.with(getActivity()).load(R.drawable.dots).into(img);
            }

            if (position == 1) {
                Picasso.with(getActivity()).load(R.drawable.dots2).into(img);
            }
        }
        return v;
    }


}
