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



    public PagerAdpater(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("PAGER","ENTRO POS"+position);
        NumberFragmetFragment fragment;
        fragment = new NumberFragmetFragment();
        fragment.init(position);
        return fragment;

    }

    @Override
    public int getCount() {
        return 3;
    }
}
