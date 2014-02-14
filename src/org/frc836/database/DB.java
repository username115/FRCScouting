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

package org.frc836.database;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.frc836.aerialassist.MatchStatsAA;
import org.frc836.database.DBSyncService.LocalBinder;
import org.frc836.database.FRCScoutingContract.EVENT_LU_Entry;
import org.frc836.database.FRCScoutingContract.FACT_CYCLE_DATA_Entry;
import org.frc836.database.FRCScoutingContract.FACT_MATCH_DATA_Entry;
import org.frc836.ultimateascent.*;
import org.growingstems.scouting.Prefs;
import org.sigmond.net.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

public class DB {

	private HttpUtils utils;
	private String password;
	private Context context;
	private LocalBinder binder;

	@SuppressWarnings("unused")
	private DB() {
	}

	public DB(Context context, String pass, LocalBinder binder) {
		utils = new HttpUtils();
		password = pass;
		this.context = context;
		if (ScoutingDBHelper.helper == null)
			ScoutingDBHelper.helper = new ScoutingDBHelper(
					context.getApplicationContext());
		this.binder = binder;
	}

	public DB(Context context, LocalBinder binder) {
		this.context = context;
		utils = new HttpUtils();
		password = Prefs.getSavedPassword(context);
		if (ScoutingDBHelper.helper == null)
			ScoutingDBHelper.helper = new ScoutingDBHelper(
					context.getApplicationContext());
		this.binder = binder;
	}

	public void setBinder(LocalBinder binder) {
		this.binder = binder;
	}

	protected static Map<String, String> getPostData(ContentValues values) {
		Map<String, String> data = new HashMap<String, String>();
		for (String key : values.keySet()) {
			data.put(key, values.getAsString(key));
		}
		return data;
	}

	public void submitMatch(MatchStatsStruct team1Data,
			MatchStatsStruct team2Data, MatchStatsStruct team3Data) {
		synchronized (ScoutingDBHelper.helper) {
			try {
				SQLiteDatabase db = ScoutingDBHelper.helper
						.getWritableDatabase();
				db.insert(FACT_MATCH_DATA_Entry.TABLE_NAME, null,
						team1Data.getValues(this));
				db.insert(FACT_MATCH_DATA_Entry.TABLE_NAME, null,
						team2Data.getValues(this));
				db.insert(FACT_MATCH_DATA_Entry.TABLE_NAME, null,
						team3Data.getValues(this));

				MatchStatsAA data;
				if (team1Data instanceof MatchStatsAA) {
					data = (MatchStatsAA) team1Data;
					for (ContentValues cycle : data.getCycles(this)) {
						db.insert(FACT_CYCLE_DATA_Entry.TABLE_NAME, null, cycle);
					}
				}
				if (team2Data instanceof MatchStatsAA) {
					data = (MatchStatsAA) team2Data;
					for (ContentValues cycle : data.getCycles(this)) {
						db.insert(FACT_CYCLE_DATA_Entry.TABLE_NAME, null, cycle);
					}
				}
				if (team3Data instanceof MatchStatsAA) {
					data = (MatchStatsAA) team3Data;
					for (ContentValues cycle : data.getCycles(this)) {
						db.insert(FACT_CYCLE_DATA_Entry.TABLE_NAME, null, cycle);
					}
				}
				ScoutingDBHelper.helper.close();

				binder.setPassword(password);
				binder.startSync();
			} catch (Exception e) {
				String temp = e.getMessage();
				int i = 1;
			}
		}
	}

	public void submitPits(PitStats stats, HttpCallback callback) {
		// TODO
		/*
		 * Map<String, String> args = stats.getPost(); args.put("type", "pits");
		 * args.put("password", password);
		 * 
		 * utils.doPost(Prefs.getScoutingURL(context), args, callback);
		 */
	}

	public void setPass(String pass) {
		password = pass;
	}

	public void checkPass(String pass, HttpCallback callback) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("password", pass);
		params.put("type", "passConfirm");

		utils.doPost(Prefs.getScoutingURL(context), params, callback);
	}

	public void checkVersion(HttpCallback callback) {
		Map<String, String> args = new HashMap<String, String>();
		args.put("type", "versioncheck");
		utils.doPost(Prefs.getScoutingURL(context), args, callback);
	}

	public void getTeamPitInfo(String teamNum, HttpCallback callback) {
		// TODO
		/*
		 * Map<String, String> args = new HashMap<String, String>();
		 * args.put("team_id", teamNum); args.put("type", "pitInfo");
		 * args.put("password", password);
		 * utils.doPost(Prefs.getScoutingURL(context), args, callback);
		 */
	}

	public void getEventList(HttpCallback callback) {
		// TODO
		/*
		 * Map<String, String> args = new HashMap<String, String>();
		 * args.put("type", "paramRequest"); args.put("req", "eventList");
		 * utils.doPost(Prefs.getScoutingURL(context), args, callback);
		 */
	}

	public void getParams(String tableName, HttpCallback callback) {
		// TODO
		/*
		 * Map<String, String> args = new HashMap<String, String>();
		 * args.put("type", "paramRequest"); args.put("req", tableName);
		 * utils.doPost(Prefs.getScoutingURL(context), args, callback);
		 */
	}

	public void getEventStats(String eventName,
			EventStats.EventCallback callback) {
		// TODO
		/*
		 * Map<String, String> args = new HashMap<String, String>();
		 * args.put("type", "eventStats"); args.put("password", password);
		 * args.put("event_name", eventName);
		 * utils.doPost(Prefs.getScoutingURL(context), args, new EventStats(
		 * callback));
		 */
	}

	public void getTeamStats(int teamId, TeamStats.TeamCallback callback) {
		// TODO
		/*
		 * Map<String, String> args = new HashMap<String, String>();
		 * args.put("type", "teamStats"); args.put("password", password);
		 * args.put("team_id", String.valueOf(teamId)); utils.doPost(
		 * Prefs.getScoutingURL(context), args, new TeamStats(callback, teamId,
		 * Prefs.getEvent(context, "Chesapeake Regional")));
		 */
	}

	public void getMatchStats(String event, String match,
			MatchStats.MatchCallback callback) {
		// TODO
		/*
		 * Map<String, String> args = new HashMap<String, String>();
		 * args.put("event_name", event); args.put("match_id", match);
		 * args.put("password", password); args.put("type", "matchStats");
		 * utils.doPost(Prefs.getScoutingURL(context), args, new MatchStats(
		 * callback));
		 */
	}

	/*
	 * public void getPictureURL(String teamID, HttpCallback callback) {
	 * Map<String, String> args = new HashMap<String, String>();
	 * args.put("type", "robotPic"); args.put("team_id", teamID);
	 * utils.doPost(Prefs.getScoutingURL(context), args, callback); }
	 */

	public void getTeamPitStats(int teamId, HttpCallback callback) {
		// TODO
		/*
		 * Map<String, String> args = new HashMap<String, String>();
		 * args.put("type", "teamStats"); args.put("password", password);
		 * args.put("team_id", String.valueOf(teamId));
		 * utils.doPost(Prefs.getScoutingURL(context), args, callback);
		 */
	}

	public void getParamsPass(String table, HttpCallback callback) {
		// TODO
		/*
		 * Map<String, String> args = new HashMap<String, String>();
		 * args.put("password", password); args.put("type", "paramRequest");
		 * args.put("req", table); utils.doPost(Prefs.getScoutingURL(context),
		 * args, callback);
		 */
	}

	public long getEventIDFromName(String EventName) {
		synchronized (ScoutingDBHelper.helper) {
			SQLiteDatabase db = ScoutingDBHelper.helper.getReadableDatabase();
			String[] projection = { EVENT_LU_Entry.COLUMN_NAME_ID };
			String[] where = { EventName };
			Cursor c = db.query(EVENT_LU_Entry.TABLE_NAME, // from the event_lu
															// table
					projection, // select
					EVENT_LU_Entry.COLUMN_NAME_EVENT_NAME + "=?", // where
																	// event_name
																	// ==
					where, // EventName
					null, // don't group
					null, // don't filter
					null, // don't order
					"0,1"); // limit to 1
			c.moveToFirst();
			db.close();
			return c.getLong(c
					.getColumnIndexOrThrow(EVENT_LU_Entry.COLUMN_NAME_ID));
		}
	}

	public static void exportToCSV(Context context) {
		try {

			ExportCallback cb = new ExportCallback();

			cb.context = context;

			CSVExporter export = new CSVExporter();
			export.execute(cb);

		} catch (Exception e) {
			Toast.makeText(context, "Error exporting Database",
					Toast.LENGTH_LONG).show();
		}
	}

	private static class ExportCallback {
		Context context;

		public void finish(int code) {
			if (code == 0)
				Toast.makeText(context, "DB Exported", Toast.LENGTH_LONG)
						.show();
			else
				Toast.makeText(context, "Error exporting Database",
						Toast.LENGTH_LONG).show();
		}
	}

	private static class CSVExporter extends
			AsyncTask<ExportCallback, Integer, Integer> {

		ExportCallback callback;

		@Override
		protected Integer doInBackground(ExportCallback... params) {
			synchronized (ScoutingDBHelper.helper) {
				try {

					SQLiteDatabase db = ScoutingDBHelper.helper
							.getReadableDatabase();

					callback = params[0];

					String match_data = "";

					Cursor c;

					c = db.rawQuery(
							"SELECT * FROM "
									+ FRCScoutingContract.FACT_MATCH_DATA_Entry.TABLE_NAME,
							null);

					for (int i = 0; i < c.getColumnCount(); i++) {
						if (i > 0)
							match_data += ",";
						match_data += c.getColumnName(i);
					}
					match_data += "\n";
					if (c.moveToFirst())
						do {
							for (int j = 0; j < c.getColumnCount(); j++) {
								if (j > 0)
									match_data += ",";
								match_data += c.getString(j);
							}
							match_data += "\n";
						} while (c.moveToNext());

					File sd = new File(
							Environment
									.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
							"matches.csv");
					FileOutputStream destination = new FileOutputStream(sd);
					destination.write(match_data.getBytes());
					destination.close();

					return 0;
				} catch (Exception e) {
					return 1;
				}
			}
		}

		protected void onPostExecute(Integer result) {
			callback.finish(result);
		}

	}

}
