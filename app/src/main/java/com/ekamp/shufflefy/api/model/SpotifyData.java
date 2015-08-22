package com.ekamp.shufflefy.api.model;

import java.util.List;

/**
 * Session data model in order to keep track of the previously downloaded track and playlist information.
 * At this point in time the application is relying on the Spotify Authentication library to hold onto the user
 * credentials between application launches. Otherwise we are simply storing the access key per session.
 *
 * @author Erik Kamp
 * @since 7/27/15.
 */
public class SpotifyData {

    private static SpotifyData spotifyDataInstance;

    private List<PlayList> userPlayLists;
    private List<Track> userSavedTracks;
    private String apiAccessToken;

    public static SpotifyData getInstance() {
        if (spotifyDataInstance == null) {
            spotifyDataInstance = new SpotifyData();
        }
        return spotifyDataInstance;
    }

    public String getApiAccessToken() {
        if (apiAccessToken == null)
            return "";
        return apiAccessToken;
    }

    public List<Track> getUserSavedTracks() {
        return userSavedTracks;
    }

    public List<PlayList> getUserPlayLists() {
        return userPlayLists;
    }

    public void setUserPlayLists(List<PlayList> userPlayLists) {
        this.userPlayLists = userPlayLists;
    }

    public void setUserSavedTracks(List<Track> userSavedTracks) {
        this.userSavedTracks = userSavedTracks;
    }

    public void setApiAccessToken(String apiAccessToken) {
        this.apiAccessToken = apiAccessToken;
    }

    @Override
    public String toString() {
        return "SpotifyData{" +
                "userPlayLists=" + userPlayLists +
                ", userSavedTracks=" + userSavedTracks +
                ", apiAccessToken='" + apiAccessToken + '\'' +
                '}';
    }
}
