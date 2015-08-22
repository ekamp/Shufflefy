package com.ekamp.shufflefy.api.events;

import com.ekamp.shufflefy.api.model.Track;

import java.util.List;

/**
 * Notifies the UI that the request for a playlist's track data has completed.
 *
 * @author Erik Kamp
 * @since 7/27/15.
 */
public class TrackListDownloadedEvent {

    private final List<Track> playlistTrackList;

    public TrackListDownloadedEvent(List<Track> playlistTrackList) {
        this.playlistTrackList = playlistTrackList;
    }

    public List<Track> getPlaylistTrackList() {
        return playlistTrackList;
    }

    @Override
    public String toString() {
        return "TrackListDownloadedEvent{" +
                "playlistTrackList=" + playlistTrackList +
                '}';
    }
}
