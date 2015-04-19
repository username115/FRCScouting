package org.growingstems.scouting;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class EventListFragment extends DataFragment {

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
		List<String> events = mParent.getDB().getEventsWithData();
		String curEvent = Prefs.getEvent(getActivity(), "");
		if (events == null) {
			events = new ArrayList<String>(1);
		}
		if (events.isEmpty()) {
			events.add("No Data for any Event");
		} else if (curEvent.length() > 0 && events.contains(curEvent)) {
			events.remove(curEvent);
			events.add(0, curEvent);
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getActivity(), defaultListResource, events);
		dataList.setAdapter(adapter);
		dataList.setOnItemClickListener(new EventClick());

	}

	private class EventClick implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String event = (String) parent.getItemAtPosition(position);

			loadEvent(event);
		}

		private void loadEvent(String event) {
			// TODO load the event data activity
			Toast.makeText(getActivity(), "Open event " + event,
					Toast.LENGTH_SHORT).show();
		}

	}

}
