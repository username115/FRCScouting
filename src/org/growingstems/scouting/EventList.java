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

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.sigmond.net.HttpCallback;
import org.sigmond.net.HttpRequestInfo;

import android.content.Context;

public class EventList implements HttpCallback {

	private Context _parent;

	private static final String FILENAME = "FRCEventList.xml";

	private DB database;

	private EventCallback _callback = null;

	public EventList(Context parent) {
		_parent = parent;
		database = new DB(parent);
		if (getEvents() == "")
			downloadEventsList(null);
	}

	public void downloadEventsList(EventCallback callback) {
		_callback = callback;
		database.getEventList(this);
	}

	public void onResponse(HttpRequestInfo resp) {
		try {
			String r = resp.getResponseString();
			if (resp.getResponse().getStatusLine().toString().contains("200")) {
				FileOutputStream fos = _parent.openFileOutput(FILENAME,
						Context.MODE_PRIVATE);
				fos.write(r.getBytes());
				fos.close();
				if (_callback != null)
					_callback.eventsUpdated(getEventList());
			}
		} catch (Exception e) {
			if (_callback != null)
				_callback.eventsUpdated(null);
		}
	}

	public void onError(Exception e) {
		if (_callback != null)
			_callback.eventsUpdated(null);
	}

	private String getEvents() {
		try {
			BufferedInputStream bis = new BufferedInputStream(
					_parent.openFileInput(FILENAME));
			byte[] buffer = new byte[bis.available()];
			bis.read(buffer, 0, buffer.length);
			return new String(buffer);
		} catch (Exception e) {
			return "";
		}
	}

	public List<String> getEventList() {
		String events = getEvents();
		if (events != "") {
			try {
				return XMLDBParser.extractColumn("event_name", events);
			} catch (Exception e) {
			}
		}
		return new ArrayList<String>();
	}

	public String getMatchScheduleURL(String event) {
		String ret = "";

		String events = getEvents();

		try {
			ret = XMLDBParser.dataLookupByValue("event_name", event,
					"match_url", events);
		} catch (Exception e) {
		}

		return ret;
	}

	public interface EventCallback {
		public void eventsUpdated(List<String> events);
	}

}
