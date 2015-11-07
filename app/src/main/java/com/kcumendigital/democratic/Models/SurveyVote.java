package com.kcumendigital.democratic.Models;


import com.kcumendigital.democratic.parse.SunshineRecord;

/**
 * Created by Dario Chamorro on 20/10/2015.
 */
public class SurveyVote extends SunshineRecord {

    @relation
    SurveyOption option;
    //pbejto de la opcion votada

    @relationId(type = ID_USER)
    String user;

    //id del usuario 

    public SurveyOption getOption() {
        return option;
    }

    public void setOption(SurveyOption option) {
        this.option = option;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
