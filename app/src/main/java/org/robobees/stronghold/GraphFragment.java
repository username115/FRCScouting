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

package org.robobees.stronghold;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.growingstems.scouting.R;
import org.growingstems.scouting.data.DataFragment;


public class GraphFragment extends DataFragment implements GraphDataSource.GraphDataCallback {

    protected int teamNum = -1;
    protected String eventName = null;

    private GraphicalView mChart;

    private GraphDataSource dataSource;

    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    public static GraphFragment getInstance(int team_num, String event_name) {
        GraphFragment fragment = new GraphFragment();
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

        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.graph);
        mRenderer.setYAxisMin(0);
        mRenderer.setXAxisMin(0);
        mRenderer.setXTitle("Match Number");
        mChart = ChartFactory.getCubeLineChartView(getActivity(), mDataset, mRenderer,
                0.0f);
        layout.addView(mChart);
        dataSource = new GraphDataSource(mParent.getDB());
        return rootView;
    }

    @Override
    protected void refreshData() {
        dataSource.getMaxScores(teamNum, eventName, this);
        //TODO more graphs
    }

    @Override
    public void onFinished(GraphDataSource.GraphData data) {
        //TODO
    }
}
