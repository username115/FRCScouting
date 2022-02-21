package org.frc836.yearly;

import org.frc836.database.MatchStatsStruct;

import java.util.ArrayList;
import java.util.List;


public abstract class MatchStatsYearly {

    public static final int NUM_GRAPHS = 5;
	public static final int TOTAL_SCORE = 0;
    public static final int AUTO = 1;
    public static final int TOTAL_HIGH = 2;
    public static final int TOTAL_LOW = 3;
    public static final int CLIMB_LEVEL = 4;


    public static int getTotalScore(final MatchStatsStruct stats) {
		return getAutoScore(stats) + getTeleScore(stats) + getClimbScore(stats);
    }

    public static int getAutoScore(final MatchStatsStruct stats) {
		return (stats.auto_taxi ? 2 : 0)
				+ (stats.auto_high_score * 4)
				+ (stats.auto_low_score * 4);
	}

	public static int getTeleScore(final MatchStatsStruct stats) {
    	return (stats.high_score * 4)
				+ (stats.low_score * 4);
	}

	public static int getClimbScore(final MatchStatsStruct stats) {
    	switch (stats.hang_level) {
			case 1:
				return 4;
			case 2:
				return 6;
			case 3:
				return 10;
			case 4:
				return 16;
			case 0:
			default:
				return 0;
		}
	}

    public static List<String> getGraphNames() {
        List<String> ret = new ArrayList<>(NUM_GRAPHS);
        ret.add(TOTAL_SCORE, "Total Score");
        ret.add(AUTO, "Total Autonomous Score");
        ret.add(TOTAL_HIGH, "Total High Goals Scored");
        ret.add(TOTAL_LOW, "Total High Goals Scored");
		ret.add(CLIMB_LEVEL, "Hang Level");

        return ret;
    }

    public static List<String> getGraphShortNames() {
        List<String> ret = new ArrayList<>(NUM_GRAPHS);
		ret.add(TOTAL_SCORE, "TS");
		ret.add(AUTO, "AS");
		ret.add(TOTAL_HIGH, "HG");
		ret.add(TOTAL_LOW, "LG");
		ret.add(CLIMB_LEVEL, "HL");

        return ret;
    }

    public static int getStat(final int statNum, final MatchStatsStruct stats) {
    	switch (statNum) {
			case TOTAL_SCORE:
				return getTotalScore(stats);
			case AUTO:
				return getAutoScore(stats);
			case TOTAL_HIGH:
				return stats.auto_high_score + stats.high_score;
			case TOTAL_LOW:
				return stats.auto_low_score + stats.low_score;
			case CLIMB_LEVEL:
			default:
				return stats.hang_level;
		}
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
