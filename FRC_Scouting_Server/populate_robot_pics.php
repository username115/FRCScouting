<?php

define('INCLUDE_CHECK', true);

include('scouting-header.php');

$dir=''; //populate with the directory on the server that pictures are stored in

$url=''; //populate with the url that leads to that same directory (begin with http:// or https://)

if ($handle = opendir($dir)) {
    echo "Entries:<br>";


    while (false !== ($entry = readdir($handle))) {
        
        $pos = strrpos($entry, '.');
        $team = substr($entry, 0, $pos);
        
        if (is_numeric($team)) {
            echo "$entry<br>";
            
            $query = "SELECT id FROM robot_lu WHERE team_id=".$team;
            $result = mysqli_query($query);
            $row = mysqli_fetch_array($result);
            $id = $row["id"];
            if (mysqli_num_rows($result) == 0) {
                $query = 'INSERT INTO robot_lu(team_id, robot_photo) VALUES ('.$team.',"'.$url.$entry.'")';
                $result = mysqli_query($query);
            } else {
                $query = 'UPDATE robot_lu SET robot_photo="'.$url.$entry.'" WHERE id = '.$id;
                $result = mysqli_query($query);
            }
        }
        
    }

    closedir($handle);
}

?>
