/*
 * Copyright 2016 Daniel Logan
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

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.growingstems.scouting.R;

import java.util.Locale;

public abstract class DataFragment extends Fragment {
    public static final int PT_EVENTS = 0;
    public static final int PT_TEAMS = 1;
    public static final int PT_MATCHES = 2;
    public static final int PT_PITS = 3;
    public static final int PT_MATCHLINEGRAPH = 4;
    public static final int PT_FUTUREMATCHES = 5;
    public static final int PT_MATCHINFOCURRENTEVENT = 6;
    public static final int PT_MATCHINFOALLEVENTS = 7;

    protected static final int defaultListResource = android.R.layout.simple_list_item_1;

    protected ListView dataList;
    protected TableLayout dataTable;
    protected AutoCompleteTextView autoText;
    protected Button loadB;
    protected boolean displayed = false;

    protected View rootView;

    protected int mSectionType;

    protected DataActivity mParent;

    protected int default_layout_resource = R.layout.fragment_data;

    public static DataFragment newInstance(int section_title, DataActivity parent) {
        return newInstance(section_title, parent, -1, null, -1);
    }

    public static DataFragment newInstance(int section_title,
                                           DataActivity parent, int teamNumber) {
        return newInstance(section_title, parent, teamNumber, null, -1);
    }

    public static DataFragment newInstance(int section_title,
                                           DataActivity parent, String event_name) {
        return newInstance(section_title, parent, -1, event_name, -1);
    }

    public static DataFragment newInstance(int section_title,
                                           DataActivity parent, String event_name, int matchNumber) {
        return newInstance(section_title, parent, -1, event_name, matchNumber);
    }


    public static DataFragment newInstance(int section_title,
                                           DataActivity parent, int teamNumber, String event_name) {
        return newInstance(section_title, parent, teamNumber, event_name, -1);
    }

    public static DataFragment newInstance(int section_title,
                                           DataActivity parent, int teamNumber, String event_name, int matchNumber) {
        DataFragment fragment;
        switch (section_title) {
            case PT_EVENTS:
                fragment = new EventListFragment();
                break;
            case PT_TEAMS:
                fragment = TeamListFragment.getInstance(event_name);
                break;
            case PT_MATCHES:
                fragment = MatchListFragment.getInstance(event_name, teamNumber);
                break;
            case PT_PITS:
                fragment = PitsDataFragment.getInstance(teamNumber);
                break;
            case PT_MATCHLINEGRAPH:
                fragment = MatchLineGraphFragment.getInstance(teamNumber, event_name);
                break;
            case PT_FUTUREMATCHES:
                fragment = MatchListFragment.getInstance(event_name, teamNumber, true);
                break;
            case PT_MATCHINFOCURRENTEVENT:
                fragment = MatchInfoFragment.getInstance(event_name, matchNumber);
                break;
            case PT_MATCHINFOALLEVENTS:
                fragment = MatchInfoFragment.getInstance(event_name, matchNumber, true);
                break;
            default:
                return null;
        }
        fragment.mParent = parent;
        fragment.mSectionType = section_title;
        return fragment;
    }

    public static String getPageTitle(int section_title, Context context) {
        Locale l = Locale.getDefault();
        switch (section_title) {
            case PT_EVENTS:
                return context.getString(R.string.title_event_section).toUpperCase(
                    l);
            case PT_TEAMS:
                return context.getString(R.string.title_team_section)
                    .toUpperCase(l);
            case PT_MATCHES:
                return context.getString(R.string.title_match_section).toUpperCase(
                    l);
            case PT_PITS:
                return context.getString(R.string.title_pits_section)
                    .toUpperCase(l);
            case PT_MATCHLINEGRAPH:
                return context.getString(R.string.title_match_graphs_section).toUpperCase(l);
            case PT_FUTUREMATCHES:
                return context.getString(R.string.title_future_match_section).toUpperCase(l);
            case PT_MATCHINFOCURRENTEVENT:
                return context.getString(R.string.title_match_current_event_section).toUpperCase(l);
            case PT_MATCHINFOALLEVENTS:
                return context.getString(R.string.title_match_all_events_section).toUpperCase(l);
        }
        return null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(default_layout_resource, container, false);
        if (default_layout_resource == R.layout.fragment_data) {
            dataList = (ListView) rootView.findViewById(R.id.dataList);
            dataTable = (TableLayout) rootView.findViewById(R.id.dataTable);
            autoText = (AutoCompleteTextView) rootView
                .findViewById(R.id.data_team_id);
            loadB = (Button) rootView.findViewById(R.id.data_teamB);
        }

        displayed = true;
        return rootView;
    }

    protected boolean isDisplayed() {
        return mParent != null && displayed && mParent.isDisplayed();
    }

    public void onResume() {
        super.onResume();
        if (isDisplayed())
            refreshData();
    }

    protected abstract void refreshData();

}
