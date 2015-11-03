package com.kcumendigital.democratic.parse;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.File;

/**
 * Created by Dario Chamorro on 23/10/2015.
 */
public class SunshneUtils {


    public static void scaleImageFile(int minAxis, String path){
        scaleImageFile(minAxis,path,null);
    }

    public static void scaleImageFile(int minAxis, String path, String newName){

    }

    public static void saveImageFile(Drawable drawable, File dir, String name ){

        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        saveImageFile(bitmap, dir, name);
    }

    public static void saveImageFile(Bitmap bitmap, File dir, String name){
        if(!dir.isDirectory())
            dir.mkdir();

        File fileImage = new File(dir, name);
    }

}
