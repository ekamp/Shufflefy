package com.ekamp.shufflefy.api.parsers;

import android.util.Log;

import com.ekamp.shufflefy.api.model.PlayList;
import com.ekamp.shufflefy.api.model.Track;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
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

    private static final String TAG_PLAYLIST_ROOT = "items", TAG_PLAYLIST_TRACK_ROOT = "track", TAG_TRACK_NAME = "name", TAG_TRACK_ID = "id", TAG_IMAGE_LOCATION = "href";

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
            track.setTrackID(jsonElement.getAsJsonObject().get(TAG_TRACK_ID).getAsString());
            track.setTrackName(jsonElement.getAsJsonObject().get(TAG_TRACK_NAME).getAsString());
            track.setTrackImageLocation(jsonElement.getAsJsonObject().get(TAG_IMAGE_LOCATION).getAsString());
        }
        return track;
    }
}
