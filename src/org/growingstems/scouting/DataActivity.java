package org.growingstems.scouting;


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
import android.util.SparseArray;
import android.view.Menu;
import android.webkit.URLUtil;

public class DataActivity extends DBActivity implements ActionBar.TabListener,
		Refreshable {

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
		super.onCreateOptionsMenu(menu);
		MainMenuSelection.setRefreshItem(menu, R.string.refresh_data);
		return true;
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
				tabs.put(position,
						DataFragment.newInstance(position, DataActivity.this));

			return tabs.get(position);
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return DataFragment.getPageTitle(position, DataActivity.this);
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
