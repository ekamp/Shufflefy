package com.ekamp.shufflefy.api.model;

/**
 * @author Erik Kamp
 * @since 7/25/15.
 */
public class PlayList {

    private String name, trackListID, ownerUserId;

    public PlayList() {
    }

    public PlayList(String name, String trackListID, String ownerUserId) {
        this.name = name;
        this.trackListID = trackListID;
        this.ownerUserId = ownerUserId;
    }

    public String getName() {
        return name;
    }

    public String getTrackListID() {
        return trackListID;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTrackListID(String trackListID) {
        this.trackListID = trackListID;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    @Override
    public String toString() {
        return "PlayList{" +
                "name='" + name + '\'' +
                ", trackListID='" + trackListID + '\'' +
                ", ownerUserId='" + ownerUserId + '\'' +
                '}';
    }
}
