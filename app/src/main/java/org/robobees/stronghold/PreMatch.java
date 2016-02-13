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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;

public class PreMatch extends MatchFragment {

    private static final int LEFT_5 = 0;
    private static final int LEFT_4 = 1;
    private static final int LEFT_3 = 2;
    private static final int LEFT_2 = 3;
    private static final int RIGHT_2 = 4;
    private static final int RIGHT_3 = 5;
    private static final int RIGHT_4 = 6;
    private static final int RIGHT_5 = 7;
    private static final int NUM_DEF = 8;

    private SparseArray<Spinner> spinners;
    private SparseArray<ImageView> images;
    private SparseIntArray resources;

    private LinearLayout leftL;
    private LinearLayout rightL;
    private Button swapB;

    private MatchStatsSH tempData = new MatchStatsSH();

    public PreMatch() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PreMatch.
     */
    public static PreMatch newInstance() {
        PreMatch fragment = new PreMatch();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spinners = new SparseArray<Spinner>(NUM_DEF);
        images = new SparseArray<ImageView>(NUM_DEF);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pre_match, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getGUIRefs(view);
        setListeners();
    }

    public void onResume() {
        super.onResume();
        if (Prefs.getRedLeft(getActivity(), true)) {
            leftL.setBackgroundResource(R.color.red);
            rightL.setBackgroundResource(R.color.blue);
        } else {
            leftL.setBackgroundResource(R.color.blue);
            rightL.setBackgroundResource(R.color.red);
        }
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
        if (Prefs.getRedLeft(getActivity(), true)) {
            data.red_def_5 = (short)spinners.get(LEFT_5).getSelectedItemPosition();
            data.red_def_4 = (short)spinners.get(LEFT_4).getSelectedItemPosition();
            data.red_def_3 = (short)spinners.get(LEFT_3).getSelectedItemPosition();
            data.red_def_2 = (short)spinners.get(LEFT_2).getSelectedItemPosition();

            data.blue_def_2 = (short)spinners.get(RIGHT_2).getSelectedItemPosition();
            data.blue_def_3 = (short)spinners.get(RIGHT_3).getSelectedItemPosition();
            data.blue_def_4 = (short)spinners.get(RIGHT_4).getSelectedItemPosition();
            data.blue_def_5 = (short)spinners.get(RIGHT_5).getSelectedItemPosition();

        }
        else {
            data.red_def_5 = (short)spinners.get(RIGHT_5).getSelectedItemPosition();
            data.red_def_4 = (short)spinners.get(RIGHT_4).getSelectedItemPosition();
            data.red_def_3 = (short)spinners.get(RIGHT_3).getSelectedItemPosition();
            data.red_def_2 = (short)spinners.get(RIGHT_2).getSelectedItemPosition();

            data.blue_def_2 = (short)spinners.get(LEFT_2).getSelectedItemPosition();
            data.blue_def_3 = (short)spinners.get(LEFT_3).getSelectedItemPosition();
            data.blue_def_4 = (short)spinners.get(LEFT_4).getSelectedItemPosition();
            data.blue_def_5 = (short)spinners.get(LEFT_5).getSelectedItemPosition();
        }
    }

    @Override
    public void loadData(MatchStatsSH data) {
        if (getView() == null)
            return;
        if (Prefs.getRedLeft(getActivity(), true)) {
            spinners.get(LEFT_5).setSelection(data.red_def_5);
            spinners.get(LEFT_4).setSelection(data.red_def_4);
            spinners.get(LEFT_3).setSelection(data.red_def_3);
            spinners.get(LEFT_2).setSelection(data.red_def_2);

            spinners.get(RIGHT_2).setSelection(data.blue_def_2);
            spinners.get(RIGHT_3).setSelection(data.blue_def_3);
            spinners.get(RIGHT_4).setSelection(data.blue_def_4);
            spinners.get(RIGHT_5).setSelection(data.blue_def_5);

            images.get(LEFT_5).setImageResource(resources.get(data.red_def_5));
            images.get(LEFT_4).setImageResource(resources.get(data.red_def_4));
            images.get(LEFT_3).setImageResource(resources.get(data.red_def_3));
            images.get(LEFT_2).setImageResource(resources.get(data.red_def_2));

            images.get(RIGHT_2).setImageResource(resources.get(data.blue_def_2));
            images.get(RIGHT_3).setImageResource(resources.get(data.blue_def_3));
            images.get(RIGHT_4).setImageResource(resources.get(data.blue_def_4));
            images.get(RIGHT_5).setImageResource(resources.get(data.blue_def_5));
        }
        else {
            spinners.get(RIGHT_5).setSelection(data.red_def_5);
            spinners.get(RIGHT_4).setSelection(data.red_def_4);
            spinners.get(RIGHT_3).setSelection(data.red_def_3);
            spinners.get(RIGHT_2).setSelection(data.red_def_2);

            spinners.get(LEFT_2).setSelection(data.blue_def_2);
            spinners.get(LEFT_3).setSelection(data.blue_def_3);
            spinners.get(LEFT_4).setSelection(data.blue_def_4);
            spinners.get(LEFT_5).setSelection(data.blue_def_5);

            images.get(RIGHT_5).setImageResource(resources.get(data.red_def_5));
            images.get(RIGHT_4).setImageResource(resources.get(data.red_def_4));
            images.get(RIGHT_3).setImageResource(resources.get(data.red_def_3));
            images.get(RIGHT_2).setImageResource(resources.get(data.red_def_2));

            images.get(LEFT_2).setImageResource(resources.get(data.blue_def_2));
            images.get(LEFT_3).setImageResource(resources.get(data.blue_def_3));
            images.get(LEFT_4).setImageResource(resources.get(data.blue_def_4));
            images.get(LEFT_5).setImageResource(resources.get(data.blue_def_5));
        }
    }

    private void onSwap() {
        saveData(tempData);
        if (Prefs.getRedLeft(getActivity(), true)) {
            leftL.setBackgroundResource(R.color.blue);
            rightL.setBackgroundResource(R.color.red);
            Prefs.setRedLeft(getActivity(), false);
        } else {
            leftL.setBackgroundResource(R.color.red);
            rightL.setBackgroundResource(R.color.blue);
            Prefs.setRedLeft(getActivity(), true);
        }
        loadData(tempData);
    }

    private void getGUIRefs(View view) {
        leftL = (LinearLayout) view.findViewById(R.id.LeftPreMatch);
        rightL = (LinearLayout) view.findViewById(R.id.RightPreMatch);
        swapB = (Button) view.findViewById(R.id.swapSidesPreB);

        spinners.put(LEFT_5, (Spinner) view.findViewById(R.id.leftDefense5S));
        spinners.put(LEFT_4, (Spinner) view.findViewById(R.id.leftDefense4S));
        spinners.put(LEFT_3, (Spinner) view.findViewById(R.id.leftDefense3S));
        spinners.put(LEFT_2, (Spinner) view.findViewById(R.id.leftDefense2S));

        spinners.put(RIGHT_2, (Spinner) view.findViewById(R.id.rightDefense2S));
        spinners.put(RIGHT_3, (Spinner) view.findViewById(R.id.rightDefense3S));
        spinners.put(RIGHT_4, (Spinner) view.findViewById(R.id.rightDefense4S));
        spinners.put(RIGHT_5, (Spinner) view.findViewById(R.id.rightDefense5S));

        images.put(LEFT_5, (ImageView) view.findViewById(R.id.leftDefense5P));
        images.put(LEFT_4, (ImageView) view.findViewById(R.id.leftDefense4P));
        images.put(LEFT_3, (ImageView) view.findViewById(R.id.leftDefense3P));
        images.put(LEFT_2, (ImageView) view.findViewById(R.id.leftDefense2P));

        images.put(RIGHT_2, (ImageView) view.findViewById(R.id.rightDefense2P));
        images.put(RIGHT_3, (ImageView) view.findViewById(R.id.rightDefense3P));
        images.put(RIGHT_4, (ImageView) view.findViewById(R.id.rightDefense4P));
        images.put(RIGHT_5, (ImageView) view.findViewById(R.id.rightDefense5P));
    }

    private void setListeners() {
        for (int i = 0; i < NUM_DEF; i++) {
            spinners.get(i).setOnItemSelectedListener(new OnDefenseChangedListener(i));
            images.get(i).setOnClickListener(new OnDefenseImageClickListener(i));
        }
        swapB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwap();
            }
        });
    }

    private class OnDefenseChangedListener implements AdapterView.OnItemSelectedListener {

        private int viewIndex;

        public OnDefenseChangedListener(int view) {
            super();
            viewIndex = view;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            images.get(viewIndex).setImageResource(resources.get(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // do nothing
        }
    }

    private class OnDefenseImageClickListener implements View.OnClickListener {

        private int viewIndex;

        public OnDefenseImageClickListener(int view) {
            super();
            viewIndex = view;
        }

        @Override
        public void onClick(View v) {
            spinners.get(viewIndex).performClick();
        }
    }
}
