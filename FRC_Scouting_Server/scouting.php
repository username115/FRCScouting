<?php

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
    
    if ($_POST['type'] == 'fullsync' || $_POST['type'] == 'sync') {
        //syncronization request. if it's a fullsync, then send all data, otherwise use the timestamp (in unix time)
        if ($_POST['type'] == 'fullsync') {
            $suffix = ';';
        } else {
            $suffix = ' WHERE timestamp >= FROM_UNIXTIME(' . $_POST['timestamp'] . ');';
        }
        
        $json = '{"timestamp" : ' . strtotime(date("Y-m-d H:i:s")) . ',';
        
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
        
        
        
        $query = "SELECT * FROM fact_match_data_2015" . $suffix;
        $result = mysql_query($query);
        $json .= genJSON($result, "fact_match_data_2015") . ",";
        mysql_free_result($result);
        
        
        $query = "SELECT * FROM notes_options" . $suffix;
        $result = mysql_query($query);
        $json .= genJSON($result, "notes_options") . ",";
        mysql_free_result($result);
        
        
        $query = "SELECT * FROM scout_pit_data_2015" . $suffix;
        $result = mysql_query($query);
        $json .= genJSON($result, "scout_pit_data_2015") . ",";
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
    else if ($_POST['type'] == 'match') {
        
        $event_id = mysql_real_escape_string(stripslashes(trim($_POST['event_id'])));
        $match_id = mysql_real_escape_string(stripslashes(trim($_POST['match_id'])));
        $team_id = mysql_real_escape_string(stripslashes(trim($_POST['team_id'])));
        $practice_match = mysql_real_escape_string(stripslashes(trim($_POST['practice_match'])));
        $auto_move = mysql_real_escape_string(stripslashes(trim($_POST['auto_move'])));
        $auto_totes = mysql_real_escape_string(stripslashes(trim($_POST['auto_totes'])));
        $auto_stack_2 = mysql_real_escape_string(stripslashes(trim($_POST['auto_stack_2'])));
        $auto_stack_3 = mysql_real_escape_string(stripslashes(trim($_POST['auto_stack_3'])));
        $auto_bin = mysql_real_escape_string(stripslashes(trim($_POST['auto_bin'])));
        $auto_step_bin = mysql_real_escape_string(stripslashes(trim($_POST['auto_step_bin'])));
        $totes_1 = mysql_real_escape_string(stripslashes(trim($_POST['totes_1'])));
        $totes_2 = mysql_real_escape_string(stripslashes(trim($_POST['totes_2'])));
        $totes_3 = mysql_real_escape_string(stripslashes(trim($_POST['totes_3'])));
        $totes_4 = mysql_real_escape_string(stripslashes(trim($_POST['totes_4'])));
        $totes_5 = mysql_real_escape_string(stripslashes(trim($_POST['totes_5'])));
        $totes_6 = mysql_real_escape_string(stripslashes(trim($_POST['totes_6'])));
        $coop_1 = mysql_real_escape_string(stripslashes(trim($_POST['coop_1'])));
        $coop_2 = mysql_real_escape_string(stripslashes(trim($_POST['coop_2'])));
        $coop_3 = mysql_real_escape_string(stripslashes(trim($_POST['coop_3'])));
        $coop_4 = mysql_real_escape_string(stripslashes(trim($_POST['coop_4'])));
        $bin_1 = mysql_real_escape_string(stripslashes(trim($_POST['bin_1'])));
        $bin_2 = mysql_real_escape_string(stripslashes(trim($_POST['bin_2'])));
        $bin_3 = mysql_real_escape_string(stripslashes(trim($_POST['bin_3'])));
        $bin_4 = mysql_real_escape_string(stripslashes(trim($_POST['bin_4'])));
        $bin_5 = mysql_real_escape_string(stripslashes(trim($_POST['bin_5'])));
        $bin_6 = mysql_real_escape_string(stripslashes(trim($_POST['bin_6'])));
        $bin_litter = mysql_real_escape_string(stripslashes(trim($_POST['bin_litter'])));
        $landfill_litter = mysql_real_escape_string(stripslashes(trim($_POST['landfill_litter'])));
        $foul = mysql_real_escape_string(stripslashes(trim($_POST['foul'])));
        $tip_over = mysql_real_escape_string(stripslashes(trim($_POST['tip_over'])));
        $yellow_card = mysql_real_escape_string(stripslashes(trim($_POST['yellow_card'])));
        $red_card = mysql_real_escape_string(stripslashes(trim($_POST['red_card'])));
        $notes = mysql_real_escape_string(stripslashes(trim($_POST['notes'])));        
        
        $result = mysql_query("SELECT id FROM fact_match_data WHERE event_id=" . $event_id . " AND match_id="
                . $match_id . " AND team_id=" . $team_id . " AND practice_match=" . $practice_match );
        $row = mysql_fetch_array($result);
        $match_row_id = $row["id"];
        
        if (mysql_num_rows($result) == 0) {
        
            $query = "INSERT INTO fact_match_data(event_id,match_id,team_id,practice_match,auto_move,auto_totes,auto_stack_2,auto_stack_3,auto_bin,"
                    . "auto_step_bin,totes_1,totes_2,totes_3,totes_4,totes_5,totes_6,coop_1,coop_2,coop_3,coop_4,bin_1,bin_2,bin_3,bin_4,bin_5,bin_6,"
                    . "bin_litter,landfill_litter,foul,tip_over,yellow_card,red_card,notes,invalid) VALUES("
                    . $event_id . ","
                    . $match_id . ","
                    . $team_id . ","
                    . $practice_match . ","
                    . $auto_move . ","
                    . $auto_totes . ","
                    . $auto_stack_2 . ","
                    . $auto_stack_3 . ","
                    . $auto_bin . ","
                    . $auto_step_bin . ","
                    . $totes_1 . ","
                    . $totes_2 . ","
                    . $totes_3 . ","
                    . $totes_4 . ","
                    . $totes_5 . ","
                    . $totes_6 . ","
                    . $coop_1 . ","
                    . $coop_2 . ","
                    . $coop_3 . ","
                    . $coop_4 . ","
                    . $bin_1 . ","
                    . $bin_2 . ","
                    . $bin_3 . ","
                    . $bin_4 . ","
                    . $bin_5 . ","
                    . $bin_6 . ","
                    . $bin_litter . ","
                    . $landfill_litter . ","
                    . $foul . ","
                    . $tech_foul . ","
                    . $tip_over . ","
                    . $yellow_card . ","
                    . $red_card . ",'"
                    . $notes . "',0);";

            $success = mysql_query($query);
            
        } 
        else {
            $query = "UPDATE fact_match_data SET "
                    . "event_id=" . $event_id
                    . ",match_id=" . $match_id
                    . ",team_id=" . $team_id
                    . ",practice_match=" . $practice_match
                    . ",auto_move=" . $auto_move
                    . ",auto_totes=" . $auto_totes
                    . ",auto_stack_2=" . $auto_stack_2
                    . ",auto_stack_3=" . $auto_stack_3
                    . ",auto_bin=" . $auto_bin
                    . ",auto_step_bin=" . $auto_step_bin
                    . ",totes_1=" . $totes_1
                    . ",totes_2=" . $totes_2
                    . ",totes_3=" . $totes_3
                    . ",totes_4=" . $totes_4
                    . ",totes_5=" . $totes_5
                    . ",totes_6=" . $totes_6
                    . ",coop_1=" . $coop_1
                    . ",coop_2=" . $coop_2
                    . ",coop_3=" . $coop_3
                    . ",coop_4=" . $coop_4
                    . ",bin_1=" . $bin_1
                    . ",bin_2=" . $bin_2
                    . ",bin_3=" . $bin_3
                    . ",bin_4=" . $bin_4
                    . ",bin_5=" . $bin_5
                    . ",bin_6=" . $bin_6
                    . ",bin_litter=" . $bin_litter
                    . ",landfill_litter=" . $landfill_litter
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
            $result = mysql_query("SELECT id, timestamp FROM fact_match_data WHERE event_id=" . $event_id . " AND match_id="
                    . $match_id . " AND team_id=" . $team_id . "AND practice_match=" . $practice_match);
            $row = mysql_fetch_array($result);
            $resp = $row["id"] . "," . strtotime($row["timestamp"]);
        } else {
            $resp = 'Database Query Failed';
        }

        //$resp = $query;
    } 
    else if ($_POST['type'] == 'pits') {
        
        $team_id = mysql_real_escape_string(stripslashes(trim($_POST['team_id'])));
        $config = mysql_real_escape_string(stripslashes(trim($_POST['config_id'])));
        $wheel_type_id = mysql_real_escape_string(stripslashes(trim($_POST['wheel_type_id'])));
        $wheel_base_id = mysql_real_escape_string(stripslashes(trim($_POST['wheel_base_id'])));
        $notes = mysql_real_escape_string(stripslashes(trim($_POST['notes'])));
        $push_tote = mysql_real_escape_string(stripslashes(trim($_POST['push_tote'])));
        $push_bin = mysql_real_escape_string(stripslashes(trim($_POST['push_bin'])));
        $lift_tote = mysql_real_escape_string(stripslashes(trim($_POST['lift_tote'])));
        $lift_bin = mysql_real_escape_string(stripslashes(trim($_POST['lift_bin'])));
        $push_litter = mysql_real_escape_string(stripslashes(trim($_POST['push_litter'])));
        $load_litter = mysql_real_escape_string(stripslashes(trim($_POST['load_litter'])));
        $stack_tote_height = mysql_real_escape_string(stripslashes(trim($_POST['stack_tote_height'])));
        $stack_bin_height = mysql_real_escape_string(stripslashes(trim($_POST['stack_bin_height'])));
        $coop_totes = mysql_real_escape_string(stripslashes(trim($_POST['coop_totes'])));
        $coop_stack_height = mysql_real_escape_string(stripslashes(trim($_POST['coop_stack_height'])));
        $move_auto = mysql_real_escape_string(stripslashes(trim($_POST['move_auto'])));
        $auto_bin_score = mysql_real_escape_string(stripslashes(trim($_POST['auto_bin_score'])));
        $auto_tote_score = mysql_real_escape_string(stripslashes(trim($_POST['auto_tote_score'])));
        $auto_tote_stack_height = mysql_real_escape_string(stripslashes(trim($_POST['auto_tote_stack_height'])));
        $auto_step_bins = mysql_real_escape_string(stripslashes(trim($_POST['auto_step_bins'])));
        $manip_style = mysql_real_escape_string(stripslashes(trim($_POST['manip_style'])));
        $need_upright_tote = mysql_real_escape_string(stripslashes(trim($_POST['need_upright_tote'])));
        $need_upright_bin = mysql_real_escape_string(stripslashes(trim($_POST['need_upright_bin'])));
        $can_upright_tote = mysql_real_escape_string(stripslashes(trim($_POST['can_upright_tote'])));
        $can_upright_bin = mysql_real_escape_string(stripslashes(trim($_POST['can_upright_bin'])));

        $query = " SELECT id FROM scout_pit_data WHERE team_id=" . $team_id;
        $result = mysql_query($query);
        $row = mysql_fetch_array($result);
        $id = $row["id"];
        if (mysql_num_rows($result) == 0) {
            $success = false;
            $query = "INSERT INTO scout_pit_data(team_id,config_id,wheel_type_id,wheel_base_id,notes,push_tote,push_bin,lift_tote,lift_bin,push_litter,"
                    . "load_litter,stack_tote_height,stack_bin_height,coop_totes,coop_stack_height,move_auto,auto_bin_score,auto_tote_score,"
                    . "auto_tote_stack_height,auto_step_bins,manip_style,need_upright_tote,need_upright_bin,can_upright_tote,can_upright_bin,invalid) "
                    . "VALUES("
                    . $team_id
                    . "," . $config_id
                    . "," . $wheel_type_id
                    . "," . $wheel_base_id
                    . ",'" . $notes
                    . "'," . $push_tote
                    . "," . $push_bin
                    . "," . $lift_tote
                    . "," . $lift_bin
                    . "," . $push_litter
                    . "," . $load_litter
                    . "," . $stack_tote_height
                    . "," . $stack_bin_height
                    . "," . $coop_totes
                    . "," . $coop_stack_height
                    . "," . $move_auto
                    . "," . $auto_bin_score
                    . "," . $auto_tote_score
                    . "," . $auto_tote_stack_height
                    . "," . $auto_step_bins
                    . ",'" . $manip_style
                    . "'," . $need_upright_tote
                    . "," . $need_upright_bin
                    . "," . $can_upright_tote
                    . "," . $can_upright_bin . ",0);";
          

            $success = mysql_query($query);
        } else {
            
            $success = false;
            $query = " UPDATE scout_pit_data ";
            $query .= "SET team_id=" . $team_id . ",
						config_id=" . $config_id . ",
						wheel_type_id=" . $wheel_type_id . ",
						wheel_base_id=" . $wheel_base_id . ",
						notes='" . $notes . "',
						push_tote=" . $push_tote . ",
						push_bin=" . $push_bin . ",
						lift_tote=" . $lift_tote . ",
						lift_bin=" . $lift_bin . ",
						push_litter=" . $push_litter . ",
                                                load_litter=" . $load_litter . ",
						stack_tote_height=" . $stack_tote_height . ",
						stack_bin_height=" . $stack_bin_height . ",
						coop_totes=" . $coop_totes . ",
                                                coop_stack_height=" . $coop_stack_height . ",
                                                move_auto=" . $move_auto . ",
                                                auto_bin_score=" . $auto_bin_score . ",
                                                auto_tote_score=" . $auto_tote_score . ",
                                                auto_tote_stack_height=" . $auto_tote_stack_height . ",
                                                auto_step_bins=" . $auto_step_bins . ",
                                                manip_style='" . $manip_style . "',
                                                need_upright_tote=" . $need_upright_tote . ",
                                                need_upright_bin=" . $need_upright_bin . ",
                                                can_upright_tote=" . $can_upright_tote . ",
                                                can_upright_bin=" . $can_upright_bin . ",
                                                invalid=0" . " 
						WHERE id=" . $id;

            $success = mysql_query($query);
        }
        
        if ($success) {
            $result = mysql_query("SELECT id, timestamp FROM scout_pit_data WHERE team_id=" . $team_id);
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

