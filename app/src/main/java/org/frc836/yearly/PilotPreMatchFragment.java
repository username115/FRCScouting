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

        if (a instanceof PilotActivity) {
            PilotActivity match = (PilotActivity) a;
            List<String> teams = match.getTeams();

            if (teams == null)
                teams = new ArrayList<String>(1);

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

        // TODO

    }

    @Override
    public void loadData(PilotStatsStruct[] data) {
        tempData = data;
        if (getView() == null || data == null || !displayed)
            return;
        // TODO
    }

    private void getGUIRefs(View view) {
        teamS[0] = (Spinner) view.findViewById(R.id.pilotTeam1S);
        teamS[1] = (Spinner) view.findViewById(R.id.pilotTeam2S);

        teamT[0] = (EditText) view.findViewById(R.id.pilotTeam1T);
        teamT[1] = (EditText) view.findViewById(R.id.pilotTeam2T);
    }

    private void setListeners() {

        // TODO

    }

}
