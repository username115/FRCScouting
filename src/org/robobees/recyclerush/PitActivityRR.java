package org.robobees.recyclerush;

import java.util.ArrayList;
import java.util.List;

import org.frc836.database.DB;
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


public class PitActivityRR extends Activity {
	private String HELPMESSAGE;

	private DB submitter;
	private PitStatsRR stats;

	private EditText teamT;
	private Spinner configS;
	private Spinner drivetrainS;
	private Spinner wheeltypeS;

	private EditText commentsT;
	private Button submitB;
	private TextView teamInfoT;
	private EditText heightT;


	// Litter Stats
	private CheckBox pushLitterC;
	private CheckBox loadLitterC;
	// Tote Stats
	private CheckBox pushToteC;
	private CheckBox liftToteC;
	private CheckBox needUprightToteC;
	private CheckBox canUprightToteC;
	private EditText stackToteHeightT;
	// Bin Stats
	private CheckBox pushBinC;
	private CheckBox liftBinC;
	private CheckBox needUprightBinC;
	private CheckBox canUprightBinC;
	private EditText stackBinHeightT;
	// Coop Stats
	private EditText coopTotesT; // TODO check: should this be a bool?
	private EditText coopStackHeightT;
	// Auto Stats
	private CheckBox moveAutoC;
	private EditText autoBinScoreT;
	private EditText autoToteScoreT;
	private EditText autoToteStackHeightT;
	private EditText autoStepBinsT;

	private EditText manipulationDescriptionT;


	private LocalBinder binder;
	private ServiceWatcher watcher = new ServiceWatcher();

	private Handler timer = new Handler();
	private static final int DELAY = 500;

	private static final int CANCEL_DIALOG = 0;

	private List<TeamNumTask> tasks = new ArrayList<TeamNumTask>(3);


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pits);

		Intent sync = new Intent(this, DBSyncService.class);
		bindService(sync, watcher, Context.BIND_AUTO_CREATE);

		HELPMESSAGE = "Enter the requested information about each team. \n\n"
				+ "When a team number is entered, the last tiem that data was" +
				"collected about this team will be shown.\n"
				+ "If the date shown is during the current event, data does " +
				"not need to be collected.";

		submitter = new DB(getBaseContext(), binder);
		stats = new PitStatsRR();
		teamT = (EditText) findViewById(R.id.pits_teamT);
		configS = (Spinner) findViewById(R.id.pits_configS);
		drivetrainS = (Spinner) findViewById(R.id.pits_drivetrainS);
		wheeltypeS = (Spinner) findViewById(R.id.pits_wheeltypeS);

		// TODO put correct id's in findView after changing in pits.xml
		pushLitterC = (CheckBox) findViewById(0);
		loadLitterC = (CheckBox) findViewById(0);

		pushToteC = (CheckBox) findViewById(0);
		liftToteC = (CheckBox) findViewById(0);
		needUprightToteC = (CheckBox) findViewById(0);
		canUprightToteC = (CheckBox) findViewById(0);
		stackToteHeightT = (EditText) findViewById(0);

		pushBinC = (CheckBox) findViewById(0);
		liftBinC = (CheckBox) findViewById(0);
		needUprightBinC = (CheckBox) findViewById(0);
		canUprightBinC = (CheckBox) findViewById(0);
		stackBinHeightT = (EditText) findViewById(0);

		coopStackHeightT = (EditText) findViewById(0);

		moveAutoC = (CheckBox) findViewById(0);
		autoBinScoreT = (EditText) findViewById(0);
		autoToteScoreT = (EditText) findViewById(0);
		autoToteStackHeightT = (EditText) findViewById(0);
		autoStepBinsT = (EditText) findViewById(0);


		commentsT = (EditText) findViewById(R.id.pits_commentsT);
		submitB = (Button) findViewById(R.id.pits_submitB);
		teamInfoT = (TextView) findViewById(R.id.pits_teamInfo);

		teamT.addTextChangedListener(new teamTextListener());
		submitB.setOnClickListener(new SubmitListener());
	}

	public void onResume() {
		super.onResume();

		List<String> config = submitter.getConfigList();
		if (config != null) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, config);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			configS.setAdapter(adapter);
		}

		List<String> wheelBase = submitter.getConfigList();
		if (wheelBase != null) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, config);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			configS.setAdapter(adapter);
		}

		List<String> wheelType = submitter.getConfigList();
		if (wheelType != null) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, config);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			configS.setAdapter(adapter);
		}

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String pass = prefs.getString("passPref", "");
		submitter.setPass(pass);
		for (TeamNumTask task : tasks) {
			timer.removeCallbacks(task);
		}
		tasks.clear();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//unbindService(watcher);
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


	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return MainMenuSelection.onOptionsItemSelected(item, this) ?
				true : super.onOptionsItemSelected(item);
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
		// TODO finish adding submit actions as new scoring matrixs are added
		stats.wheel_type = wheeltypeS.getSelectedItem().toString();

		stats.push_litter = pushLitterC.isChecked();
		stats.load_litter = loadLitterC.isChecked();

		stats.push_tote = pushToteC.isChecked();
		stats.lift_tote = liftToteC.isChecked();
		stats.need_upright_tote = needUprightToteC.isChecked();
		stats.can_upright_tote = canUprightToteC.isChecked();
		tstr = stackToteHeightT.getText().toString().trim(); // avoid re-calc
		if (tstr.length() > 0)
			stats.stack_tote_height = Short.valueOf(tstr);
		else
			stats.stack_tote_height = 0;

		stats.push_bin = pushBinC.isChecked();
		stats.lift_bin = liftBinC.isChecked();
		stats.need_upright_bin = needUprightBinC.isChecked();
		stats.can_upright_bin = canUprightBinC.isChecked();
		tstr = stackBinHeightT.getText().toString().trim();
		if (tstr.length() > 0)
			stats.stack_bin_height = Short.valueOf(tstr);
		else
			stats.stack_bin_height = 0;

		tstr = coopStackHeightT.getText().toString().trim();
		if (tstr.length() > 0)
			stats.coop_stack_height = Short.valueOf(tstr);
		else
			stats.coop_stack_height = 0;

		stats.move_auto = moveAutoC.isChecked();
		tstr = autoBinScoreT.getText().toString().trim();
		if (tstr.length() > 0)
			stats.auto_bin_score = Short.valueOf(tstr);
		else
			stats.auto_bin_score = 0;

		tstr = autoToteScoreT.getText().toString().trim();
		if (tstr.length() > 0)
			stats.auto_tote_score = Short.valueOf(tstr);
		else
			stats.auto_tote_score = 0;
		tstr = autoToteStackHeightT.getText().toString().trim();
		if (tstr.length() > 0)
			stats.auto_tote_stack_height = Short.valueOf(tstr);
		else
			stats.auto_tote_stack_height = 0;
		tstr = autoStepBinsT.getText().toString().trim();
		if (tstr.length() > 0)
			stats.auto_step_bins = Short.valueOf(tstr);
		else
			stats.auto_step_bins = 0;
	}

	protected void clear() {
		teamT.setText("");
		clearData();
	}

	protected void clearData() {
		// TODO add things to clear the data fields as submit is filled in
		wheeltypeS.setSelection(0);

		pushLitterC.setChecked(false);
		loadLitterC.setChecked(false);

		pushToteC.setChecked(false);
		liftToteC.setChecked(false);
		needUprightToteC.setChecked(false);
		canUprightToteC.setChecked(false);
		stackToteHeightT.setText("");

		pushBinC.setChecked(false);
		liftBinC.setChecked(false);
		needUprightBinC.setChecked(false);
		canUprightBinC.setChecked(false);
		stackBinHeightT.setText("");

		coopStackHeightT.setText("");

		moveAutoC.setChecked(false);
		autoBinScoreT.setText("");
		autoToteScoreT.setText("");
		autoToteStackHeightT.setText("");
		autoStepBinsT.setText("");
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

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

		public void onTextChanged(CharSequence s, int start, int before, int count) {}

	}


	private void setTeam(int teamNum) {
		String date = submitter.getTeamPitInfo(String.valueOf(teamNum));
		if (date.length() > 0) {
			teamInfoT.setText("Last Updated: " + date.trim());
			getTeamStats(teamNum);
		} else {
			teamInfoT.setText("");
			clearData();
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
		PitStats s = submitter.getTeamPitStats(teamNum);
		if (s instanceof PitStatsRR)
			stats = (PitStatsRR) s;
		else
			return;

		populateData(stats);
	}

	@SuppressWarnings({"rawytypes", "unchecked"})
	public void populateData(PitStatsRR stats) {
		int index = ((ArrayAdapter) configS.getAdapter())
				.getPosition(stats.chassis_config);

		//heightT.setText(String.valueOf(stats.height));
		//commentsT.setText(stats.comments);
		//auto_lowC.setChecked(stats.auto_score_high);
		commentsT.setText(stats.comments);

		pushLitterC.setChecked(stats.push_litter);
		loadLitterC.setChecked(stats.load_litter);

		pushToteC.setChecked(stats.push_tote);
		liftToteC.setChecked(stats.lift_tote);
		needUprightToteC.setChecked(stats.need_upright_tote);
		canUprightToteC.setChecked(stats.can_upright_tote);
		stackToteHeightT.setText(String.valueOf(stats.stack_tote_height));

		pushBinC.setChecked(stats.push_bin);
		liftBinC.setChecked(stats.lift_bin);
		needUprightBinC.setChecked(stats.need_upright_bin);
		canUprightBinC.setChecked(stats.need_upright_bin);
		stackBinHeightT.setText(String.valueOf(stats.stack_bin_height));

		coopStackHeightT.setText(String.valueOf(stats.coop_stack_height));

		moveAutoC.setChecked(stats.move_auto);
		autoBinScoreT.setText(String.valueOf(stats.auto_bin_score));
		autoToteScoreT.setText(String.valueOf(stats.auto_tote_score));
		autoToteStackHeightT.setText(String.valueOf(stats.auto_tote_stack_height));
		autoStepBinsT.setText(String.valueOf(stats.auto_step_bins));

		manipulationDescriptionT.setText(stats.manipulation_description);
	}

}
