package com.kcumendigital.democratic;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.kcumendigital.democratic.Fragments.CreateForumFragment;
import com.kcumendigital.democratic.Models.Discussion;
import com.kcumendigital.democratic.Models.User;
import com.kcumendigital.democratic.parse.SunshineParse;
import com.kcumendigital.democratic.parse.SunshineRecord;
import com.parse.ParseException;

import java.util.List;

public class CreateBoardDiscussion_activity extends AppCompatActivity implements CreateForumFragment.OnButton {

    public static String USER_ID = "9zq30KL7Gu";
    CreateForumFragment forum;
    Toolbar mToolbar;
    TextInputLayout title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board_discussion);

        mToolbar = (Toolbar) findViewById(R.id.toolbarforo);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                Toast.makeText(getApplicationContext(), "me debe llevar a la seccion anterior", Toast.LENGTH_LONG).show();
                //Toast.makeText(MainActivity.this, R.string.up, Toast.LENGTH_SHORT).show();
            }
        });

        title = (TextInputLayout) findViewById(R.id.tituloForo);


        forum = new CreateForumFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, forum);
        ft.commit();
    }

    @Override
    public void OnButtonClick() {
        Discussion discussion =  forum.getNewDisucion();
        discussion.setTitle(title.getEditText().getText().toString());
        User user = new User();
        user.setObjectId(this.USER_ID);
        discussion.setUser(user);
        SunshineParse parse = new SunshineParse();
        parse.insert(discussion, new SunshineParse.SunshineCallback() {
            @Override
            public void done(boolean success, ParseException e) {
                if (success == true){
                    Toast.makeText(getApplicationContext(),"Se guardo con Exito el foro",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(),"Se guardo con Exito el foro",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void resultRecord(boolean success, SunshineRecord record, ParseException e) {

            }

            @Override
            public void resultListRecords(boolean success, Integer requestCode, List<SunshineRecord> records, ParseException e) {

            }
        });

    }
}
