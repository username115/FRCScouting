package org.frc836.yearly;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.frc836.database.DBActivity;
import org.frc836.database.PitStats;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;

import java.util.ArrayList;
import java.util.List;

public class PitsActivity extends DBActivity {

	private String HELPMESSAGE;

	private PitStats stats;

	private AutoCompleteTextView teamT;
	private TextView teamInfoT;

	private Spinner languageS;
	private EditText weightT;
	private EditText batteryT;
	private EditText chargersT;

	private CheckBox tractionC;
	private CheckBox pneumaticC;
	private CheckBox tankC;
	private CheckBox omniC;
	private CheckBox mecanumC;
	private CheckBox swerveC;
	private CheckBox otherDriveC;

	private static final int NUM_RATINGS = 5;
	private final CheckBox[] mechC = new CheckBox[NUM_RATINGS];
	private final CheckBox[] elecC = new CheckBox[NUM_RATINGS];

	private EditText commentsT;

	////// 2020 Game Specifics //////
	////// 2020 END //////


	private final Handler timer = new Handler();
	private static final int DELAY = 500;

	private final List<TeamNumTask> tasks = new ArrayList<>(3);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pits);

		HELPMESSAGE = "Enter the requested information about each team. \n\n"
			+ "When a team number is entered, the last time that data was "
			+ "collected about this team will be shown.\n"
			+ "If the date shown is during the current event, data does "
			+ "not need to be collected.\n"
			+ "Fields in Green indicate data that can be gathered during match scouting.\n"
			+ "Fields in Red indicate data to be collected by scout by looking at the robot.";

		stats = new PitStats();
		teamT = findViewById(R.id.pits_teamT);
		teamT.setThreshold(1);
		teamInfoT = findViewById(R.id.pits_teamInfo);
		languageS = findViewById(R.id.pits_languageS);
		weightT = findViewById(R.id.robot_gross_weightT);
		batteryT = findViewById(R.id.team_batteriesT);
		chargersT = findViewById(R.id.team_chargersT);

		tractionC = findViewById(R.id.tractionC);
		pneumaticC = findViewById(R.id.pneumaticC);
		tankC = findViewById(R.id.tankC);
		omniC = findViewById(R.id.omniC);
		mecanumC = findViewById(R.id.mecanumC);
		swerveC = findViewById(R.id.swerveC);
		otherDriveC = findViewById(R.id.wheelOtherC);

		mechC[0] = findViewById(R.id.pits_mech_1);
		mechC[1] = findViewById(R.id.pits_mech_2);
		mechC[2] = findViewById(R.id.pits_mech_3);
		mechC[3] = findViewById(R.id.pits_mech_4);
		mechC[4] = findViewById(R.id.pits_mech_5);

		elecC[0] = findViewById(R.id.pits_elec_1);
		elecC[1] = findViewById(R.id.pits_elec_2);
		elecC[2] = findViewById(R.id.pits_elec_3);
		elecC[3] = findViewById(R.id.pits_elec_4);
		elecC[4] = findViewById(R.id.pits_elec_5);

		commentsT = findViewById(R.id.pits_commentsT);
		Button submitB = findViewById(R.id.pits_submitB);


		////// 2020 Game Specifics //////
		////// 2020 END //////


		teamT.addTextChangedListener(new teamTextListener());
		submitB.setOnClickListener(new SubmitListener());
		for (int i = 0; i < NUM_RATINGS; i++) {
			mechC[i].setOnCheckedChangeListener(new RatingChangeListener(mechC, i));
			elecC[i].setOnCheckedChangeListener(new RatingChangeListener(elecC, i));
		}
	}

	public void onResume() {
		super.onResume();

		List<String> config = db.getProgrammingList();
		if (config != null) {
			ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
				android.R.layout.simple_spinner_item, config);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			languageS.setAdapter(adapter);
		}

		SharedPreferences prefs = PreferenceManager
			.getDefaultSharedPreferences(getBaseContext());
		String pass = prefs.getString("passPref", "");
		db.setPass(pass);
		for (TeamNumTask task : tasks) {
			timer.removeCallbacks(task);
		}
		tasks.clear();
		setTeamList(db.getTeamsWithData());
	}

	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Cancel Data Entry?\nChanges will not be saved.")
				.setCancelable(false)
				.setPositiveButton("Yes",
						(dialog, id) -> PitsActivity.this.finish())
				.setNegativeButton("No",
						(dialog, id) -> dialog.cancel());
		builder.show();
	}

	@Override
	public String getHelpMessage() {
		return HELPMESSAGE;
	}

	private class SubmitListener implements View.OnClickListener {

		public void onClick(View v) {
			submit();
		}

	}

	protected void submit() {
		String tstr; // to avoid re-calc for string -> int check operations

		tstr = teamT.getText().toString().trim();
		if (tstr.length() > 0)
			stats.team_id = Integer.parseInt(tstr);
		else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setMessage(
					"No team number entered, please enter a team number")
					.setCancelable(true)
					.setPositiveButton("OK",
							(dialog, which) -> {
								dialog.cancel();
								teamT.requestFocus();
							});
			builder.show();
			return;
		}

		stats.programming_id = languageS.getSelectedItem().toString();

		tstr = weightT.getText().toString().trim();
		if (tstr.length() > 0)
			stats.robot_gross_weight_lbs = Integer.parseInt(tstr);
		else
			stats.robot_gross_weight_lbs = 0;

		tstr = batteryT.getText().toString().trim();
		if (tstr.length() > 0)
			stats.team_batteries = Integer.parseInt(tstr);
		else
			stats.team_batteries = 0;

		tstr = chargersT.getText().toString().trim();
		if (tstr.length() > 0)
			stats.team_battery_chargers = Integer.parseInt(tstr);
		else
			stats.team_battery_chargers = 0;

		stats.traction_wheels = tractionC.isChecked();
		stats.pneumatic_wheels = pneumaticC.isChecked();
		stats.tank_drive = tankC.isChecked();
		stats.omni_wheels = omniC.isChecked();
		stats.mecanum_wheels = mecanumC.isChecked();
		stats.swerve_drive = swerveC.isChecked();
		stats.other_drive_wheels = otherDriveC.isChecked();

		stats.mechanical_appearance = 0;
		stats.electrical_appearance = 0;

		for (int i = 0; i < NUM_RATINGS; i++) {
			if (mechC[i].isChecked())
				stats.mechanical_appearance = i + 1;
			if (elecC[i].isChecked())
				stats.electrical_appearance = i + 1;
		}

		stats.notes = commentsT.getText().toString();


		////// 2020 START //////
		////// 2020 END //////


		if (db.submitPits(stats))
			clear();
		else
			Toast.makeText(getApplicationContext(), "Error in local database",
				Toast.LENGTH_LONG).show();

		setTeamList(db.getTeamsWithData(Prefs.getEvent(this, "CHS District Central Virginia Event")));
	}

	protected void clear() {
		clearData();
		teamT.setText("");
	}

	protected void clearData() {
		teamInfoT.setText("");
		commentsT.setText("");

		languageS.setSelection(0);
		weightT.setText("");
		batteryT.setText("");
		chargersT.setText("");

		tractionC.setChecked(false);
		pneumaticC.setChecked(false);
		tankC.setChecked(false);
		omniC.setChecked(false);
		mecanumC.setChecked(false);
		swerveC.setChecked(false);
		otherDriveC.setChecked(false);

		for (int i = 0; i < NUM_RATINGS; i++) {
			mechC[i].setChecked(false);
			elecC[i].setChecked(false);
		}


		////// 2020 START //////
		////// 2020 END //////
	}

	private void clearDataDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(PitsActivity.this);
		builder.setMessage("You had already entered data. Clear form?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						(dialog, id) -> {
							teamInfoT.setText("");
							clearData();
						})
				.setNegativeButton("No",
						(dialog, id) -> dialog.cancel());
		builder.show();
	}

	private class teamTextListener implements TextWatcher {

		public void afterTextChanged(Editable s) {
			if (s.length() > 0) {
				TeamNumTask task = new TeamNumTask();
				tasks.add(task);
				task.teamNum = Integer.parseInt(s.toString());
				timer.postDelayed(task, DELAY);
			} else {
				if (!dataClear()) {
					clearDataDialog();
				}
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
				teamInfoT.setText(getString(R.string.last_updated, date.trim()));
				getTeamStats(teamNum);
			} else {
				teamLoad = teamNum;
				dateLoad = date;
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(
						"Data for this team exists. Overwrite current form?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								(dialog, id) -> {
									teamInfoT.setText(getString(R.string.last_updated, dateLoad.trim()));
									getTeamStats(teamLoad);
								})
						.setNegativeButton("No",
								(dialog, id) -> dialog.cancel());
				builder.show();
			}
		} else {
			if (dataClear()) {
				teamInfoT.setText("");
				clearData();
			} else
				clearDataDialog();
		}
	}

	private class TeamNumTask implements Runnable {
		int teamNum;

		public void run() {
			if (teamT.getText().length() > 0
				&& Integer.parseInt(teamT.getText().toString()) == teamNum) {
				setTeam(teamNum);
			}
		}
	}

	private void getTeamStats(int teamNum) {
		PitStats stats = db.getTeamPitStats(teamNum);

		populateData(stats);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public void populateData(PitStats stats) {
		int index = ((ArrayAdapter) languageS.getAdapter())
			.getPosition(stats.programming_id);
		languageS.setSelection(index);

		commentsT.setText(stats.notes);

		weightT.setText(String.valueOf(stats.robot_gross_weight_lbs));
		batteryT.setText(String.valueOf(stats.team_batteries));
		chargersT.setText(String.valueOf(stats.team_battery_chargers));

		tractionC.setChecked(stats.traction_wheels);
		pneumaticC.setChecked(stats.pneumatic_wheels);
		tankC.setChecked(stats.tank_drive);
		omniC.setChecked(stats.omni_wheels);
		mecanumC.setChecked(stats.mecanum_wheels);
		swerveC.setChecked(stats.swerve_drive);
		otherDriveC.setChecked(stats.other_drive_wheels);

		for (int i=0; i<NUM_RATINGS; i++) {
			mechC[i].setChecked(false);
			elecC[i].setChecked(false);
		}
		if (stats.mechanical_appearance > 0 && stats.mechanical_appearance <= NUM_RATINGS)
			mechC[stats.mechanical_appearance-1].setChecked(true);
		if (stats.electrical_appearance > 0 && stats.electrical_appearance <= NUM_RATINGS)
			elecC[stats.electrical_appearance-1].setChecked(true);

		////// 2020 START //////
		////// 2020 END //////
	}

	private boolean dataClear() {
		boolean ratingClear = true;
		for (int i=0; i<NUM_RATINGS; i++) {
			if (mechC[i].isChecked())
				ratingClear = false;
			if (elecC[i].isChecked())
				ratingClear = false;
		}
		return !(commentsT.getText().toString().length() > 0
			|| languageS.getSelectedItemPosition() != 0
			|| weightT.getText().toString().length() > 0
			|| batteryT.getText().toString().length() > 0
			|| chargersT.getText().toString().length() > 0
			|| tractionC.isChecked()
			|| pneumaticC.isChecked()
			|| tankC.isChecked()
			|| omniC.isChecked()
			|| mecanumC.isChecked()
			|| swerveC.isChecked()
			|| otherDriveC.isChecked()
			|| !ratingClear

			////// 2020 START //////
			////// 2020 END //////
		);
	}

	private void setTeamList(List<String> teams) {
		if (teams == null || teams.isEmpty())
			return;
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
			android.R.layout.simple_dropdown_item_1line, teams);

		teamT.setAdapter(adapter);
	}

	private static class RatingChangeListener implements CompoundButton.OnCheckedChangeListener {

		CheckBox[] m_list;
		int m_index;

		RatingChangeListener(CheckBox[] list, int curIndex) {
			m_list = list;
			m_index = curIndex;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				for (int i=0; i<NUM_RATINGS;i++) {
					if (i != m_index)
						m_list[i].setChecked(false);
				}
			}
		}
	}
}
