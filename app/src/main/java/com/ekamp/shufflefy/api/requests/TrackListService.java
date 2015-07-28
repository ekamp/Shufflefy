package com.ekamp.shufflefy.api.requests;

import com.ekamp.shufflefy.api.model.Track;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Service used to download and parse a list of tracks from a specified playlist.
 *
 * @author erikkamp
 * @since 7/25/15.
 */
public interface TrackListService {

    @GET("/users/{user_id}/playlists/{playlist_id}/tracks")
    void getTrackList(@Path("user_id") String userID, @Path("playlist_id") String playListID, Callback<List<Track>> trackListCallback);
}
