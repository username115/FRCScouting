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

import java.util.Map;

import org.growingstems.scouting.PitStats;

public class PitStatsUA extends PitStats {
	
	//public double weight;
	//public String cg;
	public boolean score_high;
	public boolean score_mid;
	public boolean score_low;
	public boolean score_pyramid;
	public boolean load_floor;
	public boolean load_player;
	public int load_pre;
	public int num_carry;
	public int climb_pyramid;
	public int height;

	public PitStatsUA() {
		init();
	}

	public void init() {
		super.init();
		//weight = 0.0;
		//cg = "high";
		score_high = false;
		score_mid = false;
		score_low = false;
		score_pyramid = false;
		load_floor = false;
		load_player = false;
		load_pre = 0;
		num_carry = 0;
		climb_pyramid = 0;
		height = 0;
	}

	public Map<String, String> getPost() {
		Map<String, String> args = super.getPost();
		//args.put("weight", String.valueOf(weight));
		//args.put("cg_id", cg);
		args.put("score_high", score_high ? "1" : "0");
		args.put("score_mid", score_mid ? "1" : "0");
		args.put("score_low", score_low ? "1" : "0");
		args.put("score_pyramid", score_pyramid ? "1" : "0");
		args.put("load_floor", load_floor ? "1" : "0");
		args.put("load_player", load_player ? "1" : "0");
		args.put("load_preload", String.valueOf(load_pre));
		args.put("carry_disks", String.valueOf(num_carry));
		args.put("pyramid_climb", String.valueOf(climb_pyramid));
		args.put("max_height", String.valueOf(height));

		return args;
	}
}
