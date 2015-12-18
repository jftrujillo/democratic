package com.kcumendigital.democraticcauca.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kcumendigital.democraticcauca.R;
import com.kcumendigital.democraticcauca.Util.StringsIntro;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends android.support.v4.app.Fragment {


    public WelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_welcome_intro, container, false);

        return v;
    }


}
