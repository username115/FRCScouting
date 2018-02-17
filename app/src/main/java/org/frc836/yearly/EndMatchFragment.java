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
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import org.frc836.database.MatchStatsStruct;
import org.growingstems.scouting.MatchFragment;
import org.growingstems.scouting.R;

import java.util.ArrayList;
import java.util.List;


public class EndMatchFragment extends MatchFragment {

    private boolean displayed = false;

    private Spinner commonNotes;

    private Spinner pastNotes;

    private EditText teamNotes;

    public EndMatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PreMatch.
     */
    public static EndMatchFragment newInstance() {
        EndMatchFragment fragment = new EndMatchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_end, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        displayed = true;
        commonNotes = (Spinner) view.findViewById(R.id.commonNotes);
        teamNotes = (EditText)getView().findViewById(R.id.notes);
        pastNotes = (Spinner)getView().findViewById(R.id.previousNotes);
        commonNotes.setOnItemSelectedListener(new NotesSelectedListener());
        pastNotes.setOnItemSelectedListener(new NotesSelectedListener());
    }

    public void onResume() {
        super.onResume();

        Activity a = getActivity();

        if (a instanceof MatchActivity) {
            MatchActivity match = (MatchActivity) a;
            List<String> options = match.getNotesOptions();
            List<String> teamOptions = match.getTeamNotes();

            if (options == null)
                options = new ArrayList<String>(1);

            if (teamOptions == null)
                teamOptions = new ArrayList<String>(1);

            options.add(0, commonNotes.getItemAtPosition(0).toString());
            teamOptions.add(0, pastNotes.getItemAtPosition(0).toString());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(match,
                    android.R.layout.simple_spinner_item, options);

            ArrayAdapter<String> adapterTeam = new ArrayAdapter<String>(match,
                    android.R.layout.simple_spinner_item, teamOptions);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterTeam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            commonNotes.setAdapter(adapter);
            pastNotes.setAdapter(adapterTeam);
        }
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public void saveData(MatchStatsStruct data) {
        if (getView() == null || data == null || !displayed)
            return;
        data.climb_attempt = ((CheckBox)getView().findViewById(R.id.climb_end_attempt)).isChecked();
        data.climbed = ((CheckBox)getView().findViewById(R.id.climb_end)).isChecked();
        data.notes = ((EditText)getView().findViewById(R.id.notes)).getText().toString();
        data.tip_over = ((CheckBox)getView().findViewById(R.id.botTip)).isChecked();
        data.foul = ((CheckBox)getView().findViewById(R.id.foul)).isChecked();
        data.yellow_card = ((CheckBox)getView().findViewById(R.id.yellow_card)).isChecked();
        data.red_card = ((CheckBox)getView().findViewById(R.id.red_card)).isChecked();
        data.supported_others = ((CheckBox)getView().findViewById(R.id.assisted_climb)).isChecked();
    }

    @Override
    public void loadData(MatchStatsStruct data) {
        if (getView() == null || data == null || !displayed)
            return;
        ((CheckBox)getView().findViewById(R.id.climb_end_attempt)).setChecked(data.climb_attempt);
        ((CheckBox)getView().findViewById(R.id.climb_end)).setChecked(data.climbed);
        ((EditText)getView().findViewById(R.id.notes)).setText(data.notes);
        ((CheckBox)getView().findViewById(R.id.botTip)).setChecked(data.tip_over);
        ((CheckBox)getView().findViewById(R.id.foul)).setChecked(data.foul);
        ((CheckBox)getView().findViewById(R.id.red_card)).setChecked(data.red_card);
        ((CheckBox)getView().findViewById(R.id.yellow_card)).setChecked(data.yellow_card);
        ((CheckBox)getView().findViewById(R.id.assisted_climb)).setChecked(data.supported_others);
    }

    public class NotesSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View v, int position,
                                   long id) {
            if (position == 0 || !(parent instanceof Spinner))
                return;
            Spinner par = (Spinner) parent;
            String note = teamNotes.getText().toString();
            if (!note.contains(par.getItemAtPosition(position).toString())) {
                if (!note.trim().equals(""))
                    note = note + "; ";
                note = note + par.getItemAtPosition(position);
                teamNotes.setText(note);
            }
            par.setSelection(0);

        }

        public void onNothingSelected(AdapterView<?> parent) {
        }

    }

}
