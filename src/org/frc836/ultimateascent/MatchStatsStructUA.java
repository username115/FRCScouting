/*
 * Copyright 2013 Daniel Logan
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

package org.frc836.ultimateascent;

import java.util.HashMap;
import java.util.Map;

import org.growingstems.scouting.MatchStatsStruct;

public class MatchStatsStructUA extends MatchStatsStruct {
	
	public int high;
	public int highAtt;
	public int midR;
	public int midRAtt;
	public int midL;
	public int midLAtt;
	public int low;
	public int lowAtt;
	public int pyramidG;
	public int pyramidGAtt;
	public int pyramidLevel;
	public int pyramidLevelAtt;
	
	public MatchStatsStructUA()
	{
		super();
		init();
	}
	
	public MatchStatsStructUA(int team, String event, int match)
	{
		super(team, event, match);
		init();
	}
	
	public MatchStatsStructUA(int team, String event, int match, boolean auto)
	{
		super(team, event, match, auto);
		init();
	}
	
	public void init()
	{
		high = 0;
		highAtt = 0;
		midR = 0;
		midRAtt = 0;
		midL = 0;
		midLAtt = 0;
		low = 0;
		lowAtt = 0;
		pyramidG = 0;
		pyramidGAtt = 0;
		pyramidLevel = 0;
		pyramidLevelAtt = 0;
	}
	
	public Map<String, String> getPost()
	{
		Map<String, String> args;
		
		if (autonomous)
		{
			args = new HashMap<String, String>();
			args.put("auto_score_high", String.valueOf(high));
			args.put("auto_score_high_attempt", String.valueOf(highAtt));
			args.put("auto_score_mid_left", String.valueOf(midL));
			args.put("auto_score_mid_left_attempt", String.valueOf(midLAtt));
			args.put("auto_score_mid_right", String.valueOf(midR));
			args.put("auto_score_mid_right_attempt", String.valueOf(midRAtt));
			args.put("auto_score_low", String.valueOf(low));
			args.put("auto_score_low_attempt", String.valueOf(lowAtt));
		}
		else
		{
			args = super.getPost();
			args.put("score_high", String.valueOf(high));
			args.put("score_high_attempt", String.valueOf(highAtt));
			args.put("score_mid_left", String.valueOf(midL));
			args.put("score_mid_left_attempt", String.valueOf(midLAtt));
			args.put("score_mid_right", String.valueOf(midR));
			args.put("score_mid_right_attempt", String.valueOf(midRAtt));
			args.put("score_low", String.valueOf(low));
			args.put("score_low_attempt", String.valueOf(lowAtt));
			args.put("score_pyramid", String.valueOf(pyramidG));
			args.put("score_pyramid_attempt", String.valueOf(pyramidGAtt));
			args.put("pyramid_level_climbed", String.valueOf(pyramidLevel));
			args.put("pyramid_level_attempt", String.valueOf(pyramidLevelAtt));
		}
		
		return args;
	}

}
