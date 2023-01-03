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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;

import org.frc836.database.MatchStatsStruct;
import org.growingstems.scouting.MatchFragment;
import org.growingstems.scouting.R;


public class AutoMatchFragment extends MatchFragment {

    private MatchStatsStruct tempData = new MatchStatsStruct();

    private boolean displayed = false;

    private ToggleButton taxi;

    private Button highSuccessIncrement;
    private Button highSuccessDecrement;
    private Button highFailureIncrement;
    private Button highFailureDecrement;
    private Button lowSuccessIncrement;
    private Button lowSuccessDecrement;
    private Button lowFailureIncrement;
    private Button lowFailureDecrement;

    private Spinner highSuccess;
    private Spinner highFailure;
    private Spinner lowSuccess;
    private Spinner lowFailure;


    public AutoMatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PreMatch.
     */
    public static AutoMatchFragment newInstance() {
        return new AutoMatchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
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
    public void saveData(@NonNull MatchStatsStruct data) {
        if (getView() == null || !displayed)
            return;

        MatchStatsYearly.clearAuto(data);

        data.auto_taxi = taxi.isChecked();

        data.auto_high_score = highSuccess.getSelectedItemPosition();
        data.auto_high_miss = highFailure.getSelectedItemPosition();
        data.auto_low_score = lowSuccess.getSelectedItemPosition();
        data.auto_low_miss = lowFailure.getSelectedItemPosition();
    }

    @Override
    public void loadData(@NonNull MatchStatsStruct data) {
        tempData = data;
        if (getView() == null || !displayed)
            return;

        taxi.setChecked(data.auto_taxi);

        highSuccess.setSelection(data.auto_high_score);
        highFailure.setSelection(data.auto_high_miss);
        lowSuccess.setSelection(data.auto_low_score);
        lowFailure.setSelection(data.auto_low_miss);
    }

    private void getGUIRefs(View view) {

        taxi = view.findViewById(R.id.taxiToggle);

        highSuccessIncrement = view.findViewById(R.id.increment_auto_high_success);
        highSuccessDecrement = view.findViewById(R.id.decrement_auto_high_success);
        highFailureIncrement = view.findViewById(R.id.increment_auto_high_failure);
        highFailureDecrement = view.findViewById(R.id.decrement_auto_high_failure);
        lowSuccessIncrement = view.findViewById(R.id.increment_auto_low_success);
        lowSuccessDecrement = view.findViewById(R.id.decrement_auto_low_success);
        lowFailureIncrement = view.findViewById(R.id.increment_auto_low_failure);
        lowFailureDecrement = view.findViewById(R.id.decrement_auto_low_failure);

        highSuccess = view.findViewById(R.id.auto_high_success_spinner);
        highFailure = view.findViewById(R.id.auto_high_failure_spinner);
        lowSuccess = view.findViewById(R.id.auto_low_success_spinner);
        lowFailure = view.findViewById(R.id.auto_low_failure_spinner);
    }

    private void setListeners() {
        highSuccessIncrement.setOnClickListener(new OnIncrementListener(highSuccess, 1));
        highSuccessDecrement.setOnClickListener(new OnIncrementListener(highSuccess, -1));
        highFailureIncrement.setOnClickListener(new OnIncrementListener(highFailure, 1));
        highFailureDecrement.setOnClickListener(new OnIncrementListener(highFailure, -1));
        lowSuccessIncrement.setOnClickListener(new OnIncrementListener(lowSuccess, 1));
        lowSuccessDecrement.setOnClickListener(new OnIncrementListener(lowSuccess, -1));
        lowFailureIncrement.setOnClickListener(new OnIncrementListener(lowFailure, 1));
        lowFailureDecrement.setOnClickListener(new OnIncrementListener(lowFailure, -1));
    }

    private static class OnIncrementListener implements View.OnClickListener {

        int m_increment;
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
