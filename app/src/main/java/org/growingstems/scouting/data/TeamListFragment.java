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

package org.growingstems.scouting.data;

import java.util.ArrayList;
import java.util.List;

import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;

public class TeamListFragment extends DataFragment {

	private String eventName = null;

	public static TeamListFragment getInstance(String event_name) {
		TeamListFragment fragment = new TeamListFragment();
		fragment.setEventName(event_name);
		return fragment;
	}

	public TeamListFragment() {
		eventName = null;
	}

	public void setEventName(String event) {
		eventName = event;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		rootView.findViewById(R.id.data_team_input_layout).setVisibility(
				View.VISIBLE);

		loadB.setOnClickListener(new LoadClick());
		autoText.setOnItemClickListener(new TeamClick());
		autoText.setThreshold(1);

		return rootView;
	}

	@Override
	protected void refreshData() {
		if (!isDisplayed() || getActivity() == null)
			return;
		List<String> teams = mParent.getDB().getTeamsWithData(eventName);
		String ourTeam = Prefs.getDefaultTeamNumber(getActivity(), "").trim();
		if (teams == null) {
			teams = new ArrayList<String>(1);
		}
		if (teams.isEmpty()) {
			teams.add("No Data for any Team");
		} else {
			if (ourTeam.length() > 0 && TextUtils.isDigitsOnly(ourTeam)
					&& teams.contains(ourTeam)) {
				teams.remove(ourTeam);
				teams.add(0, ourTeam);
			}
			setTeamList(teams);
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getActivity(), defaultListResource, teams);
		dataList.setAdapter(adapter);
		dataList.setOnItemClickListener(new TeamClick());
		dataList.setOnItemLongClickListener(new TeamLongClick());
	}

	private void setTeamList(List<String> teams) {
		if (teams.isEmpty() || getActivity() == null)
			return;
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_dropdown_item_1line, teams);

		autoText.setAdapter(adapter);
	}

	private class TeamClick implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (view instanceof TextView) {
				String team = ((TextView) view).getText().toString();
				try {
					loadTeam(Integer.valueOf(team));
				} catch (NumberFormatException e) {

				}
			}
		}

	}

	private class TeamLongClick implements AdapterView.OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			if (view instanceof TextView) {
				String team = ((TextView) view).getText().toString();
                try {
                    Integer.valueOf(team);
                } catch (NumberFormatException e) {
                    return false;
                }
                PopupMenu popup = new PopupMenu(getActivity(), view);
                List<String> t = mParent.getDB().getPickList(Prefs.getEvent(mParent, "CHS District Central Virginia Event"));
                if (t != null && t.contains(team))
                    popup.getMenu().add(REMOVETEAMPICK);
                else
                    popup.getMenu().add(PICKLISTITEM);
                popup.setOnMenuItemClickListener(new TeamMenuItemListener(team));
                popup.show();
			}
			return true;
		}
	}

    private static final String PICKLISTITEM = "Add Team to Pick List";
    private static final String REMOVETEAMPICK = "Remove Team from Pick List";

    private class TeamMenuItemListener implements PopupMenu.OnMenuItemClickListener {

        String teamNum;

        public TeamMenuItemListener(String team) {
            teamNum = team;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            try {
                if (item.getTitle().toString().compareTo(PICKLISTITEM) == 0)
                    mParent.getDB().addTeamToPickList(Integer.valueOf(teamNum), Prefs.getEvent(mParent, "CHS District Central Virginia Event"));
                else if (item.getTitle().toString().compareTo(REMOVETEAMPICK) == 0)
                    mParent.getDB().removeTeamFromPickList(Integer.valueOf(teamNum), Prefs.getEvent(mParent, "CHS District Central Virginia Event"));
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        }
    }

	private class LoadClick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (autoText.getText().toString().length() > 0)
				loadTeam(Integer.valueOf(autoText.getText().toString()));
		}

	}

	private void loadTeam(int team) {
		Intent intent = new Intent(mParent, DataActivity.class);
		intent.putExtra(DataActivity.ACTIVITY_TYPE_STRING,
				DataActivity.ACTIVITY_TYPE_TEAM);
		intent.putExtra(DataActivity.EVENT_ARG, eventName);
		intent.putExtra(DataActivity.TEAM_ARG, team);
		mParent.startActivity(intent);
	}

}
