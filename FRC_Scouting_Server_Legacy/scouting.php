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

		//{0}
		$query = "SELECT * FROM {0}" . $suffix;		$result = mysql_query($query);
		$json .= genJSON($result, "configuration_lu") . ",";
		mysql_free_result($result);

		//{0}
		$query = "SELECT * FROM {0}" . $suffix;		$result = mysql_query($query);
		$json .= genJSON($result, "event_lu") . ",";
		mysql_free_result($result);

		//{0}
		$query = "SELECT * FROM {0}" . $suffix;		$result = mysql_query($query);
		$json .= genJSON($result, "fact_match_data_2019") . ",";
		mysql_free_result($result);

		//{0}
		$query = "SELECT * FROM {0}" . $suffix;		$result = mysql_query($query);
		$json .= genJSON($result, "game_info") . ",";
		mysql_free_result($result);

		//{0}
		$query = "SELECT * FROM {0}" . $suffix;		$result = mysql_query($query);
		$json .= genJSON($result, "notes_options") . ",";
		mysql_free_result($result);

		//{0}
		$query = "SELECT * FROM {0}" . $suffix;		$result = mysql_query($query);
		$json .= genJSON($result, "picklist") . ",";
		mysql_free_result($result);

		//{0}
		$query = "SELECT * FROM {0}" . $suffix;		$result = mysql_query($query);
		$json .= genJSON($result, "position_lu") . ",";
		mysql_free_result($result);

		//{0}
		$query = "SELECT * FROM {0}" . $suffix;		$result = mysql_query($query);
		$json .= genJSON($result, "robot_lu") . ",";
		mysql_free_result($result);

		//{0}
		$query = "SELECT * FROM {0}" . $suffix;		$result = mysql_query($query);
		$json .= genJSON($result, "scout_pit_data_2019") . ",";
		mysql_free_result($result);

		//{0}
		$query = "SELECT * FROM {0}" . $suffix;		$result = mysql_query($query);
		$json .= genJSON($result, "wheel_base_lu") . ",";
		mysql_free_result($result);

		//{0}
		$query = "SELECT * FROM {0}" . $suffix;		$result = mysql_query($query);
		$json .= genJSON($result, "wheel_type_lu") . "}";
		mysql_free_result($result);

		$resp = $json;
	}
	else if ($verMatch == false) {
		$resp = 'Version Mismatch, server using version ' . $ver;
	}
	else if ($_POST['type'] == 'match') {
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['event_id']) ? $_POST['event_id'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['team_id']) ? $_POST['team_id'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['match_id']) ? $_POST['match_id'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['practice_match']) ? $_POST['practice_match'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['position_id']) ? $_POST['position_id'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_initiation_move']) ? $_POST['auto_initiation_move'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_score_low']) ? $_POST['auto_score_low'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_score_high']) ? $_POST['auto_score_high'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_miss_high']) ? $_POST['auto_miss_high'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['score_low']) ? $_POST['score_low'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['score_high']) ? $_POST['score_high'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['miss_high']) ? $_POST['miss_high'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['rotation_control']) ? $_POST['rotation_control'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['position_control']) ? $_POST['position_control'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['generator_park']) ? $_POST['generator_park'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['generator_hang']) ? $_POST['generator_hang'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['generator_hang_attempted']) ? $_POST['generator_hang_attempted'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['generator_level']) ? $_POST['generator_level'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['inner_port_percentage_guess']) ? $_POST['inner_port_percentage_guess'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['foul']) ? $_POST['foul'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['yellow_card']) ? $_POST['yellow_card'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['red_card']) ? $_POST['red_card'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['tip_over']) ? $_POST['tip_over'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['notes']) ? $_POST['notes'] : '0')));

		$result = mysql_query("SELECT id FROM fact_match_data_2019 WHERE event_id=" . $event_id . " AND match_id=" . $match_id . " AND team_id=" . $team_id . " AND practice_match=" . $practice_match);
		$row = mysql_fetch_array($result);
		$match_row_id = $row["id"];
		if (mysql_num_rows($result) == 0) {			$query = "INSERT INTO fact_match_data_2019(event_id,team_id,match_id,practice_match,position_id,auto_initiation_move,auto_score_low,auto_score_high,auto_miss_high,score_low,score_high,miss_high,rotation_control,position_control,generator_park,generator_hang,generator_hang_attempted,generator_level,inner_port_percentage_guess,foul,yellow_card,red_card,tip_over,notes,invalid) VALUES("
				. $event_id . ","
				. $team_id . ","
				. $match_id . ","
				. $practice_match . ","
				. $position_id . ","
				. $auto_initiation_move . ","
				. $auto_score_low . ","
				. $auto_score_high . ","
				. $auto_miss_high . ","
				. $score_low . ","
				. $score_high . ","
				. $miss_high . ","
				. $rotation_control . ","
				. $position_control . ","
				. $generator_park . ","
				. $generator_hang . ","
				. $generator_hang_attempted . ","
				. $generator_level . ","
				. $inner_port_percentage_guess . ","
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
				. "auto_initiation_move=" . $auto_initiation_move . ","
				. "auto_score_low=" . $auto_score_low . ","
				. "auto_score_high=" . $auto_score_high . ","
				. "auto_miss_high=" . $auto_miss_high . ","
				. "score_low=" . $score_low . ","
				. "score_high=" . $score_high . ","
				. "miss_high=" . $miss_high . ","
				. "rotation_control=" . $rotation_control . ","
				. "position_control=" . $position_control . ","
				. "generator_park=" . $generator_park . ","
				. "generator_hang=" . $generator_hang . ","
				. "generator_hang_attempted=" . $generator_hang_attempted . ","
				. "generator_level=" . $generator_level . ","
				. "inner_port_percentage_guess=" . $inner_port_percentage_guess . ","
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
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['team_id']) ? $_POST['team_id'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_move']) ? $_POST['auto_move'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_score_low']) ? $_POST['auto_score_low'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_score_outer']) ? $_POST['auto_score_outer'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_score_inner']) ? $_POST['auto_score_inner'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['score_low']) ? $_POST['score_low'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['score_outer']) ? $_POST['score_outer'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['score_inner']) ? $_POST['score_inner'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['position_control']) ? $_POST['position_control'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['rotation_control']) ? $_POST['rotation_control'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['generator_hang']) ? $_POST['generator_hang'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['max_robot_speed_fts']) ? $_POST['max_robot_speed_fts'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['robot_gross_weight_lbs']) ? $_POST['robot_gross_weight_lbs'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['config_id']) ? $_POST['config_id'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['wheel_base_id']) ? $_POST['wheel_base_id'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['wheel_type_id']) ? $_POST['wheel_type_id'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['notes']) ? $_POST['notes'] : '0')));

		$result = mysql_query("SELECT id FROM scout_pit_data_2019 WHERE team_id=" . $team_id);
		$row = mysql_fetch_array($result);
		$match_row_id = $row["id"];
		if (mysql_num_rows($result) == 0) {			$query = "INSERT INTO scout_pit_data_2019(team_id,auto_move,auto_score_low,auto_score_outer,auto_score_inner,score_low,score_outer,score_inner,position_control,rotation_control,generator_hang,max_robot_speed_fts,robot_gross_weight_lbs,config_id,wheel_base_id,wheel_type_id,notes,invalid) VALUES("
				. $team_id . ","
				. $auto_move . ","
				. $auto_score_low . ","
				. $auto_score_outer . ","
				. $auto_score_inner . ","
				. $score_low . ","
				. $score_outer . ","
				. $score_inner . ","
				. $position_control . ","
				. $rotation_control . ","
				. $generator_hang . ","
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
				. "auto_move=" . $auto_move . ","
				. "auto_score_low=" . $auto_score_low . ","
				. "auto_score_outer=" . $auto_score_outer . ","
				. "auto_score_inner=" . $auto_score_inner . ","
				. "score_low=" . $score_low . ","
				. "score_outer=" . $score_outer . ","
				. "score_inner=" . $score_inner . ","
				. "position_control=" . $position_control . ","
				. "rotation_control=" . $rotation_control . ","
				. "generator_hang=" . $generator_hang . ","
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
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['event_id']) ? $_POST['event_id'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['team_id']) ? $_POST['team_id'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['sort']) ? $_POST['sort'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['picked']) ? $_POST['picked'] : '0')));
		${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['removed']) ? $_POST['removed'] : '0')));

		$result = mysql_query("SELECT id FROM picklist WHERE event_id=" . $event_id . " AND team_id=" . $team_id);
		$row = mysql_fetch_array($result);
		$match_row_id = $row["id"];
		if (mysql_num_rows($result) == 0) {			$query = "INSERT INTO picklist(event_id,team_id,sort,picked,removed,invalid) VALUES("
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
