#! /bin/bash

python3 gen_scripts/autogen_scouting.php.py -m "fact_match_data_2023" -p "scout_pit_data_2023" -s "superscout_data_2023" -o "FRC_Scouting_Server"
python3 gen_scripts/autogen_scouting.php.py -l -m "fact_match_data_2023" -p "scout_pit_data_2023" -s "superscout_data_2023" -o "FRC_Scouting_Server_Legacy"

