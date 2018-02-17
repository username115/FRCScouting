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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.frc836.database.MatchStatsStruct;
import org.growingstems.scouting.MatchFragment;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;


public class AutoMatchFragment extends MatchFragment {


    private Spinner exchangeS;
    private Button switchB;
    private Spinner switchS;
    private Button wrongSwitchB;
    private Spinner wrongSwitchS;
    private Spinner scaleS;
    private Spinner wrongScaleS;

    private CheckBox runC;

    private View mainView;


    private MatchStatsStruct tempData = new MatchStatsStruct();

    private boolean displayed = false;


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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mainView = view;
        runC = (CheckBox) view.findViewById(R.id.autoRunC);
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
    public void saveData(MatchStatsStruct data) {
        if (getView() == null || data == null || !displayed)
            return;

        MatchStatsYearly.clearAuto(data);

        data.auto_exchange_count = exchangeS.getSelectedItemPosition();
        data.auto_scale_count = scaleS.getSelectedItemPosition();
        data.auto_scale_wrong_side_count = wrongScaleS.getSelectedItemPosition();
        data.auto_switch_count = switchS.getSelectedItemPosition();
        data.auto_switch_wrong_side_count = wrongSwitchS.getSelectedItemPosition();
        data.auto_run = runC.isChecked();
    }

    @Override
    public void loadData(MatchStatsStruct data) {
        tempData = data;
        if (getView() == null || data == null || !displayed)
            return;
        // which side are we using
        boolean redLeft = Prefs.getRedLeft(getActivity(), true);
        if (redLeft) {
            mainView.findViewById(R.id.scaleBAuto).setScaleX(1.0f);
        } else {
            mainView.findViewById(R.id.scaleBAuto).setScaleX(-1.0f);
        }

        Activity act = getActivity();
        String pos;
        if (act instanceof MatchActivity)
            pos = ((MatchActivity) act).getPosition();
        else
            pos = Prefs.getPosition(getActivity(), "Red 1");


        mainView.findViewById(R.id.scaleBAuto).setScaleY((data.scale_right == redLeft) ? -1f : 1f);

        if (pos.contains("Blue") != redLeft) { //LEFT
            mainView.findViewById(R.id.leftSwitchBAuto).setScaleY((data.near_switch_right == redLeft) ? -1f : 1f);
            mainView.findViewById(R.id.rightSwitchBAuto).setScaleY((data.far_switch_right == redLeft) ? -1f : 1f);

            //scaleB = (Button)mainView.findViewById(data.scale_right ? R.id.ScaleBotCountBAuto : R.id.ScaleTopCountBAuto);
            //wrongScaleB = (Button)mainView.findViewById(data.scale_right ? R.id.ScaleTopCountBAuto : R.id.ScaleBotCountBAuto);
            scaleS = (Spinner) mainView.findViewById(data.scale_right ? R.id.ScaleBotCountSAuto : R.id.ScaleTopCountSAuto);
            wrongScaleS = (Spinner) mainView.findViewById(data.scale_right ? R.id.ScaleTopCountSAuto : R.id.ScaleBotCountSAuto);

            switchB = (Button) mainView.findViewById(data.near_switch_right ? R.id.leftSwitchBotCountBAuto : R.id.leftSwitchTopCountBAuto);
            wrongSwitchB = (Button) mainView.findViewById(data.near_switch_right ? R.id.leftSwitchTopCountBAuto : R.id.leftSwitchBotCountBAuto);
            switchS = (Spinner) mainView.findViewById(data.near_switch_right ? R.id.leftSwitchBotCountSAuto : R.id.leftSwitchTopCountSAuto);
            wrongSwitchS = (Spinner) mainView.findViewById(data.near_switch_right ? R.id.leftSwitchTopCountSAuto : R.id.leftSwitchBotCountSAuto);

            //exchangeB = (Button)mainView.findViewById(R.id.leftExchangeCountBAuto);
            exchangeS = (Spinner) mainView.findViewById(R.id.leftExchangeCountSAuto);

            mainView.findViewById(R.id.leftExchangeLayoutAuto).setVisibility(View.VISIBLE);
            mainView.findViewById(R.id.rightExchangeLayoutAuto).setVisibility(View.GONE);

            mainView.findViewById(R.id.rightSwitchTopCountBAuto).setVisibility(View.GONE);
            mainView.findViewById(R.id.rightSwitchTopCountSAuto).setVisibility(View.GONE);
            mainView.findViewById(R.id.rightSwitchBotCountBAuto).setVisibility(View.GONE);
            mainView.findViewById(R.id.rightSwitchBotCountSAuto).setVisibility(View.GONE);

            switchB.setVisibility(View.VISIBLE);
            switchS.setVisibility(View.VISIBLE);
            wrongSwitchB.setVisibility(View.VISIBLE);
            wrongSwitchS.setVisibility(View.VISIBLE);

        } else { //RIGHT
            mainView.findViewById(R.id.leftSwitchBAuto).setScaleY((data.far_switch_right == redLeft) ? -1f : 1f);
            mainView.findViewById(R.id.rightSwitchBAuto).setScaleY((data.near_switch_right == redLeft) ? -1f : 1f);

            //wrongScaleB = (Button)mainView.findViewById(data.scale_right ? R.id.ScaleBotCountBAuto : R.id.ScaleTopCountBAuto);
            //scaleB = (Button)mainView.findViewById(data.scale_right ? R.id.ScaleTopCountBAuto : R.id.ScaleBotCountBAuto);
            wrongScaleS = (Spinner) mainView.findViewById(data.scale_right ? R.id.ScaleBotCountSAuto : R.id.ScaleTopCountSAuto);
            scaleS = (Spinner) mainView.findViewById(data.scale_right ? R.id.ScaleTopCountSAuto : R.id.ScaleBotCountSAuto);

            wrongSwitchB = (Button) mainView.findViewById(data.near_switch_right ? R.id.rightSwitchBotCountBAuto : R.id.rightSwitchTopCountBAuto);
            switchB = (Button) mainView.findViewById(data.near_switch_right ? R.id.rightSwitchTopCountBAuto : R.id.rightSwitchBotCountBAuto);
            wrongSwitchS = (Spinner) mainView.findViewById(data.near_switch_right ? R.id.rightSwitchBotCountSAuto : R.id.rightSwitchTopCountSAuto);
            switchS = (Spinner) mainView.findViewById(data.near_switch_right ? R.id.rightSwitchTopCountSAuto : R.id.rightSwitchBotCountSAuto);

            //exchangeB = (Button)mainView.findViewById(R.id.rightExchangeCountBAuto);
            exchangeS = (Spinner) mainView.findViewById(R.id.rightExchangeCountSAuto);

            mainView.findViewById(R.id.rightExchangeLayoutAuto).setVisibility(View.VISIBLE);
            mainView.findViewById(R.id.leftExchangeLayoutAuto).setVisibility(View.GONE);

            mainView.findViewById(R.id.leftSwitchTopCountBAuto).setVisibility(View.GONE);
            mainView.findViewById(R.id.leftSwitchTopCountSAuto).setVisibility(View.GONE);
            mainView.findViewById(R.id.leftSwitchBotCountBAuto).setVisibility(View.GONE);
            mainView.findViewById(R.id.leftSwitchBotCountSAuto).setVisibility(View.GONE);

            switchB.setVisibility(View.VISIBLE);
            switchS.setVisibility(View.VISIBLE);
            wrongSwitchB.setVisibility(View.VISIBLE);
            wrongSwitchS.setVisibility(View.VISIBLE);
        }

        if (pos.contains("Blue")) {
            ((ImageView) mainView.findViewById(R.id.leftExchangeAuto)).setImageDrawable(
                    act.getResources().getDrawable(R.drawable.blue_exchange));
            ((ImageView) mainView.findViewById(R.id.rightExchangeAuto)).setImageDrawable(
                    act.getResources().getDrawable(R.drawable.blue_exchange));
        } else {

            ((ImageView) mainView.findViewById(R.id.leftExchangeAuto)).setImageDrawable(
                    act.getResources().getDrawable(R.drawable.red_exchange));
            ((ImageView) mainView.findViewById(R.id.rightExchangeAuto)).setImageDrawable(
                    act.getResources().getDrawable(R.drawable.red_exchange));
        }

        exchangeS.setSelection(data.auto_exchange_count);
        scaleS.setSelection(data.auto_scale_count);
        wrongScaleS.setSelection(data.auto_scale_wrong_side_count);
        switchS.setSelection(data.auto_switch_count);
        wrongSwitchS.setSelection(data.auto_switch_wrong_side_count);
        runC.setChecked(data.auto_run);


    }

    private void setListeners() {
        mainView.findViewById(R.id.rightExchangeCountBAuto)
                .setOnClickListener(new OnIncrementListener((Spinner) mainView.findViewById(R.id.rightExchangeCountSAuto), 1));
        mainView.findViewById(R.id.leftExchangeCountBAuto)
                .setOnClickListener(new OnIncrementListener((Spinner) mainView.findViewById(R.id.leftExchangeCountSAuto), 1));
        mainView.findViewById(R.id.rightSwitchBotCountBAuto)
                .setOnClickListener(new OnIncrementListener((Spinner) mainView.findViewById(R.id.rightSwitchBotCountSAuto), 1));
        mainView.findViewById(R.id.rightSwitchTopCountBAuto)
                .setOnClickListener(new OnIncrementListener((Spinner) mainView.findViewById(R.id.rightSwitchTopCountSAuto), 1));
        mainView.findViewById(R.id.leftSwitchBotCountBAuto)
                .setOnClickListener(new OnIncrementListener((Spinner) mainView.findViewById(R.id.leftSwitchBotCountSAuto), 1));
        mainView.findViewById(R.id.leftSwitchTopCountBAuto)
                .setOnClickListener(new OnIncrementListener((Spinner) mainView.findViewById(R.id.leftSwitchTopCountSAuto), 1));
        mainView.findViewById(R.id.ScaleBotCountBAuto)
                .setOnClickListener(new OnIncrementListener((Spinner) mainView.findViewById(R.id.ScaleBotCountSAuto), 1));
        mainView.findViewById(R.id.ScaleTopCountBAuto)
                .setOnClickListener(new OnIncrementListener((Spinner) mainView.findViewById(R.id.ScaleTopCountSAuto), 1));


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
