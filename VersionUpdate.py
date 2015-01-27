#! /usr/bin/python

_description = '''
This script updates the FRCScouting source code with a new version number.
    Also updates the date and has an option for updating the database version
    number.
'''

_defaultRun = '''
    python VersionUpdate.py
        --versionstring=2.2015.1
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

import argparse

class VersionUpdater:
    def __init__(self, valFileName, manifestFileName, serverFileName, dbHelperFileName, versionString=None, versionNum=None, dbVersion=None, devEmail=None, noPrompt=False):
        self.ver = versionString
        self.verNum = versionNum
        self.dbVer = dbVersion
        self.devEmail = devEmail
        self.valFileName = valFileName
        self.manifestFileName = manifestFileName
        self.serverFileName = serverFileName
        self.dbHelperFileName = dbHelperFileName
        self.noPrompt = noPrompt



def init_args():
    parser = argparse.ArgumentParser(description=_description)

    parser.add_argument('-vs', '--versionstring', dest='versionString', required=False,
        help='String format of the new version (e.g.: 2.2015.1)')
    parser.add_argument('-vn', '--versionnum', dest='versionNum', required=False,
        help='Integer representing version number')
    parser.add_argument('-db', '--dbversion', dest='dbVersion', required=False,
        help='Database Version number')
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
                         valFileName='res/values/Version.xml',
                         manifestFileName='AndroidManifest.xml',
                         serverFilename='FRC_Scouting_Server/scounting-header.php',
                         dbHelperFileName='src/org/frc836/database/ScoutingDBHelper.java',
                         noPrompt='False'
                        )

    args = parser.parse_args()
    return args

if __name__ == "__main__":
    args = init_args()
    
    print "I ran!"

