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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Spinner;
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
    private Space autoSpaceLeft;
    private TextView autoGridRightTitle;
    private Space autoSpaceRight;

    private LinearLayout autoMobilityLeftL;
    private LinearLayout autoMobilityRightL;
    private LinearLayout autoChargeLeftL;
    private LinearLayout autoChargeRightL;
    private ImageView autoGridviewL;
    private ImageView autoGridviewR;


    private Drawable checkmarks;
    private Button mobilityB;
    private Spinner chargeS;


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

        tempData.auto_charge_station = chargeS.getSelectedItemPosition();

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

        tempData.auto_mobility = data.auto_mobility;
        setMobility(data);
        tempData.charge_station = data.charge_station;
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
            autoSpaceLeft.setVisibility(View.GONE);
            autoGridRightTitle.setText(R.string.hybrid_grid_abbr);
            autoSpaceRight.setVisibility(View.VISIBLE);
            autoMobilityLeftL.setVisibility(View.GONE);
            autoChargeLeftL.setVisibility(View.GONE);
            autoGridviewL.setVisibility(View.GONE);
            autoMobilityRightL.setVisibility(View.VISIBLE);
            autoChargeRightL.setVisibility(View.VISIBLE);
            autoGridviewR.setVisibility(View.VISIBLE);

            mobilityB = view.findViewById(R.id.mobilityRB);
            chargeS = view.findViewById(R.id.autoChargeRS);

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
            autoSpaceLeft.setVisibility(View.VISIBLE);
            autoGridRightTitle.setText(R.string.top_grid_abbr);
            autoSpaceRight.setVisibility(View.GONE);
            autoMobilityLeftL.setVisibility(View.VISIBLE);
            autoChargeLeftL.setVisibility(View.VISIBLE);
            autoGridviewL.setVisibility(View.VISIBLE);
            autoMobilityRightL.setVisibility(View.GONE);
            autoChargeRightL.setVisibility(View.GONE);
            autoGridviewR.setVisibility(View.GONE);

            mobilityB = view.findViewById(R.id.mobilityLB);
            chargeS = view.findViewById(R.id.autoChargeLS);

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
        int color;
        if (pos.contains("Blue")) {
            color = Color.BLUE;
            view.findViewById(R.id.autoTopGridLayout).setBackgroundResource(R.drawable.blueborder);
            view.findViewById(R.id.autoMidGridLayout).setBackgroundResource(R.drawable.blueborder);
            view.findViewById(R.id.autoBotGridLayout).setBackgroundResource(R.drawable.blueborder);
        } else {
            color = Color.RED;
            view.findViewById(R.id.autoTopGridLayout).setBackgroundResource(R.drawable.redborder);
            view.findViewById(R.id.autoMidGridLayout).setBackgroundResource(R.drawable.redborder);
            view.findViewById(R.id.autoBotGridLayout).setBackgroundResource(R.drawable.redborder);
        }

        gridSubTopSub.setBackgroundColor(color & MatchActivity.GRID_BACK_CONE_ALPHA);
        gridSubTopMid.setBackgroundColor(MatchActivity.GRID_BACK_CUBE);
        gridSubTopWall.setBackgroundColor(color & MatchActivity.GRID_BACK_CONE_ALPHA);
        gridSubMidSub.setBackgroundColor(color & MatchActivity.GRID_BACK_CONE_ALPHA);
        gridSubMidMid.setBackgroundColor(MatchActivity.GRID_BACK_CUBE);
        gridSubMidWall.setBackgroundColor(color & MatchActivity.GRID_BACK_CONE_ALPHA);
        gridSubHybSub.setBackgroundColor(MatchActivity.GRID_BACK_HYBRID);
        gridSubHybMid.setBackgroundColor(MatchActivity.GRID_BACK_HYBRID);
        gridSubHybWall.setBackgroundColor(MatchActivity.GRID_BACK_HYBRID);
        gridCoopTopSub.setBackgroundColor(MatchActivity.GRID_BACK_CONE_COOP);
        gridCoopTopMid.setBackgroundColor(MatchActivity.GRID_BACK_CUBE);
        gridCoopTopWall.setBackgroundColor(MatchActivity.GRID_BACK_CONE_COOP);
        gridCoopMidSub.setBackgroundColor(MatchActivity.GRID_BACK_CONE_COOP);
        gridCoopMidMid.setBackgroundColor(MatchActivity.GRID_BACK_CUBE);
        gridCoopMidWall.setBackgroundColor(MatchActivity.GRID_BACK_CONE_COOP);
        gridCoopHybSub.setBackgroundColor(MatchActivity.GRID_BACK_HYBRID);
        gridCoopHybMid.setBackgroundColor(MatchActivity.GRID_BACK_HYBRID);
        gridCoopHybWall.setBackgroundColor(MatchActivity.GRID_BACK_HYBRID);
        gridWallTopSub.setBackgroundColor(color & MatchActivity.GRID_BACK_CONE_ALPHA);
        gridWallTopMid.setBackgroundColor(MatchActivity.GRID_BACK_CUBE);
        gridWallTopWall.setBackgroundColor(color & MatchActivity.GRID_BACK_CONE_ALPHA);
        gridWallMidSub.setBackgroundColor(color & MatchActivity.GRID_BACK_CONE_ALPHA);
        gridWallMidMid.setBackgroundColor(MatchActivity.GRID_BACK_CUBE);
        gridWallMidWall.setBackgroundColor(color & MatchActivity.GRID_BACK_CONE_ALPHA);
        gridWallHybSub.setBackgroundColor(MatchActivity.GRID_BACK_HYBRID);
        gridWallHybMid.setBackgroundColor(MatchActivity.GRID_BACK_HYBRID);
        gridWallHybWall.setBackgroundColor(MatchActivity.GRID_BACK_HYBRID);
    }

    private void getBaseGUIRefs(View view) {

        autoGridLeftTitle = view.findViewById(R.id.autoGridLeftTitle);
        autoSpaceLeft = view.findViewById(R.id.autoSpaceLeft);
        autoGridRightTitle = view.findViewById(R.id.autoGridRightTitle);
        autoSpaceRight = view.findViewById(R.id.autoSpaceRight);
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

        Activity act = getActivity();
        if (act == null)
            return;

        gridSubTopSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_substn_grid_top_substn = !tempData.auto_substn_grid_top_substn;
            return tempData.auto_substn_grid_top_substn ? GridContents.CONE : GridContents.NONE_CONE;
        }, tempData));
        gridSubTopMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_substn_grid_top_mid = !tempData.auto_substn_grid_top_mid;
            return tempData.auto_substn_grid_top_mid ? GridContents.CUBE : GridContents.NONE_CUBE;
        }, tempData));
        gridSubTopWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_substn_grid_top_wall = !tempData.auto_substn_grid_top_wall;
            return tempData.auto_substn_grid_top_wall ? GridContents.CONE : GridContents.NONE_CONE;
        }, tempData));
        gridSubMidSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_substn_grid_mid_substn = !tempData.auto_substn_grid_mid_substn;
            return tempData.auto_substn_grid_mid_substn ? GridContents.CONE : GridContents.NONE_CONE;
        }, tempData));
        gridSubMidMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_substn_grid_mid_mid = !tempData.auto_substn_grid_mid_mid;
            return tempData.auto_substn_grid_mid_mid ? GridContents.CUBE : GridContents.NONE_CUBE;
        }, tempData));
        gridSubMidWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_substn_grid_mid_wall = !tempData.auto_substn_grid_mid_wall;
            return tempData.auto_substn_grid_mid_wall ? GridContents.CONE : GridContents.NONE_CONE;
        }, tempData));
        gridSubHybSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_substn_grid_hyb_substn = !tempData.auto_substn_grid_hyb_substn;
            return tempData.auto_substn_grid_hyb_substn ? GridContents.HYBRID : GridContents.NONE_HYBRID;
        }, tempData));
        gridSubHybMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_substn_grid_hyb_mid = !tempData.auto_substn_grid_hyb_mid;
            return tempData.auto_substn_grid_hyb_mid ? GridContents.HYBRID : GridContents.NONE_HYBRID;
        }, tempData));
        gridSubHybWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_substn_grid_hyb_wall = !tempData.auto_substn_grid_hyb_wall;
            return tempData.auto_substn_grid_hyb_wall ? GridContents.HYBRID : GridContents.NONE_HYBRID;
        }, tempData));

        gridCoopTopSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_coop_grid_top_substn = !tempData.auto_coop_grid_top_substn;
            return tempData.auto_coop_grid_top_substn ? GridContents.CONE : GridContents.NONE_CONE;
        }, tempData));
        gridCoopTopMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_coop_grid_top_mid = !tempData.auto_coop_grid_top_mid;
            return tempData.auto_coop_grid_top_mid ? GridContents.CUBE : GridContents.NONE_CUBE;
        }, tempData));
        gridCoopTopWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_coop_grid_top_wall = !tempData.auto_coop_grid_top_wall;
            return tempData.auto_coop_grid_top_wall ? GridContents.CONE : GridContents.NONE_CONE;
        }, tempData));
        gridCoopMidSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_coop_grid_mid_substn = !tempData.auto_coop_grid_mid_substn;
            return tempData.auto_coop_grid_mid_substn ? GridContents.CONE : GridContents.NONE_CONE;
        }, tempData));
        gridCoopMidMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_coop_grid_mid_mid = !tempData.auto_coop_grid_mid_mid;
            return tempData.auto_coop_grid_mid_mid ? GridContents.CUBE : GridContents.NONE_CUBE;
        }, tempData));
        gridCoopMidWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_coop_grid_mid_wall = !tempData.auto_coop_grid_mid_wall;
            return tempData.auto_coop_grid_mid_wall ? GridContents.CONE : GridContents.NONE_CONE;
        }, tempData));
        gridCoopHybSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_coop_grid_hyb_substn = !tempData.auto_coop_grid_hyb_substn;
            return tempData.auto_coop_grid_hyb_substn ? GridContents.HYBRID : GridContents.NONE_HYBRID;
        }, tempData));
        gridCoopHybMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_coop_grid_hyb_mid = !tempData.auto_coop_grid_hyb_mid;
            return tempData.auto_coop_grid_hyb_mid ? GridContents.HYBRID : GridContents.NONE_HYBRID;
        }, tempData));
        gridCoopHybWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_coop_grid_hyb_wall = !tempData.auto_coop_grid_hyb_wall;
            return tempData.auto_coop_grid_hyb_wall ? GridContents.HYBRID : GridContents.NONE_HYBRID;
        }, tempData));

        gridWallTopSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_wall_grid_top_substn = !tempData.auto_wall_grid_top_substn;
            return tempData.auto_wall_grid_top_substn ? GridContents.CONE : GridContents.NONE_CONE;
        }, tempData));
        gridWallTopMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_wall_grid_top_mid = !tempData.auto_wall_grid_top_mid;
            return tempData.auto_wall_grid_top_mid ? GridContents.CUBE : GridContents.NONE_CUBE;
        }, tempData));
        gridWallTopWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_wall_grid_top_wall = !tempData.auto_wall_grid_top_wall;
            return tempData.auto_wall_grid_top_wall ? GridContents.CONE : GridContents.NONE_CONE;
        }, tempData));
        gridWallMidSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_wall_grid_mid_substn = !tempData.auto_wall_grid_mid_substn;
            return tempData.auto_wall_grid_mid_substn ? GridContents.CONE : GridContents.NONE_CONE;
        }, tempData));
        gridWallMidMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_wall_grid_mid_mid = !tempData.auto_wall_grid_mid_mid;
            return tempData.auto_wall_grid_mid_mid ? GridContents.CUBE : GridContents.NONE_CUBE;
        }, tempData));
        gridWallMidWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_wall_grid_mid_wall = !tempData.auto_wall_grid_mid_wall;
            return tempData.auto_wall_grid_mid_wall ? GridContents.CONE : GridContents.NONE_CONE;
        }, tempData));
        gridWallHybSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_wall_grid_hyb_substn = !tempData.auto_wall_grid_hyb_substn;
            return tempData.auto_wall_grid_hyb_substn ? GridContents.HYBRID : GridContents.NONE_HYBRID;
        }, tempData));
        gridWallHybMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_wall_grid_hyb_mid = !tempData.auto_wall_grid_hyb_mid;
            return tempData.auto_wall_grid_hyb_mid ? GridContents.HYBRID : GridContents.NONE_HYBRID;
        }, tempData));
        gridWallHybWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.auto_wall_grid_hyb_wall = !tempData.auto_wall_grid_hyb_wall;
            return tempData.auto_wall_grid_hyb_wall ? GridContents.HYBRID : GridContents.NONE_HYBRID;
        }, tempData));
    }

    private void setMobility(@NonNull MatchStatsStruct data) {
        mobilityB.setForeground(data.auto_mobility ? checkmarks : null);
    }

    private void setChargeStation(@NonNull MatchStatsStruct data) {
        switch (data.auto_charge_station) {
            case 1:
            case 2:
            case 3:
                chargeS.setSelection(data.auto_charge_station);
                break;
            case 0:
            default:
                data.auto_charge_station = 0;
                chargeS.setSelection(data.auto_charge_station);
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
}
