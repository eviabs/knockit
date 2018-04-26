package com.knockit.android;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Our App class.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // This line needed for the ImageViewer we use in the QwantImagesAdapter
        Fresco.initialize(this);
    }
    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;
}
