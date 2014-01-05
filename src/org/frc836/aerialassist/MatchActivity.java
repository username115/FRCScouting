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

import org.growingstems.scouting.ParamList;
import org.growingstems.scouting.R;

import android.app.Activity;
import android.os.Bundle;

public class MatchActivity extends Activity {

	private String HELPMESSAGE;

	private MatchStatsAA data;
	
	private int orientation;
	
	private ParamList notesOptions;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.match);
		
		orientation = getResources().getConfiguration().orientation;
		
		notesOptions = new ParamList(getApplicationContext(), "notes_options");
	}
}
