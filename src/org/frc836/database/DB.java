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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frc836.aerialassist.MatchStatsAA;
import org.frc836.aerialassist.PitStatsAA;
import org.frc836.database.DBSyncService.LocalBinder;
import org.frc836.database.FRCScoutingContract.CONFIGURATION_LU_Entry;
import org.frc836.database.FRCScoutingContract.EVENT_LU_Entry;
import org.frc836.database.FRCScoutingContract.FACT_CYCLE_DATA_Entry;
import org.frc836.database.FRCScoutingContract.FACT_MATCH_DATA_Entry;
import org.frc836.database.FRCScoutingContract.NOTES_OPTIONS_Entry;
import org.frc836.database.FRCScoutingContract.SCOUT_PIT_DATA_Entry;
import org.frc836.database.FRCScoutingContract.WHEEL_BASE_LU_Entry;
import org.frc836.database.FRCScoutingContract.WHEEL_TYPE_LU_Entry;
import org.frc836.samsung.fileselector.FileOperation;
import org.frc836.samsung.fileselector.FileSelector;
import org.frc836.samsung.fileselector.OnHandleFileListener;
import org.frc836.ultimateascent.*;
import org.growingstems.scouting.Prefs;
import org.sigmond.net.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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

	public void startSync() {
		if (binder != null) {
			binder.setPassword(password);
			binder.startSync();
		}
	}

	private void insertOrUpdate(SQLiteDatabase db, String table,
			String nullColumnHack, ContentValues values, String idColumnName,
			String whereClause, String[] whereArgs) {

		String[] projection = { idColumnName };

		Cursor c = db.query(table, projection, whereClause, whereArgs, null,
				null, null, "0,1");
		if (c.moveToFirst()) {
			String[] id = { c.getString(c.getColumnIndexOrThrow(idColumnName)) };
			db.update(table, values, idColumnName + "=?", id);
		} else {
			db.insert(table, nullColumnHack, values);
		}
	}

	public boolean submitMatch(MatchStatsStruct team1Data,
			MatchStatsStruct team2Data, MatchStatsStruct team3Data) {
		synchronized (ScoutingDBHelper.helper) {
			try {
				SQLiteDatabase db = ScoutingDBHelper.helper
						.getWritableDatabase();

				String where = FACT_MATCH_DATA_Entry.COLUMN_NAME_EVENT_ID
						+ "=? AND "
						+ FACT_MATCH_DATA_Entry.COLUMN_NAME_MATCH_ID
						+ "=? AND " + FACT_MATCH_DATA_Entry.COLUMN_NAME_TEAM_ID
						+ "=?";

				ContentValues values = team1Data.getValues(this, db);
				String[] whereArgs = {
						values.getAsString(FACT_MATCH_DATA_Entry.COLUMN_NAME_EVENT_ID),
						values.getAsString(FACT_MATCH_DATA_Entry.COLUMN_NAME_MATCH_ID),
						values.getAsString(FACT_MATCH_DATA_Entry.COLUMN_NAME_TEAM_ID) };

				insertOrUpdate(db, FACT_MATCH_DATA_Entry.TABLE_NAME, null,
						values, FACT_MATCH_DATA_Entry.COLUMN_NAME_ID, where,
						whereArgs);

				values = team2Data.getValues(this, db);
				whereArgs[0] = values
						.getAsString(FACT_MATCH_DATA_Entry.COLUMN_NAME_EVENT_ID);
				whereArgs[1] = values
						.getAsString(FACT_MATCH_DATA_Entry.COLUMN_NAME_MATCH_ID);
				whereArgs[2] = values
						.getAsString(FACT_MATCH_DATA_Entry.COLUMN_NAME_TEAM_ID);

				insertOrUpdate(db, FACT_MATCH_DATA_Entry.TABLE_NAME, null,
						values, FACT_MATCH_DATA_Entry.COLUMN_NAME_ID, where,
						whereArgs);

				values = team3Data.getValues(this, db);
				whereArgs[0] = values
						.getAsString(FACT_MATCH_DATA_Entry.COLUMN_NAME_EVENT_ID);
				whereArgs[1] = values
						.getAsString(FACT_MATCH_DATA_Entry.COLUMN_NAME_MATCH_ID);
				whereArgs[2] = values
						.getAsString(FACT_MATCH_DATA_Entry.COLUMN_NAME_TEAM_ID);

				insertOrUpdate(db, FACT_MATCH_DATA_Entry.TABLE_NAME, null,
						values, FACT_MATCH_DATA_Entry.COLUMN_NAME_ID, where,
						whereArgs);

				whereArgs = new String[4];
				where = FACT_CYCLE_DATA_Entry.COLUMN_NAME_EVENT_ID + "=? AND "
						+ FACT_CYCLE_DATA_Entry.COLUMN_NAME_MATCH_ID
						+ "=? AND " + FACT_CYCLE_DATA_Entry.COLUMN_NAME_TEAM_ID
						+ "=? AND "
						+ FACT_CYCLE_DATA_Entry.COLUMN_NAME_CYCLE_NUM + "=?";

				MatchStatsAA data;
				if (team1Data instanceof MatchStatsAA) {
					data = (MatchStatsAA) team1Data;
					for (ContentValues cycle : data.getCycles(this, db)) {
						whereArgs[0] = cycle
								.getAsString(FACT_CYCLE_DATA_Entry.COLUMN_NAME_EVENT_ID);
						whereArgs[1] = cycle
								.getAsString(FACT_CYCLE_DATA_Entry.COLUMN_NAME_MATCH_ID);
						whereArgs[2] = cycle
								.getAsString(FACT_CYCLE_DATA_Entry.COLUMN_NAME_TEAM_ID);
						whereArgs[3] = cycle
								.getAsString(FACT_CYCLE_DATA_Entry.COLUMN_NAME_CYCLE_NUM);

						insertOrUpdate(db, FACT_CYCLE_DATA_Entry.TABLE_NAME,
								null, cycle,
								FACT_CYCLE_DATA_Entry.COLUMN_NAME_ID, where,
								whereArgs);
					}
				}
				if (team2Data instanceof MatchStatsAA) {
					data = (MatchStatsAA) team2Data;
					for (ContentValues cycle : data.getCycles(this, db)) {
						whereArgs[0] = cycle
								.getAsString(FACT_CYCLE_DATA_Entry.COLUMN_NAME_EVENT_ID);
						whereArgs[1] = cycle
								.getAsString(FACT_CYCLE_DATA_Entry.COLUMN_NAME_MATCH_ID);
						whereArgs[2] = cycle
								.getAsString(FACT_CYCLE_DATA_Entry.COLUMN_NAME_TEAM_ID);
						whereArgs[3] = cycle
								.getAsString(FACT_CYCLE_DATA_Entry.COLUMN_NAME_CYCLE_NUM);

						insertOrUpdate(db, FACT_CYCLE_DATA_Entry.TABLE_NAME,
								null, cycle,
								FACT_CYCLE_DATA_Entry.COLUMN_NAME_ID, where,
								whereArgs);
					}
				}
				if (team3Data instanceof MatchStatsAA) {
					data = (MatchStatsAA) team3Data;
					for (ContentValues cycle : data.getCycles(this, db)) {
						whereArgs[0] = cycle
								.getAsString(FACT_CYCLE_DATA_Entry.COLUMN_NAME_EVENT_ID);
						whereArgs[1] = cycle
								.getAsString(FACT_CYCLE_DATA_Entry.COLUMN_NAME_MATCH_ID);
						whereArgs[2] = cycle
								.getAsString(FACT_CYCLE_DATA_Entry.COLUMN_NAME_TEAM_ID);
						whereArgs[3] = cycle
								.getAsString(FACT_CYCLE_DATA_Entry.COLUMN_NAME_CYCLE_NUM);

						insertOrUpdate(db, FACT_CYCLE_DATA_Entry.TABLE_NAME,
								null, cycle,
								FACT_CYCLE_DATA_Entry.COLUMN_NAME_ID, where,
								whereArgs);
					}
				}
				ScoutingDBHelper.helper.close();

				startSync();

				return true;

			} catch (Exception e) {
				return false;
			}
		}
	}

	public boolean submitPits(PitStats stats) {
		synchronized (ScoutingDBHelper.helper) {
			try {

				SQLiteDatabase db = ScoutingDBHelper.helper
						.getWritableDatabase();
				ContentValues values = stats.getValues(this, db);

				String[] where = { values
						.getAsString(SCOUT_PIT_DATA_Entry.COLUMN_NAME_TEAM_ID) };

				insertOrUpdate(db, SCOUT_PIT_DATA_Entry.TABLE_NAME, null,
						values, SCOUT_PIT_DATA_Entry.COLUMN_NAME_ID,
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_TEAM_ID + "=?", where);

				ScoutingDBHelper.helper.close();

				startSync();
				return true;
			} catch (Exception e) {
				return false;
			}
		}
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

	public String getTeamPitInfo(String teamNum) {

		synchronized (ScoutingDBHelper.helper) {
			try {

				SQLiteDatabase db = ScoutingDBHelper.helper
						.getReadableDatabase();

				String[] projection = { SCOUT_PIT_DATA_Entry.COLUMN_NAME_TIMESTAMP };
				String[] where = { teamNum };
				Cursor c = db.query(SCOUT_PIT_DATA_Entry.TABLE_NAME, // from the
																		// scout_pit_data
						// table
						projection, // select
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_TEAM_ID + "=?", // where
																			// team_id
																			// ==
						where, // teamNum
						null, // don't group
						null, // don't filter
						null, // don't order
						"0,1"); // limit to 1
				c.moveToFirst();

				String date = c
						.getString(c
								.getColumnIndexOrThrow(SCOUT_PIT_DATA_Entry.COLUMN_NAME_TIMESTAMP));

				ScoutingDBHelper.helper.close();

				return date;

			} catch (Exception e) {
				return "";
			}
		}
	}

	public List<String> getEventList() {

		synchronized (ScoutingDBHelper.helper) {
			try {
				SQLiteDatabase db = ScoutingDBHelper.helper
						.getReadableDatabase();

				String[] projection = { EVENT_LU_Entry.COLUMN_NAME_EVENT_NAME };

				Cursor c = db.query(EVENT_LU_Entry.TABLE_NAME, projection,
						null, null, null, null, EVENT_LU_Entry.COLUMN_NAME_ID);

				List<String> ret = new ArrayList<String>(c.getCount());

				if (c.moveToFirst())
					do {
						ret.add(c.getString(c
								.getColumnIndexOrThrow(EVENT_LU_Entry.COLUMN_NAME_EVENT_NAME)));
					} while (c.moveToNext());
				else
					return null;

				return ret;
			} catch (Exception e) {
				return null;
			}
		}
	}

	public List<String> getConfigList() {

		synchronized (ScoutingDBHelper.helper) {
			try {
				SQLiteDatabase db = ScoutingDBHelper.helper
						.getReadableDatabase();

				String[] projection = { CONFIGURATION_LU_Entry.COLUMN_NAME_CONFIGURATION_DESC };

				Cursor c = db.query(EVENT_LU_Entry.TABLE_NAME, projection,
						null, null, null, null,
						CONFIGURATION_LU_Entry.COLUMN_NAME_ID);

				List<String> ret = new ArrayList<String>(c.getCount());

				if (c.moveToFirst())
					do {
						ret.add(c.getString(c
								.getColumnIndexOrThrow(CONFIGURATION_LU_Entry.COLUMN_NAME_CONFIGURATION_DESC)));
					} while (c.moveToNext());
				else
					return null;

				return ret;
			} catch (Exception e) {
				return null;
			}
		}
	}

	public List<String> getWheelBaseList() {

		synchronized (ScoutingDBHelper.helper) {
			try {
				SQLiteDatabase db = ScoutingDBHelper.helper
						.getReadableDatabase();

				String[] projection = { WHEEL_BASE_LU_Entry.COLUMN_NAME_WHEEL_BASE_DESC };

				Cursor c = db.query(EVENT_LU_Entry.TABLE_NAME, projection,
						null, null, null, null,
						WHEEL_BASE_LU_Entry.COLUMN_NAME_ID);

				List<String> ret = new ArrayList<String>(c.getCount());

				if (c.moveToFirst())
					do {
						ret.add(c.getString(c
								.getColumnIndexOrThrow(WHEEL_BASE_LU_Entry.COLUMN_NAME_WHEEL_BASE_DESC)));
					} while (c.moveToNext());
				else
					return null;

				return ret;
			} catch (Exception e) {
				return null;
			}
		}
	}

	public List<String> getWheelTypeList() {

		synchronized (ScoutingDBHelper.helper) {
			try {
				SQLiteDatabase db = ScoutingDBHelper.helper
						.getReadableDatabase();

				String[] projection = { WHEEL_TYPE_LU_Entry.COLUMN_NAME_WHEEL_TYPE_DESC };

				Cursor c = db.query(EVENT_LU_Entry.TABLE_NAME, projection,
						null, null, null, null,
						WHEEL_TYPE_LU_Entry.COLUMN_NAME_ID);

				List<String> ret = new ArrayList<String>(c.getCount());

				if (c.moveToFirst())
					do {
						ret.add(c.getString(c
								.getColumnIndexOrThrow(WHEEL_TYPE_LU_Entry.COLUMN_NAME_WHEEL_TYPE_DESC)));
					} while (c.moveToNext());
				else
					return null;

				return ret;
			} catch (Exception e) {
				return null;
			}
		}
	}

	public String getURLFromEventName(String eventName) {
		synchronized (ScoutingDBHelper.helper) {
			try {
				SQLiteDatabase db = ScoutingDBHelper.helper
						.getReadableDatabase();
				String[] projection = { EVENT_LU_Entry.COLUMN_NAME_MATCH_URL };
				String[] where = { eventName };
				Cursor c = db.query(EVENT_LU_Entry.TABLE_NAME, // from the
																// event_lu
																// table
						projection, // select
						EVENT_LU_Entry.COLUMN_NAME_EVENT_NAME + " LIKE ?", // where
																			// event_name
																			// ==
						where, // EventName
						null, // don't group
						null, // don't filter
						null, // don't order
						"0,1"); // limit to 1
				c.moveToFirst();
				return c.getString(c
						.getColumnIndexOrThrow(EVENT_LU_Entry.COLUMN_NAME_MATCH_URL));
			} catch (Exception e) {
				return null;
			}
		}
	}

	public List<String> getNotesOptions() {
		synchronized (ScoutingDBHelper.helper) {
			try {
				SQLiteDatabase db = ScoutingDBHelper.helper
						.getReadableDatabase();

				String[] projection = { NOTES_OPTIONS_Entry.COLUMN_NAME_OPTION_TEXT };

				Cursor c = db.query(NOTES_OPTIONS_Entry.TABLE_NAME, projection,
						null, null, null, null,
						NOTES_OPTIONS_Entry.COLUMN_NAME_ID);

				List<String> ret = new ArrayList<String>(c.getCount());

				if (c.moveToFirst())
					do {
						ret.add(c.getString(c
								.getColumnIndexOrThrow(NOTES_OPTIONS_Entry.COLUMN_NAME_OPTION_TEXT)));
					} while (c.moveToNext());
				else
					return null;

				return ret;
			} catch (Exception e) {
				return null;
			}
		}
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

	public PitStats getTeamPitStats(int teamNum) {

		synchronized (ScoutingDBHelper.helper) {

			try {
				PitStatsAA stats = new PitStatsAA();

				SQLiteDatabase db = ScoutingDBHelper.helper
						.getReadableDatabase();

				String[] projection = {
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_TEAM_ID,
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_CONFIGURATION_ID,
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_WHEEL_TYPE_ID,
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_WHEEL_BASE_ID,
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTONOMOUS_MODE,
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTO_HIGH,
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTO_LOW,
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTO_HOT,
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTO_MOBILE,
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTO_GOALIE,
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_TRUSS,
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_CATCH,
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_ACTIVE_CONTROL,
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_LAUNCH_BALL,
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_SCORE_HIGH,
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_SCORE_LOW,
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_MAX_HEIGHT,
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_SCOUT_COMMENTS };
				String[] where = { String.valueOf(teamNum) };
				Cursor c = db.query(SCOUT_PIT_DATA_Entry.TABLE_NAME, // from the
																		// scout_pit_data
						// table
						projection, // select
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_TEAM_ID + "=?", // where
																			// team_id
																			// ==
						where, // teamNum
						null, // don't group
						null, // don't filter
						null, // don't order
						"0,1"); // limit to 1

				stats.fromCursor(c, this, db);

				ScoutingDBHelper.helper.close();

				return stats;
			} catch (Exception e) {
				return null;
			}

		}
	}

	public long getEventIDFromName(String eventName, SQLiteDatabase db) {

		String[] projection = { EVENT_LU_Entry.COLUMN_NAME_ID };
		String[] where = { eventName };
		Cursor c = db.query(EVENT_LU_Entry.TABLE_NAME, // from the event_lu
														// table
				projection, // select
				EVENT_LU_Entry.COLUMN_NAME_EVENT_NAME + " LIKE ?", // where
																	// event_name
																	// ==
				where, // EventName
				null, // don't group
				null, // don't filter
				null, // don't order
				"0,1"); // limit to 1
		c.moveToFirst();
		return c.getLong(c.getColumnIndexOrThrow(EVENT_LU_Entry.COLUMN_NAME_ID));
	}

	public long getConfigIDFromName(String config, SQLiteDatabase db) {

		String[] projection = { CONFIGURATION_LU_Entry.COLUMN_NAME_ID };
		String[] where = { config };
		Cursor c = db.query(CONFIGURATION_LU_Entry.TABLE_NAME, projection, // select
				CONFIGURATION_LU_Entry.COLUMN_NAME_CONFIGURATION_DESC
						+ " LIKE ?", where, // EventName
				null, // don't group
				null, // don't filter
				null, // don't order
				"0,1"); // limit to 1
		c.moveToFirst();
		return c.getLong(c
				.getColumnIndexOrThrow(CONFIGURATION_LU_Entry.COLUMN_NAME_ID));
	}

	public long getWheelBaseIDFromName(String base, SQLiteDatabase db) {

		String[] projection = { WHEEL_BASE_LU_Entry.COLUMN_NAME_ID };
		String[] where = { base };
		Cursor c = db.query(WHEEL_BASE_LU_Entry.TABLE_NAME,
				projection, // select
				WHEEL_BASE_LU_Entry.COLUMN_NAME_WHEEL_BASE_DESC + " LIKE ?",
				where, // EventName
				null, // don't group
				null, // don't filter
				null, // don't order
				"0,1"); // limit to 1
		c.moveToFirst();
		return c.getLong(c
				.getColumnIndexOrThrow(WHEEL_BASE_LU_Entry.COLUMN_NAME_ID));
	}

	public long getWheelTypeIDFromName(String type, SQLiteDatabase db) {

		String[] projection = { WHEEL_TYPE_LU_Entry.COLUMN_NAME_ID };
		String[] where = { type };
		Cursor c = db.query(WHEEL_TYPE_LU_Entry.TABLE_NAME,
				projection, // select
				WHEEL_TYPE_LU_Entry.COLUMN_NAME_WHEEL_TYPE_DESC + " LIKE ?",
				where, // EventName
				null, // don't group
				null, // don't filter
				null, // don't order
				"0,1"); // limit to 1
		c.moveToFirst();
		return c.getLong(c
				.getColumnIndexOrThrow(WHEEL_TYPE_LU_Entry.COLUMN_NAME_ID));
	}

	public String getConfigNameFromID(int config, SQLiteDatabase db) {

		String[] projection = { CONFIGURATION_LU_Entry.COLUMN_NAME_CONFIGURATION_DESC };
		String[] where = { String.valueOf(config) };
		Cursor c = db.query(CONFIGURATION_LU_Entry.TABLE_NAME, projection, // select
				CONFIGURATION_LU_Entry.COLUMN_NAME_ID + "= ?", where, // EventName
				null, // don't group
				null, // don't filter
				null, // don't order
				"0,1"); // limit to 1
		c.moveToFirst();
		return c.getString(c
				.getColumnIndexOrThrow(CONFIGURATION_LU_Entry.COLUMN_NAME_CONFIGURATION_DESC));
	}

	public String getWheelBaseNameFromID(int base, SQLiteDatabase db) {

		String[] projection = { WHEEL_BASE_LU_Entry.COLUMN_NAME_WHEEL_BASE_DESC };
		String[] where = { String.valueOf(base) };
		Cursor c = db.query(WHEEL_BASE_LU_Entry.TABLE_NAME, projection, // select
				WHEEL_BASE_LU_Entry.COLUMN_NAME_ID + "= ?", where, // EventName
				null, // don't group
				null, // don't filter
				null, // don't order
				"0,1"); // limit to 1
		c.moveToFirst();
		return c.getString(c
				.getColumnIndexOrThrow(WHEEL_BASE_LU_Entry.COLUMN_NAME_WHEEL_BASE_DESC));
	}

	public String getWheelTypeNameFromID(int type, SQLiteDatabase db) {

		String[] projection = { WHEEL_TYPE_LU_Entry.COLUMN_NAME_WHEEL_TYPE_DESC };
		String[] where = { String.valueOf(type) };
		Cursor c = db.query(WHEEL_TYPE_LU_Entry.TABLE_NAME, projection, // select
				WHEEL_TYPE_LU_Entry.COLUMN_NAME_ID + " LIKE ?", where, // EventName
				null, // don't group
				null, // don't filter
				null, // don't order
				"0,1"); // limit to 1
		c.moveToFirst();
		return c.getString(c
				.getColumnIndexOrThrow(WHEEL_TYPE_LU_Entry.COLUMN_NAME_WHEEL_TYPE_DESC));
	}

	static OnHandleFileListener mDirSelectListener = new OnHandleFileListener() {
		@Override
		public void handleFile(String filePath) {
			cb.filename = filePath;
			CSVExporter export = new CSVExporter();
			export.execute(cb);
		}
	};

	static ExportCallback cb;

	private static final String FILENAME = "ScoutingDefaultExportLocation";

	public static void exportToCSV(Context context) {
		try {
			cb = new ExportCallback();
			String filename;
			try {
				BufferedInputStream bis = new BufferedInputStream(
						context.openFileInput(FILENAME));
				byte[] buffer = new byte[bis.available()];
				bis.read(buffer, 0, buffer.length);
				filename = new String(buffer);
			} catch (Exception e) {
				filename = null;
			}

			cb.context = context;

			new FileSelector(context, FileOperation.SELECTDIR,
					mDirSelectListener, null, filename).show();

		} catch (Exception e) {
			Toast.makeText(context, "Error exporting Database",
					Toast.LENGTH_LONG).show();
		}
	}

	private static class ExportCallback {
		Context context;
		String filename;

		public void finish(String result) {
			Toast.makeText(context, result, Toast.LENGTH_LONG).show();
		}
	}

	private static class CSVExporter extends
			AsyncTask<ExportCallback, Integer, String> {

		ExportCallback callback;

		@Override
		protected String doInBackground(ExportCallback... params) {
			synchronized (ScoutingDBHelper.helper) {
				try {

					SQLiteDatabase db = ScoutingDBHelper.helper
							.getReadableDatabase();

					callback = params[0];

					String match_data = "", cycle_data = "", pit_data = "";

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

					c = db.rawQuery(
							"SELECT * FROM "
									+ FRCScoutingContract.FACT_CYCLE_DATA_Entry.TABLE_NAME,
							null);
					for (int i = 0; i < c.getColumnCount(); i++) {
						if (i > 0)
							cycle_data += ",";
						cycle_data += c.getColumnName(i);
					}
					cycle_data += "\n";
					if (c.moveToFirst())
						do {
							for (int j = 0; j < c.getColumnCount(); j++) {
								if (j > 0)
									cycle_data += ",";
								cycle_data += c.getString(j);
							}
							cycle_data += "\n";
						} while (c.moveToNext());

					c = db.rawQuery(
							"SELECT * FROM "
									+ FRCScoutingContract.SCOUT_PIT_DATA_Entry.TABLE_NAME,
							null);
					for (int i = 0; i < c.getColumnCount(); i++) {
						if (i > 0)
							pit_data += ",";
						pit_data += c.getColumnName(i);
					}
					pit_data += "\n";
					if (c.moveToFirst())
						do {
							for (int j = 0; j < c.getColumnCount(); j++) {
								if (j > 0)
									pit_data += ",";
								pit_data += c.getString(j);
							}
							pit_data += "\n";
						} while (c.moveToNext());

					File sd = new File(callback.filename);
					File match = new File(sd, "matches.csv");
					File cycle = new File(sd, "cycles.csv");
					File pits = new File(sd, "pits.csv");
					FileOutputStream destination = new FileOutputStream(match);
					destination.write(match_data.getBytes());
					destination.close();
					destination = new FileOutputStream(cycle);
					destination.write(cycle_data.getBytes());
					destination.close();
					destination = new FileOutputStream(pits);
					destination.write(pit_data.getBytes());
					destination.close();
					ScoutingDBHelper.helper.close();
					try {
						FileOutputStream fos = callback.context.openFileOutput(
								FILENAME, Context.MODE_PRIVATE);
						fos.write(callback.filename.getBytes());
						fos.close();
					} catch (Exception e) {

					}
					return "DB exported to " + sd.getAbsolutePath();
				} catch (Exception e) {
					ScoutingDBHelper.helper.close();
					return "Error during export";
				}
			}
		}

		protected void onPostExecute(String result) {
			callback.finish(result);
		}

	}

}
