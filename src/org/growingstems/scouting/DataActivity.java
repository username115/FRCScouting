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

import java.util.ArrayList;
import java.util.List;

import org.frc836.database.DB;
import org.growingstems.scouting.MainMenuSelection;
import org.growingstems.scouting.R;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class DataActivity extends TabActivity {

	private TabHost mTabHost;

	public static DataActivity mTabs;

	public static DB db;

	public static List<String> teamList;
	//public static ParamList teamParams;

	private String HELPMESSAGE = "";

	public static final String TEAM_TAB = "team_tab";
	public static final String EVENT_TAB = "event_tab";

	public static EventStatsActivity eventTab;
	public static TeamStatsActivity teamTab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data);

		HELPMESSAGE = "The "
				+ getString(R.string.eventstats)
				+ " tab will show you data about the teams at the current competition.\n\n"
				+ "The "
				+ getString(R.string.teamstats)
				+ " tab allows you to look up data about a specific team.\n\n"
				+ "If you select a match number on the Team tab, details of that match will be shown.";

		mTabs = this;

		db = new DB(getApplicationContext(),
				Prefs.getSavedPassword(getApplicationContext()), null); // temporary
																		// change
																		// to
																		// fix
																		// errors
																		// until
																		// this
																		// class
																		// is
																		// re-written

		mTabHost = getTabHost();

		mTabHost.addTab(mTabHost.newTabSpec(EVENT_TAB)
				.setIndicator(getString(R.string.eventstats))
				.setContent(new Intent(this, EventStatsActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec(TEAM_TAB)
				.setIndicator(getString(R.string.teamstats))
				.setContent(new Intent(this, TeamStatsActivity.class)));
		mTabHost.setCurrentTab(0);

		//teamParams = new ParamList(getApplicationContext(), "team_list");
		teamList = new ArrayList<String>();

		getTeamList();

	}

	protected void onResume() {
		super.onResume();
		db.setPass(Prefs.getSavedPassword(getApplicationContext()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		MainMenuSelection.setRefreshItem(menu, R.string.refresh_data);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MainMenuSelection.onOptionsItemSelected(item, this) ? true
				: super.onOptionsItemSelected(item);
	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case MainMenuSelection.HELPDIALOG:
			builder.setMessage(HELPMESSAGE)
					.setCancelable(true)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();

								}
							});
			dialog = builder.create();
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	public String getCurrentTab() {
		return mTabHost.getCurrentTabTag();
	}

	public String getTeamNum() {
		if (mTabHost.getCurrentTabTag().compareToIgnoreCase(TEAM_TAB) == 0) {
			return ((TextView) mTabHost.getCurrentTabView().findViewById(
					R.id.data_team_id)).getText().toString().trim();
		}
		return null;
	}

	public void refreshCurrentTab() {
		if (mTabHost.getCurrentTabTag().compareToIgnoreCase(EVENT_TAB) == 0
				&& eventTab != null) {
			eventTab.refreshStats();
		} else if (mTabHost.getCurrentTabTag().compareToIgnoreCase(TEAM_TAB) == 0
				&& teamTab != null) {
			teamTab.refreshStats();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Prefs.PREFS_ACTIVITY_CODE:
			refreshCurrentTab();
			break;
		default:
			break;
		}
	}

	public void setCurrentTab(String tag) {
		mTabHost.setCurrentTabByTag(tag);
	}

	protected void getTeamList() {
		//teamParams.downloadParamListWithPass("team_id",
		//		new TeamListCallback());
	}

	/*protected class TeamListCallback implements ParamCallback {

		public void paramsUpdated(String name, String table, List<String> params) {
			teamList = params;
			try {
				teamTab.setTeamList(teamList);
			} catch (Exception e) {

			}
			Toast.makeText(getApplicationContext(), "Team List Updated",
					Toast.LENGTH_SHORT).show();
		}

	}*/

}
