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
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.kcumendigital.democratic.Adapters.CustomPagerAdapter;
import com.kcumendigital.democratic.Adapters.SurveyListAdapter;
import com.kcumendigital.democratic.Fragments.DiscussionHomeFragment;
import com.kcumendigital.democratic.Fragments.SurveyHomeFragment;
import com.kcumendigital.democratic.Models.Discussion;
import com.kcumendigital.democratic.Models.Survey;
import com.kcumendigital.democratic.Util.AppUtil;
import com.kcumendigital.democratic.Util.ColletionsStatics;
import com.kcumendigital.democratic.parse.SunshineLogin;
import com.kcumendigital.democratic.parse.SunshineParse;
import com.kcumendigital.democratic.parse.SunshineQuery;
import com.kcumendigital.democratic.parse.SunshineRecord;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.parse.ParseException;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SurveyListAdapter.OnItemClickListenerSurvey {


    DiscussionHomeFragment fragment;
    DrawerLayout drawer;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    FloatingActionButton new_encuesta, new_forum;
    String filtro;

    DiscussionHomeFragment discussionFragment;
    SurveyHomeFragment surveyFragment;

    SunshineQuery query;

    @Override
    protected void onRestart() {
        super.onRestart();
        discussionFragment.notifyDataChanged();
        surveyFragment.notifyDataChanged();
        ColletionsStatics.getDataComments().clear();
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
                .scaleType(ImageView.ScaleType.CENTER_CROP)
                .oval(true)
                .build();

        int avatar = getResources().getDimensionPixelSize(R.dimen.nav_header_avatar);
        Picasso.with(this).load(AppUtil.getUserStatic().getImg())
                .resize(avatar, avatar)
                .centerCrop()
                .transform(transformation).into(imm_nav);
        TextView userName = (TextView) nav.findViewById(R.id.txt_usr);
        userName.setText(AppUtil.getUserStatic().getName());

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

    }


    //region OptionMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);

        SearchManager searchManager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.navigate).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region DrawerLayout
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        filtro = menuItem.getTitle().toString();
        getSupportActionBar().setTitle(filtro);

        switch(menuItem.getItemId()){
            case R.id.nav_home:
                query = null;
                break;
            case R.id.nav_gobierno:
                query = new SunshineQuery();
                query.addFieldValue("category", getString(R.string.c_gobierno));
                break;
            case R.id.nav_educacion:
                query = new SunshineQuery();
                query.addFieldValue("category", getString(R.string.c_educacion));
                break;
            case R.id.nav_salud:
                query = new SunshineQuery();
                query.addFieldValue("category", getString(R.string.c_salud));
                break;
            case R.id.nav_medio_ambiente:
                query = new SunshineQuery();
                query.addFieldValue("category", getString(R.string.c_ambiente));
                break;
            case R.id.nav_my_post:
                query = new SunshineQuery();
                query.addUser("user", AppUtil.getUserStatic().getObjectId());
                break;
            case R.id.nav_logout:
                SunshineLogin.logout();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;

        }

        surveyFragment.reloadWithQuery(query);
        discussionFragment.reloadWithQuery(query);
        drawer.closeDrawers();
        return false;
    }
    //endregion

    //region FAB
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
    //endregion

    @Override
    public void onItemClick(int position) {
        startActivity(new Intent(getApplicationContext(), SurveyDescriptionActivity.class).putExtra("pos", position-3));
    }
    //endregion


}
