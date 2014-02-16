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

import java.util.ArrayList;
import java.util.List;

import org.frc836.database.DB;
import org.frc836.database.DBSyncService;
import org.frc836.database.DBSyncService.LocalBinder;
import org.growingstems.scouting.MainMenuSelection;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MatchActivity extends Activity implements OnItemSelectedListener {

	private static final int CANCEL_DIALOG = 0;

	private String HELPMESSAGE;

	private DB submitter;

	private MatchStatsAA team1Data;
	private MatchStatsAA team2Data;
	private MatchStatsAA team3Data;

	private EditText teamText1;
	private EditText teamText2;
	private EditText teamText3;

	private LinearLayout[] autoLayouts;

	private int currentView; // 0=auto, 1=cycles, 2=endgame
	private int currentCycle;

	private LinearLayout autoLayout;
	private LinearLayout cycleLayout;
	private LinearLayout endGameLayout;

	private EditText matchT;

	private Button lastB;
	private Button nextB;

	private TextView cycleT;

	private EditText team1Notes;
	private EditText team2Notes;
	private EditText team3Notes;

	private Spinner team1CommonNotes;
	private Spinner team2CommonNotes;
	private Spinner team3CommonNotes;

	private LocalBinder binder;

	private ServiceWatcher watcher = new ServiceWatcher();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.match);

		Intent sync = new Intent(this, DBSyncService.class);
		bindService(sync, watcher, Context.BIND_AUTO_CREATE);

		lastB = (Button) findViewById(R.id.backB);
		nextB = (Button) findViewById(R.id.nextB);

		HELPMESSAGE = "Record the match data on these screens.\n"
				+ "Record data for the 3 autonomous balls on the first screen, then separate data for each cycle of the match.\n\n"
				+ "AH = Auto High\n" + "AL = Auto Low\n" + "H = High\n"
				+ "L = Low\n" + "Yellow Text = Hot Goal\n";

		teamText1 = (EditText) findViewById(R.id.team1T);
		teamText2 = (EditText) findViewById(R.id.team2T);
		teamText3 = (EditText) findViewById(R.id.team3T);

		team1Notes = (EditText) findViewById(R.id.team1notes);
		team2Notes = (EditText) findViewById(R.id.team2notes);
		team3Notes = (EditText) findViewById(R.id.team3notes);

		team1CommonNotes = (Spinner) findViewById(R.id.team1common_notes);
		team2CommonNotes = (Spinner) findViewById(R.id.team2common_notes);
		team3CommonNotes = (Spinner) findViewById(R.id.team3common_notes);

		team1CommonNotes.setOnItemSelectedListener(this);
		team2CommonNotes.setOnItemSelectedListener(this);
		team3CommonNotes.setOnItemSelectedListener(this);

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
		endGameLayout = (LinearLayout) findViewById(R.id.endGameLayout);

		Intent intent = getIntent();

		String team1 = intent.getStringExtra("team1");
		String team2 = intent.getStringExtra("team2");
		String team3 = intent.getStringExtra("team3");

		String match = intent.getStringExtra("match");

		submitter = new DB(this, binder);

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

		cycleT = (TextView) findViewById(R.id.cycleText);

		cycleT.setTextSize(new Button(this).getTextSize());

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

		List<String> options = submitter.getNotesOptions();
		
		if (options == null)
			options = new ArrayList<String>(1);

		options.add(0, team1CommonNotes.getItemAtPosition(0).toString());

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, options);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		team1CommonNotes.setAdapter(adapter);
		team2CommonNotes.setAdapter(adapter);
		team3CommonNotes.setAdapter(adapter);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(watcher);
	}

	protected class ServiceWatcher implements ServiceConnection {

		public void onServiceConnected(ComponentName name, IBinder service) {
			if (service instanceof LocalBinder) {
				binder = (LocalBinder) service;
			}
		}

		public void onServiceDisconnected(ComponentName name) {
		}

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

			findViewById(R.id.team1FarPoss).setBackgroundResource(
					R.drawable.bluebackground);
			findViewById(R.id.team2FarPoss).setBackgroundResource(
					R.drawable.bluebackground);
			findViewById(R.id.team3FarPoss).setBackgroundResource(
					R.drawable.bluebackground);
			findViewById(R.id.team1NearPoss).setBackgroundResource(
					R.drawable.redbackground);
			findViewById(R.id.team2NearPoss).setBackgroundResource(
					R.drawable.redbackground);
			findViewById(R.id.team3NearPoss).setBackgroundResource(
					R.drawable.redbackground);

		} else {
			teamText1.setBackgroundResource(R.drawable.redborder);
			teamText2.setBackgroundResource(R.drawable.redborder);
			teamText3.setBackgroundResource(R.drawable.redborder);
			for (LinearLayout layout : autoLayouts) {
				layout.setBackgroundResource(R.drawable.redborder);
			}
			findViewById(R.id.team1FarPoss).setBackgroundResource(
					R.drawable.redbackground);
			findViewById(R.id.team2FarPoss).setBackgroundResource(
					R.drawable.redbackground);
			findViewById(R.id.team3FarPoss).setBackgroundResource(
					R.drawable.redbackground);
			findViewById(R.id.team1NearPoss).setBackgroundResource(
					R.drawable.bluebackground);
			findViewById(R.id.team2NearPoss).setBackgroundResource(
					R.drawable.bluebackground);
			findViewById(R.id.team3NearPoss).setBackgroundResource(
					R.drawable.bluebackground);
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

	public void onPoss(View v) {
		if (!(v instanceof Button))
			return;

		Button poss = (Button) v;

		if (poss.getText().toString().contains("X"))
			poss.setText("");
		else
			poss.setText("X");

		// TODO highlight assists
	}

	public void onTruss(View v) {
		if (!(v instanceof ToggleButton))
			return;
		ToggleButton truss = (ToggleButton) v;

		if (truss.isChecked()) {
			switch (truss.getId()) {
			case R.id.team1Truss:
				findViewById(R.id.team1Catch).setEnabled(false);
				findViewById(R.id.team2Catch).setEnabled(true);
				findViewById(R.id.team3Catch).setEnabled(true);
				((ToggleButton) findViewById(R.id.team2Truss))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3Truss))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1Catch))
						.setChecked(false);
				break;
			case R.id.team2Truss:
				findViewById(R.id.team1Catch).setEnabled(true);
				findViewById(R.id.team2Catch).setEnabled(false);
				findViewById(R.id.team3Catch).setEnabled(true);
				((ToggleButton) findViewById(R.id.team1Truss))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3Truss))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2Catch))
						.setChecked(false);
				break;
			case R.id.team3Truss:
				findViewById(R.id.team1Catch).setEnabled(true);
				findViewById(R.id.team2Catch).setEnabled(true);
				findViewById(R.id.team3Catch).setEnabled(false);
				((ToggleButton) findViewById(R.id.team2Truss))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1Truss))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3Catch))
						.setChecked(false);
				break;
			}
		} else {
			findViewById(R.id.team1Catch).setEnabled(false);
			findViewById(R.id.team2Catch).setEnabled(false);
			findViewById(R.id.team3Catch).setEnabled(false);
			((ToggleButton) findViewById(R.id.team1Catch)).setChecked(false);
			((ToggleButton) findViewById(R.id.team2Catch)).setChecked(false);
			((ToggleButton) findViewById(R.id.team3Catch)).setChecked(false);
		}
	}

	public void onCatch(View v) {
		if (!(v instanceof ToggleButton))
			return;

		ToggleButton c = (ToggleButton) v;

		if (c.isChecked()) {
			switch (c.getId()) {
			case R.id.team1Catch:
				((ToggleButton) findViewById(R.id.team2Catch))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3Catch))
						.setChecked(false);
				break;
			case R.id.team2Catch:
				((ToggleButton) findViewById(R.id.team1Catch))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3Catch))
						.setChecked(false);
				break;
			case R.id.team3Catch:
				((ToggleButton) findViewById(R.id.team2Catch))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1Catch))
						.setChecked(false);
				break;
			}
		}
	}

	public void onHigh(View v) {
		if (!(v instanceof ToggleButton))
			return;

		ToggleButton c = (ToggleButton) v;

		if (c.isChecked()) {
			switch (c.getId()) {
			case R.id.team1HighScore:
				((ToggleButton) findViewById(R.id.team2HighScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3HighScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1LowScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2LowScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3LowScore))
						.setChecked(false);
				break;
			case R.id.team2HighScore:
				((ToggleButton) findViewById(R.id.team1HighScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3HighScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1LowScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2LowScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3LowScore))
						.setChecked(false);
				break;
			case R.id.team3HighScore:
				((ToggleButton) findViewById(R.id.team1HighScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2HighScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1LowScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2LowScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3LowScore))
						.setChecked(false);
				break;
			}
		}
	}

	public void onLow(View v) {
		if (!(v instanceof ToggleButton))
			return;

		ToggleButton c = (ToggleButton) v;

		if (c.isChecked()) {
			switch (c.getId()) {
			case R.id.team1LowScore:
				((ToggleButton) findViewById(R.id.team2LowScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3LowScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1HighScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2HighScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3HighScore))
						.setChecked(false);
				break;
			case R.id.team2LowScore:
				((ToggleButton) findViewById(R.id.team1LowScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3LowScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1HighScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2HighScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3HighScore))
						.setChecked(false);
				break;
			case R.id.team3LowScore:
				((ToggleButton) findViewById(R.id.team1LowScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2LowScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team1HighScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team2HighScore))
						.setChecked(false);
				((ToggleButton) findViewById(R.id.team3HighScore))
						.setChecked(false);
				break;
			}
		}
	}

	public void nextCycle(View v) {
		saveCycle();
		currentCycle++;
		loadCycle(currentCycle);
		findViewById(R.id.lastCycleB).setEnabled(true);
		((TextView) findViewById(R.id.cycleText)).setText(String
				.valueOf(currentCycle));
	}

	public void lastCycle(View v) {
		if (currentCycle <= 1)
			return;
		saveCycle();
		currentCycle--;
		loadCycle(currentCycle);
		if (currentCycle <= 1)
			findViewById(R.id.lastCycleB).setEnabled(false);
		((TextView) findViewById(R.id.cycleText)).setText(String
				.valueOf(currentCycle));
	}

	private void saveCycle() {
		saveTeamInfo();

		MatchStatsAA.CycleStatsStruct cycle1 = team1Data.cycles
				.get(currentCycle);
		MatchStatsAA.CycleStatsStruct cycle2 = team2Data.cycles
				.get(currentCycle);
		MatchStatsAA.CycleStatsStruct cycle3 = team3Data.cycles
				.get(currentCycle);
		if (cycle1 == null)
			cycle1 = team1Data.new CycleStatsStruct();
		if (cycle2 == null)
			cycle2 = team2Data.new CycleStatsStruct();
		if (cycle3 == null)
			cycle3 = team3Data.new CycleStatsStruct();

		cycle1.cycle_number = currentCycle;
		cycle1.near_poss = ((Button) findViewById(R.id.team1NearPoss))
				.getText().toString().contains("X");
		cycle1.white_poss = ((Button) findViewById(R.id.team1WhitePoss))
				.getText().toString().contains("X");
		cycle1.far_poss = ((Button) findViewById(R.id.team1FarPoss)).getText()
				.toString().contains("X");
		cycle1.truss = ((ToggleButton) findViewById(R.id.team1Truss))
				.isChecked();
		cycle1.truss_catch = ((ToggleButton) findViewById(R.id.team1Catch))
				.isChecked();
		cycle1.high = ((ToggleButton) findViewById(R.id.team1HighScore))
				.isChecked();
		cycle1.low = ((ToggleButton) findViewById(R.id.team1LowScore))
				.isChecked();

		cycle2.cycle_number = currentCycle;
		cycle2.near_poss = ((Button) findViewById(R.id.team2NearPoss))
				.getText().toString().contains("X");
		cycle2.white_poss = ((Button) findViewById(R.id.team2WhitePoss))
				.getText().toString().contains("X");
		cycle2.far_poss = ((Button) findViewById(R.id.team2FarPoss)).getText()
				.toString().contains("X");
		cycle2.truss = ((ToggleButton) findViewById(R.id.team2Truss))
				.isChecked();
		cycle2.truss_catch = ((ToggleButton) findViewById(R.id.team2Catch))
				.isChecked();
		cycle2.high = ((ToggleButton) findViewById(R.id.team2HighScore))
				.isChecked();
		cycle2.low = ((ToggleButton) findViewById(R.id.team2LowScore))
				.isChecked();

		cycle3.cycle_number = currentCycle;
		cycle3.near_poss = ((Button) findViewById(R.id.team3NearPoss))
				.getText().toString().contains("X");
		cycle3.white_poss = ((Button) findViewById(R.id.team3WhitePoss))
				.getText().toString().contains("X");
		cycle3.far_poss = ((Button) findViewById(R.id.team3FarPoss)).getText()
				.toString().contains("X");
		cycle3.truss = ((ToggleButton) findViewById(R.id.team3Truss))
				.isChecked();
		cycle3.truss_catch = ((ToggleButton) findViewById(R.id.team3Catch))
				.isChecked();
		cycle3.high = ((ToggleButton) findViewById(R.id.team3HighScore))
				.isChecked();
		cycle3.low = ((ToggleButton) findViewById(R.id.team3LowScore))
				.isChecked();

		// TODO save off assist count

		team1Data.cycles.put(currentCycle, cycle1);
		team2Data.cycles.put(currentCycle, cycle2);
		team3Data.cycles.put(currentCycle, cycle3);
	}

	private void loadCycle(int cycle) {

		MatchStatsAA.CycleStatsStruct cycle1 = team1Data.cycles.get(cycle);
		MatchStatsAA.CycleStatsStruct cycle2 = team2Data.cycles.get(cycle);
		MatchStatsAA.CycleStatsStruct cycle3 = team3Data.cycles.get(cycle);

		if (cycle1 == null) {
			((Button) findViewById(R.id.team1FarPoss)).setText("");
			((Button) findViewById(R.id.team1WhitePoss)).setText("");
			((Button) findViewById(R.id.team1NearPoss)).setText("");
			((ToggleButton) findViewById(R.id.team1Truss)).setChecked(false);
			((ToggleButton) findViewById(R.id.team1Catch)).setChecked(false);
			((ToggleButton) findViewById(R.id.team1HighScore))
					.setChecked(false);
			((ToggleButton) findViewById(R.id.team1LowScore)).setChecked(false);
		} else {
			((Button) findViewById(R.id.team1FarPoss))
					.setText(cycle1.far_poss ? "X" : "");
			((Button) findViewById(R.id.team1WhitePoss))
					.setText(cycle1.white_poss ? "X" : "");
			((Button) findViewById(R.id.team1NearPoss))
					.setText(cycle1.near_poss ? "X" : "");
			((ToggleButton) findViewById(R.id.team1Truss))
					.setChecked(cycle1.truss);
			if (cycle1.truss)
				onTruss(findViewById(R.id.team1Truss));
			((ToggleButton) findViewById(R.id.team1Catch))
					.setChecked(cycle1.truss_catch);
			if (cycle1.truss_catch)
				onCatch(findViewById(R.id.team1Catch));
			((ToggleButton) findViewById(R.id.team1HighScore))
					.setChecked(cycle1.high);
			if (cycle1.high)
				onHigh(findViewById(R.id.team1HighScore));
			((ToggleButton) findViewById(R.id.team1LowScore))
					.setChecked(cycle1.low);
			if (cycle1.low)
				onLow(findViewById(R.id.team1LowScore));
		}
		if (cycle2 == null) {
			((Button) findViewById(R.id.team2FarPoss)).setText("");
			((Button) findViewById(R.id.team2WhitePoss)).setText("");
			((Button) findViewById(R.id.team2NearPoss)).setText("");
			((ToggleButton) findViewById(R.id.team2Truss)).setChecked(false);
			((ToggleButton) findViewById(R.id.team2Catch)).setChecked(false);
			((ToggleButton) findViewById(R.id.team2HighScore))
					.setChecked(false);
			((ToggleButton) findViewById(R.id.team2LowScore)).setChecked(false);
		} else {
			((Button) findViewById(R.id.team2FarPoss))
					.setText(cycle2.far_poss ? "X" : "");
			((Button) findViewById(R.id.team2WhitePoss))
					.setText(cycle2.white_poss ? "X" : "");
			((Button) findViewById(R.id.team2NearPoss))
					.setText(cycle2.near_poss ? "X" : "");
			((ToggleButton) findViewById(R.id.team2Truss))
					.setChecked(cycle2.truss);
			if (cycle2.truss)
				onTruss(findViewById(R.id.team2Truss));
			((ToggleButton) findViewById(R.id.team2Catch))
					.setChecked(cycle2.truss_catch);
			if (cycle2.truss_catch)
				onCatch(findViewById(R.id.team2Catch));
			((ToggleButton) findViewById(R.id.team2HighScore))
					.setChecked(cycle2.high);
			if (cycle2.high)
				onHigh(findViewById(R.id.team2HighScore));
			((ToggleButton) findViewById(R.id.team2LowScore))
					.setChecked(cycle2.low);
			if (cycle2.low)
				onLow(findViewById(R.id.team2LowScore));
		}
		if (cycle3 == null) {
			((Button) findViewById(R.id.team3FarPoss)).setText("");
			((Button) findViewById(R.id.team3WhitePoss)).setText("");
			((Button) findViewById(R.id.team3NearPoss)).setText("");
			((ToggleButton) findViewById(R.id.team3Truss)).setChecked(false);
			((ToggleButton) findViewById(R.id.team3Catch)).setChecked(false);
			((ToggleButton) findViewById(R.id.team3HighScore))
					.setChecked(false);
			((ToggleButton) findViewById(R.id.team3LowScore)).setChecked(false);
		} else {
			((Button) findViewById(R.id.team3FarPoss))
					.setText(cycle3.far_poss ? "X" : "");
			((Button) findViewById(R.id.team3WhitePoss))
					.setText(cycle3.white_poss ? "X" : "");
			((Button) findViewById(R.id.team3NearPoss))
					.setText(cycle3.near_poss ? "X" : "");
			((ToggleButton) findViewById(R.id.team3Truss))
					.setChecked(cycle3.truss);
			if (cycle3.truss)
				onTruss(findViewById(R.id.team3Truss));
			((ToggleButton) findViewById(R.id.team3Catch))
					.setChecked(cycle3.truss_catch);
			if (cycle3.truss_catch)
				onCatch(findViewById(R.id.team3Catch));
			((ToggleButton) findViewById(R.id.team3HighScore))
					.setChecked(cycle3.high);
			if (cycle3.high)
				onHigh(findViewById(R.id.team3HighScore));
			((ToggleButton) findViewById(R.id.team3LowScore))
					.setChecked(cycle3.low);
			if (cycle3.low)
				onLow(findViewById(R.id.team3LowScore));
		}

		// TODO load assists if added?
	}

	public void onBack(View v) {
		if (currentView == 0) // auto
			showDialog(CANCEL_DIALOG);
		else if (currentView == 1) {
			saveCycle();
			setAuto();
		} else if (currentView == 2) {
			saveEnd();
			setTele();
		}
	}

	public void onNext(View v) {
		if (currentView == 0) {
			saveAuto();
			setTele();
		} else if (currentView == 1) {
			saveCycle();
			setEnd();
		} else if (currentView == 2) {
			saveEnd();
			submit();
		}
	}

	private void saveTeamInfo() {
		String team1 = teamText1.getText().toString();
		if (team1 != null && team1.length() > 0)
			team1Data.team = Integer.valueOf(team1);
		String team2 = teamText2.getText().toString();
		if (team2 != null && team2.length() > 0)
			team2Data.team = Integer.valueOf(team2);
		String team3 = teamText3.getText().toString();
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

	private void saveEnd() {
		saveTeamInfo();

		team1Data.notes = team1Notes.getText().toString();
		team1Data.tipOver = ((CheckBox) findViewById(R.id.team1tipped))
				.isChecked();
		team1Data.foul = ((CheckBox) findViewById(R.id.team1foul)).isChecked();
		team1Data.tech_foul = ((CheckBox) findViewById(R.id.team1techfoul))
				.isChecked();
		team1Data.yellowCard = ((CheckBox) findViewById(R.id.team1yellowcard))
				.isChecked();
		team1Data.redCard = ((CheckBox) findViewById(R.id.team1redcard))
				.isChecked();

		team2Data.notes = team2Notes.getText().toString();
		team2Data.tipOver = ((CheckBox) findViewById(R.id.team2tipped))
				.isChecked();
		team2Data.foul = ((CheckBox) findViewById(R.id.team2foul)).isChecked();
		team2Data.tech_foul = ((CheckBox) findViewById(R.id.team2techfoul))
				.isChecked();
		team2Data.yellowCard = ((CheckBox) findViewById(R.id.team2yellowcard))
				.isChecked();
		team2Data.redCard = ((CheckBox) findViewById(R.id.team2redcard))
				.isChecked();

		team3Data.notes = team3Notes.getText().toString();
		team3Data.tipOver = ((CheckBox) findViewById(R.id.team3tipped))
				.isChecked();
		team3Data.foul = ((CheckBox) findViewById(R.id.team3foul)).isChecked();
		team3Data.tech_foul = ((CheckBox) findViewById(R.id.team3techfoul))
				.isChecked();
		team3Data.yellowCard = ((CheckBox) findViewById(R.id.team3yellowcard))
				.isChecked();
		team3Data.redCard = ((CheckBox) findViewById(R.id.team3redcard))
				.isChecked();
	}

	public void setTele() {
		currentView = 1;
		autoLayout.setVisibility(View.GONE);
		cycleLayout.setVisibility(View.VISIBLE);
		endGameLayout.setVisibility(View.GONE);
		lastB.setText("Auto");
		nextB.setText("End Game");
	}

	public void setAuto() {
		currentView = 0;
		autoLayout.setVisibility(View.VISIBLE);
		cycleLayout.setVisibility(View.GONE);
		endGameLayout.setVisibility(View.GONE);
		lastB.setText("Cancel");
		nextB.setText("Cycles");
	}

	public void setEnd() {
		currentView = 2;
		autoLayout.setVisibility(View.GONE);
		cycleLayout.setVisibility(View.GONE);
		endGameLayout.setVisibility(View.VISIBLE);
		lastB.setText("Cycles");
		nextB.setText("Submit");
	}

	public void submit() {
		submitter.submitMatch(team1Data, team2Data, team3Data);
		nextB.setEnabled(false);
		if (matchT.getText().length() > 0)
			setResult(Integer.valueOf(matchT.getText().toString()) + 1);
		finish();
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

	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {

		if (position == 0 || !(parent instanceof Spinner))
			return;
		EditText notes;
		switch (parent.getId()) {
		case R.id.team1common_notes:
			notes = team1Notes;
			break;
		case R.id.team2common_notes:
			notes = team2Notes;
			break;
		case R.id.team3common_notes:
			notes = team3Notes;
			break;
		default:
			notes = team1Notes;
			break;
		}
		Spinner par = (Spinner) parent;
		String note = notes.getText().toString();
		if (!note.contains(par.getItemAtPosition(position).toString())) {
			if (!note.trim().equals(""))
				note = note + "; ";
			note = note + par.getItemAtPosition(position);
			notes.setText(note);
		}
		par.setSelection(0);

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// do nothing
	}

}
