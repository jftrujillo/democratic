package com.kcumendigital.democratic;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.kcumendigital.democratic.Adapters.CustomPagerAdapter;
import com.kcumendigital.democratic.Adapters.SurveyListAdapter;
import com.kcumendigital.democratic.Fragments.DiscussionHomeFragment;
import com.kcumendigital.democratic.Fragments.SurveyHomeFragment;
import com.kcumendigital.democratic.Models.Discussion;
import com.kcumendigital.democratic.Models.Survey;
import com.kcumendigital.democratic.Util.ColletionsStatics;
import com.kcumendigital.democratic.parse.SunshineParse;
import com.kcumendigital.democratic.parse.SunshineRecord;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.parse.ParseException;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements DrawerLayout.DrawerListener, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SunshineParse.SunshineCallback, SurveyListAdapter.OnItemClickListenerSurvey {


    DiscussionHomeFragment fragment;
    DrawerLayout drawer;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    FloatingActionButton new_encuesta, new_forum;
    String filtro;
    Boolean isFiltred;
    DiscussionHomeFragment discussionFragment;
    SurveyHomeFragment surveyFragment;

    @Override
    protected void onRestart() {
        super.onRestart();
        discussionFragment.notifyDataChagued();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new_encuesta = (FloatingActionButton) findViewById(R.id.new_encuesta);
        new_encuesta.setOnClickListener(this);
        new_forum = (FloatingActionButton) findViewById(R.id.new_forum);
        new_forum.setOnClickListener(this);
        fragment = new DiscussionHomeFragment();
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        nav = (NavigationView) findViewById(R.id.nav);
        ImageView imm_nav = (ImageView) nav.findViewById(R.id.img_user_nav);
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(0)
                .oval(false)
                .build();
        Picasso.with(this).load("https://goo.gl/TF0Cwd").transform(transformation).into(imm_nav);
        drawer.setDrawerListener(this);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.drawer_open, R.string.drawer_close);
        nav.setNavigationItemSelectedListener(this);
        toggle.setToolbarNavigationClickListener(this);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        discussionFragment = new DiscussionHomeFragment();


        surveyFragment = new SurveyHomeFragment();


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(discussionFragment);
        fragments.add(surveyFragment);

        CustomPagerAdapter adapter = new CustomPagerAdapter(fragments, getSupportFragmentManager());

        viewPager.setAdapter(adapter);

        tabLayout.setTabsFromPagerAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        if (savedInstanceState == null) {
            SunshineParse parse = new SunshineParse();
            parse.getRecordsByPage(null, ColletionsStatics.LIMIT, null, this, null, Discussion.class);
            SunshineParse parseSurveys = new SunshineParse();
            parseSurveys.getRecordsByPage(null, ColletionsStatics.LIMIT, null, new SunshineParse.SunshineCallback() {
                @Override
                public void done(boolean success, ParseException e) {

                }

                @Override
                public void resultRecord(boolean success, SunshineRecord record, ParseException e) {

                }

                @Override
                public void resultListRecords(boolean success, Integer requestCode, List<SunshineRecord> records, ParseException e) {
                    for (int i = 0; i < records.size(); i++) {
                        ColletionsStatics.getDataSurvey().add((Survey) records.get(i));
                    }

                    surveyFragment.notifyDataset();


                }
            }
                    , null, Survey.class);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (id == R.id.navigate) {
            //startActivity(new Intent(this, LookingForAcitivity.class));

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        toggle.onDrawerSlide(drawerView, slideOffset);
    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Log.i("Navigation menu", menuItem.getTitle().toString());
        filtro = menuItem.getTitle().toString();
        getSupportActionBar().setTitle(filtro);
        isFiltred = true;
        drawer.closeDrawers();
        return false;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_encuesta:
                startActivity(new Intent(this, CreateSurveyAcitivty.class));

                break;

            case R.id.new_forum:
                startActivity(new Intent(this, CreateBoardDiscussion_activity.class));
                break;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);

        SearchManager searchManager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.navigate).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        return true;
    }


    //region ParseCallback
    @Override
    public void done(boolean success, ParseException e) {

    }

    @Override
    public void resultRecord(boolean success, SunshineRecord record, ParseException e) {

    }

    @Override
    public void resultListRecords(boolean success, Integer requestCode, List<SunshineRecord> records, ParseException e) {

        for (int i = 0; i < records.size(); i++) {
            ColletionsStatics.getDataDiscusion().add((Discussion) records.get(i));
        }

        discussionFragment.notifyDataset();


    }

    @Override
    public void onItemClick(int position) {
        startActivity(new Intent(getApplicationContext(), SurveyDescriptionActivity.class).putExtra("pos", position));
    }
    //endregion
}
