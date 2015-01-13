package org.robobees.recyclerush;

import org.frc836.database.DB;
import org.frc836.database.MatchStatsStruct;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MatchStatsRR extends MatchStatsStruct {

	
	private static final int TOTES_IN_STACK = 6;
	private static final int COOP_TOTES_STACK = 4;
	
	public boolean auto_move;
	public short auto_totes;
	public boolean auto_stack_2;
	public boolean auto_stack_3;
	public short auto_bin;
	public short auto_step_bin;
	public short totes[] = new short[TOTES_IN_STACK];
	public short coops[] = new short[COOP_TOTES_STACK];
	public short bins[] = new short[TOTES_IN_STACK];
	public short bin_litter;
	public short landfill_litter;
	
	public MatchStatsRR() {
		super.init();
		init();
	}
	
	public MatchStatsRR(int team, String event, int match) {
		super(team, event, match);
		init();
	}
	
	public MatchStatsRR(int team, String event, int match, boolean practice) {
		super(team, event, match, practice);
		init();
	}
	
	public void init() {
		auto_move = false;
		auto_totes = 0;
		auto_stack_2 = false;
		auto_stack_3 = false;
		auto_bin = 0;
		auto_step_bin = 0;
		
		for (int i=0; i<TOTES_IN_STACK; i++)
			totes[i] = 0;
		for (int i=0; i<COOP_TOTES_STACK; i++)
			coops[i] = 0;
		for (int i=0; i<TOTES_IN_STACK; i++)
			bins[i] = 0;
		
		bin_litter = 0;
		landfill_litter = 0;
	}
	
	//TODO update when database is added
	public ContentValues getValues(DB db, SQLiteDatabase database) {
		ContentValues vals = super.getValues(db, database);
		
		
		return vals;
	}
	
	//TODO update when database is added
	public void fromCursor(Cursor c, DB db, SQLiteDatabase database) {
		super.fromCursor(c, db, database);
		
	}
	
}
