package org.frc836.yearly;

import org.frc836.database.MatchStatsStruct;


public abstract class MatchStatsYearly {

    public static int getTotalScore(final MatchStatsStruct stats) {
        return 0; //no way to calculate score from stats this year, time-based
    }

    public static void clearAuto(MatchStatsStruct stats) {
        stats.auto_switch_count = 0;
        stats.auto_switch_wrong_side_count = 0;
        stats.auto_scale_count = 0;
        stats.auto_scale_wrong_side_count = 0;
        stats.auto_exchange_count = 0;
        stats.auto_run = false;
    }

    public static void clearTele(MatchStatsStruct stats) {
        stats.switch_count = 0;
        stats.switch_wrong_side_count = 0;
        stats.scale_count = 0;
        stats.scale_wrong_side_count = 0;
        stats.opposite_switch_count = 0;
        stats.opposite_switch_wrong_side_count = 0;
    }
}
