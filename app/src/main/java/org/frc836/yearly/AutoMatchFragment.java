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
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.frc836.database.MatchStatsStruct;
import org.growingstems.scouting.MatchFragment;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;


public class AutoMatchFragment extends MatchFragment {


    private static final int LEFT = 0;
    private static final int RIGHT = 1;

    private CheckBox crossBaselineC;
    private CheckBox dumpHopperC;

    private Spinner scoreHighCount;
    private Button scoreHighB;
    private Button scoreHigh5B;
    private Spinner missHighCount;
    private Button missHighB;
    private Button missHigh5B;
    private Spinner scoreLowCount;
    private Button scoreLowB;
    private Button scoreLow5B;

    private LinearLayout[] layouts = new LinearLayout[2];

    private ImageSwitcher[] AirshipTop = new ImageSwitcher[2];
    private ImageSwitcher[] AirshipMid = new ImageSwitcher[2];
    private ImageSwitcher[] AirshipBot = new ImageSwitcher[2];

    private Spinner[] LeftGearCount = new Spinner[2];
    private Button[] LeftGearB = new Button[2];
    private Spinner[] CenterGearCount = new Spinner[2];
    private Button[] CenterGearB = new Button[2];
    private Spinner[] RightGearCount = new Spinner[2];
    private Button[] RightGearB = new Button[2];

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
    public void saveData(MatchStatsStruct data) {
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
            side = RIGHT;
        } else {
            side = LEFT;
        }

        MatchStatsYearly.clearAuto(data);

        data.auto_cross_baseline = crossBaselineC.isChecked();
        data.auto_dump_hopper = dumpHopperC.isChecked();
        data.auto_score_high = scoreHighCount.getSelectedItemPosition();
        data.auto_miss_high = missHighCount.getSelectedItemPosition();
        data.auto_score_low = scoreLowCount.getSelectedItemPosition();
        data.auto_gear_delivered_left = LeftGearCount[side].getSelectedItemPosition();
        data.auto_gear_delivered_center = CenterGearCount[side].getSelectedItemPosition();
        data.auto_gear_delivered_right = RightGearCount[side].getSelectedItemPosition();
    }

    @Override
    public void loadData(MatchStatsStruct data) {
        tempData = data;
        if (getView() == null || data == null || !displayed)
            return;
        // which side are we using
        boolean redLeft = Prefs.getRedLeft(getActivity(), true);
        if (redLeft) {
            AirshipTop[LEFT].setBackgroundResource(R.drawable.red_diag);
            AirshipMid[LEFT].setBackgroundResource(R.drawable.red_square);
            AirshipBot[LEFT].setBackgroundResource(R.drawable.red_diag);
            AirshipTop[RIGHT].setBackgroundResource(R.drawable.blue_diag);
            AirshipMid[RIGHT].setBackgroundResource(R.drawable.blue_square);
            AirshipBot[RIGHT].setBackgroundResource(R.drawable.blue_diag);
        } else {
            AirshipTop[RIGHT].setBackgroundResource(R.drawable.red_diag);
            AirshipMid[RIGHT].setBackgroundResource(R.drawable.red_square);
            AirshipBot[RIGHT].setBackgroundResource(R.drawable.red_diag);
            AirshipTop[LEFT].setBackgroundResource(R.drawable.blue_diag);
            AirshipMid[LEFT].setBackgroundResource(R.drawable.blue_square);
            AirshipBot[LEFT].setBackgroundResource(R.drawable.blue_diag);
        }

        Activity act = getActivity();
        String pos;
        if (act instanceof MatchActivity)
            pos = ((MatchActivity)act).getPosition();
        else
            pos = Prefs.getPosition(getActivity(), "Red 1");

        int side;
        if ((pos.contains("Blue") && !redLeft) || ((!pos.contains("Blue") && redLeft))) {
            layouts[LEFT].setVisibility(View.GONE);
            layouts[RIGHT].setVisibility(View.VISIBLE);
            side = RIGHT;
        } else {
            layouts[LEFT].setVisibility(View.VISIBLE);
            layouts[RIGHT].setVisibility(View.GONE);
            side = LEFT;
        }

        LeftGearCount[side].setSelection(data.auto_gear_delivered_left);
        CenterGearCount[side].setSelection(data.auto_gear_delivered_center);
        RightGearCount[side].setSelection(data.auto_gear_delivered_right);

        crossBaselineC.setChecked(data.auto_cross_baseline);
        dumpHopperC.setChecked(data.auto_dump_hopper);
        scoreHighCount.setSelection(data.auto_score_high);
        missHighCount.setSelection(data.auto_miss_high);
        scoreLowCount.setSelection(data.auto_score_low);

    }

    private void getGUIRefs(View view) {
        crossBaselineC = (CheckBox) view.findViewById(R.id.crossBaselineC);
        dumpHopperC = (CheckBox) view.findViewById(R.id.dumpHopperC);

        scoreHighCount = (Spinner) view.findViewById(R.id.scoreHighCount);
        scoreHighB = (Button) view.findViewById(R.id.scoreHighB);
        scoreHigh5B = (Button) view.findViewById(R.id.scoreHigh5B);
        missHighCount = (Spinner) view.findViewById(R.id.missHighCount);
        missHighB = (Button) view.findViewById(R.id.missHighB);
        missHigh5B = (Button) view.findViewById(R.id.missHigh5B);
        scoreLowCount = (Spinner) view.findViewById(R.id.scoreLowCount);
        scoreLowB = (Button) view.findViewById(R.id.scoreLowB);
        scoreLow5B = (Button) view.findViewById(R.id.scoreLow5B);

        layouts[LEFT] = (LinearLayout) view.findViewById(R.id.leftAutoMatch);
        layouts[RIGHT] = (LinearLayout) view.findViewById(R.id.rightAutoMatch);

        AirshipTop[LEFT] = (ImageSwitcher) view.findViewById(R.id.leftAirshipTop);
        AirshipMid[LEFT] = (ImageSwitcher) view.findViewById(R.id.leftAirshipMid);
        AirshipBot[LEFT] = (ImageSwitcher) view.findViewById(R.id.leftAirshipBot);

        AirshipTop[RIGHT] = (ImageSwitcher) view.findViewById(R.id.rightAirshipTop);
        AirshipMid[RIGHT] = (ImageSwitcher) view.findViewById(R.id.rightAirshipMid);
        AirshipBot[RIGHT] = (ImageSwitcher) view.findViewById(R.id.rightAirshipBot);

        LeftGearCount[LEFT] = (Spinner) view.findViewById(R.id.leftLeftGearCount);
        LeftGearB[LEFT] = (Button) view.findViewById(R.id.leftLeftGearB);
        CenterGearCount[LEFT] = (Spinner) view.findViewById(R.id.leftCenterGearCount);
        CenterGearB[LEFT] = (Button) view.findViewById(R.id.leftCenterGearB);
        RightGearCount[LEFT] = (Spinner) view.findViewById(R.id.leftRightGearCount);
        RightGearB[LEFT] = (Button) view.findViewById(R.id.leftRightGearB);

        LeftGearCount[RIGHT] = (Spinner) view.findViewById(R.id.rightLeftGearCount);
        LeftGearB[RIGHT] = (Button) view.findViewById(R.id.rightLeftGearB);
        CenterGearCount[RIGHT] = (Spinner) view.findViewById(R.id.rightCenterGearCount);
        CenterGearB[RIGHT] = (Button) view.findViewById(R.id.rightCenterGearB);
        RightGearCount[RIGHT] = (Spinner) view.findViewById(R.id.rightRightGearCount);
        RightGearB[RIGHT] = (Button) view.findViewById(R.id.rightRightGearB);

    }

    private void setListeners() {

        scoreHighB.setOnClickListener(new OnIncrementListener(scoreHighCount, 1));
        scoreHigh5B.setOnClickListener(new OnIncrementListener(scoreHighCount, 5));
        missHighB.setOnClickListener(new OnIncrementListener(missHighCount, 1));
        missHigh5B.setOnClickListener(new OnIncrementListener(missHighCount, 5));
        scoreLowB.setOnClickListener(new OnIncrementListener(scoreLowCount, 1));
        scoreLow5B.setOnClickListener(new OnIncrementListener(scoreLowCount, 5));

        LeftGearB[LEFT].setOnClickListener(new OnIncrementListener(LeftGearCount[LEFT], 1));
        CenterGearB[LEFT].setOnClickListener(new OnIncrementListener(CenterGearCount[LEFT], 1));
        RightGearB[LEFT].setOnClickListener(new OnIncrementListener(RightGearCount[LEFT], 1));
        LeftGearB[RIGHT].setOnClickListener(new OnIncrementListener(LeftGearCount[RIGHT], 1));
        CenterGearB[RIGHT].setOnClickListener(new OnIncrementListener(CenterGearCount[RIGHT], 1));
        RightGearB[RIGHT].setOnClickListener(new OnIncrementListener(RightGearCount[RIGHT], 1));

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
