package com.kcumendigital.democratic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.github.paolorotolo.appintro.AppIntro;
import com.kcumendigital.democratic.Adapters.HomeListAdapter;
import com.kcumendigital.democratic.Fragments.IntroFragment;
import com.kcumendigital.democratic.Fragments.WelcomeFragment;

public class AppIntroActivity extends AppIntro {


    @Override
    public void init(Bundle savedInstanceState) {
        IntroFragment fragment = new IntroFragment();
        fragment.init(0);
        IntroFragment fragment1 = new IntroFragment();
        fragment1.init(1);
        IntroFragment fragment3 = new IntroFragment();
        fragment3.init(3);
        IntroFragment fragment4 = new IntroFragment();
        fragment4.init(4);
        WelcomeFragment fragment2 = new WelcomeFragment();
        addSlide(fragment2);
        addSlide(fragment4);
        addSlide(fragment3);
        addSlide(fragment1);
        addSlide(fragment);

        showSkipButton(false);
        setDoneText("Entrar");




    }

    @Override
    public void onSkipPressed() {

    }

    @Override
    public void onDonePressed() {
        startActivity(new Intent(this,HomeActivity.class));
    }
}
