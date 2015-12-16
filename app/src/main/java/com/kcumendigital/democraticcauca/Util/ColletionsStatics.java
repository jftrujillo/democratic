package com.kcumendigital.democraticcauca.Util;

import com.kcumendigital.democraticcauca.Models.Comment;
import com.kcumendigital.democraticcauca.Models.Discussion;
import com.kcumendigital.democraticcauca.Models.Survey;
import com.kcumendigital.democraticcauca.parse.SunshineQuery;

import java.util.ArrayList;

/**
 * Created by asus on 22/10/2015.
 */
public class ColletionsStatics {

    public static final int LIMIT=20;
    public static final int RECENTS=0;
    public static final int PAGE=1;

    private static ArrayList<Discussion> dataDiscusion;
    public static ArrayList<Discussion> getDataDiscusion(){
        if (dataDiscusion == null){
            dataDiscusion = new ArrayList<>();
        }
        return dataDiscusion;
    }

    private static ArrayList<Comment> dataComments;
    public static ArrayList<Comment> getDataComments(){
        if (dataComments == null){
            dataComments = new ArrayList<>();
        }
        return dataComments;
    }

    private static ArrayList<Survey> dataSurvey;
    public static ArrayList<Survey> getDataSurvey(){
        if (dataSurvey == null){
            dataSurvey = new ArrayList<>();
        }
        return dataSurvey;
    }

    public static SunshineQuery queryFilter;

    private static ArrayList<Discussion> homeDiscusion;
    public static ArrayList<Discussion> getHomeDiscusion(){
        if (homeDiscusion == null){
            homeDiscusion = new ArrayList<>();
        }
        return homeDiscusion;
    }

    private static ArrayList<Survey> homeSurvey;
    public static ArrayList<Survey> getHomeSurvey(){
        if (homeSurvey == null){
            homeSurvey= new ArrayList<>();
        }
        return homeSurvey;
    }
}
