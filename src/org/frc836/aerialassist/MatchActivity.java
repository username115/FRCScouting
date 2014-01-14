/*
 * Copyright 2014 Daniel Logan
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

package org.frc836.aerialassist;

import org.frc836.database.DB;
import org.growingstems.scouting.ParamList;
import org.growingstems.scouting.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MatchActivity extends Activity {

	private String HELPMESSAGE;
	
	private DB submitter;

	private MatchStatsAA data;

	private int orientation;

	private ParamList notesOptions;

	private EditText teamText1;
	private EditText teamText2;
	private EditText teamText3;
	
	private LinearLayout team1AutoHighL;
	private LinearLayout team1AutoLowL;
	private LinearLayout team1HighL;
	private LinearLayout team1LowL;
	private LinearLayout team2AutoHighL;
	private LinearLayout team2AutoLowL;
	private LinearLayout team2HighL;
	private LinearLayout team2LowL;
	private LinearLayout team3AutoHighL;
	private LinearLayout team3AutoLowL;
	private LinearLayout team3HighL;
	private LinearLayout team3LowL;
	
	private EditText matchT;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.match);

		orientation = getResources().getConfiguration().orientation;

		notesOptions = new ParamList(getApplicationContext(), "notes_options");

		teamText1 = (EditText) findViewById(R.id.team1T);
		teamText2 = (EditText) findViewById(R.id.team2T);
		teamText3 = (EditText) findViewById(R.id.team3T);
		
		team1AutoHighL = (LinearLayout) findViewById(R.id.team1AutoHighBox);
		team1AutoLowL = (LinearLayout) findViewById(R.id.team1AutoLowBox);
		team1HighL = (LinearLayout) findViewById(R.id.team1HighBox);
		team1LowL = (LinearLayout) findViewById(R.id.team1LowBox);
		
		team2AutoHighL = (LinearLayout) findViewById(R.id.team2AutoHighBox);
		team2AutoLowL = (LinearLayout) findViewById(R.id.team2AutoLowBox);
		team2HighL = (LinearLayout) findViewById(R.id.team2HighBox);
		team2LowL = (LinearLayout) findViewById(R.id.team2LowBox);
		
		team3AutoHighL = (LinearLayout) findViewById(R.id.team3AutoHighBox);
		team3AutoLowL = (LinearLayout) findViewById(R.id.team3AutoLowBox);
		team3HighL = (LinearLayout) findViewById(R.id.team3HighBox);
		team3LowL = (LinearLayout) findViewById(R.id.team3LowBox);
		
		matchT = (EditText) findViewById(R.id.matchT);
		
		
		Intent intent = getIntent();
		
		String team1 = intent.getStringExtra("team1");
		String team2 = intent.getStringExtra("team2");
		String team3 = intent.getStringExtra("team3");
		
		String match = intent.getStringExtra("match");
		
		submitter = new DB(this);
		
		teamText1.setText(team1);
		teamText2.setText(team2);
		teamText3.setText(team3);
		matchT.setText(match);
		

	}

	public void onResume() {
		super.onResume();

		updatePosition();
	}

	private void updatePosition() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		String pos = prefs.getString("positionPref", "Red");
		if (pos.contains("Blue")) {
			teamText1.setBackgroundResource(R.drawable.blueborder);
			teamText2.setBackgroundResource(R.drawable.blueborder);
			teamText3.setBackgroundResource(R.drawable.blueborder);
			
			team1AutoHighL.setBackgroundResource(R.drawable.blueborder);
			team1AutoLowL.setBackgroundResource(R.drawable.blueborder);
			team1HighL.setBackgroundResource(R.drawable.blueborder);
			team1LowL.setBackgroundResource(R.drawable.blueborder);
			
			team2AutoHighL.setBackgroundResource(R.drawable.blueborder);
			team2AutoLowL.setBackgroundResource(R.drawable.blueborder);
			team2HighL.setBackgroundResource(R.drawable.blueborder);
			team2LowL.setBackgroundResource(R.drawable.blueborder);
			
			team3AutoHighL.setBackgroundResource(R.drawable.blueborder);
			team3AutoLowL.setBackgroundResource(R.drawable.blueborder);
			team3HighL.setBackgroundResource(R.drawable.blueborder);
			team3LowL.setBackgroundResource(R.drawable.blueborder);
		} else {
			teamText1.setBackgroundResource(R.drawable.redborder);
			teamText2.setBackgroundResource(R.drawable.redborder);
			teamText3.setBackgroundResource(R.drawable.redborder);
			
			team1AutoHighL.setBackgroundResource(R.drawable.redborder);
			team1AutoLowL.setBackgroundResource(R.drawable.redborder);
			team1HighL.setBackgroundResource(R.drawable.redborder);
			team1LowL.setBackgroundResource(R.drawable.redborder);
			
			team2AutoHighL.setBackgroundResource(R.drawable.redborder);
			team2AutoLowL.setBackgroundResource(R.drawable.redborder);
			team2HighL.setBackgroundResource(R.drawable.redborder);
			team2LowL.setBackgroundResource(R.drawable.redborder);
			
			team3AutoHighL.setBackgroundResource(R.drawable.redborder);
			team3AutoLowL.setBackgroundResource(R.drawable.redborder);
			team3HighL.setBackgroundResource(R.drawable.redborder);
			team3LowL.setBackgroundResource(R.drawable.redborder);
		}
	}

}
