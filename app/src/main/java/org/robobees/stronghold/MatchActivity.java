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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.frc836.database.DBActivity;
import org.growingstems.scouting.MainMenuSelection;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;


public class MatchActivity extends DBActivity implements MatchFragment.OnFragmentInteractionListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match);

        HELPMESSAGE = "interface used for recording match data."; //TODO finish match help message

        getGUIRefs();
        //setListeners(); //TODO

        Intent intent = getIntent();
        teamText.setText(intent.getStringExtra("team"));
        matchT.setText(intent.getStringExtra("match"));

        mMatchViewAdapter = new MatchViewAdapter(getFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.matchPager);
        mViewPager.setAdapter(mMatchViewAdapter);

        posT.setOnClickListener(new positionClickListener());

        loadData();
        //setAuto(); //TODO
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

    ;

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

                                        //loadAuto(); //TODO
                                        //loadTele();
                                        //loadEndgame();
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

    @Override
    public void saveData(MatchStatsSH matchData, String source) {
        // TODO
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

        //TODO fill ui elements
        //loadAuto();
        //loadTele();
        //loadEndgame();
        //setAuto();
    }

    public void onBack(View v) { //TODO
        showDialog(CANCEL_DIALOG);
    }

    public void onNext(View v) { //TODO

    }


    private static class MatchViewAdapter extends FragmentPagerAdapter {

        public MatchViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            MatchFragment fragment;
            Bundle args;
            switch (i) {
                case PRE_MATCH_SCREEN:
                    fragment = PreMatch.newInstance();
                    args = new Bundle();
                    args.putInt(MatchFragment.ARG_SECTION_NUMBER, i);
                    fragment.setArguments(args);
                    return fragment;
                case AUTO_SCREEN:
                    fragment = AutoMatchFragment.newInstance();
                    args = new Bundle();
                    args.putInt(MatchFragment.ARG_SECTION_NUMBER, i);
                    fragment.setArguments(args);
                    return fragment;
                case TELE_SCREEN:
                    fragment = TeleMatchFragment.newInstance();
                    args = new Bundle();
                    args.putInt(MatchFragment.ARG_SECTION_NUMBER, i);
                    fragment.setArguments(args);
                    return fragment;
                case END_SCREEN:
                    fragment = EndMatchFragment.newInstance();
                    args = new Bundle();
                    args.putInt(MatchFragment.ARG_SECTION_NUMBER, i);
                    fragment.setArguments(args);
                    return fragment;
            }
            return null; //should never happen
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
}
