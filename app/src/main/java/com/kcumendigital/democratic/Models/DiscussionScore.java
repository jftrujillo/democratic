package com.kcumendigital.democratic.Models;


import com.kcumendigital.democratic.parse.SunshineRecord;

/**
 * Created by Dario Chamorro on 20/10/2015.
 */
public class DiscussionScore extends SunshineRecord implements ScoreTypes {

    @relationId
    String discussion;

    @relationId(type = ID_USER)
    String user;

    String type;

    public String getDiscussion() {
        return discussion;
    }

    public void setDiscussion(String discussion) {
        this.discussion = discussion;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
