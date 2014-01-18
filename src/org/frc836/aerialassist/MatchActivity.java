/*
 * Copyright 2014 Daniel Logan
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

package org.frc836.aerialassist;

import org.frc836.database.DB;
import org.growingstems.scouting.MainMenuSelection;
import org.growingstems.scouting.ParamList;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class MatchActivity extends Activity {

	private static final int CANCEL_DIALOG = 0;

	private String HELPMESSAGE;

	private DB submitter;

	private MatchStatsAA team1Data;
	private MatchStatsAA team2Data;
	private MatchStatsAA team3Data;

	private int orientation;

	private ParamList notesOptions;

	private EditText teamText1;
	private EditText teamText2;
	private EditText teamText3;

	private LinearLayout[] autoLayouts;

	private int currentView; // 0=auto, 1=cycles, 2=endgame
	private int currentCycle;
	
	private LinearLayout autoLayout;
	private LinearLayout cycleLayout;

	private EditText matchT;
	
	private Button lastB;
	private Button nextB;
	private Button lastCycleB;
	private Button nextCycleB;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.match);
		
		lastB = (Button) findViewById(R.id.backB);
		nextB = (Button) findViewById(R.id.nextB);
		lastCycleB = (Button) findViewById(R.id.lastCycleB);
		nextCycleB = (Button) findViewById(R.id.nextCycleB);

		orientation = getResources().getConfiguration().orientation;

		// TODO write help message

		notesOptions = new ParamList(getApplicationContext(), "notes_options");

		teamText1 = (EditText) findViewById(R.id.team1T);
		teamText2 = (EditText) findViewById(R.id.team2T);
		teamText3 = (EditText) findViewById(R.id.team3T);

		matchT = (EditText) findViewById(R.id.matchT);

		autoLayouts = new LinearLayout[9];

		autoLayouts[0] = (LinearLayout) findViewById(R.id.team1Ball1Box);
		autoLayouts[1] = (LinearLayout) findViewById(R.id.team1Ball2Box);
		autoLayouts[2] = (LinearLayout) findViewById(R.id.team1Ball3Box);
		autoLayouts[3] = (LinearLayout) findViewById(R.id.team2Ball1Box);
		autoLayouts[4] = (LinearLayout) findViewById(R.id.team2Ball2Box);
		autoLayouts[5] = (LinearLayout) findViewById(R.id.team2Ball3Box);
		autoLayouts[6] = (LinearLayout) findViewById(R.id.team3Ball1Box);
		autoLayouts[7] = (LinearLayout) findViewById(R.id.team3Ball2Box);
		autoLayouts[8] = (LinearLayout) findViewById(R.id.team3Ball3Box);
		
		autoLayout = (LinearLayout) findViewById(R.id.autoLayout);
		cycleLayout = (LinearLayout) findViewById(R.id.cycleLayout);

		Intent intent = getIntent();

		String team1 = intent.getStringExtra("team1");
		String team2 = intent.getStringExtra("team2");
		String team3 = intent.getStringExtra("team3");

		String match = intent.getStringExtra("match");

		submitter = new DB(this);

		teamText1.setText(team1);
		teamText2.setText(team2);
		teamText3.setText(team3);
		matchT.setText(match);
		if (team1 != null && team1.length() > 0 && match != null
				&& match.length() > 0)
			team1Data = new MatchStatsAA(Integer.valueOf(team1),
					Prefs.getEvent(getApplicationContext(),
							"Chesapeake Regional"), Integer.valueOf(match));
		else
			team1Data = new MatchStatsAA();

		if (team2 != null && team2.length() > 0 && match != null
				&& match.length() > 0)
			team2Data = new MatchStatsAA(Integer.valueOf(team2),
					Prefs.getEvent(getApplicationContext(),
							"Chesapeake Regional"), Integer.valueOf(match));
		else
			team2Data = new MatchStatsAA();

		if (team3 != null && team3.length() > 0 && match != null
				&& match.length() > 0)
			team3Data = new MatchStatsAA(Integer.valueOf(team3),
					Prefs.getEvent(getApplicationContext(),
							"Chesapeake Regional"), Integer.valueOf(match));
		else
			team3Data = new MatchStatsAA();

		currentView = 0;
		currentCycle = 1;
		
		setAuto();

	}

	public void onResume() {
		super.onResume();

		team1Data.event = Prefs.getEvent(getApplicationContext(),
				"Chesapeake Regional");
		team2Data.event = Prefs.getEvent(getApplicationContext(),
				"Chesapeake Regional");
		team3Data.event = Prefs.getEvent(getApplicationContext(),
				"Chesapeake Regional");

		updatePosition();
	}

	private void updatePosition() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		String pos = prefs.getString("positionPref", "Red");
		if (pos.contains("Blue")) {
			teamText1.setBackgroundResource(R.drawable.blueborder);
			teamText2.setBackgroundResource(R.drawable.blueborder);
			teamText3.setBackgroundResource(R.drawable.blueborder);
			for (LinearLayout layout : autoLayouts) {
				layout.setBackgroundResource(R.drawable.blueborder);
			}
		} else {
			teamText1.setBackgroundResource(R.drawable.redborder);
			teamText2.setBackgroundResource(R.drawable.redborder);
			teamText3.setBackgroundResource(R.drawable.redborder);
			for (LinearLayout layout : autoLayouts) {
				layout.setBackgroundResource(R.drawable.redborder);
			}
		}
	}

	public void onAutoScoreClick(View v) {
		int viewId = v.getId();

		if (((ToggleButton) v).isChecked()) {
			switch (viewId) {
			// team1box1
			case R.id.team1AutoHigh1:
				((ToggleButton) findViewById(R.id.team1AutoHighHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLow1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLowHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1High1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1Low1)).setChecked(false);
				break;
			case R.id.team1AutoHighHot1:
				((ToggleButton) findViewById(R.id.team1AutoHigh1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLow1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLowHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1High1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1Low1)).setChecked(false);
				break;
			case R.id.team1AutoLow1:
				((ToggleButton) findViewById(R.id.team1AutoHighHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoHigh1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLowHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1High1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1Low1)).setChecked(false);
				break;
			case R.id.team1AutoLowHot1:
				((ToggleButton) findViewById(R.id.team1AutoHighHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLow1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoHigh1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1High1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1Low1)).setChecked(false);
				break;
			case R.id.team1High1:
				((ToggleButton) findViewById(R.id.team1AutoHighHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLow1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLowHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoHigh1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1Low1)).setChecked(false);
				break;
			case R.id.team1Low1:
				((ToggleButton) findViewById(R.id.team1AutoHighHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLow1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLowHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1High1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoHigh1))
						.setChecked(false);
				break;
			// end team1box1
			// team1box2
			case R.id.team1AutoHigh2:
				((ToggleButton) findViewById(R.id.team1AutoHighHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLow2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLowHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1High2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1Low2)).setChecked(false);
				break;
			case R.id.team1AutoHighHot2:
				((ToggleButton) findViewById(R.id.team1AutoHigh2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLow2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLowHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1High2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1Low2)).setChecked(false);
				break;
			case R.id.team1AutoLow2:
				((ToggleButton) findViewById(R.id.team1AutoHighHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoHigh2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLowHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1High2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1Low2)).setChecked(false);
				break;
			case R.id.team1AutoLowHot2:
				((ToggleButton) findViewById(R.id.team1AutoHighHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLow2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoHigh2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1High2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1Low2)).setChecked(false);
				break;
			case R.id.team1High2:
				((ToggleButton) findViewById(R.id.team1AutoHighHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLow2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLowHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoHigh2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1Low2)).setChecked(false);
				break;
			case R.id.team1Low2:
				((ToggleButton) findViewById(R.id.team1AutoHighHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLow2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLowHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1High2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoHigh2))
						.setChecked(false);
				break;
			// end team1box2
			// team1box3
			case R.id.team1AutoHigh3:
				((ToggleButton) findViewById(R.id.team1AutoHighHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLow3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLowHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1High3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1Low3)).setChecked(false);
				break;
			case R.id.team1AutoHighHot3:
				((ToggleButton) findViewById(R.id.team1AutoHigh3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLow3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLowHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1High3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1Low3)).setChecked(false);
				break;
			case R.id.team1AutoLow3:
				((ToggleButton) findViewById(R.id.team1AutoHighHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoHigh3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLowHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1High3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1Low3)).setChecked(false);
				break;
			case R.id.team1AutoLowHot3:
				((ToggleButton) findViewById(R.id.team1AutoHighHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLow3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoHigh3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1High3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1Low3)).setChecked(false);
				break;
			case R.id.team1High3:
				((ToggleButton) findViewById(R.id.team1AutoHighHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLow3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLowHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoHigh3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1Low3)).setChecked(false);
				break;
			case R.id.team1Low3:
				((ToggleButton) findViewById(R.id.team1AutoHighHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLow3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoLowHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1High3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1AutoHigh3))
						.setChecked(false);
				break;
			// end team1box3
			// team2box1
			case R.id.team2AutoHigh1:
				((ToggleButton) findViewById(R.id.team2AutoHighHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLow1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLowHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2High1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2Low1)).setChecked(false);
				break;
			case R.id.team2AutoHighHot1:
				((ToggleButton) findViewById(R.id.team2AutoHigh1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLow1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLowHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2High1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2Low1)).setChecked(false);
				break;
			case R.id.team2AutoLow1:
				((ToggleButton) findViewById(R.id.team2AutoHighHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoHigh1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLowHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2High1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2Low1)).setChecked(false);
				break;
			case R.id.team2AutoLowHot1:
				((ToggleButton) findViewById(R.id.team2AutoHighHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLow1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoHigh1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2High1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2Low1)).setChecked(false);
				break;
			case R.id.team2High1:
				((ToggleButton) findViewById(R.id.team2AutoHighHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLow1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLowHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoHigh1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2Low1)).setChecked(false);
				break;
			case R.id.team2Low1:
				((ToggleButton) findViewById(R.id.team2AutoHighHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLow1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLowHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2High1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoHigh1))
						.setChecked(false);
				break;
			// end team2box1
			// team2box2
			case R.id.team2AutoHigh2:
				((ToggleButton) findViewById(R.id.team2AutoHighHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLow2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLowHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2High2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2Low2)).setChecked(false);
				break;
			case R.id.team2AutoHighHot2:
				((ToggleButton) findViewById(R.id.team2AutoHigh2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLow2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLowHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2High2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2Low2)).setChecked(false);
				break;
			case R.id.team2AutoLow2:
				((ToggleButton) findViewById(R.id.team2AutoHighHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoHigh2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLowHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2High2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2Low2)).setChecked(false);
				break;
			case R.id.team2AutoLowHot2:
				((ToggleButton) findViewById(R.id.team2AutoHighHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLow2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoHigh2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2High2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2Low2)).setChecked(false);
				break;
			case R.id.team2High2:
				((ToggleButton) findViewById(R.id.team2AutoHighHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLow2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLowHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoHigh2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2Low2)).setChecked(false);
				break;
			case R.id.team2Low2:
				((ToggleButton) findViewById(R.id.team2AutoHighHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLow2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLowHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2High2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoHigh2))
						.setChecked(false);
				break;
			// end team2box2
			// team2box3
			case R.id.team2AutoHigh3:
				((ToggleButton) findViewById(R.id.team2AutoHighHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLow3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLowHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2High3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2Low3)).setChecked(false);
				break;
			case R.id.team2AutoHighHot3:
				((ToggleButton) findViewById(R.id.team2AutoHigh3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLow3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLowHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2High3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2Low3)).setChecked(false);
				break;
			case R.id.team2AutoLow3:
				((ToggleButton) findViewById(R.id.team2AutoHighHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoHigh3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLowHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2High3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2Low3)).setChecked(false);
				break;
			case R.id.team2AutoLowHot3:
				((ToggleButton) findViewById(R.id.team2AutoHighHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLow3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoHigh3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2High3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2Low3)).setChecked(false);
				break;
			case R.id.team2High3:
				((ToggleButton) findViewById(R.id.team2AutoHighHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLow3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLowHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoHigh3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2Low3)).setChecked(false);
				break;
			case R.id.team2Low3:
				((ToggleButton) findViewById(R.id.team2AutoHighHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLow3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoLowHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2High3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2AutoHigh3))
						.setChecked(false);
				break;
			// end team2box3
			// team3box1
			case R.id.team3AutoHigh1:
				((ToggleButton) findViewById(R.id.team3AutoHighHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLow1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLowHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3High1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3Low1)).setChecked(false);
				break;
			case R.id.team3AutoHighHot1:
				((ToggleButton) findViewById(R.id.team3AutoHigh1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLow1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLowHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3High1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3Low1)).setChecked(false);
				break;
			case R.id.team3AutoLow1:
				((ToggleButton) findViewById(R.id.team3AutoHighHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoHigh1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLowHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3High1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3Low1)).setChecked(false);
				break;
			case R.id.team3AutoLowHot1:
				((ToggleButton) findViewById(R.id.team3AutoHighHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLow1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoHigh1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3High1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3Low1)).setChecked(false);
				break;
			case R.id.team3High1:
				((ToggleButton) findViewById(R.id.team3AutoHighHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLow1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLowHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoHigh1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3Low1)).setChecked(false);
				break;
			case R.id.team3Low1:
				((ToggleButton) findViewById(R.id.team3AutoHighHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLow1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLowHot1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3High1))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoHigh1))
						.setChecked(false);
				break;
			// end team3box1
			// team3box2
			case R.id.team3AutoHigh2:
				((ToggleButton) findViewById(R.id.team3AutoHighHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLow2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLowHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3High2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3Low2)).setChecked(false);
				break;
			case R.id.team3AutoHighHot2:
				((ToggleButton) findViewById(R.id.team3AutoHigh2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLow2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLowHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3High2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3Low2)).setChecked(false);
				break;
			case R.id.team3AutoLow2:
				((ToggleButton) findViewById(R.id.team3AutoHighHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoHigh2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLowHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3High2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3Low2)).setChecked(false);
				break;
			case R.id.team3AutoLowHot2:
				((ToggleButton) findViewById(R.id.team3AutoHighHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLow2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoHigh2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3High2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3Low2)).setChecked(false);
				break;
			case R.id.team3High2:
				((ToggleButton) findViewById(R.id.team3AutoHighHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLow2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLowHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoHigh2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3Low2)).setChecked(false);
				break;
			case R.id.team3Low2:
				((ToggleButton) findViewById(R.id.team3AutoHighHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLow2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLowHot2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3High2))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoHigh2))
						.setChecked(false);
				break;
			// end team3box2
			// team3box3
			case R.id.team3AutoHigh3:
				((ToggleButton) findViewById(R.id.team3AutoHighHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLow3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLowHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3High3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3Low3)).setChecked(false);
				break;
			case R.id.team3AutoHighHot3:
				((ToggleButton) findViewById(R.id.team3AutoHigh3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLow3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLowHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3High3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3Low3)).setChecked(false);
				break;
			case R.id.team3AutoLow3:
				((ToggleButton) findViewById(R.id.team3AutoHighHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoHigh3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLowHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3High3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3Low3)).setChecked(false);
				break;
			case R.id.team3AutoLowHot3:
				((ToggleButton) findViewById(R.id.team3AutoHighHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLow3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoHigh3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3High3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3Low3)).setChecked(false);
				break;
			case R.id.team3High3:
				((ToggleButton) findViewById(R.id.team3AutoHighHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLow3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLowHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoHigh3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3Low3)).setChecked(false);
				break;
			case R.id.team3Low3:
				((ToggleButton) findViewById(R.id.team3AutoHighHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLow3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoLowHot3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3High3))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3AutoHigh3))
						.setChecked(false);
				break;
			// end team3box3
			default:
				// this should never happen
				break;
			}
		}
	}

	public void onBack(View v) {
		if (currentView == 0) // auto
			showDialog(CANCEL_DIALOG);
		else if (currentView == 1)
		{
			saveTele();
			setAuto();
		}

		// TODO
	}

	public void onNext(View v) {
		if (currentView == 0) {
			saveAuto();
			setTele();
		}
		// TODO
	}

	private void saveTeamInfo() {
		String team1 = teamText1.getText().toString();
		if (team1 != null && team1.length() > 0)
			team1Data.team = Integer.valueOf(team1);
		String team2 = teamText1.getText().toString();
		if (team2 != null && team2.length() > 0)
			team2Data.team = Integer.valueOf(team2);
		String team3 = teamText1.getText().toString();
		if (team3 != null && team3.length() > 0)
			team3Data.team = Integer.valueOf(team3);
		String match = matchT.getText().toString();
		if (match != null && match.length() > 0) {
			team1Data.match = Integer.valueOf(match);
			team2Data.match = Integer.valueOf(match);
			team3Data.match = Integer.valueOf(match);
		}
	}

	private void saveAuto() {
		saveTeamInfo();
		// team1
		team1Data.auto_mobile = ((CheckBox) findViewById(R.id.team1AutoMoved))
				.isChecked();
		team1Data.auto_high = (((ToggleButton) findViewById(R.id.team1AutoHigh1))
				.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team1AutoHigh2))
						.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team1AutoHigh3))
						.isChecked() ? 1 : 0);
		team1Data.auto_high_hot = (((ToggleButton) findViewById(R.id.team1AutoHighHot1))
				.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team1AutoHighHot2))
						.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team1AutoHighHot3))
						.isChecked() ? 1 : 0);
		team1Data.auto_low = (((ToggleButton) findViewById(R.id.team1AutoLow1))
				.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team1AutoLow2))
						.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team1AutoLow3))
						.isChecked() ? 1 : 0);
		team1Data.auto_low_hot = (((ToggleButton) findViewById(R.id.team1AutoLowHot1))
				.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team1AutoLowHot2))
						.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team1AutoLowHot3))
						.isChecked() ? 1 : 0);
		team1Data.high = (((ToggleButton) findViewById(R.id.team1High1))
				.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team1High2)).isChecked() ? 1
						: 0)
				+ (((ToggleButton) findViewById(R.id.team1High3)).isChecked() ? 1
						: 0);
		team1Data.low = (((ToggleButton) findViewById(R.id.team1Low1))
				.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team1Low2)).isChecked() ? 1
						: 0)
				+ (((ToggleButton) findViewById(R.id.team1Low3)).isChecked() ? 1
						: 0);

		// team2
		team2Data.auto_mobile = ((CheckBox) findViewById(R.id.team2AutoMoved))
				.isChecked();
		team2Data.auto_high = (((ToggleButton) findViewById(R.id.team2AutoHigh1))
				.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team2AutoHigh2))
						.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team2AutoHigh3))
						.isChecked() ? 1 : 0);
		team2Data.auto_high_hot = (((ToggleButton) findViewById(R.id.team2AutoHighHot1))
				.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team2AutoHighHot2))
						.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team2AutoHighHot3))
						.isChecked() ? 1 : 0);
		team2Data.auto_low = (((ToggleButton) findViewById(R.id.team2AutoLow1))
				.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team2AutoLow2))
						.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team2AutoLow3))
						.isChecked() ? 1 : 0);
		team2Data.auto_low_hot = (((ToggleButton) findViewById(R.id.team2AutoLowHot1))
				.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team2AutoLowHot2))
						.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team2AutoLowHot3))
						.isChecked() ? 1 : 0);
		team2Data.high = (((ToggleButton) findViewById(R.id.team2High1))
				.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team2High2)).isChecked() ? 1
						: 0)
				+ (((ToggleButton) findViewById(R.id.team2High3)).isChecked() ? 1
						: 0);
		team2Data.low = (((ToggleButton) findViewById(R.id.team2Low1))
				.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team2Low2)).isChecked() ? 1
						: 0)
				+ (((ToggleButton) findViewById(R.id.team2Low3)).isChecked() ? 1
						: 0);

		// team3
		team3Data.auto_mobile = ((CheckBox) findViewById(R.id.team3AutoMoved))
				.isChecked();
		team3Data.auto_high = (((ToggleButton) findViewById(R.id.team3AutoHigh1))
				.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team3AutoHigh2))
						.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team3AutoHigh3))
						.isChecked() ? 1 : 0);
		team3Data.auto_high_hot = (((ToggleButton) findViewById(R.id.team3AutoHighHot1))
				.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team3AutoHighHot2))
						.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team3AutoHighHot3))
						.isChecked() ? 1 : 0);
		team3Data.auto_low = (((ToggleButton) findViewById(R.id.team3AutoLow1))
				.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team3AutoLow2))
						.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team3AutoLow3))
						.isChecked() ? 1 : 0);
		team3Data.auto_low_hot = (((ToggleButton) findViewById(R.id.team3AutoLowHot1))
				.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team3AutoLowHot2))
						.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team3AutoLowHot3))
						.isChecked() ? 1 : 0);
		team3Data.high = (((ToggleButton) findViewById(R.id.team3High1))
				.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team3High2)).isChecked() ? 1
						: 0)
				+ (((ToggleButton) findViewById(R.id.team3High3)).isChecked() ? 1
						: 0);
		team3Data.low = (((ToggleButton) findViewById(R.id.team3Low1))
				.isChecked() ? 1 : 0)
				+ (((ToggleButton) findViewById(R.id.team3Low2)).isChecked() ? 1
						: 0)
				+ (((ToggleButton) findViewById(R.id.team3Low3)).isChecked() ? 1
						: 0);
	}

	public void saveTele() {
		// TODO
	}
	
	public void setTele() {
		currentView = 1;
		autoLayout.setVisibility(View.GONE);
		cycleLayout.setVisibility(View.VISIBLE);
		lastB.setText("Auto");
		nextB.setText("End Game");
	}
	
	public void setAuto() {
		currentView = 0;
		autoLayout.setVisibility(View.VISIBLE);
		cycleLayout.setVisibility(View.GONE);
		lastB.setText("Cancel");
		nextB.setText("Cycles"); 
	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case CANCEL_DIALOG:
			builder.setMessage(
					"Cancel Match Entry?\nChanges will not be saved.")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									MatchActivity.this.finish();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			dialog = builder.create();
			break;
		case MainMenuSelection.HELPDIALOG:
			builder.setMessage(HELPMESSAGE)
					.setCancelable(true)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();

								}
							});
			dialog = builder.create();
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return MainMenuSelection.onOptionsItemSelected(item, this) ? true
				: super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		onBack(null);
		return;
	}

}
