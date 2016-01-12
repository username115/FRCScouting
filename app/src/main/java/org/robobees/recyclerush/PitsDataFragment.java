/*
 * Copyright 2015 Daniel Logan
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

package org.robobees.recyclerush;

import java.util.ArrayList;
import java.util.List;

import org.frc836.database.PitStats;
import org.growingstems.scouting.data.DataFragment;

import android.widget.ArrayAdapter;

public class PitsDataFragment extends DataFragment {

	protected int teamNum = -1;

	public PitsDataFragment(int team_num) {
		teamNum = team_num;
	}

	@Override
	protected void refreshData() {
		if (!displayed)
			return;

		List<String> data = null;

		PitStats stat = mParent.getDB().getTeamPitStats(teamNum);

		if (stat instanceof PitStatsRR) {
			
			// TODO populate pit data from database
		} else {
			data = new ArrayList<String>(1);
			data.add("Data gathered from wrong year.");
		}

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getActivity(), defaultListResource, data);
		dataList.setAdapter(adapter);
	}

}
