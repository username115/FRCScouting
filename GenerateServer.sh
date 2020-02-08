#! /bin/bash

python gen_scripts/autogen_scouting.php.py -m "fact_match_data_2020" -p "scout_pit_data_2020" -o "FRC_Scouting_Server"
python gen_scripts/autogen_scouting.php.py -l -m "fact_match_data_2020" -p "scout_pit_data_2020" -o "FRC_Scouting_Server_Legacy"
