package com.knockit.android.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.knockit.android.activities.MainActivity;
import com.knockit.android.R;
import com.knockit.android.utils.LocalPreferences;
import com.takisoft.fix.support.v7.preference.EditTextPreference;
import com.takisoft.fix.support.v7.preference.PreferenceCategory;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompatDividers;

public class SettingsFragment extends PreferenceFragmentCompatDividers {

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        // additional setup
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            return super.onCreateView(inflater, container, savedInstanceState);
        } finally {

            Preference version = (Preference)  getPreferenceManager().findPreference("version");
            EditTextPreference customServer = (EditTextPreference)  getPreferenceManager().findPreference("custom_server_url");

            // Try to update the version
            try {
                PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                version.setSummary(String.format(getActivity().getResources().getString(R.string.settings_version_string),  pInfo.versionName, pInfo.versionCode));

            } catch (PackageManager.NameNotFoundException e) {
                version.setSummary("");
                e.printStackTrace();
            }



            setDividerPreferences(DIVIDER_PADDING_CHILD | DIVIDER_CATEGORY_AFTER_LAST | DIVIDER_CATEGORY_BETWEEN);

            PreferenceCategory categoryCustomSearch = (PreferenceCategory)  getPreferenceManager().findPreference("category_custom_search_providers");

        }
    }
}
