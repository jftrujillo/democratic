package com.kcumendigital.democratic.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.kcumendigital.democratic.Fragments.NumberFragmetFragment;

/**
 * Created by asus on 07/10/2015.
 */
public class PagerAdpater extends FragmentStatePagerAdapter{
    public static final int TYPE_DISCUSION = 0;
    public static final int TYPE_SURVEY = 1;
    int TYPE_LIST;


    public PagerAdpater(FragmentManager fm,int TYPE_LIST) {
        super(fm);
        this.TYPE_LIST = TYPE_LIST;
    }

    @Override
    public Fragment getItem(int position) {
        NumberFragmetFragment fragment = new NumberFragmetFragment();
        if (TYPE_LIST == TYPE_DISCUSION) {
            Log.i("PAGER", "ENTRO POS" + position);
            fragment.init(position,TYPE_DISCUSION);

        }

        if (TYPE_LIST == TYPE_SURVEY){
            fragment.init(position,TYPE_SURVEY);
        }
        return fragment;

    }

    @Override
    public int getCount() {
        return 3;
    }
}
