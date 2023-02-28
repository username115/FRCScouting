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

import static org.frc836.yearly.GridClickListener.GridContents;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import org.frc836.database.MatchStatsStruct;
import org.growingstems.scouting.MatchFragment;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;


public class AutoMatchFragment extends MatchFragment {

    private final MatchStatsStruct tempData = new MatchStatsStruct();

    private boolean displayed = false;

    private TextView autoGridLeftTitle;
    private TextView autoGridRightTitle;

    private LinearLayout autoMobilityLeftL;
    private LinearLayout autoMobilityRightL;
    private LinearLayout autoChargeLeftL;
    private LinearLayout autoChargeRightL;
    private ImageView autoGridviewL;
    private ImageView autoGridviewR;


    private Drawable checkmarks;
    private Button mobilityB;
    private Button chargeB;


    private ImageButton gridSubTopSub;
    private ImageButton gridSubTopMid;
    private ImageButton gridSubTopWall;
    private ImageButton gridSubMidSub;
    private ImageButton gridSubMidMid;
    private ImageButton gridSubMidWall;
    private ImageButton gridSubHybSub;
    private ImageButton gridSubHybMid;
    private ImageButton gridSubHybWall;

    private ImageButton gridCoopTopSub;
    private ImageButton gridCoopTopMid;
    private ImageButton gridCoopTopWall;
    private ImageButton gridCoopMidSub;
    private ImageButton gridCoopMidMid;
    private ImageButton gridCoopMidWall;
    private ImageButton gridCoopHybSub;
    private ImageButton gridCoopHybMid;
    private ImageButton gridCoopHybWall;

    private ImageButton gridWallTopSub;
    private ImageButton gridWallTopMid;
    private ImageButton gridWallTopWall;
    private ImageButton gridWallMidSub;
    private ImageButton gridWallMidMid;
    private ImageButton gridWallMidWall;
    private ImageButton gridWallHybSub;
    private ImageButton gridWallHybMid;
    private ImageButton gridWallHybWall;

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
        getBaseGUIRefs(view);
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

        MatchStatsYearly.copyAuto(tempData, data);
    }

    @Override
    public void loadData(@NonNull MatchStatsStruct data) {
        tempData.copyFrom(data);
        View view = getView();
        if (view == null || !displayed)
            return;
        updateSide(view);
        setListeners();

        setMobility(data);
        setChargeStation(data);

        Activity act = getActivity();
        if (act == null)
            return;

        gridSubTopSub.setForeground(data.auto_substn_grid_top_substn ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridSubTopMid.setForeground(data.auto_substn_grid_top_mid ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cube) : null);
        gridSubTopWall.setForeground(data.auto_substn_grid_top_wall ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridSubMidSub.setForeground(data.auto_substn_grid_mid_substn ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridSubMidMid.setForeground(data.auto_substn_grid_mid_mid ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cube) : null);
        gridSubMidWall.setForeground(data.auto_substn_grid_mid_wall ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridSubHybSub.setForeground(data.auto_substn_grid_hyb_substn ? AppCompatResources.getDrawable(act, R.drawable.selection_square_hybrid) : null);
        gridSubHybMid.setForeground(data.auto_substn_grid_hyb_mid ? AppCompatResources.getDrawable(act, R.drawable.selection_square_hybrid) : null);
        gridSubHybWall.setForeground(data.auto_substn_grid_hyb_wall ? AppCompatResources.getDrawable(act, R.drawable.selection_square_hybrid) : null);
        gridCoopTopSub.setForeground(data.auto_coop_grid_top_substn ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridCoopTopMid.setForeground(data.auto_coop_grid_top_mid ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cube) : null);
        gridCoopTopWall.setForeground(data.auto_coop_grid_top_wall ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridCoopMidSub.setForeground(data.auto_coop_grid_mid_substn ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridCoopMidMid.setForeground(data.auto_coop_grid_mid_mid ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cube) : null);
        gridCoopMidWall.setForeground(data.auto_coop_grid_mid_wall ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridCoopHybSub.setForeground(data.auto_coop_grid_hyb_substn ? AppCompatResources.getDrawable(act, R.drawable.selection_square_hybrid) : null);
        gridCoopHybMid.setForeground(data.auto_coop_grid_hyb_mid ? AppCompatResources.getDrawable(act, R.drawable.selection_square_hybrid) : null);
        gridCoopHybWall.setForeground(data.auto_coop_grid_hyb_wall ? AppCompatResources.getDrawable(act, R.drawable.selection_square_hybrid) : null);
        gridWallTopSub.setForeground(data.auto_wall_grid_top_substn ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridWallTopMid.setForeground(data.auto_wall_grid_top_mid ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cube) : null);
        gridWallTopWall.setForeground(data.auto_wall_grid_top_wall ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridWallMidSub.setForeground(data.auto_wall_grid_mid_substn ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridWallMidMid.setForeground(data.auto_wall_grid_mid_mid ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cube) : null);
        gridWallMidWall.setForeground(data.auto_wall_grid_mid_wall ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridWallHybSub.setForeground(data.auto_wall_grid_hyb_substn ? AppCompatResources.getDrawable(act, R.drawable.selection_square_hybrid) : null);
        gridWallHybMid.setForeground(data.auto_wall_grid_hyb_mid ? AppCompatResources.getDrawable(act, R.drawable.selection_square_hybrid) : null);
        gridWallHybWall.setForeground(data.auto_wall_grid_hyb_wall ? AppCompatResources.getDrawable(act, R.drawable.selection_square_hybrid) : null);

    }

    private void updateSide(View view) {
        Activity act = getActivity();
        boolean redLeft = Prefs.getRedLeft(act, true);
        String pos;
        if (act instanceof MatchActivity)
            pos = ((MatchActivity) act).getPosition();
        else
            pos = Prefs.getPosition(act, "Red 1");

        if ((pos.contains("Blue") && !redLeft) || ((!pos.contains("Blue") && redLeft))) {
            //Left side layout
            autoGridLeftTitle.setText(R.string.top_grid_abbr);
            autoGridRightTitle.setText(R.string.hybrid_grid_abbr);
            autoMobilityLeftL.setVisibility(View.GONE);
            autoChargeLeftL.setVisibility(View.GONE);
            autoGridviewL.setVisibility(View.GONE);
            autoMobilityRightL.setVisibility(View.VISIBLE);
            autoChargeRightL.setVisibility(View.VISIBLE);
            autoGridviewR.setVisibility(View.VISIBLE);

            mobilityB = view.findViewById(R.id.mobilityRB);
            chargeB = view.findViewById(R.id.autoChargeRB);

            if (pos.contains("Blue")) {
                //substation at top of screen, hybrid on right
                gridSubTopSub = view.findViewById(R.id.autoGridTopTopLeft);
                gridSubTopMid = view.findViewById(R.id.autoGridTopMidLeft);
                gridSubTopWall = view.findViewById(R.id.autoGridTopBotLeft);
                gridSubMidSub = view.findViewById(R.id.autoGridTopTopMid);
                gridSubMidMid = view.findViewById(R.id.autoGridTopMidMid);
                gridSubMidWall = view.findViewById(R.id.autoGridTopBotMid);
                gridSubHybSub = view.findViewById(R.id.autoGridTopTopRight);
                gridSubHybMid = view.findViewById(R.id.autoGridTopMidRight);
                gridSubHybWall = view.findViewById(R.id.autoGridTopBotRight);

                gridCoopTopSub = view.findViewById(R.id.autoGridMidTopLeft);
                gridCoopTopMid = view.findViewById(R.id.autoGridMidMidLeft);
                gridCoopTopWall = view.findViewById(R.id.autoGridMidBotLeft);
                gridCoopMidSub = view.findViewById(R.id.autoGridMidTopMid);
                gridCoopMidMid = view.findViewById(R.id.autoGridMidMidMid);
                gridCoopMidWall = view.findViewById(R.id.autoGridMidBotMid);
                gridCoopHybSub = view.findViewById(R.id.autoGridMidTopRight);
                gridCoopHybMid = view.findViewById(R.id.autoGridMidMidRight);
                gridCoopHybWall = view.findViewById(R.id.autoGridMidBotRight);

                gridWallTopSub = view.findViewById(R.id.autoGridBotTopLeft);
                gridWallTopMid = view.findViewById(R.id.autoGridBotMidLeft);
                gridWallTopWall = view.findViewById(R.id.autoGridBotBotLeft);
                gridWallMidSub = view.findViewById(R.id.autoGridBotTopMid);
                gridWallMidMid = view.findViewById(R.id.autoGridBotMidMid);
                gridWallMidWall = view.findViewById(R.id.autoGridBotBotMid);
                gridWallHybSub = view.findViewById(R.id.autoGridBotTopRight);
                gridWallHybMid = view.findViewById(R.id.autoGridBotMidRight);
                gridWallHybWall = view.findViewById(R.id.autoGridBotBotRight);

            } else {
                //substation at bottom of screen, hybrid on right
                gridSubTopSub = view.findViewById(R.id.autoGridBotTopLeft);
                gridSubTopMid = view.findViewById(R.id.autoGridBotMidLeft);
                gridSubTopWall = view.findViewById(R.id.autoGridBotBotLeft);
                gridSubMidSub = view.findViewById(R.id.autoGridBotTopMid);
                gridSubMidMid = view.findViewById(R.id.autoGridBotMidMid);
                gridSubMidWall = view.findViewById(R.id.autoGridBotBotMid);
                gridSubHybSub = view.findViewById(R.id.autoGridBotTopRight);
                gridSubHybMid = view.findViewById(R.id.autoGridBotMidRight);
                gridSubHybWall = view.findViewById(R.id.autoGridBotBotRight);

                gridCoopTopSub = view.findViewById(R.id.autoGridMidTopLeft);
                gridCoopTopMid = view.findViewById(R.id.autoGridMidMidLeft);
                gridCoopTopWall = view.findViewById(R.id.autoGridMidBotLeft);
                gridCoopMidSub = view.findViewById(R.id.autoGridMidTopMid);
                gridCoopMidMid = view.findViewById(R.id.autoGridMidMidMid);
                gridCoopMidWall = view.findViewById(R.id.autoGridMidBotMid);
                gridCoopHybSub = view.findViewById(R.id.autoGridMidTopRight);
                gridCoopHybMid = view.findViewById(R.id.autoGridMidMidRight);
                gridCoopHybWall = view.findViewById(R.id.autoGridMidBotRight);

                gridWallTopSub = view.findViewById(R.id.autoGridTopTopLeft);
                gridWallTopMid = view.findViewById(R.id.autoGridTopMidLeft);
                gridWallTopWall = view.findViewById(R.id.autoGridTopBotLeft);
                gridWallMidSub = view.findViewById(R.id.autoGridTopTopMid);
                gridWallMidMid = view.findViewById(R.id.autoGridTopMidMid);
                gridWallMidWall = view.findViewById(R.id.autoGridTopBotMid);
                gridWallHybSub = view.findViewById(R.id.autoGridTopTopRight);
                gridWallHybMid = view.findViewById(R.id.autoGridTopMidRight);
                gridWallHybWall = view.findViewById(R.id.autoGridTopBotRight);
            }

        } else {
            //Right side layout
            autoGridLeftTitle.setText(R.string.hybrid_grid_abbr);
            autoGridRightTitle.setText(R.string.top_grid_abbr);
            autoMobilityLeftL.setVisibility(View.VISIBLE);
            autoChargeLeftL.setVisibility(View.VISIBLE);
            autoGridviewL.setVisibility(View.VISIBLE);
            autoMobilityRightL.setVisibility(View.GONE);
            autoChargeRightL.setVisibility(View.GONE);
            autoGridviewR.setVisibility(View.GONE);

            mobilityB = view.findViewById(R.id.mobilityLB);
            chargeB = view.findViewById(R.id.autoChargeLB);

            if (pos.contains("Blue")) {
                //substation at bottom of screen, hybrid on left
                gridSubTopSub = view.findViewById(R.id.autoGridBotTopRight);
                gridSubTopMid = view.findViewById(R.id.autoGridBotMidRight);
                gridSubTopWall = view.findViewById(R.id.autoGridBotBotRight);
                gridSubMidSub = view.findViewById(R.id.autoGridBotTopMid);
                gridSubMidMid = view.findViewById(R.id.autoGridBotMidMid);
                gridSubMidWall = view.findViewById(R.id.autoGridBotBotMid);
                gridSubHybSub = view.findViewById(R.id.autoGridBotTopLeft);
                gridSubHybMid = view.findViewById(R.id.autoGridBotMidLeft);
                gridSubHybWall = view.findViewById(R.id.autoGridBotBotLeft);

                gridCoopTopSub = view.findViewById(R.id.autoGridMidTopRight);
                gridCoopTopMid = view.findViewById(R.id.autoGridMidMidRight);
                gridCoopTopWall = view.findViewById(R.id.autoGridMidBotRight);
                gridCoopMidSub = view.findViewById(R.id.autoGridMidTopMid);
                gridCoopMidMid = view.findViewById(R.id.autoGridMidMidMid);
                gridCoopMidWall = view.findViewById(R.id.autoGridMidBotMid);
                gridCoopHybSub = view.findViewById(R.id.autoGridMidTopLeft);
                gridCoopHybMid = view.findViewById(R.id.autoGridMidMidLeft);
                gridCoopHybWall = view.findViewById(R.id.autoGridMidBotLeft);

                gridWallTopSub = view.findViewById(R.id.autoGridTopTopRight);
                gridWallTopMid = view.findViewById(R.id.autoGridTopMidRight);
                gridWallTopWall = view.findViewById(R.id.autoGridTopBotRight);
                gridWallMidSub = view.findViewById(R.id.autoGridTopTopMid);
                gridWallMidMid = view.findViewById(R.id.autoGridTopMidMid);
                gridWallMidWall = view.findViewById(R.id.autoGridTopBotMid);
                gridWallHybSub = view.findViewById(R.id.autoGridTopTopLeft);
                gridWallHybMid = view.findViewById(R.id.autoGridTopMidLeft);
                gridWallHybWall = view.findViewById(R.id.autoGridTopBotLeft);

            } else {
                //substation at top of screen, hybrid on left
                gridSubTopSub = view.findViewById(R.id.autoGridTopTopRight);
                gridSubTopMid = view.findViewById(R.id.autoGridTopMidRight);
                gridSubTopWall = view.findViewById(R.id.autoGridTopBotRight);
                gridSubMidSub = view.findViewById(R.id.autoGridTopTopMid);
                gridSubMidMid = view.findViewById(R.id.autoGridTopMidMid);
                gridSubMidWall = view.findViewById(R.id.autoGridTopBotMid);
                gridSubHybSub = view.findViewById(R.id.autoGridTopTopLeft);
                gridSubHybMid = view.findViewById(R.id.autoGridTopMidLeft);
                gridSubHybWall = view.findViewById(R.id.autoGridTopBotLeft);

                gridCoopTopSub = view.findViewById(R.id.autoGridMidTopRight);
                gridCoopTopMid = view.findViewById(R.id.autoGridMidMidRight);
                gridCoopTopWall = view.findViewById(R.id.autoGridMidBotRight);
                gridCoopMidSub = view.findViewById(R.id.autoGridMidTopMid);
                gridCoopMidMid = view.findViewById(R.id.autoGridMidMidMid);
                gridCoopMidWall = view.findViewById(R.id.autoGridMidBotMid);
                gridCoopHybSub = view.findViewById(R.id.autoGridMidTopLeft);
                gridCoopHybMid = view.findViewById(R.id.autoGridMidMidLeft);
                gridCoopHybWall = view.findViewById(R.id.autoGridMidBotLeft);

                gridWallTopSub = view.findViewById(R.id.autoGridBotTopRight);
                gridWallTopMid = view.findViewById(R.id.autoGridBotMidRight);
                gridWallTopWall = view.findViewById(R.id.autoGridBotBotRight);
                gridWallMidSub = view.findViewById(R.id.autoGridBotTopMid);
                gridWallMidMid = view.findViewById(R.id.autoGridBotMidMid);
                gridWallMidWall = view.findViewById(R.id.autoGridBotBotMid);
                gridWallHybSub = view.findViewById(R.id.autoGridBotTopLeft);
                gridWallHybMid = view.findViewById(R.id.autoGridBotMidLeft);
                gridWallHybWall = view.findViewById(R.id.autoGridBotBotLeft);
            }
        }
    }

    private void getBaseGUIRefs(View view) {

        autoGridLeftTitle = view.findViewById(R.id.autoGridLeftTitle);
        autoGridRightTitle = view.findViewById(R.id.autoGridRightTitle);
        autoMobilityLeftL = view.findViewById(R.id.autoMobilityLL);
        autoMobilityRightL = view.findViewById(R.id.autoMobilityRL);
        autoChargeLeftL = view.findViewById(R.id.autoChargeLL);
        autoChargeRightL = view.findViewById(R.id.autoChargeRL);
        autoGridviewL = view.findViewById(R.id.autoGridViewL);
        autoGridviewR = view.findViewById(R.id.autoGridViewR);

        checkmarks = getActivity() == null ? null : AppCompatResources.getDrawable(getActivity(), R.drawable.icon_awesome_check_double);

    }

    private void setListeners() {
        mobilityB.setOnClickListener(new MobilityClickListener());
        chargeB.setOnClickListener(new ChargeStationClickListener());

        Activity act = getActivity();
        if (act == null)
            return;

        gridSubTopSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_substn_grid_top_substn = !tempData.auto_substn_grid_top_substn;
            return tempData.auto_substn_grid_top_substn ? GridContents.CONE : GridContents.NONE;
        }, tempData));
        gridSubTopMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_substn_grid_top_mid = !tempData.auto_substn_grid_top_mid;
            return tempData.auto_substn_grid_top_mid ? GridContents.CUBE : GridContents.NONE;
        }, tempData));
        gridSubTopWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_substn_grid_top_wall = !tempData.auto_substn_grid_top_wall;
            return tempData.auto_substn_grid_top_wall ? GridContents.CONE : GridContents.NONE;
        }, tempData));
        gridSubMidSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_substn_grid_mid_substn = !tempData.auto_substn_grid_mid_substn;
            return tempData.auto_substn_grid_mid_substn ? GridContents.CONE : GridContents.NONE;
        }, tempData));
        gridSubMidMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_substn_grid_mid_mid = !tempData.auto_substn_grid_mid_mid;
            return tempData.auto_substn_grid_mid_mid ? GridContents.CUBE : GridContents.NONE;
        }, tempData));
        gridSubMidWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_substn_grid_mid_wall = !tempData.auto_substn_grid_mid_wall;
            return tempData.auto_substn_grid_mid_wall ? GridContents.CONE : GridContents.NONE;
        }, tempData));
        gridSubHybSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_substn_grid_hyb_substn = !tempData.auto_substn_grid_hyb_substn;
            return tempData.auto_substn_grid_hyb_substn ? GridContents.HYBRID : GridContents.NONE;
        }, tempData));
        gridSubHybMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_substn_grid_hyb_mid = !tempData.auto_substn_grid_hyb_mid;
            return tempData.auto_substn_grid_hyb_mid ? GridContents.HYBRID : GridContents.NONE;
        }, tempData));
        gridSubHybWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_substn_grid_hyb_wall = !tempData.auto_substn_grid_hyb_wall;
            return tempData.auto_substn_grid_hyb_wall ? GridContents.HYBRID : GridContents.NONE;
        }, tempData));

        gridCoopTopSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_coop_grid_top_substn = !tempData.auto_coop_grid_top_substn;
            return tempData.auto_coop_grid_top_substn ? GridContents.CONE : GridContents.NONE;
        }, tempData));
        gridCoopTopMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_coop_grid_top_mid = !tempData.auto_coop_grid_top_mid;
            return tempData.auto_coop_grid_top_mid ? GridContents.CUBE : GridContents.NONE;
        }, tempData));
        gridCoopTopWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_coop_grid_top_wall = !tempData.auto_coop_grid_top_wall;
            return tempData.auto_coop_grid_top_wall ? GridContents.CONE : GridContents.NONE;
        }, tempData));
        gridCoopMidSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_coop_grid_mid_substn = !tempData.auto_coop_grid_mid_substn;
            return tempData.auto_coop_grid_mid_substn ? GridContents.CONE : GridContents.NONE;
        }, tempData));
        gridCoopMidMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_coop_grid_mid_mid = !tempData.auto_coop_grid_mid_mid;
            return tempData.auto_coop_grid_mid_mid ? GridContents.CUBE : GridContents.NONE;
        }, tempData));
        gridCoopMidWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_coop_grid_mid_wall = !tempData.auto_coop_grid_mid_wall;
            return tempData.auto_coop_grid_mid_wall ? GridContents.CONE : GridContents.NONE;
        }, tempData));
        gridCoopHybSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_coop_grid_hyb_substn = !tempData.auto_coop_grid_hyb_substn;
            return tempData.auto_coop_grid_hyb_substn ? GridContents.HYBRID : GridContents.NONE;
        }, tempData));
        gridCoopHybMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_coop_grid_hyb_mid = !tempData.auto_coop_grid_hyb_mid;
            return tempData.auto_coop_grid_hyb_mid ? GridContents.HYBRID : GridContents.NONE;
        }, tempData));
        gridCoopHybWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_coop_grid_hyb_wall = !tempData.auto_coop_grid_hyb_wall;
            return tempData.auto_coop_grid_hyb_wall ? GridContents.HYBRID : GridContents.NONE;
        }, tempData));

        gridWallTopSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_wall_grid_top_substn = !tempData.auto_wall_grid_top_substn;
            return tempData.auto_wall_grid_top_substn ? GridContents.CONE : GridContents.NONE;
        }, tempData));
        gridWallTopMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_wall_grid_top_mid = !tempData.auto_wall_grid_top_mid;
            return tempData.auto_wall_grid_top_mid ? GridContents.CUBE : GridContents.NONE;
        }, tempData));
        gridWallTopWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_wall_grid_top_wall = !tempData.auto_wall_grid_top_wall;
            return tempData.auto_wall_grid_top_wall ? GridContents.CONE : GridContents.NONE;
        }, tempData));
        gridWallMidSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_wall_grid_mid_substn = !tempData.auto_wall_grid_mid_substn;
            return tempData.auto_wall_grid_mid_substn ? GridContents.CONE : GridContents.NONE;
        }, tempData));
        gridWallMidMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_wall_grid_mid_mid = !tempData.auto_wall_grid_mid_mid;
            return tempData.auto_wall_grid_mid_mid ? GridContents.CUBE : GridContents.NONE;
        }, tempData));
        gridWallMidWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_wall_grid_mid_wall = !tempData.auto_wall_grid_mid_wall;
            return tempData.auto_wall_grid_mid_wall ? GridContents.CONE : GridContents.NONE;
        }, tempData));
        gridWallHybSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_wall_grid_hyb_substn = !tempData.auto_wall_grid_hyb_substn;
            return tempData.auto_wall_grid_hyb_substn ? GridContents.HYBRID : GridContents.NONE;
        }, tempData));
        gridWallHybMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_wall_grid_hyb_mid = !tempData.auto_wall_grid_hyb_mid;
            return tempData.auto_wall_grid_hyb_mid ? GridContents.HYBRID : GridContents.NONE;
        }, tempData));
        gridWallHybWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_wall_grid_hyb_wall = !tempData.auto_wall_grid_hyb_wall;
            return tempData.auto_wall_grid_hyb_wall ? GridContents.HYBRID : GridContents.NONE;
        }, tempData));
    }

    private void setMobility(@NonNull MatchStatsStruct data) {
        mobilityB.setForeground(data.auto_mobility ? checkmarks : null);
    }

    private void setChargeStation(@NonNull MatchStatsStruct data) {
        switch (data.auto_charge_station) {
            case 1:
                chargeB.setText(R.string.station_attempt);
                break;
            case 2:
                chargeB.setText(R.string.station_docked);
                break;
            case 3:
                chargeB.setText(R.string.station_engaged);
                break;
            case 0:
            default:
                data.auto_charge_station = 0;
                chargeB.setText(R.string.station_no_attempt);
                break;
        }
    }

    private class MobilityClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            tempData.auto_mobility = !tempData.auto_mobility;

            setMobility(tempData);
        }
    }

    private class ChargeStationClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            tempData.auto_charge_station++;
            setChargeStation(tempData);
        }
    }
}
