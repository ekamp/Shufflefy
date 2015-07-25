package com.ekamp.shufflefy.controller;


/**
 * Controller backend support for this application.
 *
 * @author Erik Kamp
 * @since 7/25/15.
 */
public class SpotifyController implements SpotifyControllerInterface {

    private static SpotifyController spotifyController;

    public static SpotifyController getInstance() {
        if (spotifyController == null) {
            spotifyController = new SpotifyController();
        }
        return spotifyController;
    }

    @Override
    public String getCoverArtResource() {
        return null;
    }
}