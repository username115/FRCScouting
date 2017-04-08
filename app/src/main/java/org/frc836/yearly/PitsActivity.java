package org.frc836.yearly;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.frc836.database.DBActivity;
import org.frc836.database.PitStats;
import org.growingstems.scouting.MainMenuSelection;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;

import java.util.ArrayList;
import java.util.List;

public class PitsActivity extends DBActivity {

    private String HELPMESSAGE;

    private PitStats stats;

    private AutoCompleteTextView teamT;
    private Spinner configS;
    private Spinner drivetrainS;
    private Spinner wheeltypeS;

    private EditText commentsT;
    private Button submitB;
    private TextView teamInfoT;


    //2017 start
    private CheckBox scoreLowC;
    private CheckBox scoreHighC;
    private CheckBox scoreGearC;

    //autonomous mode
    private CheckBox autoGearC;
    private CheckBox autoHopperC;
    private EditText autoScoreLowT;
    private EditText autoScoreHighT;

    //tele-op
    private EditText lowGoalsT;
    private EditText highGoalsT;
    private EditText accuracyT;
    private EditText scoringSpeedT;
    private EditText fuelCapacityT;
    private CheckBox groundLoadFuelC;
    private CheckBox hopperLoadFuelC;
    private CheckBox stationLoadFuelC;
    private CheckBox groundLoadGearC;
    private CheckBox stationLoadGearC;

    private EditText floorLoadingSpeedT;
    private EditText maxSpeedT;
    private EditText weightT;
    private CheckBox canClimbC;
    private CheckBox customRopeC;


    //2017 end

    private Handler timer = new Handler();
    private static final int DELAY = 500;

    private static final int CANCEL_DIALOG = 0;
    private static final int NOTEAM_DIALOG = 24243;
    private static final int CLEAR_DATA_DIALOG = 23930;
    private static final int OVERWRITE_DATA_DIALOG = 59603;

    private List<TeamNumTask> tasks = new ArrayList<TeamNumTask>(3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pits);

        HELPMESSAGE = "Enter the requested information about each team. \n\n"
                + "When a team number is entered, the last time that data was "
                + "collected about this team will be shown.\n"
                + "If the date shown is during the current event, data does "
                + "not need to be collected.";

        stats = new PitStats();
        teamT = (AutoCompleteTextView) findViewById(R.id.pits_teamT);
        teamT.setThreshold(1);
        configS = (Spinner) findViewById(R.id.pits_configS);
        drivetrainS = (Spinner) findViewById(R.id.pits_drivetrainS);
        wheeltypeS = (Spinner) findViewById(R.id.pits_wheeltypeS);

        //2017 start
        scoreLowC = (CheckBox) findViewById(R.id.pits_can_score_low);
        scoreHighC = (CheckBox) findViewById(R.id.pits_can_score_high);
        scoreGearC = (CheckBox) findViewById(R.id.pits_can_score_gears);

        //autonomous mode
        autoGearC = (CheckBox) findViewById(R.id.auto_gear_pit);
        autoHopperC = (CheckBox) findViewById(R.id.auto_hopper_pit);
        autoScoreLowT = (EditText) findViewById(R.id.auto_score_low_pit);
        autoScoreHighT = (EditText) findViewById(R.id.auto_score_high_pit);

        //tele-op mode
        lowGoalsT = (EditText) findViewById(R.id.tele_score_low_pit);
        highGoalsT = (EditText) findViewById(R.id.tele_score_high_pit);
        accuracyT = (EditText) findViewById(R.id.accuracy_pit);
        scoringSpeedT = (EditText) findViewById(R.id.scoring_speed_pit);
        fuelCapacityT = (EditText) findViewById(R.id.fuel_capacity_pit);
        groundLoadFuelC = (CheckBox) findViewById(R.id.ground_load_fuel_pit);
        hopperLoadFuelC = (CheckBox) findViewById(R.id.hopper_load_fuel_pit);
        stationLoadFuelC = (CheckBox) findViewById(R.id.station_load_fuel_pit);
        groundLoadGearC = (CheckBox) findViewById(R.id.ground_load_gear_pit);
        stationLoadGearC = (CheckBox) findViewById(R.id.station_load_gear_pit);
        floorLoadingSpeedT = (EditText) findViewById(R.id.loading_speed_fps_pit);
        maxSpeedT = (EditText) findViewById(R.id.robot_speed_fps_pit);
        weightT = (EditText) findViewById(R.id.robot_gross_weightT);
        canClimbC = (CheckBox) findViewById(R.id.can_climb_pit);
        customRopeC = (CheckBox) findViewById(R.id.custom_rope_pit);
        //2017 end

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

        List<String> wheelBase = db.getWheelBaseList();
        if (wheelBase != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, wheelBase);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            drivetrainS.setAdapter(adapter);
        }

        List<String> wheelType = db.getWheelTypeList();
        if (wheelType != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, wheelType);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            wheeltypeS.setAdapter(adapter);
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

    private class SubmitListener implements View.OnClickListener {

        public void onClick(View v) {
            submit();
        }

    }

    protected void submit() {
        String tstr; // to avoid re-calc for string -> int check operations

        tstr = teamT.getText().toString().trim();
        if (tstr.length() > 0)
            stats.team_id = Integer.valueOf(tstr);
        else {
            showDialog(NOTEAM_DIALOG);
            return;
        }

        stats.wheel_type_id = wheeltypeS.getSelectedItem().toString();
        stats.config_id = configS.getSelectedItem().toString();
        stats.wheel_base_id = drivetrainS.getSelectedItem().toString();

        stats.notes = commentsT.getText().toString();



        stats.can_score_high = scoreHighC.isChecked();
        stats.can_score_low = scoreLowC.isChecked();
        stats.can_score_gears = scoreGearC.isChecked();

        stats.can_climb = canClimbC.isChecked();

        stats.ground_load_fuel = groundLoadFuelC.isChecked();
        stats.hopper_load_fuel = hopperLoadFuelC.isChecked();
        stats.station_load_fuel = stationLoadFuelC.isChecked();
        stats.ground_load_gear = groundLoadGearC.isChecked();
        stats.station_load_gear = stationLoadGearC.isChecked();

        stats.custom_rope = customRopeC.isChecked();

        tstr = autoScoreLowT.getText().toString().trim();
        if (tstr.length() > 0)
            stats.auto_score_low_count = Integer.valueOf(tstr);
        else
            stats.auto_score_low_count = 0;
        tstr = autoScoreHighT.getText().toString().trim();
        if (tstr.length() > 0)
            stats.auto_score_high_count = Integer.valueOf(tstr);
        else
            stats.auto_score_high_count = 0;
        stats.auto_gear = autoGearC.isChecked();
        stats.auto_hopper = autoHopperC.isChecked();

        tstr = highGoalsT.getText().toString().trim();
        if (tstr.length() > 0)
            stats.tele_score_high_count = Integer.valueOf(tstr);
        else
            stats.tele_score_high_count = 0;
        tstr = lowGoalsT.getText().toString().trim();
        if (tstr.length() > 0)
            stats.tele_score_low_count = Integer.valueOf(tstr);
        else
            stats.tele_score_low_count = 0;

        tstr = accuracyT.getText().toString().trim();
        if (tstr.length() > 0)
            stats.accuracy = Integer.valueOf(tstr);
        else
            stats.accuracy = 0;
        tstr = fuelCapacityT.getText().toString().trim();
        if (tstr.length() > 0)
            stats.fuel_capacity = Integer.valueOf(tstr);
        else
            stats.fuel_capacity = 0;
        tstr = scoringSpeedT.getText().toString().trim();
        if (tstr.length() > 0)
            stats.scoring_speed_bps = Integer.valueOf(tstr);
        else
            stats.scoring_speed_bps = 0;
        tstr = floorLoadingSpeedT.getText().toString().trim();
        if (tstr.length() > 0)
            stats.loading_speed_bps = Integer.valueOf(tstr);
        else
            stats.loading_speed_bps = 0;
        tstr = maxSpeedT.getText().toString().trim();
        if (tstr.length() > 0)
            stats.max_robot_speed_fts = Integer.valueOf(tstr);
        else
            stats.max_robot_speed_fts = 0;
        tstr = weightT.getText().toString().trim();
        if (tstr.length() > 0)
            stats.robot_gross_weight_lbs = Integer.valueOf(tstr);
        else
            stats.robot_gross_weight_lbs = 0;




        if (db.submitPits(stats))
            clear();
        else
            Toast.makeText(getApplicationContext(), "Error in local database",
                    Toast.LENGTH_LONG).show();

        setTeamList(db.getTeamsWithData(Prefs.getEvent(this, "CHS District - Greater DC Event")));
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


        //2017
        scoreLowC.setChecked(false);
        scoreHighC.setChecked(false);
        scoreGearC.setChecked(false);

        //autonomous mode
        autoGearC.setChecked(false);
        autoHopperC.setChecked(false);
        autoScoreLowT.setText("");
        autoScoreHighT.setText("");

        //tele-op
        lowGoalsT.setText("");
        highGoalsT.setText("");
        accuracyT.setText("");
        scoringSpeedT.setText("");
        fuelCapacityT.setText("");
        groundLoadFuelC.setChecked(false);
        hopperLoadFuelC.setChecked(false);
        stationLoadFuelC.setChecked(false);
        groundLoadGearC.setChecked(false);
        stationLoadGearC.setChecked(false);

        floorLoadingSpeedT.setText("");
        maxSpeedT.setText("");
        weightT.setText("");
        canClimbC.setChecked(false);
        customRopeC.setChecked(false);
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
        PitStats stats = db.getTeamPitStats(teamNum);

        populateData(stats);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void populateData(PitStats stats) {
        int index = ((ArrayAdapter) configS.getAdapter())
                .getPosition(stats.config_id);
        configS.setSelection(index);

        index = ((ArrayAdapter) drivetrainS.getAdapter())
                .getPosition(stats.wheel_base_id);
        drivetrainS.setSelection(index);

        index = ((ArrayAdapter) wheeltypeS.getAdapter())
                .getPosition(stats.wheel_type_id);
        wheeltypeS.setSelection(index);

        commentsT.setText(stats.notes);

        scoreLowC.setChecked(stats.can_score_low);
        scoreHighC.setChecked(stats.can_score_high);
        scoreGearC.setChecked(stats.can_score_gears);

        //autonomous mode
        autoGearC.setChecked(stats.auto_gear);
        autoHopperC.setChecked(stats.auto_hopper);
        autoScoreLowT.setText(String.valueOf(stats.auto_score_low_count));
        autoScoreHighT.setText(String.valueOf(stats.auto_score_high_count));

        //tele-op
        lowGoalsT.setText(String.valueOf(stats.tele_score_low_count));
        highGoalsT.setText(String.valueOf(stats.tele_score_high_count));
        accuracyT.setText(String.valueOf(stats.accuracy));
        scoringSpeedT.setText(String.valueOf(stats.scoring_speed_bps));
        fuelCapacityT.setText(String.valueOf(stats.fuel_capacity));
        groundLoadFuelC.setChecked(stats.ground_load_fuel);
        hopperLoadFuelC.setChecked(stats.hopper_load_fuel);
        stationLoadFuelC.setChecked(stats.station_load_fuel);
        groundLoadGearC.setChecked(stats.ground_load_gear);
        stationLoadGearC.setChecked(stats.station_load_gear);

        floorLoadingSpeedT.setText(String.valueOf(stats.loading_speed_bps));
        maxSpeedT.setText(String.valueOf(stats.max_robot_speed_fts));
        weightT.setText(String.valueOf(stats.robot_gross_weight_lbs));
        canClimbC.setChecked(stats.can_climb);
        customRopeC.setChecked(stats.custom_rope);

    }

    private boolean dataClear() {
        return  !(commentsT.getText().toString().length() > 0
                || configS.getSelectedItemPosition() != 0
                || drivetrainS.getSelectedItemPosition() != 0
                || wheeltypeS.getSelectedItemPosition() != 0
                || scoreLowC.isChecked()
                || scoreHighC.isChecked()
                || scoreGearC.isChecked()
                || autoGearC.isChecked()
                || autoHopperC.isChecked()
                || autoScoreLowT.getText().toString().length() > 0
                || autoScoreHighT.getText().toString().length() > 0
                || lowGoalsT.getText().toString().length() > 0
                || highGoalsT.getText().toString().length() > 0
                || accuracyT.getText().toString().length() > 0
                || scoringSpeedT.getText().toString().length() > 0
                || fuelCapacityT.getText().toString().length() > 0
                || groundLoadFuelC.isChecked()
                || hopperLoadFuelC.isChecked()
                || stationLoadFuelC.isChecked()
                || groundLoadGearC.isChecked()
                || stationLoadGearC.isChecked()
                || floorLoadingSpeedT.getText().toString().length() > 0
                || maxSpeedT.getText().toString().length() > 0
                || weightT.getText().toString().length() > 0
                || canClimbC.isChecked()
                || customRopeC.isChecked()
                );
    }

    private void setTeamList(List<String> teams) {
        if (teams == null || teams.isEmpty())
            return;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, teams);

        teamT.setAdapter(adapter);
    }
}
