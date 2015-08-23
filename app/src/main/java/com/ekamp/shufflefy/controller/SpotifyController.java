package com.ekamp.shufflefy.controller;


import android.util.Log;

import com.ekamp.shufflefy.api.events.CurrentUserTrackListDownloadedEvent;
import com.ekamp.shufflefy.api.events.PlayListDataDownloadedEvent;
import com.ekamp.shufflefy.api.events.PlaybackEventHandler;
import com.ekamp.shufflefy.api.events.TrackListDownloadedEvent;
import com.ekamp.shufflefy.api.model.PlayList;
import com.ekamp.shufflefy.api.model.SpotifyData;
import com.ekamp.shufflefy.api.model.Track;
import com.ekamp.shufflefy.api.parsers.TrackListDeSerializer;
import com.ekamp.shufflefy.api.parsers.UserPlayListDeSerializer;
import com.ekamp.shufflefy.api.requests.CurrentUsersTracksService;
import com.ekamp.shufflefy.api.requests.TrackListService;
import com.ekamp.shufflefy.api.requests.UserPlayListService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Controller backend support for this application. Acts as the communication between out model and
 * our view.
 *
 * @author Erik Kamp
 * @since 7/25/15.
 */
public class SpotifyController implements SpotifyControllerInterface {

    private static SpotifyController spotifyController;
    private static RestAdapter restAdapter;
    private static RequestInterceptor requestInterceptor;
    private static PlaybackEventHandler playbackEventHandler;
    private static Bus applicationEventBus;
    private static final String API_END_POINT = "https://api.spotify.com/v1",
            REQUEST_HEADER_ACCEPT_KEY = "Accept:",
            REQUEST_HEADER_ACCEPT_VALUE = "application/json",
            REQUEST_HEADER_AUTHORIZATION_KEY = "Authorization:",
            REQUEST_HEADER_AUTHORIZATION_PARTIAL_VALUE = "Bearer ";

    /**
     * Retrieves the current Singleton instance of the controller.
     *
     * @return current singleton controller instance.
     */
    public static SpotifyController getInstance() {
        if (spotifyController == null) {
            spotifyController = new SpotifyController();
        }
        return spotifyController;
    }

    /**
     * Retrieves the current Otto event bus, used to deliver events to the UI.
     *
     * @return current Otto event bus instance.
     */
    public static Bus getApplicationEventBus() {
        if (applicationEventBus == null) {
            applicationEventBus = new Bus();
        }
        return applicationEventBus;
    }

    /**
     * Creates a REST adapter based on the current user's access token and requests data parser or
     * POJO converter.
     *
     * @param gsonConverter parser used to transform raw Json data to a POJO.
     * @return new RestAdaper instance to be used to collect information on a user, track or Playlist.
     */
    private static RestAdapter createRestAdapter(Gson gsonConverter) {
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_END_POINT)
                .setRequestInterceptor(getRequestInterceptor())
                .setConverter(new GsonConverter(gsonConverter))
                .build();
        return restAdapter;
    }

    /**
     * Retrieves the current Singleton instance of the RequestInterceptor used for Spotify data
     * requests, if an instance does not exist one is created.
     *
     * @return current Singleton instance of the RequestInterceptor.
     */
    private static RequestInterceptor getRequestInterceptor() {
        if (requestInterceptor == null) {
            requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader(REQUEST_HEADER_ACCEPT_KEY, REQUEST_HEADER_ACCEPT_VALUE);
                    request.addHeader(REQUEST_HEADER_AUTHORIZATION_KEY, REQUEST_HEADER_AUTHORIZATION_PARTIAL_VALUE + SpotifyData.getInstance().getApiAccessToken());
                }
            };
        }
        return requestInterceptor;
    }

    public static PlaybackEventHandler getPlayBackEventHandler() {
        if (playbackEventHandler == null) {
            playbackEventHandler = new PlaybackEventHandler();
        }
        return playbackEventHandler;
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

        TrackListService trackListService = createRestAdapter(gson).create(TrackListService.class);
        trackListService.getTrackList(userID, playListID, new Callback<List<Track>>() {
            @Override
            public void success(List<Track> playlistTracks, Response response) {
                getApplicationEventBus().post(new TrackListDownloadedEvent(playlistTracks));
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

        UserPlayListService userPlayListService = createRestAdapter(gson).
                create(UserPlayListService.class);
        userPlayListService.getUserPlayLists(userID, new Callback<List<PlayList>>() {
            @Override
            public void success(List<PlayList> userPlayLists, Response response) {
                storePlayListData(userPlayLists);
                getApplicationEventBus().post(new PlayListDataDownloadedEvent(userPlayLists));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(getClass().getName(), error.toString());
            }
        });
    }

    @Override
    public void getCurrentUsersTrackList() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(List.class, new TrackListDeSerializer())
                .create();

        CurrentUsersTracksService currentUsersTracksService = createRestAdapter(gson).create(CurrentUsersTracksService.class);
        currentUsersTracksService.getCurrentUsersSavedTracks(new Callback<List<Track>>() {
            @Override
            public void success(List<Track> tracks, Response response) {
                getApplicationEventBus().post(new CurrentUserTrackListDownloadedEvent(tracks));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(getClass().getName(), "Error " + error.toString());
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

    @Override
    public void storeUserSavedTracks(List<Track> userTrackList) {
        SpotifyData.getInstance().setUserSavedTracks(userTrackList);
    }

    @Override
    public List<Track> getUsersSavedTracks() {
        return SpotifyData.getInstance().getUserSavedTracks();
    }
}