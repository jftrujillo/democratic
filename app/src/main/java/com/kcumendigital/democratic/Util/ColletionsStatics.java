package com.kcumendigital.democratic.Util;

import com.kcumendigital.democratic.Models.Comment;
import com.kcumendigital.democratic.Models.Discussion;
import com.kcumendigital.democratic.Models.Survey;

import java.util.ArrayList;

/**
 * Created by asus on 22/10/2015.
 */
public class ColletionsStatics {

    public static final int LIMIT=10;
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
}
