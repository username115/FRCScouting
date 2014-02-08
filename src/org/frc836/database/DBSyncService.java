package org.frc836.database;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.frc836.database.FRCScoutingContract.*;
import org.growingstems.scouting.Prefs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sigmond.net.HttpCallback;
import org.sigmond.net.HttpRequestInfo;
import org.sigmond.net.HttpUtils;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

public class DBSyncService extends Service {

	private static final String FILENAME = "DBtimestamp";

	private IBinder mBinder = new LocalBinder();

	private HttpUtils utils = new HttpUtils();

	private ScoutingDBHelper helper = null;

	private String password = null;

	private Handler mTimerTask;
	private SyncDataTask dataTask;

	private Timestamp lastSync;

	private enum Actions {
		NOTHING, INSERT, UPDATE, DELETE
	};

	private static final int DELAY = 60000;

	private static final SimpleDateFormat dateParser = new SimpleDateFormat(
			"yyyy-MM-ddTHH:mm:ss.sss", Locale.US);

	public void onCreate() {
		loadTimestamp();

		mTimerTask = new Handler();

		dataTask = new SyncDataTask();

		password = Prefs.getSavedPassword(getApplicationContext());

		initialSync();

	}

	private void loadTimestamp() {
		try {
			BufferedInputStream bis = new BufferedInputStream(
					openFileInput(FILENAME));
			byte[] buffer = new byte[bis.available()];
			bis.read(buffer, 0, buffer.length);
			lastSync = new Timestamp(Long.valueOf(new String(buffer)));
		} catch (Exception e) {
			if (lastSync == null)
				lastSync = new Timestamp(0);
		}
	}

	private void updateTimeStamp(long time) {
		lastSync = new Timestamp(time);
		saveTimestamp();
	}

	private void updateTimeStamp(Timestamp time) {
		lastSync = new Timestamp(time.getTime());
		saveTimestamp();
	}

	private void updateTimestamp() {
		lastSync = new Timestamp(new Date().getTime());
		saveTimestamp();
	}

	private void saveTimestamp() {
		try {
			FileOutputStream fos = openFileOutput(FILENAME,
					Context.MODE_PRIVATE);
			fos.write(String.valueOf(lastSync.getTime()).getBytes());
			fos.close();
		} catch (Exception e) {
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	public class LocalBinder extends Binder {
		DBSyncService getService() {
			return DBSyncService.this;
		}

		void setPassword(String pass) {
			password = pass;
		}

		void setDatabase(ScoutingDBHelper database) {
			helper = database;
		}

		void startSync() {
			mTimerTask.removeCallbacks(dataTask);
			mTimerTask.post(dataTask);
		}
	}

	// start an initial sync
	private void initialSync() {
		Map<String, String> args = new HashMap<String, String>();

		args.put("password", password);
		args.put("type", "fullsync");

		utils.doPost(Prefs.getScoutingURLNoDefault(getApplicationContext()),
				args, new SyncCallback());
	}

	// task to process a sync response
	private class ProcessData extends AsyncTask<String, Integer, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			try {
				SQLiteDatabase db = helper.getWritableDatabase();

				// TODO send updates to database

				JSONObject json = new JSONObject(params[0]);

				processConfig(
						json.getJSONArray(CONFIGURATION_LU_Entry.TABLE_NAME),
						db);

				processEvents(json.getJSONArray(EVENT_LU_Entry.TABLE_NAME), db);

				processCycles(
						json.getJSONArray(FACT_CYCLE_DATA_Entry.TABLE_NAME), db);

				processMatches(
						json.getJSONArray(FACT_MATCH_DATA_Entry.TABLE_NAME), db);

				processNotes(json.getJSONArray(NOTES_OPTIONS_Entry.TABLE_NAME),
						db);

				processRobots(json.getJSONArray(ROBOT_LU_Entry.TABLE_NAME), db);

				processPits(json.getJSONArray(SCOUT_PIT_DATA_Entry.TABLE_NAME),
						db);

				processWheelBase(
						json.getJSONArray(WHEEL_BASE_LU_Entry.TABLE_NAME), db);

				processWheelType(
						json.getJSONArray(WHEEL_TYPE_LU_Entry.TABLE_NAME), db);

				updateTimeStamp(json.getLong("timestamp"));

			} catch (JSONException e) {
				// TODO
			}

			return null;
		}
	}

	// callback for initial sync request. Expects JSON response
	private class SyncCallback implements HttpCallback {

		public void onResponse(HttpRequestInfo resp) {
			// TODO Auto-generated method stub
			ProcessData dataProc = new ProcessData();
			dataProc.execute(resp.getResponseString());
		}

		public void onError(Exception e) {
			// TODO Auto-generated method stub

			mTimerTask.postDelayed(dataTask, DELAY);
		}
	}

	// callback for updates sent to server
	private class ChangeResponseCallback implements HttpCallback {

		public void onResponse(HttpRequestInfo resp) {
			// TODO Auto-generated method stub

			mTimerTask.postDelayed(dataTask, DELAY);
		}

		public void onError(Exception e) {
			// TODO Auto-generated method stub

		}

	}

	// processes responses from server for updates sent
	private class ChangeResponseProcess extends
			AsyncTask<String, Integer, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			// TODO

			return null;
		}

		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

		}

	}

	// task to perform periodic sync
	private class SyncDataTask implements Runnable {

		public void run() {
			Map<String, String> args = new HashMap<String, String>();

			args.put("password", password);
			args.put("type", "sync");
			args.put("timestamp", String.valueOf(lastSync.getTime()));

			utils.doPost(
					Prefs.getScoutingURLNoDefault(getApplicationContext()),
					args, new SyncCallback());
		}
	}

	private void processConfig(JSONArray config, SQLiteDatabase db) {
		try {
			for (int i = 0; i < config.length(); i++) {
				JSONObject row = config.getJSONObject(i);
				Actions action = Actions.UPDATE;
				if (row.getInt(CONFIGURATION_LU_Entry.COLUMN_NAME_INVALID) != 0) {
					action = Actions.DELETE;
				}
				ContentValues vals = new ContentValues();
				vals.put(CONFIGURATION_LU_Entry.COLUMN_NAME_ID,
						row.getInt(CONFIGURATION_LU_Entry.COLUMN_NAME_ID));
				vals.put(
						CONFIGURATION_LU_Entry.COLUMN_NAME_CONFIGURATION_DESC,
						row.getString(CONFIGURATION_LU_Entry.COLUMN_NAME_CONFIGURATION_DESC));
				vals.put(
						CONFIGURATION_LU_Entry.COLUMN_NAME_TIMESTAMP,
						dateParser.format(new Date(
								row.getLong(CONFIGURATION_LU_Entry.COLUMN_NAME_TIMESTAMP))));

				// check if this entry exists already
				String[] projection = { CONFIGURATION_LU_Entry.COLUMN_NAME_CONFIGURATION_DESC };
				String[] where = { vals
						.getAsString(CONFIGURATION_LU_Entry.COLUMN_NAME_ID) };
				Cursor c = db.query(
						CONFIGURATION_LU_Entry.TABLE_NAME,
						projection, // select
						CONFIGURATION_LU_Entry.COLUMN_NAME_ID + "=?", where,
						null, // don't
						// group
						null, // don't filter
						null, // don't order
						"0,1"); // limit to 1
				if (!c.moveToFirst()) {
					if (action == Actions.UPDATE)
						action = Actions.INSERT;
					else if (action == Actions.DELETE)
						action = Actions.NOTHING;
				}

				switch (action) {
				case UPDATE:
					db.update(CONFIGURATION_LU_Entry.TABLE_NAME, vals,
							CONFIGURATION_LU_Entry.COLUMN_NAME_ID + " = ?",
							where);
					break;
				case INSERT:
					db.insert(CONFIGURATION_LU_Entry.TABLE_NAME, null, vals);
					break;
				case DELETE:
					db.delete(CONFIGURATION_LU_Entry.TABLE_NAME,
							CONFIGURATION_LU_Entry.COLUMN_NAME_ID + " = ?",
							where);
					break;
				default:
				}

			}
		} catch (JSONException e) {
			// TODO handle error
		}
	}

	private void processEvents(JSONArray events, SQLiteDatabase db) {
		try {
			for (int i = 0; i < events.length(); i++) {
				JSONObject row = events.getJSONObject(i);
				Actions action = Actions.UPDATE;
				if (row.getInt(EVENT_LU_Entry.COLUMN_NAME_INVALID) != 0) {
					action = Actions.DELETE;
				}
				ContentValues vals = new ContentValues();
				vals.put(EVENT_LU_Entry.COLUMN_NAME_ID,
						row.getInt(EVENT_LU_Entry.COLUMN_NAME_ID));
				vals.put(EVENT_LU_Entry.COLUMN_NAME_EVENT_NAME,
						row.getString(EVENT_LU_Entry.COLUMN_NAME_EVENT_NAME));
				vals.put(EVENT_LU_Entry.COLUMN_NAME_MATCH_URL,
						row.getString(EVENT_LU_Entry.COLUMN_NAME_MATCH_URL));
				vals.put(
						EVENT_LU_Entry.COLUMN_NAME_TIMESTAMP,
						dateParser.format(new Date(row
								.getLong(EVENT_LU_Entry.COLUMN_NAME_TIMESTAMP))));

				// check if this entry exists already
				String[] projection = { EVENT_LU_Entry.COLUMN_NAME_EVENT_NAME };
				String[] where = { vals
						.getAsString(EVENT_LU_Entry.COLUMN_NAME_ID) };
				Cursor c = db.query(EVENT_LU_Entry.TABLE_NAME, projection, // select
						EVENT_LU_Entry.COLUMN_NAME_ID + "=?", where, null, // don't
						// group
						null, // don't filter
						null, // don't order
						"0,1"); // limit to 1
				if (!c.moveToFirst()) {
					if (action == Actions.UPDATE)
						action = Actions.INSERT;
					else if (action == Actions.DELETE)
						action = Actions.NOTHING;
				}

				switch (action) {
				case UPDATE:
					db.update(EVENT_LU_Entry.TABLE_NAME, vals,
							EVENT_LU_Entry.COLUMN_NAME_ID + " = ?", where);
					break;
				case INSERT:
					db.insert(EVENT_LU_Entry.TABLE_NAME, null, vals);
					break;
				case DELETE:
					db.delete(EVENT_LU_Entry.TABLE_NAME,
							EVENT_LU_Entry.COLUMN_NAME_ID + " = ?", where);
					break;
				default:
				}

			}
		} catch (JSONException e) {
			// TODO handle error
		}
	}

	private void processCycles(JSONArray cycles, SQLiteDatabase db) {
		try {
			for (int i = 0; i < cycles.length(); i++) {
				JSONObject row = cycles.getJSONObject(i);
				Actions action = Actions.UPDATE;
				if (row.getInt(FACT_CYCLE_DATA_Entry.COLUMN_NAME_INVALID) != 0) {
					action = Actions.DELETE;
				}
				ContentValues vals = new ContentValues();
				vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_ID,
						row.getInt(FACT_CYCLE_DATA_Entry.COLUMN_NAME_ID));
				vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_EVENT_ID,
						row.getInt(FACT_CYCLE_DATA_Entry.COLUMN_NAME_EVENT_ID));
				vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_MATCH_ID,
						row.getInt(FACT_CYCLE_DATA_Entry.COLUMN_NAME_MATCH_ID));
				vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_TEAM_ID,
						row.getInt(FACT_CYCLE_DATA_Entry.COLUMN_NAME_TEAM_ID));
				vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_CYCLE_NUM,
						row.getInt(FACT_CYCLE_DATA_Entry.COLUMN_NAME_CYCLE_NUM));
				vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_NEAR_POSS,
						row.getInt(FACT_CYCLE_DATA_Entry.COLUMN_NAME_NEAR_POSS));
				vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_WHITE_POSS, row
						.getInt(FACT_CYCLE_DATA_Entry.COLUMN_NAME_WHITE_POSS));
				vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_FAR_POSS,
						row.getInt(FACT_CYCLE_DATA_Entry.COLUMN_NAME_FAR_POSS));
				vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_TRUSS,
						row.getInt(FACT_CYCLE_DATA_Entry.COLUMN_NAME_TRUSS));
				vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_CATCH,
						row.getInt(FACT_CYCLE_DATA_Entry.COLUMN_NAME_CATCH));
				vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_HIGH,
						row.getInt(FACT_CYCLE_DATA_Entry.COLUMN_NAME_HIGH));
				vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_LOW,
						row.getInt(FACT_CYCLE_DATA_Entry.COLUMN_NAME_LOW));
				vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_ASSISTS,
						row.getInt(FACT_CYCLE_DATA_Entry.COLUMN_NAME_ASSISTS));
				vals.put(
						FACT_CYCLE_DATA_Entry.COLUMN_NAME_TIMESTAMP,
						dateParser.format(new Date(
								row.getLong(FACT_CYCLE_DATA_Entry.COLUMN_NAME_TIMESTAMP))));

				// check if this entry exists already
				String[] projection = { FACT_CYCLE_DATA_Entry.COLUMN_NAME_ID };
				String[] where = {
						vals.getAsString(FACT_CYCLE_DATA_Entry.COLUMN_NAME_EVENT_ID),
						vals.getAsString(FACT_CYCLE_DATA_Entry.COLUMN_NAME_MATCH_ID),
						vals.getAsString(FACT_CYCLE_DATA_Entry.COLUMN_NAME_TEAM_ID),
						vals.getAsString(FACT_CYCLE_DATA_Entry.COLUMN_NAME_CYCLE_NUM) };
				Cursor c = db.query(FACT_CYCLE_DATA_Entry.TABLE_NAME,
						projection, // select
						FACT_CYCLE_DATA_Entry.COLUMN_NAME_EVENT_ID + "=? AND "
								+ FACT_CYCLE_DATA_Entry.COLUMN_NAME_MATCH_ID
								+ "=? AND"
								+ FACT_CYCLE_DATA_Entry.COLUMN_NAME_TEAM_ID
								+ "=? AND"
								+ FACT_CYCLE_DATA_Entry.COLUMN_NAME_CYCLE_NUM
								+ "=?", where, null, // don't
						// group
						null, // don't filter
						null, // don't order
						"0,1"); // limit to 1
				int id = 0;
				if (!c.moveToFirst()) {
					if (action == Actions.UPDATE)
						action = Actions.INSERT;
					else if (action == Actions.DELETE)
						action = Actions.NOTHING;
				} else
					id = c.getInt(c
							.getColumnIndexOrThrow(FACT_CYCLE_DATA_Entry.COLUMN_NAME_ID));

				where[0] = String.valueOf(id);

				switch (action) {
				case UPDATE:
					db.update(FACT_CYCLE_DATA_Entry.TABLE_NAME, vals,
							FACT_CYCLE_DATA_Entry.COLUMN_NAME_ID + " = ?",
							where);
					break;
				case INSERT:
					db.insert(FACT_CYCLE_DATA_Entry.TABLE_NAME, null, vals);
					break;
				case DELETE:
					db.delete(FACT_CYCLE_DATA_Entry.TABLE_NAME,
							FACT_CYCLE_DATA_Entry.COLUMN_NAME_ID + " = ?",
							where);
					break;
				default:
				}

			}
		} catch (JSONException e) {
			// TODO handle error
		}
	}

	private void processMatches(JSONArray matches, SQLiteDatabase db) {
		try {
			for (int i = 0; i < matches.length(); i++) {
				JSONObject row = matches.getJSONObject(i);
				Actions action = Actions.UPDATE;
				if (row.getInt(FACT_MATCH_DATA_Entry.COLUMN_NAME_INVALID) != 0) {
					action = Actions.DELETE;
				}
				ContentValues vals = new ContentValues();
				vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_ID,
						row.getInt(FACT_MATCH_DATA_Entry.COLUMN_NAME_ID));
				vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_EVENT_ID,
						row.getInt(FACT_MATCH_DATA_Entry.COLUMN_NAME_EVENT_ID));
				vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_MATCH_ID,
						row.getInt(FACT_MATCH_DATA_Entry.COLUMN_NAME_MATCH_ID));
				vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_TEAM_ID,
						row.getInt(FACT_MATCH_DATA_Entry.COLUMN_NAME_TEAM_ID));
				vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_AUTO_HIGH,
						row.getInt(FACT_MATCH_DATA_Entry.COLUMN_NAME_AUTO_HIGH));
				vals.put(
						FACT_MATCH_DATA_Entry.COLUMN_NAME_AUTO_HIGH_HOT,
						row.getInt(FACT_MATCH_DATA_Entry.COLUMN_NAME_AUTO_HIGH_HOT));
				vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_AUTO_LOW,
						row.getInt(FACT_MATCH_DATA_Entry.COLUMN_NAME_AUTO_LOW));
				vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_AUTO_LOW_HOT, row
						.getInt(FACT_MATCH_DATA_Entry.COLUMN_NAME_AUTO_LOW_HOT));
				vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_HIGH,
						row.getInt(FACT_MATCH_DATA_Entry.COLUMN_NAME_HIGH));
				vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_LOW,
						row.getInt(FACT_MATCH_DATA_Entry.COLUMN_NAME_LOW));
				vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_AUTO_MOBILE, row
						.getInt(FACT_MATCH_DATA_Entry.COLUMN_NAME_AUTO_MOBILE));
				vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_FOUL,
						row.getInt(FACT_MATCH_DATA_Entry.COLUMN_NAME_FOUL));
				vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_TECH_FOUL,
						row.getInt(FACT_MATCH_DATA_Entry.COLUMN_NAME_TECH_FOUL));
				vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_TIP_OVER,
						row.getInt(FACT_MATCH_DATA_Entry.COLUMN_NAME_TIP_OVER));
				vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_YELLOW_CARD, row
						.getInt(FACT_MATCH_DATA_Entry.COLUMN_NAME_YELLOW_CARD));
				vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_RED_CARD,
						row.getInt(FACT_MATCH_DATA_Entry.COLUMN_NAME_RED_CARD));
				vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_NOTES,
						row.getString(FACT_MATCH_DATA_Entry.COLUMN_NAME_NOTES));
				vals.put(
						FACT_MATCH_DATA_Entry.COLUMN_NAME_TIMESTAMP,
						dateParser.format(new Date(
								row.getLong(FACT_MATCH_DATA_Entry.COLUMN_NAME_TIMESTAMP))));

				// check if this entry exists already
				String[] projection = { FACT_MATCH_DATA_Entry.COLUMN_NAME_ID };
				String[] where = {
						vals.getAsString(FACT_MATCH_DATA_Entry.COLUMN_NAME_EVENT_ID),
						vals.getAsString(FACT_MATCH_DATA_Entry.COLUMN_NAME_MATCH_ID),
						vals.getAsString(FACT_MATCH_DATA_Entry.COLUMN_NAME_TEAM_ID) };
				Cursor c = db.query(FACT_MATCH_DATA_Entry.TABLE_NAME,
						projection, // select
						FACT_CYCLE_DATA_Entry.COLUMN_NAME_EVENT_ID + "=? AND "
								+ FACT_CYCLE_DATA_Entry.COLUMN_NAME_MATCH_ID
								+ "=? AND"
								+ FACT_CYCLE_DATA_Entry.COLUMN_NAME_TEAM_ID
								+ "=?", where, null, // don't
						// group
						null, // don't filter
						null, // don't order
						"0,1"); // limit to 1
				int id = 0;
				if (!c.moveToFirst()) {
					if (action == Actions.UPDATE)
						action = Actions.INSERT;
					else if (action == Actions.DELETE)
						action = Actions.NOTHING;
				} else
					id = c.getInt(c
							.getColumnIndexOrThrow(FACT_MATCH_DATA_Entry.COLUMN_NAME_ID));

				where[0] = String.valueOf(id);

				switch (action) {
				case UPDATE:
					db.update(FACT_MATCH_DATA_Entry.TABLE_NAME, vals,
							FACT_MATCH_DATA_Entry.COLUMN_NAME_ID + " = ?",
							where);
					break;
				case INSERT:
					db.insert(FACT_MATCH_DATA_Entry.TABLE_NAME, null, vals);
					break;
				case DELETE:
					db.delete(FACT_MATCH_DATA_Entry.TABLE_NAME,
							FACT_MATCH_DATA_Entry.COLUMN_NAME_ID + " = ?",
							where);
					break;
				default:
				}

			}
		} catch (JSONException e) {
			// TODO handle error
		}
	}

	private void processNotes(JSONArray notes, SQLiteDatabase db) {
		try {
			for (int i = 0; i < notes.length(); i++) {
				JSONObject row = notes.getJSONObject(i);
				Actions action = Actions.UPDATE;
				if (row.getInt(NOTES_OPTIONS_Entry.COLUMN_NAME_INVALID) != 0) {
					action = Actions.DELETE;
				}
				ContentValues vals = new ContentValues();
				vals.put(NOTES_OPTIONS_Entry.COLUMN_NAME_ID,
						row.getInt(NOTES_OPTIONS_Entry.COLUMN_NAME_ID));
				vals.put(NOTES_OPTIONS_Entry.COLUMN_NAME_OPTION_TEXT, row
						.getString(NOTES_OPTIONS_Entry.COLUMN_NAME_OPTION_TEXT));
				vals.put(
						NOTES_OPTIONS_Entry.COLUMN_NAME_TIMESTAMP,
						dateParser.format(new Date(
								row.getLong(NOTES_OPTIONS_Entry.COLUMN_NAME_TIMESTAMP))));

				// check if this entry exists already
				String[] projection = { NOTES_OPTIONS_Entry.COLUMN_NAME_OPTION_TEXT };
				String[] where = { vals
						.getAsString(NOTES_OPTIONS_Entry.COLUMN_NAME_ID) };
				Cursor c = db.query(NOTES_OPTIONS_Entry.TABLE_NAME, projection, // select
						NOTES_OPTIONS_Entry.COLUMN_NAME_ID + "=?", where, null, // don't
																				// group
						null, // don't filter
						null, // don't order
						"0,1"); // limit to 1
				if (!c.moveToFirst()) {
					if (action == Actions.UPDATE)
						action = Actions.INSERT;
					else if (action == Actions.DELETE)
						action = Actions.NOTHING;
				}

				switch (action) {
				case UPDATE:
					db.update(NOTES_OPTIONS_Entry.TABLE_NAME, vals,
							NOTES_OPTIONS_Entry.COLUMN_NAME_ID + " = ?", where);
					break;
				case INSERT:
					db.insert(NOTES_OPTIONS_Entry.TABLE_NAME, null, vals);
					break;
				case DELETE:
					db.delete(NOTES_OPTIONS_Entry.TABLE_NAME,
							NOTES_OPTIONS_Entry.COLUMN_NAME_ID + " = ?", where);
					break;
				default:
				}

			}
		} catch (JSONException e) {
			// TODO handle error
		}
	}

	private void processRobots(JSONArray robots, SQLiteDatabase db) {
		try {
			for (int i = 0; i < robots.length(); i++) {
				JSONObject row = robots.getJSONObject(i);
				Actions action = Actions.UPDATE;
				if (row.getInt(ROBOT_LU_Entry.COLUMN_NAME_INVALID) != 0) {
					action = Actions.DELETE;
				}
				ContentValues vals = new ContentValues();
				vals.put(ROBOT_LU_Entry.COLUMN_NAME_ID,
						row.getInt(ROBOT_LU_Entry.COLUMN_NAME_ID));
				vals.put(ROBOT_LU_Entry.COLUMN_NAME_TEAM_ID,
						row.getInt(ROBOT_LU_Entry.COLUMN_NAME_TEAM_ID));
				vals.put(ROBOT_LU_Entry.COLUMN_NAME_ROBOT_PHOTO,
						row.getString(ROBOT_LU_Entry.COLUMN_NAME_ROBOT_PHOTO));
				vals.put(
						ROBOT_LU_Entry.COLUMN_NAME_TIMESTAMP,
						dateParser.format(new Date(row
								.getLong(ROBOT_LU_Entry.COLUMN_NAME_TIMESTAMP))));

				// check if this entry exists already
				String[] projection = { ROBOT_LU_Entry.COLUMN_NAME_ROBOT_PHOTO };
				String[] where = { vals
						.getAsString(ROBOT_LU_Entry.COLUMN_NAME_TEAM_ID) };
				Cursor c = db.query(ROBOT_LU_Entry.TABLE_NAME, projection, // select
						ROBOT_LU_Entry.COLUMN_NAME_TEAM_ID + "=?", where, null, // don't
																				// group
						null, // don't filter
						null, // don't order
						"0,1"); // limit to 1
				if (!c.moveToFirst()) {
					if (action == Actions.UPDATE)
						action = Actions.INSERT;
					else if (action == Actions.DELETE)
						action = Actions.NOTHING;
				}

				switch (action) {
				case UPDATE:
					db.update(ROBOT_LU_Entry.TABLE_NAME, vals,
							ROBOT_LU_Entry.COLUMN_NAME_TEAM_ID + " = ?", where);
					break;
				case INSERT:
					db.insert(ROBOT_LU_Entry.TABLE_NAME, null, vals);
					break;
				case DELETE:
					db.delete(ROBOT_LU_Entry.TABLE_NAME,
							ROBOT_LU_Entry.COLUMN_NAME_TEAM_ID + " = ?", where);
					break;
				default:
				}

			}
		} catch (JSONException e) {
			// TODO handle error
		}
	}

	private void processPits(JSONArray pits, SQLiteDatabase db) {
		try {
			for (int i = 0; i < pits.length(); i++) {
				JSONObject row = pits.getJSONObject(i);
				Actions action = Actions.UPDATE;
				if (row.getInt(SCOUT_PIT_DATA_Entry.COLUMN_NAME_INVALID) != 0) {
					action = Actions.DELETE;
				}
				ContentValues vals = new ContentValues();
				vals.put(SCOUT_PIT_DATA_Entry.COLUMN_NAME_ID,
						row.getInt(SCOUT_PIT_DATA_Entry.COLUMN_NAME_ID));
				vals.put(SCOUT_PIT_DATA_Entry.COLUMN_NAME_TEAM_ID,
						row.getInt(SCOUT_PIT_DATA_Entry.COLUMN_NAME_TEAM_ID));
				vals.put(
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_CONFIGURATION_ID,
						row.getInt(SCOUT_PIT_DATA_Entry.COLUMN_NAME_CONFIGURATION_ID));
				vals.put(SCOUT_PIT_DATA_Entry.COLUMN_NAME_WHEEL_TYPE_ID, row
						.getInt(SCOUT_PIT_DATA_Entry.COLUMN_NAME_WHEEL_TYPE_ID));
				vals.put(SCOUT_PIT_DATA_Entry.COLUMN_NAME_WHEEL_BASE_ID, row
						.getInt(SCOUT_PIT_DATA_Entry.COLUMN_NAME_WHEEL_BASE_ID));
				vals.put(
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTONOMOUS_MODE,
						row.getInt(SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTONOMOUS_MODE));
				vals.put(SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTO_HIGH,
						row.getInt(SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTO_HIGH));
				vals.put(SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTO_LOW,
						row.getInt(SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTO_LOW));
				vals.put(SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTO_HOT,
						row.getInt(SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTO_HOT));
				vals.put(SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTO_MOBILE, row
						.getInt(SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTO_MOBILE));
				vals.put(SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTO_GOALIE, row
						.getInt(SCOUT_PIT_DATA_Entry.COLUMN_NAME_AUTO_GOALIE));
				vals.put(SCOUT_PIT_DATA_Entry.COLUMN_NAME_TRUSS,
						row.getInt(SCOUT_PIT_DATA_Entry.COLUMN_NAME_TRUSS));
				vals.put(SCOUT_PIT_DATA_Entry.COLUMN_NAME_CATCH,
						row.getInt(SCOUT_PIT_DATA_Entry.COLUMN_NAME_CATCH));
				vals.put(
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_ACTIVE_CONTROL,
						row.getInt(SCOUT_PIT_DATA_Entry.COLUMN_NAME_ACTIVE_CONTROL));
				vals.put(SCOUT_PIT_DATA_Entry.COLUMN_NAME_LAUNCH_BALL, row
						.getInt(SCOUT_PIT_DATA_Entry.COLUMN_NAME_LAUNCH_BALL));
				vals.put(SCOUT_PIT_DATA_Entry.COLUMN_NAME_SCORE_HIGH,
						row.getInt(SCOUT_PIT_DATA_Entry.COLUMN_NAME_SCORE_HIGH));
				vals.put(SCOUT_PIT_DATA_Entry.COLUMN_NAME_SCORE_LOW,
						row.getInt(SCOUT_PIT_DATA_Entry.COLUMN_NAME_SCORE_LOW));
				vals.put(SCOUT_PIT_DATA_Entry.COLUMN_NAME_MAX_HEIGHT,
						row.getInt(SCOUT_PIT_DATA_Entry.COLUMN_NAME_MAX_HEIGHT));
				vals.put(
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_SCOUT_COMMENTS,
						row.getString(SCOUT_PIT_DATA_Entry.COLUMN_NAME_SCOUT_COMMENTS));
				vals.put(
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_TIMESTAMP,
						dateParser.format(new Date(
								row.getLong(SCOUT_PIT_DATA_Entry.COLUMN_NAME_TIMESTAMP))));

				// check if this entry exists already
				String[] projection = { SCOUT_PIT_DATA_Entry.COLUMN_NAME_ID };
				String[] where = { vals
						.getAsString(SCOUT_PIT_DATA_Entry.COLUMN_NAME_TEAM_ID) };
				Cursor c = db.query(
						SCOUT_PIT_DATA_Entry.TABLE_NAME,
						projection, // select
						SCOUT_PIT_DATA_Entry.COLUMN_NAME_TEAM_ID + "=?", where,
						null, // don't group
						null, // don't filter
						null, // don't order
						"0,1"); // limit to 1
				if (!c.moveToFirst()) {
					if (action == Actions.UPDATE)
						action = Actions.INSERT;
					else if (action == Actions.DELETE)
						action = Actions.NOTHING;
				}

				switch (action) {
				case UPDATE:
					db.update(SCOUT_PIT_DATA_Entry.TABLE_NAME, vals,
							SCOUT_PIT_DATA_Entry.COLUMN_NAME_TEAM_ID + " = ?",
							where);
					break;
				case INSERT:
					db.insert(SCOUT_PIT_DATA_Entry.TABLE_NAME, null, vals);
					break;
				case DELETE:
					db.delete(SCOUT_PIT_DATA_Entry.TABLE_NAME,
							SCOUT_PIT_DATA_Entry.COLUMN_NAME_TEAM_ID + " = ?",
							where);
					break;
				default:
				}

			}
		} catch (JSONException e) {
			// TODO handle error
		}
	}

	private void processWheelBase(JSONArray wheelBase, SQLiteDatabase db) {
		try {
			for (int i = 0; i < wheelBase.length(); i++) {
				JSONObject row = wheelBase.getJSONObject(i);
				Actions action = Actions.UPDATE;
				if (row.getInt(WHEEL_BASE_LU_Entry.COLUMN_NAME_INVALID) != 0) {
					action = Actions.DELETE;
				}
				ContentValues vals = new ContentValues();
				vals.put(WHEEL_BASE_LU_Entry.COLUMN_NAME_ID,
						row.getInt(WHEEL_BASE_LU_Entry.COLUMN_NAME_ID));
				vals.put(
						WHEEL_BASE_LU_Entry.COLUMN_NAME_WHEEL_BASE_DESC,
						row.getString(WHEEL_BASE_LU_Entry.COLUMN_NAME_WHEEL_BASE_DESC));
				vals.put(
						WHEEL_BASE_LU_Entry.COLUMN_NAME_TIMESTAMP,
						dateParser.format(new Date(
								row.getLong(WHEEL_BASE_LU_Entry.COLUMN_NAME_TIMESTAMP))));

				// check if this entry exists already
				String[] projection = { WHEEL_BASE_LU_Entry.COLUMN_NAME_WHEEL_BASE_DESC };
				String[] where = { vals
						.getAsString(WHEEL_BASE_LU_Entry.COLUMN_NAME_ID) };
				Cursor c = db.query(WHEEL_BASE_LU_Entry.TABLE_NAME, projection, // select
						WHEEL_BASE_LU_Entry.COLUMN_NAME_ID + "=?", where, null, // don't
						// group
						null, // don't filter
						null, // don't order
						"0,1"); // limit to 1
				if (!c.moveToFirst()) {
					if (action == Actions.UPDATE)
						action = Actions.INSERT;
					else if (action == Actions.DELETE)
						action = Actions.NOTHING;
				}

				switch (action) {
				case UPDATE:
					db.update(WHEEL_BASE_LU_Entry.TABLE_NAME, vals,
							WHEEL_BASE_LU_Entry.COLUMN_NAME_ID + " = ?", where);
					break;
				case INSERT:
					db.insert(WHEEL_BASE_LU_Entry.TABLE_NAME, null, vals);
					break;
				case DELETE:
					db.delete(WHEEL_BASE_LU_Entry.TABLE_NAME,
							WHEEL_BASE_LU_Entry.COLUMN_NAME_ID + " = ?", where);
					break;
				default:
				}

			}
		} catch (JSONException e) {
			// TODO handle error
		}
	}

	private void processWheelType(JSONArray wheelType, SQLiteDatabase db) {
		try {
			for (int i = 0; i < wheelType.length(); i++) {
				JSONObject row = wheelType.getJSONObject(i);
				Actions action = Actions.UPDATE;
				if (row.getInt(WHEEL_TYPE_LU_Entry.COLUMN_NAME_INVALID) != 0) {
					action = Actions.DELETE;
				}
				ContentValues vals = new ContentValues();
				vals.put(WHEEL_TYPE_LU_Entry.COLUMN_NAME_ID,
						row.getInt(WHEEL_TYPE_LU_Entry.COLUMN_NAME_ID));
				vals.put(
						WHEEL_TYPE_LU_Entry.COLUMN_NAME_WHEEL_TYPE_DESC,
						row.getString(WHEEL_TYPE_LU_Entry.COLUMN_NAME_WHEEL_TYPE_DESC));
				vals.put(
						WHEEL_TYPE_LU_Entry.COLUMN_NAME_TIMESTAMP,
						dateParser.format(new Date(
								row.getLong(WHEEL_TYPE_LU_Entry.COLUMN_NAME_TIMESTAMP))));

				// check if this entry exists already
				String[] projection = { WHEEL_TYPE_LU_Entry.COLUMN_NAME_WHEEL_TYPE_DESC };
				String[] where = { vals
						.getAsString(WHEEL_TYPE_LU_Entry.COLUMN_NAME_ID) };
				Cursor c = db.query(WHEEL_TYPE_LU_Entry.TABLE_NAME, projection, // select
						WHEEL_TYPE_LU_Entry.COLUMN_NAME_ID + "=?", where, null, // don't
						// group
						null, // don't filter
						null, // don't order
						"0,1"); // limit to 1
				if (!c.moveToFirst()) {
					if (action == Actions.UPDATE)
						action = Actions.INSERT;
					else if (action == Actions.DELETE)
						action = Actions.NOTHING;
				}

				switch (action) {
				case UPDATE:
					db.update(WHEEL_TYPE_LU_Entry.TABLE_NAME, vals,
							WHEEL_TYPE_LU_Entry.COLUMN_NAME_ID + " = ?", where);
					break;
				case INSERT:
					db.insert(WHEEL_TYPE_LU_Entry.TABLE_NAME, null, vals);
					break;
				case DELETE:
					db.delete(WHEEL_TYPE_LU_Entry.TABLE_NAME,
							WHEEL_TYPE_LU_Entry.COLUMN_NAME_ID + " = ?", where);
					break;
				default:
				}

			}
		} catch (JSONException e) {
			// TODO handle error
		}
	}
}
