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

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.frc836.database.DB;
import org.json.JSONArray;
import org.json.JSONObject;
import org.sigmond.net.HttpCallback;
import org.sigmond.net.HttpRequestInfo;
import org.sigmond.net.HttpUtils;

import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

public class MatchSchedule implements HttpCallback {

	private static final String FILENAME = "FRCscoutingschedule";
	private static final String FRC_API_URL = "https://frc-api.usfirst.org";
	private static final String API_CALL = "/api/v1.0/schedule/2015/";

	private boolean offseason = false;
	private boolean toastComplete;

	private DB db;

	private Context _parent;

	public void updateSchedule(String event, Context parent,
			boolean toastWhenComplete) {
		HttpUtils utils = new HttpUtils();
		_parent = parent;
		toastComplete = toastWhenComplete;

		db = new DB(_parent, null);

		String url = FRC_API_URL
				+ API_CALL
				+ db.getCodeFromEventName(event)
				+ (Prefs.getPracticeMatch(parent, false) ? "/?tournamentLevel=practice"
						: "/?tournamentLevel=qual");
		Map<String, String> headers = new HashMap<String, String>(1);
		headers.put("Accept", "application/json");
		headers.put(
				"Authorization",
				"Basic "
						+ Base64.encodeToString(FMSToken.token.getBytes(),
								Base64.NO_WRAP));
		if (url != null)
			utils.doGet(url, this, headers);

	}

	public void onResponse(HttpRequestInfo resp) {
		try {
			String r = resp.getResponseString();
			if (resp.getResponse().getStatusLine().toString().contains("200")) {
				FileOutputStream fos = _parent.openFileOutput(FILENAME,
						Context.MODE_PRIVATE);
				fos.write(r.getBytes());
				fos.close();
			} else if (offseason) {
				FileOutputStream fos = _parent.openFileOutput(FILENAME,
						Context.MODE_PRIVATE);
				fos.write("No Schedule".getBytes());
				fos.close();
			}
			if (toastComplete) {
				Toast.makeText(
						_parent,
						r.contains("\"level\"") ? "Schedule Successfully Updated"
								: "No Schedule Available", Toast.LENGTH_SHORT)
						.show();
			}
		} catch (Exception e) {
			if (toastComplete)
				Toast.makeText(_parent,
						"Error Saving Schedule: " + e.getMessage(),
						Toast.LENGTH_SHORT).show();
		}
	}

	public void onError(Exception e) {
		try {
			if (toastComplete)
				Toast.makeText(_parent, "Error Downloading Schedule",
						Toast.LENGTH_SHORT).show();
			if (offseason) {
				FileOutputStream fos = _parent.openFileOutput(FILENAME,
						Context.MODE_PRIVATE);
				fos.write("No Schedule".getBytes());
				fos.close();
			}
		} catch (Exception es) {

		}

	}

	public String getTeam(int match, String pos, Context parent) {
		return getTeam(match, pos, parent, "");
	}

	public String getTeam(int match, String pos, Context parent,
			String defaultVal) {
		try {
			String schedule = getSchedule(parent);
			if (schedule.compareTo("No Schedule") == 0)
				return defaultVal;

			String position = pos.replaceAll("\\s+", "");

			String ret = "";
			JSONArray sched = new JSONObject(schedule).getJSONArray("Schedule");

			for (int i = 0; i < sched.length(); i++) {
				if (sched.getJSONObject(i).getInt("matchNumber") == match) {
					JSONArray teams = sched.getJSONObject(i).getJSONArray(
							"Teams");
					for (int j = 0; j < teams.length(); j++) {
						if (teams.getJSONObject(j).getString("station")
								.compareToIgnoreCase(position) == 0)
							ret = String.valueOf(teams.getJSONObject(j).getInt(
									"teamNumber"));
					}
				}
			}
			if (ret.length() < 10 && Integer.valueOf(ret) > 0)
				return ret;
			else
				return defaultVal;

		} catch (Exception e) {
			return defaultVal;
		}
	}

	private String getSchedule(Context parent) {
		try {
			BufferedInputStream bis = new BufferedInputStream(
					parent.openFileInput(FILENAME));
			byte[] buffer = new byte[bis.available()];
			bis.read(buffer, 0, buffer.length);
			return new String(buffer);
		} catch (Exception e) {
			return "";
		}
	}

	public boolean isValid(Context parent) {
		String schedule = getSchedule(parent);
		// if there is no schedule released yet, will still have valid json, but
		// not any entries
		if (schedule.contains("\"level\""))
			return true;
		else
			return false;
	}

}
