#! /bin/bash

python3 gen_scripts/SQLITEContractGen.py --packagename=org.frc836.database --classname=FRCScoutingContract --infile=FRC_Scouting_Server/scouting.sql --outfile=app/src/main/java/org/frc836/database/FRCScoutingContract.java
