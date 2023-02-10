package org.frc836.yearly

import org.frc836.database.MatchStatsStruct

object MatchStatsYearly {
    const val NUM_GRAPHS = 4
    const val TOTAL_SCORE = 0
    const val AUTO = 1
    const val TELEOP = 2
    const val ENGAGE = 3

    @JvmStatic
    fun getTotalScore(stats: MatchStatsStruct): Int {
        return getAutoScore(stats) + getTeleScore(stats) + getEndEngageScore(stats)
    }

    @JvmStatic
    fun getAutoScore(stats: MatchStatsStruct): Int {
        return ((if (stats.auto_mobility) 3 else 0)
            + getAutoHybScore(stats)
            + getAutoMidScore(stats)
            + getAutoTopScore(stats)
            + getAutoEngageScore(stats))
    }

    @JvmStatic
    fun getTeleScore(stats: MatchStatsStruct): Int {
        return (getLinkScore(stats)
            + getTeleHybScore(stats)
            + getTeleMidScore(stats)
            + getTeleTopScore(stats))
    }

    @JvmStatic
    fun getEngageScore(stats: MatchStatsStruct): Int {
        return (getAutoEngageScore(stats) + getEndEngageScore(stats))
    }

    @JvmStatic
    fun getAutoEngageScore(stats: MatchStatsStruct): Int {
        return when (stats.auto_charge_station) {
            2 -> 8
            3 -> 12
            else -> 0
        }
    }

    @JvmStatic
    fun getEndEngageScore(stats: MatchStatsStruct): Int {
        return when (stats.charge_station) {
            2 -> 6
            3 -> 10
            else -> 0
        }
    }

    @JvmStatic
    fun getAutoHybScore(stats: MatchStatsStruct): Int {
        return ((
              if (stats.auto_wall_grid_hyb_wall) 1 else 0
            + if (stats.auto_wall_grid_hyb_mid) 1 else 0
            + if (stats.auto_wall_grid_hyb_substn) 1 else 0
            + if (stats.auto_coop_grid_hyb_wall) 1 else 0
            + if (stats.auto_coop_grid_hyb_mid) 1 else 0
            + if (stats.auto_coop_grid_hyb_substn) 1 else 0
            + if (stats.auto_substn_grid_hyb_wall) 1 else 0
            + if (stats.auto_substn_grid_hyb_mid) 1 else 0
            + if (stats.auto_substn_grid_hyb_substn) 1 else 0)
            * 3)
    }

    @JvmStatic
    fun getAutoMidScore(stats: MatchStatsStruct): Int {
        return ((
              if (stats.auto_wall_grid_mid_wall) 1 else 0
            + if (stats.auto_wall_grid_mid_mid) 1 else 0
            + if (stats.auto_wall_grid_mid_substn) 1 else 0
            + if (stats.auto_coop_grid_mid_wall) 1 else 0
            + if (stats.auto_coop_grid_mid_mid) 1 else 0
            + if (stats.auto_coop_grid_mid_substn) 1 else 0
            + if (stats.auto_substn_grid_mid_wall) 1 else 0
            + if (stats.auto_substn_grid_mid_mid) 1 else 0
            + if (stats.auto_substn_grid_mid_substn) 1 else 0)
            * 4)
    }

    @JvmStatic
    fun getAutoTopScore(stats: MatchStatsStruct): Int {
        return ((
              if (stats.auto_wall_grid_top_wall) 1 else 0
            + if (stats.auto_wall_grid_top_mid) 1 else 0
            + if (stats.auto_wall_grid_top_substn) 1 else 0
            + if (stats.auto_coop_grid_top_wall) 1 else 0
            + if (stats.auto_coop_grid_top_mid) 1 else 0
            + if (stats.auto_coop_grid_top_substn) 1 else 0
            + if (stats.auto_substn_grid_top_wall) 1 else 0
            + if (stats.auto_substn_grid_top_mid) 1 else 0
            + if (stats.auto_substn_grid_top_substn) 1 else 0)
            * 6)
    }

    @JvmStatic
    fun getTeleHybScore(stats: MatchStatsStruct): Int {
        return ((
              if (stats.wall_grid_hyb_wall) 1 else 0
            + if (stats.wall_grid_hyb_mid) 1 else 0
            + if (stats.wall_grid_hyb_substn) 1 else 0
            + if (stats.coop_grid_hyb_wall) 1 else 0
            + if (stats.coop_grid_hyb_mid) 1 else 0
            + if (stats.coop_grid_hyb_substn) 1 else 0
            + if (stats.substn_grid_hyb_wall) 1 else 0
            + if (stats.substn_grid_hyb_mid) 1 else 0
            + if (stats.substn_grid_hyb_substn) 1 else 0)
            * 2)
    }

    @JvmStatic
    fun getTeleMidScore(stats: MatchStatsStruct): Int {
        return ((
              if (stats.wall_grid_mid_wall) 1 else 0
            + if (stats.wall_grid_mid_mid) 1 else 0
            + if (stats.wall_grid_mid_substn) 1 else 0
            + if (stats.coop_grid_mid_wall) 1 else 0
            + if (stats.coop_grid_mid_mid) 1 else 0
            + if (stats.coop_grid_mid_substn) 1 else 0
            + if (stats.substn_grid_mid_wall) 1 else 0
            + if (stats.substn_grid_mid_mid) 1 else 0
            + if (stats.substn_grid_mid_substn) 1 else 0)
            * 3)
    }

    @JvmStatic
    fun getTeleTopScore(stats: MatchStatsStruct): Int {
        return ((
              if (stats.wall_grid_top_wall) 1 else 0
            + if (stats.wall_grid_top_mid) 1 else 0
            + if (stats.wall_grid_top_substn) 1 else 0
            + if (stats.coop_grid_top_wall) 1 else 0
            + if (stats.coop_grid_top_mid) 1 else 0
            + if (stats.coop_grid_top_substn) 1 else 0
            + if (stats.substn_grid_top_wall) 1 else 0
            + if (stats.substn_grid_top_mid) 1 else 0
            + if (stats.substn_grid_top_substn) 1 else 0)
            * 5)
    }

    @JvmStatic
    fun getLinkScore(stats:MatchStatsStruct): Int {
        return 0
        //TODO
    }

    @JvmStatic
    val graphNames: List<String> by lazy {
        val ret: MutableList<String> = ArrayList(NUM_GRAPHS)
        ret.add(TOTAL_SCORE, "Total Score")
        ret.add(AUTO, "Total Autonomous Score")
        ret.add(TELEOP, "Total Tele-Op score (without Charge Station)")
        ret.add(ENGAGE, "Total Charge Station Score")
        ret
    }

    @JvmStatic
    val graphShortNames: List<String> by lazy {
        val ret: MutableList<String> = ArrayList(NUM_GRAPHS)
        ret.add(TOTAL_SCORE, "TS")
        ret.add(AUTO, "AS")
        ret.add(TELEOP, "TE")
        ret.add(ENGAGE, "EN")
        ret
    }

    @JvmStatic
    fun getStat(statNum: Int, stats: MatchStatsStruct): Int {
        return when (statNum) {
            TOTAL_SCORE -> getTotalScore(stats)
            AUTO -> getAutoScore(stats)
            TELEOP -> getTeleScore(stats)
            ENGAGE -> getEngageScore(stats)
            else -> getTotalScore(stats)
        }
    }

    @JvmStatic
    fun clearAuto(stats: MatchStatsStruct) {
        stats.auto_charge_station = 0
        stats.auto_mobility = false
        stats.auto_wall_grid_hyb_wall = false
        stats.auto_wall_grid_hyb_mid = false
        stats.auto_wall_grid_hyb_substn = false
        stats.auto_coop_grid_hyb_wall = false
        stats.auto_coop_grid_hyb_mid = false
        stats.auto_coop_grid_hyb_substn = false
        stats.auto_substn_grid_hyb_wall = false
        stats.auto_substn_grid_hyb_mid = false
        stats.auto_substn_grid_hyb_substn = false
        stats.auto_wall_grid_mid_wall = false
        stats.auto_wall_grid_mid_mid = false
        stats.auto_wall_grid_mid_substn = false
        stats.auto_coop_grid_mid_wall = false
        stats.auto_coop_grid_mid_mid = false
        stats.auto_coop_grid_mid_substn = false
        stats.auto_substn_grid_mid_wall = false
        stats.auto_substn_grid_mid_mid = false
        stats.auto_substn_grid_mid_substn = false
        stats.auto_wall_grid_top_wall = false
        stats.auto_wall_grid_top_mid = false
        stats.auto_wall_grid_top_substn = false
        stats.auto_coop_grid_top_wall = false
        stats.auto_coop_grid_top_mid = false
        stats.auto_coop_grid_top_substn = false
        stats.auto_substn_grid_top_wall = false
        stats.auto_substn_grid_top_mid = false
        stats.auto_substn_grid_top_substn = false
    }

    @JvmStatic
    fun clearTele(stats: MatchStatsStruct) {
        stats.wall_grid_hyb_wall = false
        stats.wall_grid_hyb_mid = false
        stats.wall_grid_hyb_substn = false
        stats.coop_grid_hyb_wall = false
        stats.coop_grid_hyb_mid = false
        stats.coop_grid_hyb_substn = false
        stats.substn_grid_hyb_wall = false
        stats.substn_grid_hyb_mid = false
        stats.substn_grid_hyb_substn = false
        stats.wall_grid_mid_wall = false
        stats.wall_grid_mid_mid = false
        stats.wall_grid_mid_substn = false
        stats.coop_grid_mid_wall = false
        stats.coop_grid_mid_mid = false
        stats.coop_grid_mid_substn = false
        stats.substn_grid_mid_wall = false
        stats.substn_grid_mid_mid = false
        stats.substn_grid_mid_substn = false
        stats.wall_grid_top_wall = false
        stats.wall_grid_top_mid = false
        stats.wall_grid_top_substn = false
        stats.coop_grid_top_wall = false
        stats.coop_grid_top_mid = false
        stats.coop_grid_top_substn = false
        stats.substn_grid_top_wall = false
        stats.substn_grid_top_mid = false
        stats.substn_grid_top_substn = false
        stats.dropped_gp_count = 0
    }
}
