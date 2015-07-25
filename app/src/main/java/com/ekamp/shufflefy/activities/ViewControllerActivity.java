package com.ekamp.shufflefy.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import com.ekamp.shufflefy.R;
import com.ekamp.shufflefy.adapters.CoverFlowViewPagerAdapter;
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


/**
 * View Controller Activity that controls what the user sees and hears in the main context of the application.
 * Is in charge of managing the ViewPager instance along with managing the Spotify (Android SDK) instance.
 *
 * @author Erik Kamp
 * @since 7/25/2015
 */
public class ViewControllerActivity extends FragmentActivity implements ActivityControllerCallback, PlayerNotificationCallback, ConnectionStateCallback {

    private ViewPager coverFlowViewPager;
    private CoverFlowViewPagerAdapter coverFlowViewPagerAdapter;
    private Player spotifyPlayer;
    private final static int REQUEST_ID = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coverFlowViewPager = (ViewPager) findViewById(R.id.cover_flow_view_pager);
        authenticateSpotifyUser();
    }

    /**
     * Binds our CoverFlow ViewPager to its respected ViewPagerAdapter.
     */
    private void setupViewPager() {
        //TODO include the Spotify SDK list of songs / cover art flow here
        coverFlowViewPager.setAdapter(new CoverFlowViewPagerAdapter(getSupportFragmentManager()));
        coverFlowViewPager.setOffscreenPageLimit(3);
    }

    /**
     * Starts the Spotify authentication process.
     */
    public void authenticateSpotifyUser() {
        AuthenticationRequest.Builder authenticationBuilder = new AuthenticationRequest.Builder(getString(R.string.spotify_client_id),
                AuthenticationResponse.Type.TOKEN,
                getString(R.string.spotify_redirect_url));
        authenticationBuilder.setScopes(new String[]{getString(R.string.spotify_user_access), getString(R.string.spotify_api_type)});
        AuthenticationRequest authenticationRequest = authenticationBuilder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_ID, authenticationRequest);
    }


    /**
     * Determines if the Spotify Player instance is ready to use.
     *
     * @return true if ready false otherwise.
     */
    private boolean isSpotifyPlayerReady(){
        return spotifyPlayer != null && spotifyPlayer.isInitialized() && spotifyPlayer.isLoggedIn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Setup for the Spotify player instance
        if (requestCode == REQUEST_ID) {
            AuthenticationResponse loginResponce = AuthenticationClient.getResponse(resultCode, data);
            if (loginResponce.getType() == AuthenticationResponse.Type.TOKEN) {
                Config spotifyPlayerConfig = new Config(this, loginResponce.getAccessToken(), getString(R.string.spotify_client_id));
                spotifyPlayer = Spotify.getPlayer(spotifyPlayerConfig, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player) {
                        spotifyPlayer.addConnectionStateCallback(ViewControllerActivity.this);
                        spotifyPlayer.addPlayerNotificationCallback(ViewControllerActivity.this);
//                        spotifyPlayer.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e(getClass().getName(), "Cannot instantiate Spotify Player");
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(spotifyPlayer);
        super.onDestroy();
    }

    @Override
    public void onLoggedIn() {

    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(Throwable throwable) {

    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {

    }

    @Override
    public void onPlaybackError(ErrorType errorType, String s) {

    }

    @Override
    public void playPauseSong(String songID) {
        if (isSpotifyPlayerReady()) {
            spotifyPlayer.getPlayerState(new PlayerStateCallback() {
                @Override
                public void onPlayerState(PlayerState playerState) {
                    if (playerState.playing) {
                        spotifyPlayer.pause();
                    } else {
//                        spotifyPlayer.play();
                    }
                }
            });
        }
    }

    @Override
    public String getCoverArtResource() {
        if(isSpotifyPlayerReady()){

        }
        return "";
    }
}
