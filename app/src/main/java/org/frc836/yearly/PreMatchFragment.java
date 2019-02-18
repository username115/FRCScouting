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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import org.frc836.database.MatchStatsStruct;
import org.growingstems.scouting.MatchFragment;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;


public class PreMatchFragment extends MatchFragment {


    private boolean displayed = false;

    private MatchStatsStruct tempData = new MatchStatsStruct();

    //private Button swap_sides;

    private ImageButton hab1;
    private ImageButton hab2L;
    private ImageButton hab2R;
    private ImageButton hab3;
    private ImageButton cargo_preload;
    private ImageButton hatch_preload;

    private FrameLayout Lhab1;
    private FrameLayout Lhab2L;
    private FrameLayout Lhab2R;
    private FrameLayout Lhab3;
    private FrameLayout Lcargo_preload;
    private FrameLayout Lhatch_preload;


    private View mainView;

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
        mainView = view;
        getGUIRefs(view);
        //if (!Prefs.getRedLeft(getActivity(), true)) {
        //    tempData.scale_right = true;
        //    tempData.near_switch_right = true;
        //    tempData.far_switch_right = true;
        //}
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

        data.prematch_hab_level = tempData.prematch_hab_level;
        data.prematch_robot_cargo = tempData.prematch_robot_cargo;
        data.prematch_robot_hatch = tempData.prematch_robot_hatch;

    }

    @Override
    public void loadData(MatchStatsStruct data) {
        tempData = data;
        if (getView() == null || data == null || !displayed)
            return;
        // which side are we using
        //boolean redLeft = Prefs.getRedLeft(getActivity(), true);

        //if (!redLeft)
        //    scale.setScaleX(-1f);
        //else
        //    scale.setScaleX(1f);


        Activity act = getActivity();
        String pos;
        if (act instanceof MatchActivity)
            pos = ((MatchActivity) act).getPosition();
        else
            pos = Prefs.getPosition(getActivity(), "Red 1");

        boolean blue = pos.contains("Blue");

        //set colors based on side;
        hab1.setBackgroundResource(blue ? R.drawable.blue_hab_1 : R.drawable.red_hab_1);
        hab2L.setBackgroundResource(blue ? R.drawable.blue_hab_2_left : R.drawable.red_hab_2_left);
        hab2R.setBackgroundResource(blue ? R.drawable.blue_hab_2_right : R.drawable.red_hab_2_right);
        hab3.setBackgroundResource(blue ? R.drawable.blue_hab_3 : R.drawable.red_hab_3);


        Drawable blackBorder = ContextCompat.getDrawable(mainView.getContext(), R.drawable.blackborder);
        Drawable yellowBorder = ContextCompat.getDrawable(mainView.getContext(), R.drawable.yellowborder);
        //set current selections from load
        Lhab1.setForeground(blackBorder);
        Lhab2L.setForeground(blackBorder);
        Lhab2R.setForeground(blackBorder);
        Lhab3.setForeground(blackBorder);
        switch (data.prematch_hab_level) {
            case 1:
                Lhab1.setForeground(yellowBorder);
                break;
            case 2:
                if (data.prematch_hab2_left)
                    Lhab2L.setForeground(yellowBorder);
                else
                    Lhab2R.setForeground(yellowBorder);
                break;
            case 3:
                Lhab3.setForeground(yellowBorder);
                break;
            default:
                break;
        }

        Lcargo_preload.setForeground(data.prematch_robot_cargo ? yellowBorder : blackBorder);
        Lhatch_preload.setForeground(data.prematch_robot_hatch ? yellowBorder : blackBorder);

        //scale.setScaleY((data.scale_right == redLeft) ? -1f : 1f);

        //if (pos.contains("Blue") != redLeft) {
        //    left_switch.setScaleY((data.near_switch_right == redLeft) ? -1f : 1f);
        //    right_switch.setScaleY((data.far_switch_right == redLeft) ? -1f : 1f);
        //} else {
        //    left_switch.setScaleY((data.far_switch_right == redLeft) ? -1f : 1f);
        //    right_switch.setScaleY((data.near_switch_right == redLeft) ? -1f : 1f);
        //}


    }

    private void getGUIRefs(View view) {
        hab1 = view.findViewById(R.id.hab1);
        hab2L = view.findViewById(R.id.hab2Left);
        hab2R = view.findViewById(R.id.hab2Right);
        hab3 = view.findViewById(R.id.hab3);
        cargo_preload = view.findViewById(R.id.cargoB);
        hatch_preload = view.findViewById(R.id.hatchB);

        Lhab1 = view.findViewById(R.id.hab1L);
        Lhab2L = view.findViewById(R.id.hab2LeftL);
        Lhab2R = view.findViewById(R.id.hab2RightL);
        Lhab3 = view.findViewById(R.id.hab3L);
        Lcargo_preload = view.findViewById(R.id.cargoL);
        Lhatch_preload = view.findViewById(R.id.hatchL);
    }

    private void setListeners() {
        //swap_sides.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        switchSides();
        //    }
        //});
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


        cargo_preload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCargoHatch(false);
            }
        });
        hatch_preload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCargoHatch(true);
            }
        });

    }

    private void toggleHabLevel(int level, boolean left2) {
        if (tempData.prematch_hab_level == level && (tempData.prematch_hab2_left == left2 || level != 2)) {
            //selected already selected level, deselect
            tempData.prematch_hab_level = 0;
            tempData.prematch_hab2_left = false;
        } else {
            tempData.prematch_hab_level = level;
            tempData.prematch_hab2_left = (level == 2 && left2);
        }
        loadData(tempData); //apply to UI
    }

    private void toggleCargoHatch(boolean hatch) {
        if (hatch) {
            tempData.prematch_robot_hatch = !tempData.prematch_robot_hatch;
            tempData.prematch_robot_cargo = false;
        } else {
            tempData.prematch_robot_cargo = !tempData.prematch_robot_cargo;
            tempData.prematch_robot_hatch = false;
        }
        loadData(tempData);
    }

    //private void switchSides() {
    //    saveData(tempData);
    //    tempData.scale_right = !tempData.scale_right;
    //    tempData.near_switch_right = !tempData.near_switch_right;
    //    tempData.far_switch_right = !tempData.far_switch_right;
    //    Prefs.setRedLeft(getActivity(), !(Prefs.getRedLeft(getActivity(), true)));
    //    loadData(tempData);
    //}

}
