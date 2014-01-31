package org.frc836.database;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.growingstems.scouting.Prefs;
import org.sigmond.net.HttpCallback;
import org.sigmond.net.HttpRequestInfo;
import org.sigmond.net.HttpUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
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

	private static final int DELAY = 60000;

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

	private void initialSync() {
		Map<String, String> args = new HashMap<String, String>();

		args.put("password", password);
		args.put("type", "fullsync");

		utils.doPost(Prefs.getScoutingURLNoDefault(getApplicationContext()),
				args, new SyncCallback());
	}

	private class ProcessData extends AsyncTask<String, Integer, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			// TODO
			return null;
		}

		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

		}

	}

	private class SyncCallback implements HttpCallback {

		public void onResponse(HttpRequestInfo resp) {
			// TODO Auto-generated method stub

		}

		public void onError(Exception e) {
			// TODO Auto-generated method stub

			mTimerTask.postDelayed(dataTask, DELAY);
		}
	}
	
	private class ChangeResponseCallback implements HttpCallback {

		public void onResponse(HttpRequestInfo resp) {
			// TODO Auto-generated method stub
			
			mTimerTask.postDelayed(dataTask, DELAY);
		}

		public void onError(Exception e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class ChangeResponseProcess extends AsyncTask<String, Integer, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			// TODO

			return null;
		}

		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

		}

	}

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

}
