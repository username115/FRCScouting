if __name__ != '__main__':
	print("Must run '{}' as '__main__'. Tried to run as '{}'. ..exiting..".format(__file__, __name__))
	exit()


from sys import stdout
import logging

from mysql_codec import decode
with open('scouting_2016.sql', 'r') as fin:
	tables = decode(fin)


from scouting_php__writter import write_php

post_tables = [
	('match', tables['fact_match_data_2016'], ['event_id','match_id','team_id','practice_match'])
]
write_php(stdout, tables, post_tables)
