package com.kcumendigital.democratic.Models;


import com.kcumendigital.democratic.parse.SunshineUser;

/**
 * Created by Dario Chamorro on 19/10/2015.
 */
public class User extends SunshineUser {

    @fileUrl
    String img;

    @filePath
    String imgPath;

    String name;

    //region Getters and Setters
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //endregion
}
