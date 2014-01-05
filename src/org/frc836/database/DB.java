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

package org.frc836.database;

import java.util.HashMap;
import java.util.Map;

import org.frc836.database.FRCScoutingContract.EVENT_LU_Entry;
import org.frc836.database.FRCScoutingContract.FACT_MATCH_DATA_Entry;
import org.frc836.ultimateascent.*;
import org.growingstems.scouting.Prefs;
import org.sigmond.net.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DB {

	private HttpUtils utils;
	private String password;
	private Context context;
	private ScoutingDBHelper helper;

	@SuppressWarnings("unused")
	private DB() {
	}

	public DB(Context context, String pass) {
		utils = new HttpUtils();
		password = pass;
		this.context = context;
		helper = new ScoutingDBHelper(context);
	}

	public DB(Context context) {
		this.context = context;
		utils = new HttpUtils();
		password = Prefs.getSavedPassword(context);
		helper = new ScoutingDBHelper(context);
	}
	
	protected static Map<String,String> getPostData(ContentValues values)
	{
		Map<String,String> data = new HashMap<String,String>();
		for(String key: values.keySet())
		{
			data.put(key, values.getAsString(key));
		}
		return data;
	}

	public void submitMatch(MatchStatsStruct auto, MatchStatsStruct tele,
			HttpCallback callback) {
		Map<String, String> args = tele.getPost();
		args.putAll(auto.getPost());
		args.put("type", "match");
		args.put("password", password);
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = tele.getValues(this);
		values.putAll(auto.getValues(this));
		db.insertOrThrow(FACT_MATCH_DATA_Entry.TABLE_NAME, null, values);
		utils.doPost(Prefs.getScoutingURL(context), args, callback);
	}

	public void submitPits(PitStats stats, HttpCallback callback) {
		Map<String, String> args = stats.getPost();
		args.put("type", "pits");
		args.put("password", password);

		utils.doPost(Prefs.getScoutingURL(context), args, callback);
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
		Map<String, String> args = new HashMap<String, String>();
		args.put("team_id", teamNum);
		args.put("type", "pitInfo");
		args.put("password", password);
		utils.doPost(Prefs.getScoutingURL(context), args, callback);
	}

	public void getEventList(HttpCallback callback) {
		Map<String, String> args = new HashMap<String, String>();
		args.put("type", "paramRequest");
		args.put("req", "eventList");
		utils.doPost(Prefs.getScoutingURL(context), args, callback);
	}

	public void getParams(String tableName, HttpCallback callback) {
		Map<String, String> args = new HashMap<String, String>();
		args.put("type", "paramRequest");
		args.put("req", tableName);
		utils.doPost(Prefs.getScoutingURL(context), args, callback);
	}

	public void getEventStats(String eventName,
			EventStats.EventCallback callback) {
		Map<String, String> args = new HashMap<String, String>();
		args.put("type", "eventStats");
		args.put("password", password);
		args.put("event_name", eventName);
		utils.doPost(Prefs.getScoutingURL(context), args, new EventStats(
				callback));
	}

	public void getTeamStats(int teamId, TeamStats.TeamCallback callback) {
		Map<String, String> args = new HashMap<String, String>();
		args.put("type", "teamStats");
		args.put("password", password);
		args.put("team_id", String.valueOf(teamId));
		utils.doPost(
				Prefs.getScoutingURL(context),
				args,
				new TeamStats(callback, teamId, Prefs.getEvent(context,
						"Chesapeake Regional")));
	}

	public void getMatchStats(String event, String match,
			MatchStats.MatchCallback callback) {
		Map<String, String> args = new HashMap<String, String>();
		args.put("event_name", event);
		args.put("match_id", match);
		args.put("password", password);
		args.put("type", "matchStats");
		utils.doPost(Prefs.getScoutingURL(context), args, new MatchStats(
				callback));
	}

	public void getPictureURL(String teamID, HttpCallback callback) {
		Map<String, String> args = new HashMap<String, String>();
		args.put("type", "robotPic");
		args.put("team_id", teamID);
		utils.doPost(Prefs.getScoutingURL(context), args, callback);
	}

	public void getTeamPitStats(int teamId, HttpCallback callback) {
		Map<String, String> args = new HashMap<String, String>();
		args.put("type", "teamStats");
		args.put("password", password);
		args.put("team_id", String.valueOf(teamId));
		utils.doPost(Prefs.getScoutingURL(context), args, callback);
	}

	public void getParamsPass(String table, HttpCallback callback) {
		Map<String, String> args = new HashMap<String, String>();
		args.put("password", password);
		args.put("type", "paramRequest");
		args.put("req", table);
		utils.doPost(Prefs.getScoutingURL(context), args, callback);
	}
	
	public long getEventIDFromName(String EventName)
	{
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] projection = {EVENT_LU_Entry.COLUMN_NAME_ID};
		String[] where = {EventName};
		Cursor c = db.query(EVENT_LU_Entry.TABLE_NAME, //from the event_lu table
				projection, //select 
				EVENT_LU_Entry.COLUMN_NAME_EVENT_NAME, //where event_name == 
				where, //EventName
				null, //don't group 
				null, //don't filter
				null, //don't order
				"0,1"); //limit to 1
		c.moveToFirst();
		return c.getLong(c.getColumnIndexOrThrow(EVENT_LU_Entry.COLUMN_NAME_ID));
	}

}
