package com.ekamp.shufflefy.controller;


import android.util.Log;

import com.ekamp.shufflefy.ShufflefyApplication;
import com.ekamp.shufflefy.api.events.PlayListDataDownloadedEvent;
import com.ekamp.shufflefy.api.events.TrackListDownloadedEvent;
import com.ekamp.shufflefy.api.model.PlayList;
import com.ekamp.shufflefy.api.model.SpotifyData;
import com.ekamp.shufflefy.api.model.Track;
import com.ekamp.shufflefy.api.parsers.TrackListDeSerializer;
import com.ekamp.shufflefy.api.parsers.UserPlayListDeSerializer;
import com.ekamp.shufflefy.api.requests.TrackListService;
import com.ekamp.shufflefy.api.requests.UserPlayListService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Controller backend support for this application.
 *
 * @author Erik Kamp
 * @since 7/25/15.
 */
public class SpotifyController implements SpotifyControllerInterface {

    private static SpotifyController spotifyController;
    private static RestAdapter restAdapter;
    private static RequestInterceptor requestInterceptor;
    private static final String API_END_POINT = "https://api.spotify.com/v1",
            REQUEST_HEADER_ACCEPT_KEY = "Accept:",
            REQUEST_HEADER_ACCEPT_VALUE = "application/json",
            REQUEST_HEADER_AUTHORIZATION_KEY = "Authorization:",
            REQUEST_HEADER_AUTHORIZATION_PARTIAL_VALUE = "Bearer ";


    public static SpotifyController getInstance() {
        if (spotifyController == null) {
            spotifyController = new SpotifyController();
        }
        return spotifyController;
    }

    public static RestAdapter getRestAdapter(String accessToken, Gson gsonConverter) {
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_END_POINT)
                .setRequestInterceptor(getRequestInterceptor(accessToken))
                .setConverter(new GsonConverter(gsonConverter))
                .build();
        return restAdapter;
    }

    private static RequestInterceptor getRequestInterceptor(final String accessToken) {
        if (requestInterceptor == null) {
            requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader(REQUEST_HEADER_ACCEPT_KEY, REQUEST_HEADER_ACCEPT_VALUE);
                    request.addHeader(REQUEST_HEADER_AUTHORIZATION_KEY, REQUEST_HEADER_AUTHORIZATION_PARTIAL_VALUE + accessToken);
                }
            };
        }
        return requestInterceptor;
    }

    @Override
    public String getCoverArtResource() {
        return null;
    }

    @Override
    public void getPlayListTracks(String userID, String playListID) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(List.class, new TrackListDeSerializer())
                .create();

        TrackListService trackListService = getRestAdapter(SpotifyController.getInstance().getSpotifyAccessToken(), gson).create(TrackListService.class);
        trackListService.getTrackList(userID, playListID, new Callback<List<Track>>() {
            @Override
            public void success(List<Track> playlistTracks, Response response) {
                ShufflefyApplication.get().getApplicationEventBus().post(new TrackListDownloadedEvent(playlistTracks));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(getClass().getName(), "Failure");
            }
        });
    }

    @Override
    public void getUserPlayLists(final String userID) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(List.class, new UserPlayListDeSerializer())
                .create();

        UserPlayListService userPlayListService = getRestAdapter(SpotifyController.getInstance().getSpotifyAccessToken(), gson).
                create(UserPlayListService.class);
        userPlayListService.getUserPlayLists(userID, new Callback<List<PlayList>>() {
            @Override
            public void success(List<PlayList> userPlayLists, Response response) {
                storePlayListData(userPlayLists);
                ShufflefyApplication.get().getApplicationEventBus().post(new PlayListDataDownloadedEvent(userPlayLists));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(getClass().getName(), error.toString());
            }
        });
    }

    @Override
    public void storePlayListData(List<PlayList> userPlayListData) {
        SpotifyData.getInstance().setUserPlayLists(userPlayListData);
    }

    @Override
    public List<PlayList> getUserPlayListData() {
        return SpotifyData.getInstance().getUserPlayLists();
    }

    @Override
    public void storeSpotifyAccessToken(String accessToken) {
        SpotifyData.getInstance().setApiAccessToken(accessToken);
    }

    @Override
    public String getSpotifyAccessToken() {
        return SpotifyData.getInstance().getApiAccessToken();
    }
}