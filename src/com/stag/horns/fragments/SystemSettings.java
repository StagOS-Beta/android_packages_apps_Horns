/*
 *  Copyright (C) 2015 The OmniROM Project
 *	Copyright (C) 2020 StagOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stag.horns.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.SearchIndexableResource;
import com.android.settings.R;
import android.provider.Settings;

// import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;

import com.android.settings.SettingsPreferenceFragment;

import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.Indexable;
import com.android.settingslib.search.SearchIndexable;

import com.android.internal.util.stag.udfps.UdfpsUtils;

import net.margaritov.preference.colorpicker.ColorPickerPreference;
import com.stag.horns.preferences.Utils;

import java.util.ArrayList;
import java.util.List;

@SearchIndexable
public class SystemSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

	private static final String TAG = "SystemSettings";

	// private ListPreference mCustomTheme;
        private static final String UDFPS_SETTINGS = "udfps_settings";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.horns_system);
        ContentResolver resolver = getActivity().getContentResolver();
        Context mContext = getContext();

        final PreferenceScreen prefScreen = getPreferenceScreen();

		// mCustomTheme = (ListPreference) findPreference("system_custom_theme");
		// final int currentTheme = Settings.Secure.getIntForUser(resolver,
		// 		Settings.Secure.SYSTEM_CUSTOM_THEME, 0, UserHandle.USER_CURRENT);
		// mCustomTheme.setValue(String.valueOf(currentTheme));
		// mCustomTheme.setOnPreferenceChangeListener(this);
		// Log.d(TAG, "Current theme: " + String.valueOf(currentTheme));
         if (!UdfpsUtils.hasUdfpsSupport(getContext())) {
             prefScreen.removePreference(findPreference(UDFPS_SETTINGS));
         }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.HORNS;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
		ContentResolver resolver = getActivity().getContentResolver();
		// if(preference == mCustomTheme) {
		// 	Log.d(TAG, "Custom theme changed");
		// 	int value = Integer.parseInt((String) newValue);
		// 	Log.d(TAG, "New theme: " + String.valueOf(value));
		// 	Settings.Secure.putIntForUser(resolver,
		// 			Settings.Secure.SYSTEM_CUSTOM_THEME, value, UserHandle.USER_CURRENT);
		// 	return true;
		// }
        return false;
    }

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
		new BaseSearchIndexProvider() {
			@Override
			public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
					boolean enabled) {
				ArrayList<SearchIndexableResource> result =
						new ArrayList<SearchIndexableResource>();

				SearchIndexableResource sir = new SearchIndexableResource(context);
				sir.xmlResId = R.xml.horns_system;
				result.add(sir);
				return result;
			}

			@Override
			public List<String> getNonIndexableKeys(Context context) {
				List<String> keys = super.getNonIndexableKeys(context);
				return keys;
			}
		};
}
