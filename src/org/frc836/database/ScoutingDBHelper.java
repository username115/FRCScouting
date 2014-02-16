package org.frc836.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class ScoutingDBHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 20147;
	public static final String DATABASE_NAME = "FRCscouting.db";
	
	public static ScoutingDBHelper helper;
	
	public ScoutingDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (int i = 0; i<FRCScoutingContract.SQL_CREATE_ENTRIES.length; i++)
			db.execSQL(FRCScoutingContract.SQL_CREATE_ENTRIES[i]);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (int i = 0; i<FRCScoutingContract.SQL_DELETE_ENTRIES.length; i++)
			db.execSQL(FRCScoutingContract.SQL_DELETE_ENTRIES[i]);
		onCreate(db);
	}
	
	
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

}
