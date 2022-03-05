#! /bin/bash

python gen_scripts/autogen_scouting.php.py -m "fact_match_data_2022" -p "scout_pit_data_2022" -o "FRC_Scouting_Server"
python gen_scripts/autogen_scouting.php.py -l -m "fact_match_data_2022" -p "scout_pit_data_2022" -o "FRC_Scouting_Server_Legacy"
