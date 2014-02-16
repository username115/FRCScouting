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

import org.frc836.database.DB;
import org.frc836.database.XMLDBParser;
import org.sigmond.net.HttpCallback;
import org.sigmond.net.HttpRequestInfo;

import android.content.Context;

public class ParamList implements HttpCallback {
	private String listName;
	private String tableName;
	private Context _parent;
	private DB database;
	private ParamCallback _callback;

	public ParamList(Context parent, String table) {
		_parent = parent;
		tableName = table;
		database = new DB(parent, null); //binder not needed, this class only does local DB operations
	}

	public void downloadParamList(String name, ParamCallback callback) {
		listName = name;
		_callback = callback;
		database.getParams(tableName, this);
	}
	
	public void downloadParamListWithPass(String name, ParamCallback callback) {
		listName = name;
		_callback = callback;
		//database.getParamsPass(tableName, this);
	}

	public void onResponse(HttpRequestInfo resp) {
		try {
			String r = resp.getResponseString();
			if (resp.getResponse().getStatusLine().toString().contains("200")) {
				FileOutputStream fos = _parent.openFileOutput(tableName
						+ "params.xml", Context.MODE_PRIVATE);
				fos.write(r.getBytes());
				fos.close();
				if (_callback != null && listName != null)
					_callback.paramsUpdated(listName, tableName,
							getParamList(listName));
			}
		} catch (Exception e) {

		}

	}

	public void onError(Exception e) {
	}

	private String getParams() {
		try {
			BufferedInputStream bis = new BufferedInputStream(
					_parent.openFileInput(tableName + "params.xml"));
			byte[] buffer = new byte[bis.available()];
			bis.read(buffer, 0, buffer.length);
			return new String(buffer);
		} catch (Exception e) {
			return "";
		}
	}

	public List<String> getParamList(String name) {
		String params = getParams();
		if (params != "") {
			try {
				return XMLDBParser.extractColumn(name, params);
			} catch (Exception e) {
			}
		}
		return new ArrayList<String>();
	}

	public interface ParamCallback {
		public void paramsUpdated(String name, String table, List<String> params);
	}

}
