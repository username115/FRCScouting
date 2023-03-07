/*
 * Copyright 2023 Daniel Logan
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

import org.frc836.database.SuperScoutStats

object SuperScoutStatsYearly {

    @JvmStatic
    fun copyData(from: SuperScoutStats, to: SuperScoutStats) {
        to.caused_foul = from.caused_foul
        to.defense_rank = from.defense_rank
        to.driver_rank = from.driver_rank
        to.notes = from.notes
        to.offense_rank = from.offense_rank
    }

    @JvmStatic
    fun clearContents(stats: SuperScoutStats) {
        stats.offense_rank = 0
        stats.defense_rank = 0
        stats.driver_rank = 0
        stats.caused_foul = false
        stats.notes = ""
    }
}
