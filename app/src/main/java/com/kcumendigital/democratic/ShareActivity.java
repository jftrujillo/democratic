package com.kcumendigital.democratic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ShareActivity extends AppCompatActivity {
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        if (savedInstanceState == null){
            Bundle bundle = getIntent().getExtras();
            pos = bundle.getInt("pos");
        }

        else {
            pos = savedInstanceState.getInt("pos");
        }

        Log.i("pos_share",""+pos);



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("pos",pos);
        super.onSaveInstanceState(outState);

    }
}
