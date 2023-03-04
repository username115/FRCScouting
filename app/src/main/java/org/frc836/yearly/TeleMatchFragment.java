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


public class TeleMatchFragment extends MatchFragment {

    private final MatchStatsStruct tempData = new MatchStatsStruct();

    private boolean displayed = false;

    private TextView gridLeftTitle;
    private Space spaceLeft;
    private TextView gridRightTitle;
    private Space spaceRight;

    private LinearLayout foulsLayoutL;
    private LinearLayout foulsLayoutR;
    private LinearLayout gpLayoutL;
    private LinearLayout gpLayoutR;
    private ImageView gridViewL;
    private ImageView gridViewR;

    private Spinner foulCount;
    private Button foulInc;
    private Spinner gpDropCount;
    private Button gpDropInc;


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

        MatchStatsYearly.copyTele(tempData, data);
        data.foul_count = foulCount.getSelectedItemPosition();
        data.dropped_gp_count = gpDropCount.getSelectedItemPosition();
    }

    @Override
    public void loadData(@NonNull MatchStatsStruct data) {
        tempData.copyFrom(data);
        View view = getView();
        if (view == null || !displayed)
            return;
        updateSide(view);
        setListeners();

        foulCount.setSelection(data.foul_count);
        gpDropCount.setSelection(data.dropped_gp_count);

        Activity act = getActivity();
        if (act == null)
            return;

        gridSubTopSub.setForeground(data.substn_grid_top_substn ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridSubTopMid.setForeground(data.substn_grid_top_mid ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cube) : null);
        gridSubTopWall.setForeground(data.substn_grid_top_wall ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridSubMidSub.setForeground(data.substn_grid_mid_substn ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridSubMidMid.setForeground(data.substn_grid_mid_mid ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cube) : null);
        gridSubMidWall.setForeground(data.substn_grid_mid_wall ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridSubHybSub.setForeground(data.substn_grid_hyb_substn ? AppCompatResources.getDrawable(act, R.drawable.selection_square_hybrid) : null);
        gridSubHybMid.setForeground(data.substn_grid_hyb_mid ? AppCompatResources.getDrawable(act, R.drawable.selection_square_hybrid) : null);
        gridSubHybWall.setForeground(data.substn_grid_hyb_wall ? AppCompatResources.getDrawable(act, R.drawable.selection_square_hybrid) : null);
        gridCoopTopSub.setForeground(data.coop_grid_top_substn ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridCoopTopMid.setForeground(data.coop_grid_top_mid ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cube) : null);
        gridCoopTopWall.setForeground(data.coop_grid_top_wall ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridCoopMidSub.setForeground(data.coop_grid_mid_substn ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridCoopMidMid.setForeground(data.coop_grid_mid_mid ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cube) : null);
        gridCoopMidWall.setForeground(data.coop_grid_mid_wall ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridCoopHybSub.setForeground(data.coop_grid_hyb_substn ? AppCompatResources.getDrawable(act, R.drawable.selection_square_hybrid) : null);
        gridCoopHybMid.setForeground(data.coop_grid_hyb_mid ? AppCompatResources.getDrawable(act, R.drawable.selection_square_hybrid) : null);
        gridCoopHybWall.setForeground(data.coop_grid_hyb_wall ? AppCompatResources.getDrawable(act, R.drawable.selection_square_hybrid) : null);
        gridWallTopSub.setForeground(data.wall_grid_top_substn ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridWallTopMid.setForeground(data.wall_grid_top_mid ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cube) : null);
        gridWallTopWall.setForeground(data.wall_grid_top_wall ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridWallMidSub.setForeground(data.wall_grid_mid_substn ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridWallMidMid.setForeground(data.wall_grid_mid_mid ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cube) : null);
        gridWallMidWall.setForeground(data.wall_grid_mid_wall ? AppCompatResources.getDrawable(act, R.drawable.selection_square_cone) : null);
        gridWallHybSub.setForeground(data.wall_grid_hyb_substn ? AppCompatResources.getDrawable(act, R.drawable.selection_square_hybrid) : null);
        gridWallHybMid.setForeground(data.wall_grid_hyb_mid ? AppCompatResources.getDrawable(act, R.drawable.selection_square_hybrid) : null);
        gridWallHybWall.setForeground(data.wall_grid_hyb_wall ? AppCompatResources.getDrawable(act, R.drawable.selection_square_hybrid) : null);

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
            gridLeftTitle.setText(R.string.top_grid_abbr);
            spaceLeft.setVisibility(View.GONE);
            gridRightTitle.setText(R.string.hybrid_grid_abbr);
            spaceRight.setVisibility(View.VISIBLE);
            foulsLayoutL.setVisibility(View.GONE);
            gpLayoutL.setVisibility(View.GONE);
            gridViewL.setVisibility(View.GONE);
            foulsLayoutR.setVisibility(View.VISIBLE);
            gpLayoutR.setVisibility(View.VISIBLE);
            gridViewR.setVisibility(View.VISIBLE);

            foulCount = view.findViewById(R.id.foulsRightCount);
            foulInc = view.findViewById(R.id.foulsRightButton);

            gpDropCount = view.findViewById(R.id.gpDroppedRightCount);
            gpDropInc = view.findViewById(R.id.gpDroppedRightButton);

            if (pos.contains("Blue")) {
                //substation at top of screen, hybrid on right
                gridSubTopSub = view.findViewById(R.id.gridTopTopLeft);
                gridSubTopMid = view.findViewById(R.id.gridTopMidLeft);
                gridSubTopWall = view.findViewById(R.id.gridTopBotLeft);
                gridSubMidSub = view.findViewById(R.id.gridTopTopMid);
                gridSubMidMid = view.findViewById(R.id.gridTopMidMid);
                gridSubMidWall = view.findViewById(R.id.gridTopBotMid);
                gridSubHybSub = view.findViewById(R.id.gridTopTopRight);
                gridSubHybMid = view.findViewById(R.id.gridTopMidRight);
                gridSubHybWall = view.findViewById(R.id.gridTopBotRight);

                gridCoopTopSub = view.findViewById(R.id.gridMidTopLeft);
                gridCoopTopMid = view.findViewById(R.id.gridMidMidLeft);
                gridCoopTopWall = view.findViewById(R.id.gridMidBotLeft);
                gridCoopMidSub = view.findViewById(R.id.gridMidTopMid);
                gridCoopMidMid = view.findViewById(R.id.gridMidMidMid);
                gridCoopMidWall = view.findViewById(R.id.gridMidBotMid);
                gridCoopHybSub = view.findViewById(R.id.gridMidTopRight);
                gridCoopHybMid = view.findViewById(R.id.gridMidMidRight);
                gridCoopHybWall = view.findViewById(R.id.gridMidBotRight);

                gridWallTopSub = view.findViewById(R.id.gridBotTopLeft);
                gridWallTopMid = view.findViewById(R.id.gridBotMidLeft);
                gridWallTopWall = view.findViewById(R.id.gridBotBotLeft);
                gridWallMidSub = view.findViewById(R.id.gridBotTopMid);
                gridWallMidMid = view.findViewById(R.id.gridBotMidMid);
                gridWallMidWall = view.findViewById(R.id.gridBotBotMid);
                gridWallHybSub = view.findViewById(R.id.gridBotTopRight);
                gridWallHybMid = view.findViewById(R.id.gridBotMidRight);
                gridWallHybWall = view.findViewById(R.id.gridBotBotRight);

            } else {
                //substation at bottom of screen, hybrid on right
                gridSubTopSub = view.findViewById(R.id.gridBotTopLeft);
                gridSubTopMid = view.findViewById(R.id.gridBotMidLeft);
                gridSubTopWall = view.findViewById(R.id.gridBotBotLeft);
                gridSubMidSub = view.findViewById(R.id.gridBotTopMid);
                gridSubMidMid = view.findViewById(R.id.gridBotMidMid);
                gridSubMidWall = view.findViewById(R.id.gridBotBotMid);
                gridSubHybSub = view.findViewById(R.id.gridBotTopRight);
                gridSubHybMid = view.findViewById(R.id.gridBotMidRight);
                gridSubHybWall = view.findViewById(R.id.gridBotBotRight);

                gridCoopTopSub = view.findViewById(R.id.gridMidTopLeft);
                gridCoopTopMid = view.findViewById(R.id.gridMidMidLeft);
                gridCoopTopWall = view.findViewById(R.id.gridMidBotLeft);
                gridCoopMidSub = view.findViewById(R.id.gridMidTopMid);
                gridCoopMidMid = view.findViewById(R.id.gridMidMidMid);
                gridCoopMidWall = view.findViewById(R.id.gridMidBotMid);
                gridCoopHybSub = view.findViewById(R.id.gridMidTopRight);
                gridCoopHybMid = view.findViewById(R.id.gridMidMidRight);
                gridCoopHybWall = view.findViewById(R.id.gridMidBotRight);

                gridWallTopSub = view.findViewById(R.id.gridTopTopLeft);
                gridWallTopMid = view.findViewById(R.id.gridTopMidLeft);
                gridWallTopWall = view.findViewById(R.id.gridTopBotLeft);
                gridWallMidSub = view.findViewById(R.id.gridTopTopMid);
                gridWallMidMid = view.findViewById(R.id.gridTopMidMid);
                gridWallMidWall = view.findViewById(R.id.gridTopBotMid);
                gridWallHybSub = view.findViewById(R.id.gridTopTopRight);
                gridWallHybMid = view.findViewById(R.id.gridTopMidRight);
                gridWallHybWall = view.findViewById(R.id.gridTopBotRight);
            }

        } else {
            //Right side layout
            gridLeftTitle.setText(R.string.hybrid_grid_abbr);
            gridRightTitle.setText(R.string.top_grid_abbr);
            foulsLayoutL.setVisibility(View.VISIBLE);
            gpLayoutL.setVisibility(View.VISIBLE);
            gridViewL.setVisibility(View.VISIBLE);
            foulsLayoutR.setVisibility(View.GONE);
            gpLayoutR.setVisibility(View.GONE);
            gridViewR.setVisibility(View.GONE);

            foulCount = view.findViewById(R.id.foulsLeftCount);
            foulInc = view.findViewById(R.id.foulsLeftButton);

            gpDropCount = view.findViewById(R.id.gpDroppedLeftCount);
            gpDropInc = view.findViewById(R.id.gpDroppedLeftButton);

            if (pos.contains("Blue")) {
                //substation at bottom of screen, hybrid on left
                gridSubTopSub = view.findViewById(R.id.gridBotTopRight);
                gridSubTopMid = view.findViewById(R.id.gridBotMidRight);
                gridSubTopWall = view.findViewById(R.id.gridBotBotRight);
                gridSubMidSub = view.findViewById(R.id.gridBotTopMid);
                gridSubMidMid = view.findViewById(R.id.gridBotMidMid);
                gridSubMidWall = view.findViewById(R.id.gridBotBotMid);
                gridSubHybSub = view.findViewById(R.id.gridBotTopLeft);
                gridSubHybMid = view.findViewById(R.id.gridBotMidLeft);
                gridSubHybWall = view.findViewById(R.id.gridBotBotLeft);

                gridCoopTopSub = view.findViewById(R.id.gridMidTopRight);
                gridCoopTopMid = view.findViewById(R.id.gridMidMidRight);
                gridCoopTopWall = view.findViewById(R.id.gridMidBotRight);
                gridCoopMidSub = view.findViewById(R.id.gridMidTopMid);
                gridCoopMidMid = view.findViewById(R.id.gridMidMidMid);
                gridCoopMidWall = view.findViewById(R.id.gridMidBotMid);
                gridCoopHybSub = view.findViewById(R.id.gridMidTopLeft);
                gridCoopHybMid = view.findViewById(R.id.gridMidMidLeft);
                gridCoopHybWall = view.findViewById(R.id.gridMidBotLeft);

                gridWallTopSub = view.findViewById(R.id.gridTopTopRight);
                gridWallTopMid = view.findViewById(R.id.gridTopMidRight);
                gridWallTopWall = view.findViewById(R.id.gridTopBotRight);
                gridWallMidSub = view.findViewById(R.id.gridTopTopMid);
                gridWallMidMid = view.findViewById(R.id.gridTopMidMid);
                gridWallMidWall = view.findViewById(R.id.gridTopBotMid);
                gridWallHybSub = view.findViewById(R.id.gridTopTopLeft);
                gridWallHybMid = view.findViewById(R.id.gridTopMidLeft);
                gridWallHybWall = view.findViewById(R.id.gridTopBotLeft);

            } else {
                //substation at top of screen, hybrid on left
                gridSubTopSub = view.findViewById(R.id.gridTopTopRight);
                gridSubTopMid = view.findViewById(R.id.gridTopMidRight);
                gridSubTopWall = view.findViewById(R.id.gridTopBotRight);
                gridSubMidSub = view.findViewById(R.id.gridTopTopMid);
                gridSubMidMid = view.findViewById(R.id.gridTopMidMid);
                gridSubMidWall = view.findViewById(R.id.gridTopBotMid);
                gridSubHybSub = view.findViewById(R.id.gridTopTopLeft);
                gridSubHybMid = view.findViewById(R.id.gridTopMidLeft);
                gridSubHybWall = view.findViewById(R.id.gridTopBotLeft);

                gridCoopTopSub = view.findViewById(R.id.gridMidTopRight);
                gridCoopTopMid = view.findViewById(R.id.gridMidMidRight);
                gridCoopTopWall = view.findViewById(R.id.gridMidBotRight);
                gridCoopMidSub = view.findViewById(R.id.gridMidTopMid);
                gridCoopMidMid = view.findViewById(R.id.gridMidMidMid);
                gridCoopMidWall = view.findViewById(R.id.gridMidBotMid);
                gridCoopHybSub = view.findViewById(R.id.gridMidTopLeft);
                gridCoopHybMid = view.findViewById(R.id.gridMidMidLeft);
                gridCoopHybWall = view.findViewById(R.id.gridMidBotLeft);

                gridWallTopSub = view.findViewById(R.id.gridBotTopRight);
                gridWallTopMid = view.findViewById(R.id.gridBotMidRight);
                gridWallTopWall = view.findViewById(R.id.gridBotBotRight);
                gridWallMidSub = view.findViewById(R.id.gridBotTopMid);
                gridWallMidMid = view.findViewById(R.id.gridBotMidMid);
                gridWallMidWall = view.findViewById(R.id.gridBotBotMid);
                gridWallHybSub = view.findViewById(R.id.gridBotTopLeft);
                gridWallHybMid = view.findViewById(R.id.gridBotMidLeft);
                gridWallHybWall = view.findViewById(R.id.gridBotBotLeft);
            }
        }
    }

    private void getBaseGUIRefs(View view) {

        gridLeftTitle = view.findViewById(R.id.gridLeftTitle);
        spaceLeft = view.findViewById(R.id.spaceLeft);
        gridRightTitle = view.findViewById(R.id.gridRightTitle);
        spaceRight = view.findViewById(R.id.spaceRight);
        foulsLayoutL = view.findViewById(R.id.foulsLL);
        foulsLayoutR = view.findViewById(R.id.foulsRL);
        gpLayoutL = view.findViewById(R.id.gpLL);
        gpLayoutR = view.findViewById(R.id.gpRL);
        gridViewL = view.findViewById(R.id.gridViewL);
        gridViewR = view.findViewById(R.id.gridViewR);
    }

    private void setListeners() {
        foulInc.setOnClickListener(new OnIncrementListener(foulCount, 1));
        gpDropInc.setOnClickListener(new OnIncrementListener(gpDropCount, 1));

        Activity act = getActivity();
        if (act == null)
            return;

        gridSubTopSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.substn_grid_top_substn = !tempData.substn_grid_top_substn;
            return tempData.substn_grid_top_substn ? GridClickListener.GridContents.CONE : GridClickListener.GridContents.NONE;
        }, tempData));
        gridSubTopMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.substn_grid_top_mid = !tempData.substn_grid_top_mid;
            return tempData.substn_grid_top_mid ? GridClickListener.GridContents.CUBE : GridClickListener.GridContents.NONE;
        }, tempData));
        gridSubTopWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.substn_grid_top_wall = !tempData.substn_grid_top_wall;
            return tempData.substn_grid_top_wall ? GridClickListener.GridContents.CONE : GridClickListener.GridContents.NONE;
        }, tempData));
        gridSubMidSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.substn_grid_mid_substn = !tempData.substn_grid_mid_substn;
            return tempData.substn_grid_mid_substn ? GridClickListener.GridContents.CONE : GridClickListener.GridContents.NONE;
        }, tempData));
        gridSubMidMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.substn_grid_mid_mid = !tempData.substn_grid_mid_mid;
            return tempData.substn_grid_mid_mid ? GridClickListener.GridContents.CUBE : GridClickListener.GridContents.NONE;
        }, tempData));
        gridSubMidWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.substn_grid_mid_wall = !tempData.substn_grid_mid_wall;
            return tempData.substn_grid_mid_wall ? GridClickListener.GridContents.CONE : GridClickListener.GridContents.NONE;
        }, tempData));
        gridSubHybSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.substn_grid_hyb_substn = !tempData.substn_grid_hyb_substn;
            return tempData.substn_grid_hyb_substn ? GridClickListener.GridContents.HYBRID : GridClickListener.GridContents.NONE;
        }, tempData));
        gridSubHybMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.substn_grid_hyb_mid = !tempData.substn_grid_hyb_mid;
            return tempData.substn_grid_hyb_mid ? GridClickListener.GridContents.HYBRID : GridClickListener.GridContents.NONE;
        }, tempData));
        gridSubHybWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.substn_grid_hyb_wall = !tempData.substn_grid_hyb_wall;
            return tempData.substn_grid_hyb_wall ? GridClickListener.GridContents.HYBRID : GridClickListener.GridContents.NONE;
        }, tempData));

        gridCoopTopSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.coop_grid_top_substn = !tempData.coop_grid_top_substn;
            return tempData.coop_grid_top_substn ? GridClickListener.GridContents.CONE : GridClickListener.GridContents.NONE;
        }, tempData));
        gridCoopTopMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.coop_grid_top_mid = !tempData.coop_grid_top_mid;
            return tempData.coop_grid_top_mid ? GridClickListener.GridContents.CUBE : GridClickListener.GridContents.NONE;
        }, tempData));
        gridCoopTopWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.coop_grid_top_wall = !tempData.coop_grid_top_wall;
            return tempData.coop_grid_top_wall ? GridClickListener.GridContents.CONE : GridClickListener.GridContents.NONE;
        }, tempData));
        gridCoopMidSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.coop_grid_mid_substn = !tempData.coop_grid_mid_substn;
            return tempData.coop_grid_mid_substn ? GridClickListener.GridContents.CONE : GridClickListener.GridContents.NONE;
        }, tempData));
        gridCoopMidMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.coop_grid_mid_mid = !tempData.coop_grid_mid_mid;
            return tempData.coop_grid_mid_mid ? GridClickListener.GridContents.CUBE : GridClickListener.GridContents.NONE;
        }, tempData));
        gridCoopMidWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.coop_grid_mid_wall = !tempData.coop_grid_mid_wall;
            return tempData.coop_grid_mid_wall ? GridClickListener.GridContents.CONE : GridClickListener.GridContents.NONE;
        }, tempData));
        gridCoopHybSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.coop_grid_hyb_substn = !tempData.coop_grid_hyb_substn;
            return tempData.coop_grid_hyb_substn ? GridClickListener.GridContents.HYBRID : GridClickListener.GridContents.NONE;
        }, tempData));
        gridCoopHybMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.coop_grid_hyb_mid = !tempData.coop_grid_hyb_mid;
            return tempData.coop_grid_hyb_mid ? GridClickListener.GridContents.HYBRID : GridClickListener.GridContents.NONE;
        }, tempData));
        gridCoopHybWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.coop_grid_hyb_wall = !tempData.coop_grid_hyb_wall;
            return tempData.coop_grid_hyb_wall ? GridClickListener.GridContents.HYBRID : GridClickListener.GridContents.NONE;
        }, tempData));

        gridWallTopSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.wall_grid_top_substn = !tempData.wall_grid_top_substn;
            return tempData.wall_grid_top_substn ? GridClickListener.GridContents.CONE : GridClickListener.GridContents.NONE;
        }, tempData));
        gridWallTopMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.wall_grid_top_mid = !tempData.wall_grid_top_mid;
            return tempData.wall_grid_top_mid ? GridClickListener.GridContents.CUBE : GridClickListener.GridContents.NONE;
        }, tempData));
        gridWallTopWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.wall_grid_top_wall = !tempData.wall_grid_top_wall;
            return tempData.wall_grid_top_wall ? GridClickListener.GridContents.CONE : GridClickListener.GridContents.NONE;
        }, tempData));
        gridWallMidSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.wall_grid_mid_substn = !tempData.wall_grid_mid_substn;
            return tempData.wall_grid_mid_substn ? GridClickListener.GridContents.CONE : GridClickListener.GridContents.NONE;
        }, tempData));
        gridWallMidMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.wall_grid_mid_mid = !tempData.wall_grid_mid_mid;
            return tempData.wall_grid_mid_mid ? GridClickListener.GridContents.CUBE : GridClickListener.GridContents.NONE;
        }, tempData));
        gridWallMidWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.wall_grid_mid_wall = !tempData.wall_grid_mid_wall;
            return tempData.wall_grid_mid_wall ? GridClickListener.GridContents.CONE : GridClickListener.GridContents.NONE;
        }, tempData));
        gridWallHybSub.setOnClickListener(new GridClickListener(act, data -> {
            tempData.wall_grid_hyb_substn = !tempData.wall_grid_hyb_substn;
            return tempData.wall_grid_hyb_substn ? GridClickListener.GridContents.HYBRID : GridClickListener.GridContents.NONE;
        }, tempData));
        gridWallHybMid.setOnClickListener(new GridClickListener(act, data -> {
            tempData.wall_grid_hyb_mid = !tempData.wall_grid_hyb_mid;
            return tempData.wall_grid_hyb_mid ? GridClickListener.GridContents.HYBRID : GridClickListener.GridContents.NONE;
        }, tempData));
        gridWallHybWall.setOnClickListener(new GridClickListener(act, data -> {
            tempData.wall_grid_hyb_wall = !tempData.wall_grid_hyb_wall;
            return tempData.wall_grid_hyb_wall ? GridClickListener.GridContents.HYBRID : GridClickListener.GridContents.NONE;
        }, tempData));

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
