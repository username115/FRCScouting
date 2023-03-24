<?php
if(!defined('INCLUDE_CHECK')) {die('You are not allowed to execute this file directly');}



$db_host		= 'localhost';
$db_user		= ''; //set to your database username
$db_pass		= ''; //set to your database password
$db_database	= ''; //set to whatever you name your scouting database
$php_home       = ''; //set to your php (where the PEAR HTTP directory lies, only required for update_event_list.php)

/* End config */

function mysqli_field_name($result, $field_offset)
{
	$properties = mysqli_fetch_field_direct($result, $field_offset);
	return is_object($properties) ? $properties->name : null;
}


$link = mysqli_connect($db_host,$db_user,$db_pass, $db_database) or die('Unable to establish a DB connection');

mysqli_query($link,"SET names UTF8");

$pass=''; //set to a password. It will be the same password for all of your users.
//This is just a scouting app, and does not require high security. All inserted data is escaped, so nothing should get in.

$ver='2.2023.1.2';

