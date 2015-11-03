package com.kcumendigital.democratic.parse;

/**
 * Created by Dario Chamorro on 20/10/2015.
 */
public class SunshineUser extends SunshineRecord {

    String userName, password,email;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
