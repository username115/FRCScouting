/*
-----------------------------------------------------
StatsStructGen.py 1.1

This file was autogenerated with run cmd:
  "gen_scripts/StatsStructGen.py --packagename=org.frc836.database --classname=MatchStatsStruct --tablename=fact_match_data_2020 --infile=FRC_Scouting_Server/scouting.sql --outfile=app/src/main/java/org/frc836/database/MatchStatsStruct.java"

python version info:
  3.7.2 (tags/v3.7.2:9a3ffc0492, Dec 23 2018, 22:20:52) [MSC v.1916 32 bit (Intel)]

Please take heed of modifying unnecessarily
-----------------------------------------------------
*/

package org.frc836.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.frc836.database.FRCScoutingContract.FACT_MATCH_DATA_2020_Entry;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MatchStatsStruct {


	public String event_id;
	public int team_id;
	public int match_id;
	public boolean practice_match;
	public String position_id;
	public int start_position;
	public boolean auto_initiation_move;
	public int auto_score_low;
	public int auto_score_high;
	public int auto_miss;
	public int score_low;
	public int score_high;
	public int miss;
	public boolean rotation_control;
	public boolean position_control;
	public boolean generator_park;
	public boolean generator_hang;
	public boolean generator_hang_attempted;
	public boolean generator_level;
	public boolean foul;
	public boolean yellow_card;
	public boolean red_card;
	public boolean tip_over;
	public String notes;
	

	public static final String TABLE_NAME = FACT_MATCH_DATA_2020_Entry.TABLE_NAME;
	public static final String COLUMN_NAME_ID = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_ID;
	public static final String COLUMN_NAME_EVENT_ID = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_EVENT_ID;
	public static final String COLUMN_NAME_TEAM_ID = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_TEAM_ID;
	public static final String COLUMN_NAME_MATCH_ID = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_MATCH_ID;
	public static final String COLUMN_NAME_PRACTICE_MATCH = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_PRACTICE_MATCH;
	public static final String COLUMN_NAME_POSITION_ID = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_POSITION_ID;
	public static final String COLUMN_NAME_START_POSITION = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_START_POSITION;
	public static final String COLUMN_NAME_AUTO_INITIATION_MOVE = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_AUTO_INITIATION_MOVE;
	public static final String COLUMN_NAME_AUTO_SCORE_LOW = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_AUTO_SCORE_LOW;
	public static final String COLUMN_NAME_AUTO_SCORE_HIGH = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_AUTO_SCORE_HIGH;
	public static final String COLUMN_NAME_AUTO_MISS = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_AUTO_MISS;
	public static final String COLUMN_NAME_SCORE_LOW = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_SCORE_LOW;
	public static final String COLUMN_NAME_SCORE_HIGH = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_SCORE_HIGH;
	public static final String COLUMN_NAME_MISS = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_MISS;
	public static final String COLUMN_NAME_ROTATION_CONTROL = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_ROTATION_CONTROL;
	public static final String COLUMN_NAME_POSITION_CONTROL = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_POSITION_CONTROL;
	public static final String COLUMN_NAME_GENERATOR_PARK = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_GENERATOR_PARK;
	public static final String COLUMN_NAME_GENERATOR_HANG = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_GENERATOR_HANG;
	public static final String COLUMN_NAME_GENERATOR_HANG_ATTEMPTED = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_GENERATOR_HANG_ATTEMPTED;
	public static final String COLUMN_NAME_GENERATOR_LEVEL = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_GENERATOR_LEVEL;
	public static final String COLUMN_NAME_FOUL = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_FOUL;
	public static final String COLUMN_NAME_YELLOW_CARD = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_YELLOW_CARD;
	public static final String COLUMN_NAME_RED_CARD = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_RED_CARD;
	public static final String COLUMN_NAME_TIP_OVER = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_TIP_OVER;
	public static final String COLUMN_NAME_NOTES = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_NOTES;
	public static final String COLUMN_NAME_INVALID = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_INVALID;
	public static final String COLUMN_NAME_TIMESTAMP = FACT_MATCH_DATA_2020_Entry.COLUMN_NAME_TIMESTAMP;
	

	public MatchStatsStruct() {
		init();
	}
	
	public void init() {
		event_id = "";
		team_id = 0;
		match_id = 0;
		practice_match = false;
		position_id = "Red 1";
		start_position = 0;
		auto_initiation_move = false;
		auto_score_low = 0;
		auto_score_high = 0;
		auto_miss = 0;
		score_low = 0;
		score_high = 0;
		miss = 0;
		rotation_control = false;
		position_control = false;
		generator_park = false;
		generator_hang = false;
		generator_hang_attempted = false;
		generator_level = false;
		foul = false;
		yellow_card = false;
		red_card = false;
		tip_over = false;
		notes = "";
	}
	
	public MatchStatsStruct(int team, String event, int match) {
		init();
		this.team_id = team;
		this.event_id = event;
		this.match_id = match;
	}
	
	public MatchStatsStruct(int team, String event, int match, boolean practice) {
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
		vals.put(COLUMN_NAME_START_POSITION, start_position);
		vals.put(COLUMN_NAME_AUTO_INITIATION_MOVE, auto_initiation_move ? 1 : 0);
		vals.put(COLUMN_NAME_AUTO_SCORE_LOW, auto_score_low);
		vals.put(COLUMN_NAME_AUTO_SCORE_HIGH, auto_score_high);
		vals.put(COLUMN_NAME_AUTO_MISS, auto_miss);
		vals.put(COLUMN_NAME_SCORE_LOW, score_low);
		vals.put(COLUMN_NAME_SCORE_HIGH, score_high);
		vals.put(COLUMN_NAME_MISS, miss);
		vals.put(COLUMN_NAME_ROTATION_CONTROL, rotation_control ? 1 : 0);
		vals.put(COLUMN_NAME_POSITION_CONTROL, position_control ? 1 : 0);
		vals.put(COLUMN_NAME_GENERATOR_PARK, generator_park ? 1 : 0);
		vals.put(COLUMN_NAME_GENERATOR_HANG, generator_hang ? 1 : 0);
		vals.put(COLUMN_NAME_GENERATOR_HANG_ATTEMPTED, generator_hang_attempted ? 1 : 0);
		vals.put(COLUMN_NAME_GENERATOR_LEVEL, generator_level ? 1 : 0);
		vals.put(COLUMN_NAME_FOUL, foul ? 1 : 0);
		vals.put(COLUMN_NAME_YELLOW_CARD, yellow_card ? 1 : 0);
		vals.put(COLUMN_NAME_RED_CARD, red_card ? 1 : 0);
		vals.put(COLUMN_NAME_TIP_OVER, tip_over ? 1 : 0);
		vals.put(COLUMN_NAME_NOTES, notes);
		vals.put(COLUMN_NAME_INVALID, 1);
	
		return vals;
	}

	public void fromCursor(Cursor c, DB db, SQLiteDatabase database) {
		fromCursor(c, db, database, 0);
	}
	
	public void fromCursor(Cursor c, DB db, SQLiteDatabase database, int pos) {
		c.moveToPosition(pos);
		event_id = DB.getEventNameFromID(c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_EVENT_ID)), database);
		team_id = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_TEAM_ID));
		match_id = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_MATCH_ID));
		practice_match = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_PRACTICE_MATCH)) != 0;
		position_id = DB.getPosNameFromID(c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_POSITION_ID)), database);
		start_position = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_START_POSITION));
		auto_initiation_move = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_AUTO_INITIATION_MOVE)) != 0;
		auto_score_low = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_AUTO_SCORE_LOW));
		auto_score_high = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_AUTO_SCORE_HIGH));
		auto_miss = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_AUTO_MISS));
		score_low = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_SCORE_LOW));
		score_high = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_SCORE_HIGH));
		miss = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_MISS));
		rotation_control = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_ROTATION_CONTROL)) != 0;
		position_control = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_POSITION_CONTROL)) != 0;
		generator_park = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_GENERATOR_PARK)) != 0;
		generator_hang = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_GENERATOR_HANG)) != 0;
		generator_hang_attempted = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_GENERATOR_HANG_ATTEMPTED)) != 0;
		generator_level = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_GENERATOR_LEVEL)) != 0;
		foul = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_FOUL)) != 0;
		yellow_card = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_YELLOW_CARD)) != 0;
		red_card = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_RED_CARD)) != 0;
		tip_over = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_TIP_OVER)) != 0;
		notes = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_NOTES));
	}

	public String[] getProjection() {
		List<String> temp = new ArrayList<String>(24);
		temp.add(COLUMN_NAME_EVENT_ID);
		temp.add(COLUMN_NAME_TEAM_ID);
		temp.add(COLUMN_NAME_MATCH_ID);
		temp.add(COLUMN_NAME_PRACTICE_MATCH);
		temp.add(COLUMN_NAME_POSITION_ID);
		temp.add(COLUMN_NAME_START_POSITION);
		temp.add(COLUMN_NAME_AUTO_INITIATION_MOVE);
		temp.add(COLUMN_NAME_AUTO_SCORE_LOW);
		temp.add(COLUMN_NAME_AUTO_SCORE_HIGH);
		temp.add(COLUMN_NAME_AUTO_MISS);
		temp.add(COLUMN_NAME_SCORE_LOW);
		temp.add(COLUMN_NAME_SCORE_HIGH);
		temp.add(COLUMN_NAME_MISS);
		temp.add(COLUMN_NAME_ROTATION_CONTROL);
		temp.add(COLUMN_NAME_POSITION_CONTROL);
		temp.add(COLUMN_NAME_GENERATOR_PARK);
		temp.add(COLUMN_NAME_GENERATOR_HANG);
		temp.add(COLUMN_NAME_GENERATOR_HANG_ATTEMPTED);
		temp.add(COLUMN_NAME_GENERATOR_LEVEL);
		temp.add(COLUMN_NAME_FOUL);
		temp.add(COLUMN_NAME_YELLOW_CARD);
		temp.add(COLUMN_NAME_RED_CARD);
		temp.add(COLUMN_NAME_TIP_OVER);
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
		vals.put(COLUMN_NAME_ID, json.has(COLUMN_NAME_ID) ? json.getInt(COLUMN_NAME_ID) : 0);
		vals.put(COLUMN_NAME_EVENT_ID, json.has(COLUMN_NAME_EVENT_ID) ? json.getInt(COLUMN_NAME_EVENT_ID) : 0);
		vals.put(COLUMN_NAME_TEAM_ID, json.has(COLUMN_NAME_TEAM_ID) ? json.getInt(COLUMN_NAME_TEAM_ID) : 0);
		vals.put(COLUMN_NAME_MATCH_ID, json.has(COLUMN_NAME_MATCH_ID) ? json.getInt(COLUMN_NAME_MATCH_ID) : 0);
		vals.put(COLUMN_NAME_PRACTICE_MATCH, json.has(COLUMN_NAME_PRACTICE_MATCH) ? json.getInt(COLUMN_NAME_PRACTICE_MATCH) : 0);
		vals.put(COLUMN_NAME_POSITION_ID, json.has(COLUMN_NAME_POSITION_ID) ? json.getInt(COLUMN_NAME_POSITION_ID) : 0);
		vals.put(COLUMN_NAME_START_POSITION, json.has(COLUMN_NAME_START_POSITION) ? json.getInt(COLUMN_NAME_START_POSITION) : 0);
		vals.put(COLUMN_NAME_AUTO_INITIATION_MOVE, json.has(COLUMN_NAME_AUTO_INITIATION_MOVE) ? json.getInt(COLUMN_NAME_AUTO_INITIATION_MOVE) : 0);
		vals.put(COLUMN_NAME_AUTO_SCORE_LOW, json.has(COLUMN_NAME_AUTO_SCORE_LOW) ? json.getInt(COLUMN_NAME_AUTO_SCORE_LOW) : 0);
		vals.put(COLUMN_NAME_AUTO_SCORE_HIGH, json.has(COLUMN_NAME_AUTO_SCORE_HIGH) ? json.getInt(COLUMN_NAME_AUTO_SCORE_HIGH) : 0);
		vals.put(COLUMN_NAME_AUTO_MISS, json.has(COLUMN_NAME_AUTO_MISS) ? json.getInt(COLUMN_NAME_AUTO_MISS) : 0);
		vals.put(COLUMN_NAME_SCORE_LOW, json.has(COLUMN_NAME_SCORE_LOW) ? json.getInt(COLUMN_NAME_SCORE_LOW) : 0);
		vals.put(COLUMN_NAME_SCORE_HIGH, json.has(COLUMN_NAME_SCORE_HIGH) ? json.getInt(COLUMN_NAME_SCORE_HIGH) : 0);
		vals.put(COLUMN_NAME_MISS, json.has(COLUMN_NAME_MISS) ? json.getInt(COLUMN_NAME_MISS) : 0);
		vals.put(COLUMN_NAME_ROTATION_CONTROL, json.has(COLUMN_NAME_ROTATION_CONTROL) ? json.getInt(COLUMN_NAME_ROTATION_CONTROL) : 0);
		vals.put(COLUMN_NAME_POSITION_CONTROL, json.has(COLUMN_NAME_POSITION_CONTROL) ? json.getInt(COLUMN_NAME_POSITION_CONTROL) : 0);
		vals.put(COLUMN_NAME_GENERATOR_PARK, json.has(COLUMN_NAME_GENERATOR_PARK) ? json.getInt(COLUMN_NAME_GENERATOR_PARK) : 0);
		vals.put(COLUMN_NAME_GENERATOR_HANG, json.has(COLUMN_NAME_GENERATOR_HANG) ? json.getInt(COLUMN_NAME_GENERATOR_HANG) : 0);
		vals.put(COLUMN_NAME_GENERATOR_HANG_ATTEMPTED, json.has(COLUMN_NAME_GENERATOR_HANG_ATTEMPTED) ? json.getInt(COLUMN_NAME_GENERATOR_HANG_ATTEMPTED) : 0);
		vals.put(COLUMN_NAME_GENERATOR_LEVEL, json.has(COLUMN_NAME_GENERATOR_LEVEL) ? json.getInt(COLUMN_NAME_GENERATOR_LEVEL) : 0);
		vals.put(COLUMN_NAME_FOUL, json.has(COLUMN_NAME_FOUL) ? json.getInt(COLUMN_NAME_FOUL) : 0);
		vals.put(COLUMN_NAME_YELLOW_CARD, json.has(COLUMN_NAME_YELLOW_CARD) ? json.getInt(COLUMN_NAME_YELLOW_CARD) : 0);
		vals.put(COLUMN_NAME_RED_CARD, json.has(COLUMN_NAME_RED_CARD) ? json.getInt(COLUMN_NAME_RED_CARD) : 0);
		vals.put(COLUMN_NAME_TIP_OVER, json.has(COLUMN_NAME_TIP_OVER) ? json.getInt(COLUMN_NAME_TIP_OVER) : 0);
		vals.put(COLUMN_NAME_NOTES, json.has(COLUMN_NAME_NOTES) ? json.getString(COLUMN_NAME_NOTES) : "");
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
		vals.put( COLUMN_NAME_START_POSITION, String.valueOf(start_position));
		vals.put( COLUMN_NAME_AUTO_INITIATION_MOVE, String.valueOf(auto_initiation_move ? 1 : 0));
		vals.put( COLUMN_NAME_AUTO_SCORE_LOW, String.valueOf(auto_score_low));
		vals.put( COLUMN_NAME_AUTO_SCORE_HIGH, String.valueOf(auto_score_high));
		vals.put( COLUMN_NAME_AUTO_MISS, String.valueOf(auto_miss));
		vals.put( COLUMN_NAME_SCORE_LOW, String.valueOf(score_low));
		vals.put( COLUMN_NAME_SCORE_HIGH, String.valueOf(score_high));
		vals.put( COLUMN_NAME_MISS, String.valueOf(miss));
		vals.put( COLUMN_NAME_ROTATION_CONTROL, String.valueOf(rotation_control ? 1 : 0));
		vals.put( COLUMN_NAME_POSITION_CONTROL, String.valueOf(position_control ? 1 : 0));
		vals.put( COLUMN_NAME_GENERATOR_PARK, String.valueOf(generator_park ? 1 : 0));
		vals.put( COLUMN_NAME_GENERATOR_HANG, String.valueOf(generator_hang ? 1 : 0));
		vals.put( COLUMN_NAME_GENERATOR_HANG_ATTEMPTED, String.valueOf(generator_hang_attempted ? 1 : 0));
		vals.put( COLUMN_NAME_GENERATOR_LEVEL, String.valueOf(generator_level ? 1 : 0));
		vals.put( COLUMN_NAME_FOUL, String.valueOf(foul ? 1 : 0));
		vals.put( COLUMN_NAME_YELLOW_CARD, String.valueOf(yellow_card ? 1 : 0));
		vals.put( COLUMN_NAME_RED_CARD, String.valueOf(red_card ? 1 : 0));
		vals.put( COLUMN_NAME_TIP_OVER, String.valueOf(tip_over ? 1 : 0));
		vals.put( COLUMN_NAME_NOTES, notes);
		return vals;
	}

}