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

		//game_info
		$query = "SELECT * FROM game_info" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "game_info") . ",";
		mysql_free_result($result);

		//picklist
		$query = "SELECT * FROM picklist" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "picklist") . ",";
		mysql_free_result($result);

		//event_lu
		$query = "SELECT * FROM event_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "event_lu") . ",";
		mysql_free_result($result);

		//position_lu
		$query = "SELECT * FROM position_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "position_lu") . ",";
		mysql_free_result($result);

		//fact_match_data_2018
		$query = "SELECT * FROM fact_match_data_2018" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "fact_match_data_2018") . ",";
		mysql_free_result($result);

		//wheel_base_lu
		$query = "SELECT * FROM wheel_base_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "wheel_base_lu") . ",";
		mysql_free_result($result);

		//wheel_type_lu
		$query = "SELECT * FROM wheel_type_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "wheel_type_lu") . ",";
		mysql_free_result($result);

		//robot_lu
		$query = "SELECT * FROM robot_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "robot_lu") . ",";
		mysql_free_result($result);

		//scout_pit_data_2018
		$query = "SELECT * FROM scout_pit_data_2018" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "scout_pit_data_2018") . ",";
		mysql_free_result($result);

		//configuration_lu
		$query = "SELECT * FROM configuration_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "configuration_lu") . ",";
		mysql_free_result($result);

		//notes_options
		$query = "SELECT * FROM notes_options" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "notes_options") . "}";
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
		$auto_run = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_run']) ? $_POST['auto_run'] : '0')));
		$auto_switch_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_switch_count']) ? $_POST['auto_switch_count'] : '0')));
		$auto_switch_wrong_side_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_switch_wrong_side_count']) ? $_POST['auto_switch_wrong_side_count'] : '0')));
		$auto_scale_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_scale_count']) ? $_POST['auto_scale_count'] : '0')));
		$auto_scale_wrong_side_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_scale_wrong_side_count']) ? $_POST['auto_scale_wrong_side_count'] : '0')));
		$auto_exchange_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_exchange_count']) ? $_POST['auto_exchange_count'] : '0')));
		$switch_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['switch_count']) ? $_POST['switch_count'] : '0')));
		$switch_wrong_side_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['switch_wrong_side_count']) ? $_POST['switch_wrong_side_count'] : '0')));
		$scale_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['scale_count']) ? $_POST['scale_count'] : '0')));
		$scale_wrong_side_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['scale_wrong_side_count']) ? $_POST['scale_wrong_side_count'] : '0')));
		$opposite_switch_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['opposite_switch_count']) ? $_POST['opposite_switch_count'] : '0')));
		$opposite_switch_wrong_side_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['opposite_switch_wrong_side_count']) ? $_POST['opposite_switch_wrong_side_count'] : '0')));
		$exchange_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['exchange_count']) ? $_POST['exchange_count'] : '0')));
		$parked = mysql_real_escape_string(stripslashes(trim(isset($_POST['parked']) ? $_POST['parked'] : '0')));
		$climbed = mysql_real_escape_string(stripslashes(trim(isset($_POST['climbed']) ? $_POST['climbed'] : '0')));
		$climb_attempt = mysql_real_escape_string(stripslashes(trim(isset($_POST['climb_attempt']) ? $_POST['climb_attempt'] : '0')));
		$supported_others = mysql_real_escape_string(stripslashes(trim(isset($_POST['supported_others']) ? $_POST['supported_others'] : '0')));
		$foul = mysql_real_escape_string(stripslashes(trim(isset($_POST['foul']) ? $_POST['foul'] : '0')));
		$yellow_card = mysql_real_escape_string(stripslashes(trim(isset($_POST['yellow_card']) ? $_POST['yellow_card'] : '0')));
		$red_card = mysql_real_escape_string(stripslashes(trim(isset($_POST['red_card']) ? $_POST['red_card'] : '0')));
		$tip_over = mysql_real_escape_string(stripslashes(trim(isset($_POST['tip_over']) ? $_POST['tip_over'] : '0')));
		$notes = mysql_real_escape_string(stripslashes(trim(isset($_POST['notes']) ? $_POST['notes'] : '0')));

		$result = mysql_query("SELECT id FROM fact_match_data_2018 WHERE event_id=" . $event_id . " AND match_id=" . $match_id . " AND team_id=" . $team_id . " AND practice_match=" . $practice_match);

		$row = mysql_fetch_array($result);
		$match_row_id = $row["id"];

		if (mysql_num_rows($result) == 0) {

			$query = "INSERT INTO fact_match_data_2018(event_id,team_id,match_id,practice_match,position_id,auto_run,auto_switch_count,auto_switch_wrong_side_count,auto_scale_count,auto_scale_wrong_side_count,auto_exchange_count,switch_count,switch_wrong_side_count,scale_count,scale_wrong_side_count,opposite_switch_count,opposite_switch_wrong_side_count,exchange_count,parked,climbed,climb_attempt,supported_others,foul,yellow_card,red_card,tip_over,notes,invalid) VALUES("
				. $event_id . ","
				. $team_id . ","
				. $match_id . ","
				. $practice_match . ","
				. $position_id . ","
				. $auto_run . ","
				. $auto_switch_count . ","
				. $auto_switch_wrong_side_count . ","
				. $auto_scale_count . ","
				. $auto_scale_wrong_side_count . ","
				. $auto_exchange_count . ","
				. $switch_count . ","
				. $switch_wrong_side_count . ","
				. $scale_count . ","
				. $scale_wrong_side_count . ","
				. $opposite_switch_count . ","
				. $opposite_switch_wrong_side_count . ","
				. $exchange_count . ","
				. $parked . ","
				. $climbed . ","
				. $climb_attempt . ","
				. $supported_others . ","
				. $foul . ","
				. $yellow_card . ","
				. $red_card . ","
				. $tip_over . ","
				. "'" . $notes . "',"
				. "0);";
			$success = mysql_query($query);
		}
		else {
			$query = "UPDATE fact_match_data_2018 SET "
				. "event_id=" . $event_id . ","
				. "team_id=" . $team_id . ","
				. "match_id=" . $match_id . ","
				. "practice_match=" . $practice_match . ","
				. "position_id=" . $position_id . ","
				. "auto_run=" . $auto_run . ","
				. "auto_switch_count=" . $auto_switch_count . ","
				. "auto_switch_wrong_side_count=" . $auto_switch_wrong_side_count . ","
				. "auto_scale_count=" . $auto_scale_count . ","
				. "auto_scale_wrong_side_count=" . $auto_scale_wrong_side_count . ","
				. "auto_exchange_count=" . $auto_exchange_count . ","
				. "switch_count=" . $switch_count . ","
				. "switch_wrong_side_count=" . $switch_wrong_side_count . ","
				. "scale_count=" . $scale_count . ","
				. "scale_wrong_side_count=" . $scale_wrong_side_count . ","
				. "opposite_switch_count=" . $opposite_switch_count . ","
				. "opposite_switch_wrong_side_count=" . $opposite_switch_wrong_side_count . ","
				. "exchange_count=" . $exchange_count . ","
				. "parked=" . $parked . ","
				. "climbed=" . $climbed . ","
				. "climb_attempt=" . $climb_attempt . ","
				. "supported_others=" . $supported_others . ","
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
			$result = mysql_query("SELECT id, timestamp FROM fact_match_data_2018 WHERE event_id=" . $event_id . " AND match_id=" . $match_id . " AND team_id=" . $team_id . " AND practice_match=" . $practice_match);
			$row = mysql_fetch_array($result);
			$resp = $row["id"] . "," . strtotime($row["timestamp"]);
		} else {
			$resp = 'Database Query Failed : \n' . $query;
		}
	}
	else if ($_POST['type'] == 'pits') {
		$team_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['team_id']) ? $_POST['team_id'] : '0')));
		$auto_run = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_run']) ? $_POST['auto_run'] : '0')));
		$auto_switch_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_switch_count']) ? $_POST['auto_switch_count'] : '0')));
		$auto_scale_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_scale_count']) ? $_POST['auto_scale_count'] : '0')));
		$switch_score = mysql_real_escape_string(stripslashes(trim(isset($_POST['switch_score']) ? $_POST['switch_score'] : '0')));
		$scale_score = mysql_real_escape_string(stripslashes(trim(isset($_POST['scale_score']) ? $_POST['scale_score'] : '0')));
		$exchange = mysql_real_escape_string(stripslashes(trim(isset($_POST['exchange']) ? $_POST['exchange'] : '0')));
		$climb = mysql_real_escape_string(stripslashes(trim(isset($_POST['climb']) ? $_POST['climb'] : '0')));
		$supports_others = mysql_real_escape_string(stripslashes(trim(isset($_POST['supports_others']) ? $_POST['supports_others'] : '0')));
		$floor_acquire = mysql_real_escape_string(stripslashes(trim(isset($_POST['floor_acquire']) ? $_POST['floor_acquire'] : '0')));
		$exchange_acquire = mysql_real_escape_string(stripslashes(trim(isset($_POST['exchange_acquire']) ? $_POST['exchange_acquire'] : '0')));
		$portal_acquire = mysql_real_escape_string(stripslashes(trim(isset($_POST['portal_acquire']) ? $_POST['portal_acquire'] : '0')));
		$max_robot_speed_fts = mysql_real_escape_string(stripslashes(trim(isset($_POST['max_robot_speed_fts']) ? $_POST['max_robot_speed_fts'] : '0')));
		$robot_gross_weight_lbs = mysql_real_escape_string(stripslashes(trim(isset($_POST['robot_gross_weight_lbs']) ? $_POST['robot_gross_weight_lbs'] : '0')));
		$config_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['config_id']) ? $_POST['config_id'] : '0')));
		$wheel_base_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['wheel_base_id']) ? $_POST['wheel_base_id'] : '0')));
		$wheel_type_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['wheel_type_id']) ? $_POST['wheel_type_id'] : '0')));
		$notes = mysql_real_escape_string(stripslashes(trim(isset($_POST['notes']) ? $_POST['notes'] : '0')));

		$result = mysql_query("SELECT id FROM scout_pit_data_2018 WHERE team_id=" . $team_id);

		$row = mysql_fetch_array($result);
		$match_row_id = $row["id"];

		if (mysql_num_rows($result) == 0) {

			$query = "INSERT INTO scout_pit_data_2018(team_id,auto_run,auto_switch_count,auto_scale_count,switch_score,scale_score,exchange,climb,supports_others,floor_acquire,exchange_acquire,portal_acquire,max_robot_speed_fts,robot_gross_weight_lbs,config_id,wheel_base_id,wheel_type_id,notes,invalid) VALUES("
				. $team_id . ","
				. $auto_run . ","
				. $auto_switch_count . ","
				. $auto_scale_count . ","
				. $switch_score . ","
				. $scale_score . ","
				. $exchange . ","
				. $climb . ","
				. $supports_others . ","
				. $floor_acquire . ","
				. $exchange_acquire . ","
				. $portal_acquire . ","
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
			$query = "UPDATE scout_pit_data_2018 SET "
				. "team_id=" . $team_id . ","
				. "auto_run=" . $auto_run . ","
				. "auto_switch_count=" . $auto_switch_count . ","
				. "auto_scale_count=" . $auto_scale_count . ","
				. "switch_score=" . $switch_score . ","
				. "scale_score=" . $scale_score . ","
				. "exchange=" . $exchange . ","
				. "climb=" . $climb . ","
				. "supports_others=" . $supports_others . ","
				. "floor_acquire=" . $floor_acquire . ","
				. "exchange_acquire=" . $exchange_acquire . ","
				. "portal_acquire=" . $portal_acquire . ","
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
			$result = mysql_query("SELECT id, timestamp FROM scout_pit_data_2018 WHERE team_id=" . $team_id);
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
