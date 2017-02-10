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
import android.widget.Spinner;

import org.frc836.database.PilotStatsStruct;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;


public class PilotMatchFragment extends PilotFragment {

    private static final int LEFT = 0;
    private static final int RIGHT = 1;

    private PilotStatsStruct[] tempData = new PilotStatsStruct[2];

    private boolean displayed = false;


    public PilotMatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PreMatch.
     */
    public static PilotMatchFragment newInstance() {
        return new PilotMatchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tempData[0] = new PilotStatsStruct();
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
        // which side are we using
        boolean redLeft = Prefs.getRedLeft(getActivity(), true);
        Activity act = getActivity();
        String pos;
        if (act instanceof MatchActivity)
            pos = ((MatchActivity)act).getPosition();
        else
            pos = Prefs.getPosition(getActivity(), "Red 1");
        int side;
        if ((pos.contains("Blue") && !redLeft) || ((!pos.contains("Blue") && redLeft))) {
            side = LEFT;
        } else {
            side = RIGHT;
        }
        // TODO
    }

    @Override
    public void loadData(PilotStatsStruct[] data) {
        tempData = data;
        if (getView() == null || data == null || !displayed)
            return;
        // which side are we using
        boolean redLeft = Prefs.getRedLeft(getActivity(), true);
        if (redLeft) {
            // TODO set color of airship
        } else {

        }

        Activity act = getActivity();
        String pos;
        if (act instanceof MatchActivity)
            pos = ((MatchActivity)act).getPosition();
        else
            pos = Prefs.getPosition(getActivity(), "Red 1");

        int side;
        if ((pos.contains("Blue") && !redLeft) || ((!pos.contains("Blue") && redLeft))) {
            // TODO set scale for side
            side = LEFT;
        } else {
            side = RIGHT;
        }

        // TODO
    }

    private void getGUIRefs(View view) {
        // TODO
    }

    private void setListeners() {
        // TODO
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
            m_spinner.setSelection(m_spinner.getSelectedItemPosition() + m_increment);
        }
    }
}
