package org.frc836.database;

import org.growingstems.scouting.Prefs;
import org.sigmond.net.HttpUtils;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

public class DBSyncService extends Service {

	private IBinder mBinder = new LocalBinder();

	private HttpUtils utils = new HttpUtils();

	private ScoutingDBHelper helper = null;

	private String password = null;

	private Handler mTimerTask;
	private SyncDataTask dataTask;
	
	private InitialSync init;

	private static final int DELAY = 60000;
	

	public void onCreate() {
		mTimerTask = new Handler();

		dataTask = new SyncDataTask();

		init = new InitialSync();
		password = Prefs.getSavedPassword(getApplicationContext());
		init.execute();
		
		
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

	private class InitialSync extends AsyncTask<String, Integer, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}

		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			mTimerTask.post(dataTask);
		}

	}

	private class SyncDataTask implements Runnable {

		public void run() {
			// TODO Auto-generated method stub

			mTimerTask.postDelayed(this, DELAY);
		}

	}

}
