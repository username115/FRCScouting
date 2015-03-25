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

import java.util.List;

import org.frc836.database.DB;
import org.frc836.database.DBSyncService;
import org.frc836.database.DBSyncService.LocalBinder;
import org.growingstems.scouting.R;
import org.sigmond.net.HttpCallback;
import org.sigmond.net.HttpRequestInfo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class Prefs extends PreferenceActivity {

	public static final int PREFS_ACTIVITY_CODE = 64738;

	private EditTextPreference passP;

	private EditTextPreference urlP;

	private CheckBoxPreference syncPreference;

	private ListPreference eventP;

	private static final String URL = "https://robobees.org/scouting.php";

	private DB db;

	private LocalBinder binder;
	private ServiceWatcher watcher = new ServiceWatcher();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.mainprefs);

		passP = (EditTextPreference) findPreference("passPref");
		urlP = (EditTextPreference) findPreference("databaseURLPref");
		syncPreference = (CheckBoxPreference) findPreference("enableSyncPref");

		passP.setOnPreferenceChangeListener(new onPassChangeListener(true));
		urlP.setOnPreferenceChangeListener(new onPassChangeListener(false));
		syncPreference
				.setOnPreferenceChangeListener(new OnSyncChangeListener());

		findPreference("syncFreqPref").setEnabled(
				getAutoSync(getApplicationContext(), false));

		eventP = (ListPreference) findPreference("eventPref");

		db = new DB(this, null);

		List<String> events = db.getEventList();
		if (events != null)
			updateEventPreference(events);

		Intent intent = new Intent(getApplicationContext(), DBSyncService.class);
		bindService(intent, watcher, Context.BIND_AUTO_CREATE);
	}

	protected class ServiceWatcher implements ServiceConnection {

		boolean serviceRegistered = false;

		public void onServiceConnected(ComponentName name, IBinder service) {
			if (service instanceof LocalBinder) {
				binder = (LocalBinder) service;
			}
		}

		public void onServiceDisconnected(ComponentName name) {
		}

	}

	private void updateEventPreference(List<String> events) {
		if (!events.isEmpty())// || eventP.getEntries()==null)
		{
			String[] entries = events.toArray(new String[0]);
			eventP.setEntries(entries);
			eventP.setEntryValues(entries);
		}
	}

	private class onPassChangeListener implements OnPreferenceChangeListener {

		private boolean isPass = true;

		public onPassChangeListener(boolean pass) {
			isPass = pass;
		}

		public boolean onPreferenceChange(Preference preference, Object newValue) {
			if (isPass) {
				DB db = new DB(getBaseContext(), null); // does not perform
														// database
														// sync operations
				db.checkPass(newValue.toString(), new PasswordCallback(isPass));
				return true;
			}
			else {
				String ret = newValue.toString();
				if (ret.length() > 0 && !ret.contains("://")) {
					ret = "https://" + ret;
				}
				binder.refreshNotification(ret);
				return true;
			}
		}

	}

	private class OnSyncChangeListener implements OnPreferenceChangeListener {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			if (!(newValue instanceof Boolean))
				return false;
			Boolean checked = (Boolean) newValue;
			findPreference("syncFreqPref").setEnabled(checked);
			return true;
		}

	}

	/*
	 * public boolean onCreateOptionsMenu(Menu menu) { MenuInflater inflater =
	 * getMenuInflater(); inflater.inflate(R.menu.prefsmenu, menu); return true;
	 * }
	 * 
	 * public boolean onOptionsItemSelected(MenuItem item) { switch
	 * (item.getItemId()) { case R.id.refreshEventsItem: refreshEvents(); return
	 * true; default: return super.onOptionsItemSelected(item); } }
	 * 
	 * protected class EventListCallback implements EventCallback {
	 * 
	 * public void eventsUpdated(List<String> events) {
	 * 
	 * if (events == null) { Toast.makeText(getBaseContext(),
	 * "Error Updating Event List.", Toast.LENGTH_SHORT).show(); return; } Toast
	 * toast = Toast.makeText(getBaseContext(), "Updated Event List",
	 * Toast.LENGTH_SHORT); toast.show();
	 * 
	 * updateEventPreference(events); }
	 * 
	 * }
	 */

	protected class PasswordCallback implements HttpCallback {

		private boolean isPass = true;

		public PasswordCallback(boolean pass) {
			isPass = pass;
		}

		public void onResponse(HttpRequestInfo resp) {
			Toast toast;
			try {
				if (resp.getResponseString().contains("success")) {
					toast = Toast.makeText(getBaseContext(),
							"Password confirmed", Toast.LENGTH_SHORT);
					if (binder != null) {
						binder.setPassword(getSavedPassword(getApplicationContext()));
						binder.initSync();
					}
				} else
					toast = Toast.makeText(getBaseContext(),
							"Invalid password", Toast.LENGTH_SHORT);
			} catch (Exception e) {
				toast = Toast.makeText(getBaseContext(), "Invalid password",
						Toast.LENGTH_SHORT);
			}
			if (isPass)
				toast.show();
		}

		public void onError(Exception e) {
			Toast toast = Toast.makeText(getBaseContext(),
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
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("eventPref", defaultValue);
	}

	public static boolean getRobotPicPref(Context context, boolean defaultValue) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("robotPicPref", defaultValue);
	}

	public static boolean getPracticeMatch(Context context, boolean defaultValue) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("practiceMatchPref", defaultValue);
	}

	public static String getDefaultTeamNumber(Context context,
			String defaultValue) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("teamPref", defaultValue);
	}

	public static boolean getAutoSync(Context context, boolean defaultValue) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("enableSyncPref", defaultValue);
	}

	public static String getPosition(Context context, String defaultValue) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("positionPref", defaultValue);
	}

	public static int getMilliSecondsBetweenSyncs(Context context,
			final int defaultValue) {
		String val = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("syncFreqPref", "");

		int secs = defaultValue;
		if (val == null || val.length() == 0)
			return defaultValue;
		try {
			secs = Integer.valueOf(val.split(" ")[0]) * 60 * 1000;
		} catch (Exception e) {
			return defaultValue;
		}
		return secs;
	}

	public static boolean getDontPrompt(Context context, boolean defaultValue) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("doNotAskURL", defaultValue);
	}

	public static void setDontPrompt(Context context, boolean dontPrompt) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("doNotAskURL", dontPrompt);
		editor.apply();
	}
}
