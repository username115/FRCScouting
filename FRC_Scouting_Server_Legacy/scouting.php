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

		//event_lu
		$query = "SELECT * FROM event_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "event_lu") . ",";
		mysql_free_result($result);

		//fact_match_data_2023
		$query = "SELECT * FROM fact_match_data_2023" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "fact_match_data_2023") . ",";
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

		//position_lu
		$query = "SELECT * FROM position_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "position_lu") . ",";
		mysql_free_result($result);

		//programming_lu
		$query = "SELECT * FROM programming_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "programming_lu") . ",";
		mysql_free_result($result);

		//robot_lu
		$query = "SELECT * FROM robot_lu" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "robot_lu") . ",";
		mysql_free_result($result);

		//scout_pit_data_2023
		$query = "SELECT * FROM scout_pit_data_2023" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "scout_pit_data_2023") . "}";
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
		$auto_mobility = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_mobility']) ? $_POST['auto_mobility'] : '0')));
		$auto_substn_grid_top_substn = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_substn_grid_top_substn']) ? $_POST['auto_substn_grid_top_substn'] : '0')));
		$auto_substn_grid_top_mid = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_substn_grid_top_mid']) ? $_POST['auto_substn_grid_top_mid'] : '0')));
		$auto_substn_grid_top_wall = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_substn_grid_top_wall']) ? $_POST['auto_substn_grid_top_wall'] : '0')));
		$auto_substn_grid_mid_substn = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_substn_grid_mid_substn']) ? $_POST['auto_substn_grid_mid_substn'] : '0')));
		$auto_substn_grid_mid_mid = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_substn_grid_mid_mid']) ? $_POST['auto_substn_grid_mid_mid'] : '0')));
		$auto_substn_grid_mid_wall = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_substn_grid_mid_wall']) ? $_POST['auto_substn_grid_mid_wall'] : '0')));
		$auto_substn_grid_hyb_substn = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_substn_grid_hyb_substn']) ? $_POST['auto_substn_grid_hyb_substn'] : '0')));
		$auto_substn_grid_hyb_mid = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_substn_grid_hyb_mid']) ? $_POST['auto_substn_grid_hyb_mid'] : '0')));
		$auto_substn_grid_hyb_wall = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_substn_grid_hyb_wall']) ? $_POST['auto_substn_grid_hyb_wall'] : '0')));
		$auto_coop_grid_top_substn = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_coop_grid_top_substn']) ? $_POST['auto_coop_grid_top_substn'] : '0')));
		$auto_coop_grid_top_mid = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_coop_grid_top_mid']) ? $_POST['auto_coop_grid_top_mid'] : '0')));
		$auto_coop_grid_top_wall = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_coop_grid_top_wall']) ? $_POST['auto_coop_grid_top_wall'] : '0')));
		$auto_coop_grid_mid_substn = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_coop_grid_mid_substn']) ? $_POST['auto_coop_grid_mid_substn'] : '0')));
		$auto_coop_grid_mid_mid = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_coop_grid_mid_mid']) ? $_POST['auto_coop_grid_mid_mid'] : '0')));
		$auto_coop_grid_mid_wall = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_coop_grid_mid_wall']) ? $_POST['auto_coop_grid_mid_wall'] : '0')));
		$auto_coop_grid_hyb_substn = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_coop_grid_hyb_substn']) ? $_POST['auto_coop_grid_hyb_substn'] : '0')));
		$auto_coop_grid_hyb_mid = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_coop_grid_hyb_mid']) ? $_POST['auto_coop_grid_hyb_mid'] : '0')));
		$auto_coop_grid_hyb_wall = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_coop_grid_hyb_wall']) ? $_POST['auto_coop_grid_hyb_wall'] : '0')));
		$auto_wall_grid_top_substn = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_wall_grid_top_substn']) ? $_POST['auto_wall_grid_top_substn'] : '0')));
		$auto_wall_grid_top_mid = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_wall_grid_top_mid']) ? $_POST['auto_wall_grid_top_mid'] : '0')));
		$auto_wall_grid_top_wall = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_wall_grid_top_wall']) ? $_POST['auto_wall_grid_top_wall'] : '0')));
		$auto_wall_grid_mid_substn = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_wall_grid_mid_substn']) ? $_POST['auto_wall_grid_mid_substn'] : '0')));
		$auto_wall_grid_mid_mid = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_wall_grid_mid_mid']) ? $_POST['auto_wall_grid_mid_mid'] : '0')));
		$auto_wall_grid_mid_wall = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_wall_grid_mid_wall']) ? $_POST['auto_wall_grid_mid_wall'] : '0')));
		$auto_wall_grid_hyb_substn = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_wall_grid_hyb_substn']) ? $_POST['auto_wall_grid_hyb_substn'] : '0')));
		$auto_wall_grid_hyb_mid = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_wall_grid_hyb_mid']) ? $_POST['auto_wall_grid_hyb_mid'] : '0')));
		$auto_wall_grid_hyb_wall = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_wall_grid_hyb_wall']) ? $_POST['auto_wall_grid_hyb_wall'] : '0')));
		$auto_charge_station = mysql_real_escape_string(stripslashes(trim(isset($_POST['auto_charge_station']) ? $_POST['auto_charge_station'] : '0')));
		$substn_grid_top_substn = mysql_real_escape_string(stripslashes(trim(isset($_POST['substn_grid_top_substn']) ? $_POST['substn_grid_top_substn'] : '0')));
		$substn_grid_top_mid = mysql_real_escape_string(stripslashes(trim(isset($_POST['substn_grid_top_mid']) ? $_POST['substn_grid_top_mid'] : '0')));
		$substn_grid_top_wall = mysql_real_escape_string(stripslashes(trim(isset($_POST['substn_grid_top_wall']) ? $_POST['substn_grid_top_wall'] : '0')));
		$substn_grid_mid_substn = mysql_real_escape_string(stripslashes(trim(isset($_POST['substn_grid_mid_substn']) ? $_POST['substn_grid_mid_substn'] : '0')));
		$substn_grid_mid_mid = mysql_real_escape_string(stripslashes(trim(isset($_POST['substn_grid_mid_mid']) ? $_POST['substn_grid_mid_mid'] : '0')));
		$substn_grid_mid_wall = mysql_real_escape_string(stripslashes(trim(isset($_POST['substn_grid_mid_wall']) ? $_POST['substn_grid_mid_wall'] : '0')));
		$substn_grid_hyb_substn = mysql_real_escape_string(stripslashes(trim(isset($_POST['substn_grid_hyb_substn']) ? $_POST['substn_grid_hyb_substn'] : '0')));
		$substn_grid_hyb_mid = mysql_real_escape_string(stripslashes(trim(isset($_POST['substn_grid_hyb_mid']) ? $_POST['substn_grid_hyb_mid'] : '0')));
		$substn_grid_hyb_wall = mysql_real_escape_string(stripslashes(trim(isset($_POST['substn_grid_hyb_wall']) ? $_POST['substn_grid_hyb_wall'] : '0')));
		$coop_grid_top_substn = mysql_real_escape_string(stripslashes(trim(isset($_POST['coop_grid_top_substn']) ? $_POST['coop_grid_top_substn'] : '0')));
		$coop_grid_mid_substn = mysql_real_escape_string(stripslashes(trim(isset($_POST['coop_grid_mid_substn']) ? $_POST['coop_grid_mid_substn'] : '0')));
		$coop_grid_mid_mid = mysql_real_escape_string(stripslashes(trim(isset($_POST['coop_grid_mid_mid']) ? $_POST['coop_grid_mid_mid'] : '0')));
		$coop_grid_mid_wall = mysql_real_escape_string(stripslashes(trim(isset($_POST['coop_grid_mid_wall']) ? $_POST['coop_grid_mid_wall'] : '0')));
		$coop_grid_hyb_substn = mysql_real_escape_string(stripslashes(trim(isset($_POST['coop_grid_hyb_substn']) ? $_POST['coop_grid_hyb_substn'] : '0')));
		$coop_grid_hyb_mid = mysql_real_escape_string(stripslashes(trim(isset($_POST['coop_grid_hyb_mid']) ? $_POST['coop_grid_hyb_mid'] : '0')));
		$coop_grid_hyb_wall = mysql_real_escape_string(stripslashes(trim(isset($_POST['coop_grid_hyb_wall']) ? $_POST['coop_grid_hyb_wall'] : '0')));
		$wall_grid_top_substn = mysql_real_escape_string(stripslashes(trim(isset($_POST['wall_grid_top_substn']) ? $_POST['wall_grid_top_substn'] : '0')));
		$wall_grid_top_mid = mysql_real_escape_string(stripslashes(trim(isset($_POST['wall_grid_top_mid']) ? $_POST['wall_grid_top_mid'] : '0')));
		$wall_grid_top_wall = mysql_real_escape_string(stripslashes(trim(isset($_POST['wall_grid_top_wall']) ? $_POST['wall_grid_top_wall'] : '0')));
		$coop_grid_top_mid = mysql_real_escape_string(stripslashes(trim(isset($_POST['coop_grid_top_mid']) ? $_POST['coop_grid_top_mid'] : '0')));
		$coop_grid_top_wall = mysql_real_escape_string(stripslashes(trim(isset($_POST['coop_grid_top_wall']) ? $_POST['coop_grid_top_wall'] : '0')));
		$wall_grid_mid_substn = mysql_real_escape_string(stripslashes(trim(isset($_POST['wall_grid_mid_substn']) ? $_POST['wall_grid_mid_substn'] : '0')));
		$wall_grid_mid_mid = mysql_real_escape_string(stripslashes(trim(isset($_POST['wall_grid_mid_mid']) ? $_POST['wall_grid_mid_mid'] : '0')));
		$wall_grid_mid_wall = mysql_real_escape_string(stripslashes(trim(isset($_POST['wall_grid_mid_wall']) ? $_POST['wall_grid_mid_wall'] : '0')));
		$wall_grid_hyb_substn = mysql_real_escape_string(stripslashes(trim(isset($_POST['wall_grid_hyb_substn']) ? $_POST['wall_grid_hyb_substn'] : '0')));
		$wall_grid_hyb_mid = mysql_real_escape_string(stripslashes(trim(isset($_POST['wall_grid_hyb_mid']) ? $_POST['wall_grid_hyb_mid'] : '0')));
		$wall_grid_hyb_wall = mysql_real_escape_string(stripslashes(trim(isset($_POST['wall_grid_hyb_wall']) ? $_POST['wall_grid_hyb_wall'] : '0')));
		$dropped_gp_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['dropped_gp_count']) ? $_POST['dropped_gp_count'] : '0')));
		$charge_station = mysql_real_escape_string(stripslashes(trim(isset($_POST['charge_station']) ? $_POST['charge_station'] : '0')));
		$feeder = mysql_real_escape_string(stripslashes(trim(isset($_POST['feeder']) ? $_POST['feeder'] : '0')));
		$defense = mysql_real_escape_string(stripslashes(trim(isset($_POST['defense']) ? $_POST['defense'] : '0')));
		$foul_count = mysql_real_escape_string(stripslashes(trim(isset($_POST['foul_count']) ? $_POST['foul_count'] : '0')));
		$yellow_card = mysql_real_escape_string(stripslashes(trim(isset($_POST['yellow_card']) ? $_POST['yellow_card'] : '0')));
		$red_card = mysql_real_escape_string(stripslashes(trim(isset($_POST['red_card']) ? $_POST['red_card'] : '0')));
		$notes = mysql_real_escape_string(stripslashes(trim(isset($_POST['notes']) ? $_POST['notes'] : '0')));

		$result = mysql_query("SELECT id FROM fact_match_data_2023 WHERE event_id=" . $event_id . " AND match_id=" . $match_id . " AND team_id=" . $team_id . " AND practice_match=" . $practice_match);
		$row = mysql_fetch_array($result);
		$match_row_id = $row["id"];
		if (mysql_num_rows($result) == 0) {			$query = "INSERT INTO fact_match_data_2023(event_id,team_id,match_id,practice_match,position_id,auto_mobility,auto_substn_grid_top_substn,auto_substn_grid_top_mid,auto_substn_grid_top_wall,auto_substn_grid_mid_substn,auto_substn_grid_mid_mid,auto_substn_grid_mid_wall,auto_substn_grid_hyb_substn,auto_substn_grid_hyb_mid,auto_substn_grid_hyb_wall,auto_coop_grid_top_substn,auto_coop_grid_top_mid,auto_coop_grid_top_wall,auto_coop_grid_mid_substn,auto_coop_grid_mid_mid,auto_coop_grid_mid_wall,auto_coop_grid_hyb_substn,auto_coop_grid_hyb_mid,auto_coop_grid_hyb_wall,auto_wall_grid_top_substn,auto_wall_grid_top_mid,auto_wall_grid_top_wall,auto_wall_grid_mid_substn,auto_wall_grid_mid_mid,auto_wall_grid_mid_wall,auto_wall_grid_hyb_substn,auto_wall_grid_hyb_mid,auto_wall_grid_hyb_wall,auto_charge_station,substn_grid_top_substn,substn_grid_top_mid,substn_grid_top_wall,substn_grid_mid_substn,substn_grid_mid_mid,substn_grid_mid_wall,substn_grid_hyb_substn,substn_grid_hyb_mid,substn_grid_hyb_wall,coop_grid_top_substn,coop_grid_mid_substn,coop_grid_mid_mid,coop_grid_mid_wall,coop_grid_hyb_substn,coop_grid_hyb_mid,coop_grid_hyb_wall,wall_grid_top_substn,wall_grid_top_mid,wall_grid_top_wall,coop_grid_top_mid,coop_grid_top_wall,wall_grid_mid_substn,wall_grid_mid_mid,wall_grid_mid_wall,wall_grid_hyb_substn,wall_grid_hyb_mid,wall_grid_hyb_wall,dropped_gp_count,charge_station,feeder,defense,foul_count,yellow_card,red_card,notes,invalid) VALUES("
				. $event_id . ","
				. $team_id . ","
				. $match_id . ","
				. $practice_match . ","
				. $position_id . ","
				. $auto_mobility . ","
				. $auto_substn_grid_top_substn . ","
				. $auto_substn_grid_top_mid . ","
				. $auto_substn_grid_top_wall . ","
				. $auto_substn_grid_mid_substn . ","
				. $auto_substn_grid_mid_mid . ","
				. $auto_substn_grid_mid_wall . ","
				. $auto_substn_grid_hyb_substn . ","
				. $auto_substn_grid_hyb_mid . ","
				. $auto_substn_grid_hyb_wall . ","
				. $auto_coop_grid_top_substn . ","
				. $auto_coop_grid_top_mid . ","
				. $auto_coop_grid_top_wall . ","
				. $auto_coop_grid_mid_substn . ","
				. $auto_coop_grid_mid_mid . ","
				. $auto_coop_grid_mid_wall . ","
				. $auto_coop_grid_hyb_substn . ","
				. $auto_coop_grid_hyb_mid . ","
				. $auto_coop_grid_hyb_wall . ","
				. $auto_wall_grid_top_substn . ","
				. $auto_wall_grid_top_mid . ","
				. $auto_wall_grid_top_wall . ","
				. $auto_wall_grid_mid_substn . ","
				. $auto_wall_grid_mid_mid . ","
				. $auto_wall_grid_mid_wall . ","
				. $auto_wall_grid_hyb_substn . ","
				. $auto_wall_grid_hyb_mid . ","
				. $auto_wall_grid_hyb_wall . ","
				. $auto_charge_station . ","
				. $substn_grid_top_substn . ","
				. $substn_grid_top_mid . ","
				. $substn_grid_top_wall . ","
				. $substn_grid_mid_substn . ","
				. $substn_grid_mid_mid . ","
				. $substn_grid_mid_wall . ","
				. $substn_grid_hyb_substn . ","
				. $substn_grid_hyb_mid . ","
				. $substn_grid_hyb_wall . ","
				. $coop_grid_top_substn . ","
				. $coop_grid_mid_substn . ","
				. $coop_grid_mid_mid . ","
				. $coop_grid_mid_wall . ","
				. $coop_grid_hyb_substn . ","
				. $coop_grid_hyb_mid . ","
				. $coop_grid_hyb_wall . ","
				. $wall_grid_top_substn . ","
				. $wall_grid_top_mid . ","
				. $wall_grid_top_wall . ","
				. $coop_grid_top_mid . ","
				. $coop_grid_top_wall . ","
				. $wall_grid_mid_substn . ","
				. $wall_grid_mid_mid . ","
				. $wall_grid_mid_wall . ","
				. $wall_grid_hyb_substn . ","
				. $wall_grid_hyb_mid . ","
				. $wall_grid_hyb_wall . ","
				. $dropped_gp_count . ","
				. $charge_station . ","
				. $feeder . ","
				. $defense . ","
				. $foul_count . ","
				. $yellow_card . ","
				. $red_card . ","
				. "'" . $notes . "',"
				. "0);";
			$success = mysql_query($query);
		}
		else {
			$query = "UPDATE fact_match_data_2023 SET "
				. "event_id=" . $event_id . ","
				. "team_id=" . $team_id . ","
				. "match_id=" . $match_id . ","
				. "practice_match=" . $practice_match . ","
				. "position_id=" . $position_id . ","
				. "auto_mobility=" . $auto_mobility . ","
				. "auto_substn_grid_top_substn=" . $auto_substn_grid_top_substn . ","
				. "auto_substn_grid_top_mid=" . $auto_substn_grid_top_mid . ","
				. "auto_substn_grid_top_wall=" . $auto_substn_grid_top_wall . ","
				. "auto_substn_grid_mid_substn=" . $auto_substn_grid_mid_substn . ","
				. "auto_substn_grid_mid_mid=" . $auto_substn_grid_mid_mid . ","
				. "auto_substn_grid_mid_wall=" . $auto_substn_grid_mid_wall . ","
				. "auto_substn_grid_hyb_substn=" . $auto_substn_grid_hyb_substn . ","
				. "auto_substn_grid_hyb_mid=" . $auto_substn_grid_hyb_mid . ","
				. "auto_substn_grid_hyb_wall=" . $auto_substn_grid_hyb_wall . ","
				. "auto_coop_grid_top_substn=" . $auto_coop_grid_top_substn . ","
				. "auto_coop_grid_top_mid=" . $auto_coop_grid_top_mid . ","
				. "auto_coop_grid_top_wall=" . $auto_coop_grid_top_wall . ","
				. "auto_coop_grid_mid_substn=" . $auto_coop_grid_mid_substn . ","
				. "auto_coop_grid_mid_mid=" . $auto_coop_grid_mid_mid . ","
				. "auto_coop_grid_mid_wall=" . $auto_coop_grid_mid_wall . ","
				. "auto_coop_grid_hyb_substn=" . $auto_coop_grid_hyb_substn . ","
				. "auto_coop_grid_hyb_mid=" . $auto_coop_grid_hyb_mid . ","
				. "auto_coop_grid_hyb_wall=" . $auto_coop_grid_hyb_wall . ","
				. "auto_wall_grid_top_substn=" . $auto_wall_grid_top_substn . ","
				. "auto_wall_grid_top_mid=" . $auto_wall_grid_top_mid . ","
				. "auto_wall_grid_top_wall=" . $auto_wall_grid_top_wall . ","
				. "auto_wall_grid_mid_substn=" . $auto_wall_grid_mid_substn . ","
				. "auto_wall_grid_mid_mid=" . $auto_wall_grid_mid_mid . ","
				. "auto_wall_grid_mid_wall=" . $auto_wall_grid_mid_wall . ","
				. "auto_wall_grid_hyb_substn=" . $auto_wall_grid_hyb_substn . ","
				. "auto_wall_grid_hyb_mid=" . $auto_wall_grid_hyb_mid . ","
				. "auto_wall_grid_hyb_wall=" . $auto_wall_grid_hyb_wall . ","
				. "auto_charge_station=" . $auto_charge_station . ","
				. "substn_grid_top_substn=" . $substn_grid_top_substn . ","
				. "substn_grid_top_mid=" . $substn_grid_top_mid . ","
				. "substn_grid_top_wall=" . $substn_grid_top_wall . ","
				. "substn_grid_mid_substn=" . $substn_grid_mid_substn . ","
				. "substn_grid_mid_mid=" . $substn_grid_mid_mid . ","
				. "substn_grid_mid_wall=" . $substn_grid_mid_wall . ","
				. "substn_grid_hyb_substn=" . $substn_grid_hyb_substn . ","
				. "substn_grid_hyb_mid=" . $substn_grid_hyb_mid . ","
				. "substn_grid_hyb_wall=" . $substn_grid_hyb_wall . ","
				. "coop_grid_top_substn=" . $coop_grid_top_substn . ","
				. "coop_grid_mid_substn=" . $coop_grid_mid_substn . ","
				. "coop_grid_mid_mid=" . $coop_grid_mid_mid . ","
				. "coop_grid_mid_wall=" . $coop_grid_mid_wall . ","
				. "coop_grid_hyb_substn=" . $coop_grid_hyb_substn . ","
				. "coop_grid_hyb_mid=" . $coop_grid_hyb_mid . ","
				. "coop_grid_hyb_wall=" . $coop_grid_hyb_wall . ","
				. "wall_grid_top_substn=" . $wall_grid_top_substn . ","
				. "wall_grid_top_mid=" . $wall_grid_top_mid . ","
				. "wall_grid_top_wall=" . $wall_grid_top_wall . ","
				. "coop_grid_top_mid=" . $coop_grid_top_mid . ","
				. "coop_grid_top_wall=" . $coop_grid_top_wall . ","
				. "wall_grid_mid_substn=" . $wall_grid_mid_substn . ","
				. "wall_grid_mid_mid=" . $wall_grid_mid_mid . ","
				. "wall_grid_mid_wall=" . $wall_grid_mid_wall . ","
				. "wall_grid_hyb_substn=" . $wall_grid_hyb_substn . ","
				. "wall_grid_hyb_mid=" . $wall_grid_hyb_mid . ","
				. "wall_grid_hyb_wall=" . $wall_grid_hyb_wall . ","
				. "dropped_gp_count=" . $dropped_gp_count . ","
				. "charge_station=" . $charge_station . ","
				. "feeder=" . $feeder . ","
				. "defense=" . $defense . ","
				. "foul_count=" . $foul_count . ","
				. "yellow_card=" . $yellow_card . ","
				. "red_card=" . $red_card . ","
				. "notes='" . $notes . "',"
				. "invalid=0"
				. " WHERE id=" . $match_row_id;

			$success = mysql_query($query);
		}
		if ($success) {
			$result = mysql_query("SELECT id, timestamp FROM fact_match_data_2023 WHERE event_id=" . $event_id . " AND match_id=" . $match_id . " AND team_id=" . $team_id . " AND practice_match=" . $practice_match);
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
		$robot_gross_weight_lbs = mysql_real_escape_string(stripslashes(trim(isset($_POST['robot_gross_weight_lbs']) ? $_POST['robot_gross_weight_lbs'] : '0')));
		$robot_gross_width_in = mysql_real_escape_string(stripslashes(trim(isset($_POST['robot_gross_width_in']) ? $_POST['robot_gross_width_in'] : '0')));
		$robot_gross_length_in = mysql_real_escape_string(stripslashes(trim(isset($_POST['robot_gross_length_in']) ? $_POST['robot_gross_length_in'] : '0')));
		$notes = mysql_real_escape_string(stripslashes(trim(isset($_POST['notes']) ? $_POST['notes'] : '0')));

		$result = mysql_query("SELECT id FROM scout_pit_data_2023 WHERE team_id=" . $team_id);
		$row = mysql_fetch_array($result);
		$match_row_id = $row["id"];
		if (mysql_num_rows($result) == 0) {			$query = "INSERT INTO scout_pit_data_2023(team_id,traction_wheels,pneumatic_wheels,omni_wheels,mecanum_wheels,swerve_drive,tank_drive,other_drive_wheels,robot_gross_weight_lbs,robot_gross_width_in,robot_gross_length_in,notes,invalid) VALUES("
				. $team_id . ","
				. $traction_wheels . ","
				. $pneumatic_wheels . ","
				. $omni_wheels . ","
				. $mecanum_wheels . ","
				. $swerve_drive . ","
				. $tank_drive . ","
				. $other_drive_wheels . ","
				. $robot_gross_weight_lbs . ","
				. $robot_gross_width_in . ","
				. $robot_gross_length_in . ","
				. "'" . $notes . "',"
				. "0);";
			$success = mysql_query($query);
		}
		else {
			$query = "UPDATE scout_pit_data_2023 SET "
				. "team_id=" . $team_id . ","
				. "traction_wheels=" . $traction_wheels . ","
				. "pneumatic_wheels=" . $pneumatic_wheels . ","
				. "omni_wheels=" . $omni_wheels . ","
				. "mecanum_wheels=" . $mecanum_wheels . ","
				. "swerve_drive=" . $swerve_drive . ","
				. "tank_drive=" . $tank_drive . ","
				. "other_drive_wheels=" . $other_drive_wheels . ","
				. "robot_gross_weight_lbs=" . $robot_gross_weight_lbs . ","
				. "robot_gross_width_in=" . $robot_gross_width_in . ","
				. "robot_gross_length_in=" . $robot_gross_length_in . ","
				. "notes='" . $notes . "',"
				. "invalid=0"
				. " WHERE id=" . $match_row_id;

			$success = mysql_query($query);
		}
		if ($success) {
			$result = mysql_query("SELECT id, timestamp FROM scout_pit_data_2023 WHERE team_id=" . $team_id);
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
