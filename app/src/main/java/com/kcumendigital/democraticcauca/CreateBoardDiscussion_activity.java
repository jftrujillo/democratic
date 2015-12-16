package com.kcumendigital.democraticcauca;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kcumendigital.democraticcauca.Fragments.CreateForumFragment;
import com.kcumendigital.democraticcauca.Models.Discussion;
import com.kcumendigital.democraticcauca.Models.User;
import com.kcumendigital.democraticcauca.Util.AppUtil;
import com.kcumendigital.democraticcauca.parse.SunshineParse;
import com.kcumendigital.democraticcauca.parse.SunshineRecord;
import com.parse.ParseException;

import java.util.List;

public class CreateBoardDiscussion_activity extends AppCompatActivity implements CreateForumFragment.OnButton {

    //public static String USER_ID = "9zq30KL7Gu";
    CreateForumFragment forum;
    Toolbar mToolbar;
    TextInputLayout title;
    User user;
    EditText discus;
    TextView count;

    static final String KEY_DISCUSSION = "txt_discussion";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board_discussion);
        user = new User();
        discus = (EditText) findViewById(R.id.discus);

        user = AppUtil.getUserStatic();


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
        count = (TextView) findViewById(R.id.count_caracters_toolbar);



        forum = new CreateForumFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, forum);
        ft.commit();

        if(savedInstanceState!=null){
            String titleS = savedInstanceState.getString(KEY_DISCUSSION);
            discus.setText(titleS);
        }

        discus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                int countL = charSequence.length();
                count.setText(""+countL+"/80");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

        outState.putString(KEY_DISCUSSION, discus.getText().toString());
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void OnButtonClick() {
        Discussion discussion = forum.getNewDisucion();
        if (title.getEditText().getText().toString().equals("") || discussion.getCategory().equals("") || discussion.getDescription().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            discussion.setTitle(title.getEditText().getText().toString());
            discussion.setUser(user);
            discussion.setReport(0);
            SunshineParse parse = new SunshineParse();
            parse.insert(discussion, new SunshineParse.SunshineCallback() {
                @Override
                public void done(boolean success, ParseException e) {
                    if (success == true) {
                        Toast.makeText(getApplicationContext(), "Se guardo con Exito el foro", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(getApplicationContext(), "Se guardo sin exito el foro", Toast.LENGTH_SHORT).show();
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
}
