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

package org.frc836.database;

import java.util.HashMap;
import java.util.Map;

public abstract class PitStats {
	
	public int team;
	public String chassis_config;
	public String wheel_type;
	public String wheel_base;
	public boolean auto_mode;
	public String comments;
	
	
	public PitStats()
	{
		init();
	}
	
	public void init()
	{
		team = 0;
		chassis_config = "other";
		wheel_type = "other";
		wheel_base = "other";
		auto_mode = false;
		comments = "";
	}
	
	public Map<String, String> getPost()
	{
		HashMap<String, String> args = new HashMap<String, String>();
		args.put("team_id", String.valueOf(team));
		args.put("configuration_id", chassis_config);
		args.put("wheel_type_id", wheel_type);
		args.put("wheel_base_id", wheel_base);
		args.put("autonomous_mode", auto_mode ? "1" : "0");
		args.put("scout_comments", comments);
		
		return args;
	}

}
