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
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.frc836.database.DBActivity;
import org.frc836.database.MatchStatsStruct;
import org.growingstems.scouting.MainMenuSelection;
import org.growingstems.scouting.MatchFragment;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;

import java.util.List;


public class MatchActivity extends DBActivity {

    public static final int NUM_SCREENS = 4;
    public static final int AUTO_SCREEN = 0;
    public static final int TELE_SCREEN = 1;
	public static final int CLIMB_SCREEN = 2;
    public static final int END_SCREEN = 3;

    private MatchViewAdapter mMatchViewAdapter;

    private ViewPager mViewPager;

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

    private final Handler timer = new Handler();
    private static final int DELAY = 16000;

    private final Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
			AlertDialog.Builder builder = new AlertDialog.Builder(MatchActivity.this);
			builder.setMessage("Continue to Tele-Op?")
					.setCancelable(true)
					.setPositiveButton("Yes",
							(dialog, id) -> {
								if (mCurrentPage == AUTO_SCREEN)
									onNext(nextB);
							})
					.setNegativeButton("No",
							(dialog, id) -> {
								timer.removeCallbacks(mUpdateTimeTask);
								if (!readOnly && mCurrentPage == AUTO_SCREEN)
									timer.postDelayed(mUpdateTimeTask, DELAY);
								dialog.cancel();
							});
			builder.show();
        }
    };

    private static final String defaultEvent = "CHS District Oxon Hill MD Event";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match);

        HELPMESSAGE = "Record Match Data here.\n" +
				"Input number of cargo scored in both Autonomous and Tele-op periods.\n" +
				"Input hang location, and how long it took to reach final hang position.\n" +
				"A timer has been provided to assist with this.\n" +
				"During the match, observe where cargo was scored from, and mark the locations the robot tended to use.";

        getGUIRefs();

        Intent intent = getIntent();
        teamText.setText(intent.getStringExtra("team"));
        matchT.setText(intent.getStringExtra("match"));
        readOnly = intent.getBooleanExtra("readOnly", false);
        event = intent.getStringExtra("event");
        prac = intent.getBooleanExtra("practice", false);
        position = intent.getStringExtra("position");

        mMatchViewAdapter = new MatchViewAdapter(getSupportFragmentManager());
        mCurrentPage = AUTO_SCREEN;

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
        loadAuto();
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

	@Override
	public String getHelpMessage() {
		return HELPMESSAGE;
	}

	private class positionClickListener implements View.OnClickListener {
        public void onClick(View v) {
            MainMenuSelection.openSettings(MatchActivity.this);
        }
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
                    .parseInt(match), Integer.parseInt(team), readOnly ? prac : Prefs.getPracticeMatch(getApplicationContext(), false));
            if (teamData == null)
                teamData = new MatchStatsStruct(Integer.parseInt(team),
                        event == null ? Prefs.getEvent(getApplicationContext(), defaultEvent) : event, Integer.parseInt(match),
                        readOnly ? prac : Prefs.getPracticeMatch(getApplicationContext(), false));
            else
                loadData = true;
        } else
            teamData = new MatchStatsStruct();

        if (loadData && !readOnly) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Data for this match exists.\nLoad old match?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							(dialog, id) -> dialog.cancel())
					.setNegativeButton("No",
							(dialog, id) -> {
								if (teamText.getText().toString().length() > 0
										&& matchT.getText().toString()
										.length() > 0) {
									teamData = new MatchStatsStruct(
											Integer.parseInt(teamText
													.getText().toString()),
											event == null ? Prefs.getEvent(getApplicationContext(), defaultEvent) : event,
											Integer.parseInt(matchT
													.getText().toString()));
								} else
									teamData = new MatchStatsStruct();

								loadAuto();
								loadTele();
								loadEnd();
							});
			builder.show();
        }
        mViewPager.setCurrentItem(0, true);
        loadAll();
        lastB.setText(getString(R.string.match_change_button_cancel));
        nextB.setText(getString(R.string.match_change_button_tele));
    }

    public void pageSelected(int page) {
        switch (mCurrentPage) {
            case AUTO_SCREEN:
                saveAuto();
                break;
            case TELE_SCREEN:
                saveTele();
                break;
			case CLIMB_SCREEN:
				saveClimb();
				break;
            case END_SCREEN:
                saveEnd();
                break;
        }
        mCurrentPage = page;
        switch (page) {
            case AUTO_SCREEN:
                loadAuto();
				lastB.setText(getString(R.string.match_change_button_cancel));
                nextB.setText(getString(R.string.match_change_button_tele));
                if (!readOnly)
                    timer.postDelayed(mUpdateTimeTask, DELAY);
                break;
            case TELE_SCREEN:
                loadTele();
                lastB.setText(getString(R.string.match_change_button_auto));
                nextB.setText(getString(R.string.match_change_button_climb));
                timer.removeCallbacks(mUpdateTimeTask);
                break;
			case CLIMB_SCREEN:
				loadClimb();
				lastB.setText(getString(R.string.match_change_button_tele));
				nextB.setText(getString(R.string.match_change_button_end));
				timer.removeCallbacks(mUpdateTimeTask);
				break;
            case END_SCREEN:
                loadEnd();
                lastB.setText(getString(R.string.match_change_button_climb));
                nextB.setText(readOnly ? getString(R.string.match_change_button_cancel) : getString(R.string.match_change_button_submit));
                timer.removeCallbacks(mUpdateTimeTask);
                break;
            default:
                loadAll();
                lastB.setText(getString(R.string.match_change_button_cancel));
                nextB.setText(getString(R.string.match_change_button_tele));
                timer.removeCallbacks(mUpdateTimeTask);
        }
    }

    @Override
    public void onBackPressed() {
        onBack(null);
    }

    public void onBack(View v) {
        if (mCurrentPage == 0 || mCurrentPage >= NUM_SCREENS) {
            if (!readOnly) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(
						"Cancel Match Entry?\nChanges will not be saved.")
						.setCancelable(false)
						.setPositiveButton("Yes",
								(dialog, id) -> MatchActivity.this.finish())
						.setNegativeButton("No",
								(dialog, id) -> dialog.cancel());
				builder.show();
			}
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
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            fragments = new SparseArray<>(NUM_SCREENS);
        }

        @NonNull
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
                case AUTO_SCREEN:
                    fragment = AutoMatchFragment.newInstance();
                    fragments.put(i, fragment);
                    return fragment;
                case TELE_SCREEN:
                    fragment = TeleMatchFragment.newInstance();
                    fragments.put(i, fragment);
                    return fragment;
				case CLIMB_SCREEN:
					fragment = ClimbFragment.newInstance();
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

    private void loadClimb() {
        mMatchViewAdapter.getMatchFragment(CLIMB_SCREEN).loadData(teamData);
    }

    private void saveClimb() {
        saveTeamInfo();
        mMatchViewAdapter.getMatchFragment(CLIMB_SCREEN).saveData(teamData);
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
            teamData.team_id = Integer.parseInt(team.toString());
        Editable match = matchT.getText();
        if (match != null && match.length() > 0) {
            teamData.match_id = Integer.parseInt(match.toString());
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
                    setResult(Integer.parseInt(matchT.getText().toString()) + 1);
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
