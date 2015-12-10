package com.kcumendigital.democratic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kcumendigital.democratic.library.Techniques;
import com.kcumendigital.democratic.library.YoYo;


public class ShareActivity extends AppCompatActivity implements View.OnClickListener {
    int pos;
    Button share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        share = (Button) findViewById(R.id.share);
        if (savedInstanceState == null){
            Bundle bundle = getIntent().getExtras();
            pos = bundle.getInt("pos");
        }

        else {
            pos = savedInstanceState.getInt("pos");
        }

        Log.i("pos_share",""+pos);

        share.setOnClickListener(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("pos", pos);
        super.onSaveInstanceState(outState);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.share:
                YoYo.with(Techniques.Pulse).duration(700).playOn(findViewById(R.id.share));
                break;
        }

    }
}
