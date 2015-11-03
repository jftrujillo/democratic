package com.kcumendigital.democratic.parse;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

//import com.parse.ParseFacebookUtils;

/**
 * Created by Dario Chamorro on 22/10/2015.
 */
public class SunshineLogin implements SignUpCallback, LogInCallback {

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

    public void siginUp(SunshineUser user, SunshineLoginCallback callback){

        this.callback = callback;

        ParseUser parseUser =  new ParseUser();
        parseUser.setEmail(user.getEmail());
        parseUser.setUsername(user.getUserName());
        parseUser.setPassword(user.getPassword());
        prepareParseUser(parseUser, user);

        parseUser.signUpInBackground(this);

    }

    public void login(String username, String password, SunshineLoginCallback callback){
        this.callback = callback;
        ParseUser.logInInBackground(username, password, this);
    }

    public static SunshineUser getLoggedUser(Class<? extends  SunshineUser> c){
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            return prepareUser(currentUser, c);
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

    /*public void loginByFacebook(Activity activity,Collection<String> permissions, final SunshineFacebookLoginCallback facebookLoginCallback){
        this.facebookLoginCallback = facebookLoginCallback;
        ParseFacebookUtils.logInWithReadPermissionsInBackground(activity, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if(user == null){
                    facebookLoginCallback.done(SunshineFacebookLoginCallback.CANCELED, e);
                }else if(user.isNew()){
                    HashMap<String,HashMap<String,String>> data = (HashMap<String, HashMap<String, String>>) user.get("authData");
                    HashMap<String,String> fb = data.get("facebook");
                    String id = fb.get("id");
                    facebookLoginCallback.done(SunshineFacebookLoginCallback.SIGINUP, e);

                }else{
                    facebookLoginCallback.done(SunshineFacebookLoginCallback.LOGIN, e);
                }
            }
        });
    }
*/
    private void prepareParseUser(ParseUser parseUser, SunshineUser user) {
        //HashMap<String,List<String>> fields = getFields(record.getClass());
        //ParseObject parseObject = getParseObject(fields.get(ANNOTATION_NORMAL), record);
        //addRelations(parseObject,fields.get(ANNOTATION_RELATION), record);
        //addRelationsById(parseObject, fields.get(ANNOTATION_RELATION_ID), record);
    }



    private static SunshineUser prepareUser(ParseUser currentUser, Class<? extends SunshineUser> c) {
        return null;
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
