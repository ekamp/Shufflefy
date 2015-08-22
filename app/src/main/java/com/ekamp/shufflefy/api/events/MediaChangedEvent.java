package com.ekamp.shufflefy.api.events;

/**
 * Created by erikkamp on 8/22/15.
 */
public class MediaChangedEvent {

    String newTrackID;

    public MediaChangedEvent(String newTrackID) {
        this.newTrackID = newTrackID;
    }

    public String getNewTrackID() {
        return newTrackID;
    }

    public void setNewTrackID(String newTrackID) {
        this.newTrackID = newTrackID;
    }

    @Override
    public String toString() {
        return "MediaChangedEvent{" +
                "newTrackID='" + newTrackID + '\'' +
                '}';
    }
}
