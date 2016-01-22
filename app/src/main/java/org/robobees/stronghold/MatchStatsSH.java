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
import org.frc836.database.FRCScoutingContract.FACT_MATCH_DATA_2016_Entry;
import org.frc836.database.MatchStatsStruct;

public class MatchStatsSH extends MatchStatsStruct {

    public short red_def_2;
    public short red_def_3;
    public short red_def_4;
    public short red_def_5;
    public short blue_def_2;
    public short blue_def_3;
    public short blue_def_4;
    public short blue_def_5;
    public boolean auto_reach;
    public int auto_cross_portcullis_for;
    public int auto_cross_portcullis_rev;
    public int auto_cross_cheval_for;
    public int auto_cross_cheval_rev;
    public int auto_cross_moat_for;
    public int auto_cross_moat_rev;
    public int auto_cross_ramparts_for;
    public int auto_cross_ramparts_rev;
    public int auto_cross_drawbridge_for;
    public int auto_cross_drawbridge_for_with_help;
    public int auto_cross_drawbridge_rev;
    public int auto_cross_sally_for;
    public int auto_cross_sally_for_with_help;
    public int auto_cross_sally_rev;
    public int auto_cross_rock_wall_for;
    public int auto_cross_rock_wall_rev;
    public int auto_cross_rough_terrain_for;
    public int auto_cross_rough_terrain_rev;
    public int auto_cross_low_bar_for;
    public int auto_cross_low_bar_rev;
    public int auto_score_low;
    public int auto_score_high;
    public int cross_portcullis_for;
    public int cross_portcullis_rev;
    public int cross_cheval_for;
    public int cross_cheval_rev;
    public int cross_moat_for;
    public int cross_moat_rev;
    public int cross_ramparts_for;
    public int cross_ramparts_rev;
    public int cross_drawbridge_for;
    public int cross_drawbridge_for_with_help;
    public int cross_drawbridge_rev;
    public int cross_sally_for;
    public int cross_sally_for_with_help;
    public int cross_sally_rev;
    public int cross_rock_wall_for;
    public int cross_rock_wall_rev;
    public int cross_rough_terrain_for;
    public int cross_rough_terrain_rev;
    public int cross_low_bar_for;
    public int cross_low_bar_rev;
    public int score_low;
    public int score_high;
    public boolean challenge;
    public boolean scale;

    public MatchStatsSH() {
        super.init();
        init();
    }

    public MatchStatsSH(int team, String event, int match) {
        super(team, event, match);
        init();
    }

    public MatchStatsSH(int team, String event, int match, boolean practice) {
        super(team, event, match, practice);
        init();
    }

    public void init() {
        red_def_2 = 0;
        red_def_3 = 0;
        red_def_4 = 0;
        red_def_5 = 0;
        blue_def_2 = 0;
        blue_def_3 = 0;
        blue_def_4 = 0;
        blue_def_5 = 0;
        auto_reach = false;
        auto_cross_portcullis_for = 0;
        auto_cross_portcullis_rev = 0;
        auto_cross_cheval_for = 0;
        auto_cross_cheval_rev = 0;
        auto_cross_moat_for = 0;
        auto_cross_moat_rev = 0;
        auto_cross_ramparts_for = 0;
        auto_cross_ramparts_rev = 0;
        auto_cross_drawbridge_for = 0;
        auto_cross_drawbridge_for_with_help = 0;
        auto_cross_drawbridge_rev = 0;
        auto_cross_sally_for = 0;
        auto_cross_sally_for_with_help = 0;
        auto_cross_sally_rev = 0;
        auto_cross_rock_wall_for = 0;
        auto_cross_rock_wall_rev = 0;
        auto_cross_rough_terrain_for = 0;
        auto_cross_rough_terrain_rev = 0;
        auto_cross_low_bar_for = 0;
        auto_cross_low_bar_rev = 0;
        auto_score_low = 0;
        auto_score_high = 0;
        cross_portcullis_for = 0;
        cross_portcullis_rev = 0;
        cross_cheval_for = 0;
        cross_cheval_rev = 0;
        cross_moat_for = 0;
        cross_moat_rev = 0;
        cross_ramparts_for = 0;
        cross_ramparts_rev = 0;
        cross_drawbridge_for = 0;
        cross_drawbridge_for_with_help = 0;
        cross_drawbridge_rev = 0;
        cross_sally_for = 0;
        cross_sally_for_with_help = 0;
        cross_sally_rev = 0;
        cross_rock_wall_for = 0;
        cross_rock_wall_rev = 0;
        cross_rough_terrain_for = 0;
        cross_rough_terrain_rev = 0;
        cross_low_bar_for = 0;
        cross_low_bar_rev = 0;
        score_low = 0;
        score_high = 0;
        challenge = false;
        scale = false;
    }

    public ContentValues getValues(DB db, SQLiteDatabase database) {
        ContentValues vals = super.getValues(db, database);

        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_RED_DEF_2, red_def_2);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_RED_DEF_3, red_def_3);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_RED_DEF_4, red_def_4);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_RED_DEF_5, red_def_5);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_BLUE_DEF_2, blue_def_2);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_BLUE_DEF_3, blue_def_3);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_BLUE_DEF_4, blue_def_4);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_BLUE_DEF_5, blue_def_5);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_RED_DEF_2, red_def_2);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_RED_DEF_3, red_def_3);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_RED_DEF_4, red_def_4);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_REACH, auto_reach ? 1 : 0);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_PORTCULLIS_FOR, auto_cross_portcullis_for);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_PORTCULLIS_REV, auto_cross_portcullis_rev);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_CHEVAL_FOR, auto_cross_cheval_for);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_CHEVAL_REV, auto_cross_cheval_rev);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_MOAT_FOR, auto_cross_moat_rev);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_MOAT_REV, auto_cross_moat_rev);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_RAMPARTS_FOR, auto_cross_ramparts_for);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_RAMPARTS_REV, auto_cross_ramparts_rev);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_DRAWBRIDGE_FOR, auto_cross_drawbridge_for);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_DRAWBRIDGE_FOR_WITH_HELP, auto_cross_drawbridge_for_with_help);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_DRAWBRIDGE_REV, auto_cross_drawbridge_rev);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_SALLY_FOR, auto_cross_sally_for);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_SALLY_FOR_WITH_HELP, auto_cross_sally_for_with_help);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_SALLY_REV, auto_cross_sally_rev);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_ROCK_WALL_FOR, auto_cross_rock_wall_for);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_ROCK_WALL_REV, auto_cross_rock_wall_rev);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_ROUGH_TERRAIN_FOR, auto_cross_rough_terrain_for);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_ROUGH_TERRAIN_REV, auto_cross_rough_terrain_rev);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_LOW_BAR_FOR, auto_cross_low_bar_for);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_LOW_BAR_REV, auto_cross_low_bar_rev);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_SCORE_LOW, auto_score_low);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_SCORE_HIGH, auto_score_high);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_PORTCULLIS_FOR, cross_portcullis_for);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_PORTCULLIS_REV, cross_portcullis_rev);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_CHEVAL_FOR, cross_cheval_for);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_CHEVAL_REV, cross_cheval_rev);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_MOAT_FOR, cross_moat_rev);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_MOAT_REV, cross_moat_rev);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_RAMPARTS_FOR, cross_ramparts_for);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_RAMPARTS_REV, cross_ramparts_rev);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_FOR, cross_drawbridge_for);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_FOR_WITH_HELP, cross_drawbridge_for_with_help);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_REV, cross_drawbridge_rev);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_FOR, cross_sally_for);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_FOR_WITH_HELP, cross_sally_for_with_help);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_REV, cross_sally_rev);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_ROCK_WALL_FOR, cross_rock_wall_for);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_ROCK_WALL_REV, cross_rock_wall_rev);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_ROUGH_TERRAIN_FOR, cross_rough_terrain_for);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_ROUGH_TERRAIN_REV, cross_rough_terrain_rev);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_LOW_BAR_FOR, cross_low_bar_for);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_LOW_BAR_REV, cross_low_bar_rev);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_SCORE_LOW, score_low);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_SCORE_HIGH, score_high);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CHALLENGE, challenge);
        vals.put(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_SCALE, scale);

        return vals;
    }

    public void fromCursor(Cursor c, DB db, SQLiteDatabase database) {
        super.fromCursor(c, db, database);

        red_def_2 = c.getShort(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_RED_DEF_2));
        red_def_3 = c.getShort(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_RED_DEF_3));
        red_def_4 = c.getShort(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_RED_DEF_4));
        red_def_5 = c.getShort(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_RED_DEF_5));
        blue_def_2 = c.getShort(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_BLUE_DEF_2));
        blue_def_3 = c.getShort(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_BLUE_DEF_3));
        blue_def_4 = c.getShort(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_BLUE_DEF_4));
        blue_def_5 = c.getShort(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_BLUE_DEF_5));
        auto_reach = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_REACH)) != 0;
        auto_cross_portcullis_for = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_PORTCULLIS_FOR));
        auto_cross_portcullis_rev = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_PORTCULLIS_REV));
        auto_cross_cheval_for = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_CHEVAL_FOR));
        auto_cross_cheval_rev = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_CHEVAL_REV));
        auto_cross_moat_for = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_MOAT_FOR));
        auto_cross_moat_rev = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_MOAT_REV));
        auto_cross_ramparts_for = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_RAMPARTS_FOR));
        auto_cross_ramparts_rev = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_RAMPARTS_REV));
        auto_cross_drawbridge_for = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_DRAWBRIDGE_FOR));
        auto_cross_drawbridge_for_with_help = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_DRAWBRIDGE_FOR_WITH_HELP));
        auto_cross_drawbridge_rev = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_DRAWBRIDGE_REV));
        auto_cross_sally_for = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_SALLY_FOR));
        auto_cross_sally_for_with_help = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_SALLY_FOR_WITH_HELP));
        auto_cross_sally_rev = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_SALLY_REV));
        auto_cross_rock_wall_for = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_ROCK_WALL_FOR));
        auto_cross_rock_wall_rev = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_ROCK_WALL_REV));
        auto_cross_rough_terrain_for = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_ROUGH_TERRAIN_FOR));
        auto_cross_rough_terrain_rev = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_ROUGH_TERRAIN_REV));
        auto_cross_low_bar_for = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_LOW_BAR_FOR));
        auto_cross_low_bar_rev = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_CROSS_LOW_BAR_REV));
        auto_score_low = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_SCORE_LOW));
        auto_score_high = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_AUTO_SCORE_HIGH));
        cross_portcullis_for = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_PORTCULLIS_FOR));
        cross_portcullis_rev = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_PORTCULLIS_REV));
        cross_cheval_for = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_CHEVAL_FOR));
        cross_cheval_rev = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_CHEVAL_REV));
        cross_moat_for = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_MOAT_FOR));
        cross_moat_rev = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_MOAT_REV));
        cross_ramparts_for = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_RAMPARTS_FOR));
        cross_ramparts_rev = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_RAMPARTS_REV));
        cross_drawbridge_for = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_FOR));
        cross_drawbridge_for_with_help = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_FOR_WITH_HELP));
        cross_drawbridge_rev = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_DRAWBRIDGE_REV));
        cross_sally_for = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_FOR));
        cross_sally_for_with_help = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_FOR_WITH_HELP));
        cross_sally_rev = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_SALLY_REV));
        cross_rock_wall_for = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_ROCK_WALL_FOR));
        cross_rock_wall_rev = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_ROCK_WALL_REV));
        cross_rough_terrain_for = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_ROUGH_TERRAIN_FOR));
        cross_rough_terrain_rev = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_ROUGH_TERRAIN_REV));
        cross_low_bar_for = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_LOW_BAR_FOR));
        cross_low_bar_rev = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CROSS_LOW_BAR_REV));
        score_low = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_SCORE_LOW));
        score_high = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_SCORE_HIGH));
        challenge = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_CHALLENGE)) != 0;
        scale = c.getInt(c.getColumnIndexOrThrow(FACT_MATCH_DATA_2016_Entry.COLUMN_NAME_SCALE)) != 0;
    }
    // TODO
}
