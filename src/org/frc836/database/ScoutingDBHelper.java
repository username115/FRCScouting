package org.frc836.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class ScoutingDBHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 20131;
	public static final String DATABASE_NAME = "FRCscouting.db";
	
	public ScoutingDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(FRCScoutingContract.SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(FRCScoutingContract.SQL_DELETE_ENTRIES);
		onCreate(db);
	}
	
	
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

}
