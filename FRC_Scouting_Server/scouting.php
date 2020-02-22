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
	while($row = mysqli_fetch_array($sql_result, 1)) {

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
			$col_name = mysqli_field_name($sql_result, $i);
			$col_type = mysqli_fetch_field($sql_result, $i);


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

		//event_lu
		$query = "SELECT * FROM event_lu" . $suffix;
		$result = mysqli_query($link,$query);
		$json .= genJSON($result, "event_lu") . ",";
		mysqli_free_result($result);

		//fact_match_data_2020
		$query = "SELECT * FROM fact_match_data_2020" . $suffix;
		$result = mysqli_query($link,$query);
		$json .= genJSON($result, "fact_match_data_2020") . ",";
		mysqli_free_result($result);

		//game_info
		$query = "SELECT * FROM game_info" . $suffix;
		$result = mysqli_query($link,$query);
		$json .= genJSON($result, "game_info") . ",";
		mysqli_free_result($result);

		//notes_options
		$query = "SELECT * FROM notes_options" . $suffix;
		$result = mysqli_query($link,$query);
		$json .= genJSON($result, "notes_options") . ",";
		mysqli_free_result($result);

		//picklist
		$query = "SELECT * FROM picklist" . $suffix;
		$result = mysqli_query($link,$query);
		$json .= genJSON($result, "picklist") . ",";
		mysqli_free_result($result);

		//programming_lu
		$query = "SELECT * FROM programming_lu" . $suffix;
		$result = mysqli_query($link,$query);
		$json .= genJSON($result, "programming_lu") . ",";
		mysqli_free_result($result);

		//position_lu
		$query = "SELECT * FROM position_lu" . $suffix;
		$result = mysqli_query($link,$query);
		$json .= genJSON($result, "position_lu") . ",";
		mysqli_free_result($result);

		//robot_lu
		$query = "SELECT * FROM robot_lu" . $suffix;
		$result = mysqli_query($link,$query);
		$json .= genJSON($result, "robot_lu") . ",";
		mysqli_free_result($result);

		//scout_pit_data_2020
		$query = "SELECT * FROM scout_pit_data_2020" . $suffix;
		$result = mysqli_query($link,$query);
		$json .= genJSON($result, "scout_pit_data_2020") . "}";
		mysqli_free_result($result);

		$resp = $json;
	}
	else if ($verMatch == false) {
		$resp = 'Version Mismatch, server using version ' . $ver;
	}
	else if ($_POST['type'] == 'match') {
		$event_id = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['event_id']) ? $_POST['event_id'] : '0')));
		$team_id = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['team_id']) ? $_POST['team_id'] : '0')));
		$match_id = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['match_id']) ? $_POST['match_id'] : '0')));
		$practice_match = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['practice_match']) ? $_POST['practice_match'] : '0')));
		$position_id = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['position_id']) ? $_POST['position_id'] : '0')));
		$start_position = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['start_position']) ? $_POST['start_position'] : '0')));
		$auto_initiation_move = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['auto_initiation_move']) ? $_POST['auto_initiation_move'] : '0')));
		$auto_score_low = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['auto_score_low']) ? $_POST['auto_score_low'] : '0')));
		$auto_score_high = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['auto_score_high']) ? $_POST['auto_score_high'] : '0')));
		$auto_miss = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['auto_miss']) ? $_POST['auto_miss'] : '0')));
		$score_low = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['score_low']) ? $_POST['score_low'] : '0')));
		$score_high = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['score_high']) ? $_POST['score_high'] : '0')));
		$miss = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['miss']) ? $_POST['miss'] : '0')));
		$rotation_control = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['rotation_control']) ? $_POST['rotation_control'] : '0')));
		$position_control = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['position_control']) ? $_POST['position_control'] : '0')));
		$generator_park = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['generator_park']) ? $_POST['generator_park'] : '0')));
		$generator_hang = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['generator_hang']) ? $_POST['generator_hang'] : '0')));
		$generator_hang_attempted = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['generator_hang_attempted']) ? $_POST['generator_hang_attempted'] : '0')));
		$generator_level = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['generator_level']) ? $_POST['generator_level'] : '0')));
		$foul = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['foul']) ? $_POST['foul'] : '0')));
		$yellow_card = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['yellow_card']) ? $_POST['yellow_card'] : '0')));
		$red_card = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['red_card']) ? $_POST['red_card'] : '0')));
		$tip_over = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['tip_over']) ? $_POST['tip_over'] : '0')));
		$notes = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['notes']) ? $_POST['notes'] : '0')));

		$result = mysqli_query($link,"SELECT id FROM fact_match_data_2020 WHERE event_id=" . $event_id . " AND match_id=" . $match_id . " AND team_id=" . $team_id . " AND practice_match=" . $practice_match);
		$row = mysqli_fetch_array($result);
		$match_row_id = $row["id"];
		if (mysqli_num_rows($result) == 0) {			$query = "INSERT INTO fact_match_data_2020(event_id,team_id,match_id,practice_match,position_id,start_position,auto_initiation_move,auto_score_low,auto_score_high,auto_miss,score_low,score_high,miss,rotation_control,position_control,generator_park,generator_hang,generator_hang_attempted,generator_level,foul,yellow_card,red_card,tip_over,notes,invalid) VALUES("
				. $event_id . ","
				. $team_id . ","
				. $match_id . ","
				. $practice_match . ","
				. $position_id . ","
				. $start_position . ","
				. $auto_initiation_move . ","
				. $auto_score_low . ","
				. $auto_score_high . ","
				. $auto_miss . ","
				. $score_low . ","
				. $score_high . ","
				. $miss . ","
				. $rotation_control . ","
				. $position_control . ","
				. $generator_park . ","
				. $generator_hang . ","
				. $generator_hang_attempted . ","
				. $generator_level . ","
				. $foul . ","
				. $yellow_card . ","
				. $red_card . ","
				. $tip_over . ","
				. "'" . $notes . "',"
				. "0);";
			$success = mysqli_query($link,$query);
		}
		else {
			$query = "UPDATE fact_match_data_2020 SET "
				. "event_id=" . $event_id . ","
				. "team_id=" . $team_id . ","
				. "match_id=" . $match_id . ","
				. "practice_match=" . $practice_match . ","
				. "position_id=" . $position_id . ","
				. "start_position=" . $start_position . ","
				. "auto_initiation_move=" . $auto_initiation_move . ","
				. "auto_score_low=" . $auto_score_low . ","
				. "auto_score_high=" . $auto_score_high . ","
				. "auto_miss=" . $auto_miss . ","
				. "score_low=" . $score_low . ","
				. "score_high=" . $score_high . ","
				. "miss=" . $miss . ","
				. "rotation_control=" . $rotation_control . ","
				. "position_control=" . $position_control . ","
				. "generator_park=" . $generator_park . ","
				. "generator_hang=" . $generator_hang . ","
				. "generator_hang_attempted=" . $generator_hang_attempted . ","
				. "generator_level=" . $generator_level . ","
				. "foul=" . $foul . ","
				. "yellow_card=" . $yellow_card . ","
				. "red_card=" . $red_card . ","
				. "tip_over=" . $tip_over . ","
				. "notes='" . $notes . "',"
				. "invalid=0"
				. " WHERE id=" . $match_row_id;

			$success = mysqli_query($link,$query);
		}
		if ($success) {
			$result = mysqli_query($link,"SELECT id, timestamp FROM fact_match_data_2020 WHERE event_id=" . $event_id . " AND match_id=" . $match_id . " AND team_id=" . $team_id . " AND practice_match=" . $practice_match);
			$row = mysqli_fetch_array($result);
			$resp = $row["id"] . "," . strtotime($row["timestamp"]);
		} else {
			$resp = 'Database Query Failed : \n' . $query;
		}
	}
	else if ($_POST['type'] == 'pits') {
		$team_id = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['team_id']) ? $_POST['team_id'] : '0')));
		$auto_move = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['auto_move']) ? $_POST['auto_move'] : '0')));
		$auto_score_low = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['auto_score_low']) ? $_POST['auto_score_low'] : '0')));
		$auto_score_outer = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['auto_score_outer']) ? $_POST['auto_score_outer'] : '0')));
		$auto_score_inner = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['auto_score_inner']) ? $_POST['auto_score_inner'] : '0')));
		$score_low = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['score_low']) ? $_POST['score_low'] : '0')));
		$score_outer = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['score_outer']) ? $_POST['score_outer'] : '0')));
		$score_inner = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['score_inner']) ? $_POST['score_inner'] : '0')));
		$position_control = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['position_control']) ? $_POST['position_control'] : '0')));
		$rotation_control = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['rotation_control']) ? $_POST['rotation_control'] : '0')));
		$generator_hang = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['generator_hang']) ? $_POST['generator_hang'] : '0')));
		$power_cell_capacity = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['power_cell_capacity']) ? $_POST['power_cell_capacity'] : '0')));
		$trench_run = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['trench_run']) ? $_POST['trench_run'] : '0')));
		$traction_wheels = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['traction_wheels']) ? $_POST['traction_wheels'] : '0')));
		$pneumatic_wheels = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['pneumatic_wheels']) ? $_POST['pneumatic_wheels'] : '0')));
		$omni_wheels = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['omni_wheels']) ? $_POST['omni_wheels'] : '0')));
		$mecanum_wheels = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['mecanum_wheels']) ? $_POST['mecanum_wheels'] : '0')));
		$swerve_drive = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['swerve_drive']) ? $_POST['swerve_drive'] : '0')));
		$tank_drive = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['tank_drive']) ? $_POST['tank_drive'] : '0')));
		$other_drive_wheels = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['other_drive_wheels']) ? $_POST['other_drive_wheels'] : '0')));
		$team_batteries = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['team_batteries']) ? $_POST['team_batteries'] : '0')));
		$team_battery_chargers = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['team_battery_chargers']) ? $_POST['team_battery_chargers'] : '0')));
		$robot_gross_weight_lbs = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['robot_gross_weight_lbs']) ? $_POST['robot_gross_weight_lbs'] : '0')));
		$programming_id = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['programming_id']) ? $_POST['programming_id'] : '0')));
		$mechanical_appearance = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['mechanical_appearance']) ? $_POST['mechanical_appearance'] : '0')));
		$electrical_appearance = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['electrical_appearance']) ? $_POST['electrical_appearance'] : '0')));
		$notes = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['notes']) ? $_POST['notes'] : '0')));

		$result = mysqli_query($link,"SELECT id FROM scout_pit_data_2020 WHERE team_id=" . $team_id);
		$row = mysqli_fetch_array($result);
		$match_row_id = $row["id"];
		if (mysqli_num_rows($result) == 0) {			$query = "INSERT INTO scout_pit_data_2020(team_id,auto_move,auto_score_low,auto_score_outer,auto_score_inner,score_low,score_outer,score_inner,position_control,rotation_control,generator_hang,power_cell_capacity,trench_run,traction_wheels,pneumatic_wheels,omni_wheels,mecanum_wheels,swerve_drive,tank_drive,other_drive_wheels,team_batteries,team_battery_chargers,robot_gross_weight_lbs,programming_id,mechanical_appearance,electrical_appearance,notes,invalid) VALUES("
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
				. $power_cell_capacity . ","
				. $trench_run . ","
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
			$success = mysqli_query($link,$query);
		}
		else {
			$query = "UPDATE scout_pit_data_2020 SET "
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
				. "power_cell_capacity=" . $power_cell_capacity . ","
				. "trench_run=" . $trench_run . ","
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

			$success = mysqli_query($link,$query);
		}
		if ($success) {
			$result = mysqli_query($link,"SELECT id, timestamp FROM scout_pit_data_2020 WHERE team_id=" . $team_id);
			$row = mysqli_fetch_array($result);
			$resp = $row["id"] . "," . strtotime($row["timestamp"]);
		} else {
			$resp = 'Database Query Failed : \n' . $query;
		}
	}
	else if ($_POST['type'] == 'picklist') {
		$event_id = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['event_id']) ? $_POST['event_id'] : '0')));
		$team_id = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['team_id']) ? $_POST['team_id'] : '0')));
		$sort = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['sort']) ? $_POST['sort'] : '0')));
		$picked = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['picked']) ? $_POST['picked'] : '0')));
		$removed = mysqli_real_escape_string($link,stripslashes(trim(isset($_POST['removed']) ? $_POST['removed'] : '0')));

		$result = mysqli_query($link,"SELECT id FROM picklist WHERE event_id=" . $event_id . " AND team_id=" . $team_id);
		$row = mysqli_fetch_array($result);
		$match_row_id = $row["id"];
		if (mysqli_num_rows($result) == 0) {			$query = "INSERT INTO picklist(event_id,team_id,sort,picked,removed,invalid) VALUES("
				. $event_id . ","
				. $team_id . ","
				. $sort . ","
				. $picked . ","
				. $removed . ","
				. "0);";
			$success = mysqli_query($link,$query);
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

			$success = mysqli_query($link,$query);
		}
		if ($success) {
			$result = mysqli_query($link,"SELECT id, timestamp FROM picklist WHERE event_id=" . $event_id . " AND team_id=" . $team_id);
			$row = mysqli_fetch_array($result);
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
