package com.kcumendigital.democratic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kcumendigital.democratic.library.Techniques;
import com.kcumendigital.democratic.library.YoYo;
import com.kcumendigital.democratic.library.attention.TadaAnimator;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener {

    Button share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        share = (Button) findViewById(R.id.share);
        share.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.share:
                YoYo.with(Techniques.Pulse).duration(700).playOn(findViewById(R.id.share));
                break;
        }
    }
}
