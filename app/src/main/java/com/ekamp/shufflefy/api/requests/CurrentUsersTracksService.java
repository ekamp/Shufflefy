package com.ekamp.shufflefy.api.requests;

import com.ekamp.shufflefy.api.model.Track;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * @author erikkamp
 * @since 7/25/15.
 */
public interface CurrentUsersTracksService {

    @GET("/me/tracks")
    void getCurrentUsersSavedTracks(Callback<List<Track>> callback);

}
