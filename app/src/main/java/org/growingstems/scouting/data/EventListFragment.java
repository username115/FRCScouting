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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

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
		if (!isDisplayed() || getActivity() == null)
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

	}

	private void loadEvent(String event) {
		Intent intent = new Intent(mParent, DataActivity.class);
		intent.putExtra(DataActivity.ACTIVITY_TYPE_STRING,
				DataActivity.ACTIVITY_TYPE_EVENT);
		intent.putExtra(DataActivity.EVENT_ARG, event);
		mParent.startActivity(intent);
	}

}
