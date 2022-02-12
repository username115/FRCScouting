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

		//scout_pit_data_2022
		$query = "SELECT * FROM scout_pit_data_2022" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "scout_pit_data_2022") . ",";
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

		//game_info
		$query = "SELECT * FROM game_info" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "game_info") . ",";
		mysql_free_result($result);

		//programming_lu
		$query = "SELECT * FROM programming_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "programming_lu") . ",";
		mysql_free_result($result);

		//picklist
		$query = "SELECT * FROM picklist" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "picklist") . ",";
		mysql_free_result($result);

		//notes_options
		$query = "SELECT * FROM notes_options" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "notes_options") . ",";
		mysql_free_result($result);

		//robot_lu
		$query = "SELECT * FROM robot_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "robot_lu") . ",";
		mysql_free_result($result);

		//fact_match_data_2022
		$query = "SELECT * FROM fact_match_data_2022" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "fact_match_data_2022") . "}";
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
		$auto_taxi = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_taxi']) ? $_POST['auto_taxi'] : '0')));
		$auto_low_score = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_low_score']) ? $_POST['auto_low_score'] : '0')));
		$auto_low_miss = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_low_miss']) ? $_POST['auto_low_miss'] : '0')));
		$auto_high_score = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_high_score']) ? $_POST['auto_high_score'] : '0')));
		$auto_high_miss = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_high_miss']) ? $_POST['auto_high_miss'] : '0')));
		$low_score = mysql_real_escape_string(stripslashes(trim(isset($_POST['low_score']) ? $_POST['low_score'] : '0')));
		$low_miss = mysql_real_escape_string(stripslashes(trim(isset($_POST['low_miss']) ? $_POST['low_miss'] : '0')));
		$high_score = mysql_real_escape_string(stripslashes(trim(isset($_POST['high_score']) ? $_POST['high_score'] : '0')));
		$high_miss = mysql_real_escape_string(stripslashes(trim(isset($_POST['high_miss']) ? $_POST['high_miss'] : '0')));
		$hang_attempt = mysql_real_escape_string(stripslashes(trim(isset($_POST['hang_attempt']) ? $_POST['hang_attempt'] : '0')));
		$hang_level = mysql_real_escape_string(stripslashes(trim(isset($_POST['hang_level']) ? $_POST['hang_level'] : '0')));
		$ally_tarmac = mysql_real_escape_string(stripslashes(trim(isset($_POST['ally_tarmac']) ? $_POST['ally_tarmac'] : '0')));
		$ally_outfield = mysql_real_escape_string(stripslashes(trim(isset($_POST['ally_outfield']) ? $_POST['ally_outfield'] : '0')));
		$opp_tarmac = mysql_real_escape_string(stripslashes(trim(isset($_POST['opp_tarmac']) ? $_POST['opp_tarmac'] : '0')));
		$opp_outfield = mysql_real_escape_string(stripslashes(trim(isset($_POST['opp_outfield']) ? $_POST['opp_outfield'] : '0')));
		$fender_usage = mysql_real_escape_string(stripslashes(trim(isset($_POST['fender_usage']) ? $_POST['fender_usage'] : '0')));
		$launchpad_usage = mysql_real_escape_string(stripslashes(trim(isset($_POST['launchpad_usage']) ? $_POST['launchpad_usage'] : '0')));
		$time_to_hang_s = mysql_real_escape_string(stripslashes(trim(isset($_POST['time_to_hang_s']) ? $_POST['time_to_hang_s'] : '0')));
		$yellow_card = mysql_real_escape_string(stripslashes(trim(isset($_POST['yellow_card']) ? $_POST['yellow_card'] : '0')));
		$red_card = mysql_real_escape_string(stripslashes(trim(isset($_POST['red_card']) ? $_POST['red_card'] : '0')));
		$notes = mysql_real_escape_string(stripslashes(trim(isset($_POST['notes']) ? $_POST['notes'] : '0')));

		$result = mysql_query("SELECT id FROM fact_match_data_2022 WHERE event_id=" . $event_id . " AND match_id=" . $match_id . " AND team_id=" . $team_id . " AND practice_match=" . $practice_match);
		$row = mysql_fetch_array($result);
		$match_row_id = $row["id"];
		if (mysql_num_rows($result) == 0) {			$query = "INSERT INTO fact_match_data_2022(event_id,team_id,match_id,practice_match,position_id,auto_taxi,auto_low_score,auto_low_miss,auto_high_score,auto_high_miss,low_score,low_miss,high_score,high_miss,hang_attempt,hang_level,ally_tarmac,ally_outfield,opp_tarmac,opp_outfield,fender_usage,launchpad_usage,time_to_hang_s,yellow_card,red_card,notes,invalid) VALUES("
				. $event_id . ","
				. $team_id . ","
				. $match_id . ","
				. $practice_match . ","
				. $position_id . ","
				. $auto_taxi . ","
				. $auto_low_score . ","
				. $auto_low_miss . ","
				. $auto_high_score . ","
				. $auto_high_miss . ","
				. $low_score . ","
				. $low_miss . ","
				. $high_score . ","
				. $high_miss . ","
				. $hang_attempt . ","
				. $hang_level . ","
				. $ally_tarmac . ","
				. $ally_outfield . ","
				. $opp_tarmac . ","
				. $opp_outfield . ","
				. $fender_usage . ","
				. $launchpad_usage . ","
				. $time_to_hang_s . ","
				. $yellow_card . ","
				. $red_card . ","
				. "'" . $notes . "',"
				. "0);";
			$success = mysql_query($query);
		}
		else {
			$query = "UPDATE fact_match_data_2022 SET "
				. "event_id=" . $event_id . ","
				. "team_id=" . $team_id . ","
				. "match_id=" . $match_id . ","
				. "practice_match=" . $practice_match . ","
				. "position_id=" . $position_id . ","
				. "auto_taxi=" . $auto_taxi . ","
				. "auto_low_score=" . $auto_low_score . ","
				. "auto_low_miss=" . $auto_low_miss . ","
				. "auto_high_score=" . $auto_high_score . ","
				. "auto_high_miss=" . $auto_high_miss . ","
				. "low_score=" . $low_score . ","
				. "low_miss=" . $low_miss . ","
				. "high_score=" . $high_score . ","
				. "high_miss=" . $high_miss . ","
				. "hang_attempt=" . $hang_attempt . ","
				. "hang_level=" . $hang_level . ","
				. "ally_tarmac=" . $ally_tarmac . ","
				. "ally_outfield=" . $ally_outfield . ","
				. "opp_tarmac=" . $opp_tarmac . ","
				. "opp_outfield=" . $opp_outfield . ","
				. "fender_usage=" . $fender_usage . ","
				. "launchpad_usage=" . $launchpad_usage . ","
				. "time_to_hang_s=" . $time_to_hang_s . ","
				. "yellow_card=" . $yellow_card . ","
				. "red_card=" . $red_card . ","
				. "notes='" . $notes . "',"
				. "invalid=0"
				. " WHERE id=" . $match_row_id;

			$success = mysql_query($query);
		}
		if ($success) {
			$result = mysql_query("SELECT id, timestamp FROM fact_match_data_2022 WHERE event_id=" . $event_id . " AND match_id=" . $match_id . " AND team_id=" . $team_id . " AND practice_match=" . $practice_match);
			$row = mysql_fetch_array($result);
			$resp = $row["id"] . "," . strtotime($row["timestamp"]);
		} else {
			$resp = 'Database Query Failed : \n' . $query;
		}
	}
	else if ($_POST['type'] == 'pits') {
		$team_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['team_id']) ? $_POST['team_id'] : '0')));
		$traction_wheels = mysql_real_escape_string(stripslashes(trim(isset($_POST['traction_wheels']) ? $_POST['traction_wheels'] : '0')));
		$pneumatic_wheels = mysql_real_escape_string(stripslashes(trim(isset($_POST['pneumatic_wheels']) ? $_POST['pneumatic_wheels'] : '0')));
		$omni_wheels = mysql_real_escape_string(stripslashes(trim(isset($_POST['omni_wheels']) ? $_POST['omni_wheels'] : '0')));
		$mecanum_wheels = mysql_real_escape_string(stripslashes(trim(isset($_POST['mecanum_wheels']) ? $_POST['mecanum_wheels'] : '0')));
		$swerve_drive = mysql_real_escape_string(stripslashes(trim(isset($_POST['swerve_drive']) ? $_POST['swerve_drive'] : '0')));
		$tank_drive = mysql_real_escape_string(stripslashes(trim(isset($_POST['tank_drive']) ? $_POST['tank_drive'] : '0')));
		$other_drive_wheels = mysql_real_escape_string(stripslashes(trim(isset($_POST['other_drive_wheels']) ? $_POST['other_drive_wheels'] : '0')));
		$team_batteries = mysql_real_escape_string(stripslashes(trim(isset($_POST['team_batteries']) ? $_POST['team_batteries'] : '0')));
		$team_battery_chargers = mysql_real_escape_string(stripslashes(trim(isset($_POST['team_battery_chargers']) ? $_POST['team_battery_chargers'] : '0')));
		$robot_gross_weight_lbs = mysql_real_escape_string(stripslashes(trim(isset($_POST['robot_gross_weight_lbs']) ? $_POST['robot_gross_weight_lbs'] : '0')));
		$programming_id = mysql_real_escape_string(stripslashes(trim(isset($_POST['programming_id']) ? $_POST['programming_id'] : '0')));
		$mechanical_appearance = mysql_real_escape_string(stripslashes(trim(isset($_POST['mechanical_appearance']) ? $_POST['mechanical_appearance'] : '0')));
		$electrical_appearance = mysql_real_escape_string(stripslashes(trim(isset($_POST['electrical_appearance']) ? $_POST['electrical_appearance'] : '0')));
		$notes = mysql_real_escape_string(stripslashes(trim(isset($_POST['notes']) ? $_POST['notes'] : '0')));

		$result = mysql_query("SELECT id FROM scout_pit_data_2022 WHERE team_id=" . $team_id);
		$row = mysql_fetch_array($result);
		$match_row_id = $row["id"];
		if (mysql_num_rows($result) == 0) {			$query = "INSERT INTO scout_pit_data_2022(team_id,traction_wheels,pneumatic_wheels,omni_wheels,mecanum_wheels,swerve_drive,tank_drive,other_drive_wheels,team_batteries,team_battery_chargers,robot_gross_weight_lbs,programming_id,mechanical_appearance,electrical_appearance,notes,invalid) VALUES("
				. $team_id . ","
				. $traction_wheels . ","
				. $pneumatic_wheels . ","
				. $omni_wheels . ","
				. $mecanum_wheels . ","
				. $swerve_drive . ","
				. $tank_drive . ","
				. $other_drive_wheels . ","
				. $team_batteries . ","
				. $team_battery_chargers . ","
				. $robot_gross_weight_lbs . ","
				. $programming_id . ","
				. $mechanical_appearance . ","
				. $electrical_appearance . ","
				. "'" . $notes . "',"
				. "0);";
			$success = mysql_query($query);
		}
		else {
			$query = "UPDATE scout_pit_data_2022 SET "
				. "team_id=" . $team_id . ","
				. "traction_wheels=" . $traction_wheels . ","
				. "pneumatic_wheels=" . $pneumatic_wheels . ","
				. "omni_wheels=" . $omni_wheels . ","
				. "mecanum_wheels=" . $mecanum_wheels . ","
				. "swerve_drive=" . $swerve_drive . ","
				. "tank_drive=" . $tank_drive . ","
				. "other_drive_wheels=" . $other_drive_wheels . ","
				. "team_batteries=" . $team_batteries . ","
				. "team_battery_chargers=" . $team_battery_chargers . ","
				. "robot_gross_weight_lbs=" . $robot_gross_weight_lbs . ","
				. "programming_id=" . $programming_id . ","
				. "mechanical_appearance=" . $mechanical_appearance . ","
				. "electrical_appearance=" . $electrical_appearance . ","
				. "notes='" . $notes . "',"
				. "invalid=0"
				. " WHERE id=" . $match_row_id;

			$success = mysql_query($query);
		}
		if ($success) {
			$result = mysql_query("SELECT id, timestamp FROM scout_pit_data_2022 WHERE team_id=" . $team_id);
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
