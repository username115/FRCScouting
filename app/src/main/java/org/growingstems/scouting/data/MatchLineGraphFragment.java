/*
 * Copyright 2016 Daniel Logan
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

package org.growingstems.scouting.data;


import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYValueSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.frc836.yearly.MatchStatsYearly;
import org.growingstems.scouting.R;

import java.util.Map;


public class MatchLineGraphFragment extends DataFragment implements DataSource.DataCallback {

    protected int teamNum = -1;
    protected String eventName = null;

    private GraphicalView mChart;

    private DataSource dataSource;

    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    private SparseArray<XYSeriesRenderer> mRenderers = new SparseArray<XYSeriesRenderer>(NUMGRAPHS);

    private SparseArray<XYValueSeries> mData = new SparseArray<XYValueSeries>(NUMGRAPHS);

    private static final int NUMGRAPHS = 15;

    private static final int[] COLORS = {Color.GREEN, Color.CYAN, Color.RED,
            Color.YELLOW, Color.MAGENTA, Color.WHITE, Color.BLUE, Color.DKGRAY, Color.GRAY, Color.LTGRAY}; //ALL THE COLORS

    public static MatchLineGraphFragment getInstance(int team_num, String event_name) {
        MatchLineGraphFragment fragment = new MatchLineGraphFragment();
        fragment.setEventName(event_name);
        fragment.setTeamNum(team_num);
        fragment.default_layout_resource = R.layout.fragment_graph;
        return fragment;
    }


    public void setEventName(String event_name) {
        eventName = event_name;
    }

    public void setTeamNum(int team_num) {
        teamNum = team_num;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (dataSource == null)
            dataSource = new DataSource(mParent.getDB());


        return rootView;
    }

    @Override
    protected void refreshData() {
        if (!isDisplayed()) return;
        if (dataSource == null)
            dataSource = new DataSource(mParent.getDB());

        if (mChart == null) {
            LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.graph);
            mRenderer.setYAxisMin(0);
            mRenderer.setXAxisMin(0);
            mRenderer.setXTitle("Match Number");
            DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
            float val = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, metrics);
            mRenderer.setLabelsTextSize(val);
            mRenderer.setAxisTitleTextSize(val);
            mRenderer.setLegendTextSize(val);
            mRenderer.setPointSize(val / 4.0f);
            mChart = ChartFactory.getCubeLineChartView(getActivity(), mDataset, mRenderer,
                    0.0f);
            mChart.setBackgroundColor(Color.BLACK);
            layout.addView(mChart, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, 0.0F));
        } else {
            mChart.repaint();
        }
        if (teamNum > 0) {
            dataSource.getGraphData(teamNum, eventName, this);
        }
        //TODO more graphs?
    }

    private void renderLine(int index, SparseIntArray line, String name) {
        XYValueSeries series = mData.get(index);
        if (series == null) {
            series = new XYValueSeries(name);
            mData.put(index, series);
            mDataset.addSeries(series);
        }
        series.clear();
        for (int i = 0; i < line.size(); i++) {
            int match = line.keyAt(i);
            series.add(match, line.get(match));
        }
        XYSeriesRenderer renderer = mRenderers.get(index);
        if (renderer == null) {
            renderer = new XYSeriesRenderer();
            mRenderers.put(index, renderer);
            mRenderer.addSeriesRenderer(renderer);
            renderer.setPointStyle(PointStyle.DIAMOND);
            renderer.setColor(COLORS[(index) % COLORS.length]);
        }
    }

    @Override
    public void onFinished(DataSource.Data data) {

        switch (data.getDataType()) {
            case Graphs:
                int minMatch = 10000000, maxMatch = 0;
                int j = 0;

                if (eventName != null) { //show all graphs
                    if (data.getEventGraphs(eventName) == null)
                        break;

                    for (Map.Entry<String, SparseIntArray> dataLine : data.getEventGraphs(eventName).entrySet()) {
                        SparseIntArray line = dataLine.getValue();
                        if (line == null)
                            continue;
                        renderLine(j, line, dataLine.getKey());
                        for (int i = 0; i < line.size(); i++) {
                            int match = line.keyAt(i);
                            minMatch = Math.min(minMatch, match);
                            maxMatch = Math.max(maxMatch, match);
                        }

                        j++;
                    }
                } else {
                    for (Map.Entry<String, Map<String, SparseIntArray>> event : data.getGraphs().entrySet()) {
                        String eventN = event.getKey();
                        for (Map.Entry<String, SparseIntArray> dataLine : data.getEventGraphs(eventN).entrySet()) {
                            SparseIntArray line = event.getValue().get(dataLine.getKey());
                            if (line == null)
                                continue;
                            renderLine(j, line, dataLine.getKey() + ": " + eventN);
                            for (int i = 0; i < line.size(); i++) {
                                int match = line.keyAt(i);
                                minMatch = Math.min(minMatch, match);
                                maxMatch = Math.max(maxMatch, match);
                            }

                            j++;
                        }
                    }
                }

                break;
            default:
                break;
            // TODO more graphs?
        }

        if (mChart != null)
            mChart.repaint();
    }
}
