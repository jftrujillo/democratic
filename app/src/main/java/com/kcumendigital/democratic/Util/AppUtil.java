package com.kcumendigital.democratic.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telecom.ConnectionService;

import com.kcumendigital.democratic.Models.User;

/**
 * Created by Dario Chamorro on 24/10/2015.
 */
public class AppUtil {
    public static User userStatic;
    public static User getUserStatic(){

        if (userStatic == null){
            userStatic = new User();
            userStatic.setObjectId("Jgb5AcAcBp");
        }
        return userStatic;
    }



    public static boolean isConnected(Context context){

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }



}
