from sqlparser import SqlColumn, SqlTable


def _post_msg(io, post_type, table, querylist=set()):
	columns = table.columns

	cols2update = [v for k,v in columns.items() if k not in ['id','timestamp'] ]

	io.write("\telse if ($_POST['type'] == '{}') {{\n".format(post_type))

	# Define the variables that we will be using
	for col in filter(lambda x: x not in ['id','timestamp','invalid'], columns.keys()):
		io.write("\t\t${0} = mysql_real_escape_string(stripslashes(trim(isset($_POST['{0}']) ? $_POST['{0}'] : '0'));\n".format(col))

	io.write('\n\t\t$result = mysql_query("SELECT id FROM {0}'.format(table.name))
	if querylist:
		itr = iter(querylist)
		io.write(' WHERE {0}=" . ${0}'.format(next(itr)) )
		for col in itr: io.write(' . " AND {0}=" . ${0}'.format(col))
	io.write(');\n')

	io.write(
r'''
		$row = mysql_fetch_array($result);
		$match_row_id = $row["id"];

		if (mysql_num_rows($result) == 0) {

''')

	io.write('\t\t\t$query = "INSERT INTO {}({}) VALUES("\n'.format(table.name, ','.join([x.name for x in cols2update])) )

	for col in cols2update:
		if col.name == 'invalid':
			io.write('\t\t\t\t. "0);";') # This is for the invalid flag

		elif col.type in ['text']:
			io.write('{}. "\'" . ${} . "\',"\n'.format('\t\t\t\t', col.name))

		else:
			io.write('{}. ${} . ","\n'.format('\t\t\t\t', col.name))

	io.write('\n\t\t\t$success = mysql_query($query);\n\t\t}\n')

	io.write('\t\telse {{\n\t\t\t$query = "UPDATE {} SET "\n'.format(table.name))

	tstr = ''
	for col in cols2update:
		if col.name == 'invalid':
			io.write( '\t\t\t\t. "invalid=0"\n' )
		elif 'text' == col.type:
			io.write( '\t\t\t\t. "{0}=\'" . ${0} . "\',"\n'.format(col.name) )
		else:
			io.write( '\t\t\t\t. "{0}=" . ${0} . ","\n'.format(col.name) )

	io.write('\t\t\t\t. " WHERE id=" . $match_row_id;\n\n')
	io.write('\t\t\t$success = mysql_query($query);\n\t\t}\n')

	io.write('\t\tif ($success) {\n')

	io.write('\t\t\t$result = mysql_query("SELECT id, timestamp FROM {0}'.format(table.name))
	if querylist:
		itr = iter(querylist)
		io.write(' WHERE {0}=" . ${0}'.format(next(itr)) )
		for col in itr: io.write(' . " AND {0}=" . ${0}'.format(col))
	io.write(');\n')

	io.write('\t\t\t$row = mysql_fetch_array($result);\n')
	io.write('\t\t\t$resp = $row["id"] . "," . strtotime($row["timestamp"]);\n')

	io.write('\t\t} else {\n')
	io.write("\t\t\t$resp = 'Database Query Failed : \\n' . $query;\n")
	io.write('\t\t}\n')

	io.write('\t}\n')



def write_php(io, tables = {}, post_tables=[]):

	io.write(
r'''<?php

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
''')

	io.write(
r'''
header($_SERVER['SERVER_PROTOCOL'] . ' 403 Forbidden');
define('INCLUDE_CHECK', true);
include('scouting-header.php');
''')

	io.write(
r'''
function checkVersion($client_version, $server_version) {
	$cver = substr(trim($client_version), 0, strrchr(trim($client_version), '.'));
	$sver = substr(trim($server_version), 0, strrchr(trim($server_version), '.'));
	return strcasecmp($cver, $sver) == 0;
}
''')

	io.write(
r'''
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
''')

	io.write(
r'''
if ($_POST['type'] == 'passConfirm' && $_POST['password'] == $pass) {
	header($_SERVER['SERVER_PROTOCOL'] . ' 200 OK');
	echo 'success';
}
elseif ($_POST['type'] == 'versioncheck') {
	header($_SERVER['SERVER_PROTOCOL'] . ' 200 OK');
	echo $ver;
}
''')

	io.write(
r'''
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
''')
	cnt = 0
	for tablename, table in tables.items():
		cnt = cnt+1
		if cnt == len(tables):
			sep = "}"
		else:
			sep = ","
		io.write(
r'''
		//{0}
		$query = "SELECT * FROM {0}" . $suffix;
		$result = mysql_query($query);
		$json .= genJSON($result, "{0}") . "{1}";
		mysql_free_result($result);
'''.format(tablename, sep))

	io.write(
r'''
		$resp = $json;
	}
	else if ($verMatch == false) {
		$resp = 'Version Mismatch, server using version ' . $ver;
	}
''')

	for post,table,querylist in post_tables:
		_post_msg(io, post, table, querylist)

	io.write(
'''
	else {
		$resp = 'invalid submission type';
	}

	echo $resp;
}
''')


def write(io, tables, post_tables):
	write_php(io, tables, post_tables)