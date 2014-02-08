/*
 * Copyright 2014 Daniel Logan, Matthew Berkin
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
import java.util.Map;

import org.frc836.database.DB;
import org.frc836.database.FRCScoutingContract;
import org.frc836.database.XMLDBParser;
import org.growingstems.scouting.*;
import org.sigmond.net.HttpCallback;
import org.sigmond.net.HttpRequestInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PitsActivity extends Activity {

	private String HELPMESSAGE;

	private DB submitter;
	private PitStatsAA stats;

	private EditText teamT;
	private Spinner configS;
	private Spinner drivetrainS;
	private Spinner wheeltypeS;
	// private Spinner cgS;
	
	private CheckBox auto_highC;
	private CheckBox auto_lowC;
	private CheckBox auto_hotC;
	private CheckBox auto_mobileC;
	private CheckBox trussC;
	private CheckBox launchC;
	private CheckBox active_controlC;
	private CheckBox catchC;
	private CheckBox score_highC;
	private CheckBox score_lowC;
	// private EditText weightT;
	private EditText commentsT;
	private Button submitB;
	private TextView teamInfoT;
	private EditText heightT;

	private Handler timer = new Handler();
	private static final int DELAY = 500;

	private static final int CANCEL_DIALOG = 0;

	private List<TeamNumTask> tasks = new ArrayList<TeamNumTask>(3);

	private ProgressDialog pd;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pits);

		HELPMESSAGE = "Enter requested information about each team.\n\n"
				+ "When a team number is entered, the last time that data was collected about this team will be shown.\n"
				+ "If the date shown is during the current event, data does not need to be collected.";

		submitter = new DB(getBaseContext());
		stats = new PitStatsAA();
		teamT = (EditText) findViewById(R.id.pits_teamT);
		configS = (Spinner) findViewById(R.id.pits_configS);
		drivetrainS = (Spinner) findViewById(R.id.pits_drivetrainS);
		wheeltypeS = (Spinner) findViewById(R.id.pits_wheeltypeS);
		// cgS = (Spinner) findViewById(R.id.pits_cgS);
		
		auto_highC = (CheckBox) findViewById(R.id.pits_auto_highC);
		auto_lowC = (CheckBox) findViewById(R.id.pits_auto_lowC);
		auto_hotC = (CheckBox) findViewById(R.id.pits_auto_hotC);
		auto_mobileC = (CheckBox) findViewById(R.id.pits_auto_mobileC);
		trussC = (CheckBox) findViewById(R.id.pits_trussC);
		launchC = (CheckBox) findViewById(R.id.pits_launchC);
		active_controlC = (CheckBox) findViewById(R.id.pits_active_controlC);
		catchC = (CheckBox) findViewById(R.id.pits_catchC);
		score_highC = (CheckBox) findViewById(R.id.pits_highC);
		score_lowC = (CheckBox) findViewById(R.id.pits_lowC);
		
		// weightT = (EditText) findViewById(R.id.pits_weightT);
		commentsT = (EditText) findViewById(R.id.pits_commentsT);
		submitB = (Button) findViewById(R.id.pits_submitB);
		teamInfoT = (TextView) findViewById(R.id.pits_teamInfo);
		heightT = (EditText) findViewById(R.id.pits_heightT);

		teamT.addTextChangedListener(new teamTextListener());

		submitB.setOnClickListener(new SubmitListener());

	}

	public void onResume() {
		super.onResume();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		String pass = prefs.getString("passPref", "");
		submitter.setPass(pass);
		for (TeamNumTask task : tasks) {
			timer.removeCallbacks(task);
		}
		tasks.clear();
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

	public void onBackPressed() {
		showDialog(CANCEL_DIALOG);
	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case CANCEL_DIALOG:
			builder.setMessage("Cancel Data Entry?\nChanges will not be saved.")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									PitsActivity.this.finish();
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

	private class SubmitListener implements OnClickListener {

		public void onClick(View v) {
			submit();

		}

	}
	
	protected void submit() {
		// TODO add options for "Other"
		stats.auto_score_high = auto_highC.isChecked();
		stats.auto_score_low = auto_lowC.isChecked();
		stats.auto_score_hot = auto_hotC.isChecked();
		stats.auto_score_mobile = auto_mobileC.isChecked();
		stats.truss = trussC.isChecked();
		stats.launch = launchC.isChecked();
		stats.active_control = active_controlC.isChecked();
		stats.catchBall = catchC.isChecked();
		// stats.cg = cgS.getSelectedItem().toString();
		stats.comments = commentsT.getText().toString();
		stats.chassis_config = configS.getSelectedItem().toString();
		stats.wheel_base = drivetrainS.getSelectedItem().toString();
		stats.score_low = score_lowC.isChecked();
		if (teamT.getText().toString().trim().length() > 0)
			stats.team = Integer.valueOf(teamT.getText().toString().trim());
		stats.score_high = score_highC.isChecked();
		// if (weightT.getText().toString().trim().length() > 0)
		// stats.weight = Double.valueOf(weightT.getText().toString());
		stats.wheel_type = wheeltypeS.getSelectedItem().toString();
		if (heightT.getText().toString().trim().length() > 0)
			stats.height = Integer.valueOf(heightT.getText().toString().trim());
		else
			stats.height = 0;

		pd = ProgressDialog.show(this, "Busy", "Submitting", false);
		pd.setCancelable(true);

		submitter.submitPits(stats, new PitsCallback());
		submitB.setEnabled(false);

	}

	private class PitsCallback implements HttpCallback {

		public void onResponse(HttpRequestInfo resp) {
			pd.dismiss();

			String r = resp.getResponseString();

			if (r.contains("success")) {
				Toast toast = Toast.makeText(getBaseContext(),
						"Submitted Successfully", Toast.LENGTH_SHORT);
				toast.show();
				clear();
			} else if (resp.getResponse().getStatusLine().toString()
					.contains("403")) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Authentication Error.", Toast.LENGTH_SHORT);
				toast.show();
			} else if (r.contains("Failed") || r.contains("invalid")) {
				Toast toast = Toast.makeText(getApplicationContext(), r.trim(),
						Toast.LENGTH_SHORT);
				toast.show();
			} else {
				Toast toast = Toast.makeText(getApplicationContext(), resp
						.getResponse().getStatusLine().toString(),
						Toast.LENGTH_SHORT);
				toast.show();
			}

			submitB.setEnabled(true);

		}

		public void onError(Exception e) {
			pd.dismiss();
			Toast toast = Toast
					.makeText(
							getBaseContext(),
							"Error Submitting Data.\nEnsure you have internet connectivity",
							Toast.LENGTH_SHORT);
			toast.show();
			submitB.setEnabled(true);

		}

	}

	protected void clear() {
		teamT.setText("");
		clearData();
	}
	
	
	protected void clearData() {
		// TODO add options for "Other"
		teamInfoT.setText("");
		auto_highC.setChecked(false);
		auto_lowC.setChecked(false);
		auto_hotC.setChecked(false);
		auto_mobileC.setChecked(false);
		// cgS.setSelection(0);
		commentsT.setText("");
		configS.setSelection(0);
		drivetrainS.setSelection(0);
		trussC.setChecked(false);
		launchC.setChecked(false);
		active_controlC.setChecked(false);
		catchC.setChecked(false);
		score_lowC.setChecked(false);
		score_highC.setChecked(false);
		// weightT.setText("");
		wheeltypeS.setSelection(0);
		teamT.requestFocus();
		submitB.setEnabled(true);
		heightT.setText("");
	}

	private class teamTextListener implements TextWatcher {

		public void afterTextChanged(Editable s) {
			if (s.length() > 0) {
				TeamNumTask task = new TeamNumTask();
				tasks.add(task);
				task.teamNum = Integer.valueOf(s.toString());
				timer.postDelayed(task, DELAY);
			} else {
				clearData();
			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// Auto-generated method stub

		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// Auto-generated method stub

		}

	}

	private void setTeam(int teamNum) {
		submitter.getTeamPitInfo(String.valueOf(teamNum), new teamNumCallback(
				teamNum));
	}

	private class teamNumCallback implements HttpCallback {

		int team;

		public teamNumCallback(int teamNum) {
			team = teamNum;
		}

		public void onResponse(HttpRequestInfo resp) {
			if (resp.getResponse().getStatusLine().toString().contains("200")) {
				try {
					String r = resp.getResponseString();
					if (!r.contains("no info") && !(r.trim().length() == 0)) {
						teamInfoT.setText("Last Updated: " + r.trim());
						getTeamStats(team);
					} else {
						teamInfoT.setText("");
						clearData();
					}
				} catch (Exception e) {
					teamInfoT.setText("");
					clearData();
				}
			} else {
				teamInfoT.setText("");
				clearData();
			}
		}

		public void onError(Exception e) {
			teamInfoT.setText("");
			clearData();
		}

	}

	private class TeamNumTask implements Runnable {

		int teamNum;

		public void run() {
			if (teamT.getText().length() > 0 && Integer.valueOf(teamT.getText().toString()) == teamNum) {
				setTeam(teamNum);
			}
		}
	}

	private void getTeamStats(int teamNum) {
		pd = ProgressDialog.show(this, "Busy", "Retrieving Team Pit Data",
				false);
		pd.setCancelable(false);
		submitter.getTeamPitStats(teamNum, new TeamPitStatsCallback());
	}

	private class TeamPitStatsCallback implements HttpCallback {

		public void onResponse(HttpRequestInfo resp) {
			try {
				String XML = resp.getResponseString();
				String[] split = XML.split("\\<\\/result\\>");
				String pits = "";
				for (int i = 0; i < split.length; i++) {
					if (split[i].trim().length() > 0) {
						split[i] = (split[i] + "</result>\n").trim();
						int j = split[i].indexOf("<result");
						j = split[i].indexOf("table", j + 1);
						j = split[i].indexOf("=", j + 1);
						j = split[i].indexOf("\"", j + 1);
						int k = split[i].indexOf("\"", j + 1);
						String table = split[i].substring(j + 1, k);

						if (table.trim().compareToIgnoreCase("scout_pit_data") == 0) {
							pits = split[i];
							if (i > 0)
								pits = "<?xml version=\"1.0\"?>\n" + pits;
							break;
						}
					}
				}

				List<Map<String, String>> rows = XMLDBParser.extractRows(null,
						null, pits);
				if (rows.size() > 0) {
					populateData(rows.get(0));
				}
			} catch (Exception e) {
				onError(e);
			}
			pd.dismiss();
		}

		public void onError(Exception e) {
			pd.dismiss();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void populateData(Map<String, String> row) {
		int index = ((ArrayAdapter) configS.getAdapter()).getPosition(row
				.get("configuration_id"));
		configS.setSelection(index);
		index = ((ArrayAdapter) drivetrainS.getAdapter()).getPosition(row
				.get("wheel_base_id"));
		drivetrainS.setSelection(index);
		index = ((ArrayAdapter) wheeltypeS.getAdapter()).getPosition(row
				.get("wheel_type_id"));
		wheeltypeS.setSelection(index);
		auto_highC.setChecked(row.get(FRCScoutingContract.SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTO_HIGH).compareTo("1") == 0);
		auto_lowC.setChecked(row.get(FRCScoutingContract.SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTO_LOW).compareTo("1") == 0);
		auto_hotC.setChecked(row.get(FRCScoutingContract.SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTO_HOT).compareTo("1") == 0);
		auto_mobileC.setChecked(row.get(FRCScoutingContract.SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTO_MOBILE).compareTo("1") == 0);
		trussC.setChecked(row.get(FRCScoutingContract.SCOUT_PIT_DATA_Entry.COLUMN_NAME_TRUSS).compareTo("1") == 0);
		launchC.setChecked(row.get(FRCScoutingContract.SCOUT_PIT_DATA_Entry.COLUMN_NAME_LAUNCH_BALL).compareTo("1") == 0);
		active_controlC.setChecked(row.get(FRCScoutingContract.SCOUT_PIT_DATA_Entry.COLUMN_NAME_ACTIVE_CONTROL).compareTo("1") == 0);
		catchC.setChecked(row.get(FRCScoutingContract.SCOUT_PIT_DATA_Entry.COLUMN_NAME_CATCH).compareTo("1") == 0);
		score_highC.setChecked(row.get(FRCScoutingContract.SCOUT_PIT_DATA_Entry.COLUMN_NAME_SCORE_HIGH).compareTo("1") == 0);
		score_lowC.setChecked(row.get(FRCScoutingContract.SCOUT_PIT_DATA_Entry.COLUMN_NAME_SCORE_LOW).compareTo("1") == 0);
		heightT.setText(row.get("max_height"));
		commentsT.setText(row.get(FRCScoutingContract.SCOUT_PIT_DATA_Entry.COLUMN_NAME_SCOUT_COMMENTS));
	}

}