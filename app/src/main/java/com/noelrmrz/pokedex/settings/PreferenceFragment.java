package com.noelrmrz.pokedex.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.noelrmrz.pokedex.R;

public class PreferenceFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }
}