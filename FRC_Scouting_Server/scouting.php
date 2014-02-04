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

if ($_GET['test'] == 'test') {
    if ($_GET['type'] == 'fullsync') {
            $suffix = ';';
        } else {
            $suffix = ' WHERE timestamp > FROM_UNIXTIME(' . $_GET['timestamp'] . ');';
        }
        
        echo $suffix . '<br>';
        
        
        $temp =  mysql_query('SELECT FROM_UNIXTIME(' . $_GET['timestamp'] . ');');
        
        echo mysql_result($temp, 0) . '<br>';
        
        $json = "{";
        
        $query = "SELECT * FROM configuration_lu" . $suffix;
        $result = mysql_query($query);
        
        $json .= genJSON($result, "configuration_lu") . ",";
        mysql_free_result($result);
        
        
        $query = "SELECT * FROM event_lu" . $suffix;
        $result = mysql_query($query);
        
        $json .= genJSON($result, "event_lu") . ",";
        mysql_free_result($result);
        
        
        $query = "SELECT * FROM fact_cycle_data" . $suffix;
        $result = mysql_query($query);
        
        $json .= genJSON($result, "fact_cycle_data") . ",";
        mysql_free_result($result);
        
        
        
        $query = "SELECT * FROM fact_match_data_2013" . $suffix;
        $result = mysql_query($query);
        
        $json .= genJSON($result, "fact_match_data") . ",";
        mysql_free_result($result);
        
        
        $query = "SELECT * FROM notes_options" . $suffix;
        $result = mysql_query($query);
        
        $json .= genJSON($result, "notes_options") . ",";
        mysql_free_result($result);
        
        
        $query = "SELECT * FROM scout_pit_data" . $suffix;
        $result = mysql_query($query);
        
        $json .= genJSON($result, "scout_pit_data") . ",";
        mysql_free_result($result);
        
        
        $query = "SELECT * FROM wheel_base_lu" . $suffix;
        $result = mysql_query($query);
        
        $json .= genJSON($result, "wheel_base_lu") . ",";
        mysql_free_result($result);
        
        
        $query = "SELECT * FROM wheel_type_lu" . $suffix;
        $result = mysql_query($query);
        
        $json .= genJSON($result, "wheel_type_lu") . "}";
        mysql_free_result($result);
        
        echo $json;
    
}



if ($_POST['type'] == 'passConfirm' && $_POST['password'] == $pass) {
    header($_SERVER['SERVER_PROTOCOL'] . ' 200 OK');
    echo 'success';
} elseif ($_POST['type'] == 'versioncheck') {
    header($_SERVER['SERVER_PROTOCOL'] . ' 200 OK');
    echo $ver;
} elseif ($_POST['password'] == $pass) {
    header($_SERVER['SERVER_PROTOCOL'] . ' 200 OK');
    
    if ($_POST['type'] == 'fullsync' || $_POST['type'] == 'sync') {
        //syncronization request. if it's a fullsync, then send all data, otherwise use the timestamp (in unix time)
        if ($_POST['type'] == 'fullsync') {
            $suffix = ';';
        } else {
            $suffix = ' WHERE timestamp >= FROM_UNIXTIME(' . $_POST['timestamp'] . ');';
        }
        
        $json = "{";
        
        $query = "SELECT * FROM configuration_lu" . $suffix;
        $result = mysql_query($query);
        
        $json .= genJSON($result, "configuration_lu") . ",";
        mysql_free_result($result);
        
        
        $query = "SELECT * FROM event_lu" . $suffix;
        $result = mysql_query($query);
        
        $json .= genJSON($result, "event_lu") . ",";
        mysql_free_result($result);
        
        
        $query = "SELECT * FROM fact_cycle_data" . $suffix;
        $result = mysql_query($query);
        
        $json .= genJSON($result, "fact_cycle_data") . ",";
        mysql_free_result($result);
        
        
        
        $query = "SELECT * FROM fact_match_data" . $suffix;
        $result = mysql_query($query);
        
        $json .= genJSON($result, "fact_match_data") . ",";
        mysql_free_result($result);
        
        
        $query = "SELECT * FROM notes_options" . $suffix;
        $result = mysql_query($query);
        
        $json .= genJSON($result, "notes_options") . ",";
        mysql_free_result($result);
        
        
        $query = "SELECT * FROM scout_pit_data" . $suffix;
        $result = mysql_query($query);
        
        $json .= genJSON($result, "scout_pit_data") . ",";
        mysql_free_result($result);
        
        
        $query = "SELECT * FROM wheel_base_lu" . $suffix;
        $result = mysql_query($query);
        
        $json .= genJSON($result, "wheel_base_lu") . ",";
        mysql_free_result($result);
        
        
        $query = "SELECT * FROM wheel_type_lu" . $suffix;
        $result = mysql_query($query);
        
        $json .= genJSON($result, "wheel_type_lu") . "}";
        mysql_free_result($result);
        
        echo $json;
        
    }

    if ($_POST['type'] == 'match') {
        
        $event_id = mysql_real_escape_string(stripslashes(trim($_POST['event_id'])));
        $match_id = mysql_real_escape_string(stripslashes(trim($_POST['match_id'])));
        $team_id = mysql_real_escape_string(stripslashes(trim($_POST['team_id'])));
        $auto_high = mysql_real_escape_string(stripslashes(trim($_POST['auto_high'])));
        $auto_high_hot = mysql_real_escape_string(stripslashes(trim($_POST['auto_high_hot'])));
        $auto_low = mysql_real_escape_string(stripslashes(trim($_POST['auto_low'])));
        $auto_low_hot = mysql_real_escape_string(stripslashes(trim($_POST['auto_low_hot'])));
        $high = mysql_real_escape_string(stripslashes(trim($_POST['high'])));
        $low = mysql_real_escape_string(stripslashes(trim($_POST['low'])));
        $auto_mobile = mysql_real_escape_string(stripslashes(trim($_POST['auto_mobile'])));
        $foul = mysql_real_escape_string(stripslashes(trim($_POST['foul'])));
        $tech_foul = mysql_real_escape_string(stripslashes(trim($_POST['tech_foul'])));
        $tip_over = mysql_real_escape_string(stripslashes(trim($_POST['tip_over'])));
        $yellow_card = mysql_real_escape_string(stripslashes(trim($_POST['yellow_card'])));
        $red_card = mysql_real_escape_string(stripslashes(trim($_POST['red_card'])));
        $notes = mysql_real_escape_string(stripslashes(trim($_POST['notes'])));
        
        
        $result = mysql_query("SELECT id FROM fact_match_data WHERE event_id=" . $event_id . " AND match_id="
                . $match_id . " AND team_id=" . $team_id );
        $row = mysql_fetch_array($result);
        $match_row_id = $row["id"];
        
        if (mysql_num_rows($result) == 0) {
        
            $query = "INSERT INTO fact_match_data(event_id,match_id,team_id,auto_high,auto_high_hot,auto_low,auto_low_hot,high,low,"
                    . "auto_mobile,foul,tech_foul,tip_over,yellow_card,red_card,notes,invalid) VALUES("
                    . $event_id . ","
                    . $match_id . ","
                    . $team_id . ","
                    . $auto_high . ","
                    . $auto_high_hot . ","
                    . $auto_low . ","
                    . $auto_low_hot . ","
                    . $high . ","
                    . $low . ","
                    . $auto_mobile . ","
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
                    . ",auto_high=" . $auto_high
                    . ",auto_high_hot=" . $auto_high_hot
                    . ",auto_low=" . $auto_low
                    . ",auto_low_hot=" . $auto_low_hot
                    . ",high=" . $high
                    . ",low=" . $low
                    . ",auto_mobile=" . $auto_mobile
                    . ",foul=" . $foul
                    . ",tech_foul=" . $tech_foul
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
                    . $match_id . " AND team_id=" . $team_id);
            $row = mysql_fetch_array($result);
            $resp = $row["id"];
        } else {
            $resp = 'Database Query Failed';
        }

        //$resp = $query;
    } 
    else if ($_POST['type'] == 'cycle') {
        $event_id = mysql_real_escape_string(stripslashes(trim($_POST['event_id'])));
        $match_id = mysql_real_escape_string(stripslashes(trim($_POST['match_id'])));
        $team_id = mysql_real_escape_string(stripslashes(trim($_POST['team_id'])));
        $cycle_num = mysql_real_escape_string(stripslashes(trim($_POST['cycle_num'])));
        $near_poss = mysql_real_escape_string(stripslashes(trim($_POST['near_poss'])));
        $white_poss = mysql_real_escape_string(stripslashes(trim($_POST['white_poss'])));
        $far_poss = mysql_real_escape_string(stripslashes(trim($_POST['far_poss'])));
        $truss = mysql_real_escape_string(stripslashes(trim($_POST['truss'])));
        $catch = mysql_real_escape_string(stripslashes(trim($_POST['catch'])));
        $high = mysql_real_escape_string(stripslashes(trim($_POST['high'])));
        $low = mysql_real_escape_string(stripslashes(trim($_POST['low'])));
        $assists = mysql_real_escape_string(stripslashes(trim($_POST['assists'])));
        
        $result = mysql_query("SELECT id FROM fact_cycle_data WHERE event_id=" . $event_id . " AND match_id="
                . $match_id . " AND team_id=" . $team_id . " AND cycle_num=" . $cycle_num);
        $row = mysql_fetch_array($result);
        $match_row_id = $row["id"];
        
        if (mysql_num_rows($result) == 0) {
            $query = "INSERT INTO fact_cycle_data(event_id,match_id,team_id,cycle_num,near_poss,white_poss,far_poss,"
                    . "truss,catch,high,low,assists,invalid) VALUES("
                    . $event_id . ","
                    . $match_id . ","
                    . $team_id . ","
                    . $cycle_num . ","
                    . $near_poss . ","
                    . $white_poss . ","
                    . $far_poss . ","
                    . $truss . ","
                    . $catch . ","
                    . $high . ","
                    . $low . ","
                    . $assists . ",0);";

            $success = mysql_query($query);
        }
        else {
            $query = "UPDATE fact_cycle_data SET "
                    . "event_id=" . $event_id
                    . ",match_id=" . $match_id
                    . ",team_id=" . $team_id
                    . ",cycle_num=" . $cycle_num
                    . ",near_poss=" . $near_poss
                    . ",white_poss=" . $white_poss
                    . ",far_poss=" . $far_poss
                    . ",truss=" . $truss
                    . ",catch=" . $catch
                    . ",high=" . $high
                    . ",low=" . $low
                    . ",assists=" . $assists
                    . ",invalid=0"
                    . " WHERE id=" . $match_row_id;

            $success = mysql_query($query);
        }
        if ($success) {
            $result = mysql_query("SELECT id, timestamp FROM fact_cycle_data WHERE event_id=" . $event_id . " AND match_id="
                    . $match_id . " AND team_id=" . $team_id . " AND cycle_num=" . $cycle_num);
            $row = mysql_fetch_array($result);
            $resp = $row["id"];
        } else {
            $resp = 'Database Query Failed';
        }
    }
    else if ($_POST['type'] == 'pits') {
        $team_id = mysql_real_escape_string(stripslashes(trim($_POST['team_id'])));
        $configuration_id = mysql_real_escape_string(stripslashes(trim($_POST['configuration_id'])));
        $wheel_type_id = mysql_real_escape_string(stripslashes(trim($_POST['wheel_type_id'])));
        $wheel_base_id = mysql_real_escape_string(stripslashes(trim($_POST['wheel_base_id'])));
        $autonomous_mode = mysql_real_escape_string(stripslashes(trim($_POST['autonomous_mode'])));
        $scout_comments = mysql_real_escape_string(stripslashes(trim($_POST['scout_comments'])));
        $pyramid_climb_level = mysql_real_escape_string(stripslashes(trim($_POST['pyramid_climb'])));
        $score_high = mysql_real_escape_string(stripslashes(trim($_POST['score_high'])));
        $score_mid = mysql_real_escape_string(stripslashes(trim($_POST['score_mid'])));
        $score_low = mysql_real_escape_string(stripslashes(trim($_POST['score_low'])));
        $score_pyramid = mysql_real_escape_string(stripslashes(trim($_POST['score_pyramid'])));
        $load_floor = mysql_real_escape_string(stripslashes(trim($_POST['load_floor'])));
        $load_player = mysql_real_escape_string(stripslashes(trim($_POST['load_player'])));
        $load_preload = mysql_real_escape_string(stripslashes(trim($_POST['load_preload'])));
        $disc_carry = mysql_real_escape_string(stripslashes(trim($_POST['carry_disks'])));
        $max_height = mysql_real_escape_string(stripslashes(trim($_POST['max_height'])));



        $query = " SELECT id FROM scout_pit_data WHERE team_id=" . $team_id;
        $result = mysql_query($query);
        $row = mysql_fetch_array($result);
        $id = $row["id"];
        if (mysql_num_rows($result) == 0) {


            $success = false;
            $query = " INSERT INTO scout_pit_data(team_id,configuration_id,wheel_type_id,wheel_base_id,autonomous_mode,scout_comments,pyramid_climb_level,";
            $query .= "score_high,score_mid,score_low,score_pyramid,load_floor,load_player,load_preload,disc_carry,max_height) ";
            $query .= "SELECT 
						" . $team_id . ",
						configuration_lu.id,
						wheel_type_lu.id,
						wheel_base_lu.id,
						" . $autonomous_mode . ",
						'" . $scout_comments . "',
						" . $pyramid_climb_level . ",
						" . $score_high . ",
						" . $score_mid . ",
						" . $score_low . ",
                                                " . $score_pyramid . ",
						" . $load_floor . ",
						" . $load_player . ",
						" . $load_preload . ",
                                                " . $disc_carry . ",
                                                " . $max_height . "
						FROM configuration_lu, wheel_type_lu, wheel_base_lu WHERE configuration_lu.configuration_desc = '" . $configuration_id . "' AND wheel_type_lu.wheel_type_desc = '" . $wheel_type_id . "' AND
						wheel_base_lu.wheel_base_desc = '" . $wheel_base_id . "'";

            $success = mysql_query($query);

            if ($success) {
                $resp = 'success';
            } else {
                $resp = 'Database Query Failed';
            }
        } else {
            $result = mysql_query("SELECT id FROM configuration_lu WHERE configuration_desc='" . $configuration_id . "'");
            $row = mysql_fetch_array($result);
            $configuration_id = $row["id"];
            $result = mysql_query("SELECT id FROM wheel_type_lu WHERE wheel_type_desc='" . $wheel_type_id . "'");
            $row = mysql_fetch_array($result);
            $wheel_type_id = $row["id"];
            $result = mysql_query("SELECT id FROM wheel_base_lu WHERE wheel_base_desc='" . $wheel_base_id . "'");
            $row = mysql_fetch_array($result);
            $wheel_base_id = $row["id"];




            $success = false;
            $query = " UPDATE scout_pit_data ";
            $query .= "SET team_id=" . $team_id . ",
						configuration_id=" . $configuration_id . ",
						wheel_type_id=" . $wheel_type_id . ",
						wheel_base_id=" . $wheel_base_id . ",
						autonomous_mode=" . $autonomous_mode . ",
						scout_comments='" . $scout_comments . "',
						pyramid_climb_level=" . $pyramid_climb_level . ",
						score_high=" . $score_high . ",
						score_mid=" . $score_mid . ",
						score_low=" . $score_low . ",
                                                score_pyramid=" . $score_pyramid . ",
						load_floor=" . $load_floor . ",
						load_player=" . $load_player . ",
						load_preload=" . $load_preload . ",
                                                disc_carry=" . $disc_carry . ",
                                                max_height=" . $max_height . "
						WHERE id=" . $id;

            $success = mysql_query($query);

            if ($success) {
                $resp = 'success';
            } else {
                $resp = 'Database Query Failed';
            }
        }
    } else if ($_POST['type'] == 'pitInfo') {
        $teamId = mysql_real_escape_string(stripslashes(trim($_POST['team_id'])));

        $query = "SELECT timestamp FROM scout_pit_data WHERE team_id=".$teamId;

        $result = mysql_query($query);
        $row = mysql_fetch_array($result);
        echo $row['timestamp'];
    } else if ($_POST['type'] == 'eventStats') {
        $event = mysql_real_escape_string(stripslashes(trim($_POST['event_name'])));

        $query = "SELECT id FROM event_lu WHERE event_name='".$event."'";
        $result = mysql_query($query);
        $row = mysql_fetch_array($result);
        $event_id = $row['id'];


        $query = "SELECT * FROM fact_match_data WHERE event_id=".$event_id;
        $result = mysql_query($query);
        header("Content-type: text/xml");
        $XML = "<?xml version=\"1.0\"?>\n";
        if ($xslt_file)
            $XML .= "<?xml-stylesheet href=\"$xslt_file\" type=\"text/xsl\" ?>";

        // root node
        $XML .= "<result table=\"fact_match_data\">\n";
        // rows
        while ($row = mysql_fetch_array($result, MYSQL_ASSOC)) {
            $XML .= "\t<row>\n";
            $i = 0;
            // cells
            foreach ($row as $cell) {
                // Escaping illegal characters - not tested actually 
                $cell = str_replace("&", "&amp;", $cell);
                $cell = str_replace("<", "&lt;", $cell);
                $cell = str_replace(">", "&gt;", $cell);
                $cell = str_replace("\"", "&quot;", $cell);
                $col_name = mysql_field_name($result, $i);
                // creates the "<tag>contents</tag>" representing the column
                $XML .= "\t\t<" . $col_name . ">" . $cell . "</" . $col_name . ">\n";
                $i++;
            }
            $XML .= "\t</row>\n";
        }
        $XML .= "</result>\n";

        // output the whole XML string
        echo $XML;
    } else if ($_POST['type']=='teamStats') {
        $team_id=mysql_real_escape_string(stripslashes(trim($_POST['team_id'])));
        $query = "SELECT * FROM scout_pit_data WHERE team_id=".$team_id;
        
        $result = mysql_query($query);
        
        
        header("Content-type: text/xml");
        $XML = "<?xml version=\"1.0\"?>\n";
        if ($xslt_file)
            $XML .= "<?xml-stylesheet href=\"$xslt_file\" type=\"text/xsl\" ?>";

        // root node
        $XML .= "<result table=\"scout_pit_data\">\n";
        // rows
        while ($row = mysql_fetch_array($result, MYSQL_ASSOC)) {
            $XML .= "\t<row>\n";
            $i = 0;
            // cells
            foreach ($row as $cell) {
                // Escaping illegal characters - not tested actually
                $col_name = mysql_field_name($result, $i);
                if ($col_name == "configuration_id") {
                    $tq = "SELECT configuration_desc FROM configuration_lu WHERE id=".$cell;
                    $r = mysql_query($tq);
                    $w = mysql_fetch_array($r);
                    $cell = $w["configuration_desc"];
                } else if ($col_name == "wheel_type_id") {
                    $tq = "SELECT wheel_type_desc FROM wheel_type_lu WHERE id=".$cell;
                    $r = mysql_query($tq);
                    $w = mysql_fetch_array($r);
                    $cell = $w["wheel_type_desc"];
                } else if ($col_name == "wheel_base_id") {
                    $tq = "SELECT wheel_base_desc FROM wheel_base_lu WHERE id=".$cell;
                    $r = mysql_query($tq);
                    $w = mysql_fetch_array($r);
                    $cell = $w["wheel_base_desc"];
                }
                $cell = str_replace("&", "&amp;", $cell);
                $cell = str_replace("<", "&lt;", $cell);
                $cell = str_replace(">", "&gt;", $cell);
                $cell = str_replace("\"", "&quot;", $cell);
                // creates the "<tag>contents</tag>" representing the column
                $XML .= "\t\t<" . $col_name . ">" . $cell . "</" . $col_name . ">\n";
                $i++;
            }
            $XML .= "\t</row>\n";
        }
        $XML .= "</result>\n";

        
        $query="SELECT * FROM fact_match_data WHERE team_id=".$team_id;
        $result=  mysql_query($query);
        
        // root node
        $XML .= "<result table=\"fact_match_data\">\n";
        // rows
        while ($row = mysql_fetch_array($result, MYSQL_ASSOC)) {
            $XML .= "\t<row>\n";
            $i = 0;
            // cells
            foreach ($row as $cell) {
                // Escaping illegal characters - not tested actually 
                $col_name = mysql_field_name($result, $i);
                if ($col_name == "event_id") {
                    $tq = "SELECT event_name FROM event_lu WHERE id=".$cell;
                    $r = mysql_query($tq);
                    $w = mysql_fetch_array($r);
                    $cell = $w["event_name"];
                }
                $cell = str_replace("&", "&amp;", $cell);
                $cell = str_replace("<", "&lt;", $cell);
                $cell = str_replace(">", "&gt;", $cell);
                $cell = str_replace("\"", "&quot;", $cell);
                // creates the "<tag>contents</tag>" representing the column
                $XML .= "\t\t<" . $col_name . ">" . $cell . "</" . $col_name . ">\n";
                $i++;
            }
            $XML .= "\t</row>\n";
        }
        $XML .= "</result>\n";
        
        
        // output the whole XML string
        echo $XML;
        
        
    } else if ($_POST['type'] == 'matchStats') {
        
        $event = mysql_real_escape_string(stripslashes(trim($_POST['event_name'])));

        $query = "SELECT id FROM event_lu WHERE event_name='".$event."'";
        $result = mysql_query($query);
        $row = mysql_fetch_array($result);
        $event_id = $row['id'];
        
        $match = mysql_real_escape_string(stripslashes(trim($_POST['match_id'])));


        $query = "SELECT * FROM fact_match_data WHERE match_id=".$match." AND event_id=".$event_id;
        $result = mysql_query($query);
        header("Content-type: text/xml");
        $XML = "<?xml version=\"1.0\"?>\n";
        if ($xslt_file)
            $XML .= "<?xml-stylesheet href=\"$xslt_file\" type=\"text/xsl\" ?>";

        // root node
        $XML .= "<result table=\"fact_match_data\">\n";
        // rows
        while ($row = mysql_fetch_array($result, MYSQL_ASSOC)) {
            $XML .= "\t<row>\n";
            $i = 0;
            // cells
            foreach ($row as $cell) {
                // Escaping illegal characters - not tested actually 
                $cell = str_replace("&", "&amp;", $cell);
                $cell = str_replace("<", "&lt;", $cell);
                $cell = str_replace(">", "&gt;", $cell);
                $cell = str_replace("\"", "&quot;", $cell);
                $col_name = mysql_field_name($result, $i);
                // creates the "<tag>contents</tag>" representing the column
                $XML .= "\t\t<" . $col_name . ">" . $cell . "</" . $col_name . ">\n";
                $i++;
            }
            $XML .= "\t</row>\n";
        }
        $XML .= "</result>\n";

        // output the whole XML string
        echo $XML;
    } else {
        $resp = 'invalid submission type';
    }




    echo $resp;
}
?>