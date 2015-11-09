package com.kcumendigital.democratic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kcumendigital.democratic.Models.User;
import com.kcumendigital.democratic.Util.AppUtil;
import com.kcumendigital.democratic.parse.SunshineLogin;

/**
 * Created by Dario Chamorro on 9/11/2015.
 */
public class RootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = SunshineLogin.getLoggedUser(User.class);
        Intent intent = null;
        if(user == null){
            intent = new Intent(this, AppIntroActivity.class);
        }else{
            AppUtil.setUser(user);
            intent = new Intent(this, HomeActivity.class);
        }
        startActivity(intent);

    }
}
