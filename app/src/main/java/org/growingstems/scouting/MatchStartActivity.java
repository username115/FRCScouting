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
import org.frc836.yearly.PilotActivity;
import org.sigmond.net.AsyncPictureRequest;
import org.sigmond.net.PicCallback;
import org.sigmond.net.PicRequestInfo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

public class MatchStartActivity extends DBActivity implements PicCallback {

    private EditText teamNum;
    private TextView position;
    private EditText matchNum;
    private Button startB;
    private ImageView robotPic;

    private String HELPMESSAGE;

    private MatchSchedule schedule;

    private static final int MATCH_ACTIVITY_REQUEST = 0;

    private ProgressDialog pd;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matchstart);

        HELPMESSAGE = "Ensure correct Event and Position are selected in Settings.\n\n"
                + "Enter the upcoming match number, and the team number and picture will auto-populate if available.\n\n"
                + "Match number and team number will automatically update upon successful submission of match data.";

        teamNum = (EditText) findViewById(R.id.startTeamNum);
        position = (TextView) findViewById(R.id.startPos);
        matchNum = (EditText) findViewById(R.id.startMatchNum);
        startB = (Button) findViewById(R.id.startMatchB);
        robotPic = (ImageView) findViewById(R.id.robotPic);

        position.setOnClickListener(new positionClickListener());
        startB.setOnClickListener(new StartClickListener());
        robotPic.setOnClickListener(new PictureClickListener());

        matchNum.addTextChangedListener(new matchTextListener());
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

    private class positionClickListener implements OnClickListener {

        public void onClick(View v) {
            MainMenuSelection.openSettings(MatchStartActivity.this);
        }

    }

    private class matchTextListener implements TextWatcher {

        public void afterTextChanged(Editable s) {
            if (s.length() > 0)
                setMatch(Integer.valueOf(s.toString()));

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
            if (position.getText().toString().contains("Pilot")) {
                intent = new Intent(MatchStartActivity.this, PilotActivity.class);
            }
            else {
                intent = new Intent(MatchStartActivity.this,
                        MatchActivity.class);
            }
            intent.putExtra("team", teamNum.getText().toString());
            intent.putExtra("match", matchNum.getText().toString());
            startActivityForResult(intent, MATCH_ACTIVITY_REQUEST);
        }

    }

    private void updatePosition() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        String pos = prefs.getString("positionPref", "Red 1");

        position.setText(pos);
        if (pos.contains("Blue"))
            position.setTextColor(Color.BLUE);
        else
            position.setTextColor(Color.RED);

        //2017 Change
        if (pos.contains("Pilot")) {
            teamNum.setVisibility(View.INVISIBLE);
        } else {
            teamNum.setVisibility(View.VISIBLE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Prefs.PREFS_ACTIVITY_CODE) {
            MatchSchedule schedule = new MatchSchedule();
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(getBaseContext());
            schedule.updateSchedule(
                    prefs.getString("eventPref", "Chesapeake Regional"), this,
                    false);

            updatePosition();

            if (matchNum.getText().length() > 0)
                setMatch(Integer.valueOf(matchNum.getText().toString()));
        }
        if (requestCode == MATCH_ACTIVITY_REQUEST && resultCode > 0) {
            matchNum.setText(String.valueOf(resultCode));
        }
    }

    private void setMatch(int matchNum) {

        String def = teamNum.getText().toString().trim();
        try {
            if (def.length() > 9 || Integer.valueOf(def) <= 0)
                def = "";
        } catch (Exception e) {
            def = "";
        }
        // 2017 Change
        if (!position.getText().toString().contains("Pilot")) {
            teamNum.setText(schedule.getTeam(matchNum, position.getText()
                    .toString(), this, def));
            if (Prefs.getRobotPicPref(getApplicationContext(), false)) {
                loadPicture();
            }
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
        String pictureURL = db.getPictureURL(Integer.valueOf(teamNum.getText()
                .toString()));
        if (pictureURL.length() < 5) {
            if (pd != null)
                pd.dismiss();
            robotPic.setImageResource(R.drawable.robot);
            return;
        }
        PicRequestInfo info = new PicRequestInfo(pictureURL,
                MatchStartActivity.this);
        AsyncPictureRequest req = new AsyncPictureRequest();
        req.execute(info);
    }

    public void onFinished(Drawable drawable) {
        if (pd != null)
            pd.dismiss();
        if (drawable == null) {
            robotPic.setImageResource(R.drawable.robot);
        } else {
            scaleImage(robotPic, robotPic.getWidth(), drawable);
            // robotPic.setImageDrawable(drawable);
            // robotPic.setScaleType(ScaleType.FIT_XY);
            // robotPic.setAdjustViewBounds(true);
        }
    }

    private void scaleImage(ImageView view, int widthInDp, Drawable drawable) {

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        // Get current dimensions
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scale = ((float) widthInDp) / width;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the
        // ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        width = scaledBitmap.getWidth();
        height = scaledBitmap.getHeight();

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
                .getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    protected Dialog onCreateDialog(int id) {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (id) {
            case MainMenuSelection.HELPDIALOG:
                builder.setMessage(HELPMESSAGE)
                        .setCancelable(true)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();

                                    }
                                });
                dialog = builder.create();
                break;
            default:
                dialog = null;
        }
        return dialog;
    }

}
