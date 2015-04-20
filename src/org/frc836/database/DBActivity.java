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

import org.frc836.database.DBSyncService.LocalBinder;
import org.growingstems.scouting.ScoutingMenuActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

public abstract class DBActivity extends ScoutingMenuActivity {
	protected DB db;
	protected LocalBinder binder;
	protected ServiceWatcher watcher = new ServiceWatcher();
	protected ServiceConnection m_callback = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent sync = new Intent(this, DBSyncService.class);
		bindService(sync, watcher, Context.BIND_AUTO_CREATE);
		db = new DB(this, binder);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(watcher);
	}

	/**
	 * @return the db
	 */
	public DB getDB() {
		return db;
	}

	protected class ServiceWatcher implements ServiceConnection {

		public void onServiceConnected(ComponentName name, IBinder service) {
			if (service instanceof LocalBinder) {
				binder = (LocalBinder) service;
				db.setBinder(binder);
				if (m_callback != null)
					m_callback.onServiceConnected(name, service);
			}
		}

		public void onServiceDisconnected(ComponentName name) {
			if (m_callback != null)
				m_callback.onServiceDisconnected(name);
		}

	}

}
