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
import org.frc836.ultimateascent.TeamStats;
import org.growingstems.scouting.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class TeamStatsActivity extends Activity implements
		TeamStats.TeamCallback {

	public static int teamId;

	private static DB db;

	private ExpandableListView teamStatView;
	private Button searchB;
	private AutoCompleteTextView teamT;

	private TextListAdapter adapter;

	private ProgressDialog pd;

	private TeamStats stats;

	private Handler startupTimer = new Handler();
	private static final int STARTTEAMSETDELAY = 50;
	private SetDefaultTeamTask mTeamSetTask = new SetDefaultTeamTask();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teamstats);
		db = DataActivity.db;

		DataActivity.teamTab = this;

		teamStatView = (ExpandableListView) findViewById(R.id.team_data_list);
		searchB = (Button) findViewById(R.id.data_teamB);
		teamT = (AutoCompleteTextView) findViewById(R.id.data_team_id);

		searchB.setOnClickListener(new OnTeamBListener());
		teamStatView.setOnChildClickListener(new MatchClickListener());
		teamT.setOnItemClickListener(new TeamSelectedListener());
		teamT.setThreshold(1);

		startupTimer.removeCallbacks(mTeamSetTask);
		startupTimer.postDelayed(mTeamSetTask, STARTTEAMSETDELAY);

		teamId = 0;

	}

	@Override
	protected void onResume() {
		super.onResume();
		// int i = teamS.getAdapter().getCount();
		// int j = DataActivity.teamList.size();

		if (teamT.getAdapter() == null
				|| teamT.getAdapter().getCount() != DataActivity.teamList
						.size())
			setTeamList(DataActivity.teamList);
	}

	private class OnTeamBListener implements View.OnClickListener {

		public void onClick(View v) {
			// DataActivity.mTabs.getTeamList();
			try {
				String team = teamT.getText().toString();
				teamId = Integer.valueOf(team);
				refreshStats();
			} catch (NumberFormatException e) {

			}
		}

	}

	public void onDestroy() {
		startupTimer.removeCallbacks(mTeamSetTask);
		super.onDestroy();
	}

	public void refreshStats() {
		try {
			if (teamId != 0) {
				pd = ProgressDialog.show(this, "Busy", "Retrieving Team Stats",
						false);
				pd.setCancelable(true);
				db.getTeamStats(teamId, this);
				DataActivity.mTabs.setTitle("Team " + teamId + " Stats");
				teamT.setText(String.valueOf(teamId));
			} else {
				DataActivity.mTabs.setTitle("Team Stats");
				teamT.setText("");
			}
		} catch (Exception e) {

		}
	}

	public void onResponse(TeamStats stats) {
		pd.dismiss();
		this.stats = stats;
		adapter = new TextListAdapter(this, stats.teamInfo);
		teamStatView.setAdapter(adapter);
		DataActivity.mTabs.setTitle("Team " + teamId + " Stats");
		Toast.makeText(getBaseContext(), "Team info retrieved.",
				Toast.LENGTH_SHORT).show();
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

	public void setTeam(int team) {
		teamId = team;
		refreshStats();
	}

	private class MatchClickListener implements
			ExpandableListView.OnChildClickListener {

		@SuppressWarnings("rawtypes")
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			ExpandableListAdapter adapt = parent.getExpandableListAdapter();
			Object l = adapt.getChild(groupPosition, childPosition);

			Object g = adapt.getGroup(groupPosition);
			if (g instanceof ExpandableListItem
					&& ((ExpandableListItem) g).getName().contains("Match")) {
				if (l instanceof List
						&& ((List) l).size() > 1
						&& ((List) l).get(1).toString()
								.compareToIgnoreCase("Score") != 0) {
					Object t = ((List) l).get(0);
					String match = t.toString();
					String event = null;
					int pos = childPosition - 1;
					while (event == null && pos >= 0) {
						l = adapt.getChild(groupPosition, pos);
						if (l instanceof List
								&& ((List) l).size() == 2
								&& ((List) l).get(1).toString()
										.compareToIgnoreCase("Score") == 0) {
							event = ((List) l).get(0).toString();
							event = event.substring(0, event.length() - 1);
						}
						pos--;
					}
					if (event != null) {
						Intent intent = new Intent(TeamStatsActivity.this,
								MatchStatsActivity.class);
						intent.putExtra("match", match);
						intent.putExtra("event", event);
						startActivity(intent);
					}
				}
			} else if (g instanceof ExpandableListItem
					&& ((ExpandableListItem) g).getName().contains("Graph")) {
				Intent intent = new Intent(TeamStatsActivity.this,
						GraphActivity.class);
				intent.putExtra("team_id", String.valueOf(teamId));
				if (l instanceof List && !((List) l).isEmpty()) {
					intent.putExtra("graph", ((List) l).get(0).toString());
				} else {
					intent.putExtra("graph", "");
				}
				if (stats != null) {
					intent.putExtra("XML", stats.matches);
				} else {
					intent.putExtra("XML", "");
				}
				startActivity(intent);
			}
			return true;
		}

	}

	public void setTeamList(List<String> teams) {
		if (teams.isEmpty())
			return;
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, teams);

		// adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

		teamT.setAdapter(adapter);
	}

	private class TeamSelectedListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			try {
				if (v instanceof TextView) {
					String team = ((TextView) v).getText().toString();
					teamId = Integer.valueOf(team);
					refreshStats();
				}
			} catch (NumberFormatException e) {

			}
		}

	}

	private class SetDefaultTeamTask implements Runnable {

		public void run() {
			if (teamId != 0)
				return;
			String team = Prefs.getDefaultTeamNumber(getApplicationContext(),
					"");
			teamT.setText(team);
			if (team != "") {
				try {
					teamId = Integer.valueOf(team);
					refreshStats();
				} catch (NumberFormatException e) {

				}
			} else
				teamId = 0;
		}

	}

}
