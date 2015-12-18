package com.kcumendigital.democraticcauca.Models;

import com.kcumendigital.democraticcauca.parse.SunshineRecord;

/**
 * Created by asus on 09/12/2015.
 */
public class Report extends SunshineRecord {

    String user;
    String coment;
    String foro;


    //region GETTERS AND SETTERS

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComent() {
        return coment;
    }

    public void setComent(String coment) {
        this.coment = coment;
    }

    public String getForo() {
        return foro;
    }

    public void setForo(String foro) {
        this.foro = foro;
    }


    //endregion

}
