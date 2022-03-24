package org.growingstems.scouting

import androidx.fragment.app.Fragment
import org.frc836.database.MatchStatsStruct

abstract class MatchFragment : Fragment() {
    abstract fun saveData(data: MatchStatsStruct)
    abstract fun loadData(data: MatchStatsStruct)
}
