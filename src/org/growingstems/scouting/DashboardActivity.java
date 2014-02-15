/*
 * Copyright 2014 Daniel Logan
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

import org.frc836.aerialassist.PitsActivity;
import org.frc836.database.DB;
import org.frc836.database.DBSyncService;
import org.frc836.database.DBSyncService.LocalBinder;
import org.growingstems.scouting.R;
import org.sigmond.net.HttpCallback;
import org.sigmond.net.HttpRequestInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class DashboardActivity extends Activity {

	private Button match;
	private Button pits;
	private Button data;
	private ImageView beeLogo;
	private ImageView stemsLogo;

	private LocalBinder binder;
	private ServiceWatcher watcher = new ServiceWatcher();

	private static final int VERSION_DIALOG = 7;
	private static final int URL_DIALOG = 39382;
	private static final int PITS_ACTIVITY_CODE = 4639;
	private static final int MATCH_ACTIVITY_CODE = 4640;
	private static final int DATA_ACTIVITY_CODE = 4641;

	private String HELPMESSAGE;
	private static final String URL_MESSAGE = "You have not set a web site for this app to interface with.\nWould you like to do so now?";
	private static String VERSION_MESSAGE;
	private String versionCode;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		
		versionCode = "";

		HELPMESSAGE = "Version: " + getString(R.string.VersionID) + "\nDate: "
				+ getString(R.string.VersionDate)
				+ "\nRefer all questions or comments to "
				+ getString(R.string.dev_email);

		match = (Button) findViewById(R.id.matchB);
		pits = (Button) findViewById(R.id.pitB);
		data = (Button) findViewById(R.id.dataB);
		beeLogo = (ImageView) findViewById(R.id.beeLogo);
		stemsLogo = (ImageView) findViewById(R.id.stemsLogo);

		Intent intent = new Intent(this, DBSyncService.class);
		bindService(intent, watcher, 0);

		match.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(),
						MatchStartActivity.class);
				startActivityForResult(intent, MATCH_ACTIVITY_CODE);
			}
		});

		pits.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), PitsActivity.class);
				startActivityForResult(intent, PITS_ACTIVITY_CODE);

			}
		});

		data.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), DataActivity.class);
				startActivityForResult(intent, DATA_ACTIVITY_CODE);

			}
		});

		beeLogo.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Uri uri = Uri.parse("http://robobees.org");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});

		stemsLogo.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Uri uri = Uri.parse("http://growingstems.org");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});

		String url = Prefs.getScoutingURLNoDefault(getApplicationContext());
		if (url.length() > 0) {
			DB db = new DB(getBaseContext(), binder);
			db.checkVersion(new VersionCallback());

			EventList ev = new EventList(getApplicationContext());
			ev.downloadEventsList(null);
		} else {
			showDialog(URL_DIALOG);
		}

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		if (binder == null) {

		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(watcher);
	}

	protected class ServiceWatcher implements ServiceConnection {

		public void onServiceConnected(ComponentName name, IBinder service) {
			if (service instanceof LocalBinder) {
				binder = (LocalBinder) service;
				Toast.makeText(getApplicationContext(), "Service Bound", Toast.LENGTH_SHORT).show();
			}
		}

		public void onServiceDisconnected(ComponentName name) {
			Toast.makeText(getApplicationContext(), "Service UnBound", Toast.LENGTH_SHORT).show();
		}

	}

	protected class VersionCallback implements HttpCallback {

		public void onResponse(HttpRequestInfo resp) {
			try {
				versionCode = resp.getResponseString().trim();
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
					showDialog(VERSION_DIALOG);
				}
			} catch (Exception e) {

			}
		}

		public void onError(Exception e) {
		}

	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case VERSION_DIALOG:
			builder.setMessage(VERSION_MESSAGE)
					.setCancelable(true)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Uri uri = Uri.parse(getString(
											R.string.APKURL, versionCode));
									Intent intent = new Intent(
											Intent.ACTION_VIEW, uri);
									startActivity(intent);
									finish();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			dialog = builder.create();
			break;
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
		case URL_DIALOG:
			builder.setMessage(URL_MESSAGE)
					.setCancelable(true)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									MainMenuSelection
											.openSettings(DashboardActivity.this);
									dialog.cancel();
								}
							})
					.setNegativeButton("No",
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

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return MainMenuSelection.onOptionsItemSelected(item, this) ? true
				: super.onOptionsItemSelected(item);
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
		case DATA_ACTIVITY_CODE:
			DB db = new DB(getBaseContext(), binder);
			db.checkVersion(new VersionCallback());
			break;
		default:
			break;
		}
	}

}
