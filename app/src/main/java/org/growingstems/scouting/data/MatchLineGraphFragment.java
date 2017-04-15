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
import org.growingstems.scouting.R;

import java.util.Set;


public class MatchLineGraphFragment extends DataFragment implements DataSource.DataCallback {

    protected int teamNum = -1;
    protected String eventName = null;

    private GraphicalView mChart;

    private DataSource dataSource;

    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    private SparseArray<XYSeriesRenderer> mRenderers = new SparseArray<XYSeriesRenderer>(NUMGRAPHS);

    private SparseArray<XYValueSeries> mData = new SparseArray<XYValueSeries>(NUMGRAPHS);

    private static final int NUMGRAPHS = 7;

    private static final int[] TOTALSCORES = {0, 2, 3, 4, 5, 6};

    private static final int AVERAGESCORE = 1;

    private static final int[] COLORS = {Color.GREEN, Color.CYAN, Color.RED,
            Color.YELLOW, Color.MAGENTA, Color.WHITE, Color.BLUE};

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
        if (!isDisplayed())
            return;
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
            dataSource.getMaxScores(teamNum, eventName, this);
        }
        //TODO more graphs?
    }

    @Override
    public void onFinished(DataSource.Data data) {

        switch (data.getDataType()) {
            case Scores:
                int minMatch = 10000000, maxMatch = 0;
                if (eventName != null) {
                    SparseIntArray scores = data.getTotalScores();
                    if (scores == null) {
                        break;
                    }
                    XYValueSeries series = mData.get(TOTALSCORES[0]);
                    if (series == null) {
                        series = new XYValueSeries("Match Score");
                        mData.put(TOTALSCORES[0], series);
                        mDataset.addSeries(series);
                    }
                    series.clear();
                    for (int i = 0; i < scores.size(); i++) {
                        int match = scores.keyAt(i);
                        series.add(match, scores.get(match));
                        minMatch = Math.min(minMatch, match);
                        maxMatch = Math.max(maxMatch, match);
                    }
                    XYSeriesRenderer render = mRenderers.get(TOTALSCORES[0]);
                    if (render == null) {
                        render = new XYSeriesRenderer();
                        mRenderers.put(TOTALSCORES[0], render);
                        mRenderer.addSeriesRenderer(render);
                        render.setPointStyle(PointStyle.DIAMOND);
                        render.setColor(COLORS[(TOTALSCORES[0]) % COLORS.length]);
                    }

                } else {
                    Set<String> events = data.getEventsWithScores();
                    int j = 0;
                    for (String eventName : events) {
                        if (eventName != null) {
                            SparseIntArray scores = data.getTotalScores(eventName);
                            if (scores == null) {
                                break;
                            }
                            XYValueSeries series = mData.get(TOTALSCORES[j]);
                            if (series == null) {
                                series = new XYValueSeries("Match Score: " + eventName);
                                mData.put(TOTALSCORES[j], series);
                                mDataset.addSeries(series);
                            }

                            series.clear();
                            for (int i = 0; i < scores.size(); i++) {
                                int match = scores.keyAt(i);
                                series.add(match, scores.get(match));
                                minMatch = Math.min(minMatch, match);
                                maxMatch = Math.max(maxMatch, match);
                            }
                            XYSeriesRenderer render = mRenderers.get(TOTALSCORES[j]);
                            if (render == null) {
                                render = new XYSeriesRenderer();
                                mRenderers.put(TOTALSCORES[j], render);
                                mRenderer.addSeriesRenderer(render);
                                render.setPointStyle(PointStyle.DIAMOND);
                                render.setColor(COLORS[(TOTALSCORES[j]) % COLORS.length]);
                            }
                            j++;
                        }
                    }
                }

                XYValueSeries series = mData.get(AVERAGESCORE);
                if (series == null) {
                    series = new XYValueSeries("Average Score");
                    mData.put(AVERAGESCORE, series);
                    mDataset.addSeries(series);
                }
                double average = data.getAverageScore();
                series.clear();
                series.add(0, average);
                series.add(maxMatch, average);
                XYSeriesRenderer render = mRenderers.get(AVERAGESCORE);
                if (render == null) {
                    render = new XYSeriesRenderer();
                    mRenderers.put(AVERAGESCORE, render);
                    mRenderer.addSeriesRenderer(render);
                    render.setFillPoints(false);
                    render.setColor(COLORS[AVERAGESCORE % COLORS.length]);
                }
                break;
            // TODO more graphs?
        }

        if (mChart != null)
            mChart.repaint();
    }
}
