package com.ekamp.shufflefy.api.parsers;

import com.ekamp.shufflefy.api.model.Track;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONArray;

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

        List trackList;
        JsonArray itemArray = jsonRoot.getAsJsonObject().get(TAG_PLAYLIST_ROOT).getAsJsonArray();
        trackList = new ArrayList(itemArray.size());
        if (itemArray != null) {
            for (JsonElement element : itemArray) {
                trackList.add(parseTrack(element.getAsJsonObject().get(TAG_PLAYLIST_TRACK_ROOT)));
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

            JsonObject parentElement = jsonElement.getAsJsonObject();
            JsonObject albumJsonInformation = parentElement.get(TAG_ABLUM_ROOT).getAsJsonObject();
            JsonArray artistJsonInformation = parentElement.get(TAG_ARTIST_ROOT).getAsJsonArray();

            //Artist Parsing
            track.setTrackArtist(concatinateTrackArtists(artistJsonInformation));

            //Album Parsing
            track.setTrackImageLocation(albumJsonInformation.get(TAG_ALBUM_IMAGE_ROOT).
                    getAsJsonArray().get(0).getAsJsonObject().get(TAG_ALBUM_IMAGE_URL).getAsString());
            track.setTrackAlbum(albumJsonInformation.get(TAG_ALBUM_NAME).getAsString());

            //Track Parsing
            track.setTrackID(parentElement.get(TAG_TRACK_ID).getAsString());
            track.setTrackName(parentElement.get(TAG_TRACK_NAME).getAsString());
        }
        return track;
    }

    /**
     * Concatinates the artist's that contributed to a certain track.
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
