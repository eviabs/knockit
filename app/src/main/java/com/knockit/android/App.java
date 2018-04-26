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
}
