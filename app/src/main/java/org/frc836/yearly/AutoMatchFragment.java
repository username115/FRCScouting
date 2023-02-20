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
import android.widget.Spinner;

import androidx.annotation.NonNull;

import org.frc836.database.MatchStatsStruct;
import org.growingstems.scouting.MatchFragment;
import org.growingstems.scouting.R;


public class AutoMatchFragment extends MatchFragment {

    private MatchStatsStruct tempData = new MatchStatsStruct();

    private boolean displayed = false;

    //TODO UI element references

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
        return inflater.inflate(R.layout.fragment_tele, container, false);
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

        //TODO save data
    }

    @Override
    public void loadData(@NonNull MatchStatsStruct data) {
        tempData = data;
        if (getView() == null || !displayed)
            return;

        //TODO load data
    }

    private void getGUIRefs(View view) {

        //TODO get GUI references
    }

    private void setListeners() {

        //TODO update listeners
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
