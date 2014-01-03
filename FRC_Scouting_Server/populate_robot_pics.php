<?php

define('INCLUDE_CHECK', true);

include('scouting-header.php');

$dir=''; //populate with the directory on the server that pictures are stored in

$url=''; //populate with the url that leads to that same directory

if ($handle = opendir($dir)) {
    echo "Entries:<br>";


    while (false !== ($entry = readdir($handle))) {
        
        $pos = strrpos($entry, '.');
        $team = substr($entry, 0, $pos);
        
        if (is_numeric($team)) {
            echo "$entry<br>";
            
            $query = "SELECT id FROM robot_lu WHERE team_id=".$team;
            $result = mysql_query($query);
            $row = mysql_fetch_array($result);
            $id = $row["id"];
            if (mysql_num_rows($result) == 0) {
                $query = 'INSERT INTO robot_lu(team_id, robot_photo) VALUES ('.$team.',"'.$url.$entry.'")';
                $result = mysql_query($query);
            } else {
                $query = 'UPDATE robot_lu SET robot_photo="'.$url.$entry.'" WHERE id = '.$id;
                $result = mysql_query($query);
            }
        }
        
    }

    closedir($handle);
}

?>
