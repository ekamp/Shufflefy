package com.ekamp.shufflefy.api.parsers;

import com.ekamp.shufflefy.api.model.PlayList;
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
public class UserPlayListDeSerializer implements JsonDeserializer<List<PlayList>> {

    private static final String TAG_PLAYLIST_ID = "id", TAG_PLAYLIST_NAME = "name",
            TAG_PLAYLIST_ACCESS_INFORMATION = "uri", TAG_TRACK_ARRAY = "items";

    @Override
    public List<PlayList> deserialize(JsonElement jsonRoot, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject rootPlayListObject = jsonRoot.getAsJsonObject();
        return createListOfPlayLists(rootPlayListObject);
    }

    /**
     * Creates a list of POJO PlayList objects from a raw list of Json PlayList Objects.
     *
     * @param rootPlayListObject root JsonObject containing Json PlayList Objects.
     * @return list of POJO PlayList objects.
     */
    private List<PlayList> createListOfPlayLists(JsonObject rootPlayListObject) {
        JsonArray trackArray;
        List usersPlaylists = null;
        if (rootPlayListObject.has(TAG_TRACK_ARRAY)) {
            trackArray = rootPlayListObject.get(TAG_TRACK_ARRAY).getAsJsonArray();
            usersPlaylists = new ArrayList(trackArray.size());
            for (JsonElement element : trackArray) {
                usersPlaylists.add(parseUserPlayList(element.getAsJsonObject()));
            }
        }
        return usersPlaylists;
    }

    /**
     * Parses Json playlist data into a PlayList POJO.
     *
     * @param jsonObject root JsonObject for a user's followed PlayList.
     * @return POJO PlayList.
     */
    private PlayList parseUserPlayList(JsonObject jsonObject) {
        PlayList playList = null;
        if (jsonObject != null) {
            playList = new PlayList();

            if (jsonObject.has(TAG_PLAYLIST_ID)) {
                playList.setTrackListID(jsonObject.get(TAG_PLAYLIST_ID).getAsString());
            }

            if (jsonObject.has(TAG_PLAYLIST_NAME)) {
                playList.setName(jsonObject.get(TAG_PLAYLIST_NAME).getAsString());
            }

            if (jsonObject.has(TAG_PLAYLIST_ACCESS_INFORMATION)) {
                playList.setOwnerUserId(extractPlayListOwnerName(jsonObject.get(TAG_PLAYLIST_ACCESS_INFORMATION).getAsString()));
            }

        }
        return playList;
    }

    /**
     * Grabs the owner of the PlayList that a user is following. If a user has created their own playlist it will simply populate with their username.
     *
     * @param playlistAccessInformation url grabbed from the returned JSON data.
     * @return username of the user that created the followed playlist.
     */
    private String extractPlayListOwnerName(String playlistAccessInformation) {
        return playlistAccessInformation.substring(playlistAccessInformation.indexOf("user:") + 5, playlistAccessInformation.indexOf(":playlist"));
    }
}
