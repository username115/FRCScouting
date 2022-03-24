package org.frc836.yearly

import org.frc836.database.MatchStatsStruct
import java.util.ArrayList

object MatchStatsYearly {
    const val NUM_GRAPHS = 5
    const val TOTAL_SCORE = 0
    const val AUTO = 1
    const val TOTAL_HIGH = 2
    const val TOTAL_LOW = 3
    const val CLIMB_LEVEL = 4

    fun getTotalScore(stats: MatchStatsStruct): Int {
        return getAutoScore(stats) + getTeleScore(stats) + getClimbScore(stats)
    }

    fun getAutoScore(stats: MatchStatsStruct): Int {
        return ((if (stats.auto_taxi) 2 else 0)
                + stats.auto_high_score * 4
                + stats.auto_low_score * 4)
    }

    fun getTeleScore(stats: MatchStatsStruct): Int {
        return (stats.high_score * 4
                + stats.low_score * 4)
    }

    fun getClimbScore(stats: MatchStatsStruct): Int {
        return when (stats.hang_level) {
            1 -> 4
            2 -> 6
            3 -> 10
            4 -> 16
            0 -> 0
            else -> 0
        }
    }

    @JvmStatic
	val graphNames: List<String> by lazy {
		val ret: MutableList<String> = ArrayList(NUM_GRAPHS)
		ret.add(TOTAL_SCORE, "Total Score")
		ret.add(AUTO, "Total Autonomous Score")
		ret.add(TOTAL_HIGH, "Total High Goals Scored")
		ret.add(TOTAL_LOW, "Total High Goals Scored")
		ret.add(CLIMB_LEVEL, "Hang Level")
		ret
	}

    @JvmStatic
	val graphShortNames: List<String> by lazy {
		val ret: MutableList<String> = ArrayList(NUM_GRAPHS)
		ret.add(TOTAL_SCORE, "TS")
		ret.add(AUTO, "AS")
		ret.add(TOTAL_HIGH, "HG")
		ret.add(TOTAL_LOW, "LG")
		ret.add(CLIMB_LEVEL, "HL")
		ret
	}

    @JvmStatic
	fun getStat(statNum: Int, stats: MatchStatsStruct): Int {
        return when (statNum) {
            TOTAL_SCORE -> getTotalScore(stats)
            AUTO -> getAutoScore(stats)
            TOTAL_HIGH -> stats.auto_high_score + stats.high_score
            TOTAL_LOW -> stats.auto_low_score + stats.low_score
            CLIMB_LEVEL -> stats.hang_level
            else -> stats.hang_level
        }
    }

    @JvmStatic
	fun clearAuto(stats: MatchStatsStruct) {
        stats.auto_taxi = false
        stats.auto_low_score = 0
        stats.auto_low_miss = 0
        stats.auto_high_score = 0
        stats.auto_high_miss = 0
    }

    @JvmStatic
	fun clearTele(stats: MatchStatsStruct) {
        stats.low_score = 0
        stats.low_miss = 0
        stats.high_score = 0
        stats.high_miss = 0
    }
}
