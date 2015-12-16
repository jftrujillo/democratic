package com.kcumendigital.democraticcauca.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kcumendigital.democraticcauca.Adapters.HomeListAdapter;
import com.kcumendigital.democraticcauca.ForumsActivity;
import com.kcumendigital.democraticcauca.LayoutManagers.GridLayoutManager;
import com.kcumendigital.democraticcauca.Models.Discussion;
import com.kcumendigital.democraticcauca.R;
import com.kcumendigital.democraticcauca.Util.ColletionsStatics;
import com.kcumendigital.democraticcauca.parse.SunshinePageControl;
import com.kcumendigital.democraticcauca.parse.SunshineParse;
import com.kcumendigital.democraticcauca.parse.SunshineQuery;
import com.kcumendigital.democraticcauca.parse.SunshineRecord;
import com.parse.ParseException;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class DiscussionHomeFragment extends Fragment implements HomeListAdapter.OnItemClickLister, SunshineParse.SunshineCallback {

    RecyclerView recyclerView;
    HomeListAdapter homeListAdapter;
    SwipeRefreshLayout refreshLayout;
    SunshinePageControl control;

    public DiscussionHomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.container_recycler_view);
        homeListAdapter = new HomeListAdapter(this,getActivity(),getChildFragmentManager(),recyclerView);
        homeListAdapter.setPagerEnabled(true);
        recyclerView.setAdapter(homeListAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        control =  new SunshinePageControl(SunshinePageControl.ORDER_DESCENING,recyclerView,refreshLayout, ColletionsStatics.getDataDiscusion(),null, Discussion.class);
        if(ColletionsStatics.getDataDiscusion().size()==0) {
            control.nextPage();
            SunshineParse parse = new SunshineParse();
            SunshineQuery query =  new SunshineQuery();
            query.setLimit(3);
            query.setOrderDescending("likes");
            parse.getAllRecords(query,this,null,Discussion.class);
        }
        return v;
    }



    public void notifyDataChanged(){
        if (homeListAdapter != null) {
            homeListAdapter.notifyDataSetChanged();
        }
    }

    public void nextPage(){
        if(control!=null)
            control.nextPage();
    }

    public void reloadWithQuery(SunshineQuery query){
        if(control!=null) {
            if(query==null)
                homeListAdapter.setPagerEnabled(true);
            else
                homeListAdapter.setPagerEnabled(false);
            control.reloadWithQuery(query);
        }
    }


    @Override
    public void onItemclick(int position) {
        startActivity(new Intent(getActivity(), ForumsActivity.class).putExtra("pos", position));
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
            ColletionsStatics.getHomeDiscusion().add((Discussion) record);
        }
        notifyDataChanged();
    }
}
