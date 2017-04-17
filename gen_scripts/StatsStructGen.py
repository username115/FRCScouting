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
__version__ = "1.1"
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
		ret += "import org.frc836.database.FRCScoutingContract." + self.tableName.upper() + "_Entry;\n"
		ret += "import org.json.JSONException;\n"
		ret += "import org.json.JSONObject;\n"
		ret += "import java.util.Date;\n"
		ret += "import java.util.ArrayList;\n"
		ret += "import java.util.LinkedHashMap;\n"
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
		self.event = None
		self.team = None
		self.match = None
		self.practice = None
		self.position = None
		self.config = None
		self.wheel_base = None
		self.wheel_type = None

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
						if re.search('position',column.name):
							ret += '"Red 1";'
						elif re.search('config',column.name) or re.search('wheel_base',column.name) or re.search('wheel_type',column.name):
							ret += '"other";'
						else:
							ret += '"";'
					else:
						ret += "0;"
					ret += "\n"
				if (re.search('practice',column.name)):
					self.practice = column.name
				elif (re.search('match',column.name)):
					self.match = column.name
				elif (re.search('event',column.name)):
					self.event = column.name
				elif (re.search('team',column.name)):
					self.team = column.name
				elif (re.search('position',column.name)):
					self.position = column.name
				elif (re.search('config',column.name)):
					self.config = column.name
				elif (re.search('wheel_base',column.name)):
					self.wheel_base = column.name
				elif (re.search('wheel_type',column.name)):
					self.wheel_type = column.name
				

		ret += "}\n"

		if self.match and self.event and self.team:
			ret += "\n"
			ret += "public " + self.className +"(int team, String event, int match) {\n\tinit();\n"
			ret += "\tthis." + self.team + " = team;\n"
			ret += "\tthis." + self.event + " = event;\n"
			ret += "\tthis." + self.match + " = match;\n"
			ret += "}"
			if self.practice:
				ret += "\n\n"
				ret += "public " + self.className +"(int team, String event, int match, boolean practice) {\n\tinit();\n"
				ret += "\tthis." + self.team + " = team;\n"
				ret += "\tthis." + self.event + " = event;\n"
				ret += "\tthis." + self.match + " = match;\n"
				ret += "\tthis." + self.practice + " = practice;\n"
				ret += "}"
		return ret

	def createStr_getValues(self):
		ret = "public ContentValues getValues(DB db, SQLiteDatabase database) {\n"
		ret += "\tContentValues vals = new ContentValues();\n"

		tableindex = self.findTableName(self.tableName)
		if tableindex:
			if self.event:
				ret += "\tlong ev = db.getEventIDFromName(" + self.event + ", database);\n"


			for column in self.tables[tableindex].columns:
				if 'id'==column.name and self.event:
					ret += "\tvals.put(COLUMN_NAME_" + column.name.upper() + ", ev * 10000000 + match_id * 10000 + team_id);\n"
				elif 'id'==column.name:
					ret += "\tvals.put(COLUMN_NAME_" + column.name.upper() + ", team_id * team_id);\n"
				elif self.event==column.name:
					ret += "\tvals.put(COLUMN_NAME_" + column.name.upper() + ", ev);\n"
				elif self.position==column.name:
					ret += "\tvals.put(COLUMN_NAME_" + column.name.upper() + ", db.getPosIDFromName(" + column.name + ", database));\n"
				elif self.config==column.name:
					ret += "\tvals.put(COLUMN_NAME_" + column.name.upper() + ", db.getConfigIDFromName(" + column.name + ", database));\n"
				elif self.wheel_type==column.name:
					ret += "\tvals.put(COLUMN_NAME_" + column.name.upper() + ", db.getWheelTypeIDFromName(" + column.name + ", database));\n"
				elif self.wheel_base==column.name:
					ret += "\tvals.put(COLUMN_NAME_" + column.name.upper() + ", db.getWheelBaseIDFromName(" + column.name + ", database));\n"
				elif "invalid"==column.name:
					ret += "\tvals.put(COLUMN_NAME_" + column.name.upper() + ", 1);\n"
				elif not 'timestamp'==column.name:
					ret += "\tvals.put(COLUMN_NAME_" + column.name.upper() + ", " + column.name
					if column.type=="boolean":
						ret += " ? 1 : 0"
					ret += ");\n"
		ret += "\n\treturn vals;\n}"

		return ret

	def createStr_fromCursor(self):
		ret = "public void fromCursor(Cursor c, DB db, SQLiteDatabase database) {\n"
		ret += "\tfromCursor(c, db, database, 0);\n}\n\n"
		ret += "public void fromCursor(Cursor c, DB db, SQLiteDatabase database, int pos) {\n"
		ret += "\tc.moveToPosition(pos);\n"

		tableindex = self.findTableName(self.tableName)
		if tableindex:

			for column in self.tables[tableindex].columns:
				if self.event==column.name:
					ret += "\t" + column.name + " = DB.getEventNameFromID(c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_" + column.name.upper() + ")), database);\n"
				elif self.position==column.name:
					ret += "\t" + column.name + " = DB.getPosNameFromID(c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_" + column.name.upper() + ")), database);\n"
				elif self.config==column.name:
					ret += "\t" + column.name + " = DB.getConfigNameFromID(c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_" + column.name.upper() + ")), database);\n"
				elif self.wheel_type==column.name:
					ret += "\t" + column.name + " = DB.getWheelTypeNameFromID(c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_" + column.name.upper() + ")), database);\n"
				elif self.wheel_base==column.name:
					ret += "\t" + column.name + " = DB.getWheelBaseNameFromID(c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_" + column.name.upper() + ")), database);\n"
				elif not 'timestamp'==column.name and not 'id'==column.name and not 'invalid'==column.name:
					if column.type== "String":
						ret += "\t" + column.name + " = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_" + column.name.upper() + "));\n"
					else:
						ret += "\t" + column.name + " = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_" + column.name.upper() + "))"
						if column.type=="boolean":
							ret += " != 0"
						ret += ";\n"
		ret += "}"

		return ret

	def createStr_getProjection(self):
		ret = "public String[] getProjection() {\n"
		tableindex = self.findTableName(self.tableName)
		if tableindex:
			ret += "\tList<String> temp = new ArrayList<String>(" + str(len(self.tables[tableindex].columns)-3) + ");\n"


			for column in self.tables[tableindex].columns:
				if not 'timestamp'==column.name and not 'id'==column.name and not 'invalid'==column.name:
					ret += "\ttemp.add(COLUMN_NAME_" + column.name.upper() + ");\n"
		ret += "\tString[] projection = new String[temp.size()];\n"
		ret += "\treturn temp.toArray(projection);\n}"

		return ret

	def createStr_isString(self):
		ret = "public boolean isTextField(String column_name) {\n"

		tableindex = self.findTableName(self.tableName)
		if tableindex:
			for column in self.tables[tableindex].columns:
				if (column.type=="String" and not column.name == self.event and not column.name == self.position and not column.name == self.config and not column.name == self.wheel_type and not column.name == self.wheel_base):
					ret += "\tif (COLUMN_NAME_" + column.name.upper() + ".equalsIgnoreCase(column_name)) return true;\n\n"
		ret += "\treturn false;\n}"

		return ret

	def createStr_needsConverted(self):
		ret = "public boolean needsConvertedToText(String column_name) {\n"

		tableindex = self.findTableName(self.tableName)
		if tableindex:
			for column in self.tables[tableindex].columns:
				if (column.name == self.event or column.name == self.position or column.name == self.config or column.name == self.wheel_type or column.name == self.wheel_base):
					ret += "\tif (COLUMN_NAME_" + column.name.upper() + ".equalsIgnoreCase(column_name)) return true;\n\n"
		ret += "\treturn false;\n}"

		return ret

	def createStr_jsonToCV(self):
		ret = "public ContentValues jsonToCV(JSONObject json) throws JSONException {\n"
		ret += "\tContentValues vals = new ContentValues();\n"

		tableindex = self.findTableName(self.tableName)
		if tableindex:
			for column in self.tables[tableindex].columns:
				if (column.type=="String" and not column.name == self.event and not column.name == self.position and not column.name == self.config and not column.name == self.wheel_type and not column.name == self.wheel_base):
					ret += "\tvals.put(COLUMN_NAME_" + column.name.upper() + ", json.has(COLUMN_NAME_" + column.name.upper() + ") ? json.getString(COLUMN_NAME_" + column.name.upper() + ') : "");\n'
				elif column.name == "timestamp":
					ret += "\tvals.put(COLUMN_NAME_TIMESTAMP, DB.dateParser.format(new Date(json.getLong(COLUMN_NAME_TIMESTAMP) * 1000)));\n"
				elif column.name == "invalid":
					ret += "\tvals.put(COLUMN_NAME_INVALID, 0);\n"
				else:
					ret += "\tvals.put(COLUMN_NAME_" + column.name.upper() + ", json.has(COLUMN_NAME_" + column.name.upper() + ") ? json.getInt(COLUMN_NAME_" + column.name.upper() + ") : 0);\n"

		ret += "\treturn vals;\n}"
		return ret

	def createStr_getDisplayData(self):
		ret = "public LinkedHashMap<String,String> getDisplayData() {\n"
		ret += "\tLinkedHashMap<String,String> vals = new LinkedHashMap<String,String>();\n"

		tableindex = self.findTableName(self.tableName)
		if tableindex:
			for column in self.tables[tableindex].columns:
				if (column.type=="String"):
					ret += "\tvals.put( COLUMN_NAME_" + column.name.upper() + ", " + column.name + ");\n"
				if (column.type=="int"):
					ret += "\tvals.put( COLUMN_NAME_" + column.name.upper() + ", String.valueOf(" + column.name + "));\n"
				if (column.type=="boolean" and not column.name=="invalid"):
					ret += "\tvals.put( COLUMN_NAME_" + column.name.upper() + ", String.valueOf(" + column.name + " ? 1 : 0));\n"

		ret += "\treturn vals;\n}"
		return ret;


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
		s += SQLHelper.indent(self.createStr_fromCursor()) + "\n"
		s += "\n"
		s += SQLHelper.indent(self.createStr_getProjection()) + "\n"
		s += "\n"
		s += SQLHelper.indent(self.createStr_isString()) + "\n"
		s += "\n"
		s += SQLHelper.indent(self.createStr_needsConverted()) + "\n"
		s += "\n"
		s += SQLHelper.indent(self.createStr_jsonToCV()) + "\n"
		s += "\n"
		s += SQLHelper.indent(self.createStr_getDisplayData()) + "\n"
		s += "\n"
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
