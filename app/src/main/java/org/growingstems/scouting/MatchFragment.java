package org.growingstems.scouting;


import androidx.fragment.app.Fragment;

import org.frc836.database.MatchStatsStruct;

public abstract class MatchFragment extends Fragment {
    public abstract void saveData(MatchStatsStruct data);
    public abstract void loadData(MatchStatsStruct data);
}
