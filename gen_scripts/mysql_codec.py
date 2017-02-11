from sqlparser import SqlColumn, SqlTable

import re

###### CREATE TABLE ######

_re_CreateTable = re.compile(r'''
	\s* CREATE \s+ TABLE \s+ IF \s+ NOT \s+ EXISTS \s+    # grabs the create statement
	[`] (?P<tablename>\w+) [`]                            # matches the table name
	\s* \(
	(?P<body>[^;]+)                                       # matches the body
	\s* \) \s*
	(?P<footer>[^;]*)                                     # matches the statement at the end
	\s* [;]
	''',re.VERBOSE)

def _parse_CreateTable(instr):
	m = _re_CreateTable.search(instr)
	table = SqlTable(m.group('tablename'))
	coldef = table.columns

	for x in [x.strip() for x in m.group('body').split('\n') if x and not x.isspace()]:

		if x.startswith( ('KEY','UNIQUE','PRIMARY')):
			m = re.search("(?:(\w+)\s+)?KEY (?:`(\w+)`\s+)?\((`[^)]+`,?)\)", x)
			keytype,keyname,keycolumns = m.groups()
			keycolumns = [x.strip('`') for x in keycolumns.split(',')]
			if 'PRIMARY' == keytype:
				for c in keycolumns: coldef[c].isPrimary = True
			continue

		m = re.search("`(?P<Name>\w+)`\s* (\w+)", x)
		name, type = m.group(1), m.group(2)
		coldef[name] = SqlColumn(name,type)

		if "unsigned" in x: coldef[name].type = "unsigned {}".format(coldef[name].type)
		if "NOT NULL" in x: coldef[name].isNotNull = True
		if "AUTO_INCREMENT" in x: coldef[name].autoIncrement = True
		m = re.search("DEFAULT\s+(\w+)", x)
		if m: coldef[name].default = m.group(1)

	return table




def _each_command(io):
	for x in io.read().split(';'):
		yield x + ';'


def decode(io):
	tables = dict()
	for statement in _each_command(io):
		if _re_CreateTable.search(statement):
			table = _parse_CreateTable(statement)
			tables[table.name] = table
	return tables