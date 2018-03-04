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


    //2018 start
    private CheckBox scoreSwitchC;
    private CheckBox scoreScaleC;

    //autonomous mode
    private CheckBox autoRunC;
    private EditText autoScoreSwitchT;
    private EditText autoScoreScaleT;

    //tele-op
    private CheckBox exchangeDepositC;
    private CheckBox exchangeAcquireC;
    private CheckBox portalAcquireC;
    private CheckBox floorAcquireC;

    //End game
    private CheckBox canClimbC;
    private CheckBox supportOthersC;


    //2018 end

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

        //2018 start
        scoreSwitchC = (CheckBox) findViewById(R.id.pits_can_score_switch);
        scoreScaleC = (CheckBox) findViewById(R.id.pits_can_score_scale);

        //autonomous mode
        autoRunC = (CheckBox) findViewById(R.id.auto_run_pit);
        autoScoreSwitchT = (EditText) findViewById(R.id.auto_score_switch_pit);
        autoScoreScaleT = (EditText) findViewById(R.id.auto_score_scale_pit);


        //tele-op mode
        exchangeDepositC = (CheckBox) findViewById(R.id.pits_can_deposit_exchange);
        exchangeAcquireC = (CheckBox) findViewById(R.id.pits_can_receive_exchange);
        portalAcquireC = (CheckBox) findViewById(R.id.pits_can_receive_portal);
        floorAcquireC = (CheckBox) findViewById(R.id.pits_can_acquire_floor);


        canClimbC = (CheckBox) findViewById(R.id.can_climb_pit);
        supportOthersC = (CheckBox) findViewById(R.id.support_others_pit);
        //2018 end

        maxSpeedT = (EditText) findViewById(R.id.robot_speed_fps_pit);
        weightT = (EditText) findViewById(R.id.robot_gross_weightT);

        commentsT = (EditText) findViewById(R.id.pits_commentsT);
        Button submitB = (Button) findViewById(R.id.pits_submitB);
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



        stats.switch_score = scoreSwitchC.isChecked();
        stats.scale_score = scoreScaleC.isChecked();



        stats.auto_run = autoRunC.isChecked();
        stats.exchange = exchangeDepositC.isChecked();
        stats.exchange_acquire = exchangeAcquireC.isChecked();
        stats.portal_acquire = portalAcquireC.isChecked();
        stats.floor_acquire = floorAcquireC.isChecked();


        stats.climb = canClimbC.isChecked();
        stats.supports_others = supportOthersC.isChecked();

        tstr = autoScoreSwitchT.getText().toString().trim();
        if (tstr.length() > 0)
            stats.auto_switch_count = Integer.valueOf(tstr);
        else
            stats.auto_switch_count = 0;
        tstr = autoScoreScaleT.getText().toString().trim();
        if (tstr.length() > 0)
            stats.auto_scale_count = Integer.valueOf(tstr);
        else
            stats.auto_scale_count = 0;

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


        //2018
        scoreSwitchC.setChecked(false);
        scoreScaleC.setChecked(false);

        //autonomous mode
        autoRunC.setChecked(false);
        autoScoreSwitchT.setText("");
        autoScoreScaleT.setText("");

        //tele-op
        exchangeDepositC.setChecked(false);
        exchangeAcquireC.setChecked(false);
        portalAcquireC.setChecked(false);
        floorAcquireC.setChecked(false);

        canClimbC.setChecked(false);
        supportOthersC.setChecked(false);

        maxSpeedT.setText("");
        weightT.setText("");
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

        scoreSwitchC.setChecked(stats.switch_score);
        scoreScaleC.setChecked(stats.scale_score);

        //autonomous mode
        autoRunC.setChecked(stats.auto_run);
        autoScoreSwitchT.setText(String.valueOf(stats.auto_switch_count));
        autoScoreScaleT.setText(String.valueOf(stats.auto_scale_count));

        //tele-op
        exchangeDepositC.setChecked(stats.exchange);
        exchangeAcquireC.setChecked(stats.exchange_acquire);
        portalAcquireC.setChecked(stats.portal_acquire);
        floorAcquireC.setChecked(stats.floor_acquire);

        canClimbC.setChecked(stats.climb);
        supportOthersC.setChecked(stats.supports_others);

        maxSpeedT.setText(String.valueOf(stats.max_robot_speed_fts));
        weightT.setText(String.valueOf(stats.robot_gross_weight_lbs));

    }

    private boolean dataClear() {
        return  !(commentsT.getText().toString().length() > 0
                || configS.getSelectedItemPosition() != 0
                || drivetrainS.getSelectedItemPosition() != 0
                || wheeltypeS.getSelectedItemPosition() != 0
                || scoreSwitchC.isChecked()
                || scoreScaleC.isChecked()
                || autoRunC.isChecked()
                || autoScoreSwitchT.getText().toString().length() > 0
                || autoScoreScaleT.getText().toString().length() > 0
                || exchangeDepositC.isChecked()
                || exchangeAcquireC.isChecked()
                || portalAcquireC.isChecked()
                || floorAcquireC.isChecked()
                || canClimbC.isChecked()
                || supportOthersC.isChecked()
                || maxSpeedT.getText().toString().length() > 0
                || weightT.getText().toString().length() > 0
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
