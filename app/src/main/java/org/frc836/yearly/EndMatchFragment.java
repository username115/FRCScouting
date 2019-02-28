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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;

import org.frc836.database.MatchStatsStruct;
import org.growingstems.scouting.MatchFragment;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;

import java.util.ArrayList;
import java.util.List;


public class EndMatchFragment extends MatchFragment {

    private boolean displayed = false;

    private Spinner commonNotes;

    private Spinner pastNotes;

    private EditText teamNotes;

    private MatchStatsStruct tempData = new MatchStatsStruct();


    private ImageButton hab1;
    private ImageButton hab2L;
    private ImageButton hab2R;
    private ImageButton hab3;

    private FrameLayout Lhab1;
    private FrameLayout Lhab2L;
    private FrameLayout Lhab2R;
    private FrameLayout Lhab3;

    private View mainView;


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
        mainView = view;
        hab1 = view.findViewById(R.id.hab1);
        hab2L = view.findViewById(R.id.hab2Left);
        hab2R = view.findViewById(R.id.hab2Right);
        hab3 = view.findViewById(R.id.hab3);

        Lhab1 = view.findViewById(R.id.hab1L);
        Lhab2L = view.findViewById(R.id.hab2LeftL);
        Lhab2R = view.findViewById(R.id.hab2RightL);
        Lhab3 = view.findViewById(R.id.hab3L);


        displayed = true;
        commonNotes = view.findViewById(R.id.commonNotes);
        teamNotes = view.findViewById(R.id.notes);
        pastNotes = view.findViewById(R.id.previousNotes);
        commonNotes.setOnItemSelectedListener(new NotesSelectedListener());
        pastNotes.setOnItemSelectedListener(new NotesSelectedListener());


        hab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleHabLevel(1, false);
            }
        });
        hab2R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleHabLevel(2, false);
            }
        });
        hab2L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleHabLevel(2, true);
            }
        });
        hab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleHabLevel(3, false);
            }
        });
    }


    private void toggleHabLevel(int level, boolean left2) {
        //if no climb level attempted yet, set attempted to this level.
        //if this particular hab position is currently set to attempted, set to climbed.
        //if this particular hab position is currently climbed, set climbed to none
        //if there is a hab level attempted/climbed other than current selection:
        //  Set this one to climbed if below or equal level to prior attempt
        //  Change attempted to this one if it is above prior attempt


        if (tempData.hab_climb_level == 0 && tempData.hab_climb_level_attempted == 0) {
            //if no climb level attempted yet, set attempted to this level.
            tempData.hab_climb_level_attempted = level;
            tempData.hab_climb_2_left_attempted = left2;
        } else if (tempData.hab_climb_level_attempted == level && (tempData.hab_climb_2_left_attempted == left2 || level != 2)) {
            //if this particular hab position is currently set to attempted, set to climbed.
            tempData.hab_climb_level = level;
            tempData.hab_climb_2_left = left2;
            tempData.hab_climb_level_attempted = 0;
            tempData.hab_climb_2_left_attempted = false;
        } else if (tempData.hab_climb_level == level && (tempData.hab_climb_2_left == left2 || level != 2)) {
            //if this particular hab position is currently climbed, set climbed to none
            tempData.hab_climb_level = 0;
            tempData.hab_climb_2_left = false;
        } else {
            //if there is a hab level attempted/climbed other than current selection:
            if (tempData.hab_climb_level >= level) {
                //Set this one to climbed if below or equal level to prior climb
                //Set prior climb as attempt if previous attempt was below prior climb
                if (tempData.hab_climb_level > tempData.hab_climb_level_attempted) {
                    tempData.hab_climb_level_attempted = tempData.hab_climb_level;
                    tempData.hab_climb_2_left_attempted = tempData.hab_climb_2_left;
                }
                tempData.hab_climb_level = level;
                tempData.hab_climb_2_left = left2;
            } else if (tempData.hab_climb_level_attempted >= level) {
                //Set this one to climbed if below or equal level to prior attempt
                tempData.hab_climb_level = level;
                tempData.hab_climb_2_left = left2;
            } else {
                //Change attempted to this one if it is above prior attempt
                tempData.hab_climb_level_attempted = level;
                tempData.hab_climb_2_left_attempted = left2;
            }
        }

        loadData(tempData); //apply to UI
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
        data.floor_pickup_cargo = ((CheckBox) getView().findViewById(R.id.floor_pickup_cargo)).isChecked();
        data.floor_pickup_hatch = ((CheckBox) getView().findViewById(R.id.floor_pickup_hatch)).isChecked();
        data.notes = ((EditText) getView().findViewById(R.id.notes)).getText().toString();
        data.tip_over = ((CheckBox) getView().findViewById(R.id.botTip)).isChecked();
        data.foul = ((CheckBox) getView().findViewById(R.id.foul)).isChecked();
        data.yellow_card = ((CheckBox) getView().findViewById(R.id.yellow_card)).isChecked();
        data.red_card = ((CheckBox) getView().findViewById(R.id.red_card)).isChecked();

        data.hab_climb_level = tempData.hab_climb_level;
        data.hab_climb_2_left = tempData.hab_climb_2_left;

        data.hab_climb_level_attempted = tempData.hab_climb_level_attempted;
        data.hab_climb_2_left_attempted = tempData.hab_climb_2_left_attempted;

        tempData = data;
    }

    @Override
    public void loadData(MatchStatsStruct data) {
        tempData = data;
        if (getView() == null || data == null || !displayed)
            return;
        ((CheckBox) getView().findViewById(R.id.floor_pickup_cargo)).setChecked(data.floor_pickup_cargo);
        ((CheckBox) getView().findViewById(R.id.floor_pickup_hatch)).setChecked(data.floor_pickup_hatch);

        ((EditText) getView().findViewById(R.id.notes)).setText(data.notes);
        ((CheckBox) getView().findViewById(R.id.botTip)).setChecked(data.tip_over);
        ((CheckBox) getView().findViewById(R.id.foul)).setChecked(data.foul);
        ((CheckBox) getView().findViewById(R.id.red_card)).setChecked(data.red_card);
        ((CheckBox) getView().findViewById(R.id.yellow_card)).setChecked(data.yellow_card);

        Activity act = getActivity();
        String pos;
        if (act instanceof MatchActivity)
            pos = ((MatchActivity) act).getPosition();
        else
            pos = Prefs.getPosition(getActivity(), "Red 1");

        boolean blue = pos.contains("Blue");

        //set colors based on side;
        hab1.setImageResource(blue ? R.drawable.blue_hab_1 : R.drawable.red_hab_1);
        hab2L.setImageResource(blue ? R.drawable.blue_hab_2_left : R.drawable.red_hab_2_left);
        hab2R.setImageResource(blue ? R.drawable.blue_hab_2_right : R.drawable.red_hab_2_right);
        hab3.setImageResource(blue ? R.drawable.blue_hab_3 : R.drawable.red_hab_3);


        Drawable blackBorder = ContextCompat.getDrawable(mainView.getContext(), R.drawable.blackborder);
        Drawable attemptBorder = ContextCompat.getDrawable(mainView.getContext(), R.drawable.yellowborder);
        Drawable climbBorder = ContextCompat.getDrawable(mainView.getContext(), R.drawable.greenborder);
        //set current selections from load
        Lhab1.setForeground(blackBorder);
        Lhab2L.setForeground(blackBorder);
        Lhab2R.setForeground(blackBorder);
        Lhab3.setForeground(blackBorder);
        switch (data.hab_climb_level_attempted) {
            case 1:
                Lhab1.setForeground(attemptBorder);
                break;
            case 2:
                if (data.hab_climb_2_left_attempted)
                    Lhab2L.setForeground(attemptBorder);
                else
                    Lhab2R.setForeground(attemptBorder);
                break;
            case 3:
                Lhab3.setForeground(attemptBorder);
                break;
            default:
                break;
        }
        switch (data.hab_climb_level) {
            case 1:
                Lhab1.setForeground(climbBorder);
                break;
            case 2:
                if (data.hab_climb_2_left)
                    Lhab2L.setForeground(climbBorder);
                else
                    Lhab2R.setForeground(climbBorder);
                break;
            case 3:
                Lhab3.setForeground(climbBorder);
                break;
            default:
                break;
        }

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
