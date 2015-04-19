package org.growingstems.scouting;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TeamDataFragment extends DataFragment {

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
		if (!displayed)
			return;
		List<String> teams = mParent.getDB().getTeamsWithData();
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
	}

	private void setTeamList(List<String> teams) {
		if (teams.isEmpty())
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
				loadTeam(Integer.valueOf(team));
			}
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
		// TODO load the team data activity
		Toast.makeText(getActivity(), "Open team " + team, Toast.LENGTH_SHORT)
				.show();
	}

}
