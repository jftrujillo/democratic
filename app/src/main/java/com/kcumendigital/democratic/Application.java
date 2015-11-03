package com.kcumendigital.democratic;

import com.parse.Parse;

/**
 * Created by Dario Chamorro on 24/10/2015.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "JHZauoc6yak5TrHY83jGHGr6jIIJDlYbMMfqmUoO", "1lQV62yxqmCVLHMu6HKZp9ayVOiMtTrQozVMDsCW");

    }


}
