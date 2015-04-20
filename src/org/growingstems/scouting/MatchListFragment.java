package org.growingstems.scouting;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MatchListFragment extends DataFragment {

	private int teamNum = -1;
	private String eventName = null;

	public MatchListFragment(int team_num) {
		teamNum = team_num;
		eventName = null;
	}

	public MatchListFragment(String event_name) {
		teamNum = -1;
		eventName = event_name;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		rootView.findViewById(R.id.data_team_input_layout).setVisibility(
				View.GONE);

		return rootView;
	}

	@Override
	protected void refreshData() {
		if (!displayed)
			return;

		boolean prac = Prefs.getPracticeMatch(mParent, false);
		if (prac) {
			Toast.makeText(mParent,
					"Warning, currently viewing practice match data",
					Toast.LENGTH_SHORT).show();
		}
		List<String> matches = null;
		if (eventName != null && teamNum <= 0)
			matches = mParent.getDB().getMatchesWithData(eventName, prac);
		// TODO team match fetching

		if (matches == null)
			matches = new ArrayList<String>(1);
		if (matches.isEmpty()) {
			StringBuilder message = new StringBuilder(
					"No Matches for selected ");
			if (teamNum > 0 && eventName != null) {
				message.append("Team/Event combination");
			} else if (teamNum > 0) {
				message.append("Team");
			} else if (eventName != null) {
				message.append("Event");
			} else {
				message = new StringBuilder("Invalid Event or Team Selected");
			}
			matches.add(message.toString());
		}

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getActivity(), defaultListResource, matches);
		dataList.setAdapter(adapter);
		dataList.setOnItemClickListener(new MatchClick());

	}

	private class MatchClick implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (view instanceof TextView) {
				String match = ((TextView) view).getText().toString();
				loadMatch(Integer.valueOf(match));
			}
		}

	}

	private void loadMatch(int match) {
		Toast.makeText(getActivity(), "Open match " + match, Toast.LENGTH_SHORT)
				.show();
	}

}
