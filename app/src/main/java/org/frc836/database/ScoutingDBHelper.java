package org.frc836.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScoutingDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 20177;
    public static final String DATABASE_NAME = "FRCscouting.db";

    private static ScoutingDBHelper helper;
    public static final Object lock = new Object();

    private static final String upgrade20174to20175[] = {"ALTER TABLE scout_pit_data_2017 ADD COLUMN robot_gross_weight_lbs unsigned int(4) NOT NULL DEFAULT 0;"};
    private static final String upgrade20175to20176[] = {"ALTER TABLE fact_pilot_data_2017 ADD COLUMN gears_dropped unsigned int(3) NOT NULL DEFAULT 0;"};
    private static final String upgrade20176to20177[] = {"ALTER TABLE fact_match_data_2017 ADD COLUMN climb_time int(3) NOT NULL DEFAULT -1;",
                                                         "ALTER TABLE fact_match_data_2017 ADD COLUMN align_time int(3) NOT NULL DEFAULT -1;"};

    public ScoutingDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (int i = 0; i < FRCScoutingContract.SQL_CREATE_ENTRIES.length; i++)
            db.execSQL(FRCScoutingContract.SQL_CREATE_ENTRIES[i]);
        DBActivity.dbUpdated();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 20174 || oldVersion > newVersion) {
            for (int i = 0; i < FRCScoutingContract.SQL_DELETE_ENTRIES.length; i++)
                db.execSQL(FRCScoutingContract.SQL_DELETE_ENTRIES[i]);
            onCreate(db);
        } else {
            switch (oldVersion) {
                case 20174:
                    for (String s : upgrade20174to20175) {
                        db.execSQL(s);
                    }
                case 20175:
                    for (String s : upgrade20175to20176) {
                        db.execSQL(s);
                    }
                case 20176:
                    for (String s : upgrade20176to20177) {
                        db.execSQL(s);
                    }
                default:
                    break;
            }
            DBActivity.dbUpdated();
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static ScoutingDBHelper getInstance(Context context) {
        if (helper == null) {
            helper = new ScoutingDBHelper(context);
        }
        return helper;
    }

    public static ScoutingDBHelper getInstance() {
        return helper;
    }

    public interface DBInstantiatedCallback {
        void dbInstantiated();
    }

}
