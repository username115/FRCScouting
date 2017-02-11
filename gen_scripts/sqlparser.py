_description = '''
Purpose is to take a .sql file and turn it into some python structs. Serves as the parser
class for the sql generated scripts (ie the java, php).
'''

from collections import OrderedDict

class SqlColumn:
	def __init__(self, name, type
		, isPrimary = False
		, default = None
		, isNotNull = False
		, autoIncrement = False
		):
		vars(self).update({k:v for k,v in locals().items() if k !='self'})

	def __repr__(self):
		return ', '.join(["{}({})".format(k,v) for k,v in vars(self).items() if v])


class SqlTable:
	def __init__(self, name):
		self.name = str(name)
		self.columns = OrderedDict()



import re

re_CreateTable = re.compile(r'''
	\s* CREATE \s+ TABLE \s+ IF \s+ NOT \s+ EXISTS \s+    # grabs the create statement
	[`] (?P<tablename>\w+) [`]                            # matches the table name
	\s* \(
	(?P<body>[^;]+)                                       # matches the body
	\s* \) \s*
	(?P<footer>[^;]*)                                     # matches the statement at the end
	\s* [;]
	''',re.VERBOSE)

def parse_CreateTable(instr):
	m = re_CreateTable.search(instr)
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



def read_sql(io):
	for x in io.read().split(';'):
		yield x + ';'


def parse_sql(io, verbose=False):
	tables = dict()
	for statement in read_sql(io):
		if re_CreateTable.search(statement):
			table = parse_CreateTable(statement)
			tables[table.name] = table
	return tables