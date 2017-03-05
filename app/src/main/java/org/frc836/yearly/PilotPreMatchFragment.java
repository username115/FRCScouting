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

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.frc836.database.PilotStatsStruct;
import org.growingstems.scouting.R;

import java.util.ArrayList;
import java.util.List;


public class PilotPreMatchFragment extends PilotFragment {


    private Spinner[] teamS;
    private EditText[] teamT;

    private ArrayList<Integer> teamList;


    private PilotStatsStruct[] tempData = new PilotStatsStruct[2];

    private boolean displayed = false;


    public PilotPreMatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PreMatch.
     */
    public static PilotPreMatchFragment newInstance() {
        return new PilotPreMatchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        teamS = new Spinner[2];
        teamT = new EditText[2];
        if (tempData[0] == null)
            tempData[0] = new PilotStatsStruct();
        if (tempData[1] == null)
            tempData[1] = new PilotStatsStruct();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.prematch_pilot, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getGUIRefs(view);
        setListeners();
        displayed = true;
    }

    @Override
    public void onResume() {
        super.onResume();

        Activity a = getActivity();

        teamList = new ArrayList<Integer>(3);

        if (a instanceof PilotActivity) {
            PilotActivity match = (PilotActivity) a;
            List<String> teams = match.getTeams();

            if (teams == null)
                teams = new ArrayList<String>(1);

            for (String team: teams) {
                teamList.add(Integer.valueOf(team));
            }

            teams.add(0, teamS[0].getItemAtPosition(0).toString());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(match,
                    android.R.layout.simple_spinner_item, teams);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            teamS[0].setAdapter(adapter);
            teamS[1].setAdapter(adapter);
        }

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
        for (int i=0; i<2; i++) {
            if (teamS[i].getSelectedItemPosition() == 0 && teamT[i].getText().toString().length() > 0) {
                data[i].team_id = Integer.valueOf(teamT[i].getText().toString());
            } else {
                data[i].team_id = teamList.get(teamS[i].getSelectedItemPosition()-1);
            }
        }
    }

    @Override
    public void loadData(PilotStatsStruct[] data) {
        tempData = data;
        if (getView() == null || data == null || !displayed)
            return;
        for (int i=0; i<2; i++) {
            int index = teamList.indexOf(data[i].team_id);
            if (index < 0) {
                teamS[i].setSelection(0);
                setTeamIndex(i, 0);
                if (data[i].team_id > 0)
                    teamT[i].setText(String.valueOf(data[i].team_id));
                else
                    teamT[i].setText("");
            } else {
                teamS[i].setSelection(index + 1);
                setTeamIndex(i, index + 1);
            }
        }
    }

    private void getGUIRefs(View view) {
        teamS[0] = (Spinner) view.findViewById(R.id.pilotTeam1S);
        teamS[1] = (Spinner) view.findViewById(R.id.pilotTeam2S);

        teamT[0] = (EditText) view.findViewById(R.id.pilotTeam1T);
        teamT[1] = (EditText) view.findViewById(R.id.pilotTeam2T);
    }

    private void setListeners() {
        teamS[0].setOnItemSelectedListener(new OnTeamSelectedListener());
        teamS[1].setOnItemSelectedListener(new OnTeamSelectedListener());
    }

    private void setTeamIndex(int viewNumber, int index) {
        if (index == 0) {
            teamT[viewNumber].setVisibility(View.VISIBLE);
        } else {
            teamT[viewNumber].setVisibility(View.GONE);
        }
    }

    private class OnTeamSelectedListener implements AdapterView.OnItemSelectedListener {

        /**
         * <p>Callback method to be invoked when an item in this view has been
         * selected. This callback is invoked only when the newly selected
         * position is different from the previously selected position or if
         * there was no selected item.</p>
         * <p>
         * Impelmenters can call getItemAtPosition(position) if they need to access the
         * data associated with the selected item.
         *
         * @param parent   The AdapterView where the selection happened
         * @param view     The view within the AdapterView that was clicked
         * @param position The position of the view in the adapter
         * @param id       The row id of the item that is selected
         */
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.pilotTeam1S:
                    setTeamIndex(0, position);
                    break;
                case R.id.pilotTeam2S:
                default:
                    setTeamIndex(1, position);
                    break;
            }

        }

        /**
         * Callback method to be invoked when the selection disappears from this
         * view. The selection can disappear for instance when touch is activated
         * or when the adapter becomes empty.
         *
         * @param parent The AdapterView that now contains no selected item.
         */
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}
