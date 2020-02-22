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


public class PreMatchFragment extends MatchFragment {


	private boolean displayed = false;

	private MatchStatsStruct tempData = new MatchStatsStruct();

	//these are in order, starting with player station number 1
	//not the order that they appear in the layout
	//getGUIRefs performs this mapping
	private ImageButton pos1B;
	private ImageButton pos2B;
	private ImageButton pos3B;
	private ImageButton pos4B;
	private ImageButton pos5B;

	private FrameLayout pos1L;
	private FrameLayout pos2L;
	private FrameLayout pos3L;
	private FrameLayout pos4L;
	private FrameLayout pos5L;


	private View mainView;

	public PreMatchFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment PreMatch.
	 */
	public static PreMatchFragment newInstance() {
		return new PreMatchFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_pre_match, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		mainView = view;
		getGUIRefs(view);
		setListeners();
		Button swap_sides = view.findViewById(R.id.switchSidesB);
		swap_sides.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switchSides();
			}
		});
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
		data.start_position = tempData.start_position;
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

		pos1B.setImageResource(blue ? R.drawable.blue_station_1 : R.drawable.red_station_1);
		pos2B.setImageResource(blue ? R.drawable.blue_station_port : R.drawable.red_station_port);
		pos3B.setImageResource(blue ? R.drawable.blue_station_2 : R.drawable.red_station_2);
		pos4B.setImageResource(blue ? R.drawable.blue_station_bay : R.drawable.red_station_bay);
		pos5B.setImageResource(blue ? R.drawable.blue_station_3 : R.drawable.red_station_3);

		//if red is on left, flip all resources.
		pos1B.setScaleY(redLeft ? -1f : 1f);
		pos2B.setScaleY(redLeft ? -1f : 1f);
		pos3B.setScaleY(redLeft ? -1f : 1f);
		pos4B.setScaleY(redLeft ? -1f : 1f);
		pos5B.setScaleY(redLeft ? -1f : 1f);
		pos1B.setScaleX(redLeft ? -1f : 1f);
		pos2B.setScaleX(redLeft ? -1f : 1f);
		pos3B.setScaleX(redLeft ? -1f : 1f);
		pos4B.setScaleX(redLeft ? -1f : 1f);
		pos5B.setScaleX(redLeft ? -1f : 1f);

		Drawable blackBorder = ContextCompat.getDrawable(mainView.getContext(), R.drawable.blackborder);
		Drawable selectBorder = ContextCompat.getDrawable(mainView.getContext(), R.drawable.greenborder);
		//set current selections from load
		pos1L.setForeground(blackBorder);
		pos2L.setForeground(blackBorder);
		pos3L.setForeground(blackBorder);
		pos4L.setForeground(blackBorder);
		pos5L.setForeground(blackBorder);
		switch (data.start_position) {
			case 1:
				pos1L.setForeground(selectBorder);
				break;
			case 2:
				pos2L.setForeground(selectBorder);
				break;
			case 3:
				pos3L.setForeground(selectBorder);
				break;
			case 4:
				pos4L.setForeground(selectBorder);
				break;
			case 5:
				pos5L.setForeground(selectBorder);
				break;
			default:
				break;
		}

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

		if (blue ^ redLeft) {
			//pos 1 is top
			pos1L = view.findViewById(R.id.pos1L);
			pos1B = view.findViewById(R.id.pos1B);
			pos2L = view.findViewById(R.id.pos2L);
			pos2B = view.findViewById(R.id.pos2B);
			pos3L = view.findViewById(R.id.pos3L);
			pos3B = view.findViewById(R.id.pos3B);
			pos4L = view.findViewById(R.id.pos4L);
			pos4B = view.findViewById(R.id.pos4B);
			pos5L = view.findViewById(R.id.pos5L);
			pos5B = view.findViewById(R.id.pos5B);

		} else {
			//pos 1 is bottom
			pos1L = view.findViewById(R.id.pos5L);
			pos1B = view.findViewById(R.id.pos5B);
			pos2L = view.findViewById(R.id.pos4L);
			pos2B = view.findViewById(R.id.pos4B);
			pos3L = view.findViewById(R.id.pos3L);
			pos3B = view.findViewById(R.id.pos3B);
			pos4L = view.findViewById(R.id.pos2L);
			pos4B = view.findViewById(R.id.pos2B);
			pos5L = view.findViewById(R.id.pos1L);
			pos5B = view.findViewById(R.id.pos1B);

		}
	}

	private void setListeners() {
		pos1B.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleStartPos(1);
			}
		});
		pos2B.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleStartPos(2);
			}
		});
		pos3B.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleStartPos(3);
			}
		});
		pos4B.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleStartPos(4);
			}
		});
		pos5B.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleStartPos(5);
			}
		});
	}

	private void toggleStartPos(int level) {
		if (tempData.start_position == level)
			tempData.start_position = 0;
		else
			tempData.start_position = level;
		loadData(tempData); //apply to UI
	}


	private void switchSides() {
		saveData(tempData);
		Prefs.setRedLeft(getActivity(), !(Prefs.getRedLeft(getActivity(), true)));
		//need to remap view when side changes
		getGUIRefs(mainView);
		setListeners();
		loadData(tempData);
	}

}
