<?php
if(!defined('INCLUDE_CHECK')) {die('You are not allowed to execute this file directly');}



$db_host		= 'localhost';
$db_user		= ''; //set to your database username
$db_pass		= ''; //set to your database password
$db_database	= ''; //set to whatever you name your scouting database
$php_home       = ''; //set to your php (where the PEAR HTTP directory lies, only required for update_event_list.php)

/* End config */



$link = mysql_connect($db_host,$db_user,$db_pass) or die('Unable to establish a DB connection');

mysql_select_db($db_database,$link);
mysql_query("SET names UTF8");

$pass=''; //set to a password. It will be the same password for all of your users.
//This is just a scouting app, and does not require high security. All inserted data is escaped, so nothing should get in.

$ver='2.2017.0.3';

