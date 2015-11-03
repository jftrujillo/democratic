package com.kcumendigital.democratic.Models;

import android.content.Context;

import com.orm.SugarContext;
import com.orm.SugarRecord;

/**
 * Created by asus on 07/10/2015.
 */
public class ForumsDiscussions extends SugarRecord {
    String Name;
    String Description;
    String User__Name;
    String Category;
    String Score;

    //region Getters and Setters

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUser__Name() {
        return User__Name;
    }

    public void setUser__Name(String user__Name) {
        User__Name = user__Name;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    //endregion

    public static void init(Context context){
        SugarContext.init(context);
        ForumsDiscussions foro1 = new ForumsDiscussions();
        foro1.setName("foro1");
        foro1.setDescription("Description foro1");
        foro1.setUser__Name("creador foro1 ");
        foro1.setCategory("categoria 1");
        foro1.setScore("101");
        foro1.save();

        ForumsDiscussions foro2 = new ForumsDiscussions();
        foro2.setName("foro2");
        foro2.setDescription("Description foro2");
        foro2.setUser__Name("creador foro2 ");
        foro2.setCategory("categoria 2");
        foro2.setScore("102");
        foro2.save();

        ForumsDiscussions foro3 = new ForumsDiscussions();
        foro3.setName("foro3");
        foro3.setDescription("Description foro3");
        foro3.setUser__Name("creador foro3 ");
        foro3.setCategory("categoria 3");
        foro3.setScore("103");
        foro3.save();

        ForumsDiscussions foro4 = new ForumsDiscussions();
        foro4.setName("foro4");
        foro4.setDescription("Description foro4");
        foro4.setUser__Name("creador foro4 ");
        foro4.setCategory("categoria 4");
        foro4.setScore("104");
        foro4.save();

    }
}
