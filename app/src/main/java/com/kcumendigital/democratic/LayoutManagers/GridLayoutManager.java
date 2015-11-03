package com.kcumendigital.democratic.LayoutManagers;

import android.content.Context;

/**
 * Created by asus on 07/10/2015.
 */
public class GridLayoutManager extends android.support.v7.widget.GridLayoutManager {


    public GridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);

        setSpanSizeLookup(new SpanSizeLookup() {
            @Override

            public int getSpanSize(int position) {
                    return 2;
            }
        });




    }


}
