package com.ekamp.shufflefy.api.model;

import java.util.List;

/**
 * Persistent data model in order to keep track of the previously downloaded track and playlist information.
 *
 * @author Erik Kamp
 * @since 7/27/15.
 */
public class SpotifyData {

    public static SpotifyData spotifyDataInstance;

    private List<PlayList> userPlayLists;
    private String apiAccessToken;

    public static SpotifyData getInstance() {
        if (spotifyDataInstance == null) {
            spotifyDataInstance = new SpotifyData();
        }
        return spotifyDataInstance;
    }

    public String getApiAccessToken() {
        return apiAccessToken;
    }

    public List<PlayList> getUserPlayLists() {
        return userPlayLists;
    }

    public void setUserPlayLists(List<PlayList> userPlayLists) {
        this.userPlayLists = userPlayLists;
    }

    public void setApiAccessToken(String apiAccessToken) {
        this.apiAccessToken = apiAccessToken;
    }

    @Override
    public String toString() {
        return "SpotifyData{" +
                "userPlayLists=" + userPlayLists +
                '}';
    }
}
