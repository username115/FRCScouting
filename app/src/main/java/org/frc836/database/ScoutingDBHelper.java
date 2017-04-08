package org.frc836.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScoutingDBHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 20175;
	public static final String DATABASE_NAME = "FRCscouting.db";
	
	private static ScoutingDBHelper helper;
	public static final Object lock = new Object();

	private static final String upgrade20174to20175 = "ALTER TABLE scout_pit_data_2017 ADD COLUMN robot_gross_weight_lbs unsigned int(4) NOT NULL DEFAULT 0;";
	
	public ScoutingDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (int i = 0; i<FRCScoutingContract.SQL_CREATE_ENTRIES.length; i++)
			db.execSQL(FRCScoutingContract.SQL_CREATE_ENTRIES[i]);
        DBActivity.dbUpdated();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < 20174) {
			for (int i = 0; i < FRCScoutingContract.SQL_DELETE_ENTRIES.length; i++)
				db.execSQL(FRCScoutingContract.SQL_DELETE_ENTRIES[i]);
			onCreate(db);
		} else {
			switch (oldVersion) {
				case 20174:
				    db.execSQL(upgrade20174to20175);
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
