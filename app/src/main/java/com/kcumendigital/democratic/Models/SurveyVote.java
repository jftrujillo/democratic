package com.kcumendigital.democratic.Models;


import com.kcumendigital.democratic.parse.SunshineRecord;

/**
 * Created by Dario Chamorro on 20/10/2015.
 */
public class SurveyVote extends SunshineRecord {

    @relationId(type = ID_RELATION)
    String surveyOption;
    //pbejto de la opcion votada

    @relationId(type = ID_USER)
    String user;

    //id del usuario

    public String getSurveyOption() {
        return surveyOption;
    }

    public void setSurveyOption(String option) {
        this.surveyOption= option;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
