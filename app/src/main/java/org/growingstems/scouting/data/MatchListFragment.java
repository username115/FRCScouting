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

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.frc836.yearly.MatchActivity;
import org.growingstems.scouting.MatchSchedule;
import org.growingstems.scouting.Prefs;
import org.growingstems.scouting.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class MatchListFragment extends DataFragment {

    private static final String PRACTICE = "Practice Matches";
    private static final String QUALIFICATION = "Qualification Matches";

    private int teamNum = -1;
    private String eventName = null;

    private boolean futureMatches = false;

    private List<String> matchList;

    MatchSchedule schedule = new MatchSchedule();

    public static MatchListFragment getInstance(String event_name) {
        return getInstance(event_name, -1, false);
    }

    public static MatchListFragment getInstance(int team_num) {
        return getInstance(null, team_num, false);
    }

    public static MatchListFragment getInstance(String event_name, int team_num) {
        return getInstance(event_name, team_num, false);
    }

    public static MatchListFragment getInstance(String event_name, int team_num, boolean future_matches) {
        MatchListFragment fragment = new MatchListFragment();
        fragment.setEvent(event_name);
        fragment.setTeamNum(team_num);
        fragment.setFutureMatches(future_matches);
        return fragment;
    }

    public MatchListFragment() {
    }

    public void setEvent(String event_name) {
        eventName = event_name;
    }

    public void setTeamNum(int team_num) {
        teamNum = team_num;
    }

    public void setFutureMatches(boolean future_matches) {
        futureMatches = future_matches;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (futureMatches && eventName == null)
            eventName = Prefs.getEvent(mParent, null);
        rootView.findViewById(R.id.data_team_input_layout).setVisibility(
            View.GONE);

        return rootView;
    }

    @Override
    protected void refreshData() {
        if (!isDisplayed())
            return;

        List<String> matches;
        if (eventName != null && !futureMatches)
            matches = getMatchesForEvent(eventName, teamNum);
        else if (eventName != null)
            matches = getFutureMatchesForEvent(eventName, teamNum);
        else {
            matches = getMatchesForTeam(teamNum);
        }

        if (matches == null || matches.isEmpty()) {
            StringBuilder message = new StringBuilder(
                "No Matches for selected ");
            if (teamNum > 0 && eventName != null) {
                message.append("Team/Event combination");
            } else if (teamNum > 0) {
                message.append("Team");
            } else if (eventName != null) {
                message.append("Event");
            } else {
                message = new StringBuilder("Invalid Event or Team Selected");
            }
            if (matches == null)
                matches = new ArrayList<>();
            matches.add(message.toString());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
            getActivity(), defaultListResource, matches);
        matchList = matches;
        dataList.setAdapter(adapter);
        dataList.setOnItemClickListener(new MatchClick());
    }

    private List<String> getMatchesForTeam(int teamNum) {
        List<String> events = mParent.getDB().getEventsForTeam(teamNum);
        String curEvent = Prefs.getEvent(getActivity(), "");
        List<String> matches = new ArrayList<>(events != null ? (events.size() * 12) : 10);
        if (events != null) {
            if (curEvent.length() > 0 && events.contains(curEvent)) {
                events.remove(curEvent);
                events.add(0, curEvent);
            }
            for (String event : events) {
                List<String> t = getMatchesForEvent(event, teamNum);
                if (t != null) {
                    t.add(0, event);
                    matches.addAll(t);
                }
            }
        }
        return matches;
    }

    private List<String> getMatchesForEvent(String eventName, int teamNum) {
        boolean prac = Prefs.getPracticeMatch(mParent, false);

        List<String> matches = getMatchesForEvent(eventName, prac, teamNum);
        List<String> matches2 = getMatchesForEvent(eventName, !prac, teamNum);
        if (matches.size() > 1 && matches2.size() > 1) {
            matches.addAll(matches2);
        } else if (matches.size() <= 1 && matches2.size() > 1) {
            matches = matches2;
        }
        return matches;
    }

    private List<String> getMatchesForEvent(String eventName, boolean prac,
                                            int teamNum) {
        List<String> matches = null;
        if (eventName != null)
            matches = mParent.getDB().getMatchesWithData(eventName, prac,
                teamNum);

        if (matches == null)
            matches = new ArrayList<>(1);
        if (!matches.isEmpty()) {
            matches.add(0, prac ? PRACTICE : QUALIFICATION);
        }

        return matches;
    }

    private List<String> getFutureMatchesForEvent(String eventName, int teamNum) {
        if (eventName == null || !eventName.equals(Prefs.getEvent(mParent, "")))
            return null;

        List<String> matches = getMatchesForEvent(eventName, false, teamNum);
        int lastmatch = 0;
        if (matches != null && !matches.isEmpty()) {
            //match list is guaranteed sorted from previous call
            lastmatch = Integer.parseInt(matches.get(matches.size() - 1));
        }

        if (!schedule.isValid(mParent)) {
            schedule.updateSchedule(eventName, mParent,
                false);
        }

        List<Integer> matchNums = schedule.getMatchesForTeam(teamNum, mParent);
        if (matchNums == null || matchNums.isEmpty())
            return null;

        Collections.sort(matchNums);

        matches.clear();
        for (Integer mat : matchNums) {
            if (mat > lastmatch) {
                matches.add(String.valueOf(mat));
            }
        }
        return matches;
    }

    private class MatchClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (view instanceof TextView) {
                String match = ((TextView) view).getText().toString();
                if (TextUtils.isDigitsOnly(match)) {
                    final String event = eventName == null ? getEvent(position) : eventName;
                    final boolean prac = getPractice(position);
                    final int mat = Integer.parseInt(match);
                    if (!futureMatches) {
                        if (teamNum > 0)
                            loadMatch(mat, event, prac, teamNum, mParent.getDB().getPosition(event, mat, prac, teamNum));
                        else {
                            PopupMenu popup = new PopupMenu(getActivity(), view);
                            List<String> teams = mParent.getDB().getTeamsForMatch(event, mat, prac);
                            for (String team : teams)
                                try {
                                    popup.getMenu().add(mParent.getDB().getPosition(event, mat, prac, Integer.parseInt(team)) + ":" + team);
                                } catch (NumberFormatException e) {
                                    //TODO handle this
                                }
                            popup.setOnMenuItemClickListener(item -> {
                                int team = Integer.parseInt(item.getTitle().toString().split(":")[1]);
                                loadMatch(mat, event, prac, team, mParent.getDB().getPosition(event, mat, prac, team));
                                return true;
                            });
                            popup.show();
                        }
                    } else {
                        //open future match info activity
                        loadMatchInfo(mat, event);
                    }
                }
            }
        }

    }

    private String getEvent(int position) {
        ListIterator<String> iter = matchList.listIterator(position);

        String ret = null;
        String cur;
        while (iter.hasPrevious() && ret == null) {
            cur = iter.previous();
            if (!TextUtils.isDigitsOnly(cur) && !cur.equalsIgnoreCase(PRACTICE) && !cur.equalsIgnoreCase(QUALIFICATION))
                ret = cur;
        }
        return ret;
    }

    private boolean getPractice(int position) {
        ListIterator<String> iter = matchList.listIterator(position);

        String cur;
        while (iter.hasPrevious()) {
            cur = iter.previous();
            if (cur.equalsIgnoreCase(PRACTICE))
                return true;
            if (cur.equalsIgnoreCase(QUALIFICATION))
                return false;
        }
        return false;
    }

    private void loadMatch(int match, String event, boolean prac, int team, String position) {
        if (team > 0) {
            Intent intent = new Intent(getActivity(),
                MatchActivity.class);
            intent.putExtra("team", String.valueOf(team));
            intent.putExtra("match", String.valueOf(match));
            intent.putExtra("readOnly", true);
            intent.putExtra("practice", prac);
            intent.putExtra("position", position);
            if (event != null) {
                intent.putExtra("event", event);
            }
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "Select a team first", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMatchInfo(int match, String event) {
        Intent intent = new Intent(mParent, DataActivity.class);
        intent.putExtra(DataActivity.ACTIVITY_TYPE_STRING,
            DataActivity.ACTIVITY_TYPE_FUTUREMATCH);
        intent.putExtra(DataActivity.EVENT_ARG, event);
        intent.putExtra(DataActivity.MATCH_ARG, match);
        mParent.startActivity(intent);
    }

}
