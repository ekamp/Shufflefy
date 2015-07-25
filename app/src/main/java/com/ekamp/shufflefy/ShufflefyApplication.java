package com.ekamp.shufflefy;

import android.app.Application;

/**
 * Application instance and access class.
 *
 * @author Erik Kamp
 * @since  7/25/15.
 */
public class ShufflefyApplication extends Application {

    private static ShufflefyApplication shufflefyApplication;

    public static ShufflefyApplication get() {
        if (shufflefyApplication == null) {
            shufflefyApplication = new ShufflefyApplication();
        }
        return shufflefyApplication;
    }
}
