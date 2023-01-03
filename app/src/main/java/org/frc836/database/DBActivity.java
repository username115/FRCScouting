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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import org.frc836.database.DBSyncService.LocalBinder;
import org.growingstems.scouting.MainMenuSelection;
import org.growingstems.scouting.ScoutingMenuActivity;

public abstract class DBActivity extends ScoutingMenuActivity implements ScoutingDBHelper.DBInstantiatedCallback {
    protected DB db;
    protected LocalBinder binder = null;
    protected ServiceWatcher watcher = new ServiceWatcher();
    protected ServiceConnection m_callback = null;

    private static boolean db_updated = false;
    private static ScoutingDBHelper.DBInstantiatedCallback callback;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback = this;
        initDB();
    }

    public void initDB() {
        Intent sync = new Intent(this, DBSyncService.class);
        bindService(sync, watcher, Context.BIND_AUTO_CREATE);
        db = new DB(this, binder);
        if (binder != null && db_updated) {
            binder.initSync();
            db_updated = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindDB();
    }

    public void unbindDB() {
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
                MainMenuSelection.setBinder(binder);
                if (m_callback != null)
                    m_callback.onServiceConnected(name, service);
                if (db_updated) {
                    binder.initSync();
                    db_updated = false;
                } else {
                    binder.startSync();
                }
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            if (m_callback != null)
                m_callback.onServiceDisconnected(name);
        }

    }

    public void dbInstantiated() {
        if (binder != null && db_updated) {
            binder.initSync();
            db_updated = false;
        }
    }

    public static void dbUpdated() {
        db_updated = true;
        if (callback != null)
            callback.dbInstantiated();
    }
}
