package org.frc836.yearly;

import org.frc836.database.MatchStatsStruct;

import java.util.ArrayList;
import java.util.List;


public abstract class MatchStatsYearly {

    public static final int NUM_GRAPHS = 0;
    //public static final int AUTO = 0;
    //public static final int TOTAL_CARGO = 1;
    //public static final int TOTAL_HATCHES = 2;
    //public static final int HAB_CLIMB = 3;


    public static int getTotalScore(final MatchStatsStruct stats) {
        return 0; //no easy way to calculate score from stats this year, can't differentiate between inner and outer
    }

    public static List<String> getGraphNames() {
        List<String> ret = new ArrayList<String>(NUM_GRAPHS);
        //ret.add(SANDSTORM, "Total Sandstorm Points");
        //ret.add(TOTAL_CARGO, "Total Cargo Scored");
        //ret.add(TOTAL_HATCHES, "Total Hatches Scored");
        //ret.add(HAB_CLIMB, "HAB Climb Points");

        return ret;
    }

    public static List<String> getGraphShortNames() {
        List<String> ret = new ArrayList<String>(NUM_GRAPHS);
        //ret.add(SANDSTORM, "SS");
        //ret.add(TOTAL_CARGO, "Cargo");
        //ret.add(TOTAL_HATCHES, "Hatches");
        //ret.add(HAB_CLIMB, "HAB Pts");

        return ret;
    }

    public static int getStat(final int statNum, final MatchStatsStruct stats) {
        //switch (statNum) {
        //    case SANDSTORM:
        //        return getSandstormPoints(stats);
        //    case TOTAL_CARGO:
        //        return getTotalCargoScored(stats);
        //    case TOTAL_HATCHES:
        //        return getTotalHatchesScored(stats);
        //    case HAB_CLIMB:
        //        return getHabClimbPoints(stats);
        //    default:
        //        return getHabClimbPoints(stats);
        //}
		return 0;
    }



    public static void clearAuto(MatchStatsStruct stats) {
		stats.auto_taxi = false;
		stats.auto_low_score = 0;
		stats.auto_low_miss = 0;
		stats.auto_high_score = 0;
		stats.auto_high_miss = 0;
    }

    public static void clearTele(MatchStatsStruct stats) {
		stats.low_score = 0;
		stats.low_miss = 0;
		stats.high_score = 0;
		stats.high_miss = 0;
    }
}
