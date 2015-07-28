package com.ekamp.shufflefy.api.events;

import com.ekamp.shufflefy.api.model.PlayList;

import java.util.List;

/**
 * Notifies the UI that the request for a user's list of playlist data has completed.
 *
 * @author Erik Kamp
 * @since 7/27/15.
 */
public class PlayListDataDownloadedEvent {

    private List<PlayList> userPlayListData;

    public PlayListDataDownloadedEvent(List<PlayList> userPlayListData) {
        this.userPlayListData = userPlayListData;
    }

    public List<PlayList> getUserPlayListData() {
        return userPlayListData;
    }

    @Override
    public String toString() {
        return "PlayListDataDownloadedEvent{" +
                "userPlayListData=" + userPlayListData +
                '}';
    }
}
