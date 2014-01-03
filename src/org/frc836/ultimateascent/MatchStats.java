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
import java.util.List;
import java.util.Map;

import org.growingstems.scouting.ExpandableListItem;
import org.growingstems.scouting.XMLDBParser;
import org.sigmond.net.HttpCallback;
import org.sigmond.net.HttpRequestInfo;
import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;

public class MatchStats implements HttpCallback {

	public List<ExpandableListItem<String>> contents;

	private MatchCallback call;

	public MatchStats(MatchCallback callback) {
		call = callback;
	}

	public void processFromXML(String XML) throws XmlPullParserException,
			IOException {
		contents = new ArrayList<ExpandableListItem<String>>();

		List<Map<String, String>> entries = XMLDBParser.extractRows(null, null,
				XML);
		if (entries.size() > 0) {
			for (Map<String, String> team : entries) {
				populateTeamInfo(team);
			}
		} else {
			contents.add(new ExpandableListItem<String>(
					"No data for this match"));
		}
	}

	private void populateTeamInfo(Map<String, String> teamData) {
		String key = "Team " + teamData.get("team_id");
		List<List<String>> list = new ArrayList<List<String>>();

		List<String> tempList = new ArrayList<String>(2);

		int score = Stats.getMatchScore(teamData);
		int pyramidScore = Stats.getMatchClimbScore(teamData);

		key += ": " + score;

		tempList.add("Score:");
		tempList.add(String.valueOf(score));
		list.add(tempList);
		tempList = new ArrayList<String>(2);
		tempList.add("Disc Score:");
		tempList.add(String.valueOf(Stats.getMatchDiscScore(teamData)));
		list.add(tempList);
		tempList = new ArrayList<String>(2);
		tempList.add("Auto Score:");
		tempList.add(String.valueOf(Stats.getMatchAutoScore(teamData)));
		list.add(tempList);
		tempList = new ArrayList<String>(2);
		tempList.add("Pyramid Score:");
		tempList.add(String.valueOf(pyramidScore));
		list.add(tempList);
		tempList = new ArrayList<String>(2);
		tempList.add("Accuracy:");
		tempList.add(String.valueOf(Stats.getMatchAccuracy(teamData)) + "%");
		list.add(tempList);
		String py = "no";
		if (pyramidScore > 0
				|| teamData.get("pyramid_attempted").compareToIgnoreCase("1") == 0) {
			py = "yes";
		}
		tempList = new ArrayList<String>(2);
		tempList.add("Pyramid Attempted:");
		tempList.add(py);
		list.add(tempList);

		tempList = new ArrayList<String>(2);
		tempList.add("Penalty:");
		tempList.add(teamData.get("penalty").compareToIgnoreCase("1") == 0 ? "yes"
				: "no");
		list.add(tempList);

		tempList = new ArrayList<String>(2);
		tempList.add("Major Penalty:");
		tempList.add(teamData.get("mpenalty").compareToIgnoreCase("1") == 0 ? "yes"
				: "no");
		list.add(tempList);

		tempList = new ArrayList<String>(2);
		tempList.add("Yellow Card:");
		tempList.add(teamData.get("yellow_card").compareToIgnoreCase("1") == 0 ? "yes"
				: "no");
		list.add(tempList);

		tempList = new ArrayList<String>(2);
		tempList.add("Red Card:");
		tempList.add(teamData.get("red_card").compareToIgnoreCase("1") == 0 ? "yes"
				: "no");
		list.add(tempList);

		tempList = new ArrayList<String>(2);
		tempList.add("Notes:");
		tempList.add(teamData.get("notes"));
		list.add(tempList);

		tempList = new ArrayList<String>(2);
		tempList.add("Submitted:");
		tempList.add(teamData.get("timestamp"));
		list.add(tempList);
		List<Boolean> select = new ArrayList<Boolean>();
		for (int i = 0; i < list.size(); i++) {
			select.add(false);
		}

		contents.add(new ExpandableListItem<String>(key, list, select));
	}

	public interface MatchCallback {
		public void onResponse(MatchStats stats);

		public void onError(Exception e, boolean network);
	}

	public void setCallback(MatchCallback callback) {
		call = callback;
	}

	public void onResponse(HttpRequestInfo resp) {
		try {
			AsynchMatchStatPopulate pop = new AsynchMatchStatPopulate();
			pop.execute(resp.getResponseString());
		} catch (Exception e) {
			call.onError(e, false);
		}
	}

	public void onError(Exception e) {
		call.onError(e, true);
	}

	private class AsynchMatchStatPopulate extends
			AsyncTask<String, Integer, MatchStats> {
		private Exception ex = null;

		@Override
		protected MatchStats doInBackground(String... params) {
			try {
				processFromXML(params[0]);
			} catch (Exception e) {
				ex = e;
			}
			return MatchStats.this;
		}

		protected void onPostExecute(MatchStats stats) {
			super.onPostExecute(stats);
			if (ex == null)
				call.onResponse(stats);
			else
				call.onError(ex, false);
		}

	}

}
