/*
 * Copyright 2013 Daniel Logan
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

package org.frc836.database;

import java.util.HashMap;
import java.util.Map;

import org.frc836.database.FRCScoutingContract.FACT_MATCH_DATA_Entry;

import android.content.ContentValues;

public abstract class MatchStatsStruct {
	
	

	public int team;
	public String event;
	public int match;
	public boolean autonomous;
	public String notes;
	public boolean tipOver;
	public boolean foul;
	public boolean tech_foul;
	public boolean yellowCard;
	public boolean redCard;
	
	public MatchStatsStruct()
	{
		init();
	}
	
	
	public MatchStatsStruct(int team, String event, int match)
	{
		this.team = team;
		this.event = event;
		this.match = match;
		init();
	}
	
	public MatchStatsStruct(int team, String event, int match, boolean auto)
	{

		init();
		this.team = team;
		this.event = event;
		this.match = match;
		autonomous = auto;
	}
	
	public void init()
	{
		autonomous = true;
		tipOver = false;
		notes = "";
		foul = false;
		tech_foul = false;
		yellowCard = false;
		redCard = false;
	}
	
	public Map<String, String> getPost()
	{
		HashMap<String, String> args = new HashMap<String, String>();
		args.put("team_id", String.valueOf(team));
		args.put("event_id", event);
		args.put("match_id", String.valueOf(match));
		args.put("notes", notes);
		args.put("tip_over", String.valueOf(tipOver?1:0));
		args.put("penalty", String.valueOf(foul?1:0));
		args.put("mpenalty", String.valueOf(tech_foul?1:0));
		args.put("yellow_card", String.valueOf(yellowCard?1:0));
		args.put("red_card", String.valueOf(redCard?1:0));
		
		return args;
	}
	
	public ContentValues getValues(DB db)
	{
		ContentValues args = new ContentValues();
		args.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_TEAM_ID, team);
		args.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_EVENT_ID, db.getEventIDFromName(event));
		args.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_MATCH_ID, match);
		args.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_NOTES, notes);
		args.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_TIP_OVER, tipOver?1:0);
		args.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_FOUL, foul?1:0);
		args.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_TECH_FOUL, tech_foul?1:0);
		args.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_YELLOW_CARD, yellowCard?1:0);
		args.put(FACT_MATCH_DATA_Entry.COLUMN_NAME_RED_CARD, redCard?1:0);
		
		return args;
	}
}
