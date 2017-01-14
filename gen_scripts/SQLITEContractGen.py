#! /usr/bin/python

_description = '''
This script take in a SQL file with INSERTS and CREATES and transforms
	it into a SQLite contract in Java. Meant to be used with a phpmyadmin
	exported sql file. Defaults assume the FRC 836 file structure.
'''

_defaultRun = '''
	python SQLITEContractGen.py
		--packagename=org.frc836.database
		--classname=FRCScoutingContract
		--infile=FRC_Scouting_Server/scouting.sql
		--outfile=src/org/frc836/database/FRCScoutingContract.java
'''
__author__ = "Jonny"
__version__ = "2.0"
__copyright__ = ""

import SQLHelper
import autogeninfo

import os
import re
import argparse

# note to self. Look into the 'textwrap' class for functionality



class SqlToJava():
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
					baseClass=None, baseClassHeader=None):
		self.tables = list()
		self.packageName = packageName
		self.className = className
		self.baseClass = baseClass
		self.baseClassHeader = baseClassHeader
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
		if self.baseClassHeader:
			ret += "import "+ self.baseClassHeader +";\n"
		ret += "\n"
		ret += "public final class "+ self.className +" {\n"
		ret += "\tpublic "+ self.className +"() {}"
		return ret
	def createStr_Footer(self):
		ret = "}"
		return ret
	def createStr_Classes(self):
		s = ""
		for table in self.tables:
			s += table.createStr_Class(self.baseClass) +"\n\n"
		return s[0:-2]
	def createStr_DropStr(self):
		s = "public static final String[] SQL_DELETE_ENTRIES = {\n"
		for table in self.tables:
			tmp = "\""+ table.createStr_DropStr() +"\""
			s += SQLHelper.indent(tmp) +",\n"
		return s[0:-2] +"\n};"
	def createStr_CreateStr(self):
		s = "public static final String[] SQL_CREATE_ENTRIES = {\n"
		for table in self.tables:
			s += SQLHelper.indent( SQLHelper.toJavaString(table.createStr_CreateStr()))
			s += ",\n\n"
			tmp = table.createStr_InsertStr()
			if tmp:
				s += SQLHelper.indent( SQLHelper.toJavaString(tmp))
				s += ",\n\n"
		return s[0:-3] +"\n};"
	def createStr_JavaSqLite(self):
		s = ""
		s += self.createStr_Header() +"\n"
		s += "\n"
		s += SQLHelper.indent(self.createStr_Classes()) +"\n"
		s += "\n"
		s += SQLHelper.indent(self.createStr_CreateStr()) +"\n"
		s += "\n"
		s += SQLHelper.indent(self.createStr_DropStr()) +"\n"
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
	def writeJavaSqLiteFile(self, filename, verbose=False):
		f = open(filename,'w')
		if verbose: print("Writing to \'"+ str(f.name) +"\' in mode \'"+ str(f.mode) +"\'")
		f.write( self.createStr_JavaSqLite() )
		f.close()

	def printCreates(self):
		for table in self.tables:
			print( table.createStr_CreateStr() +"\n")
	def printInserts(self):
		for table in self.tables:
			print( table.createStr_InsertStr() +"\n")

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
	parser.add_argument('--baseclass','-bc',required=False,
		help='The class that all of the generated classes will implement')
	parser.add_argument('--baseclassHeader','-bch',required=False,
		help='The file that needs to be imported to use the baseclass')

	parser.set_defaults( infilename='FRC_Scouting_Server/scouting.sql',
							outfilename='src/org/frc836/database/FRCScoutingContract.java',
							packagename='org.frc836.database',
							classname='FRCScoutingContract',
							baseclass='BaseColumns',
							baseclassHeader='android.provider.BaseColumns'
						)

	args = parser.parse_args()
	return args

if __name__ == "__main__":
	args = init_args()
	
	SqlCreator = SqlToJava(packageName = args.packagename,
							className = args.classname,
							baseClass = args.baseclass,
							baseClassHeader = args.baseclassHeader
						)
	SqlCreator.readFile(args.infilename, verbose=True)
	SqlCreator.writeJavaSqLiteFile(args.outfilename, verbose=True)
