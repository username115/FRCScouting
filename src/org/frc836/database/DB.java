/*
 * Copyright 2015 Daniel Logan
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.frc836.database.DBSyncService.LocalBinder;
import org.frc836.database.FRCScoutingContract.CONFIGURATION_LU_Entry;
import org.frc836.database.FRCScoutingContract.EVENT_LU_Entry;
import org.frc836.database.FRCScoutingContract.NOTES_OPTIONS_Entry;
import org.frc836.database.FRCScoutingContract.ROBOT_LU_Entry;
import org.frc836.database.FRCScoutingContract.WHEEL_BASE_LU_Entry;
import org.frc836.database.FRCScoutingContract.WHEEL_TYPE_LU_Entry;
import org.frc836.samsung.fileselector.FileOperation;
import org.frc836.samsung.fileselector.FileSelector;
import org.frc836.samsung.fileselector.OnHandleFileListener;
import org.growingstems.scouting.Prefs;
import org.sigmond.net.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.widget.Toast;

public class DB {

	public static final boolean debug = false;
	// kinda dangerous, but we are assuming that the timestamp field will always
	// have the same name for all tables
	public static final String COLUMN_NAME_TIMESTAMP = PitStats.COLUMN_NAME_TIMESTAMP;

	private HttpUtils utils;
	private String password;
	private Context context;
	private LocalBinder binder;

	public static final SimpleDateFormat dateParser = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.sss", Locale.US);

	@SuppressWarnings("unused")
	private DB() {
	}

	public DB(Context context, String pass, LocalBinder binder) {
		utils = new HttpUtils();
		password = pass;
		this.context = context;
		ScoutingDBHelper.getInstance(context.getApplicationContext());
		this.binder = binder;
	}

	public DB(Context context, LocalBinder binder) {
		this.context = context;
		utils = new HttpUtils();
		password = Prefs.getSavedPassword(context);
		ScoutingDBHelper.getInstance(context.getApplicationContext());
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

	private void insertOrUpdate(String table, String nullColumnHack,
			ContentValues values, String idColumnName, String whereClause,
			String[] whereArgs) {
		synchronized (ScoutingDBHelper.lock) {
			SQLiteDatabase db = ScoutingDBHelper.getInstance()
					.getWritableDatabase();

			String[] projection = { idColumnName };

			Cursor c = db.query(table, projection, whereClause, whereArgs,
					null, null, null, "0,1");
			try {
				if (c.moveToFirst()) {
					String[] id = { c.getString(c
							.getColumnIndexOrThrow(idColumnName)) };
					values.put(COLUMN_NAME_TIMESTAMP,
							dateParser.format(new Date()));
					db.update(table, values, idColumnName + "=?", id);
				} else {
					db.insert(table, nullColumnHack, values);
				}
			} finally {
				if (c != null)
					c.close();
				ScoutingDBHelper.getInstance().close();
			}
		}
	}

	public boolean submitMatch(MatchStatsStruct teamData) {
		try {
			String where = MatchStatsStruct.COLUMN_NAME_EVENT_ID + "=? AND "
					+ MatchStatsStruct.COLUMN_NAME_MATCH_ID + "=? AND "
					+ MatchStatsStruct.COLUMN_NAME_TEAM_ID + "=? AND "
					+ MatchStatsStruct.COLUMN_NAME_PRACTICE_MATCH + "=?";
			ContentValues values;
			synchronized (ScoutingDBHelper.lock) {
				SQLiteDatabase db = ScoutingDBHelper.getInstance()
						.getReadableDatabase();

				values = teamData.getValues(this, db);

				ScoutingDBHelper.getInstance().close();
			}
			String[] whereArgs = {
					values.getAsString(MatchStatsStruct.COLUMN_NAME_EVENT_ID),
					values.getAsString(MatchStatsStruct.COLUMN_NAME_MATCH_ID),
					values.getAsString(MatchStatsStruct.COLUMN_NAME_TEAM_ID),
					values.getAsString(MatchStatsStruct.COLUMN_NAME_PRACTICE_MATCH) };

			insertOrUpdate(MatchStatsStruct.TABLE_NAME, null, values,
					MatchStatsStruct.COLUMN_NAME_ID, where, whereArgs);

			startSync();

			return true;

		} catch (Exception e) {
			return false;
		}

	}

	public boolean submitPits(PitStats stats) {
		try {
			ContentValues values;
			synchronized (ScoutingDBHelper.lock) {
				SQLiteDatabase db = ScoutingDBHelper.getInstance()
						.getWritableDatabase();
				values = stats.getValues(this, db);
				ScoutingDBHelper.getInstance().close();
			}

			String[] where = { values.getAsString(PitStats.COLUMN_NAME_TEAM_ID) };

			insertOrUpdate(PitStats.TABLE_NAME, null, values,
					PitStats.COLUMN_NAME_ID, PitStats.COLUMN_NAME_TEAM_ID
							+ "=?", where);

			startSync();
			return true;
		} catch (Exception e) {
			return false;
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

		synchronized (ScoutingDBHelper.lock) {
			try {

				SQLiteDatabase db = ScoutingDBHelper.getInstance()
						.getReadableDatabase();
				String date = "";

				String[] projection = { PitStats.COLUMN_NAME_TIMESTAMP };
				String[] where = { teamNum };
				Cursor c = db.query(PitStats.TABLE_NAME, // from the
															// scout_pit_data
						// table
						projection, // select
						PitStats.COLUMN_NAME_TEAM_ID + "=?", // where
																// team_id
																// ==
						where, // teamNum
						null, // don't group
						null, // don't filter
						null, // don't order
						"0,1"); // limit to 1

				try {
					c.moveToFirst();

					date = c.getString(c
							.getColumnIndexOrThrow(PitStats.COLUMN_NAME_TIMESTAMP));
				} finally {
					if (c != null)
						c.close();
					ScoutingDBHelper.getInstance().close();
				}
				return date;

			} catch (Exception e) {
				return "";
			}
		}
	}

	public List<String> getEventList() {

		synchronized (ScoutingDBHelper.lock) {
			try {
				SQLiteDatabase db = ScoutingDBHelper.getInstance()
						.getReadableDatabase();

				String[] projection = { EVENT_LU_Entry.COLUMN_NAME_EVENT_NAME };

				Cursor c = db.query(EVENT_LU_Entry.TABLE_NAME, projection,
						null, null, null, null, EVENT_LU_Entry.COLUMN_NAME_ID);
				List<String> ret;
				try {

					ret = new ArrayList<String>(c.getCount());

					if (c.moveToFirst())
						do {
							ret.add(c.getString(c
									.getColumnIndexOrThrow(EVENT_LU_Entry.COLUMN_NAME_EVENT_NAME)));
						} while (c.moveToNext());
					else
						ret = null;
				} finally {
					if (c != null)
						c.close();
					ScoutingDBHelper.getInstance().close();
				}

				return ret;
			} catch (Exception e) {
				return null;
			}
		}
	}

	public List<String> getConfigList() {

		synchronized (ScoutingDBHelper.lock) {
			try {
				SQLiteDatabase db = ScoutingDBHelper.getInstance()
						.getReadableDatabase();

				String[] projection = { CONFIGURATION_LU_Entry.COLUMN_NAME_CONFIGURATION_DESC };

				Cursor c = db.query(EVENT_LU_Entry.TABLE_NAME, projection,
						null, null, null, null,
						CONFIGURATION_LU_Entry.COLUMN_NAME_ID);
				List<String> ret;
				try {

					ret = new ArrayList<String>(c.getCount());

					if (c.moveToFirst())
						do {
							ret.add(c.getString(c
									.getColumnIndexOrThrow(CONFIGURATION_LU_Entry.COLUMN_NAME_CONFIGURATION_DESC)));
						} while (c.moveToNext());
					else
						ret = null;
				} finally {
					if (c != null)
						c.close();
					ScoutingDBHelper.getInstance().close();
				}

				return ret;
			} catch (Exception e) {
				return null;
			}
		}
	}

	public List<String> getWheelBaseList() {

		synchronized (ScoutingDBHelper.lock) {
			try {
				SQLiteDatabase db = ScoutingDBHelper.getInstance()
						.getReadableDatabase();

				String[] projection = { WHEEL_BASE_LU_Entry.COLUMN_NAME_WHEEL_BASE_DESC };

				Cursor c = db.query(EVENT_LU_Entry.TABLE_NAME, projection,
						null, null, null, null,
						WHEEL_BASE_LU_Entry.COLUMN_NAME_ID);
				List<String> ret;

				try {

					ret = new ArrayList<String>(c.getCount());

					if (c.moveToFirst())
						do {
							ret.add(c.getString(c
									.getColumnIndexOrThrow(WHEEL_BASE_LU_Entry.COLUMN_NAME_WHEEL_BASE_DESC)));
						} while (c.moveToNext());
					else
						ret = null;
				} finally {
					if (c != null)
						c.close();
					ScoutingDBHelper.getInstance().close();
				}

				return ret;
			} catch (Exception e) {
				return null;
			}
		}
	}

	public List<String> getWheelTypeList() {

		synchronized (ScoutingDBHelper.lock) {
			try {
				SQLiteDatabase db = ScoutingDBHelper.getInstance()
						.getReadableDatabase();

				String[] projection = { WHEEL_TYPE_LU_Entry.COLUMN_NAME_WHEEL_TYPE_DESC };

				Cursor c = db.query(EVENT_LU_Entry.TABLE_NAME, projection,
						null, null, null, null,
						WHEEL_TYPE_LU_Entry.COLUMN_NAME_ID);
				List<String> ret;
				try {

					ret = new ArrayList<String>(c.getCount());

					if (c.moveToFirst())
						do {
							ret.add(c.getString(c
									.getColumnIndexOrThrow(WHEEL_TYPE_LU_Entry.COLUMN_NAME_WHEEL_TYPE_DESC)));
						} while (c.moveToNext());
					else
						ret = null;
				} finally {
					if (c != null)
						c.close();
					ScoutingDBHelper.getInstance().close();
				}

				return ret;
			} catch (Exception e) {
				return null;
			}
		}
	}

	public String getCodeFromEventName(String eventName) {
		synchronized (ScoutingDBHelper.lock) {
			try {
				SQLiteDatabase db = ScoutingDBHelper.getInstance()
						.getReadableDatabase();
				String[] projection = { EVENT_LU_Entry.COLUMN_NAME_EVENT_CODE };
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
				String ret = "";
				try {
					c.moveToFirst();
					ret = c.getString(c
							.getColumnIndexOrThrow(EVENT_LU_Entry.COLUMN_NAME_EVENT_CODE));
				} finally {
					if (c != null)
						c.close();
					ScoutingDBHelper.getInstance().close();
				}
				return ret;
			} catch (Exception e) {
				return null;
			}
		}
	}

	public List<String> getNotesOptions() {
		synchronized (ScoutingDBHelper.lock) {
			try {
				SQLiteDatabase db = ScoutingDBHelper.getInstance()
						.getReadableDatabase();

				String[] projection = { NOTES_OPTIONS_Entry.COLUMN_NAME_OPTION_TEXT };

				Cursor c = db.query(NOTES_OPTIONS_Entry.TABLE_NAME, projection,
						null, null, null, null,
						NOTES_OPTIONS_Entry.COLUMN_NAME_ID);
				List<String> ret;
				try {

					ret = new ArrayList<String>(c.getCount());

					if (c.moveToFirst())
						do {
							ret.add(c.getString(c
									.getColumnIndexOrThrow(NOTES_OPTIONS_Entry.COLUMN_NAME_OPTION_TEXT)));
						} while (c.moveToNext());
					else
						ret = null;
				} finally {
					if (c != null)
						c.close();
					ScoutingDBHelper.getInstance().close();
				}

				return ret;
			} catch (Exception e) {
				return null;
			}
		}
	}

	/*
	 * public void getEventStats(String eventName, EventStats.EventCallback
	 * callback) { // TODO data lookup /* Map<String, String> args = new
	 * HashMap<String, String>(); args.put("type", "eventStats");
	 * args.put("password", password); args.put("event_name", eventName);
	 * utils.doPost(Prefs.getScoutingURL(context), args, new EventStats(
	 * callback));
	 * 
	 * }
	 */

	/*
	 * public void getTeamStats(int teamId, TeamStats.TeamCallback callback) {
	 * // TODO data lookup /* Map<String, String> args = new HashMap<String,
	 * String>(); args.put("type", "teamStats"); args.put("password", password);
	 * args.put("team_id", String.valueOf(teamId)); utils.doPost(
	 * Prefs.getScoutingURL(context), args, new TeamStats(callback, teamId,
	 * Prefs.getEvent(context, "Chesapeake Regional")));
	 * 
	 * }
	 */

	/*
	 * public void getMatchStats(String event, String match,
	 * MatchStats.MatchCallback callback) { // TODO data lookup /* Map<String,
	 * String> args = new HashMap<String, String>(); args.put("event_name",
	 * event); args.put("match_id", match); args.put("password", password);
	 * args.put("type", "matchStats");
	 * utils.doPost(Prefs.getScoutingURL(context), args, new MatchStats(
	 * callback));
	 * 
	 * }
	 */

	public String getPictureURL(int teamNum) {
		synchronized (ScoutingDBHelper.lock) {
			String ret = "";
			try {

				SQLiteDatabase db = ScoutingDBHelper.getInstance()
						.getReadableDatabase();

				String[] projection = { ROBOT_LU_Entry.COLUMN_NAME_ROBOT_PHOTO };
				String[] where = { String.valueOf(teamNum) };
				Cursor c = db.query(ROBOT_LU_Entry.TABLE_NAME, // from the
																// robot_lu
																// table
						projection, // select
						ROBOT_LU_Entry.COLUMN_NAME_TEAM_ID + "=?", // where
																	// team_id
																	// ==
						where, // teamNum
						null, // don't group
						null, // don't filter
						null, // don't order
						"0,1"); // limit to 1
				try {

					if (c.moveToFirst()) {
						ret = c.getString(c
								.getColumnIndexOrThrow(ROBOT_LU_Entry.COLUMN_NAME_ROBOT_PHOTO));
					}

				} finally {
					if (c != null)
						c.close();
					ScoutingDBHelper.getInstance().close();
				}

				return ret;
			} catch (Exception e) {
				return null;
			}

		}
	}

	public PitStats getTeamPitStats(int teamNum) {

		synchronized (ScoutingDBHelper.lock) {

			try {
				PitStats stats = PitStats.getNewPitStats();

				SQLiteDatabase db = ScoutingDBHelper.getInstance()
						.getReadableDatabase();

				String[] projection = stats.getProjection();
				String[] where = { String.valueOf(teamNum) };
				Cursor c = db.query(PitStats.TABLE_NAME, // from the
															// scout_pit_data
															// table
						projection, // select
						PitStats.COLUMN_NAME_TEAM_ID + "=?", // where team_id ==
						where, // teamNum
						null, // don't group
						null, // don't filter
						null, // don't order
						"0,1"); // limit to 1
				try {

					stats.fromCursor(c, this, db);

				} finally {
					if (c != null)
						c.close();
					ScoutingDBHelper.getInstance().close();
				}

				return stats;
			} catch (Exception e) {
				return null;
			}

		}
	}

	public MatchStatsStruct getMatchStats(String eventName, int match,
			int team, boolean practice) {
		synchronized (ScoutingDBHelper.lock) {

			try {
				MatchStatsStruct stats = MatchStatsStruct.getNewMatchStats();

				SQLiteDatabase db = ScoutingDBHelper.getInstance()
						.getReadableDatabase();

				String[] projection = stats.getProjection();
				String[] where = { String.valueOf(match),
						String.valueOf(getEventIDFromName(eventName, db)),
						String.valueOf(team), practice ? "1" : "0" };

				Cursor c = db.query(MatchStatsStruct.TABLE_NAME, projection,
						MatchStatsStruct.COLUMN_NAME_MATCH_ID + "=? AND "
								+ MatchStatsStruct.COLUMN_NAME_EVENT_ID
								+ "=? AND "
								+ MatchStatsStruct.COLUMN_NAME_TEAM_ID
								+ "=? AND "
								+ MatchStatsStruct.COLUMN_NAME_PRACTICE_MATCH
								+ "=?", where, null, null, null, "0,1");

				stats.fromCursor(c, this, db);
				if (c != null)
					c.close();
				ScoutingDBHelper.getInstance().close();

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
		long ret = -1;
		try {
			c.moveToFirst();
			ret = c.getLong(c
					.getColumnIndexOrThrow(EVENT_LU_Entry.COLUMN_NAME_ID));
		} finally {
			if (c != null)
				c.close();
		}

		return ret;
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
		long ret = -1;
		try {
			c.moveToFirst();
			ret = c.getLong(c
					.getColumnIndexOrThrow(CONFIGURATION_LU_Entry.COLUMN_NAME_ID));
		} finally {
			if (c != null)
				c.close();
		}
		return ret;
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
		long ret = -1;
		try {
			c.moveToFirst();
			ret = c.getLong(c
					.getColumnIndexOrThrow(WHEEL_BASE_LU_Entry.COLUMN_NAME_ID));
		} finally {
			if (c != null)
				c.close();
		}
		return ret;
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
		long ret = -1;
		try {
			c.moveToFirst();
			ret = c.getLong(c
					.getColumnIndexOrThrow(WHEEL_TYPE_LU_Entry.COLUMN_NAME_ID));
		} finally {
			if (c != null)
				c.close();
		}
		return ret;
	}

	public static String getConfigNameFromID(int config, SQLiteDatabase db) {

		String[] projection = { CONFIGURATION_LU_Entry.COLUMN_NAME_CONFIGURATION_DESC };
		String[] where = { String.valueOf(config) };
		Cursor c = db.query(CONFIGURATION_LU_Entry.TABLE_NAME, projection, // select
				CONFIGURATION_LU_Entry.COLUMN_NAME_ID + "= ?", where, // EventName
				null, // don't group
				null, // don't filter
				null, // don't order
				"0,1"); // limit to 1
		String ret = "";
		try {
			c.moveToFirst();
			ret = c.getString(c
					.getColumnIndexOrThrow(CONFIGURATION_LU_Entry.COLUMN_NAME_CONFIGURATION_DESC));
		} finally {
			if (c != null)
				c.close();
		}
		return ret;
	}

	public static String getWheelBaseNameFromID(int base, SQLiteDatabase db) {

		String[] projection = { WHEEL_BASE_LU_Entry.COLUMN_NAME_WHEEL_BASE_DESC };
		String[] where = { String.valueOf(base) };
		Cursor c = db.query(WHEEL_BASE_LU_Entry.TABLE_NAME, projection, // select
				WHEEL_BASE_LU_Entry.COLUMN_NAME_ID + "= ?", where, // EventName
				null, // don't group
				null, // don't filter
				null, // don't order
				"0,1"); // limit to 1
		String ret = "";
		try {
			c.moveToFirst();
			ret = c.getString(c
					.getColumnIndexOrThrow(WHEEL_BASE_LU_Entry.COLUMN_NAME_WHEEL_BASE_DESC));
		} finally {
			if (c != null)
				c.close();
		}
		return ret;
	}

	public static String getWheelTypeNameFromID(int type, SQLiteDatabase db) {

		String[] projection = { WHEEL_TYPE_LU_Entry.COLUMN_NAME_WHEEL_TYPE_DESC };
		String[] where = { String.valueOf(type) };
		Cursor c = db.query(WHEEL_TYPE_LU_Entry.TABLE_NAME, projection, // select
				WHEEL_TYPE_LU_Entry.COLUMN_NAME_ID + " LIKE ?", where, // EventName
				null, // don't group
				null, // don't filter
				null, // don't order
				"0,1"); // limit to 1
		String ret = "";
		try {
			c.moveToFirst();
			ret = c.getString(c
					.getColumnIndexOrThrow(WHEEL_TYPE_LU_Entry.COLUMN_NAME_WHEEL_TYPE_DESC));
		} finally {
			if (c != null)
				c.close();
		}
		return ret;
	}

	public static String getEventNameFromID(int eventId, SQLiteDatabase db) {

		String[] projection = { EVENT_LU_Entry.COLUMN_NAME_EVENT_NAME };
		String[] where = { String.valueOf(eventId) };
		Cursor c = db.query(EVENT_LU_Entry.TABLE_NAME, projection, // select
				EVENT_LU_Entry.COLUMN_NAME_ID + "= ?", where, // EventName
				null, // don't group
				null, // don't filter
				null, // don't order
				"0,1"); // limit to 1
		String ret = "";
		try {
			c.moveToFirst();
			ret = c.getString(c
					.getColumnIndexOrThrow(EVENT_LU_Entry.COLUMN_NAME_EVENT_NAME));
		} finally {
			if (c != null)
				c.close();
		}
		return ret;
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
			synchronized (ScoutingDBHelper.lock) {
				try {

					SQLiteDatabase db = ScoutingDBHelper.getInstance()
							.getReadableDatabase();

					callback = params[0];

					SparseArray<String> configs = new SparseArray<String>();
					SparseArray<String> types = new SparseArray<String>();
					SparseArray<String> bases = new SparseArray<String>();

					Cursor c = null;
					StringBuilder match_data = null, pit_data = null;
					// export matches
					try {

						c = db.rawQuery("SELECT * FROM "
								+ MatchStatsStruct.TABLE_NAME, null);
						// decent estimate for how big the output will be. will
						// definitely be too small, but will keep it from having
						// to resize too many times
						match_data = new StringBuilder(c.getCount()
								* c.getColumnCount() * 2);

						for (int i = 0; i < c.getColumnCount(); i++) {
							if (i > 0)
								match_data.append(",");
							match_data.append(c.getColumnName(i));
						}
						match_data.append("\n");
						if (c.moveToFirst())
							do {
								for (int j = 0; j < c.getColumnCount(); j++) {
									if (j > 0)
										match_data.append(",");
									if (MatchStatsStruct.COLUMN_NAME_INVALID
											.equalsIgnoreCase(c
													.getColumnName(j))
											&& !debug)
										match_data.append("0");
									else if (MatchStatsStruct
											.getNewMatchStats().isTextField(
													c.getColumnName(j)))
										match_data.append("\"")
												.append(c.getString(j))
												.append("\"");
									else
										match_data.append(c.getString(j));
								}
								match_data.append("\n");
							} while (c.moveToNext());
					} finally {
						if (c != null)
							c.close();
						ScoutingDBHelper.getInstance().close();
					}

					// export pits
					db = ScoutingDBHelper.getInstance().getReadableDatabase();
					try {
						c = db.rawQuery("SELECT * FROM " + PitStats.TABLE_NAME,
								null);
						pit_data = new StringBuilder(c.getCount()
								* c.getColumnCount() * 2);
						for (int i = 0; i < c.getColumnCount(); i++) {
							if (i > 0)
								pit_data.append(",");
							pit_data.append(c.getColumnName(i));
						}
						pit_data.append("\n");
						if (c.moveToFirst()) {
							do {
								for (int j = 0; j < c.getColumnCount(); j++) {
									if (j > 0)
										pit_data.append(",");
									if (PitStats.COLUMN_NAME_INVALID
											.equalsIgnoreCase(c
													.getColumnName(j))
											&& !debug)
										pit_data.append("0");
									else if (PitStats.getNewPitStats()
											.isTextField(c.getColumnName(j)))
										pit_data.append("\"")
												.append(c.getString(j))
												.append("\"");
									// wanted to encapsulate the following, but
									// doing so would slow down the export.
									else if (PitStats.COLUMN_NAME_CONFIG_ID
											.equalsIgnoreCase(c
													.getColumnName(j))) {
										String config = configs
												.get(c.getInt(j));
										if (config == null) {
											config = getConfigNameFromID(
													c.getInt(j), db);
											configs.append(c.getInt(j), config);
										}
										pit_data.append(config);
									} else if (PitStats.COLUMN_NAME_WHEEL_BASE_ID
											.equalsIgnoreCase(c
													.getColumnName(j))) {
										String base = bases.get(c.getInt(j));
										if (base == null) {
											base = getWheelBaseNameFromID(
													c.getInt(j), db);
											bases.append(c.getInt(j), base);
										}
										pit_data.append(base);
									} else if (PitStats.COLUMN_NAME_WHEEL_TYPE_ID
											.equalsIgnoreCase(c
													.getColumnName(j))) {
										String type = types.get(c.getInt(j));
										if (type == null) {
											type = getWheelTypeNameFromID(
													c.getInt(j), db);
											types.append(c.getInt(j), type);
										}
										pit_data.append(type);
									} else
										pit_data.append(c.getString(j));
								}
								pit_data.append("\n");
							} while (c.moveToNext());
						}
					} finally {
						if (c != null)
							c.close();
						ScoutingDBHelper.getInstance().close();
					}

					File sd = new File(callback.filename);
					File match = new File(sd, "matches.csv");
					File pits = new File(sd, "pits.csv");
					FileOutputStream destination;
					if (match_data != null) {
						destination = new FileOutputStream(match);
						destination.write(match_data.toString().getBytes());
						destination.close();
					}
					if (pit_data != null) {
						destination = new FileOutputStream(pits);
						destination.write(pit_data.toString().getBytes());
						destination.close();
					}
					try {
						FileOutputStream fos = callback.context.openFileOutput(
								FILENAME, Context.MODE_PRIVATE);
						fos.write(callback.filename.getBytes());
						fos.close();
					} catch (Exception e) {

					}
					return "DB exported to " + sd.getAbsolutePath();
				} catch (Exception e) {
					ScoutingDBHelper.getInstance().close();
					return "Error during export";
				}
			}
		}

		protected void onPostExecute(String result) {
			callback.finish(result);
		}

	}

}
