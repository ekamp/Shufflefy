package com.ekamp.shufflefy.controller;

/**
 * Interface used to define functions that will be supported by our controller.
 *
 * @author Erik Kamp
 * @since 7/25/15.
 */
public interface SpotifyControllerInterface {
    /**
     * Retrieves the cover art based on the track or song id.
     *
     * @return resource url to the song/tracks cover art.
     */
    public String getCoverArtResource();
}