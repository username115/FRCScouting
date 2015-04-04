package org.frc836.database;

import org.frc836.database.DBSyncService.LocalBinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

public abstract class DBActivity extends Activity {
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
