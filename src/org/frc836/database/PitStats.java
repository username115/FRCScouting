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

import org.frc836.database.FRCScoutingContract.SCOUT_PIT_DATA_2015_Entry;
import org.robobees.recyclerush.PitStatsRR;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class PitStats {

	public int team;
	public String chassis_config;
	public String wheel_type;
	public String wheel_base;
	public boolean auto_mode;
	public String comments;

	public static final String COLUMN_NAME_TEAM_ID = SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_TEAM_ID;
	public static final String TABLE_NAME = SCOUT_PIT_DATA_2015_Entry.TABLE_NAME;
	public static final String COLUMN_NAME_ID = SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_ID;
	public static final String COLUMN_NAME_TIMESTAMP = SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_TIMESTAMP;
	public static final String COLUMN_NAME_INVALID = SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_INVALID;
	public static final String COLUMN_NAME_CONFIG_ID = SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_CONFIG_ID;
	public static final String COLUMN_NAME_WHEEL_BASE_ID = SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_WHEEL_BASE_ID;
	public static final String COLUMN_NAME_WHEEL_TYPE_ID = SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_WHEEL_TYPE_ID;

	public static PitStats getNewPitStats() {
		return new PitStatsRR();
	}

	public PitStats() {
		init();
	}

	public void init() {
		team = 0;
		chassis_config = "other";
		wheel_type = "other";
		wheel_base = "other";
		auto_mode = false;
		comments = "";
	}

	public ContentValues getValues(DB db, SQLiteDatabase database) {
		ContentValues args = new ContentValues();

		args.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_ID, team * team);
		args.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_TEAM_ID, team);
		args.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_CONFIG_ID,
				db.getConfigIDFromName(chassis_config, database));
		args.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_WHEEL_TYPE_ID,
				db.getWheelTypeIDFromName(wheel_type, database));
		args.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_WHEEL_BASE_ID,
				db.getWheelBaseIDFromName(wheel_base, database));
		// args.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTONOMOUS_MODE,
		// auto_mode ? 1 : 0);
		args.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_NOTES, comments);
		args.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_INVALID, 1);

		return args;
	}

	public void fromCursor(Cursor c, DB db, SQLiteDatabase database) {
		c.moveToFirst();

		team = c.getInt(c
				.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_TEAM_ID));
		chassis_config = DB
				.getConfigNameFromID(
						c.getInt(c
								.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_CONFIG_ID)),
						database);
		wheel_type = DB
				.getWheelTypeNameFromID(
						c.getInt(c
								.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_WHEEL_TYPE_ID)),
						database);
		wheel_base = DB
				.getWheelBaseNameFromID(
						c.getInt(c
								.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_WHEEL_BASE_ID)),
						database);
		// auto_mode = c
		// .getInt(c
		// .getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTONOMOUS_MODE))
		// != 0;
		comments = c
				.getString(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_NOTES));
	}

	public String[] getProjection() {
		String[] projection = { SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_TEAM_ID,
				SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_CONFIG_ID,
				SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_WHEEL_TYPE_ID,
				SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_WHEEL_BASE_ID,
				SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_NOTES };
		return projection;
	}
	
	public boolean isTextField(String column_name) {
		return SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_NOTES.equalsIgnoreCase(column_name);
	}
	
	public boolean needsConvertedToText(String column_name) {
		return SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_CONFIG_ID.equalsIgnoreCase(column_name) ||
				SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_WHEEL_BASE_ID.equalsIgnoreCase(column_name) ||
				SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_WHEEL_TYPE_ID.equalsIgnoreCase(column_name);
	}
}
