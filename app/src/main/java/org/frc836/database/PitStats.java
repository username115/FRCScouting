/*
-----------------------------------------------------
StatsStructGen.py 1.1

This file was autogenerated with run cmd:
  "gen_scripts/StatsStructGen.py --packagename=org.frc836.database --classname=PitStats --tablename=scout_pit_data_2023 --infile=FRC_Scouting_Server/scouting.sql --outfile=app/src/main/java/org/frc836/database/PitStats.java"

python version info:
  3.10.6 (main, Nov 14 2022, 16:10:14) [GCC 11.3.0]

Please take heed of modifying unnecessarily
-----------------------------------------------------
*/

package org.frc836.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.frc836.database.FRCScoutingContract.SCOUT_PIT_DATA_2023_Entry;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import java.util.ArrayList;
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
    public int robot_gross_weight_lbs;
    public int robot_gross_width_in;
    public int robot_gross_length_in;
    public String notes;
    

    public static final String TABLE_NAME = SCOUT_PIT_DATA_2023_Entry.TABLE_NAME;
    public static final String COLUMN_NAME_ID = SCOUT_PIT_DATA_2023_Entry.COLUMN_NAME_ID;
    public static final String COLUMN_NAME_TEAM_ID = SCOUT_PIT_DATA_2023_Entry.COLUMN_NAME_TEAM_ID;
    public static final String COLUMN_NAME_TRACTION_WHEELS = SCOUT_PIT_DATA_2023_Entry.COLUMN_NAME_TRACTION_WHEELS;
    public static final String COLUMN_NAME_PNEUMATIC_WHEELS = SCOUT_PIT_DATA_2023_Entry.COLUMN_NAME_PNEUMATIC_WHEELS;
    public static final String COLUMN_NAME_OMNI_WHEELS = SCOUT_PIT_DATA_2023_Entry.COLUMN_NAME_OMNI_WHEELS;
    public static final String COLUMN_NAME_MECANUM_WHEELS = SCOUT_PIT_DATA_2023_Entry.COLUMN_NAME_MECANUM_WHEELS;
    public static final String COLUMN_NAME_SWERVE_DRIVE = SCOUT_PIT_DATA_2023_Entry.COLUMN_NAME_SWERVE_DRIVE;
    public static final String COLUMN_NAME_TANK_DRIVE = SCOUT_PIT_DATA_2023_Entry.COLUMN_NAME_TANK_DRIVE;
    public static final String COLUMN_NAME_OTHER_DRIVE_WHEELS = SCOUT_PIT_DATA_2023_Entry.COLUMN_NAME_OTHER_DRIVE_WHEELS;
    public static final String COLUMN_NAME_ROBOT_GROSS_WEIGHT_LBS = SCOUT_PIT_DATA_2023_Entry.COLUMN_NAME_ROBOT_GROSS_WEIGHT_LBS;
    public static final String COLUMN_NAME_ROBOT_GROSS_WIDTH_IN = SCOUT_PIT_DATA_2023_Entry.COLUMN_NAME_ROBOT_GROSS_WIDTH_IN;
    public static final String COLUMN_NAME_ROBOT_GROSS_LENGTH_IN = SCOUT_PIT_DATA_2023_Entry.COLUMN_NAME_ROBOT_GROSS_LENGTH_IN;
    public static final String COLUMN_NAME_NOTES = SCOUT_PIT_DATA_2023_Entry.COLUMN_NAME_NOTES;
    public static final String COLUMN_NAME_INVALID = SCOUT_PIT_DATA_2023_Entry.COLUMN_NAME_INVALID;
    public static final String COLUMN_NAME_TIMESTAMP = SCOUT_PIT_DATA_2023_Entry.COLUMN_NAME_TIMESTAMP;
    

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
        robot_gross_weight_lbs = 0;
        robot_gross_width_in = 0;
        robot_gross_length_in = 0;
        notes = "";
    }
    

    public void copyFrom(PitStats other) {
        this.team_id = other.team_id;
        this.traction_wheels = other.traction_wheels;
        this.pneumatic_wheels = other.pneumatic_wheels;
        this.omni_wheels = other.omni_wheels;
        this.mecanum_wheels = other.mecanum_wheels;
        this.swerve_drive = other.swerve_drive;
        this.tank_drive = other.tank_drive;
        this.other_drive_wheels = other.other_drive_wheels;
        this.robot_gross_weight_lbs = other.robot_gross_weight_lbs;
        this.robot_gross_width_in = other.robot_gross_width_in;
        this.robot_gross_length_in = other.robot_gross_length_in;
        this.notes = other.notes;
    
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
        vals.put(COLUMN_NAME_ROBOT_GROSS_WEIGHT_LBS, robot_gross_weight_lbs);
        vals.put(COLUMN_NAME_ROBOT_GROSS_WIDTH_IN, robot_gross_width_in);
        vals.put(COLUMN_NAME_ROBOT_GROSS_LENGTH_IN, robot_gross_length_in);
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
        robot_gross_weight_lbs = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_ROBOT_GROSS_WEIGHT_LBS));
        robot_gross_width_in = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_ROBOT_GROSS_WIDTH_IN));
        robot_gross_length_in = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_ROBOT_GROSS_LENGTH_IN));
        notes = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_NOTES));
    }

    public String[] getProjection() {
        List<String> temp = new ArrayList<String>(12);
        temp.add(COLUMN_NAME_TEAM_ID);
        temp.add(COLUMN_NAME_TRACTION_WHEELS);
        temp.add(COLUMN_NAME_PNEUMATIC_WHEELS);
        temp.add(COLUMN_NAME_OMNI_WHEELS);
        temp.add(COLUMN_NAME_MECANUM_WHEELS);
        temp.add(COLUMN_NAME_SWERVE_DRIVE);
        temp.add(COLUMN_NAME_TANK_DRIVE);
        temp.add(COLUMN_NAME_OTHER_DRIVE_WHEELS);
        temp.add(COLUMN_NAME_ROBOT_GROSS_WEIGHT_LBS);
        temp.add(COLUMN_NAME_ROBOT_GROSS_WIDTH_IN);
        temp.add(COLUMN_NAME_ROBOT_GROSS_LENGTH_IN);
        temp.add(COLUMN_NAME_NOTES);
        String[] projection = new String[temp.size()];
        return temp.toArray(projection);
    }

    public boolean isTextField(String column_name) {
        if (COLUMN_NAME_NOTES.equalsIgnoreCase(column_name)) return true;
    
        return false;
    }

    public boolean needsConvertedToText(String column_name) {
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
        vals.put(COLUMN_NAME_ROBOT_GROSS_WEIGHT_LBS, json.has(COLUMN_NAME_ROBOT_GROSS_WEIGHT_LBS) ? json.getInt(COLUMN_NAME_ROBOT_GROSS_WEIGHT_LBS) : 0);
        vals.put(COLUMN_NAME_ROBOT_GROSS_WIDTH_IN, json.has(COLUMN_NAME_ROBOT_GROSS_WIDTH_IN) ? json.getInt(COLUMN_NAME_ROBOT_GROSS_WIDTH_IN) : 0);
        vals.put(COLUMN_NAME_ROBOT_GROSS_LENGTH_IN, json.has(COLUMN_NAME_ROBOT_GROSS_LENGTH_IN) ? json.getInt(COLUMN_NAME_ROBOT_GROSS_LENGTH_IN) : 0);
        vals.put(COLUMN_NAME_NOTES, json.has(COLUMN_NAME_NOTES) ? json.getString(COLUMN_NAME_NOTES) : "");
        vals.put(COLUMN_NAME_INVALID, 0);
        vals.put(COLUMN_NAME_TIMESTAMP, DB.dateParser.format(new Date(json.getLong(COLUMN_NAME_TIMESTAMP) * 1000)));
        return vals;
    }

    public LinkedHashMap<String,String> getDisplayData() {
        LinkedHashMap<String,String> vals = new LinkedHashMap<String,String>();
        vals.put( COLUMN_NAME_TEAM_ID, String.valueOf(team_id));
        vals.put( COLUMN_NAME_TRACTION_WHEELS, String.valueOf(traction_wheels ? 1 : 0));
        vals.put( COLUMN_NAME_PNEUMATIC_WHEELS, String.valueOf(pneumatic_wheels ? 1 : 0));
        vals.put( COLUMN_NAME_OMNI_WHEELS, String.valueOf(omni_wheels ? 1 : 0));
        vals.put( COLUMN_NAME_MECANUM_WHEELS, String.valueOf(mecanum_wheels ? 1 : 0));
        vals.put( COLUMN_NAME_SWERVE_DRIVE, String.valueOf(swerve_drive ? 1 : 0));
        vals.put( COLUMN_NAME_TANK_DRIVE, String.valueOf(tank_drive ? 1 : 0));
        vals.put( COLUMN_NAME_OTHER_DRIVE_WHEELS, String.valueOf(other_drive_wheels ? 1 : 0));
        vals.put( COLUMN_NAME_ROBOT_GROSS_WEIGHT_LBS, String.valueOf(robot_gross_weight_lbs));
        vals.put( COLUMN_NAME_ROBOT_GROSS_WIDTH_IN, String.valueOf(robot_gross_width_in));
        vals.put( COLUMN_NAME_ROBOT_GROSS_LENGTH_IN, String.valueOf(robot_gross_length_in));
        vals.put( COLUMN_NAME_NOTES, notes);
        return vals;
    }

}