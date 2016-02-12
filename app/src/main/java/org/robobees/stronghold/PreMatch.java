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

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.growingstems.scouting.R;


public class PreMatch extends MatchFragment {

    private Spinner leftDef5S;
    private Spinner leftDef4S;
    private Spinner leftDef3S;
    private Spinner leftDef2S;

    private Spinner rightDef2S;
    private Spinner rightDef3S;
    private Spinner rightDef4S;
    private Spinner rightDef5S;

    private ImageView leftDef5I;
    private ImageView leftDef4I;
    private ImageView leftDef3I;
    private ImageView leftDef2I;

    private ImageView rightDef2I;
    private ImageView rightDef3I;
    private ImageView rightDef4I;
    private ImageView rightDef5I;

    private LinearLayout leftL;
    private LinearLayout rightL;


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
        leftL = (LinearLayout) view.findViewById(R.id.LeftPreMatch);
        rightL = (LinearLayout) view.findViewById(R.id.RightPreMatch);
    }

    public void onSwap(View v) {
        leftL.setBackgroundResource(R.color.blue);
        rightL.setBackgroundResource(R.color.red);
    }

    @Override
    public void saveData(MatchStatsSH data) {
        // TODO
    }

    @Override
    public void loadData(MatchStatsSH data) {
        // TODO
    }
}
