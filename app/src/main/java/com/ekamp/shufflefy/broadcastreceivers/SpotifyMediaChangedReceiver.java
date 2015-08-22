package com.ekamp.shufflefy.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ekamp.shufflefy.ShufflefyApplication;
import com.ekamp.shufflefy.activities.ActivityControllerCallback;
import com.ekamp.shufflefy.api.events.MediaChangedEvent;
import com.ekamp.shufflefy.controller.SpotifyController;

/**
 * Created by erikkamp on 8/22/15.
 */
public class SpotifyMediaChangedReceiver extends BroadcastReceiver {

    static final class BroadcastTypes {
        static final String SPOTIFY_PACKAGE = "com.spotify.music";
        static final String METADATA_CHANGED = SPOTIFY_PACKAGE + ".metadatachanged";
    }

    public SpotifyMediaChangedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (intentAction.equals(BroadcastTypes.METADATA_CHANGED)) {
            String trackId = intent.getStringExtra("id");
            ShufflefyApplication.get().getApplicationEventBus().post(new MediaChangedEvent(trackId));
        }
    }
}
