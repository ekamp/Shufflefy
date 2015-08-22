package com.ekamp.shufflefy.activities;

/**
 * Callback interface to be utilized by fragments wanting to interact with the ViewControllerActivity.
 *
 * @author Erik Kamp
 * @since 7/25/15.
 */
public interface ActivityControllerCallback {

    /**
     * Depending on the state of the player, will play or pause a currently shown song.
     */
    void playPauseSong();
}