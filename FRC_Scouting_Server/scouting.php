<?php

/* 
 * Copyright 2016 Daniel Logan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

header($_SERVER['SERVER_PROTOCOL'] . ' 403 Forbidden');
define('INCLUDE_CHECK', true);
include('scouting-header.php');

function checkVersion($client_version, $server_version) {
	$cver = substr(trim($client_version), 0, strrchr(trim($client_version), '.'));
	$sver = substr(trim($server_version), 0, strrchr(trim($server_version), '.'));
	return strcasecmp($cver, $sver) == 0;
}

function genJSON($sql_result, $tablename) {
	$json = '"' . $tablename . '":[';

	$firstrow = true;

	while($row = mysql_fetch_array($sql_result, 1)) {
		if ($firstrow == false) {
			$json .= ",";
		}
		$firstrow = false;
		$i = 0;
		$json .= "{";
		$firstcell = true;
		foreach($row as $cell) {
			if ($firstcell == false) {
				$json .= ",";
			}
			$firstcell = false;
			// Escaping illegal characters
			$cell = str_replace("\\", "\\\\", $cell);
			$cell = str_replace("\"", "\\\"", $cell);
			$cell = str_replace("/", "\\/", $cell);
			$cell = str_replace("\n", "\\n", $cell);
			$cell = str_replace("\r", "\\r", $cell);
			$cell = str_replace("\t", "\\t", $cell);

			$col_name = mysql_field_name($sql_result, $i);
			$col_type = mysql_fetch_field($sql_result, $i);

			$json .= '"' . $col_name . '":' ;

			//echo $col_name . ": " . $col_type->type . "\n";
			if ($col_type->type == 'timestamp') {
				$json .= strtotime($cell);
			}
			elseif ($col_type->numeric == 1 ) {
				$json .= $cell;
			} else {
				$json .= '"' . $cell . '"';
			}
			$i++;
		}
		$json .= "}";
	}

	$json .= "]";

	return $json;
}

if ($_POST['type'] == 'passConfirm' && $_POST['password'] == $pass) {
	header($_SERVER['SERVER_PROTOCOL'] . ' 200 OK');
	echo 'success';
}
elseif ($_POST['type'] == 'versioncheck') {
	header($_SERVER['SERVER_PROTOCOL'] . ' 200 OK');
	echo $ver;
}

elseif ($_POST['password'] == $pass) {
	header($_SERVER['SERVER_PROTOCOL'] . ' 200 OK');

	$client_version = $_POST['version'];
	$verMatch = checkVersion($client_version, $ver);

	if ($_POST['type'] == 'fullsync' || $_POST['type'] == 'sync') {
		//syncronization request. if it's a fullsync, then send all data, otherwise use the timestamp (in unix time)
		if ($_POST['type'] == 'fullsync') {
			$suffix = ';';
		} else {
			$suffix = ' WHERE timestamp >= FROM_UNIXTIME(' . $_POST['timestamp'] . ');';
		}

		$json = '{"timestamp" : ' . strtotime(date("Y-m-d H:i:s")) . ',';
		$json .= '"version" : "' . $ver . '",';

		//position_lu
		$query = "SELECT * FROM position_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "position_lu") . ",";
		mysql_free_result($result);

		//robot_lu
		$query = "SELECT * FROM robot_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "robot_lu") . ",";
		mysql_free_result($result);

		//wheel_base_lu
		$query = "SELECT * FROM wheel_base_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "wheel_base_lu") . ",";
		mysql_free_result($result);

		//scout_pit_data_2017
		$query = "SELECT * FROM scout_pit_data_2017" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "scout_pit_data_2017") . ",";
		mysql_free_result($result);

		//fact_match_data_2017
		$query = "SELECT * FROM fact_match_data_2017" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "fact_match_data_2017") . ",";
		mysql_free_result($result);

		//notes_options
		$query = "SELECT * FROM notes_options" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "notes_options") . ",";
		mysql_free_result($result);

		//configuration_lu
		$query = "SELECT * FROM configuration_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "configuration_lu") . ",";
		mysql_free_result($result);

		//picklist
		$query = "SELECT * FROM picklist" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "picklist") . ",";
		mysql_free_result($result);

		//fact_pilot_data_2017
		$query = "SELECT * FROM fact_pilot_data_2017" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "fact_pilot_data_2017") . ",";
		mysql_free_result($result);

		//event_lu
		$query = "SELECT * FROM event_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "event_lu") . ",";
		mysql_free_result($result);

		//wheel_type_lu
		$query = "SELECT * FROM wheel_type_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "wheel_type_lu") . ",";
		mysql_free_result($result);

		//game_info
		$query = "SELECT * FROM game_info" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "game_info") . "}";
		mysql_free_result($result);

		$resp = $json;
	}
	else if ($verMatch == false) {
		$resp = 'Version Mismatch, server using version ' . $ver;
	}
	else if ($_POST['type'] == 'match') {
		$event_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['event_id']) ? $_POST['event_id'] : '0')));
		$team_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['team_id']) ? $_POST['team_id'] : '0')));
		$match_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['match_id']) ? $_POST['match_id'] : '0')));
		$practice_match = mysql_real_escape_string(stripslashes(trim(isset($_POST['practice_match']) ? $_POST['practice_match'] : '0')));
		$position_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['position_id']) ? $_POST['position_id'] : '0')));
		$auto_score_low = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_score_low']) ? $_POST['auto_score_low'] : '0')));
		$auto_score_high = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_score_high']) ? $_POST['auto_score_high'] : '0')));
		$auto_miss_high = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_miss_high']) ? $_POST['auto_miss_high'] : '0')));
		$auto_cross_baseline = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_cross_baseline']) ? $_POST['auto_cross_baseline'] : '0')));
		$auto_gear_delivered_left = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_gear_delivered_left']) ? $_POST['auto_gear_delivered_left'] : '0')));
		$auto_gear_delivered_right = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_gear_delivered_right']) ? $_POST['auto_gear_delivered_right'] : '0')));
		$auto_gear_delivered_center = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_gear_delivered_center']) ? $_POST['auto_gear_delivered_center'] : '0')));
		$auto_dump_hopper = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_dump_hopper']) ? $_POST['auto_dump_hopper'] : '0')));
		$score_low = mysql_real_escape_string(stripslashes(trim(isset($_POST['score_low']) ? $_POST['score_low'] : '0')));
		$score_high = mysql_real_escape_string(stripslashes(trim(isset($_POST['score_high']) ? $_POST['score_high'] : '0')));
		$miss_high = mysql_real_escape_string(stripslashes(trim(isset($_POST['miss_high']) ? $_POST['miss_high'] : '0')));
		$gear_delivered_left = mysql_real_escape_string(stripslashes(trim(isset($_POST['gear_delivered_left']) ? $_POST['gear_delivered_left'] : '0')));
		$gear_delivered_right = mysql_real_escape_string(stripslashes(trim(isset($_POST['gear_delivered_right']) ? $_POST['gear_delivered_right'] : '0')));
		$gear_delivered_center = mysql_real_escape_string(stripslashes(trim(isset($_POST['gear_delivered_center']) ? $_POST['gear_delivered_center'] : '0')));
		$climb_rope = mysql_real_escape_string(stripslashes(trim(isset($_POST['climb_rope']) ? $_POST['climb_rope'] : '0')));
		$climb_attempt = mysql_real_escape_string(stripslashes(trim(isset($_POST['climb_attempt']) ? $_POST['climb_attempt'] : '0')));
		$align_time = mysql_real_escape_string(stripslashes(trim(isset($_POST['align_time']) ? $_POST['align_time'] : '0')));
		$climb_time = mysql_real_escape_string(stripslashes(trim(isset($_POST['climb_time']) ? $_POST['climb_time'] : '0')));
		$foul = mysql_real_escape_string(stripslashes(trim(isset($_POST['foul']) ? $_POST['foul'] : '0')));
		$yellow_card = mysql_real_escape_string(stripslashes(trim(isset($_POST['yellow_card']) ? $_POST['yellow_card'] : '0')));
		$red_card = mysql_real_escape_string(stripslashes(trim(isset($_POST['red_card']) ? $_POST['red_card'] : '0')));
		$tip_over = mysql_real_escape_string(stripslashes(trim(isset($_POST['tip_over']) ? $_POST['tip_over'] : '0')));
		$notes = mysql_real_escape_string(stripslashes(trim(isset($_POST['notes']) ? $_POST['notes'] : '0')));

		$result = mysql_query("SELECT id FROM fact_match_data_2017 WHERE event_id=" . $event_id . " AND match_id=" . $match_id . " AND team_id=" . $team_id . " AND practice_match=" . $practice_match);

		$row = mysql_fetch_array($result);
		$match_row_id = $row["id"];

		if (mysql_num_rows($result) == 0) {

			$query = "INSERT INTO fact_match_data_2017(event_id,team_id,match_id,practice_match,position_id,auto_score_low,auto_score_high,auto_miss_high,auto_cross_baseline,auto_gear_delivered_left,auto_gear_delivered_right,auto_gear_delivered_center,auto_dump_hopper,score_low,score_high,miss_high,gear_delivered_left,gear_delivered_right,gear_delivered_center,climb_rope,climb_attempt,align_time,climb_time,foul,yellow_card,red_card,tip_over,notes,invalid) VALUES("
				. $event_id . ","
				. $team_id . ","
				. $match_id . ","
				. $practice_match . ","
				. $position_id . ","
				. $auto_score_low . ","
				. $auto_score_high . ","
				. $auto_miss_high . ","
				. $auto_cross_baseline . ","
				. $auto_gear_delivered_left . ","
				. $auto_gear_delivered_right . ","
				. $auto_gear_delivered_center . ","
				. $auto_dump_hopper . ","
				. $score_low . ","
				. $score_high . ","
				. $miss_high . ","
				. $gear_delivered_left . ","
				. $gear_delivered_right . ","
				. $gear_delivered_center . ","
				. $climb_rope . ","
				. $climb_attempt . ","
				. $align_time . ","
				. $climb_time . ","
				. $foul . ","
				. $yellow_card . ","
				. $red_card . ","
				. $tip_over . ","
				. "'" . $notes . "',"
				. "0);";
			$success = mysql_query($query);
		}
		else {
			$query = "UPDATE fact_match_data_2017 SET "
				. "event_id=" . $event_id . ","
				. "team_id=" . $team_id . ","
				. "match_id=" . $match_id . ","
				. "practice_match=" . $practice_match . ","
				. "position_id=" . $position_id . ","
				. "auto_score_low=" . $auto_score_low . ","
				. "auto_score_high=" . $auto_score_high . ","
				. "auto_miss_high=" . $auto_miss_high . ","
				. "auto_cross_baseline=" . $auto_cross_baseline . ","
				. "auto_gear_delivered_left=" . $auto_gear_delivered_left . ","
				. "auto_gear_delivered_right=" . $auto_gear_delivered_right . ","
				. "auto_gear_delivered_center=" . $auto_gear_delivered_center . ","
				. "auto_dump_hopper=" . $auto_dump_hopper . ","
				. "score_low=" . $score_low . ","
				. "score_high=" . $score_high . ","
				. "miss_high=" . $miss_high . ","
				. "gear_delivered_left=" . $gear_delivered_left . ","
				. "gear_delivered_right=" . $gear_delivered_right . ","
				. "gear_delivered_center=" . $gear_delivered_center . ","
				. "climb_rope=" . $climb_rope . ","
				. "climb_attempt=" . $climb_attempt . ","
				. "align_time=" . $align_time . ","
				. "climb_time=" . $climb_time . ","
				. "foul=" . $foul . ","
				. "yellow_card=" . $yellow_card . ","
				. "red_card=" . $red_card . ","
				. "tip_over=" . $tip_over . ","
				. "notes='" . $notes . "',"
				. "invalid=0"
				. " WHERE id=" . $match_row_id;

			$success = mysql_query($query);
		}
		if ($success) {
			$result = mysql_query("SELECT id, timestamp FROM fact_match_data_2017 WHERE event_id=" . $event_id . " AND match_id=" . $match_id . " AND team_id=" . $team_id . " AND practice_match=" . $practice_match);
			$row = mysql_fetch_array($result);
			$resp = $row["id"] . "," . strtotime($row["timestamp"]);
		} else {
			$resp = 'Database Query Failed : \n' . $query;
		}
	}
	else if ($_POST['type'] == 'pilot') {
		$event_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['event_id']) ? $_POST['event_id'] : '0')));
		$team_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['team_id']) ? $_POST['team_id'] : '0')));
		$match_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['match_id']) ? $_POST['match_id'] : '0')));
		$practice_match = mysql_real_escape_string(stripslashes(trim(isset($_POST['practice_match']) ? $_POST['practice_match'] : '0')));
		$position_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['position_id']) ? $_POST['position_id'] : '0')));
		$gears_installed_2 = mysql_real_escape_string(stripslashes(trim(isset($_POST['gears_installed_2']) ? $_POST['gears_installed_2'] : '0')));
		$gears_installed_3 = mysql_real_escape_string(stripslashes(trim(isset($_POST['gears_installed_3']) ? $_POST['gears_installed_3'] : '0')));
		$gears_installed_4 = mysql_real_escape_string(stripslashes(trim(isset($_POST['gears_installed_4']) ? $_POST['gears_installed_4'] : '0')));
		$gears_lifted = mysql_real_escape_string(stripslashes(trim(isset($_POST['gears_lifted']) ? $_POST['gears_lifted'] : '0')));
		$gears_dropped = mysql_real_escape_string(stripslashes(trim(isset($_POST['gears_dropped']) ? $_POST['gears_dropped'] : '0')));
		$rotor_1_started = mysql_real_escape_string(stripslashes(trim(isset($_POST['rotor_1_started']) ? $_POST['rotor_1_started'] : '0')));
		$rotor_2_started = mysql_real_escape_string(stripslashes(trim(isset($_POST['rotor_2_started']) ? $_POST['rotor_2_started'] : '0')));
		$rotor_3_started = mysql_real_escape_string(stripslashes(trim(isset($_POST['rotor_3_started']) ? $_POST['rotor_3_started'] : '0')));
		$rotor_4_started = mysql_real_escape_string(stripslashes(trim(isset($_POST['rotor_4_started']) ? $_POST['rotor_4_started'] : '0')));
		$foul = mysql_real_escape_string(stripslashes(trim(isset($_POST['foul']) ? $_POST['foul'] : '0')));
		$yellow_card = mysql_real_escape_string(stripslashes(trim(isset($_POST['yellow_card']) ? $_POST['yellow_card'] : '0')));
		$red_card = mysql_real_escape_string(stripslashes(trim(isset($_POST['red_card']) ? $_POST['red_card'] : '0')));
		$notes = mysql_real_escape_string(stripslashes(trim(isset($_POST['notes']) ? $_POST['notes'] : '0')));

		$result = mysql_query("SELECT id FROM fact_pilot_data_2017 WHERE event_id=" . $event_id . " AND match_id=" . $match_id . " AND team_id=" . $team_id . " AND practice_match=" . $practice_match);

		$row = mysql_fetch_array($result);
		$match_row_id = $row["id"];

		if (mysql_num_rows($result) == 0) {

			$query = "INSERT INTO fact_pilot_data_2017(event_id,team_id,match_id,practice_match,position_id,gears_installed_2,gears_installed_3,gears_installed_4,gears_lifted,gears_dropped,rotor_1_started,rotor_2_started,rotor_3_started,rotor_4_started,foul,yellow_card,red_card,notes,invalid) VALUES("
				. $event_id . ","
				. $team_id . ","
				. $match_id . ","
				. $practice_match . ","
				. $position_id . ","
				. $gears_installed_2 . ","
				. $gears_installed_3 . ","
				. $gears_installed_4 . ","
				. $gears_lifted . ","
				. $gears_dropped . ","
				. $rotor_1_started . ","
				. $rotor_2_started . ","
				. $rotor_3_started . ","
				. $rotor_4_started . ","
				. $foul . ","
				. $yellow_card . ","
				. $red_card . ","
				. "'" . $notes . "',"
				. "0);";
			$success = mysql_query($query);
		}
		else {
			$query = "UPDATE fact_pilot_data_2017 SET "
				. "event_id=" . $event_id . ","
				. "team_id=" . $team_id . ","
				. "match_id=" . $match_id . ","
				. "practice_match=" . $practice_match . ","
				. "position_id=" . $position_id . ","
				. "gears_installed_2=" . $gears_installed_2 . ","
				. "gears_installed_3=" . $gears_installed_3 . ","
				. "gears_installed_4=" . $gears_installed_4 . ","
				. "gears_lifted=" . $gears_lifted . ","
				. "gears_dropped=" . $gears_dropped . ","
				. "rotor_1_started=" . $rotor_1_started . ","
				. "rotor_2_started=" . $rotor_2_started . ","
				. "rotor_3_started=" . $rotor_3_started . ","
				. "rotor_4_started=" . $rotor_4_started . ","
				. "foul=" . $foul . ","
				. "yellow_card=" . $yellow_card . ","
				. "red_card=" . $red_card . ","
				. "notes='" . $notes . "',"
				. "invalid=0"
				. " WHERE id=" . $match_row_id;

			$success = mysql_query($query);
		}
		if ($success) {
			$result = mysql_query("SELECT id, timestamp FROM fact_pilot_data_2017 WHERE event_id=" . $event_id . " AND match_id=" . $match_id . " AND team_id=" . $team_id . " AND practice_match=" . $practice_match);
			$row = mysql_fetch_array($result);
			$resp = $row["id"] . "," . strtotime($row["timestamp"]);
		} else {
			$resp = 'Database Query Failed : \n' . $query;
		}
	}
	else if ($_POST['type'] == 'pits') {
		$team_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['team_id']) ? $_POST['team_id'] : '0')));
		$can_score_high = mysql_real_escape_string(stripslashes(trim(isset($_POST['can_score_high']) ? $_POST['can_score_high'] : '0')));
		$can_score_low = mysql_real_escape_string(stripslashes(trim(isset($_POST['can_score_low']) ? $_POST['can_score_low'] : '0')));
		$can_score_gears = mysql_real_escape_string(stripslashes(trim(isset($_POST['can_score_gears']) ? $_POST['can_score_gears'] : '0')));
		$can_climb = mysql_real_escape_string(stripslashes(trim(isset($_POST['can_climb']) ? $_POST['can_climb'] : '0')));
		$ground_load_fuel = mysql_real_escape_string(stripslashes(trim(isset($_POST['ground_load_fuel']) ? $_POST['ground_load_fuel'] : '0')));
		$hopper_load_fuel = mysql_real_escape_string(stripslashes(trim(isset($_POST['hopper_load_fuel']) ? $_POST['hopper_load_fuel'] : '0')));
		$station_load_fuel = mysql_real_escape_string(stripslashes(trim(isset($_POST['station_load_fuel']) ? $_POST['station_load_fuel'] : '0')));
		$ground_load_gear = mysql_real_escape_string(stripslashes(trim(isset($_POST['ground_load_gear']) ? $_POST['ground_load_gear'] : '0')));
		$station_load_gear = mysql_real_escape_string(stripslashes(trim(isset($_POST['station_load_gear']) ? $_POST['station_load_gear'] : '0')));
		$custom_rope = mysql_real_escape_string(stripslashes(trim(isset($_POST['custom_rope']) ? $_POST['custom_rope'] : '0')));
		$auto_score_high_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_score_high_count']) ? $_POST['auto_score_high_count'] : '0')));
		$auto_score_low_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_score_low_count']) ? $_POST['auto_score_low_count'] : '0')));
		$auto_gear = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_gear']) ? $_POST['auto_gear'] : '0')));
		$auto_hopper = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_hopper']) ? $_POST['auto_hopper'] : '0')));
		$tele_score_high_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['tele_score_high_count']) ? $_POST['tele_score_high_count'] : '0')));
		$tele_score_low_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['tele_score_low_count']) ? $_POST['tele_score_low_count'] : '0')));
		$accuracy = mysql_real_escape_string(stripslashes(trim(isset($_POST['accuracy']) ? $_POST['accuracy'] : '0')));
		$fuel_capacity = mysql_real_escape_string(stripslashes(trim(isset($_POST['fuel_capacity']) ? $_POST['fuel_capacity'] : '0')));
		$scoring_speed_bps = mysql_real_escape_string(stripslashes(trim(isset($_POST['scoring_speed_bps']) ? $_POST['scoring_speed_bps'] : '0')));
		$loading_speed_bps = mysql_real_escape_string(stripslashes(trim(isset($_POST['loading_speed_bps']) ? $_POST['loading_speed_bps'] : '0')));
		$max_robot_speed_fts = mysql_real_escape_string(stripslashes(trim(isset($_POST['max_robot_speed_fts']) ? $_POST['max_robot_speed_fts'] : '0')));
		$robot_gross_weight_lbs = mysql_real_escape_string(stripslashes(trim(isset($_POST['robot_gross_weight_lbs']) ? $_POST['robot_gross_weight_lbs'] : '0')));
		$config_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['config_id']) ? $_POST['config_id'] : '0')));
		$wheel_base_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['wheel_base_id']) ? $_POST['wheel_base_id'] : '0')));
		$wheel_type_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['wheel_type_id']) ? $_POST['wheel_type_id'] : '0')));
		$notes = mysql_real_escape_string(stripslashes(trim(isset($_POST['notes']) ? $_POST['notes'] : '0')));

		$result = mysql_query("SELECT id FROM scout_pit_data_2017 WHERE team_id=" . $team_id);

		$row = mysql_fetch_array($result);
		$match_row_id = $row["id"];

		if (mysql_num_rows($result) == 0) {

			$query = "INSERT INTO scout_pit_data_2017(team_id,can_score_high,can_score_low,can_score_gears,can_climb,ground_load_fuel,hopper_load_fuel,station_load_fuel,ground_load_gear,station_load_gear,custom_rope,auto_score_high_count,auto_score_low_count,auto_gear,auto_hopper,tele_score_high_count,tele_score_low_count,accuracy,fuel_capacity,scoring_speed_bps,loading_speed_bps,max_robot_speed_fts,robot_gross_weight_lbs,config_id,wheel_base_id,wheel_type_id,notes,invalid) VALUES("
				. $team_id . ","
				. $can_score_high . ","
				. $can_score_low . ","
				. $can_score_gears . ","
				. $can_climb . ","
				. $ground_load_fuel . ","
				. $hopper_load_fuel . ","
				. $station_load_fuel . ","
				. $ground_load_gear . ","
				. $station_load_gear . ","
				. $custom_rope . ","
				. $auto_score_high_count . ","
				. $auto_score_low_count . ","
				. $auto_gear . ","
				. $auto_hopper . ","
				. $tele_score_high_count . ","
				. $tele_score_low_count . ","
				. $accuracy . ","
				. $fuel_capacity . ","
				. $scoring_speed_bps . ","
				. $loading_speed_bps . ","
				. $max_robot_speed_fts . ","
				. $robot_gross_weight_lbs . ","
				. $config_id . ","
				. $wheel_base_id . ","
				. $wheel_type_id . ","
				. "'" . $notes . "',"
				. "0);";
			$success = mysql_query($query);
		}
		else {
			$query = "UPDATE scout_pit_data_2017 SET "
				. "team_id=" . $team_id . ","
				. "can_score_high=" . $can_score_high . ","
				. "can_score_low=" . $can_score_low . ","
				. "can_score_gears=" . $can_score_gears . ","
				. "can_climb=" . $can_climb . ","
				. "ground_load_fuel=" . $ground_load_fuel . ","
				. "hopper_load_fuel=" . $hopper_load_fuel . ","
				. "station_load_fuel=" . $station_load_fuel . ","
				. "ground_load_gear=" . $ground_load_gear . ","
				. "station_load_gear=" . $station_load_gear . ","
				. "custom_rope=" . $custom_rope . ","
				. "auto_score_high_count=" . $auto_score_high_count . ","
				. "auto_score_low_count=" . $auto_score_low_count . ","
				. "auto_gear=" . $auto_gear . ","
				. "auto_hopper=" . $auto_hopper . ","
				. "tele_score_high_count=" . $tele_score_high_count . ","
				. "tele_score_low_count=" . $tele_score_low_count . ","
				. "accuracy=" . $accuracy . ","
				. "fuel_capacity=" . $fuel_capacity . ","
				. "scoring_speed_bps=" . $scoring_speed_bps . ","
				. "loading_speed_bps=" . $loading_speed_bps . ","
				. "max_robot_speed_fts=" . $max_robot_speed_fts . ","
				. "robot_gross_weight_lbs=" . $robot_gross_weight_lbs . ","
				. "config_id=" . $config_id . ","
				. "wheel_base_id=" . $wheel_base_id . ","
				. "wheel_type_id=" . $wheel_type_id . ","
				. "notes='" . $notes . "',"
				. "invalid=0"
				. " WHERE id=" . $match_row_id;

			$success = mysql_query($query);
		}
		if ($success) {
			$result = mysql_query("SELECT id, timestamp FROM scout_pit_data_2017 WHERE team_id=" . $team_id);
			$row = mysql_fetch_array($result);
			$resp = $row["id"] . "," . strtotime($row["timestamp"]);
		} else {
			$resp = 'Database Query Failed : \n' . $query;
		}
	}
	else if ($_POST['type'] == 'picklist') {
		$event_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['event_id']) ? $_POST['event_id'] : '0')));
		$team_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['team_id']) ? $_POST['team_id'] : '0')));
		$sort = mysql_real_escape_string(stripslashes(trim(isset($_POST['sort']) ? $_POST['sort'] : '0')));
		$picked = mysql_real_escape_string(stripslashes(trim(isset($_POST['picked']) ? $_POST['picked'] : '0')));
		$removed = mysql_real_escape_string(stripslashes(trim(isset($_POST['removed']) ? $_POST['removed'] : '0')));

		$result = mysql_query("SELECT id FROM picklist WHERE event_id=" . $event_id . " AND team_id=" . $team_id);

		$row = mysql_fetch_array($result);
		$match_row_id = $row["id"];

		if (mysql_num_rows($result) == 0) {

			$query = "INSERT INTO picklist(event_id,team_id,sort,picked,removed,invalid) VALUES("
				. $event_id . ","
				. $team_id . ","
				. $sort . ","
				. $picked . ","
				. $removed . ","
				. "0);";
			$success = mysql_query($query);
		}
		else {
			$query = "UPDATE picklist SET "
				. "event_id=" . $event_id . ","
				. "team_id=" . $team_id . ","
				. "sort=" . $sort . ","
				. "picked=" . $picked . ","
				. "removed=" . $removed . ","
				. "invalid=0"
				. " WHERE id=" . $match_row_id;

			$success = mysql_query($query);
		}
		if ($success) {
			$result = mysql_query("SELECT id, timestamp FROM picklist WHERE event_id=" . $event_id . " AND team_id=" . $team_id);
			$row = mysql_fetch_array($result);
			$resp = $row["id"] . "," . strtotime($row["timestamp"]);
		} else {
			$resp = 'Database Query Failed : \n' . $query;
		}
	}

	else {
		$resp = 'invalid submission type';
	}

	echo $resp;
}
