if __name__ != '__main__':
	print("Must run '{}' as '__main__'. Tried to run as '{}'. ..exiting..".format(__file__, __name__))
	exit()

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
outfile = normpath("{}/FRC_Scouting_Server/scouting.php".format(ProjRootPath))
post_tables = [
	('match', tables['fact_match_data_2018'], ['event_id','match_id','team_id','practice_match']),
	('pits', tables['scout_pit_data_2018'], ['team_id']),
	('picklist', tables['picklist'], ['event_id','team_id'])
]

with open(outfile, 'w') as f:
	write_php(f, tables, post_tables)
	log.info("Wrote file '{}'".format(outfile))
