/*
 * Copyright 2013 Daniel Logan
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

package org.frc836.ultimateascent;

import java.util.List;

import org.frc836.database.DB;
import org.growingstems.scouting.MainMenuSelection;
import org.growingstems.scouting.MatchSchedule;
import org.growingstems.scouting.ParamList;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;
import org.sigmond.net.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity for use during matches. Includes all functionality necessary for
 * entering data during matches.
 * 
 * @author Daniel Logan
 * 
 */

public class MatchActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	private String HELPMESSAGE;

	private MatchStatsStructUA auto;
	private MatchStatsStructUA tele;
	private DB submitter;
	private MatchCallback callback = new MatchCallback();

	private static final int CANCEL_DIALOG = 0;
	private static final int SUBMIT_DIALOG = 1;
	private static final int TIME_DIALOG = 2;

	private boolean autoMode;
	private boolean end;

	private EditText teamT;
	private TextView posT;
	private EditText matchT;
	private TextView autoT;

	private Button backB;
	private Button nextB;

	private CheckBox penaltyC;
	private CheckBox mpenaltyC;
	private CheckBox yellowC;
	private CheckBox redC;

	private EditText notes;

	private LinearLayout pyramidGoalLayout;
	private LinearLayout highGoalLayout;
	private LinearLayout midLGoalLayout;
	private LinearLayout midRGoalLayout;
	private LinearLayout lowGoalLayout;

	private LinearLayout pyramidClimbLayout;

	private LinearLayout matchLayout;
	private LinearLayout endGameLayout;

	private Button addHighB;
	private Spinner highS;
	private Button missHighB;
	private Spinner missHighS;

	private Button addMidLB;
	private Spinner midLS;
	private Button missMidLB;
	private Spinner missMidLS;

	private Button addMidRB;
	private Spinner midRS;
	private Button missMidRB;
	private Spinner missMidRS;

	private Button addLowB;
	private Spinner lowS;
	private Button missLowB;
	private Spinner missLowS;

	private Button addPyramidB;
	private Spinner pyramidS;
	private Button missPyramidB;
	private Spinner missPyramidS;

	private CheckBox level3C;
	private CheckBox level2C;
	private CheckBox level1C;
	private CheckBox pyramidAttemptC;

	private CheckBox tippedC;

	private Spinner commonNotesS;

	private Handler timer = new Handler();
	private static final int DELAY = 16000;

	private Handler dispTimer = new Handler();
	private static final int DISPDEL = 50;

	private ParamList notesOptions;

	private int orientation;

	private ProgressDialog pd;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.match);

		orientation = getResources().getConfiguration().orientation;

		notesOptions = new ParamList(getApplicationContext(), "notes_options");

		HELPMESSAGE = "Record the number of discs scored (and missed) in each goal, and the pyramid level climbed\n\n"
				+ getString(R.string.score_plus)
				+ ": Score\n"
				+ getString(R.string.miss)
				+ ": Miss\n"
				+ getString(R.string.penalty)
				+ ": Penalty\n"
				+ getString(R.string.major_penalty)
				+ ": Major Penalty\n"
				+ getString(R.string.yellow_card)
				+ ": Yellow Card\n"
				+ getString(R.string.red_card) + ": Red Card";

		Intent intent = getIntent(); // retrieves the intent used to create this
										// Activity
		String team = intent.getStringExtra("team"); // retrieves team number
		String match = intent.getStringExtra("match"); // retrieves match number
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext()); // get
																// preferences
																// saved for
																// this app.

		String pos = prefs.getString("positionPref", "red 1");
		String event = prefs.getString("eventPref", "Chesapeake Regional");
		String pass = prefs.getString("passPref", "");
		submitter = new DB(getBaseContext(), pass); // create the new Database
													// interface

		if (team != null && match != null && team.length() > 0
				&& match.length() > 0) {
			auto = new MatchStatsStructUA(Integer.valueOf(team), event,
					Integer.valueOf(match), true);
			tele = new MatchStatsStructUA(Integer.valueOf(team), event,
					Integer.valueOf(match), false);
		} else {
			auto = new MatchStatsStructUA();
			auto.autonomous = true;
			tele = new MatchStatsStructUA();
			tele.autonomous = false;
		}

		teamT = (EditText) findViewById(R.id.teamNum);
		teamT.setText(team);
		posT = (TextView) findViewById(R.id.pos);
		posT.setText(pos);
		if (pos.contains("blue"))
			posT.setTextColor(Color.BLUE);
		else
			posT.setTextColor(Color.RED);

		posT.setOnClickListener(new positionClickListener());
		matchT = (EditText) findViewById(R.id.matchNum);
		matchT.setText(match);

		autoT = (TextView) findViewById(R.id.autoT);
		autoT.setText("Autonomous");

		backB = (Button) findViewById(R.id.backB);
		backB.setOnClickListener(this);
		backB.setText("Cancel");
		nextB = (Button) findViewById(R.id.nextB);
		nextB.setOnClickListener(this);
		nextB.setText("Tele-Op");

		penaltyC = (CheckBox) findViewById(R.id.foul);
		mpenaltyC = (CheckBox) findViewById(R.id.techfoul);
		yellowC = (CheckBox) findViewById(R.id.yellowCard);
		redC = (CheckBox) findViewById(R.id.redCard);

		notes = (EditText) findViewById(R.id.notes);

		pyramidGoalLayout = (LinearLayout) findViewById(R.id.pyramidGoalLayout);
		highGoalLayout = (LinearLayout) findViewById(R.id.highGoalBox);
		midLGoalLayout = (LinearLayout) findViewById(R.id.midlGoalBox);
		midRGoalLayout = (LinearLayout) findViewById(R.id.midrGoalBox);
		lowGoalLayout = (LinearLayout) findViewById(R.id.lowGoalBox);
		matchLayout = (LinearLayout) findViewById(R.id.matchLayout);
		endGameLayout = (LinearLayout) findViewById(R.id.endGameLayout);
		pyramidClimbLayout = (LinearLayout) findViewById(R.id.pyramidClimbLayout);

		addHighB = (Button) findViewById(R.id.addhigh);
		addHighB.setOnClickListener(this);
		highS = (Spinner) findViewById(R.id.highcount);
		missHighB = (Button) findViewById(R.id.addhighmiss);
		missHighB.setOnClickListener(this);
		missHighS = (Spinner) findViewById(R.id.highcountmiss);

		addMidLB = (Button) findViewById(R.id.addmidl);
		addMidLB.setOnClickListener(this);
		midLS = (Spinner) findViewById(R.id.midlcount);
		missMidLB = (Button) findViewById(R.id.addmidlmiss);
		missMidLB.setOnClickListener(this);
		missMidLS = (Spinner) findViewById(R.id.midlcountmiss);

		addMidRB = (Button) findViewById(R.id.addmidr);
		addMidRB.setOnClickListener(this);
		midRS = (Spinner) findViewById(R.id.midrcount);
		missMidRB = (Button) findViewById(R.id.addmidrmiss);
		missMidRB.setOnClickListener(this);
		missMidRS = (Spinner) findViewById(R.id.midrcountmiss);

		addLowB = (Button) findViewById(R.id.addlow);
		addLowB.setOnClickListener(this);
		lowS = (Spinner) findViewById(R.id.lowcount);
		missLowB = (Button) findViewById(R.id.addlowmiss);
		missLowB.setOnClickListener(this);
		missLowS = (Spinner) findViewById(R.id.lowcountmiss);

		addPyramidB = (Button) findViewById(R.id.addpyramid);
		addPyramidB.setOnClickListener(this);
		pyramidS = (Spinner) findViewById(R.id.pyramidcount);
		missPyramidB = (Button) findViewById(R.id.addpyramidmiss);
		missPyramidB.setOnClickListener(this);
		missPyramidS = (Spinner) findViewById(R.id.pyramidcountmiss);

		level3C = (CheckBox) findViewById(R.id.pyramid3C);
		level3C.setOnCheckedChangeListener(this);
		level2C = (CheckBox) findViewById(R.id.pyramid2C);
		level2C.setOnCheckedChangeListener(this);
		level1C = (CheckBox) findViewById(R.id.pyramid1C);
		level1C.setOnCheckedChangeListener(this);
		pyramidAttemptC = (CheckBox) findViewById(R.id.pyramidAttemptC);
		pyramidAttemptC.setOnCheckedChangeListener(this);

		tippedC = (CheckBox) findViewById(R.id.tipped);

		commonNotesS = (Spinner) findViewById(R.id.common_notes);
		commonNotesS.setOnItemSelectedListener(new NotesSelectedListener());

		setAuto();

	}

	public void onResume() {
		super.onResume();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		String pos = prefs.getString("positionPref", "red 1");
		String event = prefs.getString("eventPref", "Chesapeake Regional");
		String pass = prefs.getString("passPref", "");
		submitter.setPass(pass);

		posT.setText(pos);
		if (pos.contains("blue"))
			posT.setTextColor(Color.BLUE);
		else
			posT.setTextColor(Color.RED);

		tele.event = event;
		auto.event = event;

		dispTimer.removeCallbacks(mUpdatePyramidTask);
		dispTimer.postDelayed(mUpdatePyramidTask, DISPDEL);

		if (autoMode) {
			timer.postDelayed(mUpdateTimeTask, DELAY);
		}

		List<String> options = notesOptions.getParamList("option_text");

		options.add(0, commonNotesS.getItemAtPosition(0).toString());

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, options);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		commonNotesS.setAdapter(adapter);

	}

	public void onPause() {
		super.onPause();
		timer.removeCallbacks(mUpdateTimeTask);
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
		last();
		return;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Prefs.PREFS_ACTIVITY_CODE) {
			MatchSchedule schedule = new MatchSchedule();
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			schedule.updateSchedule(
					prefs.getString("eventPref", "Chesapeake Regional"), this,
					false);
		}
	}

	private class MatchCallback implements HttpCallback {

		public void onResponse(HttpRequestInfo resp) {
			pd.dismiss();
			try {
				String r = resp.getResponseString();

				if (r.contains("success")) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Submitted Successfully", Toast.LENGTH_SHORT);
					toast.show();
					if (matchT.getText().length() > 0)
						setResult(Integer.valueOf(matchT.getText().toString()) + 1);
					finish();
				} else if (resp.getResponse().getStatusLine().toString()
						.contains("403")) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Authentication Error.", Toast.LENGTH_SHORT);
					toast.show();
				} else if (r.contains("Failed") || r.contains("invalid")) {
					Toast toast = Toast.makeText(getApplicationContext(),
							r.trim(), Toast.LENGTH_SHORT);
					toast.show();
				} else {
					Toast toast = Toast.makeText(getApplicationContext(), resp
							.getResponse().getStatusLine().toString(),
							Toast.LENGTH_SHORT);
					toast.show();
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"Error reading response from server",
						Toast.LENGTH_SHORT).show();
			}
			nextB.setEnabled(true);

		}

		public void onError(Exception e) {
			pd.dismiss();
			Toast toast = Toast
					.makeText(
							getBaseContext(),
							"Error Submitting Data.\nEnsure you have internet connectivity",
							Toast.LENGTH_LONG);
			toast.show();
			nextB.setEnabled(true);
		}
	}

	public void onClick(View v) {

		if (v.equals(backB))
			last();
		else if (v.equals(nextB))
			next();
		else if (v.equals(addHighB) || v.equals(addMidLB) || v.equals(addMidRB)
				|| v.equals(addLowB) || v.equals(addPyramidB)
				|| v.equals(missHighB) || v.equals(missMidLB)
				|| v.equals(missMidRB) || v.equals(missLowB)
				|| v.equals(missPyramidB))
			addScore(v.getId());
	}

	private void next() {
		if (autoMode) {
			saveAuto();
			setTele();
		} else if (!end) {
			saveTele();
			setEnd();
		} else {
			saveEnd();
			showDialog(SUBMIT_DIALOG);
		}
	}

	public void submit() {
		pd = ProgressDialog.show(this, "Busy", "Submitting", false);
		pd.setCancelable(true);
		submitter.submitMatch(auto, tele, callback);
		nextB.setEnabled(false);
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
		case SUBMIT_DIALOG:
			builder.setMessage("Submit Match?")
					.setCancelable(true)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									submit();
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
		case TIME_DIALOG:
			builder.setMessage("Continue to Tele-Op?")
					.setCancelable(true)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									next();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									timer.removeCallbacks(mUpdateTimeTask);
									timer.postDelayed(mUpdateTimeTask, DELAY);
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

	private void last() {
		if (autoMode) {
			showDialog(CANCEL_DIALOG);
		} else if (!end) {
			saveTele();
			setAuto();
		} else {
			saveEnd();
			setTele();
		}
	}

	private void saveAuto() {
		if (teamT.getText().toString().length() > 0)
			tele.team = Integer.valueOf(teamT.getText().toString());
		if (matchT.getText().toString().length() > 0)
			tele.match = Integer.valueOf(matchT.getText().toString());
		auto.high = highS.getSelectedItemPosition();
		auto.highAtt = missHighS.getSelectedItemPosition() + auto.high;
		auto.midL = midLS.getSelectedItemPosition();
		auto.midLAtt = missMidLS.getSelectedItemPosition() + auto.midL;
		auto.midR = midRS.getSelectedItemPosition();
		auto.midRAtt = missMidRS.getSelectedItemPosition() + auto.midR;
		auto.low = lowS.getSelectedItemPosition();
		auto.lowAtt = missLowS.getSelectedItemPosition() + auto.low;
		tele.penalty = penaltyC.isChecked();
		tele.mpenalty = mpenaltyC.isChecked();
		tele.yellowCard = yellowC.isChecked();
		tele.redCard = redC.isChecked();
	}

	private void saveTele() {
		if (teamT.getText().toString().length() > 0)
			tele.team = Integer.valueOf(teamT.getText().toString());
		if (matchT.getText().toString().length() > 0)
			tele.match = Integer.valueOf(matchT.getText().toString());
		tele.high = highS.getSelectedItemPosition();
		tele.highAtt = missHighS.getSelectedItemPosition() + tele.high;
		tele.midL = midLS.getSelectedItemPosition();
		tele.midLAtt = missMidLS.getSelectedItemPosition() + tele.midL;
		tele.midR = midRS.getSelectedItemPosition();
		tele.midRAtt = missMidRS.getSelectedItemPosition() + tele.midR;
		tele.low = lowS.getSelectedItemPosition();
		tele.lowAtt = missLowS.getSelectedItemPosition() + tele.low;
		tele.pyramidG = pyramidS.getSelectedItemPosition();
		tele.pyramidGAtt = missPyramidS.getSelectedItemPosition()
				+ tele.pyramidG;
		tele.penalty = penaltyC.isChecked();
		tele.mpenalty = mpenaltyC.isChecked();
		tele.yellowCard = yellowC.isChecked();
		tele.redCard = redC.isChecked();
	}

	private void saveEnd() {
		if (teamT.getText().toString().length() > 0)
			tele.team = Integer.valueOf(teamT.getText().toString());
		if (matchT.getText().toString().length() > 0)
			tele.match = Integer.valueOf(matchT.getText().toString());

		tele.penalty = penaltyC.isChecked();
		tele.mpenalty = mpenaltyC.isChecked();
		tele.yellowCard = yellowC.isChecked();
		tele.redCard = redC.isChecked();

		tele.notes = notes.getText().toString();
		tele.tipOver = tippedC.isChecked();
		int level = 0;
		if (level1C.isChecked())
			level = 1;
		else if (level2C.isChecked())
			level = 2;
		else if (level3C.isChecked())
			level = 3;
		tele.pyramidLevel = level;
		tele.pyramidLevelAtt = pyramidAttemptC.isChecked() ? 1 : 0;
	}

	private void setAuto() {

		timer.removeCallbacks(mUpdateTimeTask);
		timer.postDelayed(mUpdateTimeTask, DELAY);
		autoMode = true;
		end = false;
		pyramidGoalLayout.setVisibility(View.INVISIBLE);
		matchLayout.setVisibility(View.VISIBLE);
		autoT.setText("Autonomous");
		endGameLayout.setVisibility(View.GONE);

		backB.setText("Cancel");
		nextB.setText("Tele-Op");

		highS.setSelection(auto.high);
		missHighS.setSelection(auto.highAtt - auto.high);
		lowS.setSelection(auto.low);
		missLowS.setSelection(auto.lowAtt - auto.low);
		midRS.setSelection(auto.midR);
		missMidRS.setSelection(auto.midRAtt - auto.midR);
		midLS.setSelection(auto.midL);
		missMidLS.setSelection(auto.midLAtt - auto.midL);

	}

	private void setTele() {
		timer.removeCallbacks(mUpdateTimeTask);
		autoMode = false;
		end = false;
		matchLayout.setVisibility(View.VISIBLE);
		pyramidGoalLayout.setVisibility(View.VISIBLE);
		autoT.setText("Tele-Op");
		endGameLayout.setVisibility(View.GONE);

		backB.setText("Autonomous");
		nextB.setText("End-Game");

		highS.setSelection(tele.high);
		missHighS.setSelection(tele.highAtt - tele.high);
		lowS.setSelection(tele.low);
		missLowS.setSelection(tele.lowAtt - tele.low);
		midRS.setSelection(tele.midR);
		missMidRS.setSelection(tele.midRAtt - tele.midR);
		midLS.setSelection(tele.midL);
		missMidLS.setSelection(tele.midLAtt - tele.midL);
		pyramidS.setSelection(tele.pyramidG);
		missPyramidS.setSelection(tele.pyramidGAtt - tele.pyramidG);
	}

	private void setEnd() {
		timer.removeCallbacks(mUpdateTimeTask);
		autoMode = false;
		end = true;
		matchLayout.setVisibility(View.GONE);
		autoT.setText("End-Game");
		endGameLayout.setVisibility(View.VISIBLE);
		backB.setText("Tele-Op");
		nextB.setText("Submit");
	}

	private void addScore(int buttonID) {
		Spinner spin;

		switch (buttonID) {
		case R.id.addhigh:
			spin = highS;
			break;
		case R.id.addhighmiss:
			spin = missHighS;
			break;
		case R.id.addmidl:
			spin = midLS;
			break;
		case R.id.addmidlmiss:
			spin = missMidLS;
			break;
		case R.id.addmidr:
			spin = midRS;
			break;
		case R.id.addmidrmiss:
			spin = missMidRS;
			break;
		case R.id.addlow:
			spin = lowS;
			break;
		case R.id.addlowmiss:
			spin = missLowS;
			break;
		case R.id.addpyramid:
			spin = pyramidS;
			break;
		case R.id.addpyramidmiss:
			spin = missPyramidS;
			break;
		default:
			return;
		}
		if (spin.getSelectedItemPosition() < (spin.getCount() - 1))
			spin.setSelection(spin.getSelectedItemPosition() + 1);
	}

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			showDialog(TIME_DIALOG);
		}
	};

	private Runnable mUpdatePyramidTask = new Runnable() {
		public void run() {

			int w = midLGoalLayout.getWidth();
			int h = lowGoalLayout.getHeight();
			int climbH = matchLayout.getHeight() - (matchT.getHeight() * 3);
			int climbW = matchLayout.getWidth() * 10 / 16;

			if (w <= 0 || h <= 0 || climbH <= 0 || climbW <= 0) {
				dispTimer.removeCallbacks(mUpdatePyramidTask);
				dispTimer.postDelayed(mUpdatePyramidTask, DISPDEL);
				return;
			}

			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			String pos = prefs.getString("positionPref", "red 1");
			Bitmap bmap;
			int goalresid;
			int pyramidresid;
			if (pos.contains("blue")) {
				pyramidresid = R.drawable.bluepyramid;
				goalresid = R.drawable.blueborder;
			} else {
				pyramidresid = R.drawable.redpyramid;
				goalresid = R.drawable.redborder;
			}

			bmap = BitmapFactory.decodeResource(getResources(), pyramidresid);
			// int width = getWindowManager().getDefaultDisplay().getWidth() * 6
			// / 13;
			// int climbWidth = (int) (getWindowManager().getDefaultDisplay()
			// .getWidth() / 1.6);

			BitmapDrawable bmap2 = new BitmapDrawable(
					Bitmap.createScaledBitmap(bmap, w, h * 4 / 3, false));
			pyramidGoalLayout.setBackgroundDrawable(bmap2);

			// matchT is a good approximation. actual elements are
			// visibility=GONE, so they have a height of 0

			BitmapDrawable bmap3 = new BitmapDrawable(
					Bitmap.createScaledBitmap(bmap, climbW, climbH, false));

			highGoalLayout.setBackgroundResource(goalresid);
			midLGoalLayout.setBackgroundResource(goalresid);
			midRGoalLayout.setBackgroundResource(goalresid);
			lowGoalLayout.setBackgroundResource(goalresid);

			pyramidClimbLayout.setBackgroundDrawable(bmap3);

		}
	};

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			if (!buttonView.equals(level1C))
				level1C.setChecked(false);
			if (!buttonView.equals(level2C))
				level2C.setChecked(false);
			if (!buttonView.equals(level3C))
				level3C.setChecked(false);
			if (!buttonView.equals(pyramidAttemptC))
				pyramidAttemptC.setChecked(false);
		}
	}

	private class positionClickListener implements OnClickListener {

		public void onClick(View v) {
			MainMenuSelection.openSettings(MatchActivity.this);
		}

	};

	private class NotesSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View v, int position,
				long id) {
			if (position == 0 || !(parent instanceof Spinner))
				return;
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

		public void onNothingSelected(AdapterView<?> parent) {
		}

	};

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation != orientation) {
			dispTimer.removeCallbacks(mUpdatePyramidTask);
			dispTimer.postDelayed(mUpdatePyramidTask, DISPDEL);
		}
	}
}