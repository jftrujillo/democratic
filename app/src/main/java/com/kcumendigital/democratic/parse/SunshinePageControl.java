package com.kcumendigital.democratic.parse;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.kcumendigital.democratic.Util.ColletionsStatics;
import com.parse.ParseException;

import java.util.Date;
import java.util.List;

/**
 * Created by Dario Chamorro on 8/11/2015.
 */
public class SunshinePageControl implements SwipeRefreshLayout.OnRefreshListener, SunshineParse.SunshineCallback {

    public static final int ORDER_ASCENING=0;
    public static final int ORDER_DESCENING=1;

    int type;

    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    List to;
    SunshineQuery query;
    Class parseClass;

    SunshineParse parse;

    OnScroll onScroll;
    boolean refreshing;
    boolean addingPage;


    public SunshinePageControl(int type, RecyclerView recyclerView, SwipeRefreshLayout refreshLayout, List to, SunshineQuery query, Class parseClass) {
        this.type = type;
        this.recyclerView = recyclerView;
        this.refreshLayout = refreshLayout;
        this.to = to;
        this.query = query;
        this.parseClass = parseClass;

        parse =  new SunshineParse();

        refreshing = false;
        addingPage = false;

        onScroll = new OnScroll();
        if(refreshLayout !=null)
            refreshLayout.setOnRefreshListener(this);
        if(recyclerView!=null)
            recyclerView.addOnScrollListener(onScroll);
    }



    public void release(){
        to = null;
        parseClass = null;
        query= null;
        if(recyclerView!=null)
            recyclerView.removeOnScrollListener(onScroll);
        recyclerView = null;
        refreshLayout = null;
    }


    //region Page Events
    @Override
    public void onRefresh() {
        getRecents();
    }

    private class OnScroll extends RecyclerView.OnScrollListener{

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {


            int  visibleItemCount = recyclerView.getLayoutManager().getChildCount();
            int totalItemCount = recyclerView.getLayoutManager().getItemCount();
            int pastVisiblesItems = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();



            if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount && !addingPage) {

                nextPage();
            }


        }
    }

    //endregion

    //region Page Methods

    public void reload(){

        to.clear();
        nextPage();
    }

    public void reloadWithQuery(SunshineQuery query){
        this.query = query;
        reload();
    }

    public void getRecents(){
        refreshing = true;
        Date lastDate = to.size()>0?((SunshineRecord) to.get(0)).getCreatedAt():null;
        parse.getRecentRecords(lastDate
                    ,query,this, ColletionsStatics.RECENTS,parseClass);

    }

    public void nextPage(){
        addingPage = true;
        Date lastDate = to.size()>0?((SunshineRecord) to.get(to.size() - 1)).getCreatedAt():null;

        if(type==ORDER_DESCENING)
            parse.getRecordsByPage(lastDate, ColletionsStatics.LIMIT, query, SunshinePageControl.this, ColletionsStatics.PAGE, parseClass);
        else
            parse.getRecordsByPageAsc(lastDate, ColletionsStatics.LIMIT, query, SunshinePageControl.this, ColletionsStatics.PAGE, parseClass);
    }

    private void addPageToList(List<SunshineRecord> from){
        addingPage = false;
        for(SunshineRecord obj : from){
            to.add(obj);
        }
        if(recyclerView!=null)
            recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void addRecent(List<SunshineRecord> from){
        refreshLayout.setRefreshing(false);
        refreshing = false;
        for(int i=from.size()-1; i>=0;i--){
            to.add(0,from.get(i));
        }
        if(recyclerView!=null)
            recyclerView.getAdapter().notifyDataSetChanged();
    }
    //endregion

    //region ParseMethods
    @Override
    public void done(boolean success, ParseException e) {

    }

    @Override
    public void resultRecord(boolean success, SunshineRecord record, ParseException e) {

    }

    @Override
    public void resultListRecords(boolean success, Integer requestCode, List<SunshineRecord> records, ParseException e) {
        if(requestCode==ColletionsStatics.RECENTS)
            addRecent(records);
        else
            addPageToList(records);
    }

    //endregion
}
