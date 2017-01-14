#! /usr/bin/python

_description = '''
This script take in a SQL file with INSERTS and CREATES and transforms
	it into a struct in Java. Meant to be used with a phpmyadmin
	exported sql file. Defaults assume the FRC 836 file structure.
'''

_defaultRun = '''
	python StatsStructGen.py
		--packagename=org.frc836.database
		--classname=MatchStatsStruct
		--tablename=fact_match_data_2017
		--infile=FRC_Scouting_Server/scouting.sql
		--outfile=app/src/main/java/org/frc836/database/MatchStatsStruct.java
'''
__author__ = "Dan"
__version__ = "1.0"
__copyright__ = ""

import SQLHelper
import autogeninfo

import os
import re
import argparse

# note to self. Look into the 'textwrap' class for functionality



class SqlToJavaStruct():
	re_GetSqlVar = re.compile(r"[`](?P<var>\w+)[`]")
	re_CreateStatement = re.compile(r'''
			\s* CREATE \s+ TABLE \s+ IF \s+ NOT \s+ EXISTS \s+    # grabs the create statement
			[`] (?P<tablename>\w+) [`]                            # matches the table name
			(?P<body>[^;]+)                                       # matches the body
			[;]
		''',re.VERBOSE)
	re_InsertStatement = re.compile(r'''
			\s* INSERT \s+ INTO \s+       # finds the insert statements
			[`] (?P<tablename>\w+) [`]     # matches the tablename
			\s+ [(] \s*
			(?P<colnames>[^)]+)
			[)] \s* VALUES [^(]*
			(?P<body>[^;]+) [;]
		''',re.VERBOSE)
	re_GetColumn = re.compile(r'''
			(^|\n) \s+
			[`] (?P<name>\w+) [`]     # grabs the column name
			\s+ (?P<type>\S+) \s+     # grabs the type
		''',re.VERBOSE)
	re_GetRow = re.compile(r'''
			[(]
			(?P<row>.+)
			[)]
			#[(] (?P<row>[^)]+) [)]   # matches everything in parens
		''',re.VERBOSE)
	def __init__(self, packageName=None, className="DefaultJavaClassName",
					tableName=None):
		self.tables = list()
		self.packageName = packageName
		self.className = className
		self.tableName = tableName
	def findTableName(self, tableName):
		for i in range(0, len(self.tables)):
			if tableName == self.tables[i].name:
				return i
		return None
	def addTable(self, table):
		self.tables.append(table)
		
	def createStr_Header(self):
		_myscriptname = os.path.basename(__file__)
		ret = "/*\n"
		ret += autogeninfo._autogenScriptInfo_Str(__version__, _myscriptname) +"\n"
		ret += "*/\n\n"
		ret += "package "+ self.packageName +";\n"
		ret += "\n"
		ret += "import android.content.ContentValues;\n"
		ret += "import android.database.Cursor;\n"
		ret += "import android.database.sqlite.SQLiteDatabase;\n"
		ret += "import org.frc836.database.DB;\n"
		ret += "import org.frc836.database.FRCScoutingContract." + self.tableName.upper() + "_Entry;\n"
		ret += "import org.json.JSONException;\n"
		ret += "import org.json.JSONObject;\n"
		ret += "import java.util.Date;\n"
		ret += "import java.util.ArrayList;\n"
		ret += "import java.util.Arrays;\n"
		ret += "import java.util.List;\n\n"
		ret += "public class "+ self.className + " {\n"
		return ret
	def createStr_Footer(self):
		ret = "}"
		return ret

	def createStr_Variables(self):
		ret = ""
		tableindex = self.findTableName(self.tableName)
		if tableindex:
			for column in self.tables[tableindex].columns:
				if not 'id'== column.name and not 'timestamp'==column.name and not 'invalid'==column.name:
					column.toJavaType()
					ret += "public " + column.type + " " + column.name + ";\n"
		return ret

	def createStr_Constants(self):
		ret = ""
		ret += "public static final String TABLE_NAME = " + self.tableName.upper() + "_Entry.TABLE_NAME;\n"
		tableindex = self.findTableName(self.tableName)
		if tableindex:
			for column in self.tables[tableindex].columns:
				ret += "public static final String COLUMN_NAME_" + column.name.upper() + " = " + self.tableName.upper() + "_Entry.COLUMN_NAME_" + column.name.upper() + ";\n"

		return ret

	def createStr_Init(self):
		this.event = None
		this.team = None
		this.match = None
		this.practice = None

		ret = ""
		ret += "public "+ self.className +"() {\n\tinit();\n}\n"
		ret += "\n"
		ret += "public void init() {\n"
		tableindex = self.findTableName(self.tableName)
		if tableindex:
			for column in self.tables[tableindex].columns:
				if not 'id'== column.name and not 'timestamp'==column.name and not 'invalid'==column.name:
					ret += "\t" + column.name + " = "
					if (column.type == 'boolean'):
						ret += "false;"
					elif (column.type == 'String'):
						ret += '"";'
					else:
						ret += "0;"
					ret += "\n"
				if (re.search('practice',column.name)):
					practice = column.name
				elif (re.search('match',column.name)):
					match = column.name
				elif (re.search('event',column.name)):
					event = column.name
				elif (re.search('team',column.name)):
					team = column.name
				

		ret += "}\n"

		if match and event and team:
			ret += "\n"
			ret += "public " + self.className +"(int team, String event, int match) {\n\tinit();\n"
			ret += "\tthis." + team + " = team;\n"
			ret += "\tthis." + event + " = event;\n"
			ret += "\tthis." + match + " = match;\n"
			ret += "}"
			if practice:
				ret += "\n\n"
				ret += "public " + self.className +"(int team, String event, int match, boolean practice) {\n\tinit();\n"
				ret += "\tthis." + team + " = team;\n"
				ret += "\tthis." + event + " = event;\n"
				ret += "\tthis." + match + " = match;\n"
				ret += "\tthis." + practice + " = practice;\n"
				ret += "}"
		return ret

	def createStr_getValues(self):
		ret = ""
		#TODO
		return ret

	def createStr_JavaStruct(self):
		s = ""
		s += self.createStr_Header() + "\n"
		s += "\n"
		s += SQLHelper.indent(self.createStr_Variables()) + "\n"
		s += "\n"
		s += SQLHelper.indent(self.createStr_Constants()) + "\n"
		s += "\n"
		s += SQLHelper.indent(self.createStr_Init()) + "\n"
		s += "\n"
		s += SQLHelper.indent(self.createStr_getValues()) + "\n"
		s += "\n"
		#TODO finish this section
		s += self.createStr_Footer()
		return s
		
	def _parseStatement_Create(self, statement):
		match = self.re_CreateStatement.search(statement)
		if match:
			table = SQLHelper.SqlTable( match.group('tablename') )
			for ln in match.group('body').split(','):
				match = self.re_GetColumn.search(ln)
				if match:
					name = match.group('name')
					type = match.group('type')
					
					if re.search("unsigned",ln): unsigned = True
					else: unsigned = False
						
					if re.search("NOT NULL",ln): nullVal = False
					else: nullVal = False
						
					if re.search("AUTO_INCREMENT",ln): autoInc = True
					else: autoInc = False
					
					match = re.search("DEFAULT\s+(?P<val>\S+)",ln)
					if match: default = match.group('val')
					else: default=None
					
					table.addColumn( SQLHelper.SqlColumn(columnName=name, columnType=type,
									isPrimary=False, defaultVal=default,
									nullValid=nullVal, autoIncrement=autoInc,
									isUnsigned=unsigned) )
					
				if re.search("PRIMARY\s+KEY",ln):
					primaryKey = re.search("PRIMARY\s+KEY\s+[(][`](?P<key>\w+)[`][)]",ln).group('key')
					for column in table.columns:
						if column.name == primaryKey:
							column.primary = True
					
			self.addTable(table)
	def _parseStatement_Insert(self, statement):
		match = self.re_InsertStatement.search(statement)
		if match:
			tableName = match.group('tablename')
			colNames = match.group('colnames')
			body = match.group('body')
			i_table = self.findTableName(tableName)
			mapping = self.tables[i_table].getColumnMapping_csv(colNames)
			for row in self.re_GetRow.findall( body ):
				self.tables[i_table].addRow(row)

	def readFile(self, filename, verbose=False):
		f = open(filename,'r')
		if verbose: print("Reading from \'"+ str(f.name) +"\' in mode \'"+ str(f.mode) +"\'")
		for ln in f.read().split(';'):
			ln += ';'
			if self.re_CreateStatement.search(ln):
				self._parseStatement_Create(ln)
			elif self.re_InsertStatement.search(ln):
				self._parseStatement_Insert(ln)
		f.close()
	def writeJavaStruct(self, filename, verbose=False):
		directory = os.path.dirname(filename)
		if not os.path.exists(directory):
			if verbose: print("Creating output directory: " + directory)
			os.makedirs(directory)
		f = open(filename,'w')
		if verbose: print("Writing to \'"+ str(f.name) +"\' in mode \'"+ str(f.mode) +"\'")
		f.write( self.createStr_JavaStruct() )
		f.close()

#===============================================================================
# init_args()
#	Sets up the command line parsing logic. Any changes to cmd line input should
#	take place here.
#	------------------------------------------
# return
#	args : the list of parsed arguments
#===============================================================================
def init_args():
	parser = argparse.ArgumentParser(description=_description)

	parser.add_argument('-i','--infile',dest='infilename',required=False,
		help='The .sql file that you want to parse from')
	parser.add_argument('-o','--outfile',dest='outfilename',required=False,
		help='The Java file you want to write out to')
	parser.add_argument('--classname','-cn',required=False,
		help='The name of the Java class')
	parser.add_argument('--packagename','-pn',required=False,
		help='The database package to use')
	parser.add_argument('--tablename','-tbl',required=False,
		help='The table to implement this struct for')

	parser.set_defaults( infilename='FRC_Scouting_Server/scouting.sql',
							outfilename='app/src/main/java/org/frc836/database/MatchStatsStruct.java',
							packagename='org.frc836.database',
							classname='MatchStatsStruct',
							tablename='fact_match_data_2017'
						)

	args = parser.parse_args()
	return args

if __name__ == "__main__":
	args = init_args()
	
	SqlCreator = SqlToJavaStruct(packageName = args.packagename,
							className = args.classname,
							tableName = args.tablename
							)
	SqlCreator.readFile(args.infilename, verbose=True)
	SqlCreator.writeJavaStruct(args.outfilename, verbose=True)
