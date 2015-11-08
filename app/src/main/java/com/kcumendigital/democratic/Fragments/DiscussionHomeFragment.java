package com.kcumendigital.democratic.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kcumendigital.democratic.Adapters.HomeListAdapter;
import com.kcumendigital.democratic.ForumsActivity;
import com.kcumendigital.democratic.LayoutManagers.GridLayoutManager;
import com.kcumendigital.democratic.Models.Discussion;
import com.kcumendigital.democratic.R;
import com.kcumendigital.democratic.Util.ColletionsStatics;
import com.kcumendigital.democratic.parse.SunshinePageControl;

/**
 * A placeholder fragment containing a simple view.
 */
public class DiscussionHomeFragment extends Fragment implements HomeListAdapter.OnItemClickLister {

    RecyclerView recyclerView;
    HomeListAdapter homeListAdapter;
    SwipeRefreshLayout refreshLayout;


    public DiscussionHomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.container_recycler_view);
        homeListAdapter = new HomeListAdapter(this,getActivity(),getChildFragmentManager(),recyclerView);
        recyclerView.setAdapter(homeListAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh);

        SunshinePageControl control =  new SunshinePageControl(SunshinePageControl.ORDER_DESCENING,recyclerView,refreshLayout, ColletionsStatics.getDataDiscusion(),null, Discussion.class);



        return v;
    }



    public void notifyDataChanged(){
        if (homeListAdapter != null) {
            homeListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemclick(int position) {
        startActivity(new Intent(getActivity(), ForumsActivity.class).putExtra("pos", position));
    }

}
