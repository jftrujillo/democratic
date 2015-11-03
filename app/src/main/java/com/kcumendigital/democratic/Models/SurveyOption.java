package com.kcumendigital.democratic.Models;


import com.kcumendigital.democratic.parse.SunshineRecord;

/**
 * Created by Dario Chamorro on 19/10/2015.
 */
public class SurveyOption extends SunshineRecord {

    String description;
    long votes;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getVotes() {
        return votes;
    }

    public void setVotes(long votes) {
        this.votes = votes;
    }
}
