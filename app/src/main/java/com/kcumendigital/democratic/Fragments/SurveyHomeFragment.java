package com.kcumendigital.democratic.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kcumendigital.democratic.Adapters.HomeListAdapter;
import com.kcumendigital.democratic.Adapters.PagerAdpater;
import com.kcumendigital.democratic.Adapters.SurveyListAdapter;
import com.kcumendigital.democratic.LayoutManagers.GridLayoutManager;
import com.kcumendigital.democratic.R;
import com.kcumendigital.democratic.SurveyDescriptionActivity;

/**
 * Created by Dario Chamorro on 24/10/2015.
 */
public class SurveyHomeFragment extends Fragment implements SurveyListAdapter.OnItemClickListenerSurvey {
    RecyclerView recyclerView;
    ViewPager pager;
    PagerAdpater pagerAdpater;
    SurveyListAdapter surveyListAdapter;

    public SurveyHomeFragment(){



    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.container_recycler_view);
        surveyListAdapter = new SurveyListAdapter(this,getActivity(),getChildFragmentManager(),recyclerView);
        recyclerView.setAdapter(surveyListAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        return v;
    }


    @Override
    public void onItemClick(int position) {

        Toast.makeText(getActivity(),"pos "+position,Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), SurveyDescriptionActivity.class).putExtra("pos",position));


    }

    public void notifyDataset(){
        surveyListAdapter.notifyDataSetChanged();
    }
}
