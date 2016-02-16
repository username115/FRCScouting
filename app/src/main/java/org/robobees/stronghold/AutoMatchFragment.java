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
package org.robobees.stronghold;

import android.os.Bundle;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;


public class AutoMatchFragment extends MatchFragment {

    private static final int LEFT_5 = 0;
    private static final int LEFT_4 = 1;
    private static final int LEFT_3 = 2;
    private static final int LEFT_2 = 3;
    private static final int RIGHT_2 = 4;
    private static final int RIGHT_3 = 5;
    private static final int RIGHT_4 = 6;
    private static final int RIGHT_5 = 7;
    private static final int LEFT_LOW = 8;
    private static final int RIGHT_LOW = 9;
    private static final int NUM_DEF = 10;

    private SparseArray<Spinner> forS;
    private SparseArray<Spinner> revS;
    private SparseArray<Spinner> helpS;
    private SparseArray<Button> forB;
    private SparseArray<Button> revB;
    private SparseArray<Button> helpB;
    private SparseArray<LinearLayout> images;
    private SparseIntArray resources;
    private SparseArray<LinearLayout> helpL;

    private Spinner scoreHighS;
    private Spinner missHighS;
    private Spinner scoreLowS;
    private Spinner missLowS;

    private Button scoreHighB;
    private Button missHighB;
    private Button scoreLowB;
    private Button missLowB;

    private CheckBox spyC;
    private CheckBox reachC;

    private LinearLayout leftL;
    private LinearLayout rightL;

    private MatchStatsSH tempData = new MatchStatsSH();


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
        AutoMatchFragment fragment = new AutoMatchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forS = new SparseArray<Spinner>(NUM_DEF);
        revS = new SparseArray<Spinner>(NUM_DEF);
        helpS = new SparseArray<Spinner>(NUM_DEF);
        forB = new SparseArray<Button>(NUM_DEF);
        revB = new SparseArray<Button>(NUM_DEF);
        helpB = new SparseArray<Button>(NUM_DEF);
        images = new SparseArray<LinearLayout>(NUM_DEF);
        resources = new SparseIntArray(9);
        resources.put(0, R.drawable.blank);
        resources.put(1, R.drawable.portcullis);
        resources.put(2, R.drawable.cheval);
        resources.put(3, R.drawable.moat);
        resources.put(4, R.drawable.ramparts);
        resources.put(5, R.drawable.drawbridge);
        resources.put(6, R.drawable.sally);
        resources.put(7, R.drawable.rock_wall);
        resources.put(8, R.drawable.rough_terrain);
        helpL = new SparseArray<LinearLayout>(NUM_DEF);
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
    public void saveData(MatchStatsSH data) {
        if (getView() == null)
            return;
        // TODO
    }

    @Override
    public void loadData(MatchStatsSH data) {
        if (getView() == null)
            return;
        boolean redLeft = Prefs.getRedLeft(getActivity(), true);
        if (redLeft) {
            leftL.setBackgroundResource(R.color.red);
            rightL.setBackgroundResource(R.color.blue);
        } else {
            leftL.setBackgroundResource(R.color.blue);
            rightL.setBackgroundResource(R.color.red);
        }

        String pos = Prefs.getPosition(getActivity(), "Red 1");
        if ((pos.contains("Blue") && !redLeft) || ((!pos.contains("Blue") && redLeft))) {
            leftL.setVisibility(View.GONE);
            rightL.setVisibility(View.VISIBLE);
        } else {
            leftL.setVisibility(View.VISIBLE);
            rightL.setVisibility(View.GONE);
        }
        // TODO
    }

    private void getGUIRefs(View view) {
        leftL = (LinearLayout) view.findViewById(R.id.leftAutoMatch);
        rightL = (LinearLayout) view.findViewById(R.id.rightAutoMatch);

        forS.put(LEFT_5, (Spinner) view.findViewById(R.id.leftCrossFor5Count));
        forS.put(LEFT_4, (Spinner) view.findViewById(R.id.leftCrossFor4Count));
        forS.put(LEFT_3, (Spinner) view.findViewById(R.id.leftCrossFor3Count));
        forS.put(LEFT_2, (Spinner) view.findViewById(R.id.leftCrossFor2Count));
        forS.put(LEFT_LOW, (Spinner) view.findViewById(R.id.leftCrossForLowBarCount));

        forS.put(RIGHT_5, (Spinner) view.findViewById(R.id.rightCrossFor5Count));
        forS.put(RIGHT_4, (Spinner) view.findViewById(R.id.rightCrossFor4Count));
        forS.put(RIGHT_3, (Spinner) view.findViewById(R.id.rightCrossFor3Count));
        forS.put(RIGHT_2, (Spinner) view.findViewById(R.id.rightCrossFor2Count));
        forS.put(RIGHT_LOW, (Spinner) view.findViewById(R.id.rightCrossForLowBarCount));

        revS.put(LEFT_5, (Spinner) view.findViewById(R.id.leftCrossRev5Count));
        revS.put(LEFT_4, (Spinner) view.findViewById(R.id.leftCrossRev4Count));
        revS.put(LEFT_3, (Spinner) view.findViewById(R.id.leftCrossRev3Count));
        revS.put(LEFT_2, (Spinner) view.findViewById(R.id.leftCrossRev2Count));
        revS.put(LEFT_LOW, (Spinner) view.findViewById(R.id.leftCrossRevLowBarCount));

        revS.put(RIGHT_5, (Spinner) view.findViewById(R.id.rightCrossRev5Count));
        revS.put(RIGHT_4, (Spinner) view.findViewById(R.id.rightCrossRev4Count));
        revS.put(RIGHT_3, (Spinner) view.findViewById(R.id.rightCrossRev3Count));
        revS.put(RIGHT_2, (Spinner) view.findViewById(R.id.rightCrossRev2Count));
        revS.put(RIGHT_LOW, (Spinner) view.findViewById(R.id.rightCrossRevLowBarCount));

        helpS.put(LEFT_5, (Spinner) view.findViewById(R.id.leftCrossHelp5Count));
        helpS.put(LEFT_4, (Spinner) view.findViewById(R.id.leftCrossHelp4Count));
        helpS.put(LEFT_3, (Spinner) view.findViewById(R.id.leftCrossHelp3Count));
        helpS.put(LEFT_2, (Spinner) view.findViewById(R.id.leftCrossHelp2Count));
        helpS.put(LEFT_LOW, (Spinner) view.findViewById(R.id.leftCrossHelpLowBarCount));

        helpS.put(RIGHT_5, (Spinner) view.findViewById(R.id.rightCrossHelp5Count));
        helpS.put(RIGHT_4, (Spinner) view.findViewById(R.id.rightCrossHelp4Count));
        helpS.put(RIGHT_3, (Spinner) view.findViewById(R.id.rightCrossHelp3Count));
        helpS.put(RIGHT_2, (Spinner) view.findViewById(R.id.rightCrossHelp2Count));
        helpS.put(RIGHT_LOW, (Spinner) view.findViewById(R.id.rightCrossHelpLowBarCount));

        forB.put(LEFT_5, (Button) view.findViewById(R.id.addLeftCrossFor5B));
        forB.put(LEFT_4, (Button) view.findViewById(R.id.addLeftCrossFor4B));
        forB.put(LEFT_3, (Button) view.findViewById(R.id.addLeftCrossFor3B));
        forB.put(LEFT_2, (Button) view.findViewById(R.id.addLeftCrossFor2B));
        forB.put(LEFT_LOW, (Button) view.findViewById(R.id.addLeftCrossForLowBarB));

        forB.put(RIGHT_5, (Button) view.findViewById(R.id.addRightCrossFor5B));
        forB.put(RIGHT_4, (Button) view.findViewById(R.id.addRightCrossFor4B));
        forB.put(RIGHT_3, (Button) view.findViewById(R.id.addRightCrossFor3B));
        forB.put(RIGHT_2, (Button) view.findViewById(R.id.addRightCrossFor2B));
        forB.put(RIGHT_LOW, (Button) view.findViewById(R.id.addRightCrossForLowBarB));

        revB.put(LEFT_5, (Button) view.findViewById(R.id.addLeftCrossRev5B));
        revB.put(LEFT_4, (Button) view.findViewById(R.id.addLeftCrossRev4B));
        revB.put(LEFT_3, (Button) view.findViewById(R.id.addLeftCrossRev3B));
        revB.put(LEFT_2, (Button) view.findViewById(R.id.addLeftCrossRev2B));
        revB.put(LEFT_LOW, (Button) view.findViewById(R.id.addLeftCrossRevLowBarB));

        revB.put(RIGHT_5, (Button) view.findViewById(R.id.addRightCrossRev5B));
        revB.put(RIGHT_4, (Button) view.findViewById(R.id.addRightCrossRev4B));
        revB.put(RIGHT_3, (Button) view.findViewById(R.id.addRightCrossRev3B));
        revB.put(RIGHT_2, (Button) view.findViewById(R.id.addRightCrossRev2B));
        revB.put(RIGHT_LOW, (Button) view.findViewById(R.id.addRightCrossRevLowBarB));

        helpB.put(LEFT_5, (Button) view.findViewById(R.id.addLeftCrossHelp5B));
        helpB.put(LEFT_4, (Button) view.findViewById(R.id.addLeftCrossHelp4B));
        helpB.put(LEFT_3, (Button) view.findViewById(R.id.addLeftCrossHelp3B));
        helpB.put(LEFT_2, (Button) view.findViewById(R.id.addLeftCrossHelp2B));
        helpB.put(LEFT_LOW, (Button) view.findViewById(R.id.addLeftCrossHelpLowBarB));

        helpB.put(RIGHT_5, (Button) view.findViewById(R.id.addRightCrossHelp5B));
        helpB.put(RIGHT_4, (Button) view.findViewById(R.id.addRightCrossHelp4B));
        helpB.put(RIGHT_3, (Button) view.findViewById(R.id.addRightCrossHelp3B));
        helpB.put(RIGHT_2, (Button) view.findViewById(R.id.addRightCrossHelp2B));
        helpB.put(RIGHT_LOW, (Button) view.findViewById(R.id.addRightCrossHelpLowBarB));

        helpL.put(LEFT_5, (LinearLayout) view.findViewById(R.id.leftHelp5L));
        helpL.put(LEFT_4, (LinearLayout) view.findViewById(R.id.leftHelp4L));
        helpL.put(LEFT_3, (LinearLayout) view.findViewById(R.id.leftHelp3L));
        helpL.put(LEFT_2, (LinearLayout) view.findViewById(R.id.leftHelp2L));
        helpL.put(LEFT_LOW, (LinearLayout) view.findViewById(R.id.leftHelpLowBarL));

        helpL.put(RIGHT_5, (LinearLayout) view.findViewById(R.id.rightHelp5L));
        helpL.put(RIGHT_4, (LinearLayout) view.findViewById(R.id.rightHelp4L));
        helpL.put(RIGHT_3, (LinearLayout) view.findViewById(R.id.rightHelp3L));
        helpL.put(RIGHT_2, (LinearLayout) view.findViewById(R.id.rightHelp2L));
        helpL.put(RIGHT_LOW, (LinearLayout) view.findViewById(R.id.rightHelpLowBarL));

        images.put(LEFT_5, (LinearLayout) view.findViewById(R.id.leftDefense5Box));
        images.put(LEFT_4, (LinearLayout) view.findViewById(R.id.leftDefense4Box));
        images.put(LEFT_3, (LinearLayout) view.findViewById(R.id.leftDefense3Box));
        images.put(LEFT_2, (LinearLayout) view.findViewById(R.id.leftDefense2Box));
        images.put(LEFT_LOW, (LinearLayout) view.findViewById(R.id.leftDefenseLowBarBox));

        images.put(RIGHT_2, (LinearLayout) view.findViewById(R.id.rightDefense2Box));
        images.put(RIGHT_3, (LinearLayout) view.findViewById(R.id.rightDefense3Box));
        images.put(RIGHT_4, (LinearLayout) view.findViewById(R.id.rightDefense4Box));
        images.put(RIGHT_5, (LinearLayout) view.findViewById(R.id.rightDefense5Box));
        images.put(RIGHT_LOW, (LinearLayout) view.findViewById(R.id.rightDefenseLowBarBox));

        scoreHighS = (Spinner) view.findViewById(R.id.scoreHighCount);
        missHighS = (Spinner) view.findViewById(R.id.missHighCount);
        scoreLowS = (Spinner) view.findViewById(R.id.scoreLowCount);
        missLowS = (Spinner) view.findViewById(R.id.missLowCount);

        scoreHighB = (Button) view.findViewById(R.id.scoreHighB);
        missHighB = (Button) view.findViewById(R.id.missHighB);
        scoreLowB = (Button) view.findViewById(R.id.scoreLowB);
        missLowB = (Button) view.findViewById(R.id.missLowB);

        spyC = (CheckBox) view.findViewById(R.id.startSpyC);
        reachC = (CheckBox) view.findViewById(R.id.reachOuterWorksC);
    }

    private void setListeners() {
        // TODO
    }
}
