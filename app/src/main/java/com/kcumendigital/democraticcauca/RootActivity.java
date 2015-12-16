package com.kcumendigital.democraticcauca;

import android.content.Intent;

import com.daimajia.androidanimations.library.Techniques;
import com.kcumendigital.democraticcauca.Models.User;
import com.kcumendigital.democraticcauca.Util.AppUtil;
import com.kcumendigital.democraticcauca.parse.SunshineLogin;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

/**
 * Created by Dario Chamorro on 9/11/2015.
 */
public class RootActivity extends AwesomeSplash {



    @Override
    public void initSplash(ConfigSplash configSplash) {
        configSplash.setBackgroundColor(R.color.white);
        configSplash.setAnimCircularRevealDuration(1000);
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);

        configSplash.setLogoSplash(R.drawable.iconos);
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
