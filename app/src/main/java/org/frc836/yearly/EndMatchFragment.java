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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import org.frc836.database.MatchStatsStruct;
import org.growingstems.scouting.MatchFragment;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;
import org.growingstems.scouting.SuperImageButton;
import org.growingstems.scouting.TransparentImageButton;

import java.util.ArrayList;
import java.util.List;


public class EndMatchFragment extends MatchFragment {

    private boolean displayed = false;

    private final MatchStatsStruct tempData = new MatchStatsStruct();

    private Spinner commonNotes;

    private Spinner pastNotes;

    private EditText teamNotes;

    //TODO store GUI references

    public EndMatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EndMatch.
     */
    public static EndMatchFragment newInstance() {
        return new EndMatchFragment();
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
        commonNotes = view.findViewById(R.id.commonNotes);
        teamNotes = view.findViewById(R.id.notes);
        pastNotes = view.findViewById(R.id.previousNotes);
        commonNotes.setOnItemSelectedListener(new NotesSelectedListener());
        pastNotes.setOnItemSelectedListener(new NotesSelectedListener());

        //TODO get GUI references

    }


    public void onResume() {
        super.onResume();

        Activity a = getActivity();

        if (a instanceof MatchActivity) {
            MatchActivity match = (MatchActivity) a;
            List<String> options = match.getNotesOptions();
            List<String> teamOptions = match.getTeamNotes();

            if (options == null)
                options = new ArrayList<>(1);

            if (teamOptions == null)
                teamOptions = new ArrayList<>(1);

            options.add(0, commonNotes.getItemAtPosition(0).toString());
            teamOptions.add(0, pastNotes.getItemAtPosition(0).toString());

            ArrayAdapter<String> adapter = new ArrayAdapter<>(match,
                android.R.layout.simple_spinner_item, options);

            ArrayAdapter<String> adapterTeam = new ArrayAdapter<>(match,
                android.R.layout.simple_spinner_item, teamOptions);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterTeam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            commonNotes.setAdapter(adapter);
            pastNotes.setAdapter(adapterTeam);
        }

        loadData(tempData);
    }

    public void onPause() {
        super.onPause();

        saveData(tempData);
    }

    @Override
    public void saveData(@NonNull MatchStatsStruct data) {
        if (getView() == null || !displayed)
            return;
        data.notes = ((EditText) getView().findViewById(R.id.notes)).getText().toString();
        data.yellow_card = ((CheckBox) getView().findViewById(R.id.yellow_card)).isChecked();
        data.red_card = ((CheckBox) getView().findViewById(R.id.red_card)).isChecked();
        //TODO savedata
    }

    @Override
    public void loadData(@NonNull MatchStatsStruct data) {
        if (getView() == null || !displayed)
            return;

        // which side are we using
        boolean redLeft = Prefs.getRedLeft(getActivity(), true);

        Activity act = getActivity();
        String pos;
        if (act instanceof MatchActivity)
            pos = ((MatchActivity) act).getPosition();
        else
            pos = Prefs.getPosition(getActivity(), "Red 1");

        boolean blue = pos.contains("Blue");

        ((EditText) getView().findViewById(R.id.notes)).setText(data.notes);
        ((CheckBox) getView().findViewById(R.id.red_card)).setChecked(data.red_card);
        ((CheckBox) getView().findViewById(R.id.yellow_card)).setChecked(data.yellow_card);

        //TODO load data

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
