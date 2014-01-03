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


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import org.growingstems.scouting.R;

public class MainMenuSelection {

	public static final int HELPDIALOG = 74920442;

	public static boolean onOptionsItemSelected(MenuItem item, Activity context) {
		switch (item.getItemId()) {
		case R.id.settingsitem:
			openSettings(context);
			return true;
		case R.id.refreshMatchesItem:
			refresh(context);
			return true;
		case R.id.helpitem:
			showHelp(context);
			return true;
		default:
			return false;
		}
	}

	public static void openSettings(Activity context) {
		Intent intent = new Intent(context.getBaseContext(), Prefs.class);
		context.startActivityForResult(intent, Prefs.PREFS_ACTIVITY_CODE);
	}

	public static void refresh(Activity context) {
		if (context instanceof DataActivity) {
			DataActivity act = (DataActivity) context;
			act.refreshCurrentTab();
		} else if (context instanceof MatchStatsActivity) {
			MatchStatsActivity act = (MatchStatsActivity) context;
			act.refreshStats();
		} else {
			MatchSchedule schedule = new MatchSchedule();
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(context.getBaseContext());
			schedule.updateSchedule(
					prefs.getString("eventPref", "Chesapeake Regional"),
					context, true);
		}
	}

	public static void showHelp(Activity context) {
		context.showDialog(HELPDIALOG);
	}

	public static void setRefreshItem(Menu menu, int item) {
		MenuItem i = menu.findItem(R.id.refreshMatchesItem);
		i.setTitle(item);
	}
}
