package org.frc836.yearly;

import android.database.sqlite.SQLiteDatabase;

import org.frc836.database.DB;
import org.frc836.database.MatchStatsStruct;
import org.frc836.database.ScoutingDBHelper;


public abstract class MatchStatsYearly {

    public static int getTotalScore(final MatchStatsStruct stats) {
        int score = 0;
        double pressure = 0;
        SQLiteDatabase db = ScoutingDBHelper.getInstance()
                .getReadableDatabase();

        // I'm going to assume all fuel scored by T=0 is counted.
        // This is supposed to be an estimate anyways
        pressure += stats.auto_score_low/3.0;
        pressure += stats.auto_score_high;
        pressure += stats.score_low/9.0;
        pressure += stats.score_high/3.0;

        score += pressure;

        score += stats.auto_cross_baseline ? 5 : 0;

        // TODO read from game-specific table for number of gears per rotor
        int rotor1 = 1;
        int rotor2 = 2;
        int rotor3 = DB.getGameInfoInt("2017_rotor_3_preinstalled", db, 4);
        int rotor4 = DB.getGameInfoInt("2017_rotor_4_preinstalled", db, 6);

        // Since this is also an estimate, I'm just going to assume no other gears delivered (including the free one)
        int auto_gears_delivered = stats.auto_gear_delivered_right + stats.auto_gear_delivered_left + stats.auto_gear_delivered_center;
        if (rotor1 <= auto_gears_delivered)
            score += 60;
        if (rotor2 <= (auto_gears_delivered - rotor1))
            score += 60;
        if (rotor3 <= (auto_gears_delivered - rotor1 - rotor2))
            score += 60;
        if (rotor4 <= (auto_gears_delivered - rotor1 - rotor2 - rotor3))
            score += 60;

        int gears_delivered = auto_gears_delivered + stats.gear_delivered_right + stats.gear_delivered_left + stats.gear_delivered_center;
        if (rotor1 <= gears_delivered && rotor1 > auto_gears_delivered)
            score += 40;
        if (rotor2 <= (gears_delivered - rotor1) && rotor2 > (auto_gears_delivered - rotor1))
            score += 40;
        if (rotor3 <= (gears_delivered - rotor1 - rotor2) && rotor3 > (auto_gears_delivered - rotor1 - rotor2))
            score += 40;
        if (rotor4 <= (gears_delivered - rotor1 - rotor2 - rotor3) && rotor4 > (auto_gears_delivered - rotor1 - rotor2 - rotor3))
            score += 40;

        score += stats.climb_rope ? 50 : 0;

        return score;
    }

    public static void clearAuto(MatchStatsStruct stats) {
        stats.auto_score_low = 0;
        stats.auto_score_high = 0;
        stats.auto_miss_high = 0;
        stats.auto_cross_baseline = false;
        stats.auto_gear_delivered_left = 0;
        stats.auto_gear_delivered_center = 0;
        stats.auto_gear_delivered_right = 0;
        stats.auto_dump_hopper = false;
    }

    public static void clearTele(MatchStatsStruct stats) {
        stats.score_low = 0;
        stats.score_high = 0;
        stats.miss_high = 0;
        stats.gear_delivered_left = 0;
        stats.gear_delivered_right = 0;
        stats.gear_delivered_center = 0;
    }
}
