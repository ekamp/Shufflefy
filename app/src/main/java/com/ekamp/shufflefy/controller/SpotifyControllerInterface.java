package com.ekamp.shufflefy.controller;

import com.ekamp.shufflefy.api.model.PlayList;
import com.ekamp.shufflefy.api.model.Track;

import java.util.List;

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

    /**
     * Retrieves a list of trackIDs, so that they may be queued and played using the Spotify player.
     *
     * @param playListID  playlist identifier
     * @param userID      playlists creator identifier
     */
    public void getPlayListTracks(String userID , String playListID);

    /**
     * Retrieves all playlists belonging to the current user.
     *
     * @param userID      current user identifier
     */
    public void getUserPlayLists(String userID);


    /**
     * Retrives all songs this user has saved.
     */
    public void getCurrentUsersTrackList();


    /**
     * Stores our playlist data within the persistent data model.
     *
     * @param userPlayListData list of the current user's saved playlists
     */
    public void storePlayListData(List<PlayList> userPlayListData);


    /**
     * @return list of playlists pertaining to the current user.
     */
    public List<PlayList> getUserPlayListData();

    /**
     * Stores the access token downloaded once the user approves this application for use on their
     * account.
     */
    public void storeSpotifyAccessToken(String accessToken);


    /**
     * @return access token downloaded once the user approved the application for use
     */
    public String getSpotifyAccessToken();

    /**
     * Saves the list of tracks currently in the user's song library.
     *
     * @param userTrackList downloaded user track list
     */
    public void storeUserSavedTracks(List<Track> userTrackList);

    /**
     * Retrives the list of tracks currently in the user's song library.
     *
     * @return the list of tracks currently in the user's song library.
     */
    public List<Track> getUsersSavedTracks();
}