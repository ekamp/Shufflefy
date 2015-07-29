package com.ekamp.shufflefy.api.model;

/**
 * Holds data for a specified Spotify track/song
 *
 * @author Erik Kamp
 * @since 7/25/15.
 */
public class Track {

    private String trackID, trackImageLocation, trackName;
    private static String spotifyTrackPrefix = "spotify:track:";

    public Track(String trackID, String trackImageLocation, String trackName) {
        this.trackID = trackID;
        this.trackImageLocation = trackImageLocation;
        this.trackName = trackName;
    }

    public Track() {
    }

    public String getTrackID() {
        return trackID;
    }

    public String getTrackImageLocation() {
        return trackImageLocation;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackID(String trackID) {
        this.trackID = trackID;
    }

    public void setTrackImageLocation(String trackImageLocation) {
        this.trackImageLocation = trackImageLocation;
    }

    public String getTrackPlayableName(){
        return new StringBuilder(spotifyTrackPrefix).append(trackID).toString();
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    @Override
    public String toString() {
        return "Track{" +
                "trackID='" + trackID + '\'' +
                ", trackImageLocation='" + trackImageLocation + '\'' +
                '}';
    }
}
