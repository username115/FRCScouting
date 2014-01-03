/*
 * Copyright 2013 Daniel Logan
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

import org.frc836.ultimateascent.MatchActivity;
import org.growingstems.scouting.R;
import org.sigmond.net.AsyncPictureRequest;
import org.sigmond.net.HttpCallback;
import org.sigmond.net.HttpRequestInfo;
import org.sigmond.net.PicCallback;
import org.sigmond.net.PicRequestInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MatchStartActivity extends Activity implements PicCallback {

	private EditText teamNum;
	private TextView position;
	private EditText matchNum;
	private Button startB;
	private ImageView robotPic;

	private String HELPMESSAGE;

	private MatchSchedule schedule;

	private static final int MATCH_ACTIVITY_REQUEST = 0;

	private ParamList notesList;

	private ProgressDialog pd;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.matchstart);

		notesList = new ParamList(getApplicationContext(), "notes_options");

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

		notesList.downloadParamList(null, null);

	}

	public void onResume() {
		super.onResume();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		String pos = prefs.getString("positionPref", "red 1");

		position.setText(pos);
		if (pos.contains("blue"))
			position.setTextColor(Color.BLUE);
		else
			position.setTextColor(Color.RED);
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

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return MainMenuSelection.onOptionsItemSelected(item, this) ? true
				: super.onOptionsItemSelected(item);
	}

	private class StartClickListener implements OnClickListener {

		public void onClick(View v) {
			Intent intent = new Intent(MatchStartActivity.this,
					MatchActivity.class);
			intent.putExtra("team", teamNum.getText().toString());
			intent.putExtra("match", matchNum.getText().toString());
			startActivityForResult(intent, MATCH_ACTIVITY_REQUEST);

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

			String pos = prefs.getString("positionPref", "red 1");

			position.setText(pos);
			if (pos.contains("blue"))
				position.setTextColor(Color.BLUE);
			else
				position.setTextColor(Color.RED);
			if (matchNum.getText().length() > 0)
				setMatch(Integer.valueOf(matchNum.getText().toString()));
			notesList.downloadParamList(null, null);
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
		DB db = new DB(getApplicationContext());
		db.getPictureURL(teamNum.getText().toString(), new PictureURLCallback());
	}

	private class PictureURLCallback implements HttpCallback {

		public void onResponse(HttpRequestInfo resp) {
			PicRequestInfo info = new PicRequestInfo(resp.getResponseString(),
					MatchStartActivity.this);
			AsyncPictureRequest req = new AsyncPictureRequest();
			req.execute(info);
		}

		public void onError(Exception e) {
			if (pd != null)
				pd.dismiss();
		}

	}

	public void onFinished(Drawable drawable) {
		if (pd != null)
			pd.dismiss();
		if (drawable == null) {
			robotPic.setImageResource(R.drawable.robot);
		} else {
			robotPic.setImageDrawable(drawable);
		}
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
