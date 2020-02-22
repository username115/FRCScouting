/*
 * Copyright 2016 Daniel Logan
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.frc836.database.DBSyncService.LocalBinder;
import org.frc836.database.FRCScoutingContract.EVENT_LU_Entry;
import org.frc836.database.FRCScoutingContract.NOTES_OPTIONS_Entry;
import org.frc836.database.FRCScoutingContract.POSITION_LU_Entry;
import org.frc836.database.FRCScoutingContract.ROBOT_LU_Entry;
import org.frc836.database.FRCScoutingContract.PROGRAMMING_LU_Entry;
import org.frc836.samsung.fileselector.FileOperation;
import org.frc836.samsung.fileselector.FileSelector;
import org.frc836.samsung.fileselector.OnHandleFileListener;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;
import org.growingstems.scouting.ScoutingMenuActivity;
import org.sigmond.net.*;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.SparseArray;
import android.widget.Toast;

import org.frc836.yearly.MatchStatsYearly;

public class DB {

    public static final boolean debug = false;
    // kinda dangerous, but we are assuming that the timestamp field will always
    // have the same name for all tables
    public static final String COLUMN_NAME_TIMESTAMP = PitStats.COLUMN_NAME_TIMESTAMP;

    private HttpUtils utils;
    private String password;
    private Context context;
    private LocalBinder binder;

    public static final SimpleDateFormat dateParser = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.sss", Locale.US);

    public enum RequestType {None, Matches}

    @SuppressWarnings("unused")
    private DB() {
    }

    public DB(Context context, String pass, LocalBinder binder) {
        utils = new HttpUtils();
        password = pass;
        this.context = context;
        //initialize the database if it isn't created already
        SQLiteDatabase tmp = ScoutingDBHelper.getInstance(context.getApplicationContext()).getReadableDatabase();
        tmp.close();
        this.binder = binder;
    }

    public DB(Context context, LocalBinder binder) {
        this.context = context;
        utils = new HttpUtils();
        password = Prefs.getSavedPassword(context);
        //initialize the database if it isn't created already
        SQLiteDatabase tmp = ScoutingDBHelper.getInstance(context.getApplicationContext()).getReadableDatabase();
        tmp.close();
        this.binder = binder;
    }

    public void setBinder(LocalBinder binder) {
        this.binder = binder;
    }

    protected static Map<String, String> getPostData(ContentValues values) {
        Map<String, String> data = new HashMap<String, String>();
        for (String key : values.keySet()) {
            data.put(key, values.getAsString(key));
        }
        return data;
    }

    public void startSync() {
        startSync(null);
    }

    public void startSync(SyncCallback callback) {
        if (binder != null) {
            binder.setCallback(callback);
            binder.setPassword(password);
            binder.startSync();
        }
    }

    private void insertOrUpdate(String table, String nullColumnHack,
                                ContentValues values, String idColumnName, String whereClause,
                                String[] whereArgs) {
        synchronized (ScoutingDBHelper.lock) {
            SQLiteDatabase db = ScoutingDBHelper.getInstance()
                    .getWritableDatabase();

            String[] projection = {idColumnName};

            Cursor c = db.query(table, projection, whereClause, whereArgs,
                    null, null, null, "0,1");
            try {
                if (c.moveToFirst()) {
                    String[] id = {c.getString(c
                            .getColumnIndexOrThrow(idColumnName))};
                    values.put(COLUMN_NAME_TIMESTAMP,
                            dateParser.format(new Date()));
                    db.update(table, values, idColumnName + "=?", id);
                } else {
                    db.insert(table, nullColumnHack, values);
                }
            } finally {
                if (c != null)
                    c.close();
                ScoutingDBHelper.getInstance().close();
            }
        }
    }

    public boolean submitMatch(MatchStatsStruct teamData) {
        try {
            String where = MatchStatsStruct.COLUMN_NAME_EVENT_ID + "=? AND "
                    + MatchStatsStruct.COLUMN_NAME_MATCH_ID + "=? AND "
                    + MatchStatsStruct.COLUMN_NAME_TEAM_ID + "=? AND "
                    + MatchStatsStruct.COLUMN_NAME_PRACTICE_MATCH + "=?";
            ContentValues values;
            synchronized (ScoutingDBHelper.lock) {
                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getReadableDatabase();

                values = teamData.getValues(this, db);

                ScoutingDBHelper.getInstance().close();
            }
            String[] whereArgs = {
                    values.getAsString(MatchStatsStruct.COLUMN_NAME_EVENT_ID),
                    values.getAsString(MatchStatsStruct.COLUMN_NAME_MATCH_ID),
                    values.getAsString(MatchStatsStruct.COLUMN_NAME_TEAM_ID),
                    values.getAsString(MatchStatsStruct.COLUMN_NAME_PRACTICE_MATCH)};

            insertOrUpdate(MatchStatsStruct.TABLE_NAME, null, values,
                    MatchStatsStruct.COLUMN_NAME_ID, where, whereArgs);

            startSync();

            return true;

        } catch (Exception e) {
            return false;
        }

    }

    public boolean submitPits(PitStats stats) {
        try {
            ContentValues values;
            synchronized (ScoutingDBHelper.lock) {
                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getWritableDatabase();
                values = stats.getValues(this, db);
                ScoutingDBHelper.getInstance().close();
            }

            String[] where = {values.getAsString(PitStats.COLUMN_NAME_TEAM_ID)};

            insertOrUpdate(PitStats.TABLE_NAME, null, values,
                    PitStats.COLUMN_NAME_ID, PitStats.COLUMN_NAME_TEAM_ID
                            + "=?", where);

            startSync();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private int getNextSortID(long eventID, SQLiteDatabase db) {
        String[] projection = {FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_SORT};
        String[] where = {String.valueOf(eventID), "0"};
        Cursor c = db.query(FRCScoutingContract.PICKLIST_Entry.TABLE_NAME, // from the event_lu
                // table
                projection, // select
                FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_EVENT_ID + "=? AND " +
                        FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_REMOVED + "=?", // where
                where,
                null, // don't group
                null, // don't filter
                FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_SORT, null);
        int ret = -1;
        try {
            if (c.getCount() > 0) {
                c.moveToLast();
                ret = c.getInt(c
                        .getColumnIndexOrThrow(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_SORT)) + 1;
            } else
                ret = 1;
        } finally {
            if (c != null)
                c.close();
        }

        return ret;
    }

    public boolean addTeamToPickList(int team, String eventName) {
        try {
            long eventID;
            int sortNum;
            synchronized (ScoutingDBHelper.lock) {
                SQLiteDatabase db = ScoutingDBHelper.getInstance().getWritableDatabase();
                eventID = getEventIDFromName(eventName, db);
                sortNum = getNextSortID(eventID, db);
                ScoutingDBHelper.getInstance().close();
            }

            ContentValues values = new ContentValues();
            values.put(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_TEAM_ID, team);
            values.put(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_EVENT_ID, eventID);
            values.put(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_REMOVED, 0);
            values.put(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_INVALID, 1);
            values.put(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_PICKED, 0);
            values.put(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_SORT, sortNum);
            values.put(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_ID, team * eventID);

            String[] where = {String.valueOf(team),
                    String.valueOf(eventID)};

            insertOrUpdate(FRCScoutingContract.PICKLIST_Entry.TABLE_NAME,
                    null, values, FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_ID,
                    FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_TEAM_ID + "=? AND " + FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_EVENT_ID + "=?",
                    where);

            startSync();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean removeTeamFromPickList(int team, String eventName) {
        try {
            long eventID;
            List<String> t = getPickList(eventName);
            if (t == null || !t.contains(String.valueOf(team)))
                return true;
            synchronized (ScoutingDBHelper.lock) {
                SQLiteDatabase db = ScoutingDBHelper.getInstance().getWritableDatabase();
                eventID = getEventIDFromName(eventName, db);
                ScoutingDBHelper.getInstance().close();
            }

            ContentValues values = new ContentValues();
            values.put(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_TEAM_ID, team);
            values.put(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_EVENT_ID, eventID);
            values.put(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_REMOVED, 1);
            values.put(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_INVALID, 1);
            values.put(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_PICKED, 0);

            String[] where = {String.valueOf(team),
                    String.valueOf(eventID)};

            insertOrUpdate(FRCScoutingContract.PICKLIST_Entry.TABLE_NAME,
                    null, values, FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_ID,
                    FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_TEAM_ID + "=? AND " + FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_EVENT_ID + "=?",
                    where);

            t.remove(t.indexOf(String.valueOf(team)));

            int i = 1;
            for (String teamNum : t) {
                updateSort(teamNum, i, eventID);
                i++;
            }

            startSync();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    private int getTeamPicked(int team, long eventID, SQLiteDatabase db) {
        String[] projection = {FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_PICKED};
        String[] where = {String.valueOf(team), String.valueOf(eventID)};
        Cursor c = db.query(FRCScoutingContract.PICKLIST_Entry.TABLE_NAME,
                projection, // select
                FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_TEAM_ID + "=? AND " + FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_EVENT_ID + "=?",
                where,
                null, // don't group
                null, // don't filter
                null, // don't order
                "0,1"); // limit to 1
        int ret = -1;
        try {
            c.moveToFirst();
            ret = c.getInt(c
                    .getColumnIndexOrThrow(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_PICKED));
        } finally {
            if (c != null)
                c.close();
        }

        return ret;
    }

    public boolean teamPickToggle(int team, String eventName) {
        try {
            long eventID;
            boolean picked;
            List<String> t = getPickList(eventName);
            if (t == null || !t.contains(String.valueOf(team)))
                return true;
            synchronized (ScoutingDBHelper.lock) {
                SQLiteDatabase db = ScoutingDBHelper.getInstance().getWritableDatabase();
                eventID = getEventIDFromName(eventName, db);
                picked = getTeamPicked(team, eventID, db) != 0;
                ScoutingDBHelper.getInstance().close();
            }

            ContentValues values = new ContentValues();
            values.put(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_INVALID, 1);
            values.put(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_PICKED, picked ? 0 : 1);

            String[] where = {String.valueOf(team),
                    String.valueOf(eventID)};

            insertOrUpdate(FRCScoutingContract.PICKLIST_Entry.TABLE_NAME,
                    null, values, FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_ID,
                    FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_TEAM_ID + "=? AND " + FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_EVENT_ID + "=?",
                    where);

            t.remove(t.indexOf(String.valueOf(team)));

            int i = 1;
            for (String teamNum : t) {
                updateSort(teamNum, i, eventID);
                i++;
            }

            startSync();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public void updateSort(List<String> teams, String eventName) {
        long eventID;
        synchronized (ScoutingDBHelper.lock) {
            SQLiteDatabase db = ScoutingDBHelper.getInstance().getWritableDatabase();
            eventID = getEventIDFromName(eventName, db);
            ScoutingDBHelper.getInstance().close();
        }

        int sort = 1;
        for (String team : teams) {
            updateSort(team, sort, eventID);
            sort++;
        }
    }

    private boolean updateSort(String team, int sort, long eventID) {
        try {
            ContentValues values = new ContentValues();
            values.put(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_TEAM_ID, Integer.valueOf(team));
            values.put(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_EVENT_ID, eventID);
            values.put(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_REMOVED, 0);
            values.put(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_INVALID, 1);
            values.put(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_PICKED, 0);
            values.put(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_SORT, sort);

            String[] where = {String.valueOf(team),
                    String.valueOf(eventID)};

            insertOrUpdate(FRCScoutingContract.PICKLIST_Entry.TABLE_NAME,
                    null, values, FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_ID,
                    FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_TEAM_ID + "=? AND " + FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_EVENT_ID + "=?",
                    where);

            startSync();
            return true;
        } catch (Exception e) {
            return false;
        }
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

    public String getTeamPitInfo(String teamNum) {

        synchronized (ScoutingDBHelper.lock) {
            try {

                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getReadableDatabase();
                String date = "";

                String[] projection = {PitStats.COLUMN_NAME_TIMESTAMP};
                String[] where = {teamNum};
                Cursor c = db.query(PitStats.TABLE_NAME, // from the
                        // scout_pit_data
                        // table
                        projection, // select
                        PitStats.COLUMN_NAME_TEAM_ID + "=?", // where
                        // team_id
                        // ==
                        where, // teamNum
                        null, // don't group
                        null, // don't filter
                        null, // don't order
                        "0,1"); // limit to 1

                try {
                    c.moveToFirst();

                    date = c.getString(c
                            .getColumnIndexOrThrow(PitStats.COLUMN_NAME_TIMESTAMP));
                } finally {
                    if (c != null)
                        c.close();
                    ScoutingDBHelper.getInstance().close();
                }
                return date;

            } catch (Exception e) {
                return "";
            }
        }
    }

    public List<String> getEventList() {

        synchronized (ScoutingDBHelper.lock) {
            try {
                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getReadableDatabase();

                String[] projection = {EVENT_LU_Entry.COLUMN_NAME_EVENT_NAME};

                Cursor c = db.query(EVENT_LU_Entry.TABLE_NAME, projection,
                        null, null, null, null, EVENT_LU_Entry.COLUMN_NAME_ID);
                List<String> ret;
                try {

                    ret = new ArrayList<String>(c.getCount());

                    if (c.moveToFirst())
                        do {
                            ret.add(c.getString(c
                                    .getColumnIndexOrThrow(EVENT_LU_Entry.COLUMN_NAME_EVENT_NAME)));
                        } while (c.moveToNext());
                    else
                        ret = null;
                } finally {
                    if (c != null)
                        c.close();
                    ScoutingDBHelper.getInstance().close();
                }

                return ret;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public List<String> getProgrammingList() {

        synchronized (ScoutingDBHelper.lock) {
            try {
                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getReadableDatabase();

                String[] projection = {PROGRAMMING_LU_Entry.COLUMN_NAME_LANGUAGE_NAME};

                Cursor c = db.query(PROGRAMMING_LU_Entry.TABLE_NAME, projection,
                        null, null, null, null,
						PROGRAMMING_LU_Entry.COLUMN_NAME_ID);
                List<String> ret;

                try {

                    ret = new ArrayList<String>(c.getCount());

                    if (c.moveToFirst())
                        do {
                            ret.add(c.getString(c
                                    .getColumnIndexOrThrow(PROGRAMMING_LU_Entry.COLUMN_NAME_LANGUAGE_NAME)));
                        } while (c.moveToNext());
                    else
                        ret = null;
                } finally {
                    if (c != null)
                        c.close();
                    ScoutingDBHelper.getInstance().close();
                }

                return ret;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public String getCodeFromEventName(String eventName) {
        synchronized (ScoutingDBHelper.lock) {
            try {
                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getReadableDatabase();
                String[] projection = {EVENT_LU_Entry.COLUMN_NAME_EVENT_CODE};
                String[] where = {eventName};
                Cursor c = db.query(EVENT_LU_Entry.TABLE_NAME, // from the
                        // event_lu
                        // table
                        projection, // select
                        EVENT_LU_Entry.COLUMN_NAME_EVENT_NAME + " LIKE ?", // where
                        // event_name
                        // ==
                        where, // EventName
                        null, // don't group
                        null, // don't filter
                        null, // don't order
                        "0,1"); // limit to 1
                String ret = "";
                try {
                    c.moveToFirst();
                    ret = c.getString(c
                            .getColumnIndexOrThrow(EVENT_LU_Entry.COLUMN_NAME_EVENT_CODE));
                } finally {
                    if (c != null)
                        c.close();
                    ScoutingDBHelper.getInstance().close();
                }
                return ret;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public List<String> getNotesOptions() {
        synchronized (ScoutingDBHelper.lock) {
            try {
                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getReadableDatabase();

                String[] projection = {NOTES_OPTIONS_Entry.COLUMN_NAME_OPTION_TEXT};

                Cursor c = db.query(NOTES_OPTIONS_Entry.TABLE_NAME, projection,
                        null, null, null, null,
                        NOTES_OPTIONS_Entry.COLUMN_NAME_ID);
                List<String> ret;
                try {

                    ret = new ArrayList<String>(c.getCount());

                    if (c.moveToFirst())
                        do {
                            ret.add(c.getString(c
                                    .getColumnIndexOrThrow(NOTES_OPTIONS_Entry.COLUMN_NAME_OPTION_TEXT)));
                        } while (c.moveToNext());
                    else
                        ret = null;
                } finally {
                    if (c != null)
                        c.close();
                    ScoutingDBHelper.getInstance().close();
                }

                return ret;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public List<String> getNotesForTeam(int team_id) {
        synchronized (ScoutingDBHelper.lock) {
            try {
                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getReadableDatabase();

                String[] projection = {MatchStatsStruct.COLUMN_NAME_NOTES};
                String selection = MatchStatsStruct.COLUMN_NAME_TEAM_ID + "=?";
                String[] selectionArgs = {String.valueOf(team_id)};

                Cursor c = db.query(MatchStatsStruct.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, MatchStatsStruct.COLUMN_NAME_ID);
                List<String> ret;
                try {
                    ret = new ArrayList<String>(c.getCount());

                    if (c.moveToFirst())
                        do {
                            String[] notes = c.getString(c.getColumnIndexOrThrow(MatchStatsStruct.COLUMN_NAME_NOTES)).split(";");
                            for (String note: notes) {
                                if (note.length() > 0 && !ret.contains(note))
                                    ret.add(note);
                            }
                        } while (c.moveToNext());
                    else
                        ret = null;
                } finally {
                    if (c != null)
                        c.close();
                    ScoutingDBHelper.getInstance().close();
                }

                return ret;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public List<String> getEventsWithData() {
        synchronized (ScoutingDBHelper.lock) {
            try {
                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getReadableDatabase();

                String[] projection = {
                        MatchStatsStruct.COLUMN_NAME_EVENT_ID,
                        "MAX(" + MatchStatsStruct.COLUMN_NAME_TIMESTAMP
                                + ") AS time"};

                Cursor c = db.query(MatchStatsStruct.TABLE_NAME, projection,
                        null, null, MatchStatsStruct.COLUMN_NAME_EVENT_ID,
                        null, "time");
                List<String> ret;
                try {

                    ret = new ArrayList<String>(c.getCount());

                    if (c.moveToFirst())
                        do {
                            ret.add(getEventNameFromID(
                                    c.getInt(c
                                            .getColumnIndexOrThrow(MatchStatsStruct.COLUMN_NAME_EVENT_ID)),
                                    db));
                        } while (c.moveToNext());
                    else
                        ret = null;
                } finally {
                    if (c != null)
                        c.close();
                    ScoutingDBHelper.getInstance().close();
                }

                return ret;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public List<String> getTeamsWithData() {
        return getTeamsWithData(null);
    }

    public List<String> getTeamsWithData(String eventName) {
        synchronized (ScoutingDBHelper.lock) {
            try {
                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getReadableDatabase();

                String[] projection = {MatchStatsStruct.COLUMN_NAME_TEAM_ID};

                String selection = null;
                String[] selectionArgs = null;

                if (eventName != null) {
                    selection = MatchStatsStruct.COLUMN_NAME_EVENT_ID + "=?";
                    selectionArgs = new String[1];
                    selectionArgs[0] = String.valueOf(getEventIDFromName(
                            eventName, db));
                }

                Cursor c = db.query(MatchStatsStruct.TABLE_NAME, projection,
                        selection, selectionArgs,
                        MatchStatsStruct.COLUMN_NAME_TEAM_ID, null,
                        MatchStatsStruct.COLUMN_NAME_TEAM_ID);
                List<Integer> teams;
                try {

                    teams = new ArrayList<Integer>(c.getCount());

                    if (c.moveToFirst())
                        do {
                            teams.add(c.getInt(c
                                    .getColumnIndexOrThrow(MatchStatsStruct.COLUMN_NAME_TEAM_ID)));
                        } while (c.moveToNext());

                    projection[0] = PitStats.COLUMN_NAME_TEAM_ID;

                    if (c != null)
                        c.close();

                    if (eventName == null) {

                        c = db.query(PitStats.TABLE_NAME, projection, null,
                                null, null, null, PitStats.COLUMN_NAME_TEAM_ID);
                        if (c.moveToFirst()) {
                            do {
                                int team = c
                                        .getInt(c
                                                .getColumnIndexOrThrow(PitStats.COLUMN_NAME_TEAM_ID));
                                if (!teams.contains(team)) {
                                    teams.add(team);
                                }
                            } while (c.moveToNext());
                        }
                    }

                    if (teams.isEmpty())
                        teams = null;

                } finally {
                    if (c != null)
                        c.close();
                    ScoutingDBHelper.getInstance().close();
                }

                if (teams != null) {
                    Collections.sort(teams);
                    List<String> ret = new ArrayList<String>(teams.size());
                    for (Integer team : teams) {
                        ret.add(team.toString());
                    }
                    return ret;
                } else
                    return null;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public List<Long> getEventIDsForTeam(int teamNum) {
        synchronized (ScoutingDBHelper.lock) {
            try {
                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getReadableDatabase();
                String[] projection = {
                        MatchStatsStruct.COLUMN_NAME_EVENT_ID,
                        "MAX(" + MatchStatsStruct.COLUMN_NAME_TIMESTAMP
                                + ") AS time"};

                String selection = MatchStatsStruct.COLUMN_NAME_TEAM_ID + "=?";
                String[] selectionArgs = {String.valueOf(teamNum)};

                Cursor c = db.query(MatchStatsStruct.TABLE_NAME, projection,
                        selection, selectionArgs,
                        MatchStatsStruct.COLUMN_NAME_EVENT_ID, null, "time");

                List<Long> ret;
                try {
                    ret = new ArrayList<Long>(c.getCount());

                    if (c.moveToFirst())
                        do {
                            ret.add(c.getLong(c.getColumnIndexOrThrow(MatchStatsStruct.COLUMN_NAME_EVENT_ID)));
                        } while (c.moveToNext());
                    else
                        ret = null;
                } finally {
                    if (c != null)
                        c.close();
                    ScoutingDBHelper.getInstance().close();
                }
                return ret;

            } catch (Exception e) {
                return null;
            }
        }
    }

    public List<String> getEventsForTeam(int teamNum) {
        synchronized (ScoutingDBHelper.lock) {
            try {
                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getReadableDatabase();
                String[] projection = {
                        MatchStatsStruct.COLUMN_NAME_EVENT_ID,
                        "MAX(" + MatchStatsStruct.COLUMN_NAME_TIMESTAMP
                                + ") AS time"};

                String selection = MatchStatsStruct.COLUMN_NAME_TEAM_ID + "=?";
                String[] selectionArgs = {String.valueOf(teamNum)};

                Cursor c = db.query(MatchStatsStruct.TABLE_NAME, projection,
                        selection, selectionArgs,
                        MatchStatsStruct.COLUMN_NAME_EVENT_ID, null, "time");

                List<String> ret;
                try {
                    ret = new ArrayList<String>(c.getCount());

                    if (c.moveToFirst())
                        do {
                            ret.add(getEventNameFromID(
                                    c.getInt(c
                                            .getColumnIndexOrThrow(MatchStatsStruct.COLUMN_NAME_EVENT_ID)),
                                    db));
                        } while (c.moveToNext());
                    else
                        ret = null;
                } finally {
                    if (c != null)
                        c.close();
                    ScoutingDBHelper.getInstance().close();
                }
                return ret;

            } catch (Exception e) {
                return null;
            }
        }
    }

    public List<String> getMatchesWithData(String eventName, boolean practice,
                                           int teamNum) {
        synchronized (ScoutingDBHelper.lock) {
            try {
                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getReadableDatabase();

                String[] projection = {MatchStatsStruct.COLUMN_NAME_MATCH_ID};

                List<String> args = new ArrayList<String>(3);

                String selection = "";
                String[] selectionArgs = new String[1];

                if (eventName != null) {
                    selection += MatchStatsStruct.COLUMN_NAME_EVENT_ID
                            + "=? AND ";
                    args.add(String.valueOf(getEventIDFromName(eventName, db)));
                }
                if (teamNum > 0) {
                    selection += MatchStatsStruct.COLUMN_NAME_TEAM_ID
                            + "=? AND ";
                    args.add(String.valueOf(teamNum));
                }

                selection += MatchStatsStruct.COLUMN_NAME_PRACTICE_MATCH + "=?";
                args.add(practice ? "1" : "0");
                selectionArgs = args.toArray(selectionArgs);

                Cursor c = db.query(MatchStatsStruct.TABLE_NAME, projection,
                        selection, selectionArgs,
                        MatchStatsStruct.COLUMN_NAME_MATCH_ID, null,
                        MatchStatsStruct.COLUMN_NAME_MATCH_ID);
                List<String> ret;
                try {

                    ret = new ArrayList<String>(c.getCount());

                    if (c.moveToFirst())
                        do {
                            ret.add(c.getString(c
                                    .getColumnIndexOrThrow(MatchStatsStruct.COLUMN_NAME_MATCH_ID)));
                        } while (c.moveToNext());
                    else
                        ret = null;
                } finally {
                    if (c != null)
                        c.close();
                    ScoutingDBHelper.getInstance().close();
                }

                return ret;
            } catch (Exception e) {
                return null;
            }
        }
    }

    private List<Integer> getMatchesWithData(long event_id, boolean practice,
                                             int teamNum) {
        synchronized (ScoutingDBHelper.lock) {
            try {
                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getReadableDatabase();

                String[] projection = {MatchStatsStruct.COLUMN_NAME_MATCH_ID};

                List<String> args = new ArrayList<String>(3);

                String selection = "";
                String[] selectionArgs = new String[1];

                if (event_id > 0) {
                    selection += MatchStatsStruct.COLUMN_NAME_EVENT_ID
                            + "=? AND ";
                    args.add(String.valueOf(event_id));
                }
                if (teamNum > 0) {
                    selection += MatchStatsStruct.COLUMN_NAME_TEAM_ID
                            + "=? AND ";
                    args.add(String.valueOf(teamNum));
                }

                selection += MatchStatsStruct.COLUMN_NAME_PRACTICE_MATCH + "=?";
                args.add(practice ? "1" : "0");
                selectionArgs = args.toArray(selectionArgs);

                Cursor c = db.query(MatchStatsStruct.TABLE_NAME, projection,
                        selection, selectionArgs,
                        MatchStatsStruct.COLUMN_NAME_MATCH_ID, null,
                        MatchStatsStruct.COLUMN_NAME_MATCH_ID);
                List<Integer> ret;
                try {

                    ret = new ArrayList<Integer>(c.getCount());

                    if (c.moveToFirst())
                        do {
                            ret.add(c.getInt(c
                                    .getColumnIndexOrThrow(MatchStatsStruct.COLUMN_NAME_MATCH_ID)));
                        } while (c.moveToNext());
                    else
                        ret = null;
                } finally {
                    if (c != null)
                        c.close();
                    ScoutingDBHelper.getInstance().close();
                }

                return ret;
            } catch (Exception e) {
                return null;
            }
        }
    }


    public String getPictureURL(int teamNum) {
        synchronized (ScoutingDBHelper.lock) {
            String ret = "";
            try {

                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getReadableDatabase();

                String[] projection = {ROBOT_LU_Entry.COLUMN_NAME_ROBOT_PHOTO};
                String[] where = {String.valueOf(teamNum)};
                Cursor c = db.query(ROBOT_LU_Entry.TABLE_NAME, // from the
                        // robot_lu
                        // table
                        projection, // select
                        ROBOT_LU_Entry.COLUMN_NAME_TEAM_ID + "=?", // where
                        // team_id
                        // ==
                        where, // teamNum
                        null, // don't group
                        null, // don't filter
                        null, // don't order
                        "0,1"); // limit to 1
                try {

                    if (c.moveToFirst()) {
                        ret = c.getString(c
                                .getColumnIndexOrThrow(ROBOT_LU_Entry.COLUMN_NAME_ROBOT_PHOTO));
                    }

                } finally {
                    if (c != null)
                        c.close();
                    ScoutingDBHelper.getInstance().close();
                }

                return ret;
            } catch (Exception e) {
                return null;
            }

        }
    }

    public PitStats getTeamPitStats(int teamNum) {

        synchronized (ScoutingDBHelper.lock) {

            try {
                PitStats stats = new PitStats();

                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getReadableDatabase();

                String[] projection = stats.getProjection();
                String[] where = {String.valueOf(teamNum)};
                Cursor c = db.query(PitStats.TABLE_NAME, // from the
                        // scout_pit_data
                        // table
                        projection, // select
                        PitStats.COLUMN_NAME_TEAM_ID + "=?", // where team_id ==
                        where, // teamNum
                        null, // don't group
                        null, // don't filter
                        null, // don't order
                        "0,1"); // limit to 1
                try {
                    if (c.getCount() > 0)
                        stats.fromCursor(c, this, db);

                } finally {
                    if (c != null)
                        c.close();
                    ScoutingDBHelper.getInstance().close();
                }

                return stats;
            } catch (Exception e) {
                return null;
            }

        }
    }

    public MatchStatsStruct getMatchStats(String eventName, int match,
                                          int team, boolean practice) {
        synchronized (ScoutingDBHelper.lock) {

            try {
                MatchStatsStruct stats = new MatchStatsStruct();

                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getReadableDatabase();

                String[] projection = stats.getProjection();
                String[] where = {String.valueOf(match),
                        String.valueOf(getEventIDFromName(eventName, db)),
                        String.valueOf(team), practice ? "1" : "0"};

                Cursor c = db.query(MatchStatsStruct.TABLE_NAME, projection,
                        MatchStatsStruct.COLUMN_NAME_MATCH_ID + "=? AND "
                                + MatchStatsStruct.COLUMN_NAME_EVENT_ID
                                + "=? AND "
                                + MatchStatsStruct.COLUMN_NAME_TEAM_ID
                                + "=? AND "
                                + MatchStatsStruct.COLUMN_NAME_PRACTICE_MATCH
                                + "=?", where, null, null, null, "0,1");

                stats.fromCursor(c, this, db);
                if (c != null)
                    c.close();
                ScoutingDBHelper.getInstance().close();

                return stats;

            } catch (Exception e) {
                return null;
            }

        }
    }

    public MatchStatsStruct getMatchStats(long event_id, int match,
                                          int team, boolean practice) {
        synchronized (ScoutingDBHelper.lock) {

            try {
                MatchStatsStruct stats = new MatchStatsStruct();

                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getReadableDatabase();

                String[] projection = stats.getProjection();
                String[] where = {String.valueOf(match),
                        String.valueOf(event_id),
                        String.valueOf(team), practice ? "1" : "0"};

                Cursor c = db.query(MatchStatsStruct.TABLE_NAME, projection,
                        MatchStatsStruct.COLUMN_NAME_MATCH_ID + "=? AND "
                                + MatchStatsStruct.COLUMN_NAME_EVENT_ID
                                + "=? AND "
                                + MatchStatsStruct.COLUMN_NAME_TEAM_ID
                                + "=? AND "
                                + MatchStatsStruct.COLUMN_NAME_PRACTICE_MATCH
                                + "=?", where, null, null, null, "0,1");

                stats.fromCursor(c, this, db);
                if (c != null)
                    c.close();
                ScoutingDBHelper.getInstance().close();

                return stats;

            } catch (Exception e) {
                return null;
            }

        }
    }

    public List<String> getTeamsForMatch(String eventName, int match,
                                         boolean practice) {
        synchronized (ScoutingDBHelper.lock) {

            try {

                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getReadableDatabase();

                String[] projection = {MatchStatsStruct.COLUMN_NAME_TEAM_ID};
                String[] where = {String.valueOf(match),
                        String.valueOf(getEventIDFromName(eventName, db)),
                        practice ? "1" : "0"};

                Cursor c = db.query(MatchStatsStruct.TABLE_NAME, projection,
                        MatchStatsStruct.COLUMN_NAME_MATCH_ID + "=? AND "
                                + MatchStatsStruct.COLUMN_NAME_EVENT_ID
                                + "=? AND "
                                + MatchStatsStruct.COLUMN_NAME_PRACTICE_MATCH
                                + "=?", where, null, null, MatchStatsStruct.COLUMN_NAME_POSITION_ID);
                List<String> ret;
                try {

                    ret = new ArrayList<String>(c.getCount());

                    if (c.moveToFirst())
                        do {
                            ret.add(c.getString(c
                                    .getColumnIndexOrThrow(MatchStatsStruct.COLUMN_NAME_TEAM_ID)));
                        } while (c.moveToNext());
                    else
                        ret = null;
                } finally {
                    if (c != null)
                        c.close();
                    ScoutingDBHelper.getInstance().close();
                }

                return ret;

            } catch (Exception e) {
                return null;
            }

        }
    }

    public Cursor getPickListCursor(String eventName, SQLiteDatabase db) {
        synchronized (ScoutingDBHelper.lock) {

            try {
                String[] projection = {FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_SORT, FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_TEAM_ID, FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_ID + " AS _id", FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_PICKED};
                String[] where = {
                        String.valueOf(getEventIDFromName(eventName, db)), "0"};

                Cursor c = db.query(FRCScoutingContract.PICKLIST_Entry.TABLE_NAME, projection,
                        FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_EVENT_ID
                                + "=? AND " + FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_REMOVED + "=?",
                        where, null, null, FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_SORT);


                return c;

            } catch (Exception e) {
                return null;
            }

        }
    }

    public List<String> getPickList(String eventName) {
        synchronized (ScoutingDBHelper.lock) {

            try {

                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getReadableDatabase();

                String[] projection = {FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_TEAM_ID};
                String[] where = {
                        String.valueOf(getEventIDFromName(eventName, db)), "0"};

                Cursor c = db.query(FRCScoutingContract.PICKLIST_Entry.TABLE_NAME, projection,
                        FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_EVENT_ID
                                + "=? AND " + FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_REMOVED + "=?",
                        where, null, null, FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_SORT);
                List<String> ret;
                try {

                    ret = new ArrayList<String>(c.getCount());

                    if (c.moveToFirst())
                        do {
                            ret.add(c.getString(c
                                    .getColumnIndexOrThrow(FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_TEAM_ID)));
                        } while (c.moveToNext());
                    else
                        ret = null;
                } finally {
                    if (c != null)
                        c.close();
                    ScoutingDBHelper.getInstance().close();
                }

                return ret;

            } catch (Exception e) {
                return null;
            }

        }
    }

    public String getPosition(String eventName, int match,
                              boolean practice, int team) {
        synchronized (ScoutingDBHelper.lock) {

            try {

                SQLiteDatabase db = ScoutingDBHelper.getInstance()
                        .getReadableDatabase();

                String[] projection = {MatchStatsStruct.COLUMN_NAME_POSITION_ID};
                String[] where = {String.valueOf(match),
                        String.valueOf(getEventIDFromName(eventName, db)),
                        practice ? "1" : "0",
                        String.valueOf(team)};

                Cursor c = db.query(MatchStatsStruct.TABLE_NAME, projection,
                        MatchStatsStruct.COLUMN_NAME_MATCH_ID + "=? AND "
                                + MatchStatsStruct.COLUMN_NAME_EVENT_ID
                                + "=? AND "
                                + MatchStatsStruct.COLUMN_NAME_PRACTICE_MATCH
                                + "=? AND "
                                + MatchStatsStruct.COLUMN_NAME_TEAM_ID
                                + "=?", where, null, null, null, "0,1");
                String ret;
                try {

                    if (c.moveToFirst())
                        ret = getPosNameFromID(c.getInt(c.getColumnIndexOrThrow(MatchStatsStruct.COLUMN_NAME_POSITION_ID)), db);
                    else
                        ret = null;
                } finally {
                    if (c != null)
                        c.close();
                    ScoutingDBHelper.getInstance().close();
                }

                return ret;

            } catch (Exception e) {
                return null;
            }

        }
    }

    public long getEventIDFromName(String eventName, SQLiteDatabase db) {

        String[] projection = {EVENT_LU_Entry.COLUMN_NAME_ID};
        String[] where = {eventName};
        Cursor c = db.query(EVENT_LU_Entry.TABLE_NAME, // from the event_lu
                // table
                projection, // select
                EVENT_LU_Entry.COLUMN_NAME_EVENT_NAME + " LIKE ?", // where
                // event_name
                // ==
                where, // EventName
                null, // don't group
                null, // don't filter
                null, // don't order
                "0,1"); // limit to 1
        long ret = -1;
        try {
            c.moveToFirst();
            ret = c.getLong(c
                    .getColumnIndexOrThrow(EVENT_LU_Entry.COLUMN_NAME_ID));
        } finally {
            if (c != null)
                c.close();
        }

        return ret;
    }

    public long getPosIDFromName(String position, SQLiteDatabase db) {

        String[] projection = {POSITION_LU_Entry.COLUMN_NAME_ID};
        String[] where = {position};
        Cursor c = db.query(POSITION_LU_Entry.TABLE_NAME, // from the event_lu
                // table
                projection, // select
                POSITION_LU_Entry.COLUMN_NAME_POSITION + " LIKE ?", // where
                // event_name
                // ==
                where, // EventName
                null, // don't group
                null, // don't filter
                null, // don't order
                "0,1"); // limit to 1
        long ret = -1;
        try {
            c.moveToFirst();
            ret = c.getLong(c
                    .getColumnIndexOrThrow(POSITION_LU_Entry.COLUMN_NAME_ID));
        } finally {
            if (c != null)
                c.close();
        }

        return ret;
    }

    public long getProgrammingIDFromName(String language, SQLiteDatabase db) {

        String[] projection = {PROGRAMMING_LU_Entry.COLUMN_NAME_ID};
        String[] where = {language};
        Cursor c = db.query(PROGRAMMING_LU_Entry.TABLE_NAME,
                projection, // select
				PROGRAMMING_LU_Entry.COLUMN_NAME_LANGUAGE_NAME + " LIKE ?",
                where, // EventName
                null, // don't group
                null, // don't filter
                null, // don't order
                "0,1"); // limit to 1
        long ret = -1;
        try {
            c.moveToFirst();
            ret = c.getLong(c
                    .getColumnIndexOrThrow(PROGRAMMING_LU_Entry.COLUMN_NAME_ID));
        } finally {
            if (c != null)
                c.close();
        }
        return ret;
    }

    public static String getProgrammingNameFromID(int language, SQLiteDatabase db) {

        String[] projection = {PROGRAMMING_LU_Entry.COLUMN_NAME_LANGUAGE_NAME};
        String[] where = {String.valueOf(language)};
        Cursor c = db.query(PROGRAMMING_LU_Entry.TABLE_NAME, projection, // select
				PROGRAMMING_LU_Entry.COLUMN_NAME_ID + "= ?", where, // EventName
                null, // don't group
                null, // don't filter
                null, // don't order
                "0,1"); // limit to 1
        String ret = "";
        try {
            c.moveToFirst();
            ret = c.getString(c
                    .getColumnIndexOrThrow(PROGRAMMING_LU_Entry.COLUMN_NAME_LANGUAGE_NAME));
        } finally {
            if (c != null)
                c.close();
        }
        return ret;
    }

    public static String getEventNameFromID(int eventId, SQLiteDatabase db) {

        String[] projection = {EVENT_LU_Entry.COLUMN_NAME_EVENT_NAME};
        String[] where = {String.valueOf(eventId)};
        Cursor c = db.query(EVENT_LU_Entry.TABLE_NAME, projection, // select
                EVENT_LU_Entry.COLUMN_NAME_ID + "= ?", where, // EventName
                null, // don't group
                null, // don't filter
                null, // don't order
                "0,1"); // limit to 1
        String ret = "";
        try {
            c.moveToFirst();
            ret = c.getString(c
                    .getColumnIndexOrThrow(EVENT_LU_Entry.COLUMN_NAME_EVENT_NAME));
        } finally {
            if (c != null)
                c.close();
        }
        return ret;
    }

    public static String getEventCodeFromID(int eventId, SQLiteDatabase db) {

        String[] projection = {EVENT_LU_Entry.COLUMN_NAME_EVENT_CODE};
        String[] where = {String.valueOf(eventId)};
        Cursor c = db.query(EVENT_LU_Entry.TABLE_NAME, projection, // select
                EVENT_LU_Entry.COLUMN_NAME_ID + "= ?", where, // EventName
                null, // don't group
                null, // don't filter
                null, // don't order
                "0,1"); // limit to 1
        String ret = "";
        try {
            c.moveToFirst();
            ret = c.getString(c
                    .getColumnIndexOrThrow(EVENT_LU_Entry.COLUMN_NAME_EVENT_CODE));
        } finally {
            if (c != null)
                c.close();
        }
        return ret;
    }

    public static String getPosNameFromID(int posId, SQLiteDatabase db) {

        String[] projection = {POSITION_LU_Entry.COLUMN_NAME_POSITION};
        String[] where = {String.valueOf(posId)};
        Cursor c = db.query(POSITION_LU_Entry.TABLE_NAME, projection, // select
                POSITION_LU_Entry.COLUMN_NAME_ID + "= ?", where, // EventName
                null, // don't group
                null, // don't filter
                null, // don't order
                "0,1"); // limit to 1
        String ret = "";
        try {
            c.moveToFirst();
            ret = c.getString(c
                    .getColumnIndexOrThrow(POSITION_LU_Entry.COLUMN_NAME_POSITION));
        } finally {
            if (c != null)
                c.close();
        }
        return ret;
    }

    public static int getGameInfoInt(String key, SQLiteDatabase db, int defaultValue) {

        String[] projection = {FRCScoutingContract.GAME_INFO_Entry.COLUMN_NAME_INTVALUE};
        String[] where = {key};
        Cursor c = db.query(FRCScoutingContract.GAME_INFO_Entry.TABLE_NAME, projection, // select
                FRCScoutingContract.GAME_INFO_Entry.COLUMN_NAME_KEYSTRING + "= ?", where, // Key
                null, // don't group
                null, // don't filter
                null, // don't order
                "0,1"); // limit to 1
        int ret = defaultValue;
        try {
            c.moveToFirst();
            ret = c.getInt(c
                    .getColumnIndexOrThrow(FRCScoutingContract.GAME_INFO_Entry.COLUMN_NAME_INTVALUE));
        } finally {
            if (c != null)
                c.close();
        }
        return ret;
    }

    public static String getGameInfoString(String key, SQLiteDatabase db, String defaultValue) {

        String[] projection = {FRCScoutingContract.GAME_INFO_Entry.COLUMN_NAME_STRINGVAL};
        String[] where = {key};
        Cursor c = db.query(FRCScoutingContract.GAME_INFO_Entry.TABLE_NAME, projection, // select
                FRCScoutingContract.GAME_INFO_Entry.COLUMN_NAME_KEYSTRING + "= ?", where, // Key
                null, // don't group
                null, // don't filter
                null, // don't order
                "0,1"); // limit to 1
        String ret = defaultValue;
        try {
            c.moveToFirst();
            ret = c.getString(c
                    .getColumnIndexOrThrow(FRCScoutingContract.GAME_INFO_Entry.COLUMN_NAME_STRINGVAL));
        } finally {
            if (c != null)
                c.close();
        }
        return ret;
    }


    static OnHandleFileListener mDirSelectListener = new OnHandleFileListener() {
        @Override
        public void handleFile(String filePath) {
            cb.filename = filePath;
            CSVExporter export = new CSVExporter();
            export.execute(cb);
        }
    };

    static ExportCallback cb;

    private static final String FILENAME = "ScoutingDefaultExportLocation";

    public static void exportToCSV(ScoutingMenuActivity context) {
        try {
            cb = new ExportCallback();
            String filename;
            try {
                BufferedInputStream bis = new BufferedInputStream(
                        context.openFileInput(FILENAME));
                byte[] buffer = new byte[bis.available()];
                bis.read(buffer, 0, buffer.length);
                filename = new String(buffer);
            } catch (Exception e) {
                filename = null;
            }

            cb.context = context;

            new FileSelector(context, FileOperation.SELECTDIR,
                    mDirSelectListener, null, filename).show();

        } catch (Exception e) {
            Toast.makeText(context, "Error exporting Database",
                    Toast.LENGTH_LONG).show();
        }
    }

    private static class ExportCallback {
        Context context;
        String filename;

        public void finish(String result) {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        }
    }

    private static class CSVExporter extends
            AsyncTask<ExportCallback, Integer, String> {

        ExportCallback callback;
        private static final int notifyId = 87492;

        @Override
        protected String doInBackground(ExportCallback... params) {
            synchronized (ScoutingDBHelper.lock) {
                try {
                    callback = params[0];
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                            callback.context)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle("Exporting Scouting Data")
                            .setContentText("to " + callback.filename)
                            .setProgress(300, 0, false); //2017

                    NotificationManager notManager = ((NotificationManager) callback.context
                            .getSystemService(Context.NOTIFICATION_SERVICE));
                    notManager.notify(notifyId, mBuilder.build());

                    SQLiteDatabase db = ScoutingDBHelper.getInstance()
                            .getReadableDatabase();

                    SparseArray<String> configs = new SparseArray<String>();
                    SparseArray<String> types = new SparseArray<String>();
                    SparseArray<String> bases = new SparseArray<String>();
                    SparseArray<String> events = new SparseArray<String>();
                    SparseArray<String> positions = new SparseArray<String>();
                    SparseArray<String> defenses = new SparseArray<String>();
                    defenses.put(0, "None");

                    Cursor c = null;
                    StringBuilder match_data = null, pit_data = null;
                    // export matches
                    try {

                        c = db.rawQuery("SELECT * FROM "
                                + MatchStatsStruct.TABLE_NAME, null);
                        // decent estimate for how big the output will be. will
                        // definitely be too small, but will keep it from having
                        // to resize too many times
                        match_data = new StringBuilder(c.getCount()
                                * c.getColumnCount() * 2);

                        for (int i = 0; i < c.getColumnCount(); i++) {
                            if (i > 0)
                                match_data.append(",");
                            if (MatchStatsStruct.COLUMN_NAME_INVALID
                                    .equalsIgnoreCase(c.getColumnName(i))
                                    && !debug)
                                i++;
                            if (MatchStatsStruct.COLUMN_NAME_EVENT_ID
                                    .equalsIgnoreCase(c.getColumnName(i)))
                                match_data
                                        .append(EVENT_LU_Entry.COLUMN_NAME_EVENT_CODE);
                            else if (MatchStatsStruct.COLUMN_NAME_POSITION_ID
                                    .equalsIgnoreCase(c.getColumnName(i)))
                                match_data
                                        .append(POSITION_LU_Entry.COLUMN_NAME_POSITION);
                            else
                                match_data.append(c.getColumnName(i));
                        }
                        match_data.append("\n");
                        int rowCount = c.getCount();
                        int progress = 0;
                        if (c.moveToFirst())
                            do {
                                for (int j = 0; j < c.getColumnCount(); j++) {
                                    if (j > 0)
                                        match_data.append(",");
                                    if (MatchStatsStruct.COLUMN_NAME_INVALID
                                            .equalsIgnoreCase(c
                                                    .getColumnName(j))
                                            && !debug)
                                        j++;
                                    if (new MatchStatsStruct()
                                            .isTextField(c.getColumnName(j)))
                                        match_data.append("\"")
                                                .append(c.getString(j).replace("\"", "'"))
                                                .append("\"");
                                    else if (MatchStatsStruct.COLUMN_NAME_EVENT_ID
                                            .equalsIgnoreCase(c
                                                    .getColumnName(j))) {
                                        String event = events.get(c.getInt(j));
                                        if (event == null) {
                                            event = getEventCodeFromID(
                                                    c.getInt(j), db);
                                            events.append(c.getInt(j), event);
                                        }
                                        match_data.append(event);
                                    } else if (MatchStatsStruct.COLUMN_NAME_POSITION_ID
                                            .equalsIgnoreCase(c
                                                    .getColumnName(j))) {
                                        String position = positions.get(c
                                                .getInt(j));
                                        if (position == null) {
                                            position = getPosNameFromID(
                                                    c.getInt(j), db);
                                            positions.append(c.getInt(j),
                                                    position);
                                        }
                                        match_data.append(position);
                                    } else
                                        match_data.append(c.getString(j));
                                }
                                match_data.append("\n");
                                progress++;
                                mBuilder.setProgress(300,
                                        (int) (((double) progress)
                                                / ((double) rowCount) * 100), //2017
                                        false);
                                notManager.notify(notifyId, mBuilder.build());

                            } while (c.moveToNext());
                    } finally {
                        if (c != null)
                            c.close();
                        ScoutingDBHelper.getInstance().close();
                    }

                    // export pits
                    db = ScoutingDBHelper.getInstance().getReadableDatabase();
                    try {
                        c = db.rawQuery("SELECT * FROM " + PitStats.TABLE_NAME,
                                null);
                        pit_data = new StringBuilder(c.getCount()
                                * c.getColumnCount() * 2);
                        for (int i = 0; i < c.getColumnCount(); i++) {
                            if (i > 0)
                                pit_data.append(",");
                            if (PitStats.COLUMN_NAME_INVALID.equalsIgnoreCase(c
                                    .getColumnName(i)) && !debug)
                                i++;
                            if (PitStats.COLUMN_NAME_PROGRAMMING_ID
                                    .equalsIgnoreCase(c.getColumnName(i)))
                                pit_data.append("programming_language");
                            else
                                pit_data.append(c.getColumnName(i));
                        }
                        pit_data.append("\n");
                        int rowCount = c.getCount();
                        int progress = 0;
                        if (c.moveToFirst()) {
                            do {
                                for (int j = 0; j < c.getColumnCount(); j++) {
                                    if (j > 0)
                                        pit_data.append(",");
                                    if (PitStats.COLUMN_NAME_INVALID
                                            .equalsIgnoreCase(c
                                                    .getColumnName(j))
                                            && !debug)
                                        j++;
                                    if (new PitStats().isTextField(
                                            c.getColumnName(j)))
                                        pit_data.append("\"")
                                                .append(c.getString(j).replace("\"", "'"))
                                                .append("\"");
                                        // wanted to encapsulate the following, but
                                        // doing so would slow down the export.
                                    else if (PitStats.COLUMN_NAME_PROGRAMMING_ID
                                            .equalsIgnoreCase(c
                                                    .getColumnName(j))) {
                                        String config = configs
                                                .get(c.getInt(j));
                                        if (config == null) {
                                            config = getProgrammingNameFromID(
                                                    c.getInt(j), db);
                                            configs.append(c.getInt(j), config);
                                        }
                                        pit_data.append(config);
                                    } else
                                        pit_data.append(c.getString(j));
                                }
                                pit_data.append("\n");
                                progress++;
                                mBuilder.setProgress(
                                        300,
                                        (int) (((double) progress)
                                                / ((double) rowCount) * 100) + 200, //2017
                                        false);
                                notManager.notify(notifyId, mBuilder.build());
                            } while (c.moveToNext());
                        }
                    } finally {
                        if (c != null)
                            c.close();
                        ScoutingDBHelper.getInstance().close();
                    }

                    File sd = new File(callback.filename);
                    String filename_append = Prefs.getDeviceName(callback.context, "");
                    if (filename_append.length() > 0)
                        filename_append = "_" + filename_append;
                    File match = new File(sd, "matches" + filename_append + ".csv");
                    File pits = new File(sd, "pits" + filename_append + ".csv");
                    FileOutputStream destination;
                    if (match_data != null) {
                        destination = new FileOutputStream(match);
                        destination.write(match_data.toString().getBytes());
                        destination.close();
                    }
                    if (pit_data != null) {
                        destination = new FileOutputStream(pits);
                        destination.write(pit_data.toString().getBytes());
                        destination.close();
                    }
                    try {
                        FileOutputStream fos = callback.context.openFileOutput(
                                FILENAME, Context.MODE_PRIVATE);
                        fos.write(callback.filename.getBytes());
                        fos.close();
                    } catch (Exception e) {

                    }
                    try {
                        MediaScannerConnection.scanFile(callback.context, new String[]{match.getPath(), pits.getPath()}, null, null );
                    }
                    catch (Exception e) {

                    }
                    mBuilder.setProgress(0, 0, false)
                            .setContentTitle("Export Complete")
                            .setContentText(callback.filename);
                    notManager.notify(notifyId, mBuilder.build());
                    return "DB exported to " + sd.getAbsolutePath();
                } catch (Exception e) {
                    ScoutingDBHelper.getInstance().close();
                    return "Error during export";
                }
            }
        }

        protected void onPostExecute(String result) {
            callback.finish(result);
            callback = null;
        }

    }

    public interface SyncCallback {
        void onFinish();
    }

    public interface DBCallback {
        void onFinish(DBData data);
    }

    public class DBData {
        protected RequestType _type = RequestType.None;

        protected DBCallback _callback;

        protected int _teamNum = -1; //Matches
        protected String _eventName = null; //Matches

        protected Map<String, SparseArray<MatchStatsStruct>> _matches; //eventname to matchlist

        public DBData(RequestType type, DBCallback callback) {
            _type = type;
            _callback = callback;
        }

        public int getTeamNum() {
            return _teamNum;
        }

        public String getEventName() {
            return _eventName;
        }

        public Map<String, SparseArray<MatchStatsStruct>> getMatches() {
            return _matches;
        }
    }

    public void getMatchesForTeam(int team, String eventName, DBCallback callback) {
        DBData dat = new DBData(RequestType.Matches, callback);
        dat._teamNum = team;
        dat._eventName = eventName;
        (new MatchesAsync()).execute(dat);
    }

    private class MatchesAsync extends AsyncTask<DBData, Integer, DBData> {


        @Override
        protected DBData doInBackground(DBData... params) {
            if (params[0] == null || params[0]._type != RequestType.Matches)
                return null;
            if (params[0]._teamNum > 0) {
                List<Long> eventList;
                if (params[0]._eventName != null) {
                    synchronized (ScoutingDBHelper.lock) {
                        try {
                            SQLiteDatabase db = ScoutingDBHelper.getInstance().getReadableDatabase();
                            eventList = new ArrayList<Long>(1);
                            eventList.add(getEventIDFromName(params[0]._eventName, db));
                        } finally {
                            ScoutingDBHelper.getInstance().close();
                        }
                    }
                } else {
                    eventList = getEventIDsForTeam(params[0]._teamNum);
                }

                if (eventList == null || eventList.isEmpty()) {
                    return null;
                }

                params[0]._matches = new HashMap<String, SparseArray<MatchStatsStruct>>(eventList.size());

                for (Long id : eventList) {
                    SparseArray<MatchStatsStruct> matches = new SparseArray<MatchStatsStruct>();

                    List<Integer> matchList = getMatchesWithData(id, false, params[0]._teamNum);

                    if (matchList == null)
                        continue;
                    for (Integer match : matchList) {
                        matches.append(match, getMatchStats(id, match, params[0]._teamNum, false));
                    }

                    synchronized (ScoutingDBHelper.lock) {
                        try {
                            SQLiteDatabase db = ScoutingDBHelper.getInstance().getReadableDatabase();
                            params[0]._matches.put(getEventNameFromID(id.intValue(), db), matches);
                        } finally {
                            ScoutingDBHelper.getInstance().close();
                        }
                    }
                }
                return params[0];
            }
            return null;
        }

        protected void onPostExecute(DBData data) {
            if (data != null && data._callback != null)
                data._callback.onFinish(data);
        }
    }

}
