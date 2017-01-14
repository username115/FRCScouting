
import csv
import re

try:
	import StringIO #for python 2.x
except:
	from io import StringIO #for python 3.x.


def indent(s_in="", lvl=1):
	ret = ""
	tabstr = ""
	for i in range(0,lvl):
		tabstr += "\t"
	for ln in s_in.split('\n'):
		ret += tabstr + ln + "\n"
	return chopnewline(ret)
def chopnewline(s_in):
	if len(s_in) == 0:
		return s_in
	ret = s_in
	while(ret[-1]=='\n'):
		ret = ret[0:-1]
	return ret
def getInStr( s, openC="\"", closeC="\"" ):
	return s
def toJavaString( s, maxLnLen=40 ):
	retStr = ""
	for ln in s.split('\n'):
		retStr += '\"'
		retStr += ln + "\\n"
		retStr += '\"'
		retStr += " +"
		retStr += '\n'
	return retStr[0:-6] + '\"' # removes the last newline and plus
	
class SqlColumn:
	def __init__(self, columnName=None, columnType=None,
						isPrimary=False, defaultVal=None,
						nullValid=False, autoIncrement=False,
						isUnsigned=False):
		self.name = str(columnName)
		self.type = str(columnType)
		self.unsigned = isUnsigned
		self.primary = isPrimary
		self.default = defaultVal
		self.notNull = not nullValid
		self.autoInc = autoIncrement
		self.data = list()
	def addRow(self, data):
		self.data.append( str(data) )
	def getRow(self, i_row):
		if i_row >= len(self.data): return None
		else: return self.data[i_row]
	def getColumns(self):
		return len(self.data)
	def toSqLiteType(self):
		#if re.search(r'int\(10\)', self.type):
		if self.name == 'id':
			self.type = "integer"
		if re.search('varchar',self.type):
			self.type = "text"
	def toJavaType(self):
		self.toSqLite()
		if re.search('text',self.type):
			self.type = 'String'
		if re.search('tinyint',self.type):
			self.type = 'boolean'
		if re.search('int',self.type):
			self.type = 'int'
		if self.name == 'position_id':
			self.type = 'String'
		if self.name == 'event_id':
			self.type = 'String'
	def toSqLite(self):
		self.toSqLiteType()
		if self.primary:
			self.unsigned = False
			self.notNull = False
	def createStr(self, creates = True ):
		self.toSqLite()
		s = ""
		if creates:
			s += self.name
		else:
			s += self.name
		if self.unsigned:
			s += " unsigned"
		s += " "+ self.type
		if self.primary:
			s += " primary key"
		if self.notNull:
			s += " NOT NULL"
		if self.autoInc:
			s += " autoincrement"
		if self.default:
			s += " DEFAULT "+ self.default
		return s

class SqlTable:
	def __init__(self, tableName=""):
		self.name = tableName
		self.columns = list()
		self.mapping = list()
		self.n_row = 0
	def findColumnName(self, colName):
		for i in range(0, len(self.columns)):
			if colName == self.columns[i].name:
				return i
		return None
	def getColumnMapping_csv(self, csvFormat):
		self.mapping = list()
		for s in csvFormat.split(','):
			s = s.strip('\t `')
			self.mapping.append( self.findColumnName(s) )
		return self.mapping
	def addColumn(self, column):
		self.columns.append(column)
	def addRow(self, data):
		i_col = 0
		for dat in next(csv.reader([data], quotechar="'", skipinitialspace=True)):
			dat = dat.strip()
			self.columns[self.mapping[i_col]].addRow(dat)
			i_col += 1
		self.n_row += 1
	def getRow(self, i_row):
		ret = list()
		for i in range(0,len(self.mapping)):
			ret.append(self.columns[i].getRow(i_row))
		return ret
	def checkNumRows(self):
		for col in self.columns:
			if len(col.data) != self.n_row:
				print( str(self.n_row) +"|"+ str(len(col.data)) )
				print("ERROR::Rows in "+ self.name +"."+ col.name +" do not match")
				for dat in col.data:
					print( str(dat) )
				return False
		return True
	def getPrimaryKey(self):
		idKey = None
		for column in self.columns:
			if column.primary:
				if idKey: print("ERROR::Multiple Primary Keys!!")
				idKey = column.name
		return str(idKey)
	def _createClassStr_var(self,name=None):
		s = "public static final String"
		if name:
			s += " COLUMN_NAME_"+ name.upper()
			s += " = \""+ name +"\""
		else:
			s += " TABLE_NAME = \""+ self.name +"\""
		return s +";"
	def createStr_Class(self, baseClass=None):
		s = "public static abstract class "
		s += (self.name).upper() +"_Entry"
		if baseClass:
			s += " implements "+ baseClass
		s += " {\n"
		s += indent( self._createClassStr_var()) +"\n"
		for column in self.columns:
			s += indent( self._createClassStr_var(column.name)) +"\n"
		return s +"}"
	def createStr_DropStr(self):
		return "DROP TABLE IF EXISTS "+ self.name +";"
	def createStr_CreateStr(self):
		s = "CREATE TABLE IF NOT EXISTS "+ self.name +" (\n"
		for column in self.columns:
			s += indent(column.createStr(creates=True)) +",\n"
		s = s[0:-2] # gets rid of the final comma
		s += "\n);"
		return s
	def _createStr_InsertStr_row(self, i_row):
		s = ""
		for column in self.columns:
			val = str(column.getRow( i_row ))
			if column.name == self.getPrimaryKey():
				s += "UNION SELECT"
			s += " '"+ val +"',"
		return s[0:-1]
	def _createStr_InsertStr_firstrow(self):
		s = ""
		for column in self.columns:
			val = str(column.getRow(0))
			if column.name == self.getPrimaryKey():
				s += "SELECT"
			s += ' '+ val +" AS "+ column.name
			s += ','
		return s[0:-1]	
	def createStr_InsertStr(self):
		s = ""
		if (self.n_row > 0) and (self.checkNumRows()):
			cnt = 1
			s += "INSERT INTO "+ self.name +'\n'
			s += self._createStr_InsertStr_firstrow() +'\n'
			for row in range(1,self.n_row):
				s += self._createStr_InsertStr_row(row) +'\n'
			return s[0:-1] +';'
		return None
		