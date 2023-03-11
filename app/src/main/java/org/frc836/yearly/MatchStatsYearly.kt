/*
 * Copyright 2022 Daniel Logan
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
        return (getTeleHybScore(stats)
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
    fun copyAuto(from: MatchStatsStruct, to: MatchStatsStruct) {
        to.auto_charge_station          = from.auto_charge_station
        to.auto_mobility                = from.auto_mobility
        to.auto_wall_grid_hyb_wall      = from.auto_wall_grid_hyb_wall
        to.auto_wall_grid_hyb_mid       = from.auto_wall_grid_hyb_mid
        to.auto_wall_grid_hyb_substn    = from.auto_wall_grid_hyb_substn
        to.auto_coop_grid_hyb_wall      = from.auto_coop_grid_hyb_wall
        to.auto_coop_grid_hyb_mid       = from.auto_coop_grid_hyb_mid
        to.auto_coop_grid_hyb_substn    = from.auto_coop_grid_hyb_substn
        to.auto_substn_grid_hyb_wall    = from.auto_substn_grid_hyb_wall
        to.auto_substn_grid_hyb_mid     = from.auto_substn_grid_hyb_mid
        to.auto_substn_grid_hyb_substn  = from.auto_substn_grid_hyb_substn
        to.auto_wall_grid_mid_wall      = from.auto_wall_grid_mid_wall
        to.auto_wall_grid_mid_mid       = from.auto_wall_grid_mid_mid
        to.auto_wall_grid_mid_substn    = from.auto_wall_grid_mid_substn
        to.auto_coop_grid_mid_wall      = from.auto_coop_grid_mid_wall
        to.auto_coop_grid_mid_mid       = from.auto_coop_grid_mid_mid
        to.auto_coop_grid_mid_substn    = from.auto_coop_grid_mid_substn
        to.auto_substn_grid_mid_wall    = from.auto_substn_grid_mid_wall
        to.auto_substn_grid_mid_mid     = from.auto_substn_grid_mid_mid
        to.auto_substn_grid_mid_substn  = from.auto_substn_grid_mid_substn
        to.auto_wall_grid_top_wall      = from.auto_wall_grid_top_wall
        to.auto_wall_grid_top_mid       = from.auto_wall_grid_top_mid
        to.auto_wall_grid_top_substn    = from.auto_wall_grid_top_substn
        to.auto_coop_grid_top_wall      = from.auto_coop_grid_top_wall
        to.auto_coop_grid_top_mid       = from.auto_coop_grid_top_mid
        to.auto_coop_grid_top_substn    = from.auto_coop_grid_top_substn
        to.auto_substn_grid_top_wall    = from.auto_substn_grid_top_wall
        to.auto_substn_grid_top_mid     = from.auto_substn_grid_top_mid
        to.auto_substn_grid_top_substn  = from.auto_substn_grid_top_substn
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
        stats.foul_count = 0
    }

    @JvmStatic
    fun copyTele(from: MatchStatsStruct, to: MatchStatsStruct) {
        to.wall_grid_hyb_wall       = from.wall_grid_hyb_wall
        to.wall_grid_hyb_mid        = from.wall_grid_hyb_mid
        to.wall_grid_hyb_substn     = from.wall_grid_hyb_substn
        to.coop_grid_hyb_wall       = from.coop_grid_hyb_wall
        to.coop_grid_hyb_mid        = from.coop_grid_hyb_mid
        to.coop_grid_hyb_substn     = from.coop_grid_hyb_substn
        to.substn_grid_hyb_wall     = from.substn_grid_hyb_wall
        to.substn_grid_hyb_mid      = from.substn_grid_hyb_mid
        to.substn_grid_hyb_substn   = from.substn_grid_hyb_substn
        to.wall_grid_mid_wall       = from.wall_grid_mid_wall
        to.wall_grid_mid_mid        = from.wall_grid_mid_mid
        to.wall_grid_mid_substn     = from.wall_grid_mid_substn
        to.coop_grid_mid_wall       = from.coop_grid_mid_wall
        to.coop_grid_mid_mid        = from.coop_grid_mid_mid
        to.coop_grid_mid_substn     = from.coop_grid_mid_substn
        to.substn_grid_mid_wall     = from.substn_grid_mid_wall
        to.substn_grid_mid_mid      = from.substn_grid_mid_mid
        to.substn_grid_mid_substn   = from.substn_grid_mid_substn
        to.wall_grid_top_wall       = from.wall_grid_top_wall
        to.wall_grid_top_mid        = from.wall_grid_top_mid
        to.wall_grid_top_substn     = from.wall_grid_top_substn
        to.coop_grid_top_wall       = from.coop_grid_top_wall
        to.coop_grid_top_mid        = from.coop_grid_top_mid
        to.coop_grid_top_substn     = from.coop_grid_top_substn
        to.substn_grid_top_wall     = from.substn_grid_top_wall
        to.substn_grid_top_mid      = from.substn_grid_top_mid
        to.substn_grid_top_substn   = from.substn_grid_top_substn
        to.dropped_gp_count         = from.dropped_gp_count
        to.foul_count               = from.foul_count
    }
}
