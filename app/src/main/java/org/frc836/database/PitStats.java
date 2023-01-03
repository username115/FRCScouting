/*
-----------------------------------------------------
StatsStructGen.py 1.1

This file was autogenerated with run cmd:
  "gen_scripts/StatsStructGen.py --packagename=org.frc836.database --classname=PitStats --tablename=scout_pit_data_2022 --infile=FRC_Scouting_Server/scouting.sql --outfile=app/src/main/java/org/frc836/database/PitStats.java"

python version info:
  2.7.18 (default, Mar  8 2021, 13:02:45)
[GCC 9.3.0]

Please take heed of modifying unnecessarily
-----------------------------------------------------
*/

package org.frc836.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.frc836.database.FRCScoutingContract.SCOUT_PIT_DATA_2022_Entry;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class PitStats {


    public int team_id;
    public boolean traction_wheels;
    public boolean pneumatic_wheels;
    public boolean omni_wheels;
    public boolean mecanum_wheels;
    public boolean swerve_drive;
    public boolean tank_drive;
    public boolean other_drive_wheels;
    public int team_batteries;
    public int team_battery_chargers;
    public int robot_gross_weight_lbs;
    public String programming_id;
    public int mechanical_appearance;
    public int electrical_appearance;
    public String notes;


    public static final String TABLE_NAME = SCOUT_PIT_DATA_2022_Entry.TABLE_NAME;
    public static final String COLUMN_NAME_ID = SCOUT_PIT_DATA_2022_Entry.COLUMN_NAME_ID;
    public static final String COLUMN_NAME_TEAM_ID = SCOUT_PIT_DATA_2022_Entry.COLUMN_NAME_TEAM_ID;
    public static final String COLUMN_NAME_TRACTION_WHEELS = SCOUT_PIT_DATA_2022_Entry.COLUMN_NAME_TRACTION_WHEELS;
    public static final String COLUMN_NAME_PNEUMATIC_WHEELS = SCOUT_PIT_DATA_2022_Entry.COLUMN_NAME_PNEUMATIC_WHEELS;
    public static final String COLUMN_NAME_OMNI_WHEELS = SCOUT_PIT_DATA_2022_Entry.COLUMN_NAME_OMNI_WHEELS;
    public static final String COLUMN_NAME_MECANUM_WHEELS = SCOUT_PIT_DATA_2022_Entry.COLUMN_NAME_MECANUM_WHEELS;
    public static final String COLUMN_NAME_SWERVE_DRIVE = SCOUT_PIT_DATA_2022_Entry.COLUMN_NAME_SWERVE_DRIVE;
    public static final String COLUMN_NAME_TANK_DRIVE = SCOUT_PIT_DATA_2022_Entry.COLUMN_NAME_TANK_DRIVE;
    public static final String COLUMN_NAME_OTHER_DRIVE_WHEELS = SCOUT_PIT_DATA_2022_Entry.COLUMN_NAME_OTHER_DRIVE_WHEELS;
    public static final String COLUMN_NAME_TEAM_BATTERIES = SCOUT_PIT_DATA_2022_Entry.COLUMN_NAME_TEAM_BATTERIES;
    public static final String COLUMN_NAME_TEAM_BATTERY_CHARGERS = SCOUT_PIT_DATA_2022_Entry.COLUMN_NAME_TEAM_BATTERY_CHARGERS;
    public static final String COLUMN_NAME_ROBOT_GROSS_WEIGHT_LBS = SCOUT_PIT_DATA_2022_Entry.COLUMN_NAME_ROBOT_GROSS_WEIGHT_LBS;
    public static final String COLUMN_NAME_PROGRAMMING_ID = SCOUT_PIT_DATA_2022_Entry.COLUMN_NAME_PROGRAMMING_ID;
    public static final String COLUMN_NAME_MECHANICAL_APPEARANCE = SCOUT_PIT_DATA_2022_Entry.COLUMN_NAME_MECHANICAL_APPEARANCE;
    public static final String COLUMN_NAME_ELECTRICAL_APPEARANCE = SCOUT_PIT_DATA_2022_Entry.COLUMN_NAME_ELECTRICAL_APPEARANCE;
    public static final String COLUMN_NAME_NOTES = SCOUT_PIT_DATA_2022_Entry.COLUMN_NAME_NOTES;
    public static final String COLUMN_NAME_INVALID = SCOUT_PIT_DATA_2022_Entry.COLUMN_NAME_INVALID;
    public static final String COLUMN_NAME_TIMESTAMP = SCOUT_PIT_DATA_2022_Entry.COLUMN_NAME_TIMESTAMP;


    public PitStats() {
        init();
    }

    public void init() {
        team_id = 0;
        traction_wheels = false;
        pneumatic_wheels = false;
        omni_wheels = false;
        mecanum_wheels = false;
        swerve_drive = false;
        tank_drive = false;
        other_drive_wheels = false;
        team_batteries = 0;
        team_battery_chargers = 0;
        robot_gross_weight_lbs = 0;
        programming_id = "Other";
        mechanical_appearance = 0;
        electrical_appearance = 0;
        notes = "";
    }


    public ContentValues getValues(DB db, SQLiteDatabase database) {
        ContentValues vals = new ContentValues();
        vals.put(COLUMN_NAME_ID, team_id * team_id);
        vals.put(COLUMN_NAME_TEAM_ID, team_id);
        vals.put(COLUMN_NAME_TRACTION_WHEELS, traction_wheels ? 1 : 0);
        vals.put(COLUMN_NAME_PNEUMATIC_WHEELS, pneumatic_wheels ? 1 : 0);
        vals.put(COLUMN_NAME_OMNI_WHEELS, omni_wheels ? 1 : 0);
        vals.put(COLUMN_NAME_MECANUM_WHEELS, mecanum_wheels ? 1 : 0);
        vals.put(COLUMN_NAME_SWERVE_DRIVE, swerve_drive ? 1 : 0);
        vals.put(COLUMN_NAME_TANK_DRIVE, tank_drive ? 1 : 0);
        vals.put(COLUMN_NAME_OTHER_DRIVE_WHEELS, other_drive_wheels ? 1 : 0);
        vals.put(COLUMN_NAME_TEAM_BATTERIES, team_batteries);
        vals.put(COLUMN_NAME_TEAM_BATTERY_CHARGERS, team_battery_chargers);
        vals.put(COLUMN_NAME_ROBOT_GROSS_WEIGHT_LBS, robot_gross_weight_lbs);
        vals.put(COLUMN_NAME_PROGRAMMING_ID, db.getProgrammingIDFromName(programming_id, database));
        vals.put(COLUMN_NAME_MECHANICAL_APPEARANCE, mechanical_appearance);
        vals.put(COLUMN_NAME_ELECTRICAL_APPEARANCE, electrical_appearance);
        vals.put(COLUMN_NAME_NOTES, notes);
        vals.put(COLUMN_NAME_INVALID, 1);

        return vals;
    }

    public void fromCursor(Cursor c, DB db, SQLiteDatabase database) {
        fromCursor(c, db, database, 0);
    }

    public void fromCursor(Cursor c, DB db, SQLiteDatabase database, int pos) {
        c.moveToPosition(pos);
        team_id = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_TEAM_ID));
        traction_wheels = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_TRACTION_WHEELS)) != 0;
        pneumatic_wheels = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_PNEUMATIC_WHEELS)) != 0;
        omni_wheels = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_OMNI_WHEELS)) != 0;
        mecanum_wheels = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_MECANUM_WHEELS)) != 0;
        swerve_drive = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_SWERVE_DRIVE)) != 0;
        tank_drive = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_TANK_DRIVE)) != 0;
        other_drive_wheels = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_OTHER_DRIVE_WHEELS)) != 0;
        team_batteries = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_TEAM_BATTERIES));
        team_battery_chargers = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_TEAM_BATTERY_CHARGERS));
        robot_gross_weight_lbs = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_ROBOT_GROSS_WEIGHT_LBS));
        programming_id = DB.getProgrammingNameFromID(c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_PROGRAMMING_ID)), database);
        mechanical_appearance = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_MECHANICAL_APPEARANCE));
        electrical_appearance = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_ELECTRICAL_APPEARANCE));
        notes = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_NOTES));
    }

    public String[] getProjection() {
        List<String> temp = new ArrayList<String>(15);
        temp.add(COLUMN_NAME_TEAM_ID);
        temp.add(COLUMN_NAME_TRACTION_WHEELS);
        temp.add(COLUMN_NAME_PNEUMATIC_WHEELS);
        temp.add(COLUMN_NAME_OMNI_WHEELS);
        temp.add(COLUMN_NAME_MECANUM_WHEELS);
        temp.add(COLUMN_NAME_SWERVE_DRIVE);
        temp.add(COLUMN_NAME_TANK_DRIVE);
        temp.add(COLUMN_NAME_OTHER_DRIVE_WHEELS);
        temp.add(COLUMN_NAME_TEAM_BATTERIES);
        temp.add(COLUMN_NAME_TEAM_BATTERY_CHARGERS);
        temp.add(COLUMN_NAME_ROBOT_GROSS_WEIGHT_LBS);
        temp.add(COLUMN_NAME_PROGRAMMING_ID);
        temp.add(COLUMN_NAME_MECHANICAL_APPEARANCE);
        temp.add(COLUMN_NAME_ELECTRICAL_APPEARANCE);
        temp.add(COLUMN_NAME_NOTES);
        String[] projection = new String[temp.size()];
        return temp.toArray(projection);
    }

    public boolean isTextField(String column_name) {
        if (COLUMN_NAME_NOTES.equalsIgnoreCase(column_name)) return true;

        return false;
    }

    public boolean needsConvertedToText(String column_name) {
        if (COLUMN_NAME_PROGRAMMING_ID.equalsIgnoreCase(column_name)) return true;

        return false;
    }

    public ContentValues jsonToCV(JSONObject json) throws JSONException {
        ContentValues vals = new ContentValues();
        vals.put(COLUMN_NAME_ID, json.has(COLUMN_NAME_ID) ? json.getInt(COLUMN_NAME_ID) : 0);
        vals.put(COLUMN_NAME_TEAM_ID, json.has(COLUMN_NAME_TEAM_ID) ? json.getInt(COLUMN_NAME_TEAM_ID) : 0);
        vals.put(COLUMN_NAME_TRACTION_WHEELS, json.has(COLUMN_NAME_TRACTION_WHEELS) ? json.getInt(COLUMN_NAME_TRACTION_WHEELS) : 0);
        vals.put(COLUMN_NAME_PNEUMATIC_WHEELS, json.has(COLUMN_NAME_PNEUMATIC_WHEELS) ? json.getInt(COLUMN_NAME_PNEUMATIC_WHEELS) : 0);
        vals.put(COLUMN_NAME_OMNI_WHEELS, json.has(COLUMN_NAME_OMNI_WHEELS) ? json.getInt(COLUMN_NAME_OMNI_WHEELS) : 0);
        vals.put(COLUMN_NAME_MECANUM_WHEELS, json.has(COLUMN_NAME_MECANUM_WHEELS) ? json.getInt(COLUMN_NAME_MECANUM_WHEELS) : 0);
        vals.put(COLUMN_NAME_SWERVE_DRIVE, json.has(COLUMN_NAME_SWERVE_DRIVE) ? json.getInt(COLUMN_NAME_SWERVE_DRIVE) : 0);
        vals.put(COLUMN_NAME_TANK_DRIVE, json.has(COLUMN_NAME_TANK_DRIVE) ? json.getInt(COLUMN_NAME_TANK_DRIVE) : 0);
        vals.put(COLUMN_NAME_OTHER_DRIVE_WHEELS, json.has(COLUMN_NAME_OTHER_DRIVE_WHEELS) ? json.getInt(COLUMN_NAME_OTHER_DRIVE_WHEELS) : 0);
        vals.put(COLUMN_NAME_TEAM_BATTERIES, json.has(COLUMN_NAME_TEAM_BATTERIES) ? json.getInt(COLUMN_NAME_TEAM_BATTERIES) : 0);
        vals.put(COLUMN_NAME_TEAM_BATTERY_CHARGERS, json.has(COLUMN_NAME_TEAM_BATTERY_CHARGERS) ? json.getInt(COLUMN_NAME_TEAM_BATTERY_CHARGERS) : 0);
        vals.put(COLUMN_NAME_ROBOT_GROSS_WEIGHT_LBS, json.has(COLUMN_NAME_ROBOT_GROSS_WEIGHT_LBS) ? json.getInt(COLUMN_NAME_ROBOT_GROSS_WEIGHT_LBS) : 0);
        vals.put(COLUMN_NAME_PROGRAMMING_ID, json.has(COLUMN_NAME_PROGRAMMING_ID) ? json.getInt(COLUMN_NAME_PROGRAMMING_ID) : 0);
        vals.put(COLUMN_NAME_MECHANICAL_APPEARANCE, json.has(COLUMN_NAME_MECHANICAL_APPEARANCE) ? json.getInt(COLUMN_NAME_MECHANICAL_APPEARANCE) : 0);
        vals.put(COLUMN_NAME_ELECTRICAL_APPEARANCE, json.has(COLUMN_NAME_ELECTRICAL_APPEARANCE) ? json.getInt(COLUMN_NAME_ELECTRICAL_APPEARANCE) : 0);
        vals.put(COLUMN_NAME_NOTES, json.has(COLUMN_NAME_NOTES) ? json.getString(COLUMN_NAME_NOTES) : "");
        vals.put(COLUMN_NAME_INVALID, 0);
        vals.put(COLUMN_NAME_TIMESTAMP, DB.dateParser.format(new Date(json.getLong(COLUMN_NAME_TIMESTAMP) * 1000)));
        return vals;
    }

    public LinkedHashMap<String, String> getDisplayData() {
        LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
        vals.put(COLUMN_NAME_TEAM_ID, String.valueOf(team_id));
        vals.put(COLUMN_NAME_TRACTION_WHEELS, String.valueOf(traction_wheels ? 1 : 0));
        vals.put(COLUMN_NAME_PNEUMATIC_WHEELS, String.valueOf(pneumatic_wheels ? 1 : 0));
        vals.put(COLUMN_NAME_OMNI_WHEELS, String.valueOf(omni_wheels ? 1 : 0));
        vals.put(COLUMN_NAME_MECANUM_WHEELS, String.valueOf(mecanum_wheels ? 1 : 0));
        vals.put(COLUMN_NAME_SWERVE_DRIVE, String.valueOf(swerve_drive ? 1 : 0));
        vals.put(COLUMN_NAME_TANK_DRIVE, String.valueOf(tank_drive ? 1 : 0));
        vals.put(COLUMN_NAME_OTHER_DRIVE_WHEELS, String.valueOf(other_drive_wheels ? 1 : 0));
        vals.put(COLUMN_NAME_TEAM_BATTERIES, String.valueOf(team_batteries));
        vals.put(COLUMN_NAME_TEAM_BATTERY_CHARGERS, String.valueOf(team_battery_chargers));
        vals.put(COLUMN_NAME_ROBOT_GROSS_WEIGHT_LBS, String.valueOf(robot_gross_weight_lbs));
        vals.put(COLUMN_NAME_PROGRAMMING_ID, programming_id);
        vals.put(COLUMN_NAME_MECHANICAL_APPEARANCE, String.valueOf(mechanical_appearance));
        vals.put(COLUMN_NAME_ELECTRICAL_APPEARANCE, String.valueOf(electrical_appearance));
        vals.put(COLUMN_NAME_NOTES, notes);
        return vals;
    }

}
