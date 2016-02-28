package org.robobees.stronghold;


import android.app.Fragment;

public abstract class MatchFragment extends Fragment {
    public abstract void saveData(MatchStatsSH data);
    public abstract void loadData(MatchStatsSH data);
}