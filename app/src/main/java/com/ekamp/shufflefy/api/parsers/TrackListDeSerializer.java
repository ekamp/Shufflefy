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
            TAG_TRACK_NAME = "name", TAG_TRACK_ID = "id", TAG_IMAGE_LOCATION = "href",
            TAG_ABLUM_ROOT = "album", TAG_ALBUM_IMAGE_ROOT = "images", TAG_ALBUM_IMAGE_URL = "url";

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

    private Track parseTrack(JsonElement jsonElement) {
        Track track = null;
        if (jsonElement != null) {
            track = new Track();

            JsonObject parentElement = jsonElement.getAsJsonObject();
            JsonObject albumJsonInformation = parentElement.get(TAG_ABLUM_ROOT).getAsJsonObject();

            track.setTrackImageLocation(albumJsonInformation.get(TAG_ALBUM_IMAGE_ROOT).
                    getAsJsonArray().get(0).getAsJsonObject().get(TAG_ALBUM_IMAGE_URL).getAsString());
            track.setTrackID(parentElement.get(TAG_TRACK_ID).getAsString());
            track.setTrackName(parentElement.get(TAG_TRACK_NAME).getAsString());
        }
        return track;
    }
}
