package com.kcumendigital.democraticcauca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kcumendigital.democraticcauca.Models.User;
import com.kcumendigital.democraticcauca.Util.AppUtil;
import com.kcumendigital.democraticcauca.parse.SunshineLogin;
import com.nineoldandroids.animation.Animator;

import io.codetail.animation.SupportAnimator;

public class animation extends AppCompatActivity {
    TextView texto;
    RelativeLayout parent;
    boolean isAnimationStar = false;
    ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        texto = (TextView) findViewById(R.id.text);
        imgLogo = (ImageView) findViewById(R.id.img_logo);
        parent = (RelativeLayout) findViewById(R.id.parent);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !isAnimationStar){
            starCircularAnimation();
        }
    }

    private void starCircularAnimation() {
            int finalRadius = Math.max(parent.getWidth(), parent.getHeight() + parent.getHeight() / 2);
            int y = parent.getBottom();
            int x = parent.getRight();

           parent.setBackgroundColor(getResources().getColor(R.color.white));
        SupportAnimator animator = io.codetail.animation.ViewAnimationUtils.createCircularReveal(parent,x,y,0,finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(1000);
        animator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {
                Log.i("so mucho animation", ":3");
                starLogoAnimation();
            }

            @Override
            public void onAnimationCancel() {

            }

            @Override
            public void onAnimationRepeat() {

            }
        });

        animator.start();
        isAnimationStar = true;

    }

    private void starLogoAnimation() {
        imgLogo.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn).duration(1400).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                       startTextAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(imgLogo);

    }

    private void startTextAnimation() {
        texto.setText("");
        YoYo.with(Techniques.FadeIn).duration(10).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                User user = SunshineLogin.getLoggedUser(User.class);
                Intent intent = null;
                if(user == null){
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                }else{
                    AppUtil.setUser(user);
                    intent = new Intent(getApplicationContext()
                             , HomeActivity.class);
                }
                startActivity(intent);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(texto);
    }
}
