package com.ekamp.shufflefy.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ekamp.shufflefy.controller.SpotifyController;
import com.ekamp.shufflefy.fragments.CoverFlowFragment;

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
        return CoverFlowFragment.newInstance(SpotifyController.getInstance().getUsersSavedTracks().get(position));
    }

    @Override
    public int getCount() {
        return SpotifyController.getInstance().getUsersSavedTracks().size();
    }
}
