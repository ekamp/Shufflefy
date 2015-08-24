package com.ekamp.shufflefy.api.services;

import com.ekamp.shufflefy.api.model.Track;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Service to get the current user's saved track Json information.
 *
 * @author erikkamp
 * @since 7/25/15.
 */
public interface CurrentUsersTracksService {

    @GET("/me/tracks")
    void getCurrentUsersSavedTracks(Callback<List<Track>> callback);

}
