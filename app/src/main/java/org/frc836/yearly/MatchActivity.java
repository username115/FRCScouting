/*
 * Copyright 2015 Daniel Logan
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

package org.frc836.yearly;


import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.frc836.database.DBActivity;
import org.frc836.database.MatchStatsStruct;
import org.growingstems.scouting.MainMenuSelection;
import org.growingstems.scouting.MatchFragment;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;

import java.util.List;


public class MatchActivity extends DBActivity {

    public static final int NUM_SCREENS = 4;
    public static final int PRE_MATCH_SCREEN = 0;
    public static final int AUTO_SCREEN = 1;
    public static final int TELE_SCREEN = 2;
    public static final int END_SCREEN = 3;

    private MatchViewAdapter mMatchViewAdapter;

    private ViewPager mViewPager;

    private static final int CANCEL_DIALOG = 0;
    private static final int LOAD_DIALOG = 353563;
    private static final int TIME_DIALOG = 2;

    private String HELPMESSAGE;

    private MatchStatsStruct teamData;

    private EditText teamText;
    private EditText matchT;
    private TextView posT;

    private Button lastB;
    private Button nextB;

    private int mCurrentPage;

    private boolean readOnly = false;
    private String event = null;
    private boolean prac = false;
    private String position = null;

    private Handler timer = new Handler();
    private static final int DELAY = 16000;

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            showDialog(TIME_DIALOG);
        }
    };

    private static final String defaultEvent = "CHS District Oxon Hill MD Event";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match);

        HELPMESSAGE = "Record Match Data here.\n" +
                "Pre-match:\n" +
                "Record which section of the player station the robot starts in front of.\n" +
                "Autonomous:\n" +
                "Record if the robot moved off the initiation line.\n" +
                "Record Power Cells scored and missed.\n" +
                "Tele-Op:\n" +
                "Record Power Cells scored and missed.\n" +
				"Record if the robot performed position or rotation control.\n" +
                "End game:\n" +
                "Record fouls, cards, and if a robot climbed, or attempted to climb. Climb indicated with green, attempt with yellow.\n" +
                "Record if the robot climbed and the generator was level at the end of the match.\n" +
                "Record any pertinent notes. Two drop-downs are provided for common notes.";

        getGUIRefs();

        Intent intent = getIntent();
        teamText.setText(intent.getStringExtra("team"));
        matchT.setText(intent.getStringExtra("match"));
        readOnly = intent.getBooleanExtra("readOnly", false);
        event = intent.getStringExtra("event");
        prac = intent.getBooleanExtra("practice", false);
        position = intent.getStringExtra("position");

        mMatchViewAdapter = new MatchViewAdapter(getSupportFragmentManager());
        mCurrentPage = PRE_MATCH_SCREEN;

        mViewPager = findViewById(R.id.matchPager);
        mViewPager.setAdapter(mMatchViewAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                pageSelected(position);
            }
        });

        if (!readOnly)
            posT.setOnClickListener(new positionClickListener());

        loadData();
        loadPreMatch();
    }

    protected void onResume() {
        super.onResume();

        teamData.event_id = event == null ? Prefs.getEvent(getApplicationContext(), defaultEvent) : event;

        teamData.practice_match = readOnly ? prac : Prefs.getPracticeMatch(getApplicationContext(), false);

        updatePosition();

        if (mCurrentPage == AUTO_SCREEN && !readOnly) {
            timer.postDelayed(mUpdateTimeTask, DELAY);
        }
    }

    protected void onPause() {
        super.onPause();
        timer.removeCallbacks(mUpdateTimeTask);
    }

    public List<String> getNotesOptions() {
        return db.getNotesOptions();
    }

    public List<String> getTeamNotes() {
        return db.getNotesForTeam(teamData.team_id);
    }

    private void updatePosition() {
        String pos = position == null ? Prefs.getPosition(getApplicationContext(), "Red 1") : position;
        posT.setText(pos);
        if (pos.contains("Blue")) {
            posT.setTextColor(Color.BLUE);
        } else {
            posT.setTextColor(Color.RED);
        }
    }

    private class positionClickListener implements View.OnClickListener {
        public void onClick(View v) {
            MainMenuSelection.openSettings(MatchActivity.this);
        }
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
                builder.setMessage("Data for this match exists.\nLoad old match?")
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
                                            teamData = new MatchStatsStruct(
                                                    Integer.valueOf(teamText
                                                            .getText().toString()),
                                                    event == null ? Prefs.getEvent(getApplicationContext(), defaultEvent) : event,
                                                    Integer.valueOf(matchT
                                                            .getText().toString()));
                                        } else
                                            teamData = new MatchStatsStruct();

                                        loadAuto();
                                        loadTele();
                                        loadEnd();
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
            case TIME_DIALOG:
                builder.setMessage("Continue to Tele-Op?")
                        .setCancelable(true)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        if (mCurrentPage == AUTO_SCREEN)
                                            onNext(nextB);
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        timer.removeCallbacks(mUpdateTimeTask);
                                        if (!readOnly && mCurrentPage == AUTO_SCREEN)
                                            timer.postDelayed(mUpdateTimeTask, DELAY);
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

    private void loadData() {
        Editable teamE = teamText.getText();
        Editable matchE = matchT.getText();

        boolean loadData = false;
        if (teamE != null && teamE.length() > 0 && matchE != null
                && matchE.length() > 0) {
            String team = teamE.toString();
            String match = matchE.toString();
            teamData = db.getMatchStats(event == null ? Prefs.getEvent(getApplicationContext(), defaultEvent) : event, Integer
                    .valueOf(match), Integer.valueOf(team), readOnly ? prac : Prefs.getPracticeMatch(getApplicationContext(), false));
            if (teamData == null)
                teamData = new MatchStatsStruct(Integer.valueOf(team),
                        event == null ? Prefs.getEvent(getApplicationContext(), defaultEvent) : event, Integer.valueOf(match),
                        readOnly ? prac : Prefs.getPracticeMatch(getApplicationContext(), false));
            else
                loadData = true;
        } else
            teamData = new MatchStatsStruct();

        if (loadData && !readOnly) {
            showDialog(LOAD_DIALOG);
        }
        mViewPager.setCurrentItem(0, true);
        loadAll();
        lastB.setText(getString(R.string.match_change_button_cancel));
        nextB.setText(getString(R.string.match_change_button_auto));
    }

    public void pageSelected(int page) {
        switch (mCurrentPage) {
            case PRE_MATCH_SCREEN:
                savePreMatch();
                break;
            case AUTO_SCREEN:
                saveAuto();
                break;
            case TELE_SCREEN:
                saveTele();
                break;
            case END_SCREEN:
                saveEnd();
                break;
        }
        mCurrentPage = page;
        switch (page) {
            case PRE_MATCH_SCREEN:
                loadPreMatch();
                lastB.setText(getString(R.string.match_change_button_cancel));
                nextB.setText(getString(R.string.match_change_button_auto));
                timer.removeCallbacks(mUpdateTimeTask);
                break;
            case AUTO_SCREEN:
                loadAuto();
                lastB.setText(getString(R.string.match_change_button_prematch));
                nextB.setText(getString(R.string.match_change_button_tele));
                if (!readOnly)
                    timer.postDelayed(mUpdateTimeTask, DELAY);
                break;
            case TELE_SCREEN:
                loadTele();
                lastB.setText(getString(R.string.match_change_button_auto));
                nextB.setText(getString(R.string.match_change_button_end));
                timer.removeCallbacks(mUpdateTimeTask);
                break;
            case END_SCREEN:
                loadEnd();
                lastB.setText(getString(R.string.match_change_button_tele));
                nextB.setText(readOnly ? getString(R.string.match_change_button_cancel) : getString(R.string.match_change_button_submit));
                timer.removeCallbacks(mUpdateTimeTask);
                break;
            default:
                loadAll();
                lastB.setText(getString(R.string.match_change_button_cancel));
                nextB.setText(getString(R.string.match_change_button_auto));
                timer.removeCallbacks(mUpdateTimeTask);
        }
    }

    @Override
    public void onBackPressed() {
        onBack(null);
    }

    public void onBack(View v) {
        if (mCurrentPage == 0 || mCurrentPage >= NUM_SCREENS) {
            if (!readOnly)
                showDialog(CANCEL_DIALOG);
            else
                finish();
        }
        mViewPager.setCurrentItem(mCurrentPage - 1, true);
    }

    public void onNext(View v) {
        if (mCurrentPage < 0 || mCurrentPage >= (NUM_SCREENS - 1)) {
            saveAll();
            submit();
        }
        mViewPager.setCurrentItem(mCurrentPage + 1, true);
    }


    private static class MatchViewAdapter extends FragmentPagerAdapter {

        SparseArray<MatchFragment> fragments;

         MatchViewAdapter(FragmentManager fm) {
            super(fm);
            fragments = new SparseArray<>(NUM_SCREENS);
        }

        @Override
        public Fragment getItem(int i) {
            return getMatchFragment(i);

        }

        MatchFragment getMatchFragment(int i) {
            MatchFragment fragment;

            if (fragments.get(i) != null) {
                return fragments.get(i);
            }
            switch (i) {
                case PRE_MATCH_SCREEN:
                    fragment = PreMatchFragment.newInstance();
                    fragments.put(i, fragment);
                    return fragment;
                case AUTO_SCREEN:
                    fragment = AutoMatchFragment.newInstance();
                    fragments.put(i, fragment);
                    return fragment;
                case TELE_SCREEN:
                    fragment = TeleMatchFragment.newInstance();
                    fragments.put(i, fragment);
                    return fragment;
                case END_SCREEN:
                default:
                    fragment = EndMatchFragment.newInstance();
                    fragments.put(i, fragment);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return NUM_SCREENS;
        }
    }

    private void getGUIRefs() {
        teamText = findViewById(R.id.teamNum);

        matchT = findViewById(R.id.matchNum);
        posT = findViewById(R.id.pos);

        lastB = findViewById(R.id.backB);
        nextB = findViewById(R.id.nextB);

    }

    private void loadPreMatch() {
        mMatchViewAdapter.getMatchFragment(PRE_MATCH_SCREEN).loadData(teamData);
    }

    private void savePreMatch() {
        saveTeamInfo();
        mMatchViewAdapter.getMatchFragment(PRE_MATCH_SCREEN).saveData(teamData);
    }

    private void loadAuto() {
        mMatchViewAdapter.getMatchFragment(AUTO_SCREEN).loadData(teamData);
    }

    private void saveAuto() {
        saveTeamInfo();
        mMatchViewAdapter.getMatchFragment(AUTO_SCREEN).saveData(teamData);
    }

    private void loadTele() {
        mMatchViewAdapter.getMatchFragment(TELE_SCREEN).loadData(teamData);
    }

    private void saveTele() {
        saveTeamInfo();
        mMatchViewAdapter.getMatchFragment(TELE_SCREEN).saveData(teamData);
    }

    private void loadEnd() {
        mMatchViewAdapter.getMatchFragment(END_SCREEN).loadData(teamData);
    }

    private void saveEnd() {
        saveTeamInfo();
        mMatchViewAdapter.getMatchFragment(END_SCREEN).saveData(teamData);
    }

    private void loadAll() {
        loadTele();
        loadAuto();
        loadTele();
        loadEnd();
    }

    private void saveAll() {
        saveTele();
        saveAuto();
        saveTele();
        saveEnd();
    }

    private void saveTeamInfo() {
        Editable team = teamText.getText();
        if (team != null && team.length() > 0)
            teamData.team_id = Integer.valueOf(team.toString());
        Editable match = matchT.getText();
        if (match != null && match.length() > 0) {
            teamData.match_id = Integer.valueOf(match.toString());
        }
        teamData.position_id = posT.getText().toString();
    }

    private void submit() {
        if (!readOnly) {
            saveEnd();
            if (teamData.match_id > 0 && teamData.team_id > 0) {
                db.submitMatch(teamData);
                nextB.setEnabled(false);
                if (matchT.getText().length() > 0)
                    setResult(Integer.valueOf(matchT.getText().toString()) + 1);
            } else if (teamData.match_id <= 0) {
                Toast.makeText(this, "Please enter a match number", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(this, "Please enter a team number", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        finish();
    }

    public String getPosition() {
        return position == null ? Prefs.getPosition(getApplicationContext(), "Red 1") : position;
    }


}
