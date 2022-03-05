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

import org.frc836.database.DB;
import org.frc836.database.DBActivity;
import org.frc836.database.HttpCallback;
import org.growingstems.scouting.data.DataActivity;
import org.frc836.yearly.PitsActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;

public class DashboardActivity extends DBActivity {

	private Button match;
	private Button pits;
	private Button data;
	private Button picklist;
	private ImageView beeLogo;
	private ImageView stemsLogo;

	private TextView fmsApiLink;

	private static final int PITS_ACTIVITY_CODE = 4639;
	private static final int MATCH_ACTIVITY_CODE = 4640;
	private static final int DATA_ACTIVITY_CODE = 4641;
	private static final int PICK_ACTIVITY_CODE = 4642;

	private String HELPMESSAGE;
	private static final String URL_MESSAGE = "You have not set a web site for this app to interface with.\nWould you like to do so now?";
	private static String VERSION_MESSAGE;
	private String versionCode;

	private CheckBox doNotAskAgainC;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		if (getIntent().getBooleanExtra("ExitApp", false)) {
			finish();
			return;
		}

		versionCode = "";

		HELPMESSAGE = "Version: " + getString(R.string.VersionID) + "\nDate: "
				+ getString(R.string.VersionDate)
				+ "\nRefer all questions or comments to "
				+ getString(R.string.dev_email);

		match = findViewById(R.id.matchB);
		pits = findViewById(R.id.pitB);
		data = findViewById(R.id.dataB);
		picklist = findViewById(R.id.picklistB);
		beeLogo = findViewById(R.id.beeLogo);
		stemsLogo = findViewById(R.id.stemsLogo);
		fmsApiLink = findViewById(R.id.fmsApiLink);


		match.setOnClickListener(v -> {
			Intent intent = new Intent(getBaseContext(),
					MatchStartActivity.class);
			startActivityForResult(intent, MATCH_ACTIVITY_CODE);
		});

		pits.setOnClickListener(v -> {

			Intent intent = new Intent(getBaseContext(),
					PitsActivity.class);
			startActivityForResult(intent, PITS_ACTIVITY_CODE);
		});

		data.setOnClickListener(v -> {

			Intent intent = new Intent(getBaseContext(), DataActivity.class);
			startActivityForResult(intent, DATA_ACTIVITY_CODE);

		});

		picklist.setOnClickListener(v -> {
			Intent intent = new Intent(getBaseContext(), PickActivity.class);
			startActivityForResult(intent, PICK_ACTIVITY_CODE);
		});

		beeLogo.setOnClickListener(v -> {
			Uri uri = Uri.parse("http://robobees.org");
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		});

		stemsLogo.setOnClickListener(v -> {
			Uri uri = Uri.parse("http://growingstems.org");
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		});

		fmsApiLink.setOnClickListener(view -> {
			Uri uri = Uri.parse("https://frc-events.firstinspires.org/services/API");
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		});

		DB db = new DB(getBaseContext(), binder);
		PreferenceManager.setDefaultValues(this, R.xml.mainprefs, false);
		String url = Prefs.getScoutingURLNoDefault(getApplicationContext());
		if (url.length() > 0) {
			db.checkVersion(new VersionCallback());
		} else if (!Prefs.getDontPrompt(getApplicationContext(), false)) {
			View doNotAskView = LayoutInflater.from(this).inflate(
					R.layout.donotaskagaincheckbox, null);
			doNotAskAgainC = doNotAskView
					.findViewById(R.id.donotaskagainC);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(URL_MESSAGE)
					.setCancelable(true)
					.setView(doNotAskView)
					.setPositiveButton("Yes",
							(dialog, which) -> {
								Prefs.setDontPrompt(
										getApplicationContext(),
										doNotAskAgainC.isChecked());
								MainMenuSelection
										.openSettings(DashboardActivity.this);
								dialog.cancel();
							})
					.setNegativeButton("No",
							(dialog, which) -> {
								Prefs.setDontPrompt(
										getApplicationContext(),
										doNotAskAgainC.isChecked());
								dialog.cancel();
							});
			builder.show();
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent.getBooleanExtra("ExitApp", false)) {
			finish();
		}
	}

	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public String getHelpMessage() {
		return HELPMESSAGE;
	}

	protected class VersionCallback implements HttpCallback {

		@Override
		public void onResponse(String resp) {
			try {
				versionCode = resp.trim();
				VERSION_MESSAGE = "The server you have linked to was made for a different version of this app.\nYour Version: "
						+ getString(R.string.VersionID)
						+ "\nServer Version: "
						+ versionCode
						+ "\nWould you like to download the correct version?";
				String verCode = versionCode.substring(0,
						versionCode.lastIndexOf("."));
				String localVersion = getString(R.string.VersionID);
				localVersion = localVersion.substring(0,
						localVersion.lastIndexOf("."));
				if (!verCode.equals(localVersion)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
					builder.setMessage(VERSION_MESSAGE)
							.setCancelable(true)
							.setPositiveButton("Yes",
									(dialog, id) -> {
										Uri uri = Uri.parse(getString(
												R.string.APKURL, versionCode));
										Intent intent = new Intent(
												Intent.ACTION_VIEW, uri);
										startActivity(intent);
										finish();
									})
							.setNegativeButton("No",
									(dialog, id) -> dialog.cancel());
					builder.show();
				}
			} catch (Exception e) {
				//TODO
			}
		}

		@Override
		public void onError(VolleyError e) {
			//TODO
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Prefs.PREFS_ACTIVITY_CODE:
			MatchSchedule schedule = new MatchSchedule();
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			schedule.updateSchedule(
					prefs.getString("eventPref", "Chesapeake Regional"), this,
					false);
			break;
		case PITS_ACTIVITY_CODE:
		case MATCH_ACTIVITY_CODE:
		case PICK_ACTIVITY_CODE:
		case DATA_ACTIVITY_CODE:
			DB db = new DB(getBaseContext(), binder);
			db.checkVersion(new VersionCallback());
			break;
		default:
			break;
		}
	}

}
