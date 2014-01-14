/*
 * Copyright 2013 Daniel Logan
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
import org.growingstems.scouting.R;
import org.growingstems.scouting.EventList.EventCallback;
import org.sigmond.net.HttpCallback;
import org.sigmond.net.HttpRequestInfo;

import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class Prefs extends PreferenceActivity {

	public static final int PREFS_ACTIVITY_CODE = 64738;

	private EditTextPreference passP;

	private EventList eventList;

	private ListPreference eventP;

	private static final String URL = "http://robobees.org/scouting.php";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.mainprefs);

		passP = (EditTextPreference) findPreference("passPref");

		passP.setOnPreferenceChangeListener(new onPassChangeListener());

		eventList = new EventList(getApplicationContext());

		eventP = (ListPreference) findPreference("eventPref");
		List<String> events = eventList.getEventList();
		updateEventPreference(events);
		if (events.isEmpty())
			refreshEvents();

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

		public boolean onPreferenceChange(Preference preference, Object newValue) {

			DB db = new DB(getBaseContext());
			db.checkPass(newValue.toString(), new PasswordCallback());
			return true;
		}

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.prefsmenu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refreshEventsItem:
			refreshEvents();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void refreshEvents() {
		eventList.downloadEventsList(new EventListCallback());
	}

	protected class EventListCallback implements EventCallback {

		public void eventsUpdated(List<String> events) {

			if (events == null) {
				Toast.makeText(getBaseContext(), "Error Updating Event List.",
						Toast.LENGTH_SHORT).show();
				return;
			}
			Toast toast = Toast.makeText(getBaseContext(),
					"Updated Event List", Toast.LENGTH_SHORT);
			toast.show();

			updateEventPreference(events);
		}

	}

	protected class PasswordCallback implements HttpCallback {

		public void onResponse(HttpRequestInfo resp) {
			Toast toast;
			try {
				if (resp.getResponseString().contains("success"))
					toast = Toast.makeText(getBaseContext(),
							"Password confirmed", Toast.LENGTH_SHORT);
				else
					toast = Toast.makeText(getBaseContext(),
							"Invalid password", Toast.LENGTH_SHORT);
			} catch (Exception e) {
				toast = Toast.makeText(getBaseContext(), "Invalid password",
						Toast.LENGTH_SHORT);
			}
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
			ret = "http://" + ret;
		}
		return ret;
	}

	public static String getScoutingURLNoDefault(Context context) {
		String ret = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("databaseURLPref", "");
		if (ret.length() > 0 && !ret.contains("://")) {
			ret = "http://" + ret;
		}
		return ret;
	}

	public static String getEvent(Context context, String defaultValue) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("eventPref", defaultValue);
	}

	/*public static boolean getRobotPicPref(Context context, boolean defaultValue) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("robotPicPref", defaultValue);
	}*/

	public static String getDefaultTeamNumber(Context context,
			String defaultValue) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("teamPref", defaultValue);
	}
}
