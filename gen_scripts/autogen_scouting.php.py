if __name__ != '__main__':
	print("Must run '{}' as '__main__'. Tried to run as '{}'. ..exiting..".format(__file__, __name__))
	exit()

import argparse
parser = argparse.ArgumentParser(description='Output server files based on sql')
parser.add_argument("-l", "--legacy", help="use legacy mysql syntax", action="store_true")
parser.add_argument("-m", "--match_table", help="match table name", required=True)
parser.add_argument("-p", "--pit_table", help="Pits table name", required=True)
parser.add_argument("-s", "--superscout_table", help="Superscout table name", required=True)
parser.add_argument("-o", "--out", help="Output folder relative to project root", required=True)

args = parser.parse_args()
if args.legacy:
	mysqli = False
else:
	mysqli = True

# Setup a logger
import logging
log = logging.getLogger('autogenerator')
log.setLevel(logging.DEBUG)

# Setup the logger outputs
log_console_handler = logging.StreamHandler()
log_console_handler.setLevel(logging.DEBUG)
log.addHandler(log_console_handler)

# get the location of the root path of the project. Combination of this script path and the relative path.
from os.path import dirname, realpath, normpath
ProjRootPath = normpath( dirname(realpath(__file__)) + '/..')


from sys import stdout
import logging

from mysql_codec import decode
mysqlfile = normpath("{}/FRC_Scouting_Server/scouting.sql".format(ProjRootPath) )
with open(mysqlfile, 'r') as fin:
	log.info("Opened input file '{}' for parsing".format(mysqlfile))
	tables = decode(fin)


from scouting_php__writer import write_php
outfile = normpath("{}/{}/scouting.php".format(ProjRootPath, args.out))
post_tables = [
	('match', tables[args.match_table], ['event_id','match_id','team_id','practice_match']),
	('superscout', tables[args.superscout_table], ['event_id','match_id','team_id','practice_match']),
	('pits', tables[args.pit_table], ['team_id']),
	('picklist', tables['picklist'], ['event_id','team_id'])
]

with open(outfile, 'w') as f:
	write_php(f, mysqli, tables, post_tables)
	log.info("Wrote file '{}'".format(outfile))
