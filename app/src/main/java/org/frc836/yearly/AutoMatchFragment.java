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
package org.frc836.yearly;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;

import org.frc836.database.MatchStatsStruct;
import org.growingstems.scouting.MatchFragment;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;


public class AutoMatchFragment extends MatchFragment {

	private CheckBox bonusC;

	private ImageButton highB;
	private ImageButton missB;
	private ImageButton lowB;

	private Spinner highS;
	private Spinner missS;
	private Spinner lowS;

	private MatchStatsStruct tempData = new MatchStatsStruct();

	private boolean displayed = false;


	public AutoMatchFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment PreMatch.
	 */
	public static AutoMatchFragment newInstance() {
		return new AutoMatchFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_auto, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		getGUIRefs(view);
		setListeners();
		displayed = true;
	}

	@Override
	public void onResume() {
		super.onResume();
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

		MatchStatsYearly.clearAuto(data);

		data.auto_initiation_move = bonusC.isChecked();

		data.auto_score_high = highS.getSelectedItemPosition();
		data.auto_miss = missS.getSelectedItemPosition();
		data.auto_score_low = lowS.getSelectedItemPosition();

	}

	@Override
	public void loadData(MatchStatsStruct data) {
		tempData = data;
		if (getView() == null || data == null || !displayed)
			return;

		Activity act = getActivity();
		String pos;
		if (act instanceof MatchActivity)
			pos = ((MatchActivity) act).getPosition();
		else
			pos = Prefs.getPosition(getActivity(), "Red 1");


		if (pos.contains("Blue")) {
			highB.setImageResource(R.drawable.blue_port_high);
			missB.setImageResource(R.drawable.blue_port_miss);
			lowB.setImageResource(R.drawable.blue_port_low);
		} else {
			highB.setImageResource(R.drawable.red_port_high);
			missB.setImageResource(R.drawable.red_port_miss);
			lowB.setImageResource(R.drawable.red_port_low);
		}

		bonusC.setChecked(data.auto_initiation_move);

		highS.setSelection(data.auto_score_high);
		missS.setSelection(data.auto_miss);
		lowS.setSelection(data.auto_score_low);

	}

	private void getGUIRefs(View view) {
		bonusC = view.findViewById(R.id.autoBonusC);

		highB = view.findViewById(R.id.auto_port_highB);
		missB = view.findViewById(R.id.auto_port_missB);
		lowB = view.findViewById(R.id.auto_port_lowB);

		highS = view.findViewById(R.id.auto_port_highS);
		missS = view.findViewById(R.id.auto_port_missS);
		lowS = view.findViewById(R.id.auto_port_lowS);
	}

	private void setListeners() {
		highB.setOnClickListener(new OnIncrementListener(highS, 1));
		missB.setOnClickListener(new OnIncrementListener(missS, 1));
		lowB.setOnClickListener(new OnIncrementListener(lowS, 1));

	}

	private class OnIncrementListener implements View.OnClickListener {

		int m_increment;
		Spinner m_spinner;

		OnIncrementListener(Spinner view, int inc) {
			super();
			m_increment = inc;
			m_spinner = view;
		}

		@Override
		public void onClick(View v) {
			m_spinner.setSelection(m_spinner.getSelectedItemPosition() + m_increment);
		}
	}
}
