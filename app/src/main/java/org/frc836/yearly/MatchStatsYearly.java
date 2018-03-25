package org.frc836.yearly;

import org.frc836.database.MatchStatsStruct;

import java.util.ArrayList;
import java.util.List;


public abstract class MatchStatsYearly {

    public static final int NUM_GRAPHS = 3;
    public static final int TOTAL_CUBES = 0;
    public static final int EXCHANGE_CUBES = 1;
    public static final int PARK_CLIMB_POINTS = 2;

    public static int getTotalScore(final MatchStatsStruct stats) {
        return 0; //no way to calculate score from stats this year, time-based
    }

    public static List<String> getGraphNames() {
        List<String> ret = new ArrayList<String>(NUM_GRAPHS);
        ret.add(TOTAL_CUBES,"Total Cubes Scored");
        ret.add(EXCHANGE_CUBES, "Cubes in Exchange");
        ret.add(PARK_CLIMB_POINTS, "Park/Climb Points");

        return ret;
    }

    public static int getStat(final int statNum, final MatchStatsStruct stats) {
        switch (statNum) {
            case TOTAL_CUBES:
                return getTotalCubesScored(stats);
            case EXCHANGE_CUBES:
                return getExchangeCount(stats);
            case PARK_CLIMB_POINTS:
            default:
                return getPnCPoints(stats);
        }
    }

    public static int getTotalCubesScored(final MatchStatsStruct stats) {
        return stats.auto_switch_count + stats.auto_scale_count + stats.switch_count
                + stats.scale_count + stats.opposite_switch_count;
    }

    public static int getExchangeCount(final MatchStatsStruct stats) {
        return stats.auto_exchange_count + stats.exchange_count;
    }

    public static int getPnCPoints(final MatchStatsStruct stats) {
        return (stats.parked ? 5 : 0) + (stats.climbed ? 30 : 0) + (stats.supported_others ? 30 : 0);
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
