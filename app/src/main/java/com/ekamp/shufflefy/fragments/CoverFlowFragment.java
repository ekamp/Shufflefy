package com.ekamp.shufflefy.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ekamp.shufflefy.R;
import com.ekamp.shufflefy.activities.ActivityControllerCallback;
import com.squareup.picasso.Picasso;

/**
 * Fragment used to display cover art for the currently playing song.
 *
 * @author Erik Kamp
 * @since 7/25/15
 */
public class CoverFlowFragment extends Fragment {

    public static String TAG = "CoverFlowFragment";
    private ImageView coverFlowImageView;
    private ActivityControllerCallback activityControllerCallback;
    private String trackID;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityControllerCallback = (ActivityControllerCallback) activity;
        } catch (Exception e) {
            Log.e(getClass().getName(), "Parent class is not the correct implementation");
        }
    }

    public static CoverFlowFragment newInstance(String trackID) {
        CoverFlowFragment coverFlowFragment = new CoverFlowFragment();
        coverFlowFragment.setTrackID(trackID);
        return coverFlowFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        coverFlowImageView = (ImageView) view.findViewById(R.id.cover_flow_image_view);
//        populateCoverFlowImage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cover_flow_fragment, container, true);
    }

    /**
     * Sets the track ID for this fragment instance.
     *
     * @param trackID Spotify Track ID
     */
    public void setTrackID(String trackID) {
        this.trackID = trackID;
    }

    /**
     * Using Picasso loads the cover art for the specified song into this fragment's ImageView
     *
     * @param imagePathFromSpotify imagePath or resource collected from the Spotify API
     */
    private void populateCoverFlowImage(String imagePathFromSpotify) {
        if (coverFlowImageView != null) {
            //TODO add placeholder color
            Picasso.with(getActivity()).load(imagePathFromSpotify).centerCrop().into(coverFlowImageView);
        }
    }

    /**
     * Detects touches on the fragment which indicate a play or pause event depending on the state of the track.
     *
     * @param touchArea
     */
    private void detectTouchForPlayPause(View touchArea) {
        if (touchArea != null) {
            touchArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activityControllerCallback.playPauseSong(trackID);
                }
            });
        }
    }
}