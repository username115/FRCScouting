#! /usr/bin/python
from operator import contains

_description = '''
This script updates the FRCScouting source code with a new version number.
    Also updates the date and has an option for updating the database version
    number.
'''

_defaultRun = '''
    python VersionUpdate.py
        --versionstring=2.2015.1.1
        --versionnum=19
        --dbversion=20151
'''
__author__ = "Dan Logan"
__version__ = "1.0"
__copyright__ = '''Copyright 2015 Daniel Logan
 
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
'''

import argparse, os, re, time
from xml.etree.ElementTree import parse

class VersionUpdater:
    
    
    re_server = re.compile('\\$ver\\s*=\\s*\\\'(.*?)\\\'\\s*;', re.IGNORECASE)
    re_db = re.compile('(\\s*public\\s+static\\s+final\\s+int\\s+DATABASE_VERSION\\s*=\\s*)(\\d+)(\\s*;.*?)')
    re_manifest_name = re.compile('(\\s*android:versionName\\s*=\\s*")(.*?)(".*?\\n)')
    re_manifest_code = re.compile('(\\s*android:versionCode\\s*=\\s*")(\\d+)(".*?\\n)')
    
    def __init__(self, versionString=None, versionNum=None, dbVersion=None, devEmail=None, versionDate=None, noPrompt=False):
        self.ver = versionString
        self.verNum = versionNum
        self.dbVer = dbVersion
        self.devEmail = devEmail
        self.versionDate = versionDate
        self.noPrompt = noPrompt
        self.manifestChanged = False
        self.valuesChanged = False
        self.serverHeaderChanged = False
        self.dbHelperChanged = False
    
    def getVersionString(self, default):
        if self.ver is not None:
            return self.ver
        if self.noPrompt:
            self.ver = default
            return default
        
        input = raw_input("Please enter a new Version String [" + default + "]:")
        if len(input) < 1:
            self.ver = default
            return default
        self.ver = input
        return input
    
    def getVersionCode(self, default):
        if self.verNum is not None:
            return self.verNum
        if self.noPrompt:
            self.verNum = default
            return default
        
        input = raw_input("Please enter a new Version Code (increment this if version string is updated) [" + default + "]:")
        if len(input) < 1:
            self.verNum = default
            return default
        self.verNum = input
        return input 
    
    def getVersionDate(self, default):
        if self.versionDate is None:
            if self.noPrompt:
                self.versionDate = default
            else:
                input = raw_input("Please enter a date for the version update, or enter 'today' for today [" + default + "]:")
                if len(input) < 1:
                    self.versionDate = default
                else:
                    self.versionDate = input
        if self.versionDate.lower() == "today":
            self.versionDate = time.strftime("%Y-%m-%d")
        return self.versionDate
    
    def getDevEmail(self, default):
        if self.devEmail is not None:
            return self.devEmail
        if self.noPrompt:
            self.devEmail = default
            return default
        
        input = raw_input("Please enter the lead developer's email address [" + default + "]:")
        if len(input) < 1:
            self.devEmail = default
            return default
        self.devEmail = input
        return input
    
    def getdbVersion(self, default):
        if self.dbVer is not None:
            return self.dbVer
        if self.noPrompt:
            self.dbVer = default
            return default
        
        input = raw_input("Please enter the database version number [" + default + "]:")
        if len(input) < 1:
            self.dbVer = default
            return default
        self.dbVer = input
        return input
            
    
    def UpdateManifest(self, manifestFileName, verbose=False):
        try:
            tempfilename = "temp.versionupdatemanifest"
            if not os.path.isfile(manifestFileName):
                if verbose:
                    print "Manifest " + manifestFileName + " does not exist!"
                return
            with open(manifestFileName, 'r') as manifest, open(tempfilename, 'w') as tempfile:
                for line in manifest:
                    match = self.re_manifest_name.search(line)
                    match2 = self.re_manifest_code.search(line)
                    if match:
                        ver = match.group(2)
                        temp = self.getVersionString(ver)
                        if ver != temp:
                            tempfile.write(match.group(1) + temp + match.group(3))
                            self.manifestChanged = True
                            if verbose:
                                print "Updating manifest version string to " + temp
                        else:
                            tempfile.write(line)
                    elif match2:
                        ver = match2.group(2)
                        temp = self.getVersionCode(ver)
                        if ver != temp:
                            tempfile.write(match2.group(1) + temp + match2.group(3))
                            self.manifestChanged = True
                            if verbose:
                                print "Updating manifest version code to " + temp
                        else:
                            tempfile.write(line)
                    else:
                        tempfile.write(line)
            if self.manifestChanged:
                os.remove(manifestFileName)
                os.rename(tempfilename, manifestFileName)
                print "Saving new " + manifestFileName
            else:
                os.remove(tempfilename)
                print "No changes made to " + manifestFileName  
        except:
            if verbose:
                print "Error updating manifest: " + manifestFileName
    
    def UpdateValsFile(self, valFileName, verbose=False):
        try:
            if not os.path.isfile(valFileName):
                if verbose:
                    print "Values file " + valFileName + " does not exist!"
                return
            vals = parse(valFileName)
            strings = vals.findall("string")
            for s in strings:
                if s.attrib["name"] == "VersionID":
                    temp = self.getVersionString(s.text)
                    if temp != s.text:
                        s.text = temp
                        self.valuesChanged = True
                        if verbose:
                            print "Updating values version string to " + temp
                if s.attrib["name"] == "VersionDate":
                    temp = self.getVersionDate(s.text)
                    if temp != s.text:
                        s.text = temp
                        self.valuesChanged = True
                        if verbose:
                            print "Updating version date to " + temp
                if s.attrib["name"] == "dev_email":
                    temp = self.getDevEmail(s.text)
                    if temp != s.text:
                        s.text = temp
                        self.valuesChanged = True
                        if verbose:
                            print "Updating developer email to " + temp
                
            if self.valuesChanged:
                vals.write(valFileName)
                print "Saving new " + valFileName
            else:
                print "No changes made to " + valFileName
        except:
            if verbose:
                print "Error updating values: " + valFileName
                
    def UpdateServerHeader(self, serverFileName, verbose=False):
        try:
            tempfilename = "temp.versionupdateserver"
            if not os.path.isfile(serverFileName):
                if verbose:
                    print "Server Header " + serverFileName + " does not exist!"
                return
            
            with open(serverFileName, 'r') as header, open(tempfilename, 'w') as tempfile:
                for line in header:
                    match = self.re_server.search(line)
                    if match:
                        ver = match.group(1)
                        temp = self.getVersionString(ver)
                        if ver != temp:
                            tempfile.write("$ver='" + temp + "';\n")
                            self.serverHeaderChanged = True
                            if verbose:
                                print "Updating server version string to " + temp
                        else:
                            tempfile.write(line)
                    else:
                        tempfile.write(line)
            if self.serverHeaderChanged:
                os.remove(serverFileName)
                os.rename(tempfilename, serverFileName)
                print "Saving new " + serverFileName
            else:
                os.remove(tempfilename)
                print "No changes made to " + serverFileName
        except:
            if verbose:
                print "Error updating server header: " + serverFileName
        
    def UpdateDBVersion(self, dbHelperFileName, verbose=False):
        try:
            tempfilename = "temp.versionupdatedatabase"
            if not os.path.isfile(dbHelperFileName):
                if verbose:
                    print "DB Helper Class " + dbHelperFileName + " does not exist!"
                return
            
            with open(dbHelperFileName, 'r') as header, open(tempfilename, 'w') as tempfile:
                for line in header:
                    match = self.re_db.search(line)
                    if match:
                        ver = match.group(2)
                        temp = self.getdbVersion(ver)
                        if ver != temp:
                            tempfile.write(match.group(1) + temp + match.group(3) + "\n")
                            self.dbHelperChanged = True
                            if verbose:
                                print "Updating db version to " + temp
                        else:
                            tempfile.write(line)
                    else:
                        tempfile.write(line)
            if self.dbHelperChanged:
                os.remove(dbHelperFileName)
                os.rename(tempfilename, dbHelperFileName)
                print "Saving new " + dbHelperFileName
            else:
                os.remove(tempfilename)
                print "No changes made to " + dbHelperFileName
        except:
            if verbose:
                print "Error updating db helper: " + dbHelperFileName


def init_args():
    parser = argparse.ArgumentParser(description=_description)

    parser.add_argument('-vs', '--versionstring', dest='versionString', required=False,
        help='String format of the new version (e.g.: 2.2015.1)')
    parser.add_argument('-vn', '--versionnum', dest='versionNum', required=False,
        help='Integer representing version number')
    parser.add_argument('-db', '--dbversion', dest='dbVersion', required=False,
        help='Database Version number')
    parser.add_argument('-dt', '--date', dest='versionDate', required=False, help='Version Updated Date')
    parser.add_argument('-dev', '--devemail', dest='devEmail', required=False,
        help='Developer Email Address')
    parser.add_argument('-vf', '--valfile', dest='valFileName', required=False,
        help='Values xml file (e.q: Version.xml)')
    parser.add_argument('-mf', '--manifestfile', dest='manifestFileName', required=False,
        help='Android Manifest')
    parser.add_argument('-sf', '--serverfile', dest='serverFileName', required=False,
        help='Server header filename')
    parser.add_argument('-dbf', '--dbhelperfile', dest='dbHelperFileName', required=False,
        help='DB Helper filename for databse version number')
    parser.add_argument('-np', '--noprompt', dest='noPrompt', required=False,
        help="Don't prompt for values not provided")
    

    parser.set_defaults(versionString=None,
                         versionNum=None,
                         dbVersion=None,
                         devEmail=None,
                         versionDate=None,
                         valFileName='res/values/Version.xml',
                         manifestFileName='AndroidManifest.xml',
                         serverFilename='FRC_Scouting_Server/scouting-header.php',
                         dbHelperFileName='src/org/frc836/database/ScoutingDBHelper.java',
                         noPrompt=False
                        )

    args = parser.parse_args()
    return args

if __name__ == "__main__":
    args = init_args()
    
    Version = VersionUpdater(versionString=args.versionString, versionNum=args.versionNum, dbVersion=args.dbVersion, devEmail=args.devEmail, versionDate=args.versionDate, noPrompt=args.noPrompt)
    
    Version.UpdateManifest(args.manifestFileName, verbose=True)
    Version.UpdateValsFile(args.valFileName, verbose=True)
    Version.UpdateServerHeader(args.serverFilename, verbose=True)
    Version.UpdateDBVersion(args.dbHelperFileName, verbose=True)
    

