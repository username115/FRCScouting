<?php

define('INCLUDE_CHECK', true);
include('scouting-header.php');
ini_set("include_path", $php_home . ':' . ini_get("include_path")  );
require_once 'HTTP/Request2.php'; //Requires the Request2 PEAR library
/* 
 * Copyright 2015 Daniel Logan.
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

$token_string = ""; //token string


$frc_api_url = "https://frc-api.usfirst.org";

$token = base64_encode($token_string);
$season = $_GET['season'];
$truncate = $_GET['truncate'];
//$season = NULL;
if (strlen($season) === 0) {
    $season = date("Y"); //use current year if none specified
}
try {
    $headers = array("Content-type"=>"application/x-www-form-urlencoded", "Accept"=>"application/json", "Authorization"=>"Basic " . $token);


    $request = new HTTP_Request2($frc_api_url . '/api/v1.0/events/' . $season, HTTP_Request2::METHOD_GET);
    $request->setHeader($headers);
    
    $request->setAdapter('curl');

    $response = $request->send();

    if (200 == $response->getStatus()) {
        $json = $response->getBody();
            
        $values = json_decode($json, true);
        
        $eventCount = $values["eventCount"];
        $events = $values["Events"];
        
        $return = true;
        
        
        foreach($events as $event) {
            $event_name = mysql_real_escape_string(stripslashes(trim($event["name"])));
            $event_code = mysql_real_escape_string(stripslashes(trim($event["code"])));
            $date_start = mysql_real_escape_string(stripslashes(trim($event["dateStart"])));
            
            $query = "SELECT id FROM event_lu WHERE event_code='" . $event_code . "'";
            $result = mysql_query($query);
            $row = mysql_fetch_array($result);
            $id = $row["id"];
            if (mysql_num_rows($result) == 0) { //insert
                $success = false;
                $query = "INSERT INTO event_lu(event_name,event_code,date_start,invalid) VALUES('"
                    . $event_name . "','"
                    . $event_code . "','"
                    . $date_start . "',0);";
                $success = mysql_query($query);
            }
            else { //update
                $success = false;
                $query = "UPDATE event_lu SET event_name='" . $event_name 
                        . "',event_code='" . $event_code 
                        . "',date_start='" . $date_start
                        . "',invalid=0 WHERE id=" . $id;
                $success = mysql_query($query);
            }
            if ($success != true) {
                $return = false;
                //echo $query;
            }
        }
        if ($return) {
            echo "success";
        }
        else {
            echo "failed to update event list";
        }
        
    } else {
        echo 'Unexpected HTTP status from FIRST API: ' . $response->getStatus() . ' ' .
             $response->getReasonPhrase();
    }
} catch (HTTP_Request2_Exception $e) {
    echo 'Error';//.': ' . $e->getMessage();
}


