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
import com.ekamp.shufflefy.api.model.Track;
import com.squareup.picasso.Picasso;

/**
 * Fragment used to display cover art for the currently playing song.
 *
 * @author Erik Kamp
 * @since 7/25/15
 */
public class CoverFlowFragment extends Fragment {

    public static String TAG = "CoverFlowFragment";
    private Track currentTrack;
    private ImageView coverFlowImageView;
    private ActivityControllerCallback activityControllerCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityControllerCallback = (ActivityControllerCallback) activity;
        } catch (Exception e) {
            Log.e(getClass().getName(), "Parent class is not the correct implementation");
        }
    }

    public static CoverFlowFragment newInstance(Track currentTrack) {
        CoverFlowFragment coverFlowFragment = new CoverFlowFragment();
        coverFlowFragment.setCurrentTrack(currentTrack);
        return coverFlowFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        coverFlowImageView = (ImageView) view.findViewById(R.id.cover_flow_image_view);

        populateCoverFlowImage();
        populateTrackInformationTextViews();
        detectTouchForPlayPause(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cover_flow, container, false);
    }

    /**
     * Sets the track for the fragment
     *
     * @param currentTrack track to be displayed in this fragment
     */
    public void setCurrentTrack(Track currentTrack) {
        this.currentTrack = currentTrack;
    }

    /**
     * Using Picasso loads the cover art for the specified song into this fragment's ImageView
     */
    private void populateCoverFlowImage() {
        if (coverFlowImageView != null) {
            //TODO add placeholder color
            Picasso.with(getActivity()).load(currentTrack.getTrackImageLocation()).error(R.color.cover_art_placeholder_color)
                    .placeholder(R.color.cover_art_placeholder_color).fit().into(coverFlowImageView);
        }
    }

    /**
     * Given the TextView was bound properly, will populate the track information TextViews.
     */
    private void populateTrackInformationTextViews() {
//        if(artistNameTextView != null)
//            artistNameTextView.setText(currentTrack.getTrackName());
//        if (songNameTextView != null)
//            songNameTextView.setText(currentTrack.getTrackName());
//        if(albumNameTextView != null)
//            artistNameTextView.setText(currentTrack.getTrackName());
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
                    activityControllerCallback.playPauseSong();
                }
            });
        }
    }
}