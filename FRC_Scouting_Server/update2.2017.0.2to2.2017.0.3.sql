ALTER TABLE  `scout_pit_data_2017` ADD  `robot_gross_weight_lbs` INT( 4 ) UNSIGNED NOT NULL AFTER  `max_robot_speed_fts` ;
ALTER TABLE  `fact_pilot_data_2017` ADD  `gears_dropped` INT( 3 ) UNSIGNED NOT NULL AFTER  `gears_lifted` ;
ALTER TABLE  `fact_match_data_2017` ADD  `align_time` INT( 4 ) UNSIGNED NOT NULL AFTER  `climb_attempt` ;
ALTER TABLE  `fact_match_data_2017` ADD  `climb_time` INT( 4 ) UNSIGNED NOT NULL AFTER  `align_time` ;
