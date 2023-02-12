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
import android.content.ContentResolver;
import android.content.Context;
import android.provider.SearchIndexableResource;
import com.android.settings.R;
import android.provider.Settings;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.ListPreference;

import com.android.settings.SettingsPreferenceFragment;

import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.Indexable;
import com.android.settingslib.search.SearchIndexable;

import net.margaritov.preference.colorpicker.ColorPickerPreference;
import com.stag.horns.preferences.Utils;
import com.stag.horns.preferences.CustomSeekBarPreference;
import com.stag.horns.preferences.SystemSettingSwitchPreference;

import java.util.ArrayList;
import java.util.List;

import com.stag.horns.preferences.Utils;

@SearchIndexable
public class NotificationSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener, Indexable {

    private static final String INCALL_VIB_OPTIONS = "incall_vib_options";

    private static final String ALERT_SLIDER_CAT = "alert_slider_cat";
    
    private static final String KEY_BATTERY_CHARGING_LIGHT = "battery_light_options";

    private Preference mBatteryLightPref;
    private SystemSettingSwitchPreference mLowBatteryBlinking; 

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.horns_notifications);
	ContentResolver resolver = getActivity().getContentResolver();
	final PreferenceScreen prefScreen = getPreferenceScreen();

        PreferenceCategory incallVibCategory = (PreferenceCategory) findPreference(INCALL_VIB_OPTIONS);
        if (!Utils.isVoiceCapable(getActivity())) {
            prefScreen.removePreference(incallVibCategory);
        }

	final PreferenceCategory alertSliderCat = (PreferenceCategory) findPreference(ALERT_SLIDER_CAT);
         boolean alertSliderAvailable = getActivity().getResources().getBoolean(
                 com.android.internal.R.bool.config_hasAlertSlider);
        if (!alertSliderAvailable)
            prefScreen.removePreference(alertSliderCat);

        mBatteryLightPref = (Preference) findPreference(KEY_BATTERY_CHARGING_LIGHT);
        if (!getResources()
                .getBoolean(com.android.internal.R.bool.config_intrusiveBatteryLed))
        {
                prefScreen.removePreference(mBatteryLightPref);
        }
        mLowBatteryBlinking = (SystemSettingSwitchPreference)prefScreen.findPreference("battery_light_low_blinking");
        if (getResources().getBoolean(
                        com.android.internal.R.bool.config_ledCanPulse)) {
            mLowBatteryBlinking.setChecked(Settings.System.getIntForUser(getContentResolver(),
                            Settings.System.BATTERY_LIGHT_LOW_BLINKING, 0, UserHandle.USER_CURRENT) == 1);
            mLowBatteryBlinking.setOnPreferenceChangeListener(this);
        } else {
            prefScreen.removePreference(mLowBatteryBlinking);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
	if (preference == mLowBatteryBlinking) {
            boolean value = (Boolean) newValue;
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.BATTERY_LIGHT_LOW_BLINKING, value ? 1 : 0,
                    UserHandle.USER_CURRENT);
            mLowBatteryBlinking.setChecked(value);
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.HORNS;
    }

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
		new BaseSearchIndexProvider() {
			@Override
			public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
					boolean enabled) {
				ArrayList<SearchIndexableResource> result =
						new ArrayList<SearchIndexableResource>();

				SearchIndexableResource sir = new SearchIndexableResource(context);
				sir.xmlResId = R.xml.horns_notifications;
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
