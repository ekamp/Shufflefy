package com.ekamp.shufflefy;

import android.app.Application;

import com.squareup.otto.Bus;

/**
 * Application instance and access class.
 *
 * @author Erik Kamp
 * @since 7/25/15.
 */
public class ShufflefyApplication extends Application {

    private static ShufflefyApplication shufflefyApplication;
    private static Bus applicationEventBus;

    public static ShufflefyApplication get() {
        if (shufflefyApplication == null) {
            shufflefyApplication = new ShufflefyApplication();
            applicationEventBus = new Bus();
        }
        return shufflefyApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public Bus getApplicationEventBus() {
        return applicationEventBus;
    }
}
