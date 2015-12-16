package com.kcumendigital.democraticcauca.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.kcumendigital.democraticcauca.Models.User;

/**
 * Created by Dario Chamorro on 24/10/2015.
 */
public class AppUtil {
    private static User userStatic;
    public static User getUserStatic(){

        if (userStatic == null){
            userStatic = new User();
        }
        return userStatic;
    }

    public static void setUser(User user){
        userStatic=user;
    }



    public static boolean isConnected(Context context){

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }



}
