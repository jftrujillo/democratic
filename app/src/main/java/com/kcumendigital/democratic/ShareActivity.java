package com.kcumendigital.democratic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
<<<<<<< HEAD
import android.util.Log;

public class ShareActivity extends AppCompatActivity {
    int pos;
=======
import android.view.View;
import android.widget.Button;

import com.kcumendigital.democratic.library.Techniques;
import com.kcumendigital.democratic.library.YoYo;
import com.kcumendigital.democratic.library.attention.TadaAnimator;
>>>>>>> c808a6e95ac84d740a46145feb26eeade9ae6891

public class ShareActivity extends AppCompatActivity implements View.OnClickListener {

    Button share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

<<<<<<< HEAD
        if (savedInstanceState == null){
            Bundle bundle = getIntent().getExtras();
            pos = bundle.getInt("pos");
        }

        else {
            pos = savedInstanceState.getInt("pos");
        }

        Log.i("pos_share",""+pos);


=======
        share = (Button) findViewById(R.id.share);
        share.setOnClickListener(this);
>>>>>>> c808a6e95ac84d740a46145feb26eeade9ae6891

    }

    @Override
<<<<<<< HEAD
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("pos",pos);
        super.onSaveInstanceState(outState);

=======
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.share:
                YoYo.with(Techniques.Pulse).duration(700).playOn(findViewById(R.id.share));
                break;
        }
>>>>>>> c808a6e95ac84d740a46145feb26eeade9ae6891
    }
}
