/*
 * Copyright 2018 Daniel Logan
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
import android.widget.ImageButton;

import org.frc836.database.MatchStatsStruct;
import org.growingstems.scouting.MatchFragment;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;


public class PreMatchFragment extends MatchFragment {


    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int SCALE = 2;

    private boolean displayed = false;

    private MatchStatsStruct tempData = new MatchStatsStruct();

    private Button swap_sides;

    private ImageButton left_switch;
    private ImageButton scale;
    private ImageButton right_switch;


    public PreMatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PreMatch.
     */
    public static PreMatchFragment newInstance() {
        return new PreMatchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pre_match, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getGUIRefs(view);


        if (!Prefs.getRedLeft(getActivity(), true)) {
            tempData.scale_right = true;
            tempData.near_switch_right = true;
            tempData.far_switch_right = true;
        }
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

        data.far_switch_right = tempData.far_switch_right;
        data.near_switch_right = tempData.near_switch_right;
        data.scale_right = tempData.scale_right;

    }

    @Override
    public void loadData(MatchStatsStruct data) {
        tempData = data;
        if (getView() == null || data == null || !displayed)
            return;
        // which side are we using
        boolean redLeft = Prefs.getRedLeft(getActivity(), true);

        if (!redLeft)
            scale.setScaleX(-1f);
        else
            scale.setScaleX(1f);


        Activity act = getActivity();
        String pos;
        if (act instanceof MatchActivity)
            pos = ((MatchActivity) act).getPosition();
        else
            pos = Prefs.getPosition(getActivity(), "Red 1");

        if (pos.contains("Blue") != redLeft) {
            left_switch.setScaleY(data.near_switch_right ? -1f : 1f);
            scale.setScaleY(data.scale_right ? -1f : 1f);
            right_switch.setScaleY(data.far_switch_right ? -1f : 1f);
        } else {
            left_switch.setScaleY(data.near_switch_right ? 1f : -1f);
            scale.setScaleY(data.scale_right ? 1f : -1f);
            right_switch.setScaleY(data.far_switch_right ? 1f : -1f);
        }


    }

    private void getGUIRefs(View view) {
        left_switch = (ImageButton) view.findViewById(R.id.leftSwitchB);
        scale = (ImageButton) view.findViewById(R.id.scaleB);
        right_switch = (ImageButton) view.findViewById(R.id.rightSwitchB);
        swap_sides = (Button) view.findViewById(R.id.switchSidesB);
    }

    private void setListeners() {
        swap_sides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchSides();
            }
        });
        left_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipResource(left_switch, LEFT);
            }
        });
        right_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipResource(right_switch, RIGHT);
            }
        });
        scale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipResource(scale, SCALE);
            }
        });

    }

    private void switchSides() {
        saveData(tempData);
        tempData.scale_right = !tempData.scale_right;
        tempData.near_switch_right = !tempData.near_switch_right;
        tempData.far_switch_right = !tempData.far_switch_right;
        Prefs.setRedLeft(getActivity(), !(Prefs.getRedLeft(getActivity(), true)));
        loadData(tempData);
    }

    private void flipResource(ImageButton resource, int side) {
        boolean redLeft = Prefs.getRedLeft(getActivity(), true);
        Activity act = getActivity();
        String pos;
        if (act instanceof MatchActivity)
            pos = ((MatchActivity) act).getPosition();
        else
            pos = Prefs.getPosition(getActivity(), "Red 1");
        boolean isRed = pos.contains("Red");

        if ((side == RIGHT && ((!redLeft && isRed) || (redLeft && !isRed)))
                || (side == LEFT && ((!redLeft && !isRed) || (redLeft && isRed)))) {
            tempData.near_switch_right = !tempData.near_switch_right;
        } else if (side != SCALE) {
            tempData.far_switch_right = !tempData.far_switch_right;
        } else {
            tempData.scale_right = !tempData.scale_right;
        }

        resource.setScaleY(-1 * resource.getScaleY());
    }

}
