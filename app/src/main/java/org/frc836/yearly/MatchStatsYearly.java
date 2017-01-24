package org.frc836.yearly;

import org.frc836.database.MatchStatsStruct;



public class MatchStatsYearly extends MatchStatsStruct {
    public MatchStatsYearly() {
        super.init();
    }

    public MatchStatsYearly(int team, String event, int match) {
        super(team, event, match);
    }

    public MatchStatsYearly(int team, String event, int match, boolean practice) {
        super(team, event, match, practice);
    }

    public int getTotalScore() {
        int score = 0;
        // TODO

        return score;
    }
}
