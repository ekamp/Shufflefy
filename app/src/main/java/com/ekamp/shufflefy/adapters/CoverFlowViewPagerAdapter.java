package com.ekamp.shufflefy.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Adapter to be used for the cover art flow ViewPager.
 *
 * @author Erik Kamp
 * @since 7/25/15.
 */
public class CoverFlowViewPagerAdapter extends FragmentStatePagerAdapter {

    public CoverFlowViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
