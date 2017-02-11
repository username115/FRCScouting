/*
-----------------------------------------------------
StatsStructGen.py 1.1

This file was autogenerated with run cmd:
  "gen_scripts/StatsStructGen.py --packagename=org.frc836.database --classname=PilotStatsStruct --tablename=fact_pilot_data_2017 --infile=FRC_Scouting_Server/scouting.sql --outfile=app/src/main/java/org/frc836/database/PilotStatsStruct.java"

python version info:
  3.4.2 (default, Oct  8 2014, 10:45:20) 
[GCC 4.9.1]

Please take heed of modifying unnecessarily
-----------------------------------------------------
*/

package org.frc836.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.frc836.database.FRCScoutingContract.FACT_PILOT_DATA_2017_Entry;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PilotStatsStruct {


	public String event_id;
	public int team_id;
	public int match_id;
	public boolean practice_match;
	public String position_id;
	public int gears_installed_2;
	public int gears_installed_3;
	public int gears_installed_4;
	public int gears_lifed;
	public boolean rotor_1_started;
	public boolean rotor_2_started;
	public boolean rotor_3_started;
	public boolean rotor_4_started;
	public boolean foul;
	public boolean yellow_card;
	public boolean red_card;
	public String notes;
	

	public static final String TABLE_NAME = FACT_PILOT_DATA_2017_Entry.TABLE_NAME;
	public static final String COLUMN_NAME_ID = FACT_PILOT_DATA_2017_Entry.COLUMN_NAME_ID;
	public static final String COLUMN_NAME_EVENT_ID = FACT_PILOT_DATA_2017_Entry.COLUMN_NAME_EVENT_ID;
	public static final String COLUMN_NAME_TEAM_ID = FACT_PILOT_DATA_2017_Entry.COLUMN_NAME_TEAM_ID;
	public static final String COLUMN_NAME_MATCH_ID = FACT_PILOT_DATA_2017_Entry.COLUMN_NAME_MATCH_ID;
	public static final String COLUMN_NAME_PRACTICE_MATCH = FACT_PILOT_DATA_2017_Entry.COLUMN_NAME_PRACTICE_MATCH;
	public static final String COLUMN_NAME_POSITION_ID = FACT_PILOT_DATA_2017_Entry.COLUMN_NAME_POSITION_ID;
	public static final String COLUMN_NAME_GEARS_INSTALLED_2 = FACT_PILOT_DATA_2017_Entry.COLUMN_NAME_GEARS_INSTALLED_2;
	public static final String COLUMN_NAME_GEARS_INSTALLED_3 = FACT_PILOT_DATA_2017_Entry.COLUMN_NAME_GEARS_INSTALLED_3;
	public static final String COLUMN_NAME_GEARS_INSTALLED_4 = FACT_PILOT_DATA_2017_Entry.COLUMN_NAME_GEARS_INSTALLED_4;
	public static final String COLUMN_NAME_GEARS_LIFED = FACT_PILOT_DATA_2017_Entry.COLUMN_NAME_GEARS_LIFED;
	public static final String COLUMN_NAME_ROTOR_1_STARTED = FACT_PILOT_DATA_2017_Entry.COLUMN_NAME_ROTOR_1_STARTED;
	public static final String COLUMN_NAME_ROTOR_2_STARTED = FACT_PILOT_DATA_2017_Entry.COLUMN_NAME_ROTOR_2_STARTED;
	public static final String COLUMN_NAME_ROTOR_3_STARTED = FACT_PILOT_DATA_2017_Entry.COLUMN_NAME_ROTOR_3_STARTED;
	public static final String COLUMN_NAME_ROTOR_4_STARTED = FACT_PILOT_DATA_2017_Entry.COLUMN_NAME_ROTOR_4_STARTED;
	public static final String COLUMN_NAME_FOUL = FACT_PILOT_DATA_2017_Entry.COLUMN_NAME_FOUL;
	public static final String COLUMN_NAME_YELLOW_CARD = FACT_PILOT_DATA_2017_Entry.COLUMN_NAME_YELLOW_CARD;
	public static final String COLUMN_NAME_RED_CARD = FACT_PILOT_DATA_2017_Entry.COLUMN_NAME_RED_CARD;
	public static final String COLUMN_NAME_NOTES = FACT_PILOT_DATA_2017_Entry.COLUMN_NAME_NOTES;
	public static final String COLUMN_NAME_INVALID = FACT_PILOT_DATA_2017_Entry.COLUMN_NAME_INVALID;
	public static final String COLUMN_NAME_TIMESTAMP = FACT_PILOT_DATA_2017_Entry.COLUMN_NAME_TIMESTAMP;
	

	public PilotStatsStruct() {
		init();
	}
	
	public void init() {
		event_id = "";
		team_id = 0;
		match_id = 0;
		practice_match = false;
		position_id = "Red 1";
		gears_installed_2 = 0;
		gears_installed_3 = 0;
		gears_installed_4 = 0;
		gears_lifed = 0;
		rotor_1_started = false;
		rotor_2_started = false;
		rotor_3_started = false;
		rotor_4_started = false;
		foul = false;
		yellow_card = false;
		red_card = false;
		notes = "";
	}
	
	public PilotStatsStruct(int team, String event, int match) {
		init();
		this.team_id = team;
		this.event_id = event;
		this.match_id = match;
	}
	
	public PilotStatsStruct(int team, String event, int match, boolean practice) {
		init();
		this.team_id = team;
		this.event_id = event;
		this.match_id = match;
		this.practice_match = practice;
	}

	public ContentValues getValues(DB db, SQLiteDatabase database) {
		ContentValues vals = new ContentValues();
		long ev = db.getEventIDFromName(event_id, database);
		vals.put(COLUMN_NAME_ID, ev * 10000000 + match_id * 10000 + team_id);
		vals.put(COLUMN_NAME_EVENT_ID, ev);
		vals.put(COLUMN_NAME_TEAM_ID, team_id);
		vals.put(COLUMN_NAME_MATCH_ID, match_id);
		vals.put(COLUMN_NAME_PRACTICE_MATCH, practice_match ? 1 : 0);
		vals.put(COLUMN_NAME_POSITION_ID, db.getPosIDFromName(position_id, database));
		vals.put(COLUMN_NAME_GEARS_INSTALLED_2, gears_installed_2);
		vals.put(COLUMN_NAME_GEARS_INSTALLED_3, gears_installed_3);
		vals.put(COLUMN_NAME_GEARS_INSTALLED_4, gears_installed_4);
		vals.put(COLUMN_NAME_GEARS_LIFED, gears_lifed);
		vals.put(COLUMN_NAME_ROTOR_1_STARTED, rotor_1_started ? 1 : 0);
		vals.put(COLUMN_NAME_ROTOR_2_STARTED, rotor_2_started ? 1 : 0);
		vals.put(COLUMN_NAME_ROTOR_3_STARTED, rotor_3_started ? 1 : 0);
		vals.put(COLUMN_NAME_ROTOR_4_STARTED, rotor_4_started ? 1 : 0);
		vals.put(COLUMN_NAME_FOUL, foul ? 1 : 0);
		vals.put(COLUMN_NAME_YELLOW_CARD, yellow_card ? 1 : 0);
		vals.put(COLUMN_NAME_RED_CARD, red_card ? 1 : 0);
		vals.put(COLUMN_NAME_NOTES, notes);
		vals.put(COLUMN_NAME_INVALID, 1);
	
		return vals;
	}

	public void fromCursor(Cursor c, DB db, SQLiteDatabase database, int pos) {
		c.moveToPosition(pos);
		event_id = DB.getEventNameFromID(c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_EVENT_ID)), database);
		team_id = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_TEAM_ID));
		match_id = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_MATCH_ID));
		practice_match = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_PRACTICE_MATCH)) != 0;
		position_id = DB.getPosNameFromID(c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_POSITION_ID)), database);
		gears_installed_2 = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_GEARS_INSTALLED_2));
		gears_installed_3 = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_GEARS_INSTALLED_3));
		gears_installed_4 = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_GEARS_INSTALLED_4));
		gears_lifed = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_GEARS_LIFED));
		rotor_1_started = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_ROTOR_1_STARTED)) != 0;
		rotor_2_started = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_ROTOR_2_STARTED)) != 0;
		rotor_3_started = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_ROTOR_3_STARTED)) != 0;
		rotor_4_started = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_ROTOR_4_STARTED)) != 0;
		foul = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_FOUL)) != 0;
		yellow_card = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_YELLOW_CARD)) != 0;
		red_card = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_RED_CARD)) != 0;
		notes = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_NOTES));
	}

	public String[] getProjection() {
		List<String> temp = new ArrayList<String>(17);
		temp.add(COLUMN_NAME_EVENT_ID);
		temp.add(COLUMN_NAME_TEAM_ID);
		temp.add(COLUMN_NAME_MATCH_ID);
		temp.add(COLUMN_NAME_PRACTICE_MATCH);
		temp.add(COLUMN_NAME_POSITION_ID);
		temp.add(COLUMN_NAME_GEARS_INSTALLED_2);
		temp.add(COLUMN_NAME_GEARS_INSTALLED_3);
		temp.add(COLUMN_NAME_GEARS_INSTALLED_4);
		temp.add(COLUMN_NAME_GEARS_LIFED);
		temp.add(COLUMN_NAME_ROTOR_1_STARTED);
		temp.add(COLUMN_NAME_ROTOR_2_STARTED);
		temp.add(COLUMN_NAME_ROTOR_3_STARTED);
		temp.add(COLUMN_NAME_ROTOR_4_STARTED);
		temp.add(COLUMN_NAME_FOUL);
		temp.add(COLUMN_NAME_YELLOW_CARD);
		temp.add(COLUMN_NAME_RED_CARD);
		temp.add(COLUMN_NAME_NOTES);
		String[] projection = new String[temp.size()];
		return temp.toArray(projection);
	}

	public boolean isTextField(String column_name) {
		if (COLUMN_NAME_NOTES.equalsIgnoreCase(column_name)) return true;
	
		return false;
	}

	public boolean needsConvertedToText(String column_name) {
		if (COLUMN_NAME_EVENT_ID.equalsIgnoreCase(column_name)) return true;
	
		if (COLUMN_NAME_POSITION_ID.equalsIgnoreCase(column_name)) return true;
	
		return false;
	}

	public ContentValues jsonToCV(JSONObject json) throws JSONException {
		ContentValues vals = new ContentValues();
		vals.put(COLUMN_NAME_ID, json.getInt(COLUMN_NAME_ID));
		vals.put(COLUMN_NAME_EVENT_ID, json.getInt(COLUMN_NAME_EVENT_ID));
		vals.put(COLUMN_NAME_TEAM_ID, json.getInt(COLUMN_NAME_TEAM_ID));
		vals.put(COLUMN_NAME_MATCH_ID, json.getInt(COLUMN_NAME_MATCH_ID));
		vals.put(COLUMN_NAME_PRACTICE_MATCH, json.getInt(COLUMN_NAME_PRACTICE_MATCH));
		vals.put(COLUMN_NAME_POSITION_ID, json.getInt(COLUMN_NAME_POSITION_ID));
		vals.put(COLUMN_NAME_GEARS_INSTALLED_2, json.getInt(COLUMN_NAME_GEARS_INSTALLED_2));
		vals.put(COLUMN_NAME_GEARS_INSTALLED_3, json.getInt(COLUMN_NAME_GEARS_INSTALLED_3));
		vals.put(COLUMN_NAME_GEARS_INSTALLED_4, json.getInt(COLUMN_NAME_GEARS_INSTALLED_4));
		vals.put(COLUMN_NAME_GEARS_LIFED, json.getInt(COLUMN_NAME_GEARS_LIFED));
		vals.put(COLUMN_NAME_ROTOR_1_STARTED, json.getInt(COLUMN_NAME_ROTOR_1_STARTED));
		vals.put(COLUMN_NAME_ROTOR_2_STARTED, json.getInt(COLUMN_NAME_ROTOR_2_STARTED));
		vals.put(COLUMN_NAME_ROTOR_3_STARTED, json.getInt(COLUMN_NAME_ROTOR_3_STARTED));
		vals.put(COLUMN_NAME_ROTOR_4_STARTED, json.getInt(COLUMN_NAME_ROTOR_4_STARTED));
		vals.put(COLUMN_NAME_FOUL, json.getInt(COLUMN_NAME_FOUL));
		vals.put(COLUMN_NAME_YELLOW_CARD, json.getInt(COLUMN_NAME_YELLOW_CARD));
		vals.put(COLUMN_NAME_RED_CARD, json.getInt(COLUMN_NAME_RED_CARD));
		vals.put(COLUMN_NAME_NOTES, json.getString(COLUMN_NAME_NOTES));
		vals.put(COLUMN_NAME_INVALID, 0);
		vals.put(COLUMN_NAME_TIMESTAMP, DB.dateParser.format(new Date(json.getLong(COLUMN_NAME_TIMESTAMP) * 1000)));
		return vals;
	}

	public LinkedHashMap<String,String> getDisplayData() {
		LinkedHashMap<String,String> vals = new LinkedHashMap<String,String>();
		vals.put( COLUMN_NAME_EVENT_ID, event_id);
		vals.put( COLUMN_NAME_TEAM_ID, String.valueOf(team_id));
		vals.put( COLUMN_NAME_MATCH_ID, String.valueOf(match_id));
		vals.put( COLUMN_NAME_PRACTICE_MATCH, String.valueOf(practice_match ? 1 : 0));
		vals.put( COLUMN_NAME_POSITION_ID, position_id);
		vals.put( COLUMN_NAME_GEARS_INSTALLED_2, String.valueOf(gears_installed_2));
		vals.put( COLUMN_NAME_GEARS_INSTALLED_3, String.valueOf(gears_installed_3));
		vals.put( COLUMN_NAME_GEARS_INSTALLED_4, String.valueOf(gears_installed_4));
		vals.put( COLUMN_NAME_GEARS_LIFED, String.valueOf(gears_lifed));
		vals.put( COLUMN_NAME_ROTOR_1_STARTED, String.valueOf(rotor_1_started ? 1 : 0));
		vals.put( COLUMN_NAME_ROTOR_2_STARTED, String.valueOf(rotor_2_started ? 1 : 0));
		vals.put( COLUMN_NAME_ROTOR_3_STARTED, String.valueOf(rotor_3_started ? 1 : 0));
		vals.put( COLUMN_NAME_ROTOR_4_STARTED, String.valueOf(rotor_4_started ? 1 : 0));
		vals.put( COLUMN_NAME_FOUL, String.valueOf(foul ? 1 : 0));
		vals.put( COLUMN_NAME_YELLOW_CARD, String.valueOf(yellow_card ? 1 : 0));
		vals.put( COLUMN_NAME_RED_CARD, String.valueOf(red_card ? 1 : 0));
		vals.put( COLUMN_NAME_NOTES, notes);
		return vals;
	}

}