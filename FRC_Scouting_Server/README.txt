This web-side application requires a LAMP Server (or equivalent) to be installed on your server. 
It uses mysql calls, and the interface is written in php.

To install:
1. 	Use your preferred sql tool to import the scouting.sql database into your sql server.
	You can name the database whatever you like, but the table structure must remain intact.

2. 	Edit scouting-header.php to add you database name (that you imported in step 1), 
	your database username and password, and set a password for interfacing with the server.
	
3.	Put scouting-header.php and scouting.php into a directory on your webserver.

4.	When you first load the FRC Scouting app, it will prompt you to set a web url to interface with.
	Set the Server Address to point to the scouting.php file (eg. robobees.org/scouting.php).
	Set the password to what you changed $pass to in scouting-header.php
	
5.	If successful, The app will inform you that the password was confirmed.


To use the populate_robot_pics.php script (optional):
1.	Place all robot pictures into a single folder on your server, accessible via http requests.
	Name all pictures <team#>.<ext> (eg: 836.jpg)

2.	Edit the populate_robot_pics.php script to use the directory that these pictures are in.

3.	Place the populate_robot_pics.php into the same directory as your scouting-header.php.

4.	Run the script (either locally with php or with a web browser by visiting the php page.

5.	Store the script somewhere not accessible by the public.


Database Structure:
-configuration_lu: this table is for robot configuration (pit scouting). Later versions of the app will pull from this table when pit scouting.

-event_lu: this table stores the names of all events. Adding or removing entries from this table will change what events appear in the event list in the app.
		The "match_url" column stores the url to the match schedule posted by FIRST for each event.
		
-fact_cycle_data: New to 2014. This table stores the data on each individual cycle for a match. There will be multiple
		entries in this table for each entry in fact_match_data.
		
-fact_match_data: this table stores all data recorded during match scouting.

-notes_options: this table stores the options presented to the scouter in the "Common Notes" dropdown in match scouting.
		Adding or removing rows in this table will change what options are presented.
		
-robot_lu: this table stores the urls to pictures of each robot. team_id is the team number. Add a row to this table for each robot picture you have.
		Picture urls stored here will be loaded prior to each match.
		
-scout_pit_data: this table stores all data recorded during pit scouting. One row per team.

-wheel_base_lu: this table is for robot wheel base (pit scouting). Later versions of the app will pull from this table when pit scouting.

-wheel_type_lu: this table is for robot wheel type (pit scouting). Later versions of the app will pull from this table when pit scouting.

