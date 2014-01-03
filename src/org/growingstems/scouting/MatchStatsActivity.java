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
import org.frc836.ultimateascent.MatchStats;
import org.growingstems.scouting.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class MatchStatsActivity extends Activity implements
		MatchStats.MatchCallback {

	private String eventName;
	private String matchNum;
	private DB db;

	private ExpandableListView matchStatsView;

	private List<ExpandableListItem<String>> items;
	private TextListAdapter adapter;

	private ProgressDialog pd;
	
	private String HELPMESSAGE = "Displays recorded data about the selected match.";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.matchstats);
		db = new DB(getApplicationContext(),
				Prefs.getSavedPassword(getApplicationContext()));
		matchStatsView = (ExpandableListView) findViewById(R.id.match_data_list);

		Intent intent = getIntent();

		eventName = intent.getStringExtra("event");
		matchNum = intent.getStringExtra("match");

		setTitle(eventName + ", Match " + matchNum);
		refreshStats();
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

	public void refreshStats() {
		db.getMatchStats(eventName, matchNum, this);
		pd = ProgressDialog.show(this, "Busy", "Retrieving Match Stats", false);
		pd.setCancelable(true);
	}

	public void onResponse(MatchStats stats) {
		pd.dismiss();
		items = stats.contents;
		adapter = new TextListAdapter(this, items);
		matchStatsView.setAdapter(adapter);
	}

	public void onError(Exception e, boolean network) {
		pd.dismiss();
		if (network)
			Toast.makeText(getBaseContext(), "Network Error",
					Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(getBaseContext(),
					"Error processing server response", Toast.LENGTH_SHORT)
					.show();
	}

}
