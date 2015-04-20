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

package org.growingstems.scouting;

import java.util.Locale;

import org.frc836.database.DBActivity;
import org.robobees.recyclerush.PitsDataFragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

public abstract class DataFragment extends Fragment {
	public static final int PT_EVENTS = 0;
	public static final int PT_TEAMS = 1;
	public static final int PT_MATCHES = 2;
	public static final int PT_PITS = 3;

	protected static final int defaultListResource = android.R.layout.simple_list_item_1;

	protected ListView dataList;
	protected AutoCompleteTextView autoText;
	protected Button loadB;
	protected boolean displayed = false;

	protected View rootView;

	protected int mSectionType;

	protected DBActivity mParent;

	public static DataFragment newInstance(int section_title, DBActivity parent) {
		return newInstance(section_title, parent, -1, null);
	}

	public static DataFragment newInstance(int section_title,
			DBActivity parent, int teamNumber) {
		return newInstance(section_title, parent, teamNumber, null);
	}

	public static DataFragment newInstance(int section_title,
			DBActivity parent, String event_name) {
		return newInstance(section_title, parent, -1, event_name);
	}

	public static DataFragment newInstance(int section_title,
			DBActivity parent, int teamNumber, String event_name) {
		DataFragment fragment;
		switch (section_title) {
		case PT_EVENTS:
			fragment = new EventListFragment();
			break;
		case PT_TEAMS:
			fragment = new TeamListFragment(event_name);
			break;
		case PT_MATCHES:
			fragment = new MatchListFragment(event_name, teamNumber);
			break;
		case PT_PITS:
			fragment = new PitsDataFragment(teamNumber);
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
		}
		return null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		rootView = inflater.inflate(R.layout.fragment_data, container, false);
		dataList = (ListView) rootView.findViewById(R.id.dataList);

		autoText = (AutoCompleteTextView) rootView
				.findViewById(R.id.data_team_id);
		loadB = (Button) rootView.findViewById(R.id.data_teamB);

		displayed = true;
		refreshData();
		return rootView;
	}

	protected abstract void refreshData();

}
