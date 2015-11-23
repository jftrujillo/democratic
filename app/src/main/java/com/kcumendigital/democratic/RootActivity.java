package com.kcumendigital.democratic;

import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.kcumendigital.democratic.Models.User;
import com.kcumendigital.democratic.Util.AppUtil;
import com.kcumendigital.democratic.parse.SunshineLogin;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

/**
 * Created by Dario Chamorro on 9/11/2015.
 */
public class RootActivity extends AwesomeSplash {



    @Override
    public void initSplash(ConfigSplash configSplash) {
        configSplash.setBackgroundColor(R.color.morado_demo);
        configSplash.setAnimCircularRevealDuration(1000);
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);

        configSplash.setLogoSplash(R.drawable.iconodemocratic05);
        configSplash.setAnimLogoSplashDuration(1000);
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn);

        configSplash.setTitleSplash("Democratic");
        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(30f);
        configSplash.setAnimTitleDuration(1000);
        configSplash.setAnimTitleTechnique(Techniques.FadeIn);




    }

    @Override
    public void animationsFinished() {
        User user = SunshineLogin.getLoggedUser(User.class);
        Intent intent = null;
        if(user == null){
            intent = new Intent(this, LoginActivity.class);
        }else{
            AppUtil.setUser(user);
            intent = new Intent(this, HomeActivity.class);
        }
        startActivity(intent);
    }
}
