/*
 * Copyright 2015 Daniel Logan
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

package org.growingstems.scouting.data;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.SparseArray;
import android.view.Menu;
import android.webkit.URLUtil;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import org.frc836.database.DB.SyncCallback;
import org.frc836.database.DBActivity;
import org.frc836.database.DBSyncService.LocalBinder;
import org.growingstems.scouting.MainMenuSelection;
import org.growingstems.scouting.MenuSelections.Refreshable;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;

public class DataActivity extends DBActivity implements ActionBar.TabListener,
    Refreshable {

    private ProgressDialog pd;

    public static final String ACTIVITY_TYPE_STRING = "ACTIVITY_TYPE";
    public static final String EVENT_ARG = "EVENT_NAME";
    public static final String TEAM_ARG = "TEAM_NUM";
    public static final String MATCH_ARG = "MATCH_NUM";
    public static final int ACTIVITY_TYPE_DEFAULT = 0;
    public static final int ACTIVITY_TYPE_EVENT = 1;
    public static final int ACTIVITY_TYPE_TEAM = 2;
    public static final int ACTIVITY_TYPE_FUTUREMATCH = 3;
    private final ActivityResultLauncher<Intent> resultForPrefs = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
    });

    @NonNull
    @Override
    public ActivityResultLauncher<Intent> getResultForPrefs() {
        return resultForPrefs;
    }

    protected enum DataType {
        dt_Default, dt_Event, dt_Team, dt_FutureMatch
    }

    protected DataType dataType = DataType.dt_Default;
    protected String eventName = null;
    protected int teamNum = -1;
    protected int matchNum = -1;

    protected boolean isDisplayed = false;

    protected static final int[] DEFAULT_TABS = {DataFragment.PT_EVENTS,
        DataFragment.PT_TEAMS};
    protected static final int[] EVENT_TABS = {DataFragment.PT_TEAMS,
        DataFragment.PT_MATCHES};
    protected static final int[] TEAM_TABS = {DataFragment.PT_MATCHES,
        DataFragment.PT_MATCHLINEGRAPH,
        DataFragment.PT_PITS}; // TODO
    protected static final int[] CURRENT_TEAM_TABS = {DataFragment.PT_MATCHES,
        DataFragment.PT_FUTUREMATCHES,
        DataFragment.PT_MATCHLINEGRAPH,
        DataFragment.PT_PITS}; // TODO
    protected static final int[] FUTURE_MATCH_TABS = {DataFragment.PT_MATCHINFOCURRENTEVENT
    };

    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDisplayed = true;
        m_callback = new ServiceWatcher();
        setContentView(R.layout.activity_data);
        Intent intent = getIntent();
        int temp = intent.getIntExtra(ACTIVITY_TYPE_STRING,
            ACTIVITY_TYPE_DEFAULT);
        switch (temp) {
            case ACTIVITY_TYPE_EVENT:
                dataType = DataType.dt_Event;
                eventName = intent.getStringExtra(EVENT_ARG);
                if (eventName != null)
                    setTitle(eventName);
                break;
            case ACTIVITY_TYPE_TEAM:
                dataType = DataType.dt_Team;
                teamNum = intent.getIntExtra(TEAM_ARG, -1);
                eventName = intent.getStringExtra(EVENT_ARG);
                if (teamNum > 0 && eventName != null) {
                    setTitle(teamNum + " at " + eventName);
                } else if (teamNum > 0) {
                    setTitle("Team " + teamNum);
                }
                break;
            case ACTIVITY_TYPE_FUTUREMATCH:
                dataType = DataType.dt_FutureMatch;
                matchNum = intent.getIntExtra(MATCH_ARG, -1);
                eventName = intent.getStringExtra(EVENT_ARG);
                setTitle("Match " + matchNum);
                break;
            case ACTIVITY_TYPE_DEFAULT:
            default:
                dataType = DataType.dt_Default;
                break;
        }

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager
            .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    actionBar.setSelectedNavigationItem(position);
                }
            });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab()
                .setText(mSectionsPagerAdapter.getPageTitle(i))
                .setTabListener(this));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isDisplayed = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isDisplayed = true;
        reloadData();
    }

    public boolean isDisplayed() {
        return isDisplayed;
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        super.onCreateOptionsMenu(menu);
        MainMenuSelection.setRefreshItem(menu, R.string.refresh_data);
        return true;
    }

    @NonNull
    @Override
    public String getHelpMessage() {
        return "";
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,
                              FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SparseArray<Fragment> tabs;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = new SparseArray<>(getCount());
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            if (tabs.get(position) == null) {
                int tab;
                switch (dataType) {
                    case dt_Event:
                        tab = EVENT_TABS[position];
                        tabs.put(position, DataFragment.newInstance(tab,
                            DataActivity.this, eventName));
                        break;
                    case dt_Team:
                        if (eventName == null || Prefs.getEvent(DataActivity.this, "").equals(eventName))
                            tab = CURRENT_TEAM_TABS[position];
                        else
                            tab = TEAM_TABS[position];
                        tabs.put(position, DataFragment.newInstance(tab,
                            DataActivity.this, teamNum, eventName));
                        break;
                    case dt_FutureMatch:
                        tab = FUTURE_MATCH_TABS[position];
                        tabs.put(position, DataFragment.newInstance(tab, DataActivity.this, eventName, matchNum));
                        break;
                    case dt_Default:
                    default:
                        tab = DEFAULT_TABS[position];
                        tabs.put(position,
                            DataFragment.newInstance(tab, DataActivity.this));
                        break;
                }

            }

            return tabs.get(position);
        }

        @Override
        public int getCount() {
            switch (dataType) {
                case dt_Event:
                    return EVENT_TABS.length;
                case dt_Team:
                    if (eventName == null || Prefs.getEvent(DataActivity.this, "").equals(eventName))
                        return CURRENT_TEAM_TABS.length;
                    else
                        return TEAM_TABS.length;
                case dt_FutureMatch:
                    return FUTURE_MATCH_TABS.length;
                case dt_Default:
                default:
                    return DEFAULT_TABS.length;
            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            int tab;
            switch (dataType) {
                case dt_Event:
                    tab = EVENT_TABS[position];
                    break;
                case dt_Team:
                    if (eventName == null || Prefs.getEvent(DataActivity.this, "").equals(eventName))
                        tab = CURRENT_TEAM_TABS[position];
                    else
                        tab = TEAM_TABS[position];
                    break;
                case dt_FutureMatch:
                    tab = FUTURE_MATCH_TABS[position];
                    break;
                case dt_Default:
                default:
                    tab = DEFAULT_TABS[position];
                    break;
            }
            return DataFragment.getPageTitle(tab, DataActivity.this);
        }
    }

    protected class ServiceWatcher implements ServiceConnection {

        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service instanceof LocalBinder) {
                db.startSync(new RefreshCallback(DataActivity.this));
            }
        }

        public void onServiceDisconnected(ComponentName name) {
        }

    }

    @Override
    public void refresh() {
        pd = ProgressDialog.show(this, "Busy", "Refreshing Data", false);
        pd.setCancelable(true);
        String url = Prefs.getScoutingURLNoDefault(getApplicationContext());

        if (url.length() > 1 && URLUtil.isValidUrl(url)
            && Prefs.getAutoSync(getApplicationContext(), false))
            db.startSync(new RefreshCallback(this));
        else
            reloadData();
    }

    private static class RefreshCallback implements SyncCallback {

        private final DataActivity parent;

        public RefreshCallback(DataActivity parent) {
            this.parent = parent;
        }

        @Override
        public void onFinish() {
            parent.reloadData();
        }
    }

    private void reloadData() {
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            Fragment f = mSectionsPagerAdapter.getItem(i);
            if (f instanceof DataFragment) {
                ((DataFragment) f).refreshData();
            }
        }
        if (pd != null)
            pd.dismiss();
    }

}
