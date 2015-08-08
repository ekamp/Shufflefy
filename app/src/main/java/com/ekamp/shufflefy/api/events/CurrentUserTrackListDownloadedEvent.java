package com.ekamp.shufflefy.api.events;

import com.ekamp.shufflefy.api.model.Track;

import java.util.List;

/**
 * Event bus callback referenced when the currently logged in user's track list is successfully downloaded.
 *
 * @author Erik Kamp
 * @since 7/25/15.
 */
public class CurrentUserTrackListDownloadedEvent {

    private List<Track> trackList;

    public CurrentUserTrackListDownloadedEvent(List<Track> trackList) {
        this.trackList = trackList;
    }

    public List<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
    }

    @Override
    public String toString() {
        return "CurrentUserTrackListDownloaded{" +
                "trackList=" + trackList +
                '}';
    }
}
