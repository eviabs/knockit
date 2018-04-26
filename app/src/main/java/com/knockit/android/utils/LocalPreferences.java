package com.knockit.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.webkit.URLUtil;

/**
 * This class wraps the SharedPreferences objects which is modified by the SettingsFragment.
 * We use this class to ease the process of accessing our user's preferences.
 */
public class LocalPreferences {

    // Settings keys
    private static final String SETTINGS_INITIALIZED = "init";
    private static final String SETTINGS_NUM_OF_IMAGES_TO_SHOW = "num_of_images";
    private static final String SETTINGS_NUM_OF_SEGGESTIONS = "num_of_suggestions";
    private static final String SETTINGS_CUSTOM_SERVER = "custom_server";
    private static final String SETTINGS_CUSTOM_SERVER_URL = "custom_server_url";
    private static final String SETTINGS_CUSTOM_SEARCH_PROVIDERS = "custom_search_providers";
    private static final String SETTINGS_SEARCH_PROVIDER_IMAGES = "search_provider_images";
    private static final String SETTINGS_CUSTOM_SEARCH_PROVIDERS_LIST = "custom_search_providers_list";


    // Settings default value
    private static final boolean DEFAULT_SETTINGS_INITIALIZED = false;
    private static final int DEFAULT_SETTINGS_NUM_OF_IMAGES_TO_SHOW = 20;
    private static final int DEFAULT_SETTINGS_NUM_OF_SEGGESTIONS = 5;
    private static final boolean DEFAULT_SETTINGS_CUSTOM_SERVER = false;
    private static final String DEFAULT_SETTINGS_CUSTOM_SERVER_URL = "http://10.0.0.5/";
    private static final boolean DEFAULT_SETTINGS_CUSTOM_SEARCH_PROVIDERS = false;
    private static final boolean DEFAULT_SETTINGS_SEARCH_PROVIDER_IMAGES = true;




    private SharedPreferences prefs;

    public LocalPreferences(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        // Check if any setting were set yet
        if (prefs.getBoolean(SETTINGS_INITIALIZED, DEFAULT_SETTINGS_INITIALIZED) == DEFAULT_SETTINGS_INITIALIZED) {

            // Set default settings if no settings have been applied
            prefs.edit().putBoolean(SETTINGS_INITIALIZED, !DEFAULT_SETTINGS_INITIALIZED).apply();

            prefs.edit().putString(SETTINGS_NUM_OF_IMAGES_TO_SHOW, Integer.toString(DEFAULT_SETTINGS_NUM_OF_IMAGES_TO_SHOW)).apply();
            prefs.edit().putString(SETTINGS_NUM_OF_SEGGESTIONS, Integer.toString(DEFAULT_SETTINGS_NUM_OF_SEGGESTIONS)).apply();
            prefs.edit().putBoolean(SETTINGS_CUSTOM_SERVER, DEFAULT_SETTINGS_CUSTOM_SERVER).apply();
            prefs.edit().putString(SETTINGS_CUSTOM_SERVER_URL, DEFAULT_SETTINGS_CUSTOM_SERVER_URL).apply();
            prefs.edit().putBoolean(SETTINGS_CUSTOM_SEARCH_PROVIDERS, DEFAULT_SETTINGS_CUSTOM_SEARCH_PROVIDERS).apply();
            prefs.edit().putBoolean(SETTINGS_SEARCH_PROVIDER_IMAGES, DEFAULT_SETTINGS_SEARCH_PROVIDER_IMAGES).apply();

        }
    }

    public int getNumOfImagesToShow() {
        return Integer.parseInt(prefs.getString(SETTINGS_NUM_OF_IMAGES_TO_SHOW, Integer.toString(DEFAULT_SETTINGS_NUM_OF_IMAGES_TO_SHOW)));
    }

    public int getNumOfSuggestions() {
        return Integer.parseInt(prefs.getString(SETTINGS_NUM_OF_SEGGESTIONS, Integer.toString(DEFAULT_SETTINGS_NUM_OF_SEGGESTIONS)));
    }

    public String getCustonURLServer() {
        return prefs.getString(SETTINGS_CUSTOM_SERVER_URL, DEFAULT_SETTINGS_CUSTOM_SERVER_URL);
    }

    public boolean isCustomServer() {
        return prefs.getBoolean(SETTINGS_CUSTOM_SERVER, DEFAULT_SETTINGS_CUSTOM_SERVER);
    }

    private boolean isCustomSearchProvider() {
        return prefs.getBoolean(SETTINGS_CUSTOM_SEARCH_PROVIDERS, DEFAULT_SETTINGS_CUSTOM_SEARCH_PROVIDERS);
    }


    public boolean isCustomSearchProviderImages() {
        return !isCustomSearchProvider() || prefs.getBoolean(SETTINGS_SEARCH_PROVIDER_IMAGES, DEFAULT_SETTINGS_SEARCH_PROVIDER_IMAGES);
    }

    public static boolean isValidURL(String url){
        return URLUtil.isValidUrl(url);
    }
}