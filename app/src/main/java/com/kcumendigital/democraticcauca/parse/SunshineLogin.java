package com.kcumendigital.democraticcauca.parse;

import android.app.Activity;

import com.kcumendigital.democraticcauca.Models.User;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;


//import com.parse.ParseFacebookUtils;

/**
 * Created by Dario Chamorro on 22/10/2015.
 */
public class SunshineLogin implements SignUpCallback, LogInCallback {

    static User user;

    ParseUser parseUser;
    ParseFile parseFile;

    public interface SunshineLoginCallback{
        void done(boolean success, ParseException e);
    }

    public interface SunshineFacebookLoginCallback{

        int CANCELED=0;
        int SIGINUP=1;
        int LOGIN=2;

        void done(int type, ParseException e);
    }

    SunshineLoginCallback callback;
    SunshineFacebookLoginCallback facebookLoginCallback;

    public void siginUp(User user, SunshineLoginCallback callback){

        this.callback = callback;

        parseUser =  new ParseUser();
        parseUser.setEmail(user.getEmail());
        parseUser.setUsername(user.getUserName());
        parseUser.setPassword(user.getPassword());
        //prepareParseUser(parseUser, user);
        parseUser.put("name", user.getName());

        if(user.getImgPath()!=null) {
            File file = new File(user.getImgPath());
            parseFile = new ParseFile(file);
            parseFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e!=null)
                        e.printStackTrace();
                    parseUser.put("img", parseFile);
                    parseUser.signUpInBackground(SunshineLogin.this);
                }
            });
        }else{
            parseUser.signUpInBackground(SunshineLogin.this);
        }
    }

    public void login(String username, String password, SunshineLoginCallback callback){
        this.callback = callback;
        ParseUser.logInInBackground(username, password, this);
    }

    public static User getLoggedUser(Class<? extends  SunshineUser> c){
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {

            if(user == null)
                user =  new User();

            if(currentUser.get("authData")==null){
                user.setImg(currentUser.getParseFile("img").getUrl());
            }else{
                HashMap<String, HashMap<String, String>> data = (HashMap<String, HashMap<String, String>>) currentUser.get("authData");
                HashMap<String, String> fb = data.get("facebook");
                String id = fb.get("id");
                user.setImg("https://graph.facebook.com/"+id+"/picture?type=large");
            }
            user.setEmail(currentUser.getEmail());
            user.setName(currentUser.getString("name"));
            user.setUserName(currentUser.getUsername());
            user.setObjectId(currentUser.getObjectId());
            user.setCreatedAt(currentUser.getCreatedAt());
            user.setUpdateAt(currentUser.getUpdatedAt());
            return user;
        } else {
            return null;
        }
    }

    public static void logout(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser!=null){
            currentUser.logOut();
        }
    }

    public void loginByFacebook(Activity activity,Collection<String> permissions, final SunshineFacebookLoginCallback facebookLoginCallback){
        this.facebookLoginCallback = facebookLoginCallback;
        ParseFacebookUtils.logInWithReadPermissionsInBackground(activity, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if (user == null) {
                    facebookLoginCallback.done(SunshineFacebookLoginCallback.CANCELED, e);
                } else if (user.isNew()) {
                    facebookLoginCallback.done(SunshineFacebookLoginCallback.SIGINUP, e);

                } else {
                    facebookLoginCallback.done(SunshineFacebookLoginCallback.LOGIN, e);
                }
            }
        });
    }


    //region Callbacks
    @Override
    public void done(ParseException e) {
        if(e==null)
            callback.done(true, null);
        else
            callback.done(false, e);
    }

    @Override
    public void done(ParseUser user, ParseException e) {
        if(e==null)
            callback.done(true, null);
        else
            callback.done(false, e);
    }
    //endregion
}
