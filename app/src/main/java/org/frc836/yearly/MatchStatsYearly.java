package org.frc836.yearly;

import org.frc836.database.MatchStatsStruct;

import java.util.ArrayList;
import java.util.List;


public abstract class MatchStatsYearly {

    public static final int NUM_GRAPHS = 4;
    public static final int SANDSTORM = 0;
    public static final int TOTAL_CARGO = 1;
    public static final int TOTAL_HATCHES = 2;
    public static final int HAB_CLIMB = 3;


    public static int getTotalScore(final MatchStatsStruct stats) {
        return 0; //no way to calculate score from stats this year, time-based
    }

    public static List<String> getGraphNames() {
        List<String> ret = new ArrayList<String>(NUM_GRAPHS);
        ret.add(SANDSTORM, "Total Sandstorm Points");
        ret.add(TOTAL_CARGO, "Total Cargo Scored");
        ret.add(TOTAL_HATCHES, "Total Hatches Scored");
        ret.add(HAB_CLIMB, "HAB Climb Points");

        return ret;
    }

    public static List<String> getGraphShortNames() {
        List<String> ret = new ArrayList<String>(NUM_GRAPHS);
        ret.add(SANDSTORM, "SS");
        ret.add(TOTAL_CARGO, "Cargo");
        ret.add(TOTAL_HATCHES, "Hatches");
        ret.add(HAB_CLIMB, "HAB Pts");

        return ret;
    }

    public static int getStat(final int statNum, final MatchStatsStruct stats) {
        switch (statNum) {
            case SANDSTORM:
                return getSandstormPoints(stats);
            case TOTAL_CARGO:
                return getTotalCargoScored(stats);
            case TOTAL_HATCHES:
                return getTotalHatchesScored(stats);
            case HAB_CLIMB:
                return getHabClimbPoints(stats);
            default:
                return getHabClimbPoints(stats);
        }
    }

    public static int getSandstormPoints(final MatchStatsStruct stats) {
        int cargo = stats.sandstorm_cargo_ship
                + stats.sandstorm_cargo_rocket_1
                + stats.sandstorm_cargo_rocket_2
                + stats.sandstorm_cargo_rocket_3;
        int hatch = stats.sandstorm_hatch_ship
                + stats.sandstorm_hatch_rocket_1
                + stats.sandstorm_hatch_rocket_2
                + stats.sandstorm_hatch_rocket_3;
        int bonus = 0;
        if (stats.sandstorm_bonus)
        {
            if (stats.prematch_hab_level == 1)
                bonus = 3;
            else if (stats.prematch_hab_level > 1)
                bonus = 6;
        }
        return (cargo*3) + (hatch*2) + bonus;
    }

    public static int getTotalCargoScored(final MatchStatsStruct stats) {
        return stats.sandstorm_cargo_ship
                + stats.sandstorm_cargo_rocket_1
                + stats.sandstorm_cargo_rocket_2
                + stats.sandstorm_cargo_rocket_3
                + stats.cargo_ship
                + stats.cargo_rocket_1
                + stats.cargo_rocket_2
                + stats.cargo_rocket_3;
    }

    public static int getTotalHatchesScored(final MatchStatsStruct stats) {
        return stats.sandstorm_hatch_ship
                + stats.sandstorm_hatch_rocket_1
                + stats.sandstorm_hatch_rocket_2
                + stats.sandstorm_hatch_rocket_3
                + stats.hatch_ship
                + stats.hatch_rocket_1
                + stats.hatch_rocket_2
                + stats.hatch_rocket_3;
    }

    public static int getHabClimbPoints(final MatchStatsStruct stats) {
        return stats.hab_climb_level < 3 ? stats.hab_climb_level * 3 : 12;
    }


    public static void clearAuto(MatchStatsStruct stats) {
        stats.sandstorm_bonus = false;
        stats.sandstorm_cargo_ship = 0;
        stats.sandstorm_hatch_ship = 0;
        stats.sandstorm_cargo_rocket_1 = 0;
        stats.sandstorm_hatch_rocket_1 = 0;
        stats.sandstorm_cargo_rocket_2 = 0;
        stats.sandstorm_hatch_rocket_2 = 0;
        stats.sandstorm_cargo_rocket_3 = 0;
        stats.sandstorm_hatch_rocket_3 = 0;
        stats.sandstorm_cargo_dropped = 0;
        stats.sandstorm_hatch_dropped = 0;
    }

    public static void clearTele(MatchStatsStruct stats) {
        stats.cargo_ship = 0;
        stats.hatch_ship = 0;
        stats.cargo_rocket_1 = 0;
        stats.hatch_rocket_1 = 0;
        stats.cargo_rocket_2 = 0;
        stats.hatch_rocket_2 = 0;
        stats.cargo_rocket_3 = 0;
        stats.hatch_rocket_3 = 0;
        stats.cargo_dropped = 0;
        stats.hatch_dropped = 0;
    }
}
