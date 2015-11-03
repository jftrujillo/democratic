package com.kcumendigital.democratic.Models;


import com.kcumendigital.democratic.parse.SunshineRecord;

import java.util.List;

/**
 * Created by Dario Chamorro on 19/10/2015.
 */
public class Survey extends SunshineRecord {

    String title, description,category;
    @user
    User user;
    @relation (type = ONE_TO_MANY_ARRAY)
    List<SurveyOption> options;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<SurveyOption> getOptions() {
        return options;
    }

    public void setOptions(List<SurveyOption> options) {
        this.options = options;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
