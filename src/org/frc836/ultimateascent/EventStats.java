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

package org.frc836.ultimateascent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.frc836.database.XMLDBParser;
import org.growingstems.scouting.ExpandableListItem;
import org.sigmond.net.HttpCallback;
import org.sigmond.net.HttpRequestInfo;
import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;

public class EventStats implements HttpCallback {

	public List<ExpandableListItem<String>> contents;

	private EventCallback call;
	private List<String> teams;

	public EventStats(EventCallback callback) {
		call = callback;
	}

	public void processFromXML(String XML) throws XmlPullParserException,
			IOException {

		contents = new ArrayList<ExpandableListItem<String>>();
		Map<String, List<Map<String, String>>> data;
		data = new TreeMap<String, List<Map<String, String>>>();
		teams = XMLDBParser.extractColumn("team_id", XML);
		List<Map<String, String>> entries = XMLDBParser.extractRows(null, null, XML);
		
		if (teams.size() > 0) {
			HashSet<String> set = new HashSet<String>(teams);
			teams.clear();
			teams.addAll(set);
			for (String team : teams) {
				data.put(team, new ArrayList<Map<String, String>>());
			}
			
			for (Map<String, String> row : entries) {
				String t = row.get("team_id");
				List<Map<String, String>> teamData = data.get(t);
				teamData.add(row);
			}
			populateTopScoringTeams(data);
			populateTopDiscScoringTeams(data);
			populateTopClimbingTeams(data);
			populateTopAutoScoringTeams(data);
			populateTopAccuracyTeams(data);
		} else {
			contents.add(new ExpandableListItem<String>(
					"No data for this event"));
		}
	}

	private void populateTopScoringTeams(Map<String, List<Map<String, String>>> data)
			throws XmlPullParserException, IOException {
		String key = "Top Scoring Teams";
		List<List<String>> list = new ArrayList<List<String>>();
		List<Boolean> selectable = new ArrayList<Boolean>();

		List<Map<String, String>> entries;// XMLDBParser.extractRows(null, null,
											// XML);

		List<Team_Stat> teamList = new ArrayList<Team_Stat>();

		for (String team : teams) {
			entries = data.get(team);
			teamList.add(new Team_Stat(team, Stats.getAvgScore(entries)));
		}

		Collections.sort(teamList, Collections.reverseOrder());

		List<String> tempList = new ArrayList<String>(2);
		tempList.add("Team #");
		tempList.add("Avg Score");
		list.add(tempList);
		selectable.add(false);

		for (Team_Stat stat : teamList) {
			tempList = new ArrayList<String>(2);
			tempList.add(stat.team);
			tempList.add(String.valueOf(stat.stat));
			list.add(tempList);
			selectable.add(true);
		}
		contents.add(new ExpandableListItem<String>(key, list, selectable));

	}

	private void populateTopDiscScoringTeams(Map<String, List<Map<String, String>>> data)
			throws XmlPullParserException, IOException {
		String key = "Top Disc Scorers";

		List<List<String>> list = new ArrayList<List<String>>();
		List<Boolean> selectable = new ArrayList<Boolean>();

		List<Map<String, String>> entries;

		List<Team_Stat> teamList = new ArrayList<Team_Stat>();

		for (String team : teams) {
			entries = data.get(team);
			teamList.add(new Team_Stat(team, Stats.getAvgDiscScore(entries)));
		}

		Collections.sort(teamList, Collections.reverseOrder());

		List<String> tempList = new ArrayList<String>(2);
		tempList.add("Team #");
		tempList.add("Avg Disc Score");
		list.add(tempList);
		selectable.add(false);

		for (Team_Stat stat : teamList) {
			tempList = new ArrayList<String>(2);
			tempList.add(stat.team);
			tempList.add(String.valueOf(stat.stat));
			list.add(tempList);
			selectable.add(true);
		}
		contents.add(new ExpandableListItem<String>(key, list, selectable));
	}

	private void populateTopClimbingTeams(Map<String, List<Map<String, String>>> data)
			throws XmlPullParserException, IOException {
		String key = "Top Climbing Teams";
		List<List<String>> list = new ArrayList<List<String>>();
		List<Boolean> selectable = new ArrayList<Boolean>();

		List<Map<String, String>> entries;// XMLDBParser.extractRows(null, null,
											// XML);

		List<Team_Stat> teamList = new ArrayList<Team_Stat>();

		for (String team : teams) {
			entries = data.get(team);
			teamList.add(new Team_Stat(team, Stats.getAvgClimbScore(entries)));
		}

		Collections.sort(teamList, Collections.reverseOrder());

		List<String> tempList = new ArrayList<String>(2);
		tempList.add("Team #");
		tempList.add("Avg Climb Score");
		list.add(tempList);
		selectable.add(false);

		for (Team_Stat stat : teamList) {
			tempList = new ArrayList<String>(2);
			tempList.add(stat.team);
			tempList.add(String.valueOf(stat.stat));
			list.add(tempList);
			selectable.add(true);
		}
		contents.add(new ExpandableListItem<String>(key, list, selectable));
	}

	private void populateTopAutoScoringTeams(Map<String, List<Map<String, String>>> data)
			throws XmlPullParserException, IOException {
		String key = "Top Autonomous Scorers";

		List<List<String>> list = new ArrayList<List<String>>();
		List<Boolean> selectable = new ArrayList<Boolean>();

		List<Map<String, String>> entries;

		List<Team_Stat> teamList = new ArrayList<Team_Stat>();

		for (String team : teams) {
			entries = data.get(team);
			teamList.add(new Team_Stat(team, Stats.getAvgAutoScore(entries)));
		}

		Collections.sort(teamList, Collections.reverseOrder());

		List<String> tempList = new ArrayList<String>(2);
		tempList.add("Team #");
		tempList.add("Avg Autonomous Score");
		list.add(tempList);
		selectable.add(false);

		for (Team_Stat stat : teamList) {
			tempList = new ArrayList<String>(2);
			tempList.add(stat.team);
			tempList.add(String.valueOf(stat.stat));
			list.add(tempList);
			selectable.add(true);
		}
		contents.add(new ExpandableListItem<String>(key, list, selectable));
	}

	private void populateTopAccuracyTeams(Map<String, List<Map<String, String>>> data)
			throws XmlPullParserException, IOException {
		String key = "Top Accuracy";

		List<List<String>> list = new ArrayList<List<String>>();
		List<Boolean> selectable = new ArrayList<Boolean>();

		List<Map<String, String>> entries;

		List<Team_Stat> teamList = new ArrayList<Team_Stat>();

		for (String team : teams) {
			entries = data.get(team);
			teamList.add(new Team_Stat(team, Stats.getAvgAccuracy(entries)));
		}

		Collections.sort(teamList, Collections.reverseOrder());

		List<String> tempList = new ArrayList<String>(2);
		tempList.add("Team #");
		tempList.add("Accuracy");
		list.add(tempList);
		selectable.add(false);

		for (Team_Stat stat : teamList) {
			tempList = new ArrayList<String>(2);
			tempList.add(stat.team);
			tempList.add(String.valueOf(stat.stat) + "%");
			list.add(tempList);
			selectable.add(true);
		}
		contents.add(new ExpandableListItem<String>(key, list, selectable));
	}

	private class Team_Stat implements Comparable<Team_Stat> {
		String team;
		float stat;

		public Team_Stat(String team, float stat) {
			this.team = team;
			this.stat = stat;
		}

		public int compareTo(Team_Stat another) {
			if (stat == another.stat)
				return 0;
			Float c = stat - another.stat;
			if (c < 1.0 && c > 0.0)
				return 1;
			if (c > -1.0 && c < 0.0)
				return -1;
			return c.intValue();
		}
	}

	public interface EventCallback {
		public void onResponse(EventStats stats);

		public void onError(Exception e, boolean network);
	}

	public void setCallback(EventCallback callback) {
		call = callback;
	}

	public void onResponse(HttpRequestInfo resp) {
		try {
			AsynchEventStatPopulate pop = new AsynchEventStatPopulate();
			pop.execute(resp.getResponseString());
		} catch (Exception e) {
			call.onError(e, false);
		}
	}

	public void onError(Exception e) {
		call.onError(e, true);
	}

	private class AsynchEventStatPopulate extends
			AsyncTask<String, Integer, EventStats> {
		private Exception ex = null;

		@Override
		protected EventStats doInBackground(String... params) {
			try {
				processFromXML(params[0]);
			} catch (Exception e) {
				ex = e;
			}
			return EventStats.this;
		}

		protected void onPostExecute(EventStats stats) {
			super.onPostExecute(stats);
			if (ex == null)
				call.onResponse(stats);
			else
				call.onError(ex, false);
		}

	}

}
