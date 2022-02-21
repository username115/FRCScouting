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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frc836.database.DB;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.GuardedBy;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

public class MatchSchedule {

	private static final String FILENAME = "FRCscoutingschedule";
	private static final String SCHEDULE_URL = "https://growingstems.org/schedule.php?request=schedule";

	private boolean offseason = false;
	private boolean toastComplete;

	private DB db;

	private Context _parent;

	private RequestQueue reqQueue = null;

	private class ScheduleResponse {
		String resp;
		int statusCode;

	}

	private class ScheduleRequest extends Request<ScheduleResponse> {

		private final Object mLock = new Object();

		@GuardedBy("mLock")
		Response.Listener<ScheduleResponse> callback;

		public ScheduleRequest(String eventCode, boolean practiceMatch, Response.Listener<ScheduleResponse> listener, @Nullable Response.ErrorListener errorListener) {
			super(Request.Method.GET, SCHEDULE_URL + "&event=" + eventCode + (practiceMatch ? "&tournamentLevel=Practice" : "&tournamentLevel=Qualification"), errorListener);
			callback = listener;
		}

		@Override
		public void cancel() {
			super.cancel();
			synchronized (mLock) {
				callback = null;
			}
		}

		@Override
		protected Response<ScheduleResponse> parseNetworkResponse(NetworkResponse response) {
			String parsed;
			try {
				parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			} catch (UnsupportedEncodingException e) {
				parsed = new String(response.data);
			}
			ScheduleResponse schResp = new ScheduleResponse();
			schResp.resp = parsed;
			schResp.statusCode = response.statusCode;
			return Response.success(schResp, HttpHeaderParser.parseCacheHeaders(response));
		}

		@Override
		protected void deliverResponse(ScheduleResponse response) {
			Response.Listener<ScheduleResponse> listener;
			synchronized (mLock) {
				listener = callback;
			}
			if (listener != null) {
				listener.onResponse(response);
			}
		}

		@Override
		public Map<String, String> getHeaders() {
			Map<String, String> headers = new HashMap<String, String>(1);
			headers.put("Accept", "application/json");
			return headers;
		}
	}

	public void updateSchedule(String event, Context parent,
			boolean toastWhenComplete) {
		if (reqQueue == null) {
			reqQueue = Volley.newRequestQueue(parent);
			reqQueue.start();
		}

		_parent = parent;
		toastComplete = toastWhenComplete;

		db = new DB(_parent, null);

		reqQueue.add(new ScheduleRequest(db.getCodeFromEventName(event), Prefs.getPracticeMatch(parent, false), this::onResponse, this::onError));

	}

	public void onResponse(ScheduleResponse resp) {
		try {
			String r = resp.resp;
			if (resp.statusCode == 200) {
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

	public void onError(VolleyError e) {
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

	public List<Integer> getMatchesForTeam(int team, Context parent) {
        try {
            String schedule = getSchedule(parent);
            if (schedule.compareTo("No Schedule") == 0)
                return null;
            List<Integer> matches = new ArrayList<>(16);

            JSONArray sched = new JSONObject(schedule).getJSONArray("Schedule");

            for (int i = 0; i < sched.length(); i++) {
                JSONArray teams = sched.getJSONObject(i).getJSONArray(
                        "Teams");
                for (int j = 0; j < teams.length(); j++) {
                    if (teams.getJSONObject(j).getInt("teamNumber") == team)
                        matches.add(sched.getJSONObject(i).getInt("matchNumber"));
                }
            }
            return matches;

        } catch (Exception e) {
            return null;
        }
    }

    public List<Integer> getTeamList(Context parent) {
		try {
			String schedule = getSchedule(parent);
			if (schedule.compareTo("No Schedule") == 0)
				return null;
			List<Integer> teamList = new ArrayList<>(16);

			JSONArray sched = new JSONObject(schedule).getJSONArray("Schedule");

			for (int i = 0; i < sched.length(); i++) {
				JSONArray teams = sched.getJSONObject(i).getJSONArray(
						"Teams");
				for (int j = 0; j < teams.length(); j++) {
					if (!teamList.contains(teams.getJSONObject(j).getInt("number")))
						teamList.add(teams.getJSONObject(j).getInt("number"));
				}
			}
			return teamList;

		} catch (Exception e) {
			return null;
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
