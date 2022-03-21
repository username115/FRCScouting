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

package org.growingstems.scouting;

import org.frc836.database.DBActivity;
import org.frc836.yearly.MatchActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;


public class MatchStartActivity extends DBActivity {

    private EditText teamNum;
    private TextView position;
    private EditText matchNum;
    private Button startB;
    private ImageView robotPic;

    private TextView teamText;

    private String HELPMESSAGE;

    private MatchSchedule schedule;

    private ProgressDialog pd;

	private RequestQueue reqQueue = null;


	@NonNull
	private final ActivityResultLauncher<Intent> resultForMatch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
		if (result.getResultCode() > 0) {
			matchNum.setText(String.valueOf(result.getResultCode()));
		}
	});

	private final ActivityResultLauncher<Intent> resultForPrefs = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
		MatchSchedule schedule = new MatchSchedule();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		schedule.updateSchedule(
				prefs.getString("eventPref", "Chesapeake Regional"), this,
				false);

		updatePosition();

		if (matchNum.getText().length() > 0)
			setMatch(Integer.parseInt(matchNum.getText().toString()));
	});

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matchstart);
        if (reqQueue == null) {
			reqQueue = Volley.newRequestQueue(this);
			reqQueue.start();
		}

        HELPMESSAGE = "Ensure correct Event and Position are selected in Settings.\n\n"
                + "Enter the upcoming match number, and the team number and picture will auto-populate if available.\n\n"
                + "Match number and team number will automatically update upon successful submission of match data.";

        teamNum = findViewById(R.id.startTeamNum);
        position = findViewById(R.id.startPos);
        matchNum = findViewById(R.id.startMatchNum);
        startB = findViewById(R.id.startMatchB);
        robotPic = findViewById(R.id.robotPic);

        teamText = findViewById(R.id.startMatchTeamT);

        position.setOnClickListener(new positionClickListener());
        startB.setOnClickListener(new StartClickListener());
        robotPic.setOnClickListener(new PictureClickListener());
        teamText.setOnClickListener(new PictureClickListener());

        matchNum.addTextChangedListener(new matchTextListener());
        teamNum.addTextChangedListener(new teamTextListener());
        schedule = new MatchSchedule();

    }

    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        updatePosition();

        if (!schedule.isValid(this)) {
            schedule.updateSchedule(
                    prefs.getString("eventPref", "Chesapeake Regional"), this,
                    false);
        }

    }

	@NonNull
	@Override
	public String getHelpMessage() {
		return HELPMESSAGE;
	}

	@NonNull
	@Override
	public ActivityResultLauncher<Intent> getResultForPrefs() {
		return resultForPrefs;
	}

	private class positionClickListener implements OnClickListener {

        public void onClick(View v) {
            MainMenuSelection.openSettings(MatchStartActivity.this);
        }

    }

    private class teamTextListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            teamText.setText(s.toString());
        }
    }

    private class matchTextListener implements TextWatcher {

        public void afterTextChanged(Editable s) {
            if (s.length() > 0)
                setMatch(Integer.parseInt(s.toString()));

        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // Auto-generated method stub

        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // Auto-generated method stub

        }

    }

    private class StartClickListener implements OnClickListener {

        public void onClick(View v) {
            Intent intent;
            intent = new Intent(MatchStartActivity.this,
                    MatchActivity.class);
            intent.putExtra("team", teamNum.getText().toString());
            intent.putExtra("match", matchNum.getText().toString());
			resultForMatch.launch(intent);
        }

    }

    private void updatePosition() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        String pos = prefs.getString("positionPref", "Red 1");

        if (pos == null)
        	return;

        position.setText(pos);
        if (pos.contains("Blue"))
            position.setTextColor(Color.BLUE);
        else
            position.setTextColor(Color.RED);
    }

    private void setMatch(int matchNum) {

        String def = teamNum.getText().toString().trim();
        try {
            if (def.length() > 9 || Integer.parseInt(def) <= 0)
                def = "";
        } catch (Exception e) {
            def = "";
        }
        teamNum.setText(schedule.getTeam(matchNum, position.getText()
                .toString(), this, def));
        if (Prefs.getRobotPicPref(getApplicationContext(), false)) {
            loadPicture();
        }
    }

    private class PictureClickListener implements OnClickListener {

        public void onClick(View v) {
            pd = ProgressDialog.show(MatchStartActivity.this, "Busy",
                    "Retrieving Team Robot Photo", false);
            pd.setCancelable(true);
            loadPicture();
        }

    }

    private void loadPicture() {
        if (teamNum.getText().length() < 1) {
            if (pd != null)
                pd.dismiss();
            return;
        }
        String pictureURL = db.getPictureURL(Integer.parseInt(teamNum.getText()
                .toString()));
        if (pictureURL.length() < 5) {
            if (pd != null)
                pd.dismiss();
            robotPic.setVisibility(View.GONE);
            teamText.setVisibility(View.VISIBLE);
            return;
        }

		reqQueue.add(new ImageRequest(pictureURL,
				this::onFinished,
				Resources.getSystem().getDisplayMetrics().widthPixels,
				0,
				ImageView.ScaleType.FIT_XY,
				Bitmap.Config.ARGB_8888,
				null));
    }

    public void onFinished(Bitmap bitmap) {
        if (pd != null)
            pd.dismiss();
        if (bitmap == null) {
            robotPic.setVisibility(View.GONE);
            teamText.setVisibility(View.VISIBLE);
        } else {

            robotPic.setVisibility(View.VISIBLE);
            teamText.setVisibility(View.GONE);
            scaleImage(robotPic, bitmap);
        }
    }

    private int orient = Configuration.ORIENTATION_UNDEFINED;

    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation != orient)
            scaleImage(robotPic, ((BitmapDrawable)robotPic.getDrawable()).getBitmap());
        orient = newConfig.orientation;
    }


    private void scaleImage(ImageView view, Bitmap bitmap) {

        BitmapDrawable result = new BitmapDrawable(Resources.getSystem(), bitmap);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
                .getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

}
