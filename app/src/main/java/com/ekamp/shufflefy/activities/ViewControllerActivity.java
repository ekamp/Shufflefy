package com.ekamp.shufflefy.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekamp.shufflefy.R;
import com.ekamp.shufflefy.adapters.CoverFlowViewPagerAdapter;
import com.ekamp.shufflefy.api.events.CurrentUserTrackListDownloadedEvent;
import com.ekamp.shufflefy.api.model.Track;
import com.ekamp.shufflefy.controller.SpotifyController;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.PlayerStateCallback;
import com.spotify.sdk.android.player.Spotify;
import com.squareup.otto.Subscribe;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnPageChange;

/**
 * View Controller Activity that controls what the user sees and hears in the main context of the application.
 * Is in charge of managing the ViewPager instance along with managing the Spotify (Android SDK) instance.
 *
 * @author Erik Kamp
 * @since 7/25/2015
 */
public class ViewControllerActivity extends FragmentActivity implements ActivityControllerCallback, ConnectionStateCallback, PlayerNotificationCallback {

    @Bind(R.id.parent_viewgroup)
    RelativeLayout parentViewGroup;

    @Bind(R.id.cover_flow_view_pager)
    ViewPager coverFlowViewPager;

    @Bind(R.id.current_track_name)
    TextView trackNameTextView;

    @Bind(R.id.current_track_artist)
    TextView trackArtistTextView;

    @Bind(R.id.current_track_album)
    TextView trackAlbumTextView;

    private Player spotifyPlayer;
    private final static int REQUEST_ID = 1337;
    private int previousCoverPosition = 0;
    private boolean viewPagerListenerEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        authenticateSpotifyUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SpotifyController.getInstance().getApplicationEventBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SpotifyController.getInstance().getApplicationEventBus().unregister(this);
    }

    /**
     * Binds our CoverFlow ViewPager to its respected ViewPagerAdapter.
     */
    private void setupViewPager() {
        coverFlowViewPager.setAdapter(new CoverFlowViewPagerAdapter(getSupportFragmentManager()));
        coverFlowViewPager.setOffscreenPageLimit(3);
        coverFlowViewPager.setClipToPadding(false);
        coverFlowViewPager.setPageMargin((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
    }

    /**
     * Binds a  OnPageChangeListener to the album cover art ViewPager in order to notify the Player instance
     * of a track change.
     *
     * @param newTrackPosition new page position within our cover art ViewPager.
     */
    @OnPageChange(R.id.cover_flow_view_pager)
    public void onCoverArtChangedListener(int newTrackPosition) {
        if (!viewPagerListenerEnabled) {
            viewPagerListenerEnabled = true;
            return;
        }
        if (newTrackPosition > previousCoverPosition) {
            spotifyPlayer.skipToNext();
        } else {
            spotifyPlayer.skipToPrevious();
        }
        updateTrackInformationTextView(newTrackPosition);
        previousCoverPosition = newTrackPosition;
    }

    /**
     * Updates the Activity content to reflect the currently playing track
     *
     * @param currentTrackPosition the currently playing tracks location in our persistent data.
     */
    private void updateTrackInformationTextView(int currentTrackPosition) {
        if (trackAlbumTextView == null || trackArtistTextView == null || trackNameTextView == null)
            return;
        Track currentTrackPointer = SpotifyController.getInstance().getUsersSavedTracks().get(currentTrackPosition);
        trackArtistTextView.setText(currentTrackPointer.getTrackArtist());
        trackNameTextView.setText(currentTrackPointer.getTrackName());
        trackAlbumTextView.setText(currentTrackPointer.getTrackAlbum());
    }

    /**
     * Starts the Spotify authentication process utilizing the v1.0 Spotify authentication library.
     * Under to hood creates a new Activity which is tasked with logging the user in and retrieving an
     * access token from the Oauth login process.
     */
    private void authenticateSpotifyUser() {
        AuthenticationRequest.Builder authenticationBuilder = new AuthenticationRequest.Builder(getString(R.string.spotify_client_id),
                AuthenticationResponse.Type.TOKEN,
                getString(R.string.spotify_redirect_url));
        authenticationBuilder.setScopes(new String[]{getString(R.string.spotify_user_library_read_scope), getString(R.string.spotify_user_access), getString(R.string.spotify_api_type)});
        AuthenticationRequest authenticationRequest = authenticationBuilder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_ID, authenticationRequest);
    }

    /**
     * Determines if the Spotify Player instance is ready to use.
     *
     * @return true if ready false otherwise.
     */
    private boolean isSpotifyPlayerReady() {
        return spotifyPlayer != null && spotifyPlayer.isInitialized();
    }

    /**
     * Populates the SpotifyPlayer instance with a few of the current user's saved tracks.
     */
    private void queuePlayerWithUserSongs() {
        if (isSpotifyPlayerReady()) {
            for (Track track : SpotifyController.getInstance().getUsersSavedTracks()) {
                spotifyPlayer.queue(track.getTrackPlayableName());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID) {
            //Collect the LoginActivity intent response through the use of the Spotify AuthenticationClient.
            final AuthenticationResponse loginResponce = AuthenticationClient.getResponse(resultCode, data);
            //Check to make sure this is a login authentication response
            if (loginResponce.getType() == AuthenticationResponse.Type.TOKEN) {
                //Grab the configuration for our player from the response
                Config spotifyPlayerConfig = new Config(this, loginResponce.getAccessToken(), getString(R.string.spotify_client_id));
                spotifyPlayerConfig.useCache(true);

                //Create a new Spotify Player instance.
                spotifyPlayer = Spotify.getPlayer(spotifyPlayerConfig, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player) {
                        setUpSpotifyPlayer(loginResponce);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e(getClass().getName(), "Cannot instantiate Spotify Player");
                    }
                });
            }
        }
    }

    /**
     * General setup for the current SpotifyPlayer instance.
     *
     * @param loginResponce AuthenticationResponse collected from the Spotify API's LoginActivity.
     */
    private void setUpSpotifyPlayer(AuthenticationResponse loginResponce) {
        bindSpotifyPlayerActivityCallbacks();
        requestCurrentUsersTrackList(loginResponce);
    }

    /**
     * Binds the SpotifyPlayer track callbacks to our current activity.
     */
    private void bindSpotifyPlayerActivityCallbacks() {
        spotifyPlayer.addConnectionStateCallback(ViewControllerActivity.this);
        spotifyPlayer.addPlayerNotificationCallback(ViewControllerActivity.this);
    }

    /**
     * Requests the current tracklist from the CurrentUsersTracksService.
     *
     * @param loginResponce AuthenticationResponse collected from the Spotify API's LoginActivity.
     */
    private void requestCurrentUsersTrackList(AuthenticationResponse loginResponce) {
        SpotifyController.getInstance().storeSpotifyAccessToken(loginResponce.getAccessToken());
        SpotifyController.getInstance().getCurrentUsersTrackList();
    }

    @Override
    protected void onDestroy() {
        try {
            Spotify.awaitDestroyPlayer(this, 10, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            Log.e(getClass().getName(), "Could not destroy player");
        }
        super.onDestroy();
    }

    @Override
    public void onLoggedIn() {
        Snackbar.make(parentViewGroup, getString(R.string.user_notification_logged_in), Snackbar.LENGTH_SHORT);
    }

    @Override
    public void onLoggedOut() {
        Snackbar.make(parentViewGroup, getString(R.string.user_notification_logged_out), Snackbar.LENGTH_SHORT);
    }

    @Override
    public void onLoginFailed(Throwable throwable) {
        Snackbar.make(parentViewGroup, getString(R.string.user_notification_error_login), Snackbar.LENGTH_SHORT);
        AuthenticationClient.logout(this);
        authenticateSpotifyUser();
    }

    @Override
    public void onTemporaryError() {
        Snackbar.make(parentViewGroup, getString(R.string.user_notification_unknown_error), Snackbar.LENGTH_SHORT);
    }

    @Override
    public void onConnectionMessage(String s) {
        Log.e(getClass().getName(), "Connected " + s);
    }

    @Override
    public void playPauseSong() {
        spotifyPlayer.getPlayerState(new PlayerStateCallback() {
            @Override
            public void onPlayerState(PlayerState playerState) {
                if (playerState.playing) {
                    spotifyPlayer.pause();
                } else {
                    spotifyPlayer.resume();
                }
            }
        });
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        if (SpotifyController.getPlayBackEventHandler().isTrackChangeEvent(eventType)) {
            viewPagerListenerEnabled = false;

            if (coverFlowViewPager.getChildCount() == coverFlowViewPager.getCurrentItem())
                return;

            updateTrackInformationTextView(coverFlowViewPager.getCurrentItem() + 1);
            coverFlowViewPager.setCurrentItem(coverFlowViewPager.getCurrentItem() + 1, true);
        }
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorString) {
        Snackbar.make(parentViewGroup, errorString, Snackbar.LENGTH_SHORT);
    }

    @Subscribe
    public void onCurrentTrackListDownloaded(CurrentUserTrackListDownloadedEvent currentUserTrackListDownloadedEvent) {
        //Once we have downloaded we just want to store and play the users first song from their library.
        SpotifyController.getInstance().storeUserSavedTracks(currentUserTrackListDownloadedEvent.getTrackList());
        queuePlayerWithUserSongs();
        setupViewPager();
        updateTrackInformationTextView(0);
    }
}