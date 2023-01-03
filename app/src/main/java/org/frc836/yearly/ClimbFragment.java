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

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import org.frc836.database.MatchStatsStruct;
import org.growingstems.scouting.MatchFragment;
import org.growingstems.scouting.R;
import org.growingstems.scouting.SuperImageButton;
import org.growingstems.scouting.TransparentImageButton;


public class ClimbFragment extends MatchFragment {


    private boolean displayed = false;

    private MatchStatsStruct tempData = new MatchStatsStruct();

    private RadioGroup hangGroup;

    private TransparentImageButton imgLow;
    private TransparentImageButton imgMid;
    private TransparentImageButton imgHigh;
    private TransparentImageButton imgTraverse;

    private SuperImageButton fieldImage;


    private EditText hangTime;

    private Button timerStartStop;
    private Button timerReset;

    private final Handler timer = new Handler();
    private boolean timerRunning = false;

    private int time = 0;

    private View mainView;

    private final Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            if (timerRunning)
                timer.postDelayed(mUpdateTimeTask, 1000);
            time++;
            hangTime.setText(String.valueOf(time));
        }
    };

    public ClimbFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PreMatch.
     */
    public static ClimbFragment newInstance() {
        return new ClimbFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_climb, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        mainView = view;
        getGUIRefs(view);
        setListeners();
        displayed = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        //need to remap view every time it is loaded, in case position or side changed
        getGUIRefs(mainView);
        setListeners();
        loadData(tempData);
        timerStartStop.setText(R.string.start);
        timer.removeCallbacks(mUpdateTimeTask);
        timerRunning = false;
    }

    public void onPause() {
        super.onPause();
        timer.removeCallbacks(mUpdateTimeTask);
        timerRunning = false;
        saveData(tempData);
    }

    @Override
    public void saveData(@NonNull MatchStatsStruct data) {
        if (getView() == null || !displayed)
            return;

        data.hang_level = getHangLevelFromResId(hangGroup.getCheckedRadioButtonId());
        data.hang_attempt = data.hang_level > 0
            || hangGroup.getCheckedRadioButtonId() == R.id.hangAttempt;

        String temp = hangTime.getText().toString();
        if (temp.length() > 0)
            data.time_to_hang_s = Integer.parseInt(temp);
    }

    @Override
    public void loadData(@NonNull MatchStatsStruct data) {
        tempData = data;
        if (getView() == null || !displayed)
            return;


        switch (data.hang_level) {
            case 1:
                hangGroup.check(R.id.hangLow);
                break;
            case 2:
                hangGroup.check(R.id.hangMid);
                break;
            case 3:
                hangGroup.check(R.id.hangHigh);
                break;
            case 4:
                hangGroup.check(R.id.hangTraverse);
                break;
            case 0:
            default:
                if (data.hang_attempt)
                    hangGroup.check(R.id.hangAttempt);
                else
                    hangGroup.check(R.id.hangNone);
                break;
        }

        setHangLevelImg(data.hang_level);

        if (data.time_to_hang_s > 0) {
            hangTime.setText(String.valueOf(data.time_to_hang_s));
            timerReset.setEnabled(true);
            time = data.time_to_hang_s;
        } else {
            hangTime.setText("");
            timerReset.setEnabled(false);
            time = 0;
        }

    }

    private void getGUIRefs(View view) {

        hangGroup = view.findViewById(R.id.hangRadioGroup);

        imgLow = view.findViewById(R.id.imageButtonLow);
        imgMid = view.findViewById(R.id.imageButtonMiddle);
        imgHigh = view.findViewById(R.id.imageButtonHigh);
        imgTraverse = view.findViewById(R.id.imageButtonTraverse);

        fieldImage = view.findViewById(R.id.fieldimage);

        hangTime = view.findViewById(R.id.hangSeconds);

        timerStartStop = view.findViewById(R.id.triggerHangTimerStartStop);
        timerReset = view.findViewById(R.id.triggerHangTimerReset);
    }

    private void setListeners() {

        hangGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int level = getHangLevelFromResId(checkedId);
            setHangLevelImg(level);
        });

        hangTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                timerReset.setEnabled(s.length() > 0);
            }
        });

        timerReset.setOnClickListener(v -> {
            hangTime.setText("");
            time = 0;
        });

        timerStartStop.setOnClickListener(v -> {
            if (timerRunning) {
                timer.removeCallbacks(mUpdateTimeTask);
                timerRunning = false;
                timerStartStop.setText(R.string.start);
            } else {
                timerRunning = true;
                timer.postDelayed(mUpdateTimeTask, 1000);
                timerStartStop.setText(R.string.stop);
            }
        });

        imgLow.setOnClickListener(v -> hangGroup.check(R.id.hangLow));
        imgMid.setOnClickListener(v -> hangGroup.check(R.id.hangMid));
        imgHigh.setOnClickListener(v -> hangGroup.check(R.id.hangHigh));
        imgTraverse.setOnClickListener(v -> hangGroup.check(R.id.hangTraverse));

        fieldImage.clearImageButtons();
        fieldImage.addImageButton(imgLow);
        fieldImage.addImageButton(imgMid);
        fieldImage.addImageButton(imgHigh);
        fieldImage.addImageButton(imgTraverse);
    }

    private int getHangLevelFromResId(int resId) {
        switch (resId) {
            case R.id.hangLow:
                return 1;
            case R.id.hangMid:
                return 2;
            case R.id.hangHigh:
                return 3;
            case R.id.hangTraverse:
                return 4;
            case R.id.hangNone:
            case R.id.hangAttempt:
            default:
                return 0;
        }
    }

    private void setHangLevelImg(int level) {
        if (level == 1)
            imgLow.setImageResource(R.drawable.fieldimage_hanger_side_low_green);
        else
            imgLow.setImageResource(R.drawable.fieldimage_hanger_side_low_red);

        if (level == 2)
            imgMid.setImageResource(R.drawable.fieldimage_hanger_side_middle_green);
        else
            imgMid.setImageResource(R.drawable.fieldimage_hanger_side_middle_red);

        if (level == 3)
            imgHigh.setImageResource(R.drawable.fieldimage_hanger_side_high_green);
        else
            imgHigh.setImageResource(R.drawable.fieldimage_hanger_side_high_red);

        if (level >= 4)
            imgTraverse.setImageResource(R.drawable.fieldimage_hanger_side_traverse_green);
        else
            imgTraverse.setImageResource(R.drawable.fieldimage_hanger_side_traverse_red);

    }


}
