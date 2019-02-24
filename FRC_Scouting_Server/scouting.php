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

		//configuration_lu
		$query = "SELECT * FROM configuration_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "configuration_lu") . ",";
		mysql_free_result($result);

		//event_lu
		$query = "SELECT * FROM event_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "event_lu") . ",";
		mysql_free_result($result);

		//fact_match_data_2019
		$query = "SELECT * FROM fact_match_data_2019" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "fact_match_data_2019") . ",";
		mysql_free_result($result);

		//game_info
		$query = "SELECT * FROM game_info" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "game_info") . ",";
		mysql_free_result($result);

		//notes_options
		$query = "SELECT * FROM notes_options" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "notes_options") . ",";
		mysql_free_result($result);

		//picklist
		$query = "SELECT * FROM picklist" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "picklist") . ",";
		mysql_free_result($result);

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

		//scout_pit_data_2019
		$query = "SELECT * FROM scout_pit_data_2019" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "scout_pit_data_2019") . ",";
		mysql_free_result($result);

		//wheel_base_lu
		$query = "SELECT * FROM wheel_base_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "wheel_base_lu") . ",";
		mysql_free_result($result);

		//wheel_type_lu
		$query = "SELECT * FROM wheel_type_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "wheel_type_lu") . "}";
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
		$prematch_robot_cargo = mysql_real_escape_string(stripslashes(trim(isset($_POST['prematch_robot_cargo']) ? $_POST['prematch_robot_cargo'] : '0')));
		$prematch_robot_hatch = mysql_real_escape_string(stripslashes(trim(isset($_POST['prematch_robot_hatch']) ? $_POST['prematch_robot_hatch'] : '0')));
		$prematch_hab2_left = mysql_real_escape_string(stripslashes(trim(isset($_POST['prematch_hab2_left']) ? $_POST['prematch_hab2_left'] : '0')));
		$prematch_hab_level = mysql_real_escape_string(stripslashes(trim(isset($_POST['prematch_hab_level']) ? $_POST['prematch_hab_level'] : '0')));
		$sandstorm_bonus = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_bonus']) ? $_POST['sandstorm_bonus'] : '0')));
		$sandstorm_hatch_ship = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_hatch_ship']) ? $_POST['sandstorm_hatch_ship'] : '0')));
		$sandstorm_hatch_rocket_1 = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_hatch_rocket_1']) ? $_POST['sandstorm_hatch_rocket_1'] : '0')));
		$sandstorm_hatch_rocket_2 = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_hatch_rocket_2']) ? $_POST['sandstorm_hatch_rocket_2'] : '0')));
		$sandstorm_hatch_rocket_3 = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_hatch_rocket_3']) ? $_POST['sandstorm_hatch_rocket_3'] : '0')));
		$sandstorm_hatch_dropped = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_hatch_dropped']) ? $_POST['sandstorm_hatch_dropped'] : '0')));
		$sandstorm_cargo_ship = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_cargo_ship']) ? $_POST['sandstorm_cargo_ship'] : '0')));
		$sandstorm_cargo_rocket_1 = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_cargo_rocket_1']) ? $_POST['sandstorm_cargo_rocket_1'] : '0')));
		$sandstorm_cargo_rocket_2 = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_cargo_rocket_2']) ? $_POST['sandstorm_cargo_rocket_2'] : '0')));
		$sandstorm_cargo_rocket_3 = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_cargo_rocket_3']) ? $_POST['sandstorm_cargo_rocket_3'] : '0')));
		$sandstorm_cargo_dropped = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_cargo_dropped']) ? $_POST['sandstorm_cargo_dropped'] : '0')));
		$hatch_ship = mysql_real_escape_string(stripslashes(trim(isset($_POST['hatch_ship']) ? $_POST['hatch_ship'] : '0')));
		$hatch_rocket_1 = mysql_real_escape_string(stripslashes(trim(isset($_POST['hatch_rocket_1']) ? $_POST['hatch_rocket_1'] : '0')));
		$hatch_rocket_2 = mysql_real_escape_string(stripslashes(trim(isset($_POST['hatch_rocket_2']) ? $_POST['hatch_rocket_2'] : '0')));
		$hatch_rocket_3 = mysql_real_escape_string(stripslashes(trim(isset($_POST['hatch_rocket_3']) ? $_POST['hatch_rocket_3'] : '0')));
		$hatch_dropped = mysql_real_escape_string(stripslashes(trim(isset($_POST['hatch_dropped']) ? $_POST['hatch_dropped'] : '0')));
		$cargo_ship = mysql_real_escape_string(stripslashes(trim(isset($_POST['cargo_ship']) ? $_POST['cargo_ship'] : '0')));
		$cargo_rocket_1 = mysql_real_escape_string(stripslashes(trim(isset($_POST['cargo_rocket_1']) ? $_POST['cargo_rocket_1'] : '0')));
		$cargo_rocket_2 = mysql_real_escape_string(stripslashes(trim(isset($_POST['cargo_rocket_2']) ? $_POST['cargo_rocket_2'] : '0')));
		$cargo_rocket_3 = mysql_real_escape_string(stripslashes(trim(isset($_POST['cargo_rocket_3']) ? $_POST['cargo_rocket_3'] : '0')));
		$cargo_dropped = mysql_real_escape_string(stripslashes(trim(isset($_POST['cargo_dropped']) ? $_POST['cargo_dropped'] : '0')));
		$hab_climb_level = mysql_real_escape_string(stripslashes(trim(isset($_POST['hab_climb_level']) ? $_POST['hab_climb_level'] : '0')));
		$hab_climb_level_attempted = mysql_real_escape_string(stripslashes(trim(isset($_POST['hab_climb_level_attempted']) ? $_POST['hab_climb_level_attempted'] : '0')));
		$hab_climb_2_left = mysql_real_escape_string(stripslashes(trim(isset($_POST['hab_climb_2_left']) ? $_POST['hab_climb_2_left'] : '0')));
		$floor_pickup_cargo = mysql_real_escape_string(stripslashes(trim(isset($_POST['floor_pickup_cargo']) ? $_POST['floor_pickup_cargo'] : '0')));
		$floor_pickup_hatch = mysql_real_escape_string(stripslashes(trim(isset($_POST['floor_pickup_hatch']) ? $_POST['floor_pickup_hatch'] : '0')));
		$foul = mysql_real_escape_string(stripslashes(trim(isset($_POST['foul']) ? $_POST['foul'] : '0')));
		$yellow_card = mysql_real_escape_string(stripslashes(trim(isset($_POST['yellow_card']) ? $_POST['yellow_card'] : '0')));
		$red_card = mysql_real_escape_string(stripslashes(trim(isset($_POST['red_card']) ? $_POST['red_card'] : '0')));
		$tip_over = mysql_real_escape_string(stripslashes(trim(isset($_POST['tip_over']) ? $_POST['tip_over'] : '0')));
		$notes = mysql_real_escape_string(stripslashes(trim(isset($_POST['notes']) ? $_POST['notes'] : '0')));

		$result = mysql_query("SELECT id FROM fact_match_data_2019 WHERE event_id=" . $event_id . " AND match_id=" . $match_id . " AND team_id=" . $team_id . " AND practice_match=" . $practice_match);

		$row = mysql_fetch_array($result);
		$match_row_id = $row["id"];

		if (mysql_num_rows($result) == 0) {

			$query = "INSERT INTO fact_match_data_2019(event_id,team_id,match_id,practice_match,position_id,prematch_robot_cargo,prematch_robot_hatch,prematch_hab2_left,prematch_hab_level,sandstorm_bonus,sandstorm_hatch_ship,sandstorm_hatch_rocket_1,sandstorm_hatch_rocket_2,sandstorm_hatch_rocket_3,sandstorm_hatch_dropped,sandstorm_cargo_ship,sandstorm_cargo_rocket_1,sandstorm_cargo_rocket_2,sandstorm_cargo_rocket_3,sandstorm_cargo_dropped,hatch_ship,hatch_rocket_1,hatch_rocket_2,hatch_rocket_3,hatch_dropped,cargo_ship,cargo_rocket_1,cargo_rocket_2,cargo_rocket_3,cargo_dropped,hab_climb_level,hab_climb_level_attempted,hab_climb_2_left,floor_pickup_cargo,floor_pickup_hatch,foul,yellow_card,red_card,tip_over,notes,invalid) VALUES("
				. $event_id . ","
				. $team_id . ","
				. $match_id . ","
				. $practice_match . ","
				. $position_id . ","
				. $prematch_robot_cargo . ","
				. $prematch_robot_hatch . ","
				. $prematch_hab2_left . ","
				. $prematch_hab_level . ","
				. $sandstorm_bonus . ","
				. $sandstorm_hatch_ship . ","
				. $sandstorm_hatch_rocket_1 . ","
				. $sandstorm_hatch_rocket_2 . ","
				. $sandstorm_hatch_rocket_3 . ","
				. $sandstorm_hatch_dropped . ","
				. $sandstorm_cargo_ship . ","
				. $sandstorm_cargo_rocket_1 . ","
				. $sandstorm_cargo_rocket_2 . ","
				. $sandstorm_cargo_rocket_3 . ","
				. $sandstorm_cargo_dropped . ","
				. $hatch_ship . ","
				. $hatch_rocket_1 . ","
				. $hatch_rocket_2 . ","
				. $hatch_rocket_3 . ","
				. $hatch_dropped . ","
				. $cargo_ship . ","
				. $cargo_rocket_1 . ","
				. $cargo_rocket_2 . ","
				. $cargo_rocket_3 . ","
				. $cargo_dropped . ","
				. $hab_climb_level . ","
				. $hab_climb_level_attempted . ","
				. $hab_climb_2_left . ","
				. $floor_pickup_cargo . ","
				. $floor_pickup_hatch . ","
				. $foul . ","
				. $yellow_card . ","
				. $red_card . ","
				. $tip_over . ","
				. "'" . $notes . "',"
				. "0);";
			$success = mysql_query($query);
		}
		else {
			$query = "UPDATE fact_match_data_2019 SET "
				. "event_id=" . $event_id . ","
				. "team_id=" . $team_id . ","
				. "match_id=" . $match_id . ","
				. "practice_match=" . $practice_match . ","
				. "position_id=" . $position_id . ","
				. "prematch_robot_cargo=" . $prematch_robot_cargo . ","
				. "prematch_robot_hatch=" . $prematch_robot_hatch . ","
				. "prematch_hab2_left=" . $prematch_hab2_left . ","
				. "prematch_hab_level=" . $prematch_hab_level . ","
				. "sandstorm_bonus=" . $sandstorm_bonus . ","
				. "sandstorm_hatch_ship=" . $sandstorm_hatch_ship . ","
				. "sandstorm_hatch_rocket_1=" . $sandstorm_hatch_rocket_1 . ","
				. "sandstorm_hatch_rocket_2=" . $sandstorm_hatch_rocket_2 . ","
				. "sandstorm_hatch_rocket_3=" . $sandstorm_hatch_rocket_3 . ","
				. "sandstorm_hatch_dropped=" . $sandstorm_hatch_dropped . ","
				. "sandstorm_cargo_ship=" . $sandstorm_cargo_ship . ","
				. "sandstorm_cargo_rocket_1=" . $sandstorm_cargo_rocket_1 . ","
				. "sandstorm_cargo_rocket_2=" . $sandstorm_cargo_rocket_2 . ","
				. "sandstorm_cargo_rocket_3=" . $sandstorm_cargo_rocket_3 . ","
				. "sandstorm_cargo_dropped=" . $sandstorm_cargo_dropped . ","
				. "hatch_ship=" . $hatch_ship . ","
				. "hatch_rocket_1=" . $hatch_rocket_1 . ","
				. "hatch_rocket_2=" . $hatch_rocket_2 . ","
				. "hatch_rocket_3=" . $hatch_rocket_3 . ","
				. "hatch_dropped=" . $hatch_dropped . ","
				. "cargo_ship=" . $cargo_ship . ","
				. "cargo_rocket_1=" . $cargo_rocket_1 . ","
				. "cargo_rocket_2=" . $cargo_rocket_2 . ","
				. "cargo_rocket_3=" . $cargo_rocket_3 . ","
				. "cargo_dropped=" . $cargo_dropped . ","
				. "hab_climb_level=" . $hab_climb_level . ","
				. "hab_climb_level_attempted=" . $hab_climb_level_attempted . ","
				. "hab_climb_2_left=" . $hab_climb_2_left . ","
				. "floor_pickup_cargo=" . $floor_pickup_cargo . ","
				. "floor_pickup_hatch=" . $floor_pickup_hatch . ","
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
			$result = mysql_query("SELECT id, timestamp FROM fact_match_data_2019 WHERE event_id=" . $event_id . " AND match_id=" . $match_id . " AND team_id=" . $team_id . " AND practice_match=" . $practice_match);
			$row = mysql_fetch_array($result);
			$resp = $row["id"] . "," . strtotime($row["timestamp"]);
		} else {
			$resp = 'Database Query Failed : \n' . $query;
		}
	}
	else if ($_POST['type'] == 'pits') {
		$team_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['team_id']) ? $_POST['team_id'] : '0')));
		$start_hab_level = mysql_real_escape_string(stripslashes(trim(isset($_POST['start_hab_level']) ? $_POST['start_hab_level'] : '0')));
		$preload_cargo = mysql_real_escape_string(stripslashes(trim(isset($_POST['preload_cargo']) ? $_POST['preload_cargo'] : '0')));
		$preload_hatch = mysql_real_escape_string(stripslashes(trim(isset($_POST['preload_hatch']) ? $_POST['preload_hatch'] : '0')));
		$sandstorm_bonus = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_bonus']) ? $_POST['sandstorm_bonus'] : '0')));
		$sandstorm_hatch_ship_front = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_hatch_ship_front']) ? $_POST['sandstorm_hatch_ship_front'] : '0')));
		$sandstorm_hatch_ship_side = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_hatch_ship_side']) ? $_POST['sandstorm_hatch_ship_side'] : '0')));
		$sandstorm_hatch_rocket_1 = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_hatch_rocket_1']) ? $_POST['sandstorm_hatch_rocket_1'] : '0')));
		$sandstorm_hatch_rocket_2 = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_hatch_rocket_2']) ? $_POST['sandstorm_hatch_rocket_2'] : '0')));
		$sandstorm_hatch_rocket_3 = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_hatch_rocket_3']) ? $_POST['sandstorm_hatch_rocket_3'] : '0')));
		$sandstorm_cargo_ship = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_cargo_ship']) ? $_POST['sandstorm_cargo_ship'] : '0')));
		$sandstorm_cargo_rocket_1 = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_cargo_rocket_1']) ? $_POST['sandstorm_cargo_rocket_1'] : '0')));
		$sandstorm_cargo_rocket_2 = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_cargo_rocket_2']) ? $_POST['sandstorm_cargo_rocket_2'] : '0')));
		$sandstorm_cargo_rocket_3 = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_cargo_rocket_3']) ? $_POST['sandstorm_cargo_rocket_3'] : '0')));
		$sandstorm_hatch_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_hatch_count']) ? $_POST['sandstorm_hatch_count'] : '0')));
		$sandstorm_cargo_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['sandstorm_cargo_count']) ? $_POST['sandstorm_cargo_count'] : '0')));
		$hatch_1 = mysql_real_escape_string(stripslashes(trim(isset($_POST['hatch_1']) ? $_POST['hatch_1'] : '0')));
		$hatch_2 = mysql_real_escape_string(stripslashes(trim(isset($_POST['hatch_2']) ? $_POST['hatch_2'] : '0')));
		$hatch_3 = mysql_real_escape_string(stripslashes(trim(isset($_POST['hatch_3']) ? $_POST['hatch_3'] : '0')));
		$cargo_1 = mysql_real_escape_string(stripslashes(trim(isset($_POST['cargo_1']) ? $_POST['cargo_1'] : '0')));
		$cargo_2 = mysql_real_escape_string(stripslashes(trim(isset($_POST['cargo_2']) ? $_POST['cargo_2'] : '0')));
		$cargo_3 = mysql_real_escape_string(stripslashes(trim(isset($_POST['cargo_3']) ? $_POST['cargo_3'] : '0')));
		$hab_climb_2 = mysql_real_escape_string(stripslashes(trim(isset($_POST['hab_climb_2']) ? $_POST['hab_climb_2'] : '0')));
		$hab_climb_3 = mysql_real_escape_string(stripslashes(trim(isset($_POST['hab_climb_3']) ? $_POST['hab_climb_3'] : '0')));
		$hab_climb_speed_lvl_2_sec = mysql_real_escape_string(stripslashes(trim(isset($_POST['hab_climb_speed_lvl_2_sec']) ? $_POST['hab_climb_speed_lvl_2_sec'] : '0')));
		$hab_climb_speed_lvl_3_sec = mysql_real_escape_string(stripslashes(trim(isset($_POST['hab_climb_speed_lvl_3_sec']) ? $_POST['hab_climb_speed_lvl_3_sec'] : '0')));
		$floor_pickup_hatch = mysql_real_escape_string(stripslashes(trim(isset($_POST['floor_pickup_hatch']) ? $_POST['floor_pickup_hatch'] : '0')));
		$floor_pickup_cargo = mysql_real_escape_string(stripslashes(trim(isset($_POST['floor_pickup_cargo']) ? $_POST['floor_pickup_cargo'] : '0')));
		$max_robot_speed_fts = mysql_real_escape_string(stripslashes(trim(isset($_POST['max_robot_speed_fts']) ? $_POST['max_robot_speed_fts'] : '0')));
		$robot_gross_weight_lbs = mysql_real_escape_string(stripslashes(trim(isset($_POST['robot_gross_weight_lbs']) ? $_POST['robot_gross_weight_lbs'] : '0')));
		$config_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['config_id']) ? $_POST['config_id'] : '0')));
		$wheel_base_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['wheel_base_id']) ? $_POST['wheel_base_id'] : '0')));
		$wheel_type_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['wheel_type_id']) ? $_POST['wheel_type_id'] : '0')));
		$notes = mysql_real_escape_string(stripslashes(trim(isset($_POST['notes']) ? $_POST['notes'] : '0')));

		$result = mysql_query("SELECT id FROM scout_pit_data_2019 WHERE team_id=" . $team_id);

		$row = mysql_fetch_array($result);
		$match_row_id = $row["id"];

		if (mysql_num_rows($result) == 0) {

			$query = "INSERT INTO scout_pit_data_2019(team_id,start_hab_level,preload_cargo,preload_hatch,sandstorm_bonus,sandstorm_hatch_ship_front,sandstorm_hatch_ship_side,sandstorm_hatch_rocket_1,sandstorm_hatch_rocket_2,sandstorm_hatch_rocket_3,sandstorm_cargo_ship,sandstorm_cargo_rocket_1,sandstorm_cargo_rocket_2,sandstorm_cargo_rocket_3,sandstorm_hatch_count,sandstorm_cargo_count,hatch_1,hatch_2,hatch_3,cargo_1,cargo_2,cargo_3,hab_climb_2,hab_climb_3,hab_climb_speed_lvl_2_sec,hab_climb_speed_lvl_3_sec,floor_pickup_hatch,floor_pickup_cargo,max_robot_speed_fts,robot_gross_weight_lbs,config_id,wheel_base_id,wheel_type_id,notes,invalid) VALUES("
				. $team_id . ","
				. $start_hab_level . ","
				. $preload_cargo . ","
				. $preload_hatch . ","
				. $sandstorm_bonus . ","
				. $sandstorm_hatch_ship_front . ","
				. $sandstorm_hatch_ship_side . ","
				. $sandstorm_hatch_rocket_1 . ","
				. $sandstorm_hatch_rocket_2 . ","
				. $sandstorm_hatch_rocket_3 . ","
				. $sandstorm_cargo_ship . ","
				. $sandstorm_cargo_rocket_1 . ","
				. $sandstorm_cargo_rocket_2 . ","
				. $sandstorm_cargo_rocket_3 . ","
				. $sandstorm_hatch_count . ","
				. $sandstorm_cargo_count . ","
				. $hatch_1 . ","
				. $hatch_2 . ","
				. $hatch_3 . ","
				. $cargo_1 . ","
				. $cargo_2 . ","
				. $cargo_3 . ","
				. $hab_climb_2 . ","
				. $hab_climb_3 . ","
				. $hab_climb_speed_lvl_2_sec . ","
				. $hab_climb_speed_lvl_3_sec . ","
				. $floor_pickup_hatch . ","
				. $floor_pickup_cargo . ","
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
			$query = "UPDATE scout_pit_data_2019 SET "
				. "team_id=" . $team_id . ","
				. "start_hab_level=" . $start_hab_level . ","
				. "preload_cargo=" . $preload_cargo . ","
				. "preload_hatch=" . $preload_hatch . ","
				. "sandstorm_bonus=" . $sandstorm_bonus . ","
				. "sandstorm_hatch_ship_front=" . $sandstorm_hatch_ship_front . ","
				. "sandstorm_hatch_ship_side=" . $sandstorm_hatch_ship_side . ","
				. "sandstorm_hatch_rocket_1=" . $sandstorm_hatch_rocket_1 . ","
				. "sandstorm_hatch_rocket_2=" . $sandstorm_hatch_rocket_2 . ","
				. "sandstorm_hatch_rocket_3=" . $sandstorm_hatch_rocket_3 . ","
				. "sandstorm_cargo_ship=" . $sandstorm_cargo_ship . ","
				. "sandstorm_cargo_rocket_1=" . $sandstorm_cargo_rocket_1 . ","
				. "sandstorm_cargo_rocket_2=" . $sandstorm_cargo_rocket_2 . ","
				. "sandstorm_cargo_rocket_3=" . $sandstorm_cargo_rocket_3 . ","
				. "sandstorm_hatch_count=" . $sandstorm_hatch_count . ","
				. "sandstorm_cargo_count=" . $sandstorm_cargo_count . ","
				. "hatch_1=" . $hatch_1 . ","
				. "hatch_2=" . $hatch_2 . ","
				. "hatch_3=" . $hatch_3 . ","
				. "cargo_1=" . $cargo_1 . ","
				. "cargo_2=" . $cargo_2 . ","
				. "cargo_3=" . $cargo_3 . ","
				. "hab_climb_2=" . $hab_climb_2 . ","
				. "hab_climb_3=" . $hab_climb_3 . ","
				. "hab_climb_speed_lvl_2_sec=" . $hab_climb_speed_lvl_2_sec . ","
				. "hab_climb_speed_lvl_3_sec=" . $hab_climb_speed_lvl_3_sec . ","
				. "floor_pickup_hatch=" . $floor_pickup_hatch . ","
				. "floor_pickup_cargo=" . $floor_pickup_cargo . ","
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
			$result = mysql_query("SELECT id, timestamp FROM scout_pit_data_2019 WHERE team_id=" . $team_id);
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
