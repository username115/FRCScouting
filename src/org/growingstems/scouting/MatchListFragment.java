package org.growingstems.scouting;

public class MatchListFragment extends DataFragment {
	
	private int teamNum = -1;
	private String eventName = null;
	
	
	public MatchListFragment(int team_num) {
		teamNum = team_num;
		eventName = null;
		// TODO Auto-generated constructor stub
	}
	
	public MatchListFragment(String event_name) {
		teamNum = -1;
		eventName = event_name;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void refreshData() {
		// TODO Auto-generated method stub

	}

}
