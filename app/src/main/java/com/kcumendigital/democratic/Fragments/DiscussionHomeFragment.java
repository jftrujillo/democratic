package com.kcumendigital.democratic.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kcumendigital.democratic.Adapters.HomeListAdapter;
import com.kcumendigital.democratic.ForumsActivity;
import com.kcumendigital.democratic.LayoutManagers.GridLayoutManager;
import com.kcumendigital.democratic.Models.Discussion;
import com.kcumendigital.democratic.R;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DiscussionHomeFragment extends Fragment implements HomeListAdapter.OnItemClickLister {
    RecyclerView recyclerView;
    ViewPager pager;
    PagerAdapter pagerAdapter;
    HomeListAdapter homeListAdapter;

    public DiscussionHomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_home, container, false);


        recyclerView = (RecyclerView) v.findViewById(R.id.container_recycler_view);
        homeListAdapter = new HomeListAdapter(this,getActivity(),getChildFragmentManager(),recyclerView);
        recyclerView.setAdapter(homeListAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        return v;
    }

    @Override
    public void onItemclick(int position) {
        Toast.makeText(getActivity(),"pos"+position,Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), ForumsActivity.class).putExtra("pos",position));
    }

    public void notifyDataset(){
        homeListAdapter.notifyDataSetChanged();
    }


}
