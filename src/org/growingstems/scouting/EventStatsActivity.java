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
import org.frc836.ultimateascent.EventStats;
import org.growingstems.scouting.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class EventStatsActivity extends Activity implements
		EventStats.EventCallback {

	private static String eventName;
	private static DB db;

	private ExpandableListView eventStatView;

	private static EventStats stats;

	private List<ExpandableListItem<String>> items;
	private TextListAdapter adapter;

	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventstats);
		db = DataActivity.db;

		eventStatView = (ExpandableListView) findViewById(R.id.event_data_list);
		DataActivity.eventTab = this;

		String ev = Prefs.getEvent(getApplicationContext(),
				"Chesapeake Regional");
		if (eventName != null && stats != null && eventName.compareTo(ev) == 0) {
			items = stats.contents;
			adapter = new TextListAdapter(this, items);
			eventStatView.setAdapter(adapter);
		}

		eventStatView.setOnChildClickListener(new TeamClickListener());
	}

	@Override
	protected void onResume() {
		super.onResume();

		String ev = Prefs.getEvent(getApplicationContext(),
				"Chesapeake Regional");
		if (eventName == null || stats == null || eventName.compareTo(ev) != 0) {
			eventName = ev;
			refreshStats();
		}

		DataActivity.mTabs.setTitle("Stats for " + eventName);

	}

	public void refreshStats() {
		eventName = Prefs.getEvent(getApplicationContext(),
				"Chesapeake Regional");
		db.getEventStats(eventName, this);
		pd = ProgressDialog.show(this, "Busy", "Retrieving Event Stats", false);
		pd.setCancelable(true);

	}

	public void onResponse(EventStats stats) {
		pd.dismiss();
		EventStatsActivity.stats = stats;
		items = stats.contents;
		adapter = new TextListAdapter(this, items);
		eventStatView.setAdapter(adapter);
		Toast.makeText(getBaseContext(), "Event Stats Updated",
				Toast.LENGTH_SHORT).show();
		DataActivity.mTabs.setTitle("Stats for " + eventName);
	}

	private class TeamClickListener implements
			ExpandableListView.OnChildClickListener {

		@SuppressWarnings("rawtypes")
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			ExpandableListAdapter adapt = parent.getExpandableListAdapter();
			Object l = adapt.getChild(groupPosition, childPosition);
			if (l instanceof List && ((List) l).size() > 0) {
				Object t = ((List) l).get(0);
				int team = Integer.valueOf(t.toString());
				DataActivity.mTabs.setCurrentTab(DataActivity.TEAM_TAB);
				DataActivity.teamTab.setTeam(team);
			}
			return true;
		}
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
