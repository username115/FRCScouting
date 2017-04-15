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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.frc836.database.PitStats;

import android.widget.ArrayAdapter;

public class PitsDataFragment extends DataFragment {

    protected int teamNum = -1;

    public static PitsDataFragment getInstance(int team_num) {
        PitsDataFragment fragment = new PitsDataFragment();
        fragment.setTeamNum(team_num);
        return fragment;
    }

    public PitsDataFragment() {
        teamNum = -1;
    }

    public void setTeamNum(int team_num) {
        teamNum = team_num;
    }

    @Override
    protected void refreshData() {
        if (!isDisplayed())
            return;

        List<String> data = null;

        PitStats stat = mParent.getDB().getTeamPitStats(teamNum);

        //if (stat instanceof PitStatsSH) {

        if (stat.team_id > 0) {
            Map<String, String> myData = stat.getDisplayData();

            data = new ArrayList<String>(myData.size());

            for (Map.Entry<String, String> line : myData.entrySet()) {
                data.add(line.getKey() + ": " + line.getValue());
            }
        } else {
            data = new ArrayList<String>(1);
            data.add("No Pit data available for team");
        }
        //} else {
        //    data = new ArrayList<String>(1);
        //    data.add("Data gathered from wrong year.");
        //}

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), defaultListResource, data);
        dataList.setAdapter(adapter);
    }

}