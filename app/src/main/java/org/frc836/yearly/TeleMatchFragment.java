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
import android.widget.ImageView;
import android.widget.Spinner;

import org.frc836.database.MatchStatsStruct;
import org.growingstems.scouting.MatchFragment;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;


public class TeleMatchFragment extends MatchFragment {


    private Button shipCargoB;
    private Spinner shipCargoS;
    private Button shipHatchB;
    private Spinner shipHatchS;

    private Button rocketL1CargoB;
    private Spinner rocketL1CargoS;
    private Button rocketL1HatchB;
    private Spinner rocketL1HatchS;

    private Button rocketL2CargoB;
    private Spinner rocketL2CargoS;
    private Button rocketL2HatchB;
    private Spinner rocketL2HatchS;

    private Button rocketL3CargoB;
    private Spinner rocketL3CargoS;
    private Button rocketL3HatchB;
    private Spinner rocketL3HatchS;

    private Button droppedCargoB;
    private Spinner droppedCargoS;
    private Button droppedHatchB;
    private Spinner droppedHatchS;

    private ImageView cargoShip;

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

        MatchStatsYearly.clearTele(data);

        data.cargo_ship = shipCargoS.getSelectedItemPosition();
        data.hatch_ship = shipHatchS.getSelectedItemPosition();
        data.cargo_rocket_1 = rocketL1CargoS.getSelectedItemPosition();
        data.hatch_rocket_1 = rocketL1HatchS.getSelectedItemPosition();
        data.cargo_rocket_2 = rocketL2CargoS.getSelectedItemPosition();
        data.hatch_rocket_2 = rocketL2HatchS.getSelectedItemPosition();
        data.cargo_rocket_3 = rocketL3CargoS.getSelectedItemPosition();
        data.hatch_rocket_3 = rocketL3HatchS.getSelectedItemPosition();
        data.cargo_dropped = droppedCargoS.getSelectedItemPosition();
        data.hatch_dropped = droppedHatchS.getSelectedItemPosition();
    }

    @Override
    public void loadData(MatchStatsStruct data) {
        tempData = data;
        if (getView() == null || data == null || !displayed)
            return;
        // which side are we using
        //boolean redLeft = Prefs.getRedLeft(getActivity(), true);
        //if (redLeft) {
        //    mainView.findViewById(R.id.scaleBTele).setScaleX(1.0f);
        //} else {
        //    mainView.findViewById(R.id.scaleBTele).setScaleX(-1.0f);
        //}

        Activity act = getActivity();
        String pos;
        if (act instanceof MatchActivity)
            pos = ((MatchActivity) act).getPosition();
        else
            pos = Prefs.getPosition(getActivity(), "Red 1");


        if (pos.contains("Blue")) {
            cargoShip.setImageResource(R.drawable.blue_ship);
        } else {
            cargoShip.setImageResource(R.drawable.red_ship);
        }

        shipCargoS.setSelection(data.cargo_ship);
        shipHatchS.setSelection(data.hatch_ship);
        rocketL1CargoS.setSelection(data.cargo_rocket_1);
        rocketL1HatchS.setSelection(data.hatch_rocket_1);
        rocketL2CargoS.setSelection(data.cargo_rocket_2);
        rocketL2HatchS.setSelection(data.hatch_rocket_2);
        rocketL3CargoS.setSelection(data.cargo_rocket_3);
        rocketL3HatchS.setSelection(data.hatch_rocket_3);
        droppedCargoS.setSelection(data.cargo_dropped);
        droppedHatchS.setSelection(data.hatch_dropped);
    }

    private void getGUIRefs(View view) {
        shipCargoB = view.findViewById(R.id.shipCargoB);
        shipCargoS = view.findViewById(R.id.shipCargoCount);
        shipHatchB = view.findViewById(R.id.shipHatchB);
        shipHatchS = view.findViewById(R.id.shipHatchCount);
        rocketL1CargoB = view.findViewById(R.id.rocketL1CargoB);
        rocketL1CargoS = view.findViewById(R.id.rocketL1CargoCount);
        rocketL1HatchB = view.findViewById(R.id.rocketL1HatchB);
        rocketL1HatchS = view.findViewById(R.id.rocketL1HatchCount);
        rocketL2CargoB = view.findViewById(R.id.rocketL2CargoB);
        rocketL2CargoS = view.findViewById(R.id.rocketL2CargoCount);
        rocketL2HatchB = view.findViewById(R.id.rocketL2HatchB);
        rocketL2HatchS = view.findViewById(R.id.rocketL2HatchCount);
        rocketL3CargoB = view.findViewById(R.id.rocketL3CargoB);
        rocketL3CargoS = view.findViewById(R.id.rocketL3CargoCount);
        rocketL3HatchB = view.findViewById(R.id.rocketL3HatchB);
        rocketL3HatchS = view.findViewById(R.id.rocketL3HatchCount);
        droppedCargoB = view.findViewById(R.id.cargoDroppedB);
        droppedCargoS = view.findViewById(R.id.cargoDroppedCount);
        droppedHatchB = view.findViewById(R.id.hatchDroppedB);
        droppedHatchS = view.findViewById(R.id.hatchDroppedCount);


        cargoShip = view.findViewById(R.id.cargoShip);
    }

    private void setListeners() {

        shipCargoB.setOnClickListener(new OnIncrementListener(shipCargoS, 1));
        shipHatchB.setOnClickListener(new OnIncrementListener(shipHatchS, 1));
        rocketL1CargoB.setOnClickListener(new OnIncrementListener(rocketL1CargoS, 1));
        rocketL1HatchB.setOnClickListener(new OnIncrementListener(rocketL1HatchS, 1));
        rocketL2CargoB.setOnClickListener(new OnIncrementListener(rocketL2CargoS, 1));
        rocketL2HatchB.setOnClickListener(new OnIncrementListener(rocketL2HatchS, 1));
        rocketL3CargoB.setOnClickListener(new OnIncrementListener(rocketL3CargoS, 1));
        rocketL3HatchB.setOnClickListener(new OnIncrementListener(rocketL3HatchS, 1));
        droppedCargoB.setOnClickListener(new OnIncrementListener(droppedCargoS, 1));
        droppedHatchB.setOnClickListener(new OnIncrementListener(droppedHatchS, 1));
    }

    private class OnIncrementListener implements View.OnClickListener {

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
