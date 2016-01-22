/*
 * Copyright 2016 Daniel Logan
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

package org.robobees.stronghold;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.frc836.database.DB;
import org.frc836.database.FRCScoutingContract.SCOUT_PIT_DATA_2016_Entry;
import org.frc836.database.PitStats;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PitStatsSH extends PitStats {
    public boolean start_spy;
    public boolean auto_reach;
    public boolean auto_cross;
    public int auto_score_low;
    public int auto_score_high;
    public boolean cross_portcullis;
    public boolean cross_cheval;
    public boolean cross_moat;
    public boolean cross_ramparts;
    public boolean cross_drawbridge_for;
    public boolean cross_drawbridge_for_with_help;
    public boolean cross_drawbridge_rev;
    public boolean cross_sally_for;
    public boolean cross_sally_for_with_help;
    public boolean cross_sally_rev;
    public boolean cross_rock_wall;
    public boolean cross_rough_terrain;
    public boolean cross_low_bar;
    public boolean score_high;
    public boolean score_low;
    public boolean challenge;
    public boolean scale;

    public PitStatsSH() {
        init();
    }

    @Override
    public void init() {
        super.init();
        start_spy = false;
        auto_reach = false;
        auto_cross = false;
        auto_score_low = 0;
        auto_score_high = 0;
        cross_portcullis = false;
        cross_cheval = false;
        cross_moat = false;
        cross_ramparts = false;
        cross_drawbridge_for = false;
        cross_drawbridge_for_with_help = false;
        cross_drawbridge_rev = false;
        cross_sally_for = false;
        cross_sally_for_with_help = false;
        cross_sally_rev = false;
        cross_rock_wall = false;
        cross_rough_terrain = false;
        cross_low_bar = false;
        score_high = false;
        score_low = false;
        challenge = false;
        scale = false;
    }

    @Override
    public ContentValues getValues(DB db, SQLiteDatabase database) {
        ContentValues vals = super.getValues(db, database);

        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_START_SPY, start_spy ? 1 : 0);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_AUTO_REACH, auto_reach ? 1 : 0);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS, auto_cross ? 1 : 0);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_AUTO_SCORE_LOW, auto_score_low);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_AUTO_SCORE_HIGH, auto_score_high);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_PORTCULLIS, cross_portcullis);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_CHEVAL, cross_cheval);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_MOAT, cross_moat);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_RAMPARTS, cross_ramparts);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_FOR, cross_drawbridge_for);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_FOR_WITH_HELP, cross_drawbridge_for_with_help);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_REV, cross_drawbridge_rev);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_FOR, cross_sally_for);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_FOR_WITH_HELP, cross_sally_for_with_help);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_REV, cross_sally_rev);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_ROCK_WALL, cross_rock_wall);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_ROUGH_TERRAIN, cross_rough_terrain);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_LOW_BAR, cross_low_bar);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_SCORE_HIGH, score_high);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_SCORE_LOW, score_low);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CHALLENGE, challenge);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_SCALE, scale);

        return vals;
    }

    @Override
    public void fromCursor(Cursor c, DB db, SQLiteDatabase database) {
        super.fromCursor(c, db, database);

        start_spy = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_START_SPY)) != 0;
        auto_reach = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_AUTO_REACH)) != 0;
        auto_cross = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS)) != 0;
        auto_score_low = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_AUTO_SCORE_LOW));
        auto_score_high = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_AUTO_SCORE_HIGH));
        cross_portcullis = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_PORTCULLIS)) != 0;
        cross_cheval = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_CHEVAL)) != 0;
        cross_moat = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_MOAT)) != 0;
        cross_ramparts = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_RAMPARTS)) != 0;
        cross_drawbridge_for = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_FOR)) != 0;
        cross_drawbridge_for_with_help = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_FOR_WITH_HELP)) != 0;
        cross_drawbridge_rev = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_REV)) != 0;
        cross_sally_for = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_FOR)) != 0;
        cross_sally_for_with_help = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_FOR_WITH_HELP)) != 0;
        cross_sally_rev = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_REV)) != 0;
        cross_rock_wall = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_ROCK_WALL)) != 0;
        cross_rough_terrain = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_ROUGH_TERRAIN)) != 0;
        cross_low_bar = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_LOW_BAR)) != 0;
        score_high = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_SCORE_HIGH)) != 0;
        score_low = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_SCORE_LOW)) != 0;
        challenge = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CHALLENGE)) != 0;
        scale = c.getInt(c.getColumnIndexOrThrow(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_SCALE)) != 0;
    }

    @Override
    public String[] getProjection() {
        String[] projection = super.getProjection();
        List<String> temp = new ArrayList<String>(Arrays.asList(projection));
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_START_SPY);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_AUTO_REACH);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_AUTO_SCORE_LOW);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_AUTO_SCORE_HIGH);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_PORTCULLIS);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_CHEVAL);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_MOAT);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_RAMPARTS);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_FOR);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_FOR_WITH_HELP);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_REV);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_FOR);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_FOR_WITH_HELP);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_REV);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_ROCK_WALL);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_ROUGH_TERRAIN);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_LOW_BAR);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_SCORE_HIGH);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_SCORE_LOW);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CHALLENGE);
        temp.add(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_SCALE);

        projection = new String[temp.size()];
        return temp.toArray(projection);
    }

    @Override
    public ContentValues jsonToCV(JSONObject json) throws JSONException {
        ContentValues vals = super.jsonToCV(json);
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_START_SPY, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_START_SPY));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_AUTO_REACH, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_AUTO_REACH));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_AUTO_SCORE_LOW, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_AUTO_SCORE_LOW));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_AUTO_SCORE_HIGH, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_AUTO_SCORE_HIGH));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_PORTCULLIS, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_PORTCULLIS));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_CHEVAL, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_CHEVAL));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_MOAT, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_MOAT));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_RAMPARTS, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_RAMPARTS));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_FOR, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_FOR));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_FOR_WITH_HELP, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_FOR_WITH_HELP));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_REV, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_REV));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_FOR, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_FOR));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_FOR_WITH_HELP, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_FOR_WITH_HELP));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_REV, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_REV));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_ROCK_WALL, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_ROCK_WALL));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_ROUGH_TERRAIN, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_ROUGH_TERRAIN));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_LOW_BAR, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CROSS_LOW_BAR));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_SCORE_HIGH, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_SCORE_HIGH));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_SCORE_LOW, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_SCORE_LOW));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CHALLENGE, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_CHALLENGE));
        vals.put(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_SCALE, json.getInt(SCOUT_PIT_DATA_2016_Entry.COLUMN_NAME_SCALE));

        return vals;
    }
}
