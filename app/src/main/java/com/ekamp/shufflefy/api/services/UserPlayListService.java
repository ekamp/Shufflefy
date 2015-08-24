package com.ekamp.shufflefy.api.services;

import com.ekamp.shufflefy.api.model.PlayList;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Service used to download and parse a list of a specified user's playlists.
 *
 * @author erikkamp
 * @since 7/25/15.
 */
public interface UserPlayListService {

    @GET("/users/{user_id}/playlists")
    void getUserPlayLists(@Path("user_id") String userID, Callback<List<PlayList>> playListCallback);
}
