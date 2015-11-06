package com.kcumendigital.democratic.Adapters;

import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kcumendigital.democratic.Fragments.NumberFragmetFragment;
import com.kcumendigital.democratic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 07/10/2015.
 */
public class PagerAdpater extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {
    public static final int TYPE_DISCUSION = 0;
    public static final int TYPE_SURVEY = 1;
    int TYPE_LIST;

    ViewPager pager;
    LinearLayout marksLayout;
    ImageView marks[];
    int prevMark;

    public PagerAdpater(FragmentManager fm,ViewPager pager,LinearLayout marksLayout,int TYPE_LIST) {
        super(fm);
        this.TYPE_LIST = TYPE_LIST;
        this.pager = pager;
        this.marksLayout = marksLayout;
        marks = new ImageView[3];
        marks[0] = (ImageView) marksLayout.findViewById(R.id.img_mark_1);
        marks[1] = (ImageView) marksLayout.findViewById(R.id.img_mark_2);
        marks[2] = (ImageView) marksLayout.findViewById(R.id.img_mark_3);
        marks[0].setImageResource(R.drawable.mark_selected);
        prevMark =0;

        pager.addOnPageChangeListener(this);
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        marks[prevMark].setImageResource(R.drawable.mark_unselected);
        marks[position].setImageResource(R.drawable.mark_selected);
        prevMark = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
