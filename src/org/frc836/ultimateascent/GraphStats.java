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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.achartengine.model.XYSeries;
import org.growingstems.scouting.XMLDBParser;
import org.xmlpull.v1.XmlPullParserException;

import android.graphics.Color;
import android.os.AsyncTask;

public class GraphStats {

	private GraphCallback call;

	private String eventName;

	public Map<String, XYSeries> data;

	public static String[] graphList = { "Score", "Disc Score",
			"Climbing Score", "Autonomous Score", "Accuracy" };
	public static int[] colorList = { Color.GREEN, Color.CYAN, Color.RED,
			Color.YELLOW, Color.MAGENTA, Color.WHITE };

	public GraphStats(String eventName, GraphCallback callback) {
		call = callback;
		this.eventName = eventName;
	}

	public void processFromXML(String XML) throws XmlPullParserException,
			IOException {
		data = new TreeMap<String, XYSeries>();
		List<Map<String, String>> avgInfoEvent = XMLDBParser.extractRows(
				"event_id", eventName, XML);
		data.put("Score", populateScores(avgInfoEvent));
		data.put("Disc Score", populateDiscScores(avgInfoEvent));
		data.put("Climbing Score", populateClimbScores(avgInfoEvent));
		data.put("Autonomous Score", populateAutoScores(avgInfoEvent));
		data.put("Accuracy", populateAccuracyGraph(avgInfoEvent));
		// TODO
	}

	private XYSeries populateScores(List<Map<String, String>> avgInfoEvent) {
		XYSeries series = new XYSeries("Score");
		for (Map<String, String> row : avgInfoEvent) {
			series.add(Double.valueOf(row.get("match_id")),
					Stats.getMatchScore(row));
		}
		return series;
	}

	private XYSeries populateDiscScores(List<Map<String, String>> avgInfoEvent) {
		XYSeries series = new XYSeries("Disc Score");
		for (Map<String, String> row : avgInfoEvent) {
			series.add(Double.valueOf(row.get("match_id")),
					Stats.getMatchDiscScore(row));
		}
		return series;
	}

	private XYSeries populateClimbScores(List<Map<String, String>> avgInfoEvent) {
		XYSeries series = new XYSeries("Climbing Score");
		for (Map<String, String> row : avgInfoEvent) {
			series.add(Double.valueOf(row.get("match_id")),
					Stats.getMatchClimbScore(row));
		}
		return series;
	}

	private XYSeries populateAutoScores(List<Map<String, String>> avgInfoEvent) {
		XYSeries series = new XYSeries("Autonomous Score");
		for (Map<String, String> row : avgInfoEvent) {
			series.add(Double.valueOf(row.get("match_id")),
					Stats.getMatchAutoScore(row));
		}
		return series;
	}

	private XYSeries populateAccuracyGraph(
			List<Map<String, String>> avgInfoEvent) {
		XYSeries series = new XYSeries("Accuracy (%)");
		for (Map<String, String> row : avgInfoEvent) {
			series.add(Double.valueOf(row.get("match_id")),
					Stats.getMatchAccuracy(row));
		}
		return series;
	}

	public void refresh(String XML) {
		AsynchGraphStatPopulate pop = new AsynchGraphStatPopulate();
		pop.execute(XML);
	}

	public interface GraphCallback {
		public void onResponse(GraphStats stats);

		public void onError(Exception e);
	}

	private class AsynchGraphStatPopulate extends
			AsyncTask<String, Integer, GraphStats> {
		private Exception ex = null;

		@Override
		protected GraphStats doInBackground(String... params) {
			try {
				processFromXML(params[0]);
			} catch (Exception e) {
				ex = e;
			}
			return GraphStats.this;
		}

		protected void onPostExecute(GraphStats stats) {
			super.onPostExecute(stats);
			try {
				if (ex == null)
					call.onResponse(stats);
				else
					call.onError(ex);
			} catch (Exception e) {
				call.onError(e);
			}
		}
	}
}
