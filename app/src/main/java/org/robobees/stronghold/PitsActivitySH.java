package org.robobees.stronghold;

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
import org.growingstems.scouting.R;

import java.util.ArrayList;
import java.util.List;

public class PitsActivitySH extends DBActivity {

    private String HELPMESSAGE;

    private PitStatsSH stats;

    private AutoCompleteTextView teamT;
    private Spinner configS;
    private Spinner drivetrainS;
    private Spinner wheeltypeS;

    private EditText commentsT;
    private Button submitB;
    private TextView teamInfoT;

    //autonomous mode
    private CheckBox startSpyC;
    private CheckBox autoReachC;
    private CheckBox autoCrossC;
    private EditText autoScoreLowT;
    private EditText autoScoreHighT;

    //Defenses
    private CheckBox crossPortcullisC;
    private CheckBox crossChevalC;
    private CheckBox crossMoatC;
    private CheckBox crossRampartsC;
    private CheckBox crossDrawbridgeForC;
    private CheckBox crossDrawbridgeHelpC;
    private CheckBox crossDrawbridgeRevC;
    private CheckBox crossSallyForC;
    private CheckBox crossSallyHelpC;
    private CheckBox crossSallyRevC;
    private CheckBox crossRockWallC;
    private CheckBox crossRoughTerrainC;
    private CheckBox crossLowBarC;

    //other
    private CheckBox scoreLowC;
    private CheckBox scoreHighC;
    private CheckBox challengeC;
    private CheckBox scaleC;

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

        stats = new PitStatsSH();
        teamT = (AutoCompleteTextView) findViewById(R.id.pits_teamT);
        teamT.setThreshold(1);
        configS = (Spinner) findViewById(R.id.pits_configS);
        drivetrainS = (Spinner) findViewById(R.id.pits_drivetrainS);
        wheeltypeS = (Spinner) findViewById(R.id.pits_wheeltypeS);

        //autonomous mode
        startSpyC = (CheckBox) findViewById(R.id.start_spy_pit);
        autoReachC = (CheckBox) findViewById(R.id.auto_reach_pit);
        autoCrossC = (CheckBox) findViewById(R.id.auto_cross_pit);
        autoScoreLowT = (EditText) findViewById(R.id.auto_score_low_pit);
        autoScoreHighT = (EditText) findViewById(R.id.auto_score_high_pit);

        //Defenses
        crossPortcullisC = (CheckBox) findViewById(R.id.portcullis_pit);
        crossChevalC = (CheckBox) findViewById(R.id.cheval_pit);
        crossMoatC = (CheckBox) findViewById(R.id.moat_pit);
        crossRampartsC = (CheckBox) findViewById(R.id.ramparts_pit);
        crossDrawbridgeForC = (CheckBox) findViewById(R.id.drawbridge_for_pit);
        crossDrawbridgeHelpC = (CheckBox) findViewById(R.id.drawbridge_for_with_help_pit);
        crossDrawbridgeRevC = (CheckBox) findViewById(R.id.drawbridge_rev_pit);
        crossSallyForC = (CheckBox) findViewById(R.id.sally_for_pit);
        crossSallyHelpC = (CheckBox) findViewById(R.id.sally_for_with_help_pit);
        crossSallyRevC = (CheckBox) findViewById(R.id.sally_rev_pit);
        crossRockWallC = (CheckBox) findViewById(R.id.rock_wall_pit);
        crossRoughTerrainC = (CheckBox) findViewById(R.id.rough_terrain_pit);
        crossLowBarC = (CheckBox) findViewById(R.id.low_bar_pit);

        //other
        scoreLowC = (CheckBox) findViewById(R.id.score_low_pit);
        scoreHighC = (CheckBox) findViewById(R.id.score_high_pit);
        challengeC = (CheckBox) findViewById(R.id.challenge_pit);
        scaleC = (CheckBox) findViewById(R.id.scale_pit);

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
                                        PitsActivitySH.this.finish();
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
            stats.team = Integer.valueOf(tstr);
        else {
            showDialog(NOTEAM_DIALOG);
            return;
        }

        stats.wheel_type = wheeltypeS.getSelectedItem().toString();
        stats.chassis_config = configS.getSelectedItem().toString();
        stats.wheel_base = drivetrainS.getSelectedItem().toString();

        stats.comments = commentsT.getText().toString();

        stats.start_spy = startSpyC.isChecked();
        stats.auto_reach = autoReachC.isChecked();
        stats.auto_cross = autoCrossC.isChecked();
        tstr = autoScoreLowT.getText().toString().trim();
        if (tstr.length() > 0)
            stats.auto_score_low = Short.valueOf(tstr);
        else
            stats.auto_score_low = 0;
        tstr = autoScoreHighT.getText().toString().trim();
        if (tstr.length() > 0)
            stats.auto_score_high = Short.valueOf(tstr);
        else
            stats.auto_score_high = 0;

        stats.cross_portcullis = crossPortcullisC.isChecked();
        stats.cross_cheval = crossChevalC.isChecked();
        stats.cross_moat = crossMoatC.isChecked();
        stats.cross_ramparts = crossRampartsC.isChecked();
        stats.cross_drawbridge_for = crossDrawbridgeForC.isChecked();
        stats.cross_drawbridge_for_with_help = crossDrawbridgeHelpC.isChecked();
        stats.cross_drawbridge_rev = crossDrawbridgeRevC.isChecked();
        stats.cross_sally_for = crossSallyForC.isChecked();
        stats.cross_sally_for_with_help = crossSallyHelpC.isChecked();
        stats.cross_sally_rev = crossSallyRevC.isChecked();
        stats.cross_rock_wall = crossRockWallC.isChecked();
        stats.cross_rough_terrain = crossRoughTerrainC.isChecked();
        stats.cross_low_bar = crossLowBarC.isChecked();
        stats.score_low = scoreLowC.isChecked();
        stats.score_high = scoreHighC.isChecked();
        stats.challenge = challengeC.isChecked();
        stats.scale = scaleC.isChecked();

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

        //autonomous mode
        startSpyC.setChecked(false);
        autoReachC.setChecked(false);
        autoCrossC.setChecked(false);
        autoScoreLowT.setText("");
        autoScoreHighT.setText("");

        //Defenses
        crossPortcullisC.setChecked(false);
        crossChevalC.setChecked(false);
        crossMoatC.setChecked(false);
        crossRampartsC.setChecked(false);
        crossDrawbridgeForC.setChecked(false);
        crossDrawbridgeHelpC.setChecked(false);
        crossDrawbridgeRevC.setChecked(false);
        crossSallyForC.setChecked(false);
        crossSallyHelpC.setChecked(false);
        crossSallyRevC.setChecked(false);
        crossRockWallC.setChecked(false);
        crossRoughTerrainC.setChecked(false);
        crossLowBarC.setChecked(false);

        //other
        scoreLowC.setChecked(false);
        scoreHighC.setChecked(false);
        challengeC.setChecked(false);
        scaleC.setChecked(false);
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
        if (s instanceof PitStatsSH)
            stats = (PitStatsSH) s;
        else
            return;

        populateData(stats);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void populateData(PitStatsSH stats) {
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

        startSpyC.setChecked(stats.start_spy);
        autoReachC.setChecked(stats.auto_reach);
        autoCrossC.setChecked(stats.auto_cross);
        autoScoreLowT.setText(String.valueOf(stats.auto_score_low));
        autoScoreHighT.setText(String.valueOf(stats.auto_score_high));

        crossPortcullisC.setChecked(stats.cross_portcullis);
        crossChevalC.setChecked(stats.cross_cheval);
        crossMoatC.setChecked(stats.cross_moat);
        crossRampartsC.setChecked(stats.cross_ramparts);
        crossDrawbridgeForC.setChecked(stats.cross_drawbridge_for);
        crossDrawbridgeHelpC.setChecked(stats.cross_drawbridge_for_with_help);
        crossDrawbridgeRevC.setChecked(stats.cross_drawbridge_rev);
        crossSallyForC.setChecked(stats.cross_sally_for);
        crossSallyHelpC.setChecked(stats.cross_sally_for_with_help);
        crossSallyRevC.setChecked(stats.cross_sally_rev);
        crossRockWallC.setChecked(stats.cross_rock_wall);
        crossRoughTerrainC.setChecked(stats.cross_rough_terrain);
        crossLowBarC.setChecked(stats.cross_low_bar);
        scoreLowC.setChecked(stats.score_low);
        scoreHighC.setChecked(stats.score_high);
        challengeC.setChecked(stats.challenge);
        scaleC.setChecked(stats.scale);
    }

    private boolean dataClear() {
        if (commentsT.getText().toString().length() > 0
                || configS.getSelectedItemPosition() != 0
                || drivetrainS.getSelectedItemPosition() != 0
                || wheeltypeS.getSelectedItemPosition() != 0
                || startSpyC.isChecked() || autoReachC.isChecked()
                || autoCrossC.isChecked()
                || autoScoreLowT.getText().toString().length() > 0
                || autoScoreHighT.getText().toString().length() > 0
                || crossPortcullisC.isChecked() || crossChevalC.isChecked()
                || crossMoatC.isChecked() || crossRampartsC.isChecked()
                || crossDrawbridgeForC.isChecked() || crossDrawbridgeHelpC.isChecked()
                || crossDrawbridgeRevC.isChecked() || crossSallyForC.isChecked()
                || crossSallyHelpC.isChecked() || crossSallyRevC.isChecked()
                || crossRockWallC.isChecked() || crossRoughTerrainC.isChecked()
                || crossLowBarC.isChecked() || scoreHighC.isChecked()
                || scoreLowC.isChecked() || challengeC.isChecked() || scaleC.isChecked())
            return false;
        return true;
    }

    private void setTeamList(List<String> teams) {
        if (teams.isEmpty())
            return;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, teams);

        teamT.setAdapter(adapter);
    }
}
