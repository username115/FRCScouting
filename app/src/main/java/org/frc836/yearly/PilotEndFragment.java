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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.frc836.database.PilotStatsStruct;
import org.growingstems.scouting.R;

import java.util.ArrayList;
import java.util.List;


public class PilotEndFragment extends PilotFragment {

    private boolean displayed = false;

    private Spinner[] commonNotes;

    private Spinner[] pastNotes;

    private TextView[] teamT;

    public PilotEndFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PreMatch.
     */
    public static PilotEndFragment newInstance() {
        return new PilotEndFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pilot_end, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        displayed = true;
        commonNotes = new Spinner[2];
        EditText[] teamNotes = new EditText[2];
        pastNotes = new Spinner[2];
        teamT = new TextView[2];
        commonNotes[0] = (Spinner) view.findViewById(R.id.commonNotes1);
        teamNotes[0] = (EditText)getView().findViewById(R.id.notes1);
        pastNotes[0] = (Spinner)getView().findViewById(R.id.previousNotes1);
        commonNotes[1] = (Spinner) view.findViewById(R.id.commonNotes2);
        teamNotes[1] = (EditText)getView().findViewById(R.id.notes2);
        pastNotes[1] = (Spinner)getView().findViewById(R.id.previousNotes2);
        commonNotes[0].setOnItemSelectedListener(new NotesSelectedListener(teamNotes[0]));
        pastNotes[0].setOnItemSelectedListener(new NotesSelectedListener(teamNotes[0]));
        commonNotes[1].setOnItemSelectedListener(new NotesSelectedListener(teamNotes[1]));
        pastNotes[1].setOnItemSelectedListener(new NotesSelectedListener(teamNotes[1]));

        teamT[0] = (TextView) getView().findViewById(R.id.team1T);
        teamT[1] = (TextView) getView().findViewById(R.id.team2T);

    }

    public void onResume() {
        super.onResume();

        Activity a = getActivity();

        if (a instanceof PilotActivity) {
            PilotActivity match = (PilotActivity) a;
            List<String> options = match.getNotesOptions();
            List<String> teamOptions1 = match.getTeamNotes(match.getTeam(0));
            List<String> teamOptions2 = match.getTeamNotes(match.getTeam(1));

            if (options == null)
                options = new ArrayList<String>(1);

            if (teamOptions1 == null)
                teamOptions1 = new ArrayList<String>(1);
            if (teamOptions2 == null)
                teamOptions2 = new ArrayList<String>(1);

            options.add(0, commonNotes[0].getItemAtPosition(0).toString());
            teamOptions1.add(0, pastNotes[0].getItemAtPosition(0).toString());
            teamOptions2.add(0, pastNotes[0].getItemAtPosition(0).toString());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(match,
                    android.R.layout.simple_spinner_item, options);

            ArrayAdapter<String> adapterTeam1 = new ArrayAdapter<String>(match,
                    android.R.layout.simple_spinner_item, teamOptions1);
            ArrayAdapter<String> adapterTeam2 = new ArrayAdapter<String>(match,
                    android.R.layout.simple_spinner_item, teamOptions2);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterTeam1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterTeam2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            commonNotes[0].setAdapter(adapter);
            pastNotes[0].setAdapter(adapterTeam1);
            commonNotes[1].setAdapter(adapter);
            pastNotes[1].setAdapter(adapterTeam2);

            teamT[0].setText(String.valueOf(match.getTeam(0)));
            teamT[1].setText(String.valueOf(match.getTeam(1)));
        }
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public void saveData(PilotStatsStruct[] data) {
        if (getView() == null || data == null || !displayed)
            return;
        data[0].notes = ((EditText)getView().findViewById(R.id.notes1)).getText().toString();
        data[0].foul = ((CheckBox)getView().findViewById(R.id.foul1)).isChecked();
        data[0].yellow_card = ((CheckBox)getView().findViewById(R.id.yellow_card1)).isChecked();
        data[0].red_card = ((CheckBox)getView().findViewById(R.id.red_card1)).isChecked();

        data[1].notes = ((EditText)getView().findViewById(R.id.notes2)).getText().toString();
        data[1].foul = ((CheckBox)getView().findViewById(R.id.foul2)).isChecked();
        data[1].yellow_card = ((CheckBox)getView().findViewById(R.id.yellow_card2)).isChecked();
        data[1].red_card = ((CheckBox)getView().findViewById(R.id.red_card2)).isChecked();
    }

    @Override
    public void loadData(PilotStatsStruct[] data) {
        if (getView() == null || data == null || !displayed)
            return;
        ((EditText)getView().findViewById(R.id.notes1)).setText(data[0].notes);
        ((CheckBox)getView().findViewById(R.id.foul1)).setChecked(data[0].foul);
        ((CheckBox)getView().findViewById(R.id.red_card1)).setChecked(data[0].red_card);
        ((CheckBox)getView().findViewById(R.id.yellow_card1)).setChecked(data[0].yellow_card);

        ((EditText)getView().findViewById(R.id.notes2)).setText(data[1].notes);
        ((CheckBox)getView().findViewById(R.id.foul2)).setChecked(data[1].foul);
        ((CheckBox)getView().findViewById(R.id.red_card2)).setChecked(data[1].red_card);
        ((CheckBox)getView().findViewById(R.id.yellow_card2)).setChecked(data[1].yellow_card);

        ((TextView)getView().findViewById(R.id.team1T)).setText(String.valueOf(data[0].team_id));
        ((TextView)getView().findViewById(R.id.team2T)).setText(String.valueOf(data[1].team_id));
    }

    public class NotesSelectedListener implements AdapterView.OnItemSelectedListener {

        EditText notes;

        NotesSelectedListener(EditText n) {
            notes = n;
        }

        public void onItemSelected(AdapterView<?> parent, View v, int position,
                                   long id) {
            if (position == 0 || !(parent instanceof Spinner))
                return;
            Spinner par = (Spinner) parent;
            String note = notes.getText().toString();
            if (!note.contains(par.getItemAtPosition(position).toString())) {
                if (!note.trim().equals(""))
                    note = note + "; ";
                note = note + par.getItemAtPosition(position);
                notes.setText(note);
            }
            par.setSelection(0);

        }

        public void onNothingSelected(AdapterView<?> parent) {
        }

    }

}
