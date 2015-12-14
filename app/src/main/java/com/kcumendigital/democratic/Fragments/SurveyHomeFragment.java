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
import com.kcumendigital.democratic.Models.Discussion;
import com.kcumendigital.democratic.Models.Survey;
import com.kcumendigital.democratic.R;
import com.kcumendigital.democratic.SurveyDescriptionActivity;
import com.kcumendigital.democratic.Util.ColletionsStatics;
import com.kcumendigital.democratic.parse.SunshinePageControl;
import com.kcumendigital.democratic.parse.SunshineParse;
import com.kcumendigital.democratic.parse.SunshineQuery;
import com.kcumendigital.democratic.parse.SunshineRecord;
import com.parse.ParseException;

import java.util.List;

/**
 * Created by Dario Chamorro on 24/10/2015.
 */
public class SurveyHomeFragment extends Fragment implements SurveyListAdapter.OnItemClickListenerSurvey, SunshineParse.SunshineCallback {

    RecyclerView recyclerView;
    SurveyListAdapter surveyListAdapter;
    SwipeRefreshLayout refreshLayout;

    SunshinePageControl control;

    public SurveyHomeFragment(){

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.container_recycler_view);
        surveyListAdapter = new SurveyListAdapter(this,getActivity(),getChildFragmentManager(),recyclerView);
        surveyListAdapter.setPagerEnabled(true);
        recyclerView.setAdapter(surveyListAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh);

        control =  new SunshinePageControl(SunshinePageControl.ORDER_DESCENING,recyclerView,refreshLayout, ColletionsStatics.getDataSurvey(),null, Survey.class);
        if(ColletionsStatics.getDataSurvey().size()==0) {
            control.nextPage();
            SunshineParse parse = new SunshineParse();
            SunshineQuery query =  new SunshineQuery();
            query.setLimit(3);
            query.setOrderDescending("likes");
            parse.getAllRecords(query,this,null,Survey.class);
        }
        return v;
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(getActivity(),"pos "+position,Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), SurveyDescriptionActivity.class).putExtra("pos", position));
    }

    public void nextPage(){
        if(control!=null)
            control.nextPage();
    }

    public void reloadWithQuery(SunshineQuery query){
        if(control!=null) {
            if(query==null)
                surveyListAdapter.setPagerEnabled(true);
            else
                surveyListAdapter.setPagerEnabled(false);
            control.reloadWithQuery(query);
        }
    }

    public void notifyDataChanged (){
        if (surveyListAdapter != null){
            surveyListAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void done(boolean success, ParseException e) {

    }

    @Override
    public void resultRecord(boolean success, SunshineRecord record, ParseException e) {

    }

    @Override
    public void resultListRecords(boolean success, Integer requestCode, List<SunshineRecord> records, ParseException e) {
        for(SunshineRecord record : records) {
            ColletionsStatics.getHomeSurvey().add((Survey) record);
        }
        notifyDataChanged();
    }
}
