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

    private CheckBox allyOutfield;
    private CheckBox allyTarmac;
    private CheckBox oppOutfield;
    private CheckBox oppTarmac;

    private ImageView fieldView;

    private ImageView leftOutfieldSelect;
    private ImageView leftTarmacSelect;
    private ImageView rightOutfieldSelect;
    private ImageView rightTarmacSelect;

    private boolean allyOnLeft = false;

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

        allyOutfield = view.findViewById(R.id.ally_outfield);
        allyTarmac = view.findViewById(R.id.ally_tarmac);
        oppOutfield = view.findViewById(R.id.opp_outfield);
        oppTarmac = view.findViewById(R.id.opp_tarmac);

        fieldView = view.findViewById(R.id.fieldView);

        TransparentImageButton leftOutfieldRegion = view.findViewById(R.id.leftOutfieldRegion);
        TransparentImageButton leftTarmacRegion = view.findViewById(R.id.leftTarmacRegion);
        TransparentImageButton rightOutfieldRegion = view.findViewById(R.id.rightOutfieldRegion);
        TransparentImageButton rightTarmacRegion = view.findViewById(R.id.rightTarmacRegion);

        SuperImageButton transparentField = view.findViewById(R.id.fieldimage_transparent);

        leftOutfieldSelect = view.findViewById(R.id.leftOutfieldSelect);
        leftTarmacSelect = view.findViewById(R.id.leftTarmacSelect);
        rightOutfieldSelect = view.findViewById(R.id.rightOutfieldSelect);
        rightTarmacSelect = view.findViewById(R.id.rightTarmacSelect);

        leftOutfieldRegion.setOnClickListener(v -> {
            if (allyOnLeft) {
                allyOutfield.toggle();
            } else {
                oppOutfield.toggle();
            }
        });

        leftTarmacRegion.setOnClickListener(v -> {
            if (allyOnLeft) {
                allyTarmac.toggle();
            } else {
                oppTarmac.toggle();
            }
        });

        rightOutfieldRegion.setOnClickListener(v -> {
            if (!allyOnLeft) {
                allyOutfield.toggle();
            } else {
                oppOutfield.toggle();
            }
        });

        rightTarmacRegion.setOnClickListener(v -> {
            if (!allyOnLeft) {
                allyTarmac.toggle();
            } else {
                oppTarmac.toggle();
            }
        });

        transparentField.clearImageButtons();
        transparentField.addImageButton(leftOutfieldRegion);
        transparentField.addImageButton(leftTarmacRegion);
        transparentField.addImageButton(rightOutfieldRegion);
        transparentField.addImageButton(rightTarmacRegion);

        allyOutfield.setOnCheckedChangeListener((buttonView, isChecked) -> updateFieldImage(true, false, isChecked));

        allyTarmac.setOnCheckedChangeListener((buttonView, isChecked) -> updateFieldImage(true, true, isChecked));

        oppOutfield.setOnCheckedChangeListener((buttonView, isChecked) -> updateFieldImage(false, false, isChecked));

        oppTarmac.setOnCheckedChangeListener((buttonView, isChecked) -> updateFieldImage(false, true, isChecked));

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
        data.tech_foul = ((CheckBox) getView().findViewById(R.id.tech_foul)).isChecked();

        data.opp_tarmac = oppTarmac.isChecked();
        data.opp_outfield = oppOutfield.isChecked();
        data.ally_tarmac = allyTarmac.isChecked();
        data.ally_outfield = allyOutfield.isChecked();

        data.fender_usage = ((CheckBox) getView().findViewById(R.id.fender_usage)).isChecked();
        data.launchpad_usage = ((CheckBox) getView().findViewById(R.id.launchpad_usage)).isChecked();
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

        allyOnLeft = blue ^ redLeft;

        fieldView.setScaleY(redLeft ? -1f : 1f);
        fieldView.setScaleX(redLeft ? -1f : 1f);

        ((EditText) getView().findViewById(R.id.notes)).setText(data.notes);
        ((CheckBox) getView().findViewById(R.id.red_card)).setChecked(data.red_card);
        ((CheckBox) getView().findViewById(R.id.yellow_card)).setChecked(data.yellow_card);
        ((CheckBox) getView().findViewById(R.id.tech_foul)).setChecked(data.tech_foul);

        oppTarmac.setChecked(data.opp_tarmac);
        oppOutfield.setChecked(data.opp_outfield);
        allyTarmac.setChecked(data.ally_tarmac);
        allyOutfield.setChecked(data.ally_outfield);
        //need to manually update field images because onChecked callbacks are not called if checked status did not change
        updateFieldImage(false, false, data.opp_outfield);
        updateFieldImage(false, true, data.opp_tarmac);
        updateFieldImage(true, false, data.ally_outfield);
        updateFieldImage(true, true, data.ally_tarmac);

        ((CheckBox) getView().findViewById(R.id.fender_usage)).setChecked(data.fender_usage);
        ((CheckBox) getView().findViewById(R.id.launchpad_usage)).setChecked(data.launchpad_usage);

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

    private void updateFieldImage(boolean ally, boolean tarmac, boolean isChecked) {
        if (ally) {
            if (tarmac) {
                if (allyOnLeft) {
                    leftTarmacSelect.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                } else {
                    rightTarmacSelect.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                }
            } else {
                if (allyOnLeft) {
                    leftOutfieldSelect.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                } else {
                    rightOutfieldSelect.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                }
            }
        } else {
            if (tarmac) {
                if (!allyOnLeft) {
                    leftTarmacSelect.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                } else {
                    rightTarmacSelect.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                }
            } else {
                if (!allyOnLeft) {
                    leftOutfieldSelect.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                } else {
                    rightOutfieldSelect.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                }
            }
        }

    }

}
