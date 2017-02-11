package org.frc836.yearly;


import android.app.Fragment;

import org.frc836.database.MatchStatsStruct;
import org.frc836.database.PilotStatsStruct;

public abstract class PilotFragment extends Fragment {
    public abstract void saveData(PilotStatsStruct[] data);
    public abstract void loadData(PilotStatsStruct[] data);
}