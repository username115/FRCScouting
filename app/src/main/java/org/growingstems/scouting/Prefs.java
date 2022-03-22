/*
 * Copyright 2014 Daniel Logan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.growingstems.scouting;

import java.util.Collections;
import java.util.List;

import org.frc836.database.DB;
import org.frc836.database.DBSyncService;
import org.frc836.database.DBSyncService.LocalBinder;
import org.frc836.database.HttpCallback;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.android.volley.VolleyError;

public class Prefs extends PreferenceFragmentCompat {

    private EditTextPreference passP;

    private EditTextPreference urlP;

    private SwitchPreferenceCompat syncPreference;

    private ListPreference eventP;

    private static final String URL = "https://growingstems.org/scouting.php";

    private DB db;

    private LocalBinder binder;
    private final ServiceWatcher watcher = new ServiceWatcher();

	@Override
	public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
		setPreferencesFromResource(R.xml.mainprefs, rootKey);

		passP = findPreference("passPref");
		urlP = findPreference("databaseURLPref");
		syncPreference = findPreference("enableSyncPref");

		passP.setOnPreferenceChangeListener(new onPassChangeListener(true));
		urlP.setOnPreferenceChangeListener(new onPassChangeListener(false));
		syncPreference
				.setOnPreferenceChangeListener(new OnSyncChangeListener());

		findPreference("syncFreqPref").setEnabled(
				getAutoSync(getPreferenceManager().getContext(), false));

		eventP = findPreference("eventPref");

		db = new DB(getPreferenceManager().getContext(), binder);

		List<String> events = db.getEventList();
		if (events != null)
			updateEventPreference(events);

		Intent intent = new Intent(getPreferenceManager().getContext(), DBSyncService.class);
		getPreferenceManager().getContext().bindService(intent, watcher, Context.BIND_AUTO_CREATE);

	}

	@Override
    public void onDestroyView() {
		super.onDestroyView();
		unbindDB();
	}

    public void unbindDB() {
		getPreferenceManager().getContext().unbindService(watcher);
    }

    protected class ServiceWatcher implements ServiceConnection {

        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service instanceof LocalBinder) {
                binder = (LocalBinder) service;
                db.setBinder(binder);
            }
        }

        public void onServiceDisconnected(ComponentName name) {
        }

    }

    private void updateEventPreference(List<String> events) {
        if (!events.isEmpty())// || eventP.getEntries()==null)
        {
            Collections.sort(events);
            String[] entries = events.toArray(new String[0]);
            eventP.setEntries(entries);
            eventP.setEntryValues(entries);
        }
    }

    private class onPassChangeListener implements Preference.OnPreferenceChangeListener {

        private final boolean isPass;

        public onPassChangeListener(boolean pass) {
            isPass = pass;
        }

        public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
            if (isPass) {
                DB db = new DB(getPreferenceManager().getContext(), null); // does not perform
                // database
                // sync operations
                db.checkPass(newValue.toString(), new PasswordCallback(true));
			} else {
                String ret = newValue.toString();
                if (ret.length() > 0 && !ret.contains("://")) {
                    ret = "https://" + ret;
                }
                binder.refreshNotification(ret);
			}
			return true;
		}

    }

    private class OnSyncChangeListener implements Preference.OnPreferenceChangeListener {

        @Override
        public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
            if (!(newValue instanceof Boolean))
                return false;
            Boolean checked = (Boolean) newValue;
            findPreference("syncFreqPref").setEnabled(checked);
            return true;
        }

    }

    protected class PasswordCallback implements HttpCallback {

        private final boolean isPass;

        public PasswordCallback(boolean pass) {
            isPass = pass;
        }

        @Override
        public void onResponse(@NonNull String resp) {
            Toast toast;
            try {
                if (resp.contains("success")) {
                    toast = Toast.makeText(getPreferenceManager().getContext(),
                            "Password confirmed", Toast.LENGTH_SHORT);
                    if (binder != null) {
                        binder.setPassword(getSavedPassword(getPreferenceManager().getContext()));
                        binder.initSync();
                    }
                } else
                    toast = Toast.makeText(getPreferenceManager().getContext(),
                            "Invalid password", Toast.LENGTH_SHORT);
            } catch (Exception e) {
                toast = Toast.makeText(getPreferenceManager().getContext(), "Invalid password",
                        Toast.LENGTH_SHORT);
            }
            if (isPass)
                toast.show();
        }

        @Override
        public void onError(@NonNull VolleyError e) {
            Toast toast = Toast.makeText(getPreferenceManager().getContext(),
                    "Cannot connect to Server", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static String getSavedPassword(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString("passPref", "");
    }

    public static String getScoutingURL(Context context) {
        String ret = PreferenceManager.getDefaultSharedPreferences(context)
                .getString("databaseURLPref", URL);
        if (ret.length() > 0 && !ret.contains("://")) {
            ret = "https://" + ret;
        }
        return ret;
    }

    public static String getScoutingURLNoDefault(Context context) {
        String ret = PreferenceManager.getDefaultSharedPreferences(context)
                .getString("databaseURLPref", "");
        if (ret.length() > 0 && !ret.contains("://")) {
            ret = "https://" + ret;
        }
        return ret;
    }

    public static String getEvent(Context context, String defaultValue) {
        try {
            return PreferenceManager.getDefaultSharedPreferences(context)
                    .getString("eventPref", defaultValue);
        } catch (NullPointerException e) {
            return defaultValue;
        }
    }

    public static boolean getRobotPicPref(Context context, boolean defaultValue) {
        try {
            return PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean("robotPicPref", defaultValue);
        } catch (NullPointerException e) {
            return defaultValue;
        }
    }

    public static boolean getPracticeMatch(Context context, boolean defaultValue) {
        try {
            return PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean("practiceMatchPref", defaultValue);
        } catch (NullPointerException e) {
            return defaultValue;
        }
    }

    public static String getDefaultTeamNumber(Context context,
                                              String defaultValue) {
        try {
            return PreferenceManager.getDefaultSharedPreferences(context)
                    .getString("teamPref", defaultValue);
        } catch (NullPointerException e) {
            return defaultValue;
        }
    }

    public static boolean getAutoSync(Context context, boolean defaultValue) {
        try {
            return PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean("enableSyncPref", defaultValue);
        } catch (NullPointerException e) {
            return defaultValue;
        }
    }

    public static String getPosition(Context context, String defaultValue) {
        try {
            return PreferenceManager.getDefaultSharedPreferences(context)
                    .getString("positionPref", defaultValue);
        } catch (NullPointerException e) {
            return defaultValue;
        }
    }

    public static Boolean getRedLeft(Context context, Boolean defaultValue) {
        try {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("redLeft", defaultValue);
        } catch (NullPointerException e) {
            return defaultValue;
        }
    }

    public static void setRedLeft(Context context, boolean redLeft) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("redLeft", redLeft);
        editor.apply();
    }

    public static int getMilliSecondsBetweenSyncs(Context context,
                                                  final int defaultValue) {
        try {
            String val = PreferenceManager.getDefaultSharedPreferences(context)
                    .getString("syncFreqPref", "");

            int secs;
            if (val == null || val.length() == 0)
                return defaultValue;
            try {
                secs = Integer.parseInt(val.split(" ")[0]) * 60 * 1000;
            } catch (NumberFormatException e) {
                return defaultValue;
            }
            return secs;
        } catch (NullPointerException e) {
            return defaultValue;
        }
    }

    public static boolean getDontPrompt(Context context, boolean defaultValue) {
        try {
            return PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean("doNotAskURL", defaultValue);
        } catch (NullPointerException e) {
            return defaultValue;
        }
    }

    public static void setDontPrompt(Context context, boolean dontPrompt) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("doNotAskURL", dontPrompt);
        editor.apply();
    }


    public static String getDeviceName(Context context,
                                              String defaultValue) {
        try {
            return PreferenceManager.getDefaultSharedPreferences(context)
                    .getString("deviceName", defaultValue);
        } catch (NullPointerException e) {
            return defaultValue;
        }
    }

}
