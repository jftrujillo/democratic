package com.kcumendigital.democratic;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.kcumendigital.democratic.Fragments.IntroFragment;
import com.kcumendigital.democratic.Fragments.WelcomeFragment;

import java.util.List;

public class AppIntroActivity extends AppIntro {


    @Override
    public void init(Bundle savedInstanceState) {
        IntroFragment fragment = new IntroFragment();
        fragment.init(0);
        IntroFragment fragment1 = new IntroFragment();
        fragment1.init(1);
        WelcomeFragment fragment2 = new WelcomeFragment();
        addSlide(fragment2);
        addSlide(fragment1);
        addSlide(fragment);

        showSkipButton(false);
        setBarColor(Color.parseColor("#512DA8"));
        setSeparatorColor(Color.parseColor("#FFC107"));
        setDoneText("Continuar");




    }

    @Override
    public void onSkipPressed() {

    }

    @Override
    public void onDonePressed() {
        startActivity(new Intent(this,HomeActivity.class));
    }
}