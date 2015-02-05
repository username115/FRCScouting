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
package org.robobees.recyclerush;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.frc836.database.DB;
import org.frc836.database.PitStats;
import org.frc836.database.FRCScoutingContract.SCOUT_PIT_DATA_2015_Entry;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PitStatsRR extends PitStats {
	public boolean push_tote;
	public boolean push_bin;
	public boolean lift_tote;
	public boolean lift_bin;
	public boolean push_litter;
	public boolean load_litter;
	public short stack_tote_height;
	public short stack_bin_height;
	public boolean coop_totes;
	public short coop_stack_height;
	public boolean move_auto;
	public short auto_bin_score;
	public short auto_tote_score;
	public short auto_tote_stack_height;
	public short auto_step_bins;
	public String manipulation_description;
	public boolean need_upright_tote;
	public boolean need_upright_bin;
	public boolean can_upright_tote;
	public boolean can_upright_bin;

	public PitStatsRR() {
		init();
	}

	@Override
	public void init() {
		super.init();
		push_tote = false;
		push_bin = false;
		lift_tote = false;
		lift_bin = false;
		push_litter = false;
		load_litter = false;
		stack_tote_height = 0;
		stack_bin_height = 0;
		coop_totes = false;
		coop_stack_height = 0;
		move_auto = false;
		auto_bin_score = 0;
		auto_tote_score = 0;
		auto_tote_stack_height = 0;
		auto_step_bins = 0;
		manipulation_description = "";
		need_upright_tote = false;
		need_upright_bin = false;
		can_upright_tote = false;
		can_upright_bin = false;
	}

	@Override
	public ContentValues getValues(DB db, SQLiteDatabase database) {
		ContentValues vals = super.getValues(db, database);

		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_PUSH_TOTE, push_tote ? 1
				: 0);
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_PUSH_BIN, push_bin ? 1
				: 0);
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_LIFT_TOTE, lift_tote ? 1
				: 0);
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_LIFT_BIN, lift_bin ? 1
				: 0);
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_PUSH_LITTER,
				push_litter ? 1 : 0);
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_LOAD_LITTER,
				load_litter ? 1 : 0);
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_STACK_TOTE_HEIGHT,
				stack_tote_height);
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_STACK_BIN_HEIGHT,
				stack_bin_height);
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_COOP_TOTES, coop_totes);
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_COOP_STACK_HEIGHT,
				coop_stack_height);
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_MOVE_AUTO, move_auto ? 1
				: 0);
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTO_BIN_SCORE,
				auto_bin_score);
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTO_TOTE_SCORE,
				auto_tote_score);
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTO_TOTE_STACK_HEIGHT,
				auto_tote_stack_height);
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTO_STEP_BINS,
				auto_step_bins);
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_MANIP_STYLE,
				manipulation_description);
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_NEED_UPRIGHT_TOTE,
				need_upright_tote ? 1 : 0);
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_NEED_UPRIGHT_BIN,
				need_upright_bin ? 1 : 0);
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_CAN_UPRIGHT_TOTE,
				can_upright_tote ? 1 : 0);
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_CAN_UPRIGHT_BIN,
				can_upright_bin ? 1 : 0);

		return vals;
	}

	@Override
	public void fromCursor(Cursor c, DB db, SQLiteDatabase database) {
		super.fromCursor(c, db, database);

		push_tote = c
				.getInt(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_PUSH_TOTE)) != 0;
		push_bin = c
				.getInt(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_PUSH_BIN)) != 0;
		lift_tote = c
				.getInt(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_LIFT_TOTE)) != 0;
		lift_bin = c
				.getInt(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_LIFT_BIN)) != 0;
		push_litter = c
				.getInt(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_PUSH_LITTER)) != 0;
		load_litter = c
				.getInt(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_LOAD_LITTER)) != 0;
		stack_tote_height = c
				.getShort(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_STACK_TOTE_HEIGHT));
		stack_bin_height = c
				.getShort(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_STACK_BIN_HEIGHT));
		coop_totes = c
				.getInt(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_COOP_TOTES)) != 0;
		coop_stack_height = c
				.getShort(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_COOP_STACK_HEIGHT));
		move_auto = c
				.getInt(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_MOVE_AUTO)) != 0;
		auto_bin_score = c
				.getShort(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTO_BIN_SCORE));
		auto_tote_score = c
				.getShort(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTO_TOTE_SCORE));
		auto_tote_stack_height = c
				.getShort(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTO_TOTE_STACK_HEIGHT));
		auto_step_bins = c
				.getShort(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTO_STEP_BINS));
		manipulation_description = c
				.getString(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_MANIP_STYLE));
		need_upright_tote = c
				.getInt(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_NEED_UPRIGHT_TOTE)) != 0;
		need_upright_bin = c
				.getInt(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_NEED_UPRIGHT_BIN)) != 0;
		can_upright_tote = c
				.getInt(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_CAN_UPRIGHT_TOTE)) != 0;
		can_upright_bin = c
				.getInt(c
						.getColumnIndexOrThrow(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_CAN_UPRIGHT_BIN)) != 0;
	}

	@Override
	public String[] getProjection() {
		String[] projection = super.getProjection();
		List<String> temp = new ArrayList<String>(Arrays.asList(projection));
		temp.add(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_PUSH_TOTE);
		temp.add(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_PUSH_BIN);
		temp.add(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_LIFT_TOTE);
		temp.add(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_LIFT_BIN);
		temp.add(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_PUSH_LITTER);
		temp.add(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_LOAD_LITTER);
		temp.add(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_STACK_TOTE_HEIGHT);
		temp.add(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_STACK_BIN_HEIGHT);
		temp.add(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_COOP_TOTES);
		temp.add(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_COOP_STACK_HEIGHT);
		temp.add(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_MOVE_AUTO);
		temp.add(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTO_BIN_SCORE);
		temp.add(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTO_TOTE_SCORE);
		temp.add(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTO_TOTE_STACK_HEIGHT);
		temp.add(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTO_STEP_BINS);
		temp.add(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_MANIP_STYLE);
		temp.add(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_NEED_UPRIGHT_BIN);
		temp.add(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_NEED_UPRIGHT_TOTE);
		temp.add(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_CAN_UPRIGHT_BIN);
		temp.add(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_CAN_UPRIGHT_TOTE);
		
		projection = new String[temp.size()];
		return temp.toArray(projection);
	}
	
	@Override
	public boolean isTextField(String column_name) {
		return SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_MANIP_STYLE
				.equalsIgnoreCase(column_name)
				|| super.isTextField(column_name);
	}
	
	@Override
	public ContentValues jsonToCV(JSONObject json) throws JSONException {
		ContentValues vals = super.jsonToCV(json);
		
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_PUSH_TOTE, json.getInt(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_PUSH_TOTE));
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_PUSH_BIN, json.getInt(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_PUSH_BIN));
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_LIFT_TOTE, json.getInt(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_LIFT_TOTE));
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_LIFT_BIN, json.getInt(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_LIFT_BIN));
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_PUSH_LITTER, json.getInt(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_PUSH_LITTER));
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_LOAD_LITTER, json.getInt(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_LOAD_LITTER));
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_STACK_TOTE_HEIGHT, json.getInt(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_STACK_TOTE_HEIGHT));
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_STACK_BIN_HEIGHT, json.getInt(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_STACK_BIN_HEIGHT));
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_COOP_TOTES, json.getInt(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_COOP_TOTES));
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_COOP_STACK_HEIGHT, json.getInt(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_COOP_STACK_HEIGHT));
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_MOVE_AUTO, json.getInt(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_MOVE_AUTO));
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTO_BIN_SCORE, json.getInt(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTO_BIN_SCORE));
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTO_TOTE_SCORE, json.getInt(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTO_TOTE_SCORE));
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTO_TOTE_STACK_HEIGHT, json.getInt(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTO_TOTE_STACK_HEIGHT));
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTO_STEP_BINS, json.getInt(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_AUTO_STEP_BINS));
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_MANIP_STYLE, json.getString(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_MANIP_STYLE));
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_NEED_UPRIGHT_BIN, json.getInt(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_NEED_UPRIGHT_BIN));
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_NEED_UPRIGHT_TOTE, json.getInt(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_NEED_UPRIGHT_TOTE));
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_CAN_UPRIGHT_BIN, json.getInt(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_CAN_UPRIGHT_BIN));
		vals.put(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_CAN_UPRIGHT_TOTE, json.getInt(SCOUT_PIT_DATA_2015_Entry.COLUMN_NAME_CAN_UPRIGHT_TOTE));
	
		return vals;
	}
}
