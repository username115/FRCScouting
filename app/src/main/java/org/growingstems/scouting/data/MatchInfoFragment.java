package org.growingstems.scouting.data;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.frc836.yearly.MatchStatsYearly;
import org.growingstems.scouting.MatchSchedule;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MatchInfoFragment extends DataFragment implements DataSource.DataCallback {

    private String eventName = null;
    private int matchNum = -1;

    private final MatchSchedule schedule = new MatchSchedule();

    private DataSource dataSource;

    private int numTeams = 0;
    private int teamsProcessed = 0;
    private boolean allEvents = false;
    private final List<DataSource.Data> data = new ArrayList<>(6);

    public static MatchInfoFragment getInstance(String event_name, int match_num) {
        return getInstance(event_name, match_num, false);
    }

    public static MatchInfoFragment getInstance(String event_name, int match_num, boolean all_events) {
        MatchInfoFragment fragment = new MatchInfoFragment();
        fragment.setEvent(event_name);
        fragment.setMatch(match_num);
        fragment.setAllEvents(all_events);
        return fragment;
    }

    public MatchInfoFragment() {
    }

    public void setEvent(String event_name) {
        eventName = event_name;
    }

    public void setMatch(int match_num) {
        matchNum = match_num;
    }

    public void setAllEvents(boolean all_events) {
        allEvents = all_events;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (eventName == null)
            eventName = Prefs.getEvent(mParent, null);
        rootView.findViewById(R.id.data_team_input_layout).setVisibility(
            View.GONE);
        dataList.setVisibility(View.GONE);
        dataTable.setVisibility(View.VISIBLE);
        dataTable.setStretchAllColumns(true);
        if (dataSource == null)
            dataSource = new DataSource(mParent.getDB());
        return rootView;
    }


    @Override
    protected void refreshData() {
        if (!isDisplayed() || getActivity() == null)
            return;

        if (dataSource == null)
            dataSource = new DataSource(mParent.getDB());

        synchronized (this) {
            if (numTeams == 0 || numTeams == teamsProcessed) {
                data.clear();
                for (String pos : getResources().getStringArray(R.array.positions)) {
                    String team = schedule.getTeam(matchNum, pos, mParent);
                    if (team != null && TextUtils.isDigitsOnly(team)) {
                        int teamNum = Integer.parseInt(team);
                        if (teamNum > 0) {
                            numTeams++;
                            dataSource.getAVGData(teamNum, eventName, this);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void onFinished(DataSource.Data dataElement) {
        synchronized (this) {
            data.add(dataElement);
            teamsProcessed++;
            if (numTeams <= teamsProcessed)
                populateTable();
        }
    }

    private void populateTable() {

        dataTable.removeAllViews();
        TextView tv = new TextView(mParent);

        int id = 0;

        tv.setText(R.string.team_num_short);
        tv.setGravity(Gravity.START);
        TableRow titles = new TableRow(mParent);
        titles.setId(id);
        TableLayout.LayoutParams rowParams =
            new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        rowParams.setMargins(1, 1, 1, 1);
        titles.setPadding(0, 0, 0, 0);
        titles.setLayoutParams(rowParams);

        titles.addView(tv);
        for (String name : MatchStatsYearly.getGraphShortNames()) {
            final TextView col = new TextView(mParent);
            col.setGravity(Gravity.START);
            col.setText(name);
            titles.addView(col);
        }

        dataTable.addView(titles, rowParams);

        for (DataSource.Data dat : data) {
            Map<String, Double> avgs = dat.getEventAVGs(eventName);
            if (avgs != null && !avgs.isEmpty()) {
                id++;
                TableRow row = new TableRow(mParent);
                row.setId(id);
                row.setPadding(0, 0, 0, 0);
                row.setLayoutParams(rowParams);


                TextView tm = new TextView(mParent);
                tm.setText(String.valueOf(dat.getTeamNum()));
                tm.setGravity(Gravity.START);
                row.addView(tm);
                for (String name : MatchStatsYearly.getGraphNames()) {
                    Double num = avgs.get(name);
                    TextView datum = new TextView(mParent);
                    if (num != null) {
                        String s = String.valueOf(num);
                        datum.setText(s.substring(0, Math.min(5, s.length() - 1)));
                    } else {
                        datum.setText("");
                    }
                    tm.setGravity(Gravity.START);
                    row.addView(datum);
                }
                row.setOnClickListener(new OnTeamClickListener(dat.getTeamNum()));
                dataTable.addView(row, rowParams);
            }

        }
    }

    private class OnTeamClickListener implements View.OnClickListener {

        private final int teamId;

        public OnTeamClickListener(int team) {
            teamId = team;
        }

        @Override
        public void onClick(View v) {
            loadTeam(teamId);
        }
    }


    private void loadTeam(int team) {
        Intent intent = new Intent(mParent, DataActivity.class);
        intent.putExtra(DataActivity.ACTIVITY_TYPE_STRING,
            DataActivity.ACTIVITY_TYPE_TEAM);
        intent.putExtra(DataActivity.EVENT_ARG, eventName);
        intent.putExtra(DataActivity.TEAM_ARG, team);
        mParent.startActivity(intent);
    }
}
