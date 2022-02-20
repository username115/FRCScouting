/*
 * Copyright 2018 Daniel Logan
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
package org.frc836.yearly;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import org.frc836.database.MatchStatsStruct;
import org.growingstems.scouting.MatchFragment;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;


public class ClimbFragment extends MatchFragment {


	private boolean displayed = false;

	private MatchStatsStruct tempData = new MatchStatsStruct();


	private View mainView;

	public ClimbFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment PreMatch.
	 */
	public static ClimbFragment newInstance() {
		return new ClimbFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_climb, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		mainView = view;
		getGUIRefs(view);
		setListeners();
		displayed = true;
	}

	@Override
	public void onResume() {
		super.onResume();
		//need to remap view every time it is loaded, in case position or side changed
		getGUIRefs(mainView);
		setListeners();
		loadData(tempData);
	}

	public void onPause() {
		super.onPause();
		saveData(tempData);
	}

	@Override
	public void saveData(MatchStatsStruct data) {
		if (getView() == null || data == null || !displayed)
			return;
		//TODO save data for climb
	}

	@Override
	public void loadData(MatchStatsStruct data) {
		tempData = data;
		if (getView() == null || data == null || !displayed)
			return;
		// which side are we using
		boolean redLeft = Prefs.getRedLeft(getActivity(), true);

		Activity act = getActivity();
		String pos;
		if (act instanceof MatchActivity)
			pos = ((MatchActivity) act).getPosition();
		else
			pos = Prefs.getPosition(getActivity(), "Red 1");

		boolean blue = pos.contains("Blue");

		//TODO load data

	}

	private void getGUIRefs(View view) {
		// which side are we using
		boolean redLeft = Prefs.getRedLeft(getActivity(), true);
		//and which position are we watching
		Activity act = getActivity();
		String pos;
		if (act instanceof MatchActivity)
			pos = ((MatchActivity) act).getPosition();
		else
			pos = Prefs.getPosition(getActivity(), "Red 1");

		boolean blue = pos.contains("Blue");

		//TODO get references to GUI items
	}

	private void setListeners() {
		//TODO set up listeners
	}


}
