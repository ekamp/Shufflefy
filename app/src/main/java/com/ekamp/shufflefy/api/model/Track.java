package com.ekamp.shufflefy.api.model;

/**
 * Holds data for a specified Spotify track/song.
 *
 * @author Erik Kamp
 * @since 7/25/15.
 */
public class Track {

    private String trackID, trackImageLocation, trackName, trackArtist, trackAlbum;
    private static String spotifyTrackPrefix = "spotify:track:";

    public Track(String trackID, String trackImageLocation, String trackName, String trackArtist, String trackAlbum) {
        this.trackID = trackID;
        this.trackImageLocation = trackImageLocation;
        this.trackName = trackName;
        this.trackArtist = trackArtist;
        this.trackAlbum = trackAlbum;
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

    public String getTrackPlayableName() {
        return new StringBuilder(spotifyTrackPrefix).append(trackID).toString();
    }

    public String getTrackArtist() {
        return trackArtist;
    }

    public String getTrackAlbum() {
        return trackAlbum;
    }

    public static String getSpotifyTrackPrefix() {
        return spotifyTrackPrefix;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public void setTrackArtist(String trackArtist) {
        this.trackArtist = trackArtist;
    }

    public void setTrackAlbum(String trackAlbum) {
        this.trackAlbum = trackAlbum;
    }

    public static void setSpotifyTrackPrefix(String spotifyTrackPrefix) {
        Track.spotifyTrackPrefix = spotifyTrackPrefix;
    }

    public static String getTrackPlayableName(String trackID) {
        return new StringBuilder(spotifyTrackPrefix).append(trackID).toString();
    }

    @Override
    public String toString() {
        return "Track{" +
                "trackID='" + trackID + '\'' +
                ", trackImageLocation='" + trackImageLocation + '\'' +
                '}';
    }
}
