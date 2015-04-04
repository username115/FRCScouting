package org.robobees.recyclerush;

import java.util.ArrayList;
import java.util.List;

import org.frc836.database.DB;
import org.frc836.database.DBActivity;
import org.frc836.database.DBSyncService;
import org.frc836.database.PitStats;
import org.frc836.database.DBSyncService.LocalBinder;
import org.growingstems.scouting.*;

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
import android.os.Handler;
import android.os.IBinder;
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

public class PitActivityRR extends DBActivity {
	private String HELPMESSAGE;

	private PitStatsRR stats;

	private EditText teamT;
	private Spinner configS;
	private Spinner drivetrainS;
	private Spinner wheeltypeS;

	private EditText commentsT;
	private Button submitB;
	private TextView teamInfoT;

	// Litter Stats
	private CheckBox push_litterC;
	private CheckBox load_litterC;
	// Tote Stats
	private CheckBox push_toteC;
	private CheckBox lift_toteC;
	private CheckBox need_upright_toteC;
	private CheckBox can_upright_toteC;
	private EditText stack_tote_heightT;
	// Bin Stats
	private CheckBox push_binC;
	private CheckBox lift_binC;
	private CheckBox need_upright_binC;
	private CheckBox can_upright_binC;
	private EditText stack_bin_heightT;
	// Coop Stats
	private CheckBox coop_totesC;
	private EditText coop_stack_heightT;
	// Auto Stats
	private CheckBox move_autoC;
	private EditText auto_bin_scoreT;
	private EditText auto_tote_scoreT;
	private EditText auto_tote_stack_heightT;
	private EditText auto_step_binsT;

	private EditText manipulation_descriptionT;

	private Handler timer = new Handler();
	private static final int DELAY = 500;

	private static final int CANCEL_DIALOG = 0;
	private static final int NOTEAM_DIALOG = 24243;
	private static final int CLEAR_DATA_DIALOG = 23930;
	private static final int OVERWRITE_DATA_DIALOG = 59603;

	private List<TeamNumTask> tasks = new ArrayList<TeamNumTask>(3);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pits);

		HELPMESSAGE = "Enter the requested information about each team. \n\n"
				+ "When a team number is entered, the last time that data was "
				+ "collected about this team will be shown.\n"
				+ "If the date shown is during the current event, data does "
				+ "not need to be collected.";

		stats = new PitStatsRR();
		teamT = (EditText) findViewById(R.id.pits_teamT);
		configS = (Spinner) findViewById(R.id.pits_configS);
		drivetrainS = (Spinner) findViewById(R.id.pits_drivetrainS);
		wheeltypeS = (Spinner) findViewById(R.id.pits_wheeltypeS);

		push_litterC = (CheckBox) findViewById(R.id.push_litter);
		load_litterC = (CheckBox) findViewById(R.id.load_litter);

		push_toteC = (CheckBox) findViewById(R.id.push_tote);
		lift_toteC = (CheckBox) findViewById(R.id.lift_tote);
		need_upright_toteC = (CheckBox) findViewById(R.id.need_upright_tote);
		can_upright_toteC = (CheckBox) findViewById(R.id.can_upright_tote);
		stack_tote_heightT = (EditText) findViewById(R.id.stack_tote_height);

		push_binC = (CheckBox) findViewById(R.id.push_bin);
		lift_binC = (CheckBox) findViewById(R.id.lift_bin);
		need_upright_binC = (CheckBox) findViewById(R.id.need_upright_bin);
		can_upright_binC = (CheckBox) findViewById(R.id.can_upright_bin);
		stack_bin_heightT = (EditText) findViewById(R.id.stack_bin_height);

		coop_totesC = (CheckBox) findViewById(R.id.coop_totes);
		coop_stack_heightT = (EditText) findViewById(R.id.coop_stack_height);

		move_autoC = (CheckBox) findViewById(R.id.move_auto);
		auto_bin_scoreT = (EditText) findViewById(R.id.auto_bin_score);
		auto_tote_scoreT = (EditText) findViewById(R.id.auto_tote_score);
		auto_tote_stack_heightT = (EditText) findViewById(R.id.auto_tote_stack_height);
		auto_step_binsT = (EditText) findViewById(R.id.auto_step_bins);

		manipulation_descriptionT = (EditText) findViewById(R.id.manipulation_description);

		commentsT = (EditText) findViewById(R.id.pits_commentsT);
		submitB = (Button) findViewById(R.id.pits_submitB);
		teamInfoT = (TextView) findViewById(R.id.pits_teamInfo);

		teamT.addTextChangedListener(new teamTextListener());
		submitB.setOnClickListener(new SubmitListener());
	}

	public void onResume() {
		super.onResume();

		List<String> config = db.getConfigList();
		if (config != null) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, config);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			configS.setAdapter(adapter);
		}

		List<String> wheelBase = db.getConfigList();
		if (wheelBase != null) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, config);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			configS.setAdapter(adapter);
		}

		List<String> wheelType = db.getConfigList();
		if (wheelType != null) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, config);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			configS.setAdapter(adapter);
		}

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		String pass = prefs.getString("passPref", "");
		db.setPass(pass);
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
									PitActivityRR.this.finish();
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
		case NOTEAM_DIALOG:
			builder.setMessage(
					"No team number entered, please enter a team number")
					.setCancelable(true)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
									teamT.requestFocus();
								}
							});
			dialog = builder.create();
			break;
		case OVERWRITE_DATA_DIALOG:
			builder.setMessage(
					"Data for this team exists. Overwrite current form?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									teamInfoT.setText("Last Updated: "
											+ dateLoad.trim());
									getTeamStats(teamLoad);
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
		case CLEAR_DATA_DIALOG:
			builder.setMessage("You had already entered data. Clear form?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									teamInfoT.setText("");
									clearData();
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
		String tstr; // to avoid re-calc for string -> int check operations

		tstr = teamT.getText().toString().trim();
		if (tstr.length() > 0)
			stats.team = Integer.valueOf(tstr);
		else {
			showDialog(NOTEAM_DIALOG);
			return;
		}

		stats.wheel_type = wheeltypeS.getSelectedItem().toString();
		stats.chassis_config = configS.getSelectedItem().toString();
		stats.wheel_base = drivetrainS.getSelectedItem().toString();

		stats.comments = commentsT.getText().toString();

		stats.manipulation_description = manipulation_descriptionT.getText()
				.toString();

		// LITTER
		stats.push_litter = push_litterC.isChecked();
		stats.load_litter = load_litterC.isChecked();
		// TOTES
		stats.push_tote = push_toteC.isChecked();
		stats.lift_tote = lift_toteC.isChecked();
		stats.need_upright_tote = need_upright_toteC.isChecked();
		stats.can_upright_tote = can_upright_toteC.isChecked();

		tstr = stack_tote_heightT.getText().toString().trim(); // avoid re-calc
		if (tstr.length() > 0)
			stats.stack_tote_height = Short.valueOf(tstr);
		else
			stats.stack_tote_height = 0;

		// BINS
		stats.push_bin = push_binC.isChecked();
		stats.lift_bin = lift_binC.isChecked();
		stats.need_upright_bin = need_upright_binC.isChecked();
		stats.can_upright_bin = can_upright_binC.isChecked();

		tstr = stack_bin_heightT.getText().toString().trim();
		if (tstr.length() > 0)
			stats.stack_bin_height = Short.valueOf(tstr);
		else
			stats.stack_bin_height = 0;

		// COOP
		stats.coop_totes = coop_totesC.isChecked();

		tstr = coop_stack_heightT.getText().toString().trim();
		if (tstr.length() > 0)
			stats.coop_stack_height = Short.valueOf(tstr);
		else
			stats.coop_stack_height = 0;

		// AUTO
		stats.move_auto = move_autoC.isChecked();

		tstr = auto_bin_scoreT.getText().toString().trim();
		if (tstr.length() > 0)
			stats.auto_bin_score = Short.valueOf(tstr);
		else
			stats.auto_bin_score = 0;

		tstr = auto_tote_scoreT.getText().toString().trim();
		if (tstr.length() > 0)
			stats.auto_tote_score = Short.valueOf(tstr);
		else
			stats.auto_tote_score = 0;

		tstr = auto_tote_stack_heightT.getText().toString().trim();
		if (tstr.length() > 0)
			stats.auto_tote_stack_height = Short.valueOf(tstr);
		else
			stats.auto_tote_stack_height = 0;

		tstr = auto_step_binsT.getText().toString().trim();
		if (tstr.length() > 0)
			stats.auto_step_bins = Short.valueOf(tstr);
		else
			stats.auto_step_bins = 0;

		if (db.submitPits(stats))
			clear();
		else
			Toast.makeText(getApplicationContext(), "Error in local database",
					Toast.LENGTH_LONG).show();
	}

	protected void clear() {
		clearData();
		teamT.setText("");
	}

	protected void clearData() {
		teamInfoT.setText("");
		commentsT.setText("");

		configS.setSelection(0);
		drivetrainS.setSelection(0);
		wheeltypeS.setSelection(0);

		push_litterC.setChecked(false);
		load_litterC.setChecked(false);

		push_toteC.setChecked(false);
		lift_toteC.setChecked(false);
		need_upright_toteC.setChecked(false);
		can_upright_toteC.setChecked(false);
		stack_tote_heightT.setText("");

		push_binC.setChecked(false);
		lift_binC.setChecked(false);
		need_upright_binC.setChecked(false);
		can_upright_binC.setChecked(false);
		stack_bin_heightT.setText("");

		coop_totesC.setChecked(false);
		coop_stack_heightT.setText("");

		move_autoC.setChecked(false);
		auto_bin_scoreT.setText("");
		auto_tote_scoreT.setText("");
		auto_tote_stack_heightT.setText("");
		auto_step_binsT.setText("");

		manipulation_descriptionT.setText("");
	}

	private class teamTextListener implements TextWatcher {

		public void afterTextChanged(Editable s) {
			if (s.length() > 0) {
				TeamNumTask task = new TeamNumTask();
				tasks.add(task);
				task.teamNum = Integer.valueOf(s.toString());
				timer.postDelayed(task, DELAY);
			} else {
				if (!dataClear())
					showDialog(CLEAR_DATA_DIALOG);
				else
					clearData();
			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

	}

	private int teamLoad = 0;
	private String dateLoad = "";

	private void setTeam(int teamNum) {
		String date = db.getTeamPitInfo(String.valueOf(teamNum));
		if (date.length() > 0) {
			if (dataClear()) {
				teamInfoT.setText("Last Updated: " + date.trim());
				getTeamStats(teamNum);
			} else {
				teamLoad = teamNum;
				dateLoad = date;
				showDialog(OVERWRITE_DATA_DIALOG);
			}
		} else {
			if (dataClear()) {
				teamInfoT.setText("");
				clearData();
			} else
				showDialog(CLEAR_DATA_DIALOG);
		}
	}

	private class TeamNumTask implements Runnable {
		int teamNum;

		public void run() {
			if (teamT.getText().length() > 0
					&& Integer.valueOf(teamT.getText().toString()) == teamNum) {
				setTeam(teamNum);
			}
		}
	}

	private void getTeamStats(int teamNum) {
		PitStats s = db.getTeamPitStats(teamNum);
		if (s instanceof PitStatsRR)
			stats = (PitStatsRR) s;
		else
			return;

		populateData(stats);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void populateData(PitStatsRR stats) {
		int index = ((ArrayAdapter) configS.getAdapter())
				.getPosition(stats.chassis_config);
		configS.setSelection(index);

		index = ((ArrayAdapter) drivetrainS.getAdapter())
				.getPosition(stats.wheel_base);
		drivetrainS.setSelection(index);

		index = ((ArrayAdapter) wheeltypeS.getAdapter())
				.getPosition(stats.wheel_type);
		wheeltypeS.setSelection(index);

		commentsT.setText(stats.comments);

		push_litterC.setChecked(stats.push_litter);
		load_litterC.setChecked(stats.load_litter);

		push_toteC.setChecked(stats.push_tote);
		lift_toteC.setChecked(stats.lift_tote);
		need_upright_toteC.setChecked(stats.need_upright_tote);
		can_upright_toteC.setChecked(stats.can_upright_tote);
		stack_tote_heightT.setText(String.valueOf(stats.stack_tote_height));

		push_binC.setChecked(stats.push_bin);
		lift_binC.setChecked(stats.lift_bin);
		need_upright_binC.setChecked(stats.need_upright_bin);
		can_upright_binC.setChecked(stats.can_upright_bin);
		stack_bin_heightT.setText(String.valueOf(stats.stack_bin_height));

		coop_totesC.setChecked(stats.coop_totes);
		coop_stack_heightT.setText(String.valueOf(stats.coop_stack_height));

		move_autoC.setChecked(stats.move_auto);
		auto_bin_scoreT.setText(String.valueOf(stats.auto_bin_score));
		auto_tote_scoreT.setText(String.valueOf(stats.auto_tote_score));
		auto_tote_stack_heightT.setText(String
				.valueOf(stats.auto_tote_stack_height));
		auto_step_binsT.setText(String.valueOf(stats.auto_step_bins));

		manipulation_descriptionT.setText(stats.manipulation_description);
	}

	private boolean dataClear() {
		if (commentsT.getText().toString().length() > 0
				|| configS.getSelectedItemPosition() != 0
				|| drivetrainS.getSelectedItemPosition() != 0
				|| wheeltypeS.getSelectedItemPosition() != 0
				|| push_litterC.isChecked() || load_litterC.isChecked()
				|| push_toteC.isChecked() || lift_toteC.isChecked()
				|| need_upright_toteC.isChecked()
				|| can_upright_toteC.isChecked()
				|| stack_tote_heightT.getText().toString().length() > 0
				|| push_binC.isChecked() || lift_binC.isChecked()
				|| need_upright_binC.isChecked()
				|| can_upright_binC.isChecked()
				|| stack_bin_heightT.getText().toString().length() > 0
				|| coop_totesC.isChecked()
				|| coop_stack_heightT.getText().toString().length() > 0
				|| move_autoC.isChecked()
				|| auto_bin_scoreT.getText().toString().length() > 0
				|| auto_tote_scoreT.getText().toString().length() > 0
				|| auto_tote_stack_heightT.getText().toString().length() > 0
				|| auto_step_binsT.getText().toString().length() > 0
				|| manipulation_descriptionT.getText().toString().length() > 0)
			return false;
		return true;
	}

}
