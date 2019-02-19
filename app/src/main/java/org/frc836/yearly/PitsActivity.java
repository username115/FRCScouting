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
    private TextView teamInfoT;

    private EditText maxSpeedT;
    private EditText weightT;


    // TODO - define
    ////// 2019 Game Specifics //////
    private Spinner habStartLvlS;
    private CheckBox canPreloadHatchC;
    private CheckBox canPreloadCargoC;
    private CheckBox canFloorPickupHatchC;
    private CheckBox canFloorPickupCargoC;

    private CheckBox sandstormBonusC;
    private CheckBox sandstormRocketHatch1C;
    private CheckBox sandstormRocketHatch2C;
    private CheckBox sandstormRocketHatch3C;
    private CheckBox sandstormRocketCargo1C;
    private CheckBox sandstormRocketCargo2C;
    private CheckBox sandstormRocketCargo3C;
    private CheckBox sandstormShipCargoC;
    private CheckBox sandstormShipHatchFrontC;
    private CheckBox sandstormShipHatchSideC;
    private EditText sandstormCountHatchC;
    private EditText sandstormCountCargoC;

    private CheckBox rocketHatch1C;
    private CheckBox rocketHatch2C;
    private CheckBox rocketHatch3C;
    private CheckBox rocketCargo1C;
    private CheckBox rocketCargo2C;
    private CheckBox rocketCargo3C;
    private CheckBox canClimbHab2C;
    private CheckBox canClimbHab3C;
    private EditText climbSpeed2T;
    private EditText climbSpeed3T;
    ////// 2019 END //////


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

        maxSpeedT = (EditText) findViewById(R.id.robot_speed_fps_pit);
        weightT = (EditText) findViewById(R.id.robot_gross_weightT);

        commentsT = (EditText) findViewById(R.id.pits_commentsT);
        Button submitB = (Button) findViewById(R.id.pits_submitB);
        teamInfoT = (TextView) findViewById(R.id.pits_teamInfo);


        // TODO - map
        ////// 2019 Game Specifics //////
        habStartLvlS = (Spinner)findViewById(R.id.pits_hab_start_level);
        canPreloadHatchC = (CheckBox)findViewById(R.id.pits_can_preload_hatch);
        canPreloadCargoC = (CheckBox)findViewById(R.id.pits_can_preload_cargo);
        canFloorPickupHatchC = (CheckBox)findViewById(R.id.floor_pickup_hatch);
        canFloorPickupCargoC = (CheckBox)findViewById(R.id.floor_pickup_cargo);

        sandstormBonusC = (CheckBox)findViewById(R.id.pits_sandstorm_bonus);
        sandstormRocketHatch1C = (CheckBox)findViewById(R.id.pits_sandstorm_rocket_hatch_1);
        sandstormRocketHatch2C = (CheckBox)findViewById(R.id.pits_sandstorm_rocket_hatch_2);
        sandstormRocketHatch3C = (CheckBox)findViewById(R.id.pits_sandstorm_rocket_hatch_3);
        sandstormRocketCargo1C = (CheckBox)findViewById(R.id.pits_sandstorm_rocket_cargo_1);
        sandstormRocketCargo2C = (CheckBox)findViewById(R.id.pits_sandstorm_rocket_cargo_2);
        sandstormRocketCargo3C = (CheckBox)findViewById(R.id.pits_sandstorm_rocket_cargo_3);
        sandstormShipCargoC = (CheckBox)findViewById(R.id.pits_sandstorm_ship_cargo);
        sandstormShipHatchFrontC = (CheckBox)findViewById(R.id.pits_sandstorm_ship_hatch_front);
        sandstormShipHatchSideC = (CheckBox)findViewById(R.id.pits_sandstorm_ship_hatch_side);
        sandstormCountHatchC = (EditText)findViewById(R.id.pits_sandstorm_count_hatch);
        sandstormCountCargoC = (EditText)findViewById(R.id.pits_sandstorm_count_cargo);

        rocketHatch1C = (CheckBox)findViewById(R.id.pits_rocket_hatch_1);
        rocketHatch2C = (CheckBox)findViewById(R.id.pits_rocket_hatch_2);
        rocketHatch3C = (CheckBox)findViewById(R.id.pits_rocket_hatch_3);
        rocketCargo1C = (CheckBox)findViewById(R.id.pits_rocket_cargo_1);
        rocketCargo2C = (CheckBox)findViewById(R.id.pits_rocket_cargo_2);
        rocketCargo3C = (CheckBox)findViewById(R.id.pits_rocket_cargo_3);
        canClimbHab2C = (CheckBox)findViewById(R.id.pits_climb_level_2);
        canClimbHab3C = (CheckBox)findViewById(R.id.pits_climb_level_3);
        climbSpeed2T = (EditText)findViewById(R.id.pits_climb_time_2);
        climbSpeed3T = (EditText)findViewById(R.id.pits_climb_time_3);
        ////// 2019 END //////


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


        // TODO - submit
        ////// 2019 START //////
        tstr = habStartLvlS.getSelectedItem().toString();
        stats.start_hab_level = Integer.valueOf(tstr);
        stats.preload_hatch = canPreloadHatchC.isChecked();
        stats.preload_cargo = canPreloadCargoC.isChecked();
        stats.floor_pickup_hatch = canFloorPickupHatchC.isChecked();
        stats.floor_pickup_cargo = canFloorPickupCargoC.isChecked();

        // Sandstorm
        stats.sandstorm_bonus = sandstormBonusC.isChecked();
        stats.sandstorm_hatch_rocket_1 = sandstormRocketHatch1C.isChecked();
        stats.sandstorm_hatch_rocket_2 = sandstormRocketHatch2C.isChecked();
        stats.sandstorm_hatch_rocket_3 = sandstormRocketHatch3C.isChecked();
        stats.sandstorm_cargo_rocket_1 = sandstormRocketCargo1C.isChecked();
        stats.sandstorm_cargo_rocket_2 = sandstormRocketCargo2C.isChecked();
        stats.sandstorm_cargo_rocket_3 = sandstormRocketCargo3C.isChecked();
        stats.sandstorm_cargo_ship = sandstormShipCargoC.isChecked();
        stats.sandstorm_hatch_ship_front = sandstormShipHatchFrontC.isChecked();
        stats.sandstorm_hatch_ship_side = sandstormShipHatchSideC.isChecked();

        tstr = sandstormCountHatchC.toString().trim();
        if (tstr.length() > 0) stats.sandstorm_hatch_count = Integer.valueOf(tstr);
        else stats.sandstorm_hatch_count = 0;

        tstr = sandstormCountCargoC.toString().trim();
        if (tstr.length() > 0) stats.sandstorm_cargo_count = Integer.valueOf(tstr);
        else stats.sandstorm_cargo_count = 0;

        // TeleOp
        stats.hatch_1 = rocketHatch1C.isChecked();
        stats.hatch_2 = rocketHatch2C.isChecked();
        stats.hatch_3 = rocketHatch3C.isChecked();
        stats.cargo_1 = rocketCargo1C.isChecked();
        stats.cargo_2 = rocketCargo2C.isChecked();
        stats.cargo_3 = rocketCargo3C.isChecked();
        stats.hab_climb_2 = canClimbHab2C.isChecked();
        stats.hab_climb_3 = canClimbHab3C.isChecked();

        tstr = climbSpeed2T.toString().trim();
        if (tstr.length() > 0) stats.hab_climb_speed_lvl_2_sec = Integer.valueOf(tstr);
        else stats.hab_climb_speed_lvl_2_sec = 0;

        tstr = climbSpeed3T.toString().trim();
        if (tstr.length() > 0) stats.hab_climb_speed_lvl_3_sec = Integer.valueOf(tstr);
        else stats.hab_climb_speed_lvl_3_sec = 0;
        ////// 2019 END //////


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

        configS.setSelection(0);
        drivetrainS.setSelection(0);
        wheeltypeS.setSelection(0);

        maxSpeedT.setText("");
        weightT.setText("");


        // TODO - clear
        ////// 2019 START //////
        habStartLvlS.setSelection(0);
        canPreloadHatchC.setChecked(false);
        canPreloadCargoC.setChecked(false);
        canFloorPickupHatchC.setChecked(false);
        canFloorPickupCargoC.setChecked(false);

        sandstormBonusC.setChecked(false);
        sandstormRocketHatch1C.setChecked(false);
        sandstormRocketHatch2C.setChecked(false);
        sandstormRocketHatch3C.setChecked(false);
        sandstormRocketCargo1C.setChecked(false);
        sandstormRocketCargo2C.setChecked(false);
        sandstormRocketCargo3C.setChecked(false);
        sandstormShipCargoC.setChecked(false);
        sandstormShipHatchFrontC.setChecked(false);
        sandstormShipHatchSideC.setChecked(false);
        sandstormCountHatchC.setText("");
        sandstormCountCargoC.setText("");

        rocketHatch1C.setChecked(false);
        rocketHatch2C.setChecked(false);
        rocketHatch3C.setChecked(false);
        rocketCargo1C.setChecked(false);
        rocketCargo2C.setChecked(false);
        rocketCargo3C.setChecked(false);
        canClimbHab2C.setChecked(false);
        canClimbHab3C.setChecked(false);
        climbSpeed2T.setText("");
        climbSpeed3T.setText("");
        ////// 2019 END //////
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

        maxSpeedT.setText(String.valueOf(stats.max_robot_speed_fts));
        weightT.setText(String.valueOf(stats.robot_gross_weight_lbs));


        // TODO - populate
        ////// 2019 START //////
        index = ((ArrayAdapter) habStartLvlS.getAdapter())
                .getPosition(stats.start_hab_level);
        habStartLvlS.setSelection(index);
        canPreloadHatchC.setChecked(stats.preload_hatch);
        canPreloadCargoC.setChecked(stats.preload_cargo);
        canFloorPickupHatchC.setChecked(stats.floor_pickup_hatch);
        canFloorPickupCargoC.setChecked(stats.floor_pickup_cargo);

        sandstormBonusC.setChecked(stats.sandstorm_bonus);
        sandstormRocketHatch1C.setChecked(stats.sandstorm_hatch_rocket_1);
        sandstormRocketHatch2C.setChecked(stats.sandstorm_hatch_rocket_2);
        sandstormRocketHatch3C.setChecked(stats.sandstorm_hatch_rocket_3);
        sandstormRocketCargo1C.setChecked(stats.sandstorm_cargo_rocket_1);
        sandstormRocketCargo2C.setChecked(stats.sandstorm_cargo_rocket_2);
        sandstormRocketCargo3C.setChecked(stats.sandstorm_cargo_rocket_3);
        sandstormShipCargoC.setChecked(stats.sandstorm_cargo_ship);
        sandstormShipHatchFrontC.setChecked(stats.sandstorm_hatch_ship_front);
        sandstormShipHatchSideC.setChecked(stats.sandstorm_hatch_ship_side);
        sandstormCountHatchC.setText(String.valueOf(stats.sandstorm_hatch_count));
        sandstormCountCargoC.setText(String.valueOf(stats.sandstorm_cargo_count));

        rocketHatch1C.setChecked(stats.hatch_1);
        rocketHatch2C.setChecked(stats.hatch_2);
        rocketHatch3C.setChecked(stats.hatch_3);
        rocketCargo1C.setChecked(stats.cargo_1);
        rocketCargo2C.setChecked(stats.cargo_2);
        rocketCargo3C.setChecked(stats.cargo_3);
        canClimbHab2C.setChecked(stats.hab_climb_2);
        canClimbHab3C.setChecked(stats.hab_climb_3);
        climbSpeed2T.setText(String.valueOf(stats.hab_climb_speed_lvl_2_sec));
        climbSpeed3T.setText(String.valueOf(stats.hab_climb_speed_lvl_3_sec));
        ////// 2019 END //////
    }

    private boolean dataClear() {
        return  !(commentsT.getText().toString().length() > 0
                || configS.getSelectedItemPosition() != 0
                || drivetrainS.getSelectedItemPosition() != 0
                || wheeltypeS.getSelectedItemPosition() != 0
                || maxSpeedT.getText().toString().length() > 0
                || weightT.getText().toString().length() > 0

                // TODO - isCleared
                ////// 2019 START //////
                || habStartLvlS.getSelectedItemPosition() != 0
                || canPreloadHatchC.isChecked()
                || canPreloadCargoC.isChecked()
                || canFloorPickupHatchC.isChecked()
                || canFloorPickupCargoC.isChecked()

                || sandstormBonusC.isChecked()
                || sandstormRocketHatch1C.isChecked()
                || sandstormRocketHatch2C.isChecked()
                || sandstormRocketHatch3C.isChecked()
                || sandstormRocketCargo1C.isChecked()
                || sandstormRocketCargo2C.isChecked()
                || sandstormRocketCargo3C.isChecked()
                || sandstormShipCargoC.isChecked()
                || sandstormShipHatchFrontC.isChecked()
                || sandstormShipHatchSideC.isChecked()
                || sandstormCountHatchC.getText().toString().length() > 0
                || sandstormCountCargoC.getText().toString().length() > 0

                || rocketHatch1C.isChecked()
                || rocketHatch2C.isChecked()
                || rocketHatch3C.isChecked()
                || rocketCargo1C.isChecked()
                || rocketCargo2C.isChecked()
                || rocketCargo3C.isChecked()
                || canClimbHab2C.isChecked()
                || canClimbHab3C.isChecked()
                || climbSpeed2T.getText().toString().length() > 0
                || climbSpeed3T.getText().toString().length() > 0
                ////// 2019 END //////
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
