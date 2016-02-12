/*
 * Copyright 2015 Daniel Logan, Matthew Berkin
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

package org.robobees.stronghold;


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
import org.growingstems.scouting.MainMenuSelection;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;


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

    private String HELPMESSAGE;

    private MatchStatsSH teamData;

    private EditText teamText;
    private EditText matchT;
    private TextView posT;

    private Button lastB;
    private Button nextB;

    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match);

        HELPMESSAGE = "interface used for recording match data."; //TODO finish match help message

        getGUIRefs();

        Intent intent = getIntent();
        teamText.setText(intent.getStringExtra("team"));
        matchT.setText(intent.getStringExtra("match"));

        mMatchViewAdapter = new MatchViewAdapter(getFragmentManager());
        mCurrentPage = PRE_MATCH_SCREEN;

        mViewPager = (ViewPager) findViewById(R.id.matchPager);
        mViewPager.setAdapter(mMatchViewAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                pageSelected(position);
            }
        });

        posT.setOnClickListener(new positionClickListener());

        loadData();
    }

    protected void onResume() {
        super.onResume();

        teamData.event = Prefs.getEvent(getApplicationContext(), "CHS District - Greater DC Event");

        teamData.practice_match = Prefs.getPracticeMatch(getApplicationContext(), false);

        updatePosition();

        //TODO update note options from db
        /*
        List<String> options = db.getNotesOptions();

		if (options == null)
			options = new ArrayList<String>(1);

		options.add(0, commonNotes.getItemAtPosition(0).toString());

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, options);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		commonNotes.setAdapter(adapter);
         */
    }

    private void updatePosition() {
        String pos = Prefs.getPosition(getApplicationContext(), "Red 1");
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
                builder.setMessage("Data for this match Exists.\nLoad old match?")
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
                                            teamData = new MatchStatsSH(
                                                    Integer.valueOf(teamText
                                                            .getText().toString()),
                                                    Prefs.getEvent(
                                                            getApplicationContext(),
                                                            "Chesapeake Regional"),
                                                    Integer.valueOf(matchT
                                                            .getText().toString()
                                                            .length()));
                                        } else
                                            teamData = new MatchStatsSH();

                                        loadPreMatch();
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
            default:
                dialog = null;
        }
        return dialog;
    }

    private void loadData() {
        String team = teamText.getText().toString();
        String match = matchT.getText().toString();

        boolean loadData = false;
        if (team != null && team.length() > 0 && match != null
                && match.length() > 0) {
            teamData = (MatchStatsSH) db.getMatchStats(Prefs.getEvent(
                    getApplicationContext(), "CHS District - Greater DC Event"), Integer
                    .valueOf(match), Integer.valueOf(team), Prefs
                    .getPracticeMatch(getApplicationContext(), false));
            if (teamData == null)
                teamData = new MatchStatsSH(Integer.valueOf(team),
                        Prefs.getEvent(getApplicationContext(),
                                "CHS District - Greater DC Event"), Integer.valueOf(match),
                        Prefs.getPracticeMatch(getApplicationContext(), false));
            else
                loadData = true;
        } else
            teamData = new MatchStatsSH();

        if (loadData) {
            showDialog(LOAD_DIALOG);
        }
        loadAll();
        mViewPager.setCurrentItem(0, true);
        lastB.setText("Cancel");
        nextB.setText("Auto");
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
            default:
                saveAll();
        }
        mCurrentPage = page;
        switch (page) {
            case PRE_MATCH_SCREEN:
                loadPreMatch();
                lastB.setText("Cancel");
                nextB.setText("Auto");
                break;
            case AUTO_SCREEN:
                loadAuto();
                lastB.setText("Pre-match");
                nextB.setText("Tele op");
                break;
            case TELE_SCREEN:
                loadTele();
                lastB.setText("Auto");
                nextB.setText("End Game");
                break;
            case END_SCREEN:
                loadEnd();
                lastB.setText("Tele op");
                nextB.setText("submit");
                break;
            default:
                loadAll();
                lastB.setText("Cancel");
                nextB.setText("Auto");
        }
    }

    public void onBack(View v) {
        if (mCurrentPage == 0 || mCurrentPage >= NUM_SCREENS) {
            showDialog(CANCEL_DIALOG);
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

        public MatchViewAdapter(FragmentManager fm) {
            super(fm);
            fragments = new SparseArray<MatchFragment>(NUM_SCREENS);
        }

        @Override
        public Fragment getItem(int i) {
            return getMatchFragment(i);

        }

        public MatchFragment getMatchFragment(int i) {
            MatchFragment fragment;

            if (fragments.get(i) != null) {
                return fragments.get(i);
            }
            switch (i) {
                case PRE_MATCH_SCREEN:
                    fragment = PreMatch.newInstance();
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
        teamText = (EditText) findViewById(R.id.teamNum);

        matchT = (EditText) findViewById(R.id.matchNum);
        posT = (TextView) findViewById(R.id.pos);

        lastB = (Button) findViewById(R.id.backB);
        nextB = (Button) findViewById(R.id.nextB);

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
        loadPreMatch();
        loadAuto();
        loadTele();
        loadEnd();
    }

    private void saveAll() {
        savePreMatch();
        saveAuto();
        saveTele();
        saveEnd();
    }

    private void saveTeamInfo() {
        String team = teamText.getText().toString();
        if (team != null && team.length() > 0)
            teamData.team = Integer.valueOf(team);
        String match = matchT.getText().toString();
        if (match != null && match.length() > 0) {
            teamData.match = Integer.valueOf(match);
        }
        teamData.position = posT.getText().toString();
    }

    private void submit() {
        // TODO
    }


}
