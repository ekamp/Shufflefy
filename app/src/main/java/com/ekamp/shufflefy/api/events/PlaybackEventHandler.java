package com.ekamp.shufflefy.api.events;

import com.spotify.sdk.android.player.PlayerNotificationCallback;

/**
 * Handler used to determine if a Spotify PlaybackEvent warrants a change in the cover art.
 * Logically this is done by determining if the user triggered the Skip Forward within the track queue.
 *
 * @author Erik Kamp
 * @since 8/23/15.
 */
public class PlaybackEventHandler {

    String previousEventType;
    private static final String playBackEventSkipNext = PlayerNotificationCallback.EventType.SKIP_NEXT.name(),
            playBackEventBecameActive = PlayerNotificationCallback.EventType.BECAME_ACTIVE.name(),
            playBackEventTrackChanged = PlayerNotificationCallback.EventType.TRACK_CHANGED.name();

    public PlaybackEventHandler() {
    }

    /**
     * Stores the previously triggered Spotify PlaybackEvent name.
     *
     * @param eventType previously triggered PlaybackEvent.
     */
    private void storePlaybackEvent(PlayerNotificationCallback.EventType eventType) {
        previousEventType = eventType.name();
//        Log.e(getClass().getName(), "Storing " + eventType.name());
    }

    /**
     * Determines if the currently triggered PlayBackEvent warrants a cover art ViewPager
     * page change.
     *
     * @param eventType triggered PlayBackEvent.
     * @return true if the PlayBack event encountered should change the cover art.
     */
    public boolean isTrackChangeEvent(PlayerNotificationCallback.EventType eventType) {
        if (previousEventType == null) {
            storePlaybackEvent(eventType);
            return false;
        }

        //We wish to change the cover art if a track change occurs via a change in the queue and
        // not a change caused by the user swiping, or caused by the initial start of the track queue.
        boolean systemChangedEvent = eventType.name().equals(playBackEventTrackChanged)
                && !(previousEventType.equals(playBackEventSkipNext)
                || previousEventType.equals(playBackEventBecameActive));

        storePlaybackEvent(eventType);
        return systemChangedEvent;
    }
}
