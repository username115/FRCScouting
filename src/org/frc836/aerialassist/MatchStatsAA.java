/*
 * Copyright 2014 Daniel Logan
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

package org.frc836.aerialassist;

import java.util.ArrayList;
import java.util.List;

import org.frc836.database.DB;
import org.frc836.database.MatchStatsStruct;
import org.frc836.database.FRCScoutingContract.FACT_CYCLE_DATA_Entry;
import org.frc836.database.FRCScoutingContract.FACT_MATCH_DATA_Entry;

import android.content.ContentValues;

public class MatchStatsAA extends MatchStatsStruct {

	public int auto_high;
	public int auto_high_hot;
	public int auto_low;
	public int auto_low_hot;
	public int high;
	public int low;
	public boolean auto_mobile;

	public List<CycleStatsStruct> cycles;

	public MatchStatsAA() {
		init();
	}

	public MatchStatsAA(int team, String event, int match) {
		super(team, event, match);
		init();
	}

	public MatchStatsAA(int team, String event, int match, boolean auto) {
		super(team, event, match, auto);
		init();
	}

	public void init() {
		super.init();
		auto_high = 0;
		auto_high_hot = 0;
		auto_low = 0;
		auto_low_hot = 0;
		high = 0;
		low = 0;
		auto_mobile = false;
		cycles = new ArrayList<MatchStatsAA.CycleStatsStruct>();
	}

	public ContentValues getValues(DB db) {
		ContentValues vals = super.getValues(db);

		vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_AUTO_HIGH, auto_high);
		vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_AUTO_HIGH_HOT, auto_high_hot);
		vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_AUTO_LOW, auto_low);
		vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_AUTO_LOW_HOT, auto_low_hot);
		vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_HIGH, high);
		vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_LOW, low);
		vals.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_AUTO_MOBILE, auto_mobile ? 1
				: 0);

		return vals;
	}

	public List<ContentValues> getCycles() {
		List<ContentValues> vals = new ArrayList<ContentValues>(cycles.size());

		for (CycleStatsStruct cycle : cycles) {
			vals.add(cycle.getValues());
		}

		return vals;
	}

	public class CycleStatsStruct {
		public int cycle_number;
		public boolean red_poss;
		public boolean white_poss;
		public boolean blue_poss;
		public boolean truss;
		public int truss_attempt;
		public boolean truss_catch;
		public boolean truss_catch_attempt;
		public boolean high;
		public int high_attempt;
		public boolean low;
		public int low_attempt;
		public int assists;

		public ContentValues getValues() {
			ContentValues vals = new ContentValues();

			vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_CYCLE_NUM, cycle_number);
			vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_RED_POSS, red_poss ? 1
					: 0);
			vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_WHITE_POSS,
					white_poss ? 1 : 0);
			vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_BLUE_POSS, blue_poss ? 1
					: 0);
			vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_TRUSS, truss ? 1 : 0);
			vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_TRUSS_ATTEMPT,
					truss_attempt);
			vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_CATCH, truss_catch ? 1
					: 0);
			vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_CATCH_ATTEMPT,
					truss_catch_attempt ? 1 : 0);
			vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_HIGH, high ? 1 : 0);
			vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_HIGH_ATTEMPT,
					high_attempt);
			vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_LOW, low ? 1 : 0);
			vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_LOW_ATTEMPT, low_attempt);
			vals.put(FACT_CYCLE_DATA_Entry.COLUMN_NAME_ASSISTS, assists);

			return vals;
		}
	}

}
