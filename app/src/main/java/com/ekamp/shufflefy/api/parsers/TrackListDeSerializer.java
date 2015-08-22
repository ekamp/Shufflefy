package com.ekamp.shufflefy.api.parsers;

import com.ekamp.shufflefy.api.model.Track;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Json Deserialization class for list of PlayLists downloaded.
 *
 * @author Erik Kamp
 * @since 7/25/15.
 */
public class TrackListDeSerializer implements JsonDeserializer<List<Track>> {

    private static final String TAG_PLAYLIST_ROOT = "items", TAG_PLAYLIST_TRACK_ROOT = "track",
            TAG_TRACK_NAME = "name", TAG_TRACK_ID = "id", TAG_ABLUM_ROOT = "album",
            TAG_ALBUM_IMAGE_ROOT = "images", TAG_ALBUM_NAME = "name",
            TAG_ALBUM_IMAGE_URL = "url", TAG_ARTIST_ROOT = "artists", TAG_ARTIST_NAME = "name";

    @Override
    public List<Track> deserialize(JsonElement jsonRoot, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonArray jsonTrackArray = jsonRoot.getAsJsonObject().get(TAG_PLAYLIST_ROOT).getAsJsonArray();

        return createTrackList(jsonTrackArray);
    }

    /**
     * Given the Json array collected from our request, creates a List of POJO tracks to be easily
     * used within our application.
     *
     * @param trackArray Json array of track JsonObjects.
     * @return POJO list of Track objects generated from Json Track objects.
     */
    private List<Track> createTrackList(JsonArray trackArray) {
        List trackList;
        JsonObject trackElementRoot;
        if (trackArray == null)
            return null;

        trackList = new ArrayList(trackArray.size());
        for (JsonElement element : trackArray) {
            trackElementRoot = element.getAsJsonObject();
            if (trackElementRoot.has(TAG_PLAYLIST_TRACK_ROOT)) {
                trackList.add(parseTrack(trackElementRoot.get(TAG_PLAYLIST_TRACK_ROOT)));
            }
        }
        return trackList;
    }

    /**
     * Parses JSON track information into POJO Track objects.
     *
     * @param jsonElement downloaded root jsonElement.
     * @return POJO Track object.
     */
    private Track parseTrack(JsonElement jsonElement) {
        Track track = null;

        if (jsonElement != null) {
            track = new Track();

            //Grab the parent JsonElement
            JsonObject parentElement = jsonElement.getAsJsonObject();

            //Album Parsing
            parseTrackAlbumInformation(track, parentElement);

            //Artist Parsing
            parseTrackArtist(track, parentElement);

            //Track Parsing
            parseTrackID(track, parentElement);
            parseTrackName(track, parentElement);
        }
        return track;
    }

    /**
     * Parses the current Json track's unique identifier.
     *
     * @param track         POJO track object to be populated.
     * @param parentElement parent Json element used for parsing
     */
    private void parseTrackID(Track track, JsonObject parentElement) {
        if (parentElement.has(TAG_TRACK_ID)) {
            track.setTrackID(parentElement.get(TAG_TRACK_ID).getAsString());
        }
    }

    /**
     * Parses the current Json track's title or name.
     *
     * @param track         POJO track object to be populated.
     * @param parentElement parent Json element used for parsing
     */
    private void parseTrackName(Track track, JsonObject parentElement) {
        if (parentElement.has(TAG_TRACK_NAME)) {
            track.setTrackName(parentElement.get(TAG_TRACK_NAME).getAsString());
        }
    }

    /**
     * Parses the current Json track's artist(s) name(s)
     *
     * @param track         POJO track object to be populated.
     * @param parentElement parent Json element used for parsing
     */
    private void parseTrackArtist(Track track, JsonObject parentElement) {
        JsonArray artistJsonInformation;
        if (parentElement.has(TAG_ARTIST_ROOT)) {
            artistJsonInformation = parentElement.get(TAG_ARTIST_ROOT).getAsJsonArray();
            track.setTrackArtist(concatinateTrackArtists(artistJsonInformation));
        }
    }

    /**
     * Parses the current Json track's Album name and cover art.
     *
     * @param track         POJO track object to be populated.
     * @param parentElement parent Json element used for parsing
     */
    private void parseTrackAlbumInformation(Track track, JsonObject parentElement) {
        JsonObject albumJsonInformation;

        if (parentElement.has(TAG_ABLUM_ROOT)) {
            albumJsonInformation = parentElement.get(TAG_ABLUM_ROOT).getAsJsonObject();
            parseAlbumCoverImage(track, albumJsonInformation);
            parseAlbumTitle(track, albumJsonInformation);
        }
    }

    /**
     * Parses the current Json track's album cover image url location.
     *
     * @param track                POJO track object to be populated
     * @param albumJsonInformation parent Json object for album information.
     */
    private void parseAlbumCoverImage(Track track, JsonObject albumJsonInformation) {
        JsonArray albumCoverArtImageArray;
        JsonObject firstAlbumCoverImage;
        String albumImageLocation;

        if (albumJsonInformation.has(TAG_ALBUM_IMAGE_ROOT)) {
            albumCoverArtImageArray = albumJsonInformation.get(TAG_ALBUM_IMAGE_ROOT).getAsJsonArray();
            //We just take the first iamge from the array as our display image.
            firstAlbumCoverImage = albumCoverArtImageArray.get(0).getAsJsonObject();
            if (firstAlbumCoverImage.has(TAG_ALBUM_IMAGE_URL)) {
                albumImageLocation = firstAlbumCoverImage.get(TAG_ALBUM_IMAGE_URL).getAsString();
                track.setTrackImageLocation(albumImageLocation);
            }
            track.setTrackAlbum(albumJsonInformation.get(TAG_ALBUM_NAME).getAsString());
        }
    }

    /**
     * Parses the current Json track's album title or name.
     *
     * @param track                POJO track object to be populated
     * @param albumJsonInformation parent Json object for album information.
     */
    private void parseAlbumTitle(Track track, JsonObject albumJsonInformation) {
        if (albumJsonInformation.has(TAG_ALBUM_NAME)) {
            track.setTrackAlbum(albumJsonInformation.get(TAG_ALBUM_NAME).getAsString());
        }
    }

    /**
     * Concatenates the artist's that contributed to a certain track.
     *
     * @param artistArray array of artists.
     * @return concatenated string of artist names
     */
    private String concatinateTrackArtists(JsonArray artistArray) {
        if (artistArray == null) return "";
        StringBuilder stringBuilder = new StringBuilder();
        JsonObject artistRoot;
        for (JsonElement artist : artistArray) {
            artistRoot = artist.getAsJsonObject();
            stringBuilder.append(artistRoot.get(TAG_ARTIST_NAME).getAsString()).append("  ");
        }

        return stringBuilder.toString().trim();
    }
}
