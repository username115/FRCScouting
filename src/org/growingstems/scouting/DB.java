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

import java.util.HashMap;
import java.util.Map;

import org.frc836.ultimateascent.*;
import org.sigmond.net.*;

import android.content.Context;

public class DB {

	private HttpUtils utils;
	private String password;
	private Context context;

	@SuppressWarnings("unused")
	private DB() {
	}

	public DB(Context context, String pass) {
		utils = new HttpUtils();
		password = pass;
		this.context = context;
	}

	public DB(Context context) {
		this.context = context;
		utils = new HttpUtils();
		password = Prefs.getSavedPassword(context);
	}

	public void submitMatch(MatchStatsStruct auto, MatchStatsStruct tele,
			HttpCallback callback) {
		Map<String, String> args = tele.getPost();
		args.putAll(auto.getPost());
		args.put("type", "match");
		args.put("password", password);

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

}
