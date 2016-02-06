<?php
/* 
 * Copyright 2016 Daniel Logan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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

function checkVersion($client_version, $server_version) {
    $cver = substr(trim($client_version), 0, strrchr(trim($client_version), '.'));
    $sver = substr(trim($server_version), 0, strrchr(trim($server_version), '.'));
    return strcasecmp($cver, $sver) == 0;
}

/*if ($_GET['test'] == 'test') {
    
}*/



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
        
        //robot_lu
        $query = "SELECT * FROM robot_lu" . $suffix;
        $result = mysql_query($query);
        $json .= genJSON($result, "robot_lu") . ",";
        mysql_free_result($result);
        
        //position_lu
        $query = "SELECT * FROM position_lu" . $suffix;
        $result = mysql_query($query);
        $json .= genJSON($result, "position_lu") . ",";
        mysql_free_result($result);
        
        
        $query = "SELECT * FROM fact_match_data_2016" . $suffix;
        $result = mysql_query($query);
        $json .= genJSON($result, "fact_match_data_2016") . ",";
        mysql_free_result($result);
        
        
        $query = "SELECT * FROM notes_options" . $suffix;
        $result = mysql_query($query);
        $json .= genJSON($result, "notes_options") . ",";
        mysql_free_result($result);
        
        
        $query = "SELECT * FROM scout_pit_data_2016" . $suffix;
        $result = mysql_query($query);
        $json .= genJSON($result, "scout_pit_data_2016") . ",";
        mysql_free_result($result);
        
        
        $query = "SELECT * FROM wheel_base_lu" . $suffix;
        $result = mysql_query($query);
        $json .= genJSON($result, "wheel_base_lu") . ",";
        mysql_free_result($result);
        
        
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
        
        $event_id = mysql_real_escape_string(stripslashes(trim($_POST['event_id'])));
        $match_id = mysql_real_escape_string(stripslashes(trim($_POST['match_id'])));
        $team_id = mysql_real_escape_string(stripslashes(trim($_POST['team_id'])));
        $practice_match = mysql_real_escape_string(stripslashes(trim($_POST['practice_match'])));
        $position_id = mysql_real_escape_string(stripslashes(trim($_POST['position_id'])));
        $red_def_2 = mysql_real_escape_string(stripslashes(trim($_POST['red_def_2'])));
        $red_def_3 = mysql_real_escape_string(stripslashes(trim($_POST['red_def_3'])));
        $red_def_4 = mysql_real_escape_string(stripslashes(trim($_POST['red_def_4'])));
        $red_def_5 = mysql_real_escape_string(stripslashes(trim($_POST['red_def_5'])));
        $blue_def_2 = mysql_real_escape_string(stripslashes(trim($_POST['blue_def_2'])));
        $blue_def_3 = mysql_real_escape_string(stripslashes(trim($_POST['blue_def_3'])));
        $blue_def_4 = mysql_real_escape_string(stripslashes(trim($_POST['blue_def_4'])));
        $blue_def_5 = mysql_real_escape_string(stripslashes(trim($_POST['blue_def_5'])));
        $auto_reach = mysql_real_escape_string(stripslashes(trim($_POST['auto_reach'])));
        $start_spy = mysql_real_escape_string(stripslashes(trim($_POST['start_spy'])));
        $auto_cross_portcullis_for = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross_portcullis_for'])));
        $auto_cross_portcullis_rev = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross_portcullis_rev'])));
        $auto_cross_cheval_for = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross_cheval_for'])));
        $auto_cross_cheval_rev = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross_cheval_rev'])));
        $auto_cross_moat_for = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross_moat_for'])));
        $auto_cross_moat_rev = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross_moat_rev'])));
        $auto_cross_ramparts_for = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross_ramparts_for'])));
        $auto_cross_ramparts_rev = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross_ramparts_rev'])));
        $auto_cross_drawbridge_for = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross_drawbridge_for'])));
        $auto_cross_drawbridge_for_with_help = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross_drawbridge_for_with_help'])));
        $auto_cross_drawbridge_rev = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross_drawbridge_rev'])));
        $auto_cross_sally_for = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross_sally_for'])));
        $auto_cross_sally_for_with_help = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross_sally_for_with_help'])));
        $auto_cross_sally_rev = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross_sally_rev'])));
        $auto_cross_rock_wall_for = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross_rock_wall_for'])));
        $auto_cross_rock_wall_rev = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross_rock_wall_rev'])));
        $auto_cross_rough_terrain_for = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross_rough_terrain_for'])));
        $auto_cross_rough_terrain_rev = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross_rough_terrain_rev'])));
        $auto_cross_low_bar_for = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross_low_bar_for'])));
        $auto_cross_low_bar_rev = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross_low_bar_rev'])));
        $auto_score_low = mysql_real_escape_string(stripslashes(trim($_POST['auto_score_low'])));
        $auto_score_high = mysql_real_escape_string(stripslashes(trim($_POST['auto_score_high'])));
        $auto_miss_low = mysql_real_escape_string(stripslashes(trim($_POST['auto_miss_low'])));
        $auto_miss_high = mysql_real_escape_string(stripslashes(trim($_POST['auto_miss_high'])));
        $cross_portcullis_for = mysql_real_escape_string(stripslashes(trim($_POST['cross_portcullis_for'])));
        $cross_portcullis_rev = mysql_real_escape_string(stripslashes(trim($_POST['cross_portcullis_rev'])));
        $cross_cheval_for = mysql_real_escape_string(stripslashes(trim($_POST['cross_cheval_for'])));
        $cross_cheval_rev = mysql_real_escape_string(stripslashes(trim($_POST['cross_cheval_rev'])));
        $cross_moat_for = mysql_real_escape_string(stripslashes(trim($_POST['cross_moat_for'])));
        $cross_moat_rev = mysql_real_escape_string(stripslashes(trim($_POST['cross_moat_rev'])));
        $cross_ramparts_for = mysql_real_escape_string(stripslashes(trim($_POST['cross_ramparts_for'])));
        $cross_ramparts_rev = mysql_real_escape_string(stripslashes(trim($_POST['cross_ramparts_rev'])));
        $cross_drawbridge_for = mysql_real_escape_string(stripslashes(trim($_POST['cross_drawbridge_for'])));
        $cross_drawbridge_for_with_help = mysql_real_escape_string(stripslashes(trim($_POST['cross_drawbridge_for_with_help'])));
        $cross_drawbridge_rev = mysql_real_escape_string(stripslashes(trim($_POST['cross_drawbridge_rev'])));
        $cross_sally_for = mysql_real_escape_string(stripslashes(trim($_POST['cross_sally_for'])));
        $cross_sally_for_with_help = mysql_real_escape_string(stripslashes(trim($_POST['cross_sally_for_with_help'])));
        $cross_sally_rev = mysql_real_escape_string(stripslashes(trim($_POST['cross_sally_rev'])));
        $cross_rock_wall_for = mysql_real_escape_string(stripslashes(trim($_POST['cross_rock_wall_for'])));
        $cross_rock_wall_rev = mysql_real_escape_string(stripslashes(trim($_POST['cross_rock_wall_rev'])));
        $cross_rough_terrain_for = mysql_real_escape_string(stripslashes(trim($_POST['cross_rough_terrain_for'])));
        $cross_rough_terrain_rev = mysql_real_escape_string(stripslashes(trim($_POST['cross_rough_terrain_rev'])));
        $cross_low_bar_for = mysql_real_escape_string(stripslashes(trim($_POST['cross_low_bar_for'])));
        $cross_low_bar_rev = mysql_real_escape_string(stripslashes(trim($_POST['cross_low_bar_rev'])));
        $score_low = mysql_real_escape_string(stripslashes(trim($_POST['score_low'])));
        $score_high = mysql_real_escape_string(stripslashes(trim($_POST['score_high'])));
        $miss_low = mysql_real_escape_string(stripslashes(trim($_POST['miss_low'])));
        $miss_high = mysql_real_escape_string(stripslashes(trim($_POST['miss_high'])));
        $challenge = mysql_real_escape_string(stripslashes(trim($_POST['challenge'])));
        $scale = mysql_real_escape_string(stripslashes(trim($_POST['scale'])));
        $foul = mysql_real_escape_string(stripslashes(trim($_POST['foul'])));
        $tip_over = mysql_real_escape_string(stripslashes(trim($_POST['tip_over'])));
        $yellow_card = mysql_real_escape_string(stripslashes(trim($_POST['yellow_card'])));
        $red_card = mysql_real_escape_string(stripslashes(trim($_POST['red_card'])));
        $notes = mysql_real_escape_string(stripslashes(trim($_POST['notes'])));

        
        $result = mysql_query("SELECT id FROM fact_match_data_2016 WHERE event_id=" . $event_id . " AND match_id="
                . $match_id . " AND team_id=" . $team_id . " AND practice_match=" . $practice_match );
        $row = mysql_fetch_array($result);
        $match_row_id = $row["id"];
        
        if (mysql_num_rows($result) == 0) {
        
            $query = "INSERT INTO fact_match_data_2016(event_id,match_id,team_id,practice_match,position_id,"
                    . "red_def_2,red_def_3,red_def_4,red_def_5,blue_def_2,blue_def_3,blue_def_4,blue_def_5,"
                    . "auto_reach,start_spy,auto_cross_portcullis_for,auto_cross_portcullis_rev,auto_cross_cheval_for,"
                    . "auto_cross_cheval_rev,auto_cross_moat_for,auto_cross_moat_rev,auto_cross_ramparts_for,"
                    . "auto_cross_ramparts_rev,auto_cross_drawbridge_for,auto_cross_drawbridge_for_with_help,"
                    . "auto_cross_drawbridge_rev,auto_cross_sally_for,auto_cross_sally_for_with_help,"
                    . "auto_cross_sally_rev,auto_cross_rock_wall_for,auto_cross_rock_wall_rev,"
                    . "auto_cross_rough_terrain_for,auto_cross_rough_terrain_rev,auto_cross_low_bar_for,"
                    . "auto_cross_low_bar_rev,auto_score_low,auto_score_high,auto_miss_low,auto_miss_high,"
                    . "cross_portcullis_for,cross_portcullis_rev,cross_cheval_for,"
                    . "cross_cheval_rev,cross_moat_for,cross_moat_rev,cross_ramparts_for,"
                    . "cross_ramparts_rev,cross_drawbridge_for,cross_drawbridge_for_with_help,"
                    . "cross_drawbridge_rev,cross_sally_for,cross_sally_for_with_help,"
                    . "cross_sally_rev,cross_rock_wall_for,cross_rock_wall_rev,"
                    . "cross_rough_terrain_for,cross_rough_terrain_rev,cross_low_bar_for,"
                    . "cross_low_bar_rev,score_low,score_high,miss_low,miss_high,challenge,scale,"
                    . "foul,tip_over,yellow_card,red_card,notes,invalid) VALUES("
                    . $event_id . ","
                    . $match_id . ","
                    . $team_id . ","
                    . $practice_match . ","
                    . $position_id . ","
                    . $red_def_2 . ","
                    . $red_def_3 . ","
                    . $red_def_4 . ","
                    . $red_def_5 . ","
                    . $blue_def_2 . ","
                    . $blue_def_3 . ","
                    . $blue_def_4 . ","
                    . $blue_def_5 . ","
                    . $auto_reach . ","
                    . $start_spy . ","
                    . $auto_cross_portcullis_for . ","
                    . $auto_cross_portcullis_rev . ","
                    . $auto_cross_cheval_for . ","
                    . $auto_cross_cheval_rev . ","
                    . $auto_cross_moat_for . ","
                    . $auto_cross_moat_rev . ","
                    . $auto_cross_ramparts_for . ","
                    . $auto_cross_ramparts_rev . ","
                    . $auto_cross_drawbridge_for . ","
                    . $auto_cross_drawbridge_for_with_help . ","
                    . $auto_cross_drawbridge_rev . ","
                    . $auto_cross_sally_for . ","
                    . $auto_cross_sally_for_with_help . ","
                    . $auto_cross_sally_rev . ","
                    . $auto_cross_rock_wall_for . ","
                    . $auto_cross_rock_wall_rev . ","
                    . $auto_cross_rough_terrain_for . ","
                    . $auto_cross_rough_terrain_rev . ","
                    . $auto_cross_low_bar_for . ","
                    . $auto_cross_low_bar_rev . ","
                    . $auto_score_low . ","
                    . $auto_score_high . ","
                    . $auto_miss_low . ","
                    . $auto_miss_high . ","
                    . $cross_portcullis_for . ","
                    . $cross_portcullis_rev . ","
                    . $cross_cheval_for . ","
                    . $cross_cheval_rev . ","
                    . $cross_moat_for . ","
                    . $cross_moat_rev . ","
                    . $cross_ramparts_for . ","
                    . $cross_ramparts_rev . ","
                    . $cross_drawbridge_for . ","
                    . $cross_drawbridge_for_with_help . ","
                    . $cross_drawbridge_rev . ","
                    . $cross_sally_for . ","
                    . $cross_sally_for_with_help . ","
                    . $cross_sally_rev . ","
                    . $cross_rock_wall_for . ","
                    . $cross_rock_wall_rev . ","
                    . $cross_rough_terrain_for . ","
                    . $cross_rough_terrain_rev . ","
                    . $cross_low_bar_for . ","
                    . $cross_low_bar_rev . ","
                    . $score_low . ","
                    . $score_high . ","
                    . $miss_low . ","
                    . $miss_high . ","
                    . $challenge . ","
                    . $scale . ","
                    . $foul . ","
                    . $tip_over . ","
                    . $yellow_card . ","
                    . $red_card . ",'"
                    . $notes . "',0);";

            $success = mysql_query($query);
            
        } 
        else {
            $query = "UPDATE fact_match_data_2016 SET "
                    . "event_id=" . $event_id
                    . ",match_id=" . $match_id
                    . ",team_id=" . $team_id
                    . ",practice_match=" . $practice_match
                    . ",position_id=" . $position_id
                    . ",red_def_2=" . $red_def_2
                    . ",red_def_3=" . $red_def_3
                    . ",red_def_4=" . $red_def_4
                    . ",red_def_5=" . $red_def_5
                    . ",blue_def_2=" . $blue_def_2
                    . ",blue_def_3=" . $blue_def_3
                    . ",blue_def_4=" . $blue_def_4
                    . ",blue_def_5=" . $blue_def_5
                    . ",auto_reach=" . $auto_reach
                    . ",start_spy=" . $start_spy
                    . ",auto_cross_portcullis_for=" . $auto_cross_portcullis_for
                    . ",auto_cross_portcullis_rev=" . $auto_cross_portcullis_rev
                    . ",auto_cross_cheval_for=" . $auto_cross_cheval_for
                    . ",auto_cross_cheval_rev=" . $auto_cross_cheval_rev
                    . ",auto_cross_moat_for=" . $auto_cross_moat_for
                    . ",auto_cross_moat_rev=" . $auto_cross_moat_rev
                    . ",auto_cross_ramparts_for=" . $auto_cross_ramparts_for
                    . ",auto_cross_ramparts_rev=" . $auto_cross_ramparts_rev
                    . ",auto_cross_drawbridge_for=" . $auto_cross_drawbridge_for
                    . ",auto_cross_drawbridge_for_with_help=" . $auto_cross_drawbridge_for_with_help
                    . ",auto_cross_drawbridge_rev=" . $auto_cross_drawbridge_rev
                    . ",auto_cross_sally_for=" . $auto_cross_sally_for
                    . ",auto_cross_sally_for_with_help=" . $auto_cross_sally_for_with_help
                    . ",auto_cross_sally_rev=" . $auto_cross_sally_rev
                    . ",auto_cross_rock_wall_for=" . $auto_cross_rock_wall_for
                    . ",auto_cross_rock_wall_rev=" . $auto_cross_rock_wall_rev
                    . ",auto_cross_rough_terrain_for=" . $auto_cross_rough_terrain_for
                    . ",auto_cross_rough_terrain_rev=" . $auto_cross_rough_terrain_rev
                    . ",auto_cross_low_bar_for=" . $auto_cross_low_bar_for
                    . ",auto_cross_low_bar_rev=" . $auto_cross_low_bar_rev
                    . ",auto_score_low=" . $auto_score_low
                    . ",auto_score_high=" . $auto_score_high
                    . ",auto_miss_low=" . $auto_miss_low
                    . ",auto_miss_high=" . $auto_miss_high
                    . ",cross_portcullis_for=" . $cross_portcullis_for
                    . ",cross_portcullis_rev=" . $cross_portcullis_rev
                    . ",cross_cheval_for=" . $cross_cheval_for
                    . ",cross_cheval_rev=" . $cross_cheval_rev
                    . ",cross_moat_for=" . $cross_moat_for
                    . ",cross_moat_rev=" . $cross_moat_rev
                    . ",cross_ramparts_for=" . $cross_ramparts_for
                    . ",cross_ramparts_rev=" . $cross_ramparts_rev
                    . ",cross_drawbridge_for=" . $cross_drawbridge_for
                    . ",cross_drawbridge_for_with_help=" . $cross_drawbridge_for_with_help
                    . ",cross_drawbridge_rev=" . $cross_drawbridge_rev
                    . ",cross_sally_for=" . $cross_sally_for
                    . ",cross_sally_for_with_help=" . $cross_sally_for_with_help
                    . ",cross_sally_rev=" . $cross_sally_rev
                    . ",cross_rock_wall_for=" . $cross_rock_wall_for
                    . ",cross_rock_wall_rev=" . $cross_rock_wall_rev
                    . ",cross_rough_terrain_for=" . $cross_rough_terrain_for
                    . ",cross_rough_terrain_rev=" . $cross_rough_terrain_rev
                    . ",cross_low_bar_for=" . $cross_low_bar_for
                    . ",cross_low_bar_rev=" . $cross_low_bar_rev
                    . ",score_low=" . $score_low
                    . ",score_high=" . $score_high
                    . ",miss_low=" . $miss_low
                    . ",miss_high=" . $miss_high
                    . ",challenge=" . $challenge
                    . ",scale=" . $scale
                    . ",foul=" . $foul
                    . ",tip_over=" . $tip_over
                    . ",yellow_card=" . $yellow_card
                    . ",red_card=" . $red_card
                    . ",notes='" . $notes
                    . "',invalid=0"
                    . " WHERE id=" . $match_row_id;

            $success = mysql_query($query);
        }
        
        
        if ($success) {
            $query = "SELECT id, timestamp FROM fact_match_data_2016 WHERE event_id=" . $event_id . " AND match_id="
                    . $match_id . " AND team_id=" . $team_id . " AND practice_match=" . $practice_match;
            $result = mysql_query($query);
            if ($result) {
                $row = mysql_fetch_array($result);
                $resp = $row["id"] . "," . strtotime($row["timestamp"]);
            }
            else {
                $resp = 'Failed to retrieve timestamp';
            }
        } else {
            $resp = 'Database Query Failed';
            //$resp .= $query;
        }

        //$resp = $query;
    } 
    else if ($_POST['type'] == 'pits') {
        
        $team_id = mysql_real_escape_string(stripslashes(trim($_POST['team_id'])));
        $config_id = mysql_real_escape_string(stripslashes(trim($_POST['config_id'])));
        $wheel_type_id = mysql_real_escape_string(stripslashes(trim($_POST['wheel_type_id'])));
        $wheel_base_id = mysql_real_escape_string(stripslashes(trim($_POST['wheel_base_id'])));
        $notes = mysql_real_escape_string(stripslashes(trim($_POST['notes'])));
        $start_spy = mysql_real_escape_string(stripslashes(trim($_POST['start_spy'])));
        $auto_reach = mysql_real_escape_string(stripslashes(trim($_POST['auto_reach'])));
        $auto_cross = mysql_real_escape_string(stripslashes(trim($_POST['auto_cross'])));
        $auto_score_low = mysql_real_escape_string(stripslashes(trim($_POST['auto_score_low'])));
        $auto_score_high = mysql_real_escape_string(stripslashes(trim($_POST['auto_score_high'])));
        $cross_portcullis = mysql_real_escape_string(stripslashes(trim($_POST['cross_portcullis'])));
        $cross_cheval = mysql_real_escape_string(stripslashes(trim($_POST['cross_cheval'])));
        $cross_moat = mysql_real_escape_string(stripslashes(trim($_POST['cross_moat'])));
        $cross_ramparts = mysql_real_escape_string(stripslashes(trim($_POST['cross_ramparts'])));
        $cross_drawbridge_for = mysql_real_escape_string(stripslashes(trim($_POST['cross_drawbridge_for'])));
        $cross_drawbridge_for_with_help = mysql_real_escape_string(stripslashes(trim($_POST['cross_drawbridge_for_with_help'])));
        $cross_drawbridge_rev = mysql_real_escape_string(stripslashes(trim($_POST['cross_drawbridge_rev'])));
        $cross_sally_for = mysql_real_escape_string(stripslashes(trim($_POST['cross_sally_for'])));
        $cross_sally_for_with_help = mysql_real_escape_string(stripslashes(trim($_POST['cross_sally_for_with_help'])));
        $cross_sally_rev = mysql_real_escape_string(stripslashes(trim($_POST['cross_sally_rev'])));
        $cross_rock_wall = mysql_real_escape_string(stripslashes(trim($_POST['cross_rock_wall'])));
        $cross_rough_terrain = mysql_real_escape_string(stripslashes(trim($_POST['cross_rough_terrain'])));
        $cross_low_bar = mysql_real_escape_string(stripslashes(trim($_POST['cross_low_bar'])));
        $score_high = mysql_real_escape_string(stripslashes(trim($_POST['score_high'])));
        $score_low = mysql_real_escape_string(stripslashes(trim($_POST['score_low'])));
        $challenge = mysql_real_escape_string(stripslashes(trim($_POST['challenge'])));
        $scale = mysql_real_escape_string(stripslashes(trim($_POST['scale'])));


        $query = " SELECT id FROM scout_pit_data_2016 WHERE team_id=" . $team_id;
        $result = mysql_query($query);
        $row = mysql_fetch_array($result);
        $id = $row["id"];
        if (mysql_num_rows($result) == 0) {
            $success = false;
            $query = "INSERT INTO scout_pit_data_2016(team_id,config_id,wheel_type_id,wheel_base_id,notes,"
                    . "start_spy,auto_reach,auto_cross,auto_score_low,auto_score_high,cross_portcullis,cross_cheval,"
                    . "cross_moat,cross_ramparts,cross_drawbridge_for,cross_drawbridge_for_with_help,cross_drawbridge_rev,"
                    . "cross_sally_for,cross_sally_for_with_help,cross_sally_rev,cross_rock_wall,cross_rough_terrain,"
                    . "cross_low_bar,score_high,score_low,challenge,scale,invalid) "
                    . "VALUES("
                    . $team_id
                    . "," . $config_id
                    . "," . $wheel_type_id
                    . "," . $wheel_base_id
                    . ",'" . $notes
                    . "'," . $start_spy
                    . "," . $auto_reach
                    . "," . $auto_cross
                    . "," . $auto_score_low
                    . "," . $auto_score_high
                    . "," . $cross_portcullis
                    . "," . $cross_cheval
                    . "," . $cross_moat
                    . "," . $cross_ramparts
                    . "," . $cross_drawbridge_for
                    . "," . $cross_drawbridge_for_with_help
                    . "," . $cross_drawbridge_rev
                    . "," . $cross_sally_for
                    . "," . $cross_sally_for_with_help
                    . "," . $cross_sally_rev
                    . "," . $cross_rock_wall
                    . "," . $cross_rough_terrain
                    . "," . $cross_low_bar
                    . "," . $score_high
                    . "," . $score_low
                    . "," . $challenge
                    . "," . $scale . ",0);";
          

            $success = mysql_query($query);
        } else {
            
            $success = false;
            $query = " UPDATE scout_pit_data_2016 ";
            $query .= "SET team_id=" . $team_id . ","
                    . "config_id=" . $config_id . ","
                    . "wheel_type_id=" . $wheel_type_id . ","
                    . "wheel_base_id=" . $wheel_base_id . ","
                    . "notes='" . $notes . "',"
                    . "start_spy=" . $start_spy . ","
                    . "auto_reach=" . $auto_reach . ","
                    . "auto_cross=" . $auto_cross . ","
                    . "auto_score_low=" . $auto_score_low . ","
                    . "auto_score_high=" . $auto_score_high . ","
                    . "cross_portcullis=" . $cross_portcullis . ","
                    . "cross_cheval=" . $cross_cheval . ","
                    . "cross_moat=" . $cross_moat . ","
                    . "cross_ramparts=" . $cross_ramparts . ","
                    . "cross_drawbridge_for=" . $cross_drawbridge_for . ","
                    . "cross_drawbridge_for_with_help=" . $cross_drawbridge_for_with_help . ","
                    . "cross_drawbridge_rev=" . $cross_drawbridge_rev . ","
                    . "cross_sally_for=" . $cross_sally_for . ","
                    . "cross_sally_for_with_help=" . $cross_sally_for_with_help . ","
                    . "cross_sally_rev=" . $cross_sally_rev . ","
                    . "cross_rock_wall=" . $cross_rock_wall . ","
                    . "cross_rough_terrain=" . $cross_rough_terrain . ","
                    . "cross_low_bar=" . $cross_low_bar . ","
                    . "score_high=" . $score_high . ","
                    . "score_low=" . $score_low . ","
                    . "challenge=" . $challenge . ","
                    . "scale=" . $scale . ","
                    . "invalid=0"
                    . " WHERE id=" . $id;

            $success = mysql_query($query);
        }
        
        if ($success) {
            $result = mysql_query("SELECT id, timestamp FROM scout_pit_data_2016 WHERE team_id=" . $team_id);
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
