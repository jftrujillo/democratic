package com.kcumendigital.democraticcauca.Fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kcumendigital.democraticcauca.Adapters.PagerAdpater;
import com.kcumendigital.democraticcauca.ForumsActivity;
import com.kcumendigital.democraticcauca.Models.Discussion;
import com.kcumendigital.democraticcauca.Models.Survey;
import com.kcumendigital.democraticcauca.R;
import com.kcumendigital.democraticcauca.SurveyDescriptionActivity;
import com.kcumendigital.democraticcauca.Util.ColletionsStatics;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumberFragmetFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
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
        v.setOnClickListener(this);
        TextView txt = (TextView) v.findViewById(R.id.numberPage);
        ImageView img_bacground = (ImageView) v.findViewById(R.id.bacground_pager);
        if (TYPE_lIST == PagerAdpater.TYPE_DISCUSION) {
            discussion = ColletionsStatics.getHomeDiscusion().get(position);
            txt.setText(discussion.getTitle());
            String categoria = discussion.getCategory();

            if (categoria.equals("Salud")){

                Picasso.with(getActivity()).load(R.drawable.screensalud).into(img_bacground);

            }

            if (categoria.equals("Gobierno")){
                Picasso .with(getActivity()).load(R.drawable.screengobierno).into(img_bacground);
            }

            if (categoria.equals("Educación")){

                Picasso.with(getActivity()).load(R.drawable.screeneducacion).into(img_bacground);
            }

            if (categoria.equals("Medio Ambiente")){
                Picasso.with(getActivity()).load(R.drawable.screenmedio).into(img_bacground);
            }
        }

        if (TYPE_lIST == PagerAdpater.TYPE_SURVEY){
            survey = ColletionsStatics.getHomeSurvey().get(position);
            txt.setText(survey.getTitle());
            String categoria = survey.getCategory();
            if (categoria.equals("Salud")){
                Picasso.with(getActivity()).load(R.drawable.screensalud).into(img_bacground);
            }

            if (categoria.equals("Gobierno")){
                Picasso.with(getActivity()).load(R.drawable.screengobierno).into(img_bacground);
            }

            if (categoria.equals("Educación")){
                Picasso.with(getActivity()).load(R.drawable.screenmedio).into(img_bacground);
            }

            if (categoria.equals("Medio Ambiente")){
                Picasso.with(getActivity()).load(R.drawable.bg_ciudad).into(img_bacground);
            }

        }
        return v;
    }


    @Override
    public void onClick(View v) {
        Intent  intent =  null;
        if(TYPE_lIST == PagerAdpater.TYPE_SURVEY)
            intent = new Intent(getActivity(), SurveyDescriptionActivity.class);
        else
            intent = new Intent(getActivity(), ForumsActivity.class);

        intent.putExtra("pos", position);
        intent.putExtra("pager",true);
        startActivity(intent);

    }
}
