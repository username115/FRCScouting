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

import org.frc836.database.DB;
import org.frc836.database.DBSyncService.LocalBinder;
import org.growingstems.scouting.R;
import org.growingstems.scouting.MenuSelections.Refreshable;

public class MainMenuSelection {

	public static final int HELPDIALOG = 74920442;
	
	private static LocalBinder mBinder;

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
		case R.id.exportItem:
			exportDB(context);
			return true;
		case R.id.exitItem:
			exit(context);
			return true;
		case R.id.syncItem:
			return forceSync(context);
		default:
			return false;
		}
	}

	public static void openSettings(Activity context) {
		Intent intent = new Intent(context.getBaseContext(), Prefs.class);
		context.startActivityForResult(intent, Prefs.PREFS_ACTIVITY_CODE);
	}

	public static void refresh(Activity context) {
		
		/*if (context instanceof DataActivity) {
			DataActivity act = (DataActivity) context;
			act.refreshCurrentTab();
		} else if (context instanceof MatchStatsActivity) {
			MatchStatsActivity act = (MatchStatsActivity) context;
			act.refreshStats();
		} else {*/
		if (context instanceof Refreshable) {
			((Refreshable) context).refresh();
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
	
	public static void exportDB(Activity context) {
		DB.exportToCSV(context);
	}
	
	public static void exit(Activity context) {
		if (context instanceof DashboardActivity)
			context.finish();
		else {
			Intent intent = new Intent(context, DashboardActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("ExitApp", true);
			context.startActivity(intent);
			context.finish();
		}
	}
	
	public static void setBinder(LocalBinder binder) {
		mBinder = binder;
	}
	
	public static boolean forceSync(Activity context) {
		if (mBinder != null) {
			mBinder.forceSync();
			return true;
		}
		return false;
	}
}
