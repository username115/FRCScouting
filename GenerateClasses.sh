#! /bin/bash

python3 gen_scripts/SQLITEContractGen.py --packagename=org.frc836.database --classname=FRCScoutingContract --infile=FRC_Scouting_Server/scouting.sql --outfile=app/src/main/java/org/frc836/database/FRCScoutingContract.java

python3 gen_scripts/StatsStructGen.py --packagename=org.frc836.database --classname=MatchStatsStruct --tablename=fact_match_data_2018 --infile=FRC_Scouting_Server/scouting.sql --outfile=app/src/main/java/org/frc836/database/MatchStatsStruct.java

python3 gen_scripts/StatsStructGen.py --packagename=org.frc836.database --classname=PitStats --tablename=scout_pit_data_2018 --infile=FRC_Scouting_Server/scouting.sql --outfile=app/src/main/java/org/frc836/database/PitStats.java

