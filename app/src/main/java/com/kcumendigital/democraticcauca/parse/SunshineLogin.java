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

import org.json.JSONException;
import org.json.JSONObject;

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

        void doneLoginFacebook(boolean success);
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

    public void siginUpFB(User user,String data, SunshineFacebookLoginCallback callback){

        this.facebookLoginCallback = callback;

        parseUser =  new ParseUser();
        parseUser.setEmail(user.getEmail());
        parseUser.setUsername(user.getUserName());
        parseUser.setPassword(user.getPassword());
        //prepareParseUser(parseUser, user);
        parseUser.put("name", user.getName());

        parseUser.put("facebookData", data);

        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null)
                    facebookLoginCallback.doneLoginFacebook(true);
                else
                    facebookLoginCallback.doneLoginFacebook(false);
            }
        });

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

            if(currentUser.get("facebookData")==null){
                user.setImg(currentUser.getParseFile("img").getUrl());
            }else{

                try {
                    JSONObject fb = new JSONObject(currentUser.getString("facebookData"));
                    String id = fb.getString("id");
                    user.setImg("https://graph.facebook.com/"+id+"/picture?type=large");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
