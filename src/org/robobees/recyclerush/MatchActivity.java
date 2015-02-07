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

package org.robobees.recyclerush;

import java.util.ArrayList;
import java.util.List;

import org.frc836.database.DB;
import org.frc836.database.DBSyncService;
import org.frc836.database.DBSyncService.LocalBinder;
import org.robobees.recyclerush.MatchActivity;
import org.robobees.recyclerush.MatchActivity.NotesSelectedListener;
import org.robobees.recyclerush.MatchStatsRR;
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
import android.view.View.OnClickListener;
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

public class MatchActivity extends Activity implements OnClickListener {

	private static final int CANCEL_DIALOG = 0;
	private static final int LOAD_DIALOG = 353563;

	private String HELPMESSAGE;

	private DB submitter;

	private MatchStatsRR teamData;
	private MatchStatsRR auto;
	private MatchStatsRR tele;

	private EditText teamText;

	private int currentView; // 0=auto, 1=tele, 2=endgame

	private LinearLayout autoLayout;
	private LinearLayout teleLayout;
	private LinearLayout endGameLayout;

	private EditText matchT;

	private Button lastB;
	private Button nextB;
	
	private CheckBox aMoved;
	private CheckBox aT3;
	private CheckBox aT2;
	private Button bins_scored;
	private Button aAdd1;
	private Button bins_step;
	private Spinner numBins_scored;
	private Spinner aCount1;
	private Spinner numBins_step;

	private Button addLf;
	private Button addLBin;
	private Button gAdd6;
	private Button gAdd5;
	private Button gAdd4;
	private Button gAdd3;
	private Button gAdd2;
	private Button gAdd1;
	private Button yAdd4;
	private Button yAdd3;
	private Button yAdd2;
	private Button yAdd1;
	private Button binAdd6;
	private Button binAdd5;
	private Button binAdd4;
	private Button binAdd3;
	private Button binAdd2;
	private Button binAdd1;
	private Spinner LfCount;
	private Spinner LBinCount;
	private Spinner gCount6;
	private Spinner gCount5;
	private Spinner gCount4;
	private Spinner gCount3;
	private Spinner gCount2;
	private Spinner gCount1;
	private Spinner yCount4;
	private Spinner yCount3;
	private Spinner yCount2;
	private Spinner yCount1;
	private Spinner binCount6;
	private Spinner binCount5;
	private Spinner binCount4;
	private Spinner binCount3;
	private Spinner binCount2;
	private Spinner binCount1;

	private EditText teamNotes;
	private Spinner commonNotes;
	private CheckBox botTip;
	private CheckBox stackTip;
	private CheckBox yellowcard;
	private CheckBox redcard;
	private CheckBox foul;

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
				+ "Y0 dawg, just record data for any totes or garbage cans moved on the first screen, then .\n\n"
				+ "AH = Auto High\n" + "AL = Auto Low\n" + "H = High\n"
				+ "L = Low\n" + "Yellow Text = Hot Goal\n";

		teamText = (EditText) findViewById(R.id.teamNum);

		teamNotes = (EditText) findViewById(R.id.notes);

		commonNotes = (Spinner) findViewById(R.id.commonNotes);

		commonNotes.setOnItemSelectedListener(new NotesSelectedListener());

		matchT = (EditText) findViewById(R.id.matchNum);

		autoLayout = (LinearLayout) findViewById(R.id.autoLayout);
		teleLayout = (LinearLayout) findViewById(R.id.teleLayout);
		endGameLayout = (LinearLayout) findViewById(R.id.endGameLayout);

		Intent intent = getIntent();

		String team = intent.getStringExtra("team");

		String match = intent.getStringExtra("match");

		submitter = new DB(this, binder);

		teamText.setText(team);
		matchT.setText(match);
		boolean loadData = false;
		if (team != null && team.length() > 0 && match != null
				&& match.length() > 0) {
			teamData = (MatchStatsRR) submitter.getMatchStats(Prefs.getEvent(
					getApplicationContext(), "Chesapeake Regional"), Integer
					.valueOf(match), Integer.valueOf(team), Prefs.getPracticMatch(getApplicationContext(), false));
			if (teamData == null)
				teamData = new MatchStatsRR(Integer.valueOf(team),
						Prefs.getEvent(getApplicationContext(),
								"Chesapeake Regional"), Integer.valueOf(match), Prefs.getPracticMatch(getApplicationContext(), false));
			else
				loadData = true;
		} else
			teamData = new MatchStatsRR();

		if (loadData) {
			showDialog(LOAD_DIALOG);
		}

		currentView = 0;

		loadAuto();
		loadTele();
		loadEndgame();

		setAuto();

	}

	public void onResume() {
		super.onResume();

		teamData.event = Prefs.getEvent(getApplicationContext(),
				"Chesapeake Regional");

		updatePosition();

		List<String> options = submitter.getNotesOptions();

		if (options == null)
			options = new ArrayList<String>(1);

		options.add(0, commonNotes.getItemAtPosition(0).toString());

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, options);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		commonNotes.setAdapter(adapter);

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
				submitter.setBinder(binder);
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
			teamText.setBackgroundResource(R.drawable.blueborder);
		} else {
			teamText.setBackgroundResource(R.drawable.redborder);
		}
	}

	private void saveTele() {
		saveTeamInfo();

		tele.landfill_litter = (short) LfCount.getSelectedItemPosition();
		tele.bin_litter = (short) LBinCount.getSelectedItemPosition();
		tele.totes[5] = (short) gCount6.getSelectedItemPosition();
		tele.totes[4] = (short) gCount5.getSelectedItemPosition();
		tele.totes[3] = (short) gCount4.getSelectedItemPosition();
		tele.totes[2] = (short) gCount3.getSelectedItemPosition();
		tele.totes[1] = (short) gCount2.getSelectedItemPosition();
		tele.totes[0] = (short) gCount1.getSelectedItemPosition();
		tele.coops[3] = (short) yCount4.getSelectedItemPosition();
		tele.coops[2] = (short) yCount3.getSelectedItemPosition();
		tele.coops[1] = (short) yCount2.getSelectedItemPosition();
		tele.coops[0] = (short) yCount1.getSelectedItemPosition();
		tele.bins[5] = (short) binCount6.getSelectedItemPosition();
		tele.bins[4] = (short) binCount5.getSelectedItemPosition();
		tele.bins[3] = (short) binCount4.getSelectedItemPosition();
		tele.bins[2] = (short) binCount3.getSelectedItemPosition();
		tele.bins[1] = (short) binCount2.getSelectedItemPosition();
		tele.bins[0] = (short) binCount1.getSelectedItemPosition();
	}

	private void loadAuto() {
		((CheckBox) findViewById(R.id.aMoved)).setChecked(teamData.auto_move);
		
		aMoved = (CheckBox) findViewById(R.id.aMoved);
		aT3 = (CheckBox) findViewById(R.id.aT3);
		aT2 = (CheckBox) findViewById(R.id.aT2);
		bins_scored = (Button) findViewById(R.id.bins_scored);
		gAdd1 = (Button) findViewById(R.id.aAdd1);
		bins_step = (Button) findViewById(R.id.bins_step);
		numBins_scored = (Spinner) findViewById(R.id.numBins_scored);
		aCount1 = (Spinner) findViewById(R.id.aCount1);
		numBins_step = (Spinner) findViewById(R.id.numBins_step);
	}

	private void loadEndgame() {
		((EditText) findViewById(R.id.notes)).setText(teamData.notes);
		((CheckBox) findViewById(R.id.botTip)).setChecked(teamData.tipOver);
		((CheckBox) findViewById(R.id.stackTip)).setChecked(teamData.tipped_stack);
		((CheckBox) findViewById(R.id.foul)).setChecked(teamData.foul);
		((CheckBox) findViewById(R.id.redcard)).setChecked(teamData.redCard);
		((CheckBox) findViewById(R.id.yellowcard)).setChecked(teamData.yellowCard);
	}

	private void loadTele() {

		addLf = (Button) findViewById(R.id.addLf);
		addLf.setOnClickListener(this);
		addLBin = (Button) findViewById(R.id.addLBin);
		addLBin.setOnClickListener(this);
		gAdd6 = (Button) findViewById(R.id.gAdd6);
		gAdd6.setOnClickListener(this);
		gAdd5 = (Button) findViewById(R.id.gAdd5);
		gAdd5.setOnClickListener(this);
		gAdd4 = (Button) findViewById(R.id.gAdd4);
		gAdd4.setOnClickListener(this);
		gAdd3 = (Button) findViewById(R.id.gAdd3);
		gAdd3.setOnClickListener(this);
		gAdd2 = (Button) findViewById(R.id.gAdd2);
		gAdd2.setOnClickListener(this);
		gAdd1 = (Button) findViewById(R.id.gAdd1);
		gAdd1.setOnClickListener(this);
		yAdd4 = (Button) findViewById(R.id.yAdd4);
		yAdd4.setOnClickListener(this);
		yAdd3 = (Button) findViewById(R.id.yAdd3);
		yAdd3.setOnClickListener(this);
		yAdd2 = (Button) findViewById(R.id.yAdd2);
		yAdd2.setOnClickListener(this);
		yAdd1 = (Button) findViewById(R.id.yAdd1);
		yAdd1.setOnClickListener(this);
		binAdd6 = (Button) findViewById(R.id.binAdd6);
		binAdd6.setOnClickListener(this);
		binAdd5 = (Button) findViewById(R.id.binAdd5);
		binAdd5.setOnClickListener(this);
		binAdd4 = (Button) findViewById(R.id.binAdd4);
		binAdd4.setOnClickListener(this);
		binAdd3 = (Button) findViewById(R.id.binAdd3);
		binAdd3.setOnClickListener(this);
		binAdd2 = (Button) findViewById(R.id.binAdd2);
		binAdd2.setOnClickListener(this);
		binAdd1 = (Button) findViewById(R.id.binAdd1);
		binAdd1.setOnClickListener(this);
		LfCount = (Spinner) findViewById(R.id.lfCount);
		LBinCount = (Spinner) findViewById(R.id.lBinCount);
		gCount6 = (Spinner) findViewById(R.id.gCount6);
		gCount5 = (Spinner) findViewById(R.id.gCount5);
		gCount4 = (Spinner) findViewById(R.id.gCount4);
		gCount3 = (Spinner) findViewById(R.id.gCount3);
		gCount2 = (Spinner) findViewById(R.id.gCount2);
		gCount1 = (Spinner) findViewById(R.id.gCount1);
		yCount4 = (Spinner) findViewById(R.id.yCount4);
		yCount3 = (Spinner) findViewById(R.id.yCount3);
		yCount2 = (Spinner) findViewById(R.id.yCount2);
		yCount1 = (Spinner) findViewById(R.id.yCount1);
		binCount6 = (Spinner) findViewById(R.id.binCount6);
		binCount5 = (Spinner) findViewById(R.id.binCount5);
		binCount4 = (Spinner) findViewById(R.id.binCount4);
		binCount3 = (Spinner) findViewById(R.id.binCount3);
		binCount2 = (Spinner) findViewById(R.id.binCount2);
		binCount1 = (Spinner) findViewById(R.id.binCount1);
		}


	public void onBack(View v) {
		if (currentView == 0) // auto
			showDialog(CANCEL_DIALOG);
		else if (currentView == 1) {
			saveTele();
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
			saveTele();
			setEnd();
		} else if (currentView == 2) {
			saveEnd();
			submit();
		}
	}

	private void saveTeamInfo() {
		String team = teamText.getText().toString();
		if (team != null && team.length() > 0)
			teamData.team = Integer.valueOf(team);
		String match = matchT.getText().toString();
		if (match != null && match.length() > 0) {
			teamData.match = Integer.valueOf(match);
		}
	}

	private void saveAuto() {
		saveTeamInfo();
		
		auto.auto_move = aMoved.isChecked();
		auto.auto_stack_3 = aT3.isChecked();
		auto.auto_stack_2 = aT2.isChecked();
		auto.auto_totes = (short) aCount1.getSelectedItemPosition();
		auto.auto_bin = (short) numBins_scored.getSelectedItemPosition();
		auto.auto_step_bin = (short) numBins_step.getSelectedItemPosition();
	}
	
	private void saveEnd() {
		saveTeamInfo();

		teamData.notes = teamNotes.getText().toString();
		teamData.tipOver = ((CheckBox) findViewById(R.id.botTip)).isChecked();
		teamData.tipped_stack = ((CheckBox) findViewById(R.id.stackTip)).isChecked();
		teamData.foul = ((CheckBox) findViewById(R.id.foul)).isChecked();
		teamData.yellowCard = ((CheckBox) findViewById(R.id.yellowcard)).isChecked();
		teamData.redCard = ((CheckBox) findViewById(R.id.redcard)).isChecked();

	}

	public void setTele() {
		currentView = 1;
		autoLayout.setVisibility(View.GONE);
		teleLayout.setVisibility(View.VISIBLE);
		endGameLayout.setVisibility(View.GONE);
		lastB.setText("Auto");
		nextB.setText("End Game");
	}

	public void setAuto() {
		currentView = 0;
		autoLayout.setVisibility(View.VISIBLE);
		teleLayout.setVisibility(View.GONE);
		endGameLayout.setVisibility(View.GONE);
		lastB.setText("Cancel");
		nextB.setText("Tele op");
	}

	public void setEnd() {
		currentView = 2;
		autoLayout.setVisibility(View.GONE);
		teleLayout.setVisibility(View.GONE);
		endGameLayout.setVisibility(View.VISIBLE);
		lastB.setText("Tele op");
		nextB.setText("Submit");
	}

	public void submit() {
		submitter.submitMatch(teamData);
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
		case LOAD_DIALOG:
			builder.setMessage("Data for this match Exists.\nLoad old match?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									if (teamText.getText().toString().length() > 0
											&& matchT.getText().toString()
													.length() > 0) {
										teamData = new MatchStatsRR(
												Integer.valueOf(teamText
														.getText().toString()),
												Prefs.getEvent(
														getApplicationContext(),
														"Chesapeake Regional"),
												Integer.valueOf(matchT
														.getText().toString()
														.length()));
									} else
										teamData = new MatchStatsRR();

									loadAuto();
									loadTele();
									loadEndgame();
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
		case R.id.commonNotes:
			notes = teamNotes;
			break;
		default:
			notes = teamNotes;
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
	
	private class positionClickListener implements OnClickListener {

		public void onClick(View v) {
			MainMenuSelection.openSettings(MatchActivity.this);
		}

	};
	
	public class NotesSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View v, int position,
				long id) {
			if (position == 0 || !(parent instanceof Spinner))
				return;
			Spinner par = (Spinner) parent;
			String note = teamNotes.getText().toString();
			if (!note.contains(par.getItemAtPosition(position).toString())) {
				if (!note.trim().equals(""))
					note = note + "; ";
				note = note + par.getItemAtPosition(position);
				teamNotes.setText(note);
			}
			par.setSelection(0);

		}

		public void onNothingSelected(AdapterView<?> parent) {
		}

	};

	public void onNothingSelected(AdapterView<?> arg0) {
		// do nothing
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
