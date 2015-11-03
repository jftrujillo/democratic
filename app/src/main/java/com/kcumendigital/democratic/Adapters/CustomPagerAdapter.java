package com.kcumendigital.democratic.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by asus on 14/10/2015.
 */
public class CustomPagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragments;


    public CustomPagerAdapter(List<Fragment> fragments,FragmentManager fm) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);

        /*switch (position){
            case 0:

                DiscussionHomeFragment tab = new DiscussionHomeFragment();
                tab.init(data);

                return tab;



        case 1:
        DiscussionHomeFragment tab2 = new DiscussionHomeFragment();
            tab2.init(data);
        return tab2;

        default:

            return null;

    }*/
    }

    @Override
    public int getCount() {
        return  fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0)
            return "Discusiones";
        else
            return "Encuestas";
    }
}
