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
