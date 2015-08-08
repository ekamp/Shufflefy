package com.ekamp.shufflefy.api.parsers;

import com.ekamp.shufflefy.api.model.PlayList;
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
public class UserPlayListDeSerializer implements JsonDeserializer<List<PlayList>> {

    private static final String TAG_PLAYLIST_ID = "id", TAG_PLAYLIST_NAME = "name", TAG_PLAYLIST_ACCESS_INFORMATION = "uri";

    @Override
    public List<PlayList> deserialize(JsonElement jsonRoot, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        List usersPlaylists;
        JsonArray itemArray = jsonRoot.getAsJsonObject().get("items").getAsJsonArray();
        usersPlaylists = new ArrayList(itemArray.size());
        if (itemArray != null) {
            for (JsonElement element : itemArray) {
                usersPlaylists.add(parseUserPlayList(element));
            }
        }
        return usersPlaylists;
    }

    /**
     * Parses Json playlist data into a PlayList POJO.
     *
     * @param jsonElement root JsonElement for a user's followed PlayList.
     * @return POJO PlayList.
     */
    private PlayList parseUserPlayList(JsonElement jsonElement) {
        PlayList playList = null;
        if (jsonElement != null) {
            playList = new PlayList();
            playList.setTrackListID(jsonElement.getAsJsonObject().get(TAG_PLAYLIST_ID).getAsString());
            playList.setName(jsonElement.getAsJsonObject().get(TAG_PLAYLIST_NAME).getAsString());
            playList.setOwnerUserId(extractPlayListOwnerName(jsonElement.getAsJsonObject().get(TAG_PLAYLIST_ACCESS_INFORMATION).getAsString()));
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
