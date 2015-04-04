package org.growingstems.scouting;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.frc836.database.DB.SyncCallback;
import org.frc836.database.DBActivity;
import org.frc836.database.DBSyncService.LocalBinder;
import org.growingstems.scouting.MenuSelections.Refreshable;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DataActivity extends DBActivity implements ActionBar.TabListener,
		Refreshable {

	private static final int defaultListResource = android.R.layout.simple_list_item_1;

	private static final int PT_EVENTS = 0;
	private static final int PT_TEAMS = 1;

	private ProgressDialog pd;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_callback = new ServiceWatcher();
		setContentView(R.layout.activity_data);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		MainMenuSelection.setRefreshItem(menu, R.string.refresh_data);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MainMenuSelection.onOptionsItemSelected(item, this) ? true
				: super.onOptionsItemSelected(item);
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
			tabs = new SparseArray<Fragment>(getCount());
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			if (tabs.get(position) == null)
				tabs.put(position, DataFragment.newInstance(position, DataActivity.this));

			return tabs.get(position);
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case PT_EVENTS:
				return getString(R.string.title_event_section).toUpperCase(l);
			case PT_TEAMS:
				return getString(R.string.title_team_section).toUpperCase(l);
			}
			return null;
		}
	}

	public static class DataFragment extends Fragment {

		private static final String ARG_SECTION_TITLE = "section_title";

		private ListView dataList;
		private AutoCompleteTextView teamT;
		private Button loadB;
		private boolean displayed = false;

		private View rootView;

		private int mSectionType;
		
		private DataActivity mParent;

		public static DataFragment newInstance(int section_title, DataActivity parent) {
			DataFragment fragment = new DataFragment();
			fragment.mParent = parent;
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_TITLE, section_title);
			fragment.setArguments(args);
			return fragment;
		}

		public DataFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			super.onCreateView(inflater, container, savedInstanceState);
			rootView = inflater.inflate(R.layout.fragment_data, container,
					false);
			mSectionType = getArguments().getInt(ARG_SECTION_TITLE, PT_EVENTS);
			dataList = (ListView) rootView.findViewById(R.id.dataList);

			if (mSectionType == PT_TEAMS) { // team tab
				rootView.findViewById(R.id.data_team_input_layout)
						.setVisibility(View.VISIBLE);

				teamT = (AutoCompleteTextView) rootView
						.findViewById(R.id.data_team_id);
				loadB = (Button) rootView.findViewById(R.id.data_teamB);
				loadB.setOnClickListener(new LoadClick());
				teamT.setOnItemClickListener(new TeamClick());
				teamT.setThreshold(1);

			} else { // Event tab
				rootView.findViewById(R.id.data_team_input_layout)
						.setVisibility(View.GONE);
			}
			displayed = true;
			refreshData();

			return rootView;
		}

		private void refreshData() {
			if (!displayed)
				return;
			if (mSectionType == PT_TEAMS) { // team tab
				List<String> teams = mParent.db.getTeamsWithData();
				String ourTeam = Prefs.getDefaultTeamNumber(getActivity(), "")
						.trim();
				if (teams == null) {
					teams = new ArrayList<String>(1);
				}
				if (teams.isEmpty()) {
					teams.add("No Data for any Team");
				} else {
					if (ourTeam.length() > 0 && TextUtils.isDigitsOnly(ourTeam)
							&& teams.contains(ourTeam)) {
						teams.remove(ourTeam);
						teams.add(0, ourTeam);
					}
					setTeamList(teams);
				}
				final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getActivity(), defaultListResource, teams);
				dataList.setAdapter(adapter);
				dataList.setOnItemClickListener(new TeamClick());
			} else { // Event tab
				List<String> events = mParent.db.getEventsWithData();
				String curEvent = Prefs.getEvent(getActivity(), "");
				if (events == null) {
					events = new ArrayList<String>(1);
				}
				if (events.isEmpty()) {
					events.add("No Data for any Event");
				} else if (curEvent.length() > 0 && events.contains(curEvent)) {
					events.remove(curEvent);
					events.add(0, curEvent);
				}
				final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getActivity(), defaultListResource, events);
				dataList.setAdapter(adapter);
				dataList.setOnItemClickListener(new EventClick());
			}
		}

		private void setTeamList(List<String> teams) {
			if (teams.isEmpty())
				return;
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_dropdown_item_1line,
					teams);

			teamT.setAdapter(adapter);
		}

		private class EventClick implements AdapterView.OnItemClickListener {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String event = (String) parent.getItemAtPosition(position);

				loadEvent(event);
			}

		}

		private class TeamClick implements AdapterView.OnItemClickListener {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (view instanceof TextView) {
					String team = ((TextView) view).getText().toString();
					loadTeam(Integer.valueOf(team));
				}
			}

		}

		private class LoadClick implements View.OnClickListener {

			@Override
			public void onClick(View v) {
				if (teamT.getText().toString().length() > 0)
					loadTeam(Integer.valueOf(teamT.getText().toString()));
			}

		}

		private void loadTeam(int team) {
			// TODO load the team data activity
			Toast.makeText(getActivity(), "Open team " + team,
					Toast.LENGTH_SHORT).show();
		}

		private void loadEvent(String event) {
			// TODO load the event data activity
			Toast.makeText(getActivity(), "Open event " + event,
					Toast.LENGTH_SHORT).show();
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

	private class RefreshCallback implements SyncCallback {

		private DataActivity parent;

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
