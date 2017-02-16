/*
 * Copyright 2017 Daniel Logan
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
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.frc836.database.DBActivity;
import org.frc836.database.PilotStatsStruct;
import org.growingstems.scouting.MainMenuSelection;
import org.growingstems.scouting.MatchSchedule;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;

import java.util.ArrayList;
import java.util.List;


public class PilotActivity extends DBActivity {

    public static final int NUM_SCREENS = 3;
    public static final int PRE_MATCH = 0;
    public static final int MATCH = 1;
    public static final int END_MATCH = 2;

    private PilotMatchViewAdapter mMatchViewAdapter;

    private ViewPager mViewPager;

    private static final int CANCEL_DIALOG = 0;
    private static final int LOAD_DIALOG = 353563;

    private String HELPMESSAGE;

    private PilotStatsStruct[] pilotData;

    private TextView teamThidden;
    private EditText matchT;
    private TextView posT;

    private Button lastB;
    private Button nextB;

    private int mCurrentPage;

    private boolean readOnly = false;
    private String event = null;
    private boolean prac = false;
    private String position = null;

    private MatchSchedule schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match);

        // TODO
        HELPMESSAGE = "Record Match Data here.\nEnter Which defenses are on the field first. (Important!!)\nRecord crossings forward: f, and reverse: r.\nSome defenses can be crossed forward with help: h\nRecord scores: s, and misses: m";

        getGUIRefs();

        schedule = new MatchSchedule();

        Intent intent = getIntent();
        matchT.setText(intent.getStringExtra("match"));
        readOnly = intent.getBooleanExtra("readOnly", false);
        event = intent.getStringExtra("event");
        prac = intent.getBooleanExtra("practice", false);
        position = intent.getStringExtra("position");

        teamThidden.setVisibility(View.INVISIBLE);
        teamThidden.setEnabled(false);

        mMatchViewAdapter = new PilotMatchViewAdapter(getFragmentManager());
        mCurrentPage = PRE_MATCH;

        mViewPager = (ViewPager) findViewById(R.id.matchPager);
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
    }

    protected void onResume() {
        super.onResume();

        pilotData[0].event_id = (event == null || event.length() == 0) ? Prefs.getEvent(getApplicationContext(), "CHS District - Greater DC Event") : event;
        pilotData[1].event_id = (event == null || event.length() == 0) ? Prefs.getEvent(getApplicationContext(), "CHS District - Greater DC Event") : event;

        pilotData[0].practice_match = readOnly ? prac : Prefs.getPracticeMatch(getApplicationContext(), false);
        pilotData[1].practice_match = readOnly ? prac : Prefs.getPracticeMatch(getApplicationContext(), false);

        updatePosition();
    }

    protected void onPause() {
        super.onPause();
    }

    public List<String> getNotesOptions() {
        return db.getNotesOptions();
    }

    public List<String> getTeamNotes(int team_id) {
        return db.getPilotNotesForTeam(team_id);
    }

    public int getTeam(int position) {
        return pilotData[position].team_id;
    }

    public List<String> getTeams() {
        String pos = position == null ? Prefs.getPosition(getApplicationContext(), "Red Pilot") : position;
        List<String> teams = new ArrayList<String>(3);
        String posPrefix;
        if (pos.contains("Blue")) {
            posPrefix = "Blue ";
        } else {
            posPrefix = "Red ";
        }

        for (int i = 0; i < 3; i++) {
            String temp = schedule.getTeam(pilotData[0].match_id, posPrefix + i, this);
            if (temp.length() > 0) {
                teams.add(temp);
            }
        }
        return teams;
    }

    private void updatePosition() {
        String pos = position == null ? Prefs.getPosition(getApplicationContext(), "Red Pilot") : position;
        posT.setText(pos);
        if (pos.contains("Blue")) {
            posT.setTextColor(Color.BLUE);
        } else {
            posT.setTextColor(Color.RED);
        }
        if (!pos.contains("Pilot")) {
            finish();
        }
    }

    private class positionClickListener implements View.OnClickListener {
        public void onClick(View v) {
            MainMenuSelection.openSettings(PilotActivity.this);
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
                                        PilotActivity.this.finish();
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
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (matchT.getText().toString().length() > 0) {
                                    pilotData = new PilotStatsStruct[2];
                                    pilotData[0] = new PilotStatsStruct(0, (event == null || event.length() == 0) ? Prefs.getEvent(getApplicationContext(), "CHS District - Greater DC Event") : event, Integer.valueOf(matchT.getText().toString()),
                                            readOnly ? prac : Prefs.getPracticeMatch(getApplicationContext(), false));
                                    pilotData[1] = new PilotStatsStruct(0, (event == null || event.length() == 0) ? Prefs.getEvent(getApplicationContext(), "CHS District - Greater DC Event") : event, Integer.valueOf(matchT.getText().toString()),
                                            readOnly ? prac : Prefs.getPracticeMatch(getApplicationContext(), false));
                                } else {
                                    pilotData = new PilotStatsStruct[2];
                                    pilotData[0] = new PilotStatsStruct();
                                    pilotData[1] = new PilotStatsStruct();
                                }
                                loadPreMatch();
                                loadMatch();
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
            default:
                dialog = null;
        }
        return dialog;
    }

    private void loadData() {
        String match = matchT.getText().toString();

        boolean loadData = false;
        if (match != null && match.length() > 0) {
            pilotData = db.getPilotData((event == null || event.length() == 0) ? Prefs.getEvent(getApplicationContext(), "CHS District - Greater DC Event") : event, Integer
                    .valueOf(match), readOnly ? prac : Prefs.getPracticeMatch(getApplicationContext(), false));
            if (pilotData == null) {
                pilotData = new PilotStatsStruct[2];
                pilotData[0] = new PilotStatsStruct(0, (event == null || event.length() == 0) ? Prefs.getEvent(getApplicationContext(), "CHS District - Greater DC Event") : event, Integer.valueOf(match),
                        readOnly ? prac : Prefs.getPracticeMatch(getApplicationContext(), false));
                pilotData[1] = new PilotStatsStruct(0, (event == null || event.length() == 0) ? Prefs.getEvent(getApplicationContext(), "CHS District - Greater DC Event") : event, Integer.valueOf(match),
                        readOnly ? prac : Prefs.getPracticeMatch(getApplicationContext(), false));
            } else {
                loadData = true;
            }
        } else {
            pilotData = new PilotStatsStruct[2];
            pilotData[0] = new PilotStatsStruct();
            pilotData[1] = new PilotStatsStruct();
        }

        if (loadData && !readOnly) {
            showDialog(LOAD_DIALOG);
        }

        mViewPager.setCurrentItem(0, true);
        loadAll();
        lastB.setText("Cancel");
        nextB.setText("Start");
    }

    public void pageSelected(int page) {
        switch (mCurrentPage) {
            case PRE_MATCH:
                savePreMatch();
                break;
            case MATCH:
                saveMatch();
                break;
            case END_MATCH:
                saveEnd();
                break;
        }
        mCurrentPage = page;
        switch (page) {
            case PRE_MATCH:
                loadPreMatch();
                lastB.setText("Cancel");
                nextB.setText("Start");
                break;
            case MATCH:
                loadMatch();
                lastB.setText("Pre-Match");
                nextB.setText("End Match");
                break;
            case END_MATCH:
                loadEnd();
                lastB.setText("Match");
                nextB.setText(readOnly ? "Cancel" : "Submit");
                break;
            default:
                loadAll();
                lastB.setText("Cancel");
                nextB.setText("Start");
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


    private static class PilotMatchViewAdapter extends FragmentPagerAdapter {

        SparseArray<PilotFragment> fragments;

        public PilotMatchViewAdapter(FragmentManager fm) {
            super(fm);
            fragments = new SparseArray<PilotFragment>(NUM_SCREENS);
        }

        @Override
        public Fragment getItem(int i) {
            return getMatchFragment(i);

        }

        public PilotFragment getMatchFragment(int i) {
            PilotFragment fragment;

            if (fragments.get(i) != null) {
                return fragments.get(i);
            }
            switch (i) {
                case PRE_MATCH:
                    fragment = PilotPreMatchFragment.newInstance();
                    fragments.put(i, fragment);
                    return fragment;
                case MATCH:
                    fragment = PilotMatchFragment.newInstance();
                    fragments.put(i, fragment);
                    return fragment;
                case END_MATCH:
                default:
                    fragment = PilotEndFragment.newInstance();
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
        teamThidden = (EditText) findViewById(R.id.teamNum);

        matchT = (EditText) findViewById(R.id.matchNum);
        posT = (TextView) findViewById(R.id.pos);

        lastB = (Button) findViewById(R.id.backB);
        nextB = (Button) findViewById(R.id.nextB);

    }

    private void loadPreMatch() {
        mMatchViewAdapter.getMatchFragment(PRE_MATCH).loadData(pilotData);
    }

    private void savePreMatch() {
        saveTeamInfo();
        mMatchViewAdapter.getMatchFragment(PRE_MATCH).saveData(pilotData);
    }

    private void loadMatch() {
        mMatchViewAdapter.getMatchFragment(MATCH).loadData(pilotData);
    }

    private void saveMatch() {
        saveTeamInfo();
        mMatchViewAdapter.getMatchFragment(MATCH).saveData(pilotData);
    }

    private void loadEnd() {
        mMatchViewAdapter.getMatchFragment(END_MATCH).loadData(pilotData);
    }

    private void saveEnd() {
        saveTeamInfo();
        mMatchViewAdapter.getMatchFragment(END_MATCH).saveData(pilotData);
    }

    private void loadAll() {
        loadPreMatch();
        loadMatch();
        loadEnd();
    }

    private void saveAll() {
        savePreMatch();
        saveMatch();
        saveEnd();
    }

    private void saveTeamInfo() {
        String match = matchT.getText().toString();
        if (match != null && match.length() > 0) {
            pilotData[0].match_id = Integer.valueOf(match);
            pilotData[1].match_id = Integer.valueOf(match);
        }
        pilotData[0].position_id = posT.getText().toString();
        pilotData[1].position_id = posT.getText().toString();

        pilotData[0].event_id = (event == null || event.length() == 0) ? Prefs.getEvent(getApplicationContext(), "CHS District - Greater DC Event") : event;
        pilotData[1].event_id = (event == null || event.length() == 0) ? Prefs.getEvent(getApplicationContext(), "CHS District - Greater DC Event") : event;
    }

    private void submit() {
        if (!readOnly) {
            saveEnd();
            db.submitPilot(pilotData[0]);
            db.submitPilot(pilotData[1]);
            nextB.setEnabled(false);
            if (matchT.getText().length() > 0)
                setResult(Integer.valueOf(matchT.getText().toString()) + 1);
        }
        finish();
    }

    public String getPosition() {
        return position == null ? Prefs.getPosition(getApplicationContext(), "Red Pilot") : position;
    }


}
