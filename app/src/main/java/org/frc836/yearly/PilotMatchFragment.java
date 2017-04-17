/*
 * Copyright 2016 Daniel Logan
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

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageSwitcher;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.frc836.database.DB;
import org.frc836.database.PilotStatsStruct;
import org.frc836.database.ScoutingDBHelper;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;


public class PilotMatchFragment extends PilotFragment {


    private PilotStatsStruct[] tempData = new PilotStatsStruct[2];

    private boolean displayed = false;

    private TextView[] teamT = new TextView[2];
    private Spinner[] liftS = new Spinner[2];
    private Button[] liftB = new Button[2];
    private Spinner[] dropS = new Spinner[2];
    private Button[] dropB = new Button[2];

    private CheckBox[][] rotorC = {new CheckBox[2], new CheckBox[2], new CheckBox[2], new CheckBox[2]};
    private Spinner[][] gearInstallS = {new Spinner[2], new Spinner[2], new Spinner[2], new Spinner[2]};
    private Button[][] gearinstallB = {new Button[2], new Button[2], new Button[2], new Button[2]};

    private ImageSwitcher[] diags = new ImageSwitcher[4];
    private ImageSwitcher[] sides = new ImageSwitcher[2];

    private RelativeLayout airship;

    private LinearLayout[] internals = new LinearLayout[4];

    private int rotorMax[] = new int[4];


    public PilotMatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment.
     */
    public static PilotMatchFragment newInstance() {
        return new PilotMatchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (tempData[0] == null)
            tempData[0] = new PilotStatsStruct();
        if (tempData[1] == null)
            tempData[1] = new PilotStatsStruct();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pilot, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getGUIRefs(view);
        setListeners();
        SQLiteDatabase db = ScoutingDBHelper.getInstance()
                .getReadableDatabase();

        rotorMax[0] = 1;
        rotorMax[1] = 2;
        rotorMax[2] = 5 - DB.getGameInfoInt("2017_rotor_3_preinstalled", db, 1);
        rotorMax[3] = 8 - DB.getGameInfoInt("2017_rotor_4_preinstalled", db, 2);
        displayed = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(tempData);
    }

    public void onPause() {
        super.onPause();
        saveData(tempData);
    }

    @Override
    public void saveData(PilotStatsStruct[] data) {
        if (getView() == null || data == null || !displayed)
            return;

        for (int i = 0; i < 2; i++) {
            data[i].gears_lifted = liftS[i].getSelectedItemPosition();
            data[i].gears_dropped = dropS[i].getSelectedItemPosition();
            data[i].gears_installed_2 = gearInstallS[1][i].getSelectedItemPosition();
            data[i].gears_installed_3 = gearInstallS[2][i].getSelectedItemPosition();
            data[i].gears_installed_4 = gearInstallS[3][i].getSelectedItemPosition();
            data[i].rotor_1_started = rotorC[0][i].isChecked();
            data[i].rotor_2_started = rotorC[1][i].isChecked();
            data[i].rotor_3_started = rotorC[2][i].isChecked();
            data[i].rotor_4_started = rotorC[3][i].isChecked();
        }
    }

    @Override
    public void loadData(PilotStatsStruct[] data) {
        tempData = data;
        if (getView() == null || data == null || !displayed)
            return;
        // which side are we using
        boolean redLeft = Prefs.getRedLeft(getActivity(), true);

        Activity act = getActivity();
        String pos;
        if (act instanceof PilotActivity)
            pos = ((PilotActivity) act).getPosition();
        else
            pos = Prefs.getPosition(getActivity(), "Red 1");

        if (pos.contains("Blue")) {
            for (int i = 0; i < 4; i++) {
                diags[i].setBackgroundResource(R.drawable.blue_diag);
            }
            for (int i = 0; i < 2; i++) {
                sides[i].setBackgroundResource(R.drawable.blue_square);
            }
        } else {
            for (int i = 0; i < 4; i++) {
                diags[i].setBackgroundResource(R.drawable.red_diag);
            }
            for (int i = 0; i < 2; i++) {
                sides[i].setBackgroundResource(R.drawable.red_square);
            }
        }

        if ((pos.contains("Blue") && !redLeft) || ((!pos.contains("Blue") && redLeft))) {
            airship.setScaleX(1.0f);
            airship.setScaleY(1.0f);
            for (int i = 0; i < 4; i++) {
                internals[i].setScaleX(1.0f);
                internals[i].setScaleY(1.0f);
            }
        } else {
            airship.setScaleX(-1.0f);
            airship.setScaleY(-1.0f);
            for (int i = 0; i < 4; i++) {
                internals[i].setScaleX(-1.0f);
                internals[i].setScaleY(-1.0f);
            }
        }

        updateGearInstall(1, 0, data[0].gears_installed_2);
        updateGearInstall(1, 1, data[1].gears_installed_2);
        updateGearInstall(2, 0, data[0].gears_installed_3);
        updateGearInstall(2, 1, data[1].gears_installed_3);
        updateGearInstall(3, 0, data[0].gears_installed_4);
        updateGearInstall(3, 1, data[1].gears_installed_4);

        for (int i = 0; i < 2; i++) {
            teamT[i].setText(String.valueOf(data[i].team_id));

            liftS[i].setSelection(data[i].gears_lifted);
            dropS[i].setSelection(data[i].gears_dropped);

            rotorC[0][i].setChecked(data[i].rotor_1_started);
            rotorC[1][i].setChecked(data[i].rotor_2_started);
            rotorC[2][i].setChecked(data[i].rotor_3_started);
            rotorC[3][i].setChecked(data[i].rotor_4_started);
        }
    }

    private void getGUIRefs(View view) {

        teamT[0] = (TextView) view.findViewById(R.id.pilotTeam1T_tele);
        teamT[1] = (TextView) view.findViewById(R.id.pilotTeam2T_tele);

        liftS[0] = (Spinner) view.findViewById(R.id.team1LiftCount);
        liftS[1] = (Spinner) view.findViewById(R.id.team2LiftCount);

        liftB[0] = (Button) view.findViewById(R.id.team1LiftButton);
        liftB[1] = (Button) view.findViewById(R.id.team2LiftButton);

        dropS[0] = (Spinner) view.findViewById(R.id.team1DropCount);
        dropS[1] = (Spinner) view.findViewById(R.id.team2DropCount);

        dropB[0] = (Button) view.findViewById(R.id.team1DropButton);
        dropB[1] = (Button) view.findViewById(R.id.team2DropButton);

        rotorC[0][0] = (CheckBox) view.findViewById(R.id.team1TurnR1);
        rotorC[0][1] = (CheckBox) view.findViewById(R.id.team2TurnR1);
        rotorC[1][0] = (CheckBox) view.findViewById(R.id.team1TurnR2);
        rotorC[1][1] = (CheckBox) view.findViewById(R.id.team2TurnR2);
        rotorC[2][0] = (CheckBox) view.findViewById(R.id.team1TurnR3);
        rotorC[2][1] = (CheckBox) view.findViewById(R.id.team2TurnR3);
        rotorC[3][0] = (CheckBox) view.findViewById(R.id.team1TurnR4);
        rotorC[3][1] = (CheckBox) view.findViewById(R.id.team2TurnR4);


        gearInstallS[1][0] = (Spinner) view.findViewById(R.id.team1R2Gears);
        gearInstallS[1][1] = (Spinner) view.findViewById(R.id.team2R2Gears);
        gearInstallS[2][0] = (Spinner) view.findViewById(R.id.team1R3Gears);
        gearInstallS[2][1] = (Spinner) view.findViewById(R.id.team2R3Gears);
        gearInstallS[3][0] = (Spinner) view.findViewById(R.id.team1R4Gears);
        gearInstallS[3][1] = (Spinner) view.findViewById(R.id.team2R4Gears);

        gearinstallB[1][0] = (Button) view.findViewById(R.id.team1R2GearB);
        gearinstallB[1][1] = (Button) view.findViewById(R.id.team2R2GearB);
        gearinstallB[2][0] = (Button) view.findViewById(R.id.team1R3GearB);
        gearinstallB[2][1] = (Button) view.findViewById(R.id.team2R3GearB);
        gearinstallB[3][0] = (Button) view.findViewById(R.id.team1R4GearB);
        gearinstallB[3][1] = (Button) view.findViewById(R.id.team2R4GearB);

        diags[0] = (ImageSwitcher) view.findViewById(R.id.upperLeftAirship1);
        diags[1] = (ImageSwitcher) view.findViewById(R.id.upperRightAirship1);
        diags[2] = (ImageSwitcher) view.findViewById(R.id.lowerLeftAirship1);
        diags[3] = (ImageSwitcher) view.findViewById(R.id.lowerRightAirship1);

        sides[0] = (ImageSwitcher) view.findViewById(R.id.midLeftAirship1);
        sides[1] = (ImageSwitcher) view.findViewById(R.id.midRightAirship1);

        airship = (RelativeLayout) view.findViewById(R.id.airshipLayout);

        internals[0] = (LinearLayout) view.findViewById(R.id.R1Layout);
        internals[1] = (LinearLayout) view.findViewById(R.id.R2Layout);
        internals[2] = (LinearLayout) view.findViewById(R.id.R3Layout);
        internals[3] = (LinearLayout) view.findViewById(R.id.R4Layout);
    }

    private void setListeners() {
        for (int i = 0; i < 2; i++) {
            liftB[i].setOnClickListener(new OnIncrementListener(liftS[i], 1));
            dropB[i].setOnClickListener(new OnIncrementListener(dropS[i], 1));

            for (int j = 1; j < 4; j++) {
                gearinstallB[j][i].setOnClickListener(new OnGearInstallListener(j, i, 1));
                gearInstallS[j][i].setOnItemSelectedListener(new OnGearInstallListener(j, i, 1));
            }
        }
    }

    private void updateGearInstall(int rotor, int team, int count) {
        gearInstallS[rotor][team].setSelection(count);

        int installed = gearInstallS[rotor][0].getSelectedItemPosition() + gearInstallS[rotor][1].getSelectedItemPosition();

        if (installed >= rotorMax[rotor]) {
            if (installed > rotorMax[rotor]) {
                gearInstallS[rotor][team].setSelection(rotorMax[rotor] - gearInstallS[rotor][(team + 1) % 2].getSelectedItemPosition());
            }
            for (int i = 0; i < 2; i++) {
                rotorC[rotor][i].setEnabled(true);
                gearinstallB[rotor][i].setEnabled(false);
                if (rotor < 3) {
                    gearInstallS[rotor + 1][i].setClickable(true);
                    gearinstallB[rotor + 1][i].setEnabled(true);
                }
            }
        } else {
            for (int i = 0; i < 2; i++) {
                rotorC[rotor][i].setEnabled(false);
                rotorC[rotor][i].setChecked(false);
                if (rotor > 1)
                    gearinstallB[rotor][i].setEnabled((gearInstallS[rotor - 1][0].getSelectedItemPosition() + gearInstallS[rotor - 1][1].getSelectedItemPosition()) >= rotorMax[rotor - 1]);
                else
                    gearinstallB[rotor][i].setEnabled(true);
                if (rotor < 3) {
                    gearInstallS[rotor + 1][i].setClickable(false);
                    gearInstallS[rotor + 1][i].setSelection(0);
                    gearinstallB[rotor + 1][i].setEnabled(false);
                }
            }
        }

    }

    private class OnIncrementListener implements View.OnClickListener {

        int m_increment = 1;
        Spinner m_spinner;

        OnIncrementListener(Spinner view, int inc) {
            super();
            m_increment = inc;
            m_spinner = view;
        }

        @Override
        public void onClick(View v) {
            int actual = m_spinner.getSelectedItemPosition() + m_increment;
            m_spinner.setSelection(actual);
        }
    }

    private class OnGearInstallListener implements View.OnClickListener, AdapterView.OnItemSelectedListener {

        int m_increment = 1;
        int m_rotor = 1;
        int m_team = 0;

        OnGearInstallListener(int rotor, int team, int inc) {
            super();
            m_increment = inc;
            m_rotor = rotor;
            m_team = team;
        }

        @Override
        public void onClick(View v) {
            int new_score = gearInstallS[m_rotor][m_team].getSelectedItemPosition() + m_increment;
            updateGearInstall(m_rotor, m_team, new_score);
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            updateGearInstall(m_rotor, m_team, position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
