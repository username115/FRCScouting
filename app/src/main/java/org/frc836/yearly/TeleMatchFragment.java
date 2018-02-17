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
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.frc836.database.MatchStatsStruct;
import org.growingstems.scouting.MatchFragment;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;


public class TeleMatchFragment extends MatchFragment {


    private Spinner exchangeS;
    private Spinner switchS;
    private Spinner wrongSwitchS;
    private Spinner scaleS;
    private Spinner wrongScaleS;
    private Spinner oppositeSwitchS;
    private Spinner wrongOppositeSwitchS;

    private View mainView;


    private MatchStatsStruct tempData = new MatchStatsStruct();

    private boolean displayed = false;


    public TeleMatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PreMatch.
     */
    public static TeleMatchFragment newInstance() {
        return new TeleMatchFragment();
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mainView = view;
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

        MatchStatsYearly.clearTele(data);

        data.exchange_count = exchangeS.getSelectedItemPosition();
        data.scale_count = scaleS.getSelectedItemPosition();
        data.scale_wrong_side_count = wrongScaleS.getSelectedItemPosition();
        data.switch_count = switchS.getSelectedItemPosition();
        data.switch_wrong_side_count = wrongSwitchS.getSelectedItemPosition();
        data.opposite_switch_count = oppositeSwitchS.getSelectedItemPosition();
        data.opposite_switch_wrong_side_count = wrongOppositeSwitchS.getSelectedItemPosition();
    }

    @Override
    public void loadData(MatchStatsStruct data) {
        tempData = data;
        if (getView() == null || data == null || !displayed)
            return;
        // which side are we using
        boolean redLeft = Prefs.getRedLeft(getActivity(), true);
        if (redLeft) {
            mainView.findViewById(R.id.scaleBTele).setScaleX(1.0f);
        } else {
            mainView.findViewById(R.id.scaleBTele).setScaleX(-1.0f);
        }

        Activity act = getActivity();
        String pos;
        if (act instanceof MatchActivity)
            pos = ((MatchActivity) act).getPosition();
        else
            pos = Prefs.getPosition(getActivity(), "Red 1");

        mainView.findViewById(R.id.scaleBTele).setScaleY((data.scale_right == redLeft) ? -1f : 1f);
        if (pos.contains("Blue") != redLeft) { //LEFT
            mainView.findViewById(R.id.leftSwitchBTele).setScaleY((data.near_switch_right == redLeft) ? -1f : 1f);
            mainView.findViewById(R.id.rightSwitchBTele).setScaleY((data.far_switch_right == redLeft) ? -1f : 1f);

            //scaleB = (Button)mainView.findViewById(data.scale_right ? R.id.ScaleBotCountBTele : R.id.ScaleTopCountBTele);
            //wrongScaleB = (Button)mainView.findViewById(data.scale_right ? R.id.ScaleTopCountBTele : R.id.ScaleBotCountBTele);
            scaleS = (Spinner) mainView.findViewById(data.scale_right ? R.id.ScaleBotCountSTele : R.id.ScaleTopCountSTele);
            wrongScaleS = (Spinner) mainView.findViewById(data.scale_right ? R.id.ScaleTopCountSTele : R.id.ScaleBotCountSTele);

            //switchB = (Button) mainView.findViewById(data.near_switch_right ? R.id.leftSwitchBotCountBTele : R.id.leftSwitchTopCountBTele);
            //wrongSwitchB = (Button) mainView.findViewById(data.near_switch_right ? R.id.leftSwitchTopCountBTele : R.id.leftSwitchBotCountBTele);
            switchS = (Spinner) mainView.findViewById(data.near_switch_right ? R.id.leftSwitchBotCountSTele : R.id.leftSwitchTopCountSTele);
            wrongSwitchS = (Spinner) mainView.findViewById(data.near_switch_right ? R.id.leftSwitchTopCountSTele : R.id.leftSwitchBotCountSTele);

            oppositeSwitchS = (Spinner) mainView.findViewById(data.near_switch_right ? R.id.rightSwitchBotCountSTele : R.id.rightSwitchTopCountSTele);
            wrongOppositeSwitchS = (Spinner) mainView.findViewById(data.near_switch_right ? R.id.rightSwitchTopCountSTele : R.id.rightSwitchBotCountSTele);

            //exchangeB = (Button)mainView.findViewById(R.id.leftExchangeCountBTele);
            exchangeS = (Spinner) mainView.findViewById(R.id.leftExchangeCountSTele);

            mainView.findViewById(R.id.leftExchangeLayoutTele).setVisibility(View.VISIBLE);
            mainView.findViewById(R.id.rightExchangeLayoutTele).setVisibility(View.GONE);


        } else { //RIGHT
            mainView.findViewById(R.id.leftSwitchBTele).setScaleY((data.far_switch_right == redLeft) ? -1f : 1f);
            mainView.findViewById(R.id.rightSwitchBTele).setScaleY((data.near_switch_right == redLeft) ? -1f : 1f);

            //wrongScaleB = (Button)mainView.findViewById(data.scale_right ? R.id.ScaleBotCountBTele : R.id.ScaleTopCountBTele);
            //scaleB = (Button)mainView.findViewById(data.scale_right ? R.id.ScaleTopCountBTele : R.id.ScaleBotCountBTele);
            wrongScaleS = (Spinner) mainView.findViewById(data.scale_right ? R.id.ScaleBotCountSTele : R.id.ScaleTopCountSTele);
            scaleS = (Spinner) mainView.findViewById(data.scale_right ? R.id.ScaleTopCountSTele : R.id.ScaleBotCountSTele);

            //wrongSwitchB = (Button) mainView.findViewById(data.near_switch_right ? R.id.rightSwitchBotCountBTele : R.id.rightSwitchTopCountBTele);
            //switchB = (Button) mainView.findViewById(data.near_switch_right ? R.id.rightSwitchTopCountBTele : R.id.rightSwitchBotCountBTele);
            wrongSwitchS = (Spinner) mainView.findViewById(data.near_switch_right ? R.id.rightSwitchBotCountSTele : R.id.rightSwitchTopCountSTele);
            switchS = (Spinner) mainView.findViewById(data.near_switch_right ? R.id.rightSwitchTopCountSTele : R.id.rightSwitchBotCountSTele);

            wrongOppositeSwitchS = (Spinner) mainView.findViewById(data.near_switch_right ? R.id.leftSwitchBotCountSTele : R.id.leftSwitchTopCountSTele);
            oppositeSwitchS = (Spinner) mainView.findViewById(data.near_switch_right ? R.id.leftSwitchTopCountSTele : R.id.leftSwitchBotCountSTele);

            //exchangeB = (Button)mainView.findViewById(R.id.rightExchangeCountBTele);
            exchangeS = (Spinner) mainView.findViewById(R.id.rightExchangeCountSTele);

            mainView.findViewById(R.id.rightExchangeLayoutTele).setVisibility(View.VISIBLE);
            mainView.findViewById(R.id.leftExchangeLayoutTele).setVisibility(View.GONE);
        }

        if (pos.contains("Blue")) {
            ((ImageView) mainView.findViewById(R.id.leftExchangeTele)).setImageDrawable(
                    act.getResources().getDrawable(R.drawable.blue_exchange));
            ((ImageView) mainView.findViewById(R.id.rightExchangeTele)).setImageDrawable(
                    act.getResources().getDrawable(R.drawable.blue_exchange));
        } else {

            ((ImageView) mainView.findViewById(R.id.leftExchangeTele)).setImageDrawable(
                    act.getResources().getDrawable(R.drawable.red_exchange));
            ((ImageView) mainView.findViewById(R.id.rightExchangeTele)).setImageDrawable(
                    act.getResources().getDrawable(R.drawable.red_exchange));
        }

        exchangeS.setSelection(data.exchange_count);
        scaleS.setSelection(data.scale_count);
        wrongScaleS.setSelection(data.scale_wrong_side_count);
        switchS.setSelection(data.switch_count);
        wrongSwitchS.setSelection(data.switch_wrong_side_count);
        oppositeSwitchS.setSelection(data.opposite_switch_count);
        wrongOppositeSwitchS.setSelection(data.opposite_switch_wrong_side_count);
    }

    private void setListeners() {

        mainView.findViewById(R.id.rightExchangeCountBTele)
                .setOnClickListener(new OnIncrementListener((Spinner) mainView.findViewById(R.id.rightExchangeCountSTele), 1));
        mainView.findViewById(R.id.leftExchangeCountBTele)
                .setOnClickListener(new OnIncrementListener((Spinner) mainView.findViewById(R.id.leftExchangeCountSTele), 1));
        mainView.findViewById(R.id.rightSwitchBotCountBTele)
                .setOnClickListener(new OnIncrementListener((Spinner) mainView.findViewById(R.id.rightSwitchBotCountSTele), 1));
        mainView.findViewById(R.id.rightSwitchTopCountBTele)
                .setOnClickListener(new OnIncrementListener((Spinner) mainView.findViewById(R.id.rightSwitchTopCountSTele), 1));
        mainView.findViewById(R.id.leftSwitchBotCountBTele)
                .setOnClickListener(new OnIncrementListener((Spinner) mainView.findViewById(R.id.leftSwitchBotCountSTele), 1));
        mainView.findViewById(R.id.leftSwitchTopCountBTele)
                .setOnClickListener(new OnIncrementListener((Spinner) mainView.findViewById(R.id.leftSwitchTopCountSTele), 1));
        mainView.findViewById(R.id.ScaleBotCountBTele)
                .setOnClickListener(new OnIncrementListener((Spinner) mainView.findViewById(R.id.ScaleBotCountSTele), 1));
        mainView.findViewById(R.id.ScaleTopCountBTele)
                .setOnClickListener(new OnIncrementListener((Spinner) mainView.findViewById(R.id.ScaleTopCountSTele), 1));
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
