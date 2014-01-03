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

import java.util.HashMap;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.frc836.ultimateascent.GraphStats;
import org.growingstems.scouting.R;

public class GraphActivity extends Activity implements GraphStats.GraphCallback {

	private String teamId;
	private String event;
	private String XML;

	private GraphicalView mChart;

	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

	private ProgressDialog pd;

	private Map<String, Boolean> active = new HashMap<String, Boolean>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graph_view);

		Intent intent = getIntent();

		teamId = intent.getStringExtra("team_id");
		XML = intent.getStringExtra("XML");
		event = Prefs.getEvent(getApplicationContext(), "Chesapeake Regional");
		String init = intent.getStringExtra("graph");
		
		if (init.length() > 0 && init.compareTo("All") != 0) {
			active.put(init, true);
		} else if (init.compareTo("All") == 0) {
			for (String key : GraphStats.graphList) {
				active.put(key, true);
			}
		}

		setTitle(event + ": Team " + teamId);
	}

	protected void onResume() {
		super.onResume();
		if (mChart == null) {
			refreshGraphs();
		} else {
			mChart.repaint();
		}
	}

	public void refreshGraphs() {
		pd = ProgressDialog.show(this, "Busy", "Processing Stats", false);
		pd.setCancelable(true);
		GraphStats s = new GraphStats(event, this);
		s.refresh(XML);
	}

	private void fillDataSets(GraphStats stats) {
		int i = 0;
		for (String key : GraphStats.graphList) {
			if (active.containsKey(key) && active.get(key)) {
				mDataset.addSeries(stats.data.get(key));
				XYSeriesRenderer render = new XYSeriesRenderer();
				render.setPointStyle(PointStyle.DIAMOND);
				render.setColor(GraphStats.colorList[i]);
				mRenderer.addSeriesRenderer(render);
				i = i >= GraphStats.colorList.length ? 0 : i + 1;
			}
		}
	}

	public void onResponse(GraphStats stats) {
		pd.dismiss();
		fillDataSets(stats);

		LinearLayout layout = (LinearLayout) findViewById(R.id.graph);
		mRenderer.setYAxisMin(0);
		mRenderer.setXAxisMin(0);
		mRenderer.setXTitle("Match Number");
		mChart = ChartFactory.getCubeLineChartView(this, mDataset, mRenderer,
				0.0f);
		layout.addView(mChart);
	}

	public void onError(Exception e) {
		Toast.makeText(getApplicationContext(), "Error processing graph",
				Toast.LENGTH_SHORT).show();
	}

	// TODO add options for selecting which lines to display

}
