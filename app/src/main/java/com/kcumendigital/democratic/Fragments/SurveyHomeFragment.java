package com.kcumendigital.democratic.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kcumendigital.democratic.Adapters.SurveyListAdapter;
import com.kcumendigital.democratic.LayoutManagers.GridLayoutManager;
import com.kcumendigital.democratic.Models.Survey;
import com.kcumendigital.democratic.R;
import com.kcumendigital.democratic.SurveyDescriptionActivity;
import com.kcumendigital.democratic.Util.ColletionsStatics;
import com.kcumendigital.democratic.parse.SunshinePageControl;

/**
 * Created by Dario Chamorro on 24/10/2015.
 */
public class SurveyHomeFragment extends Fragment implements SurveyListAdapter.OnItemClickListenerSurvey {

    RecyclerView recyclerView;
    SurveyListAdapter surveyListAdapter;
    SwipeRefreshLayout refreshLayout;

    public SurveyHomeFragment(){}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.container_recycler_view);
        surveyListAdapter = new SurveyListAdapter(this,getActivity(),getChildFragmentManager(),recyclerView);
        recyclerView.setAdapter(surveyListAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh);

        SunshinePageControl control =  new SunshinePageControl(SunshinePageControl.ORDER_DESCENING,recyclerView,refreshLayout, ColletionsStatics.getDataSurvey(),null, Survey.class);
        return v;
    }


    @Override
    public void onItemClick(int position) {

        Toast.makeText(getActivity(),"pos "+position,Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), SurveyDescriptionActivity.class).putExtra("pos", position));
    }

    public void notidyDataChanged (){
        if (surveyListAdapter != null){
            surveyListAdapter.notifyDataSetChanged();
        }
    }


}
