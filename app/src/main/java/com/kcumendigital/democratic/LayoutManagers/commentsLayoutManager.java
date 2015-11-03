package com.kcumendigital.democratic.LayoutManagers;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

/**
 * Created by manuelfernando on 16/10/2015.
 */
public class commentsLayoutManager extends GridLayoutManager {

    public commentsLayoutManager(Context context) {
        super(context, 1);

        setSpanSizeLookup(new SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
            return  1;
            }
        });
    }
}
